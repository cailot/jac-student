package hyung.jin.seo.jae.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ListBlobsOptions;

import hyung.jin.seo.jae.dto.OnlineSessionDTO;
import hyung.jin.seo.jae.model.OnlineActivity;
import hyung.jin.seo.jae.service.CycleService;
import hyung.jin.seo.jae.service.LoginActivityService;
import hyung.jin.seo.jae.service.OnlineActivityService;
import hyung.jin.seo.jae.service.OnlineSessionService;
import hyung.jin.seo.jae.utils.JaeConstants;
import hyung.jin.seo.jae.utils.JaeUtils;

@Controller
@RequestMapping("elearning")
public class OnlineController {

	private static final Logger logger = LoggerFactory.getLogger(OnlineController.class);

	@Value("${azure.storage.connection:}")
	private String azureStorageConnection;

	@Value("${azure.material.container:online}")
	private String azureMaterialContainer;

	@Value("${online.recorded.current-from-monday:P2,P3,P4}")
	private String recordedCurrentFromMondayGrades;

	@Value("${online.recorded.current-from-tuesday:P5,P6,S7,FT6,FT8,JMSS}")
	private String recordedCurrentFromTuesdayGrades;

	@Value("${online.recorded.current-from-wednesday:TT6,TT8,S8,S9,S10}")
	private String recordedCurrentFromWednesdayGrades;

	@Value("${test-day-override:}")
	private String recordedTestDayOverride;

	@Autowired
	private OnlineSessionService onlineSessionService;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private LoginActivityService loginActivityService;

	@Autowired
	private OnlineActivityService onlineActivityService;

	@GetMapping("/recorded/week/{studentId}/{year}/{week}")
	@ResponseBody
	public List<RecordedLessonView> getWeeklyRecordedLessons(@PathVariable("studentId") long studentId,
			@PathVariable("year") int year, @PathVariable("week") int week) {
		List<RecordedLessonView> lessons = new ArrayList<>();
		LocalDate overrideDate = resolveOverrideDate();
		DayOfWeek today = resolveCurrentDayOfWeek(overrideDate);
		int requestedYear = year;
		int requestedWeek = week;
		int effectiveYear = year;
		int effectiveWeek = week;
		if (overrideDate != null) {
			String overrideDateText = overrideDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			int overrideYear = cycleService.academicYear(overrideDateText);
			int overrideWeek = cycleService.academicWeeks(overrideDateText);
			if (overrideYear > 0 && overrideWeek > 0) {
				effectiveYear = overrideYear;
				effectiveWeek = overrideWeek;
			}
			logger.warn("[RECORDED-DEBUG] override applied: requestedYear={}, requestedWeek={}, effectiveYear={}, effectiveWeek={}, overrideDate={}",
					requestedYear, requestedWeek, effectiveYear, effectiveWeek, overrideDate);
		}

		int lessonSet = effectiveWeek - 1;
		int lookupYear = effectiveYear;
		boolean suppressEarlyWeekFallback = effectiveWeek <= 2;
		String gradeCode = "";
		List<String> gradeFolders = new ArrayList<>();
		List<String> blobPrefixes = new ArrayList<>();
		List<RecordedLookupTarget> lookupTargets = new ArrayList<>();
		String latestEnrolmentGrade = StringUtils.trimToEmpty(onlineSessionService.getLatestEnrolmentGrade(studentId));
		try {
			if (lessonSet < 1) { // week 1: use last year's last week baseline set
				lookupYear = effectiveYear - 1;
				int lastWeek = cycleService.lastAcademicWeek(lookupYear);
				lessonSet = lastWeek;
				String previousYearGrade = StringUtils.trimToEmpty(
						onlineSessionService.getOnlineSessionGrade(studentId, lookupYear, lastWeek));
				gradeCode = StringUtils.isNotBlank(previousYearGrade)
						? previousYearGrade
						: latestEnrolmentGrade;
				if (StringUtils.isBlank(gradeCode)) {
					logger.warn("[RECORDED-DEBUG] week 1 without grade: studentId={}, lookupYear={}, lastWeek={}",
							studentId, lookupYear, lastWeek);
					return lessons;
				}
				logger.warn("[RECORDED-DEBUG] week 1 lookup: studentId={}, lookupYear={}, lessonSet={}, gradeCode={}",
						studentId, lookupYear, lessonSet, gradeCode);
			} else {
				// Week-priority: prefer the enrolment that actually covers the current academic week
				// (week-aware), and fall back to the latest-registered enrolment only when no enrolment
				// matches the current week. This stops a future-term enrolment (e.g. S7 weeks 11-20
				// registered early) from overriding the current-week grade (e.g. TT6 weeks 1-10).
				// effectiveWeek is always >= 2 here, so getOnlineSessionGrade never hits its week-1 branch.
				String weekGrade = StringUtils.trimToEmpty(
						onlineSessionService.getOnlineSessionGrade(studentId, effectiveYear, effectiveWeek));
				gradeCode = StringUtils.isNotBlank(weekGrade)
						? weekGrade
						: latestEnrolmentGrade;
			}
			gradeFolders = resolveGradeFoldersForDay(gradeCode, today);
			if (gradeFolders.isEmpty() || StringUtils.isBlank(azureStorageConnection)
					|| StringUtils.isBlank(azureMaterialContainer)) {
				logger.warn("[RECORDED-DEBUG] skip lookup: studentId={}, year={}, week={}, lessonSet={}, lookupYear={}, gradeCode={}, gradeFolders={}, hasConnection={}, container={}",
						studentId, effectiveYear, effectiveWeek, lessonSet, lookupYear, gradeCode, gradeFolders,
						StringUtils.isNotBlank(azureStorageConnection), azureMaterialContainer);
				return lessons;
			}

			for (String gradeFolder : gradeFolders) {
				int[] sessionLookup = resolveRecordedLessonSetLookupForGrade(
						gradeFolder, lessonSet, lookupYear, today, suppressEarlyWeekFallback, !suppressEarlyWeekFallback);
				int sessionSet = sessionLookup[0];
				int sessionLookupYear = sessionLookup[1];
				if (sessionSet < 1) {
					logger.warn("[RECORDED-DEBUG] skip invalid target set: studentId={}, year={}, week={}, gradeFolder={}, sessionSet={}, sessionLookupYear={}, suppressEarlyWeekFallback={}",
							studentId, effectiveYear, effectiveWeek, gradeFolder, sessionSet, sessionLookupYear, suppressEarlyWeekFallback);
					continue;
				}
				String prefix = buildRecordedLessonBlobPrefix(gradeFolder, sessionSet);
				lookupTargets.add(new RecordedLookupTarget(gradeFolder, sessionSet, sessionLookupYear, prefix));
				blobPrefixes.add(prefix);
			}
			if (lookupTargets.isEmpty()) {
				logger.warn("[RECORDED-DEBUG] no valid lookup targets: studentId={}, year={}, week={}, lessonSet={}, gradeCode={}, gradeFolders={}",
						studentId, effectiveYear, effectiveWeek, lessonSet, gradeCode, gradeFolders);
				return lessons;
			}
			logger.warn("[RECORDED-DEBUG] lookup start: studentId={}, year={}, week={}, lessonSet={}, dayOfWeek={}, gradeCode={}, gradeFolders={}, targets={}, prefixes={}",
					studentId, effectiveYear, effectiveWeek, lessonSet, today, gradeCode, gradeFolders, lookupTargets, blobPrefixes);

			List<OnlineSessionDTO> recordedSessions = new ArrayList<>();
			Set<String> seenSessionIds = new HashSet<>();
			Set<String> queriedSessionKeys = new HashSet<>();
			for (RecordedLookupTarget target : lookupTargets) {
				String sessionKey = target.getSessionSet() + ":" + target.getSessionLookupYear();
				if (queriedSessionKeys.contains(sessionKey)) {
					continue;
				}
				queriedSessionKeys.add(sessionKey);
				List<OnlineSessionDTO> sessions = onlineSessionService.getOnlineSessionByGradeNSetNYear(
						gradeCode, target.getSessionSet(), target.getSessionLookupYear());
				for (OnlineSessionDTO session : sessions) {
					String sessionId = StringUtils.defaultString(session.getId());
					if (StringUtils.isBlank(sessionId) || seenSessionIds.contains(sessionId)) {
						continue;
					}
					seenSessionIds.add(sessionId);
					recordedSessions.add(session);
				}
			}

			BlobContainerClient containerClient = new BlobContainerClientBuilder()
					.connectionString(azureStorageConnection)
					.containerName(azureMaterialContainer)
					.buildClient();

			List<String[]> files = new ArrayList<>();
			Set<String> seenFileNames = new HashSet<>();
			List<String> fileNames = new ArrayList<>();
			for (RecordedLookupTarget target : lookupTargets) {
				ListBlobsOptions listOptions = new ListBlobsOptions().setPrefix(target.getPrefix());
				for (BlobItem blobItem : containerClient.listBlobs(listOptions, Duration.ofSeconds(10))) {
					String blobName = blobItem.getName();
					if (StringUtils.isBlank(blobName) || blobName.endsWith("/")) {
						continue;
					}
					String fileName = blobName.substring(blobName.lastIndexOf('/') + 1);
					if (!isSupportedVideo(fileName)) {
						continue;
					}
					String fileKey = fileName.toLowerCase();
					if (seenFileNames.contains(fileKey)) {
						continue;
					}
					String blobUrl = containerClient.getBlobClient(blobName).getBlobUrl();
					seenFileNames.add(fileKey);
					files.add(new String[] { fileName, blobUrl, String.valueOf(target.getSessionSet()) });
					fileNames.add(fileName);
				}
			}
			logger.warn("[RECORDED-DEBUG] lookup result: studentId={}, year={}, week={}, lessonSet={}, targets={}, prefixes={}, videoCount={}, files={}",
					studentId, effectiveYear, effectiveWeek, lessonSet, lookupTargets, blobPrefixes, files.size(), fileNames);

			files.sort(Comparator.comparing(item -> item[0], String.CASE_INSENSITIVE_ORDER));
			Set<String> usedSessionIds = new HashSet<>();
			for (int i = 0; i < files.size(); i++) {
				String fileName = files.get(i)[0];
				String sessionId = resolveRecordedSessionId(fileName, recordedSessions, usedSessionIds);
				int fileSessionSet = Integer.parseInt(files.get(i)[2]);
				lessons.add(new RecordedLessonView(sessionId, fileName, files.get(i)[1], fileSessionSet));
			}
		} catch (Exception e) {
			logger.warn("Unable to load weekly recorded lessons for studentId={}, year={}, week={}, lessonSet={}, lookupYear={}, gradeCode={}, gradeFolders={}, targets={}, prefixes={}",
					studentId, effectiveYear, effectiveWeek, lessonSet, lookupYear, gradeCode, gradeFolders, lookupTargets, blobPrefixes, e);
		}
		return lessons;
	}

	// keep start time for online watching
	@GetMapping("/startWatch/{studentId}")	
	@ResponseBody
	public ResponseEntity<String> saveStartWatch(@PathVariable("studentId") long studentId,
			@RequestParam(value = "fileName", required = false, defaultValue = "") String fileName) {	
		String safeFileName = StringUtils.left(StringUtils.trimToEmpty(fileName), 255);
		// print out student id and week with timestamp
		System.out.println(">>>>>>> Start time - Student ID: " + studentId + " File: " + safeFileName + " Time: " + System.currentTimeMillis());
		OnlineActivity activity = null;
		if (StringUtils.isNotBlank(safeFileName)) {
			activity = onlineActivityService.getOnlineActivity(studentId, safeFileName);
		}
		if(activity == null){
			// save online activity
			onlineActivityService.addOnlineActivity(studentId, null, safeFileName);
		}else{ 
			// if status = 2 (completed), then no need to keep track again
			if(activity.getStatus() == JaeConstants.STATUS_COMPLETED){
				return ResponseEntity.ok("Already completed");
			}
			// update online activity
			LocalDateTime now = LocalDateTime.now();
			activity.setStartDateTime(now);
			activity.setStatus(JaeConstants.STATUS_PROCESSING);
			activity.setEndDateTime(null);
			onlineActivityService.updateOnlineActivity(activity, activity.getId(), safeFileName);
		}
		return ResponseEntity.ok("Start log enters successfully in the server");
	}
	
	// keep end time for online watching
	@GetMapping("/endWatch/{studentId}/{id}")
	@ResponseBody
	public ResponseEntity<String> saveEndWatch(@PathVariable("studentId") long studentId,
			@PathVariable("id") long id,
			@RequestParam(value = "fileName", required = false, defaultValue = "") String fileName,
			@RequestParam(value = "completed", required = false, defaultValue = "false") boolean completed) {	
		String safeFileName = StringUtils.left(StringUtils.trimToEmpty(fileName), 255);
		// print out student id and week with timestamp
		System.out.println(">>>>>>> End time - Student ID: " + studentId + " ID: " + id + " File: " + safeFileName + " Time: " + System.currentTimeMillis());
		OnlineActivity activity = null;
		if (StringUtils.isNotBlank(safeFileName)) {
			activity = onlineActivityService.getOnlineActivity(studentId, safeFileName);
		}
		if (activity == null) {
			activity = onlineActivityService.getOnlineActivity(studentId, id);
		}
		if(activity != null && activity.getStatus() != JaeConstants.STATUS_COMPLETED){
			// update online activity
			LocalDateTime now = LocalDateTime.now();
			activity.setEndDateTime(now);
			// check if watching is completed or not
			if (completed) {
				activity.setStatus(JaeConstants.STATUS_COMPLETED);
			} else {
				// Only evaluate duration ratio when session time range exists.
				if (activity.getOnlineSession() != null) {
					long watching = JaeUtils.calculateDurationInMinutes(activity.getStartDateTime(), activity.getEndDateTime());
					long lecture = JaeUtils.calculateDurationInMinutes(activity.getOnlineSession().getStartTime(), activity.getOnlineSession().getEndTime());
					if(isCompleted(lecture, watching)){
						activity.setStatus(JaeConstants.STATUS_COMPLETED);
					}
				}
			}
			onlineActivityService.updateOnlineActivity(activity, activity.getId(), null);
		}
		return ResponseEntity.ok("End log enters successfully in the server");
	}

	// store login activity
	@GetMapping("/checkLogin/{studentId}")
	public ResponseEntity<String> storeLoginActivity(@PathVariable("studentId") long studentId) {	
		loginActivityService.saveLoginActivity(studentId);
		return ResponseEntity.ok("End log enters successfully in the server");
	}

	private String buildRecordedLessonBlobPrefix(String gradeFolder, int lessonSet) {
		return gradeFolder + "/Set" + lessonSet + "/";
	}

	private String resolveGradeLabel(String gradeCode) {
		return JaeUtils.getGradeName(StringUtils.defaultString(gradeCode).trim()).trim().toUpperCase();
	}

	private String resolveGradeFolder(String gradeCode) {
		String gradeLabel = resolveGradeLabel(gradeCode);
		if (StringUtils.isBlank(gradeLabel)) {
			return "";
		}
		// Azure folder uses grade notation directly (e.g. P2, S7, TT8, SRW4).
		return gradeLabel;
	}

	private List<String> resolveGradeFoldersForDay(String gradeCode, DayOfWeek dayOfWeek) {
		List<String> folders = new ArrayList<>();
		String gradeLabel = resolveGradeFolder(gradeCode);
		if (StringUtils.isBlank(gradeLabel)) {
			return folders;
		}
		// TT6/TT8 always show both FT and TT folders.
		if ("TT6".equals(gradeLabel)) {
			folders.add("FT6");
			folders.add("TT6");
			return folders;
		}
		if ("TT8".equals(gradeLabel)) {
			folders.add("FT8");
			folders.add("TT8");
			return folders;
		}
		folders.add(gradeLabel);
		return folders;
	}

	/**
	 * Returns [sessionSet, sessionLookupYear] based on grade and day of week (Australia/Melbourne).
	 * P2–P4: current set from Monday.
	 * P5/P6/S7/JMSS (+FT6/FT8 aliases): current set from Tuesday.
	 * Others: current set from Wednesday.
	 */
	private int[] resolveRecordedLessonSetLookupForGrade(String gradeLabel, int lessonSet, int lookupYear,
			DayOfWeek dayOfWeek, boolean suppressEarlyWeekFallback, boolean allowCrossYearFallback) {
		boolean useCurrentSet = shouldUseCurrentLessonSetForGrade(gradeLabel, dayOfWeek);
		// Early academic weeks (1-2): if this grade should look at previous set, return empty instead of fallback.
		if (suppressEarlyWeekFallback && !useCurrentSet) {
			return new int[] { 0, lookupYear };
		}
		if (useCurrentSet) {
			return new int[] { lessonSet, lookupYear };
		}
		return previousLessonSetLookup(lessonSet, lookupYear, allowCrossYearFallback);
	}

	private boolean shouldUseCurrentLessonSetForGrade(String gradeLabel, DayOfWeek dayOfWeek) {
		if (containsGrade(recordedCurrentFromMondayGrades, gradeLabel)) {
			return true;
		}
		if (containsGrade(recordedCurrentFromTuesdayGrades, gradeLabel)) {
			return dayOfWeek != DayOfWeek.MONDAY;
		}
		if (containsGrade(recordedCurrentFromWednesdayGrades, gradeLabel)) {
			return dayOfWeek.getValue() >= DayOfWeek.WEDNESDAY.getValue();
		}
		// Default for any unspecified grade: current set from Wednesday.
		return dayOfWeek.getValue() >= DayOfWeek.WEDNESDAY.getValue();
	}

	private static class RecordedLookupTarget {
		private final String gradeFolder;
		private final int sessionSet;
		private final int sessionLookupYear;
		private final String prefix;

		RecordedLookupTarget(String gradeFolder, int sessionSet, int sessionLookupYear, String prefix) {
			this.gradeFolder = gradeFolder;
			this.sessionSet = sessionSet;
			this.sessionLookupYear = sessionLookupYear;
			this.prefix = prefix;
		}

		int getSessionSet() {
			return sessionSet;
		}

		int getSessionLookupYear() {
			return sessionLookupYear;
		}

		String getPrefix() {
			return prefix;
		}

		@Override
		public String toString() {
			return gradeFolder + ":Set" + sessionSet + ":" + sessionLookupYear;
		}
	}

	private boolean containsGrade(String csv, String grade) {
		if (StringUtils.isBlank(csv) || StringUtils.isBlank(grade)) {
			return false;
		}
		for (String token : csv.split(",")) {
			if (grade.equalsIgnoreCase(StringUtils.trim(token))) {
				return true;
			}
		}
		return false;
	}

	private DayOfWeek resolveCurrentDayOfWeek(LocalDate overrideDate) {
		if (overrideDate != null) {
			return overrideDate.getDayOfWeek();
		}
		if (StringUtils.isBlank(recordedTestDayOverride)) {
			return LocalDate.now().getDayOfWeek();
		}
		String override = StringUtils.trim(recordedTestDayOverride);
		try {
			return DayOfWeek.valueOf(override.toUpperCase());
		} catch (Exception ex) {
			logger.warn("Invalid test-day-override='{}'. Falling back to system date.", override);
			return LocalDate.now().getDayOfWeek();
		}
	}

	private LocalDate resolveOverrideDate() {
		if (StringUtils.isBlank(recordedTestDayOverride)) {
			return null;
		}
		String override = StringUtils.trim(recordedTestDayOverride);
		try {
			return LocalDate.parse(override);
		} catch (Exception ignored) {
			return null;
		}
	}

	private int[] previousLessonSetLookup(int lessonSet, int lookupYear, boolean allowCrossYearFallback) {
		if (lessonSet > 1) {
			return new int[] { lessonSet - 1, lookupYear };
		}
		if (!allowCrossYearFallback) {
			return new int[] { 0, lookupYear };
		}
		int previousYear = lookupYear - 1;
		return new int[] { cycleService.lastAcademicWeek(previousYear), previousYear };
	}

	private boolean isSupportedVideo(String fileName) {
		String lower = StringUtils.defaultString(fileName).toLowerCase();
		return lower.endsWith(".mp4")
				|| lower.endsWith(".m4v")
				|| lower.endsWith(".mov")
				|| lower.endsWith(".webm")
				|| lower.endsWith(".avi");
	}

	private String resolveRecordedSessionId(String fileName, List<OnlineSessionDTO> sessions, Set<String> usedSessionIds) {
		if (sessions == null || sessions.isEmpty()) {
			return "0";
		}
		String normalizedFile = normalizeLessonKey(fileName);
		for (OnlineSessionDTO session : sessions) {
			String sessionId = StringUtils.defaultString(session.getId());
			if (StringUtils.isBlank(sessionId) || usedSessionIds.contains(sessionId)) {
				continue;
			}
			String normalizedTitle = normalizeLessonKey(session.getTitle());
			if (StringUtils.isBlank(normalizedTitle)) {
				continue;
			}
			if (normalizedTitle.contains(normalizedFile) || normalizedFile.contains(normalizedTitle)) {
				usedSessionIds.add(sessionId);
				return sessionId;
			}
		}
		for (OnlineSessionDTO session : sessions) {
			String sessionId = StringUtils.defaultString(session.getId());
			if (StringUtils.isBlank(sessionId) || usedSessionIds.contains(sessionId)) {
				continue;
			}
			usedSessionIds.add(sessionId);
			return sessionId;
		}
		return "0";
	}

	private String normalizeLessonKey(String value) {
		String fileBase = StringUtils.defaultString(value);
		int dot = fileBase.lastIndexOf('.');
		if (dot > 0) {
			fileBase = fileBase.substring(0, dot);
		}
		return fileBase.toLowerCase().replaceAll("[^a-z0-9]+", "");
	}

	public static class RecordedLessonView {
		private final String id;
		private final String title;
		private final String address;
		private final int set;

		public RecordedLessonView(String id, String title, String address, int set) {
			this.id = id;
			this.title = title;
			this.address = address;
			this.set = set;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getAddress() {
			return address;
		}

		public int getSet() {
			return set;
		}
	}

	// check if watching is completed or not
	private boolean isCompleted(long lecture, long watching){
		// if watching time is more than 80% of lecture time, then it is completed
		if(watching > lecture * 0.8){
			return true;
		}else{
			return false;
		}
	}

}

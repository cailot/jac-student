<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<sec:authorize access="isAuthenticated()">
<sec:authentication var="role" property='principal.authorities'/>
<sec:authentication var="id" property="principal.username"/>
<sec:authentication var="firstName" property="principal.firstName"/>
<sec:authentication var="lastName" property="principal.lastName"/>
	<script>
		var role = '${role}';
		var numericGrade = role.replace(/[\[\]]/g, ''); // replace '[' & ']' with an empty string
		var enrolGrade = numericGrade;
		var studentId = '${id}';
		var firstName = '${firstName}';
		var lastName = '${lastName}';
		var academicYear;
    	var academicWeek;
		var watchingId = 0;
		var watchingFileName = '';
		var watchedSeconds = 0;
		var lastPlaybackPos = 0;

	</script>
</sec:authorize>

<script>
function normalizeGradeValue(gradeValue) {
	if (gradeValue === null || gradeValue === undefined) {
		return '';
	}
	return String(gradeValue).replace(/[\[\]\s]/g, '');
}

function popupGradeLabel(gradeValue) {
	var normalizedGrade = normalizeGradeValue(gradeValue);
	if (!normalizedGrade || isNaN(+normalizedGrade)) {
		return displayGrade();
	}
	return gradeName(normalizedGrade);
}

function updatePopupGradeTitles(gradeValue) {
	var popupGrade = popupGradeLabel(gradeValue);
	$('#recordGrade').text(popupGrade);
}

$(function() {
	// to get the academic year and week
	$.ajax({
		url : '${pageContext.request.contextPath}/class/academy',
		method: "GET",
		success: function(response) {
			// save the response into the variable
			academicYear = response[0];
			academicWeek = parseInt(response[1], 10);

			console.log('NumericGrade ---> ' + numericGrade);

			// Weekly lessons always use previous set (current academic week - 1)
			getRecordedWeeklyLessons(academicWeek);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Error : ' + errorThrown);
		}
	});
	// initialise state list when loading
	listState('#editState');
    listBranch('#editBranch');
	listGrade('#editGrade');
	updatePopupGradeTitles(numericGrade);

	$.ajax({
		url: '${pageContext.request.contextPath}/connected/enrolGrade/' + studentId,
		type: 'GET',
		success: function(response) {
			enrolGrade = response;
			updatePopupGradeTitles(enrolGrade);
		},
		error: function(xhr, status, error) {
			console.error('Error getting enrolment grade:', error);
			updatePopupGradeTitles(numericGrade);
		}
	});

	const lessonVideo = document.getElementById('lessonVideo');
	if (lessonVideo) {
		lessonVideo.addEventListener('ended', function() {
			endVideoTimer(true);
		});
		// Accumulate genuine playback time only. timeupdate fires several times per second,
		// so normal playback produces small positive deltas; large jumps (skip/seek) and
		// rewinds are ignored so that skipping ahead does not inflate the watched ratio.
		lessonVideo.addEventListener('timeupdate', function() {
			var pos = lessonVideo.currentTime;
			var delta = pos - lastPlaybackPos;
			if (delta > 0 && delta < 1.5) {
				watchedSeconds += delta;
			}
			lastPlaybackPos = pos;
		});
	}

	$('#videoSkipControls').on('click', '.video-skip-btn', function() {
		var seconds = parseInt($(this).data('skip'), 10);
		if (!isNaN(seconds)) {
			skipLessonVideo(seconds);
		}
	});

	window.addEventListener('beforeunload', function() {
		if (!watchingId) {
			return;
		}
		var endId = watchingId;
		var endFileName = watchingFileName;
		var completedFlag = hasWatchedEnough() ? 'true' : 'false';
		watchingId = 0;
		watchingFileName = '';
		try {
			fetch('${pageContext.request.contextPath}/elearning/endWatch/' + studentId + '/' + endId + '?completed=' + completedFlag + '&fileName=' + encodeURIComponent(endFileName), {
				method: 'GET',
				keepalive: true
			});
		} catch (error) {
			console.log('Error sending endWatch before unload:', error);
		}
	});

});

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Retrieve Student Info
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function retrieveStudentInfo() {
	$.ajax({
		url : '${pageContext.request.contextPath}/online/get/' + studentId,
		type : 'GET',
		success : function(student) {
			$('#editStudentModal').modal('show');
			// Update display info
			//  console.log(student);
			$("#editId").val(student.id);
			$("#editFirstName").val(student.firstName);
			$("#editLastName").val(student.lastName);
			// $("#editEmail1").val(student.email1);
			// $("#editEmail2").val(student.email2);
			// $("#editRelation1").val(student.relation1);
			// $("#editRelation2").val(student.relation2);
			// $("#editAddress").val(student.address);
			// $("#editContact1").val(student.contactNo1);
			// $("#editContact2").val(student.contactNo2);
			$("#editState").val(student.state);
			$("#editBranch").val(student.branch);
			$("#editGrade").val(student.grade);
			// $("#editGender").val(student.gender);
            // var regDate = formatDate(student.registerDate);
			// $("#editRegisterDate").val(regDate);
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Update password
////////////////////////////////////////////////////////////////////////////////////////////////////////
function updatePassword() {
	var id = $("#editId").val();
	var newPwd = $("#newPassword").val();
	var confirmPwd = $("#confirmPassword").val();
	//warn if Id is empty
	if (id == '') {
		$('#warning-alert .modal-body').text('Please search student record before updating');
		$('#warning-alert').modal('toggle');
		return;
	}
	// warn if newPwd or confirmPwd is empty
	if (newPwd == '' || confirmPwd == '') {
		$('#warning-alert .modal-body').text('Please enter new password and confirm password');
		$('#warning-alert').modal('toggle');
		return;
	}
	//warn if newPwd is not same as confirmPwd
	if(newPwd != confirmPwd){
		$('#warning-alert .modal-body').text('New password and confirm password are not the same');
		$('#warning-alert').modal('toggle');
		return;
	}
	// send query to controller
	$.ajax({
		url : '${pageContext.request.contextPath}/online/updatePassword/' + id + '/' + confirmPwd,
		type : 'PUT',
		success : function(data) {
			$('#success-alert .modal-body').html('<b>Password</b> is now updated');
			$('#success-alert').modal('toggle');
			// clear fields
			clearPassword();
			// close modal
			$('#editStudentModal').modal('toggle');
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
		
	}); 
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Retrieve Recorded Session Info
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function escapeHtml(value) {
	return $('<div>').text(value || '').html();
}

function stripFileExtension(fileName) {
	if (!fileName) {
		return '';
	}
	return String(fileName).replace(/\.(mp4|m4v|mov|webm|avi)$/i, '');
}

function appendEmptyRecordedBlock(week) {
	var label = formatWeeklyLessonLabel(week, '<i>No video file found.</i>');
	$('#recordBlocks').append(
		'<div class="recordLesson alert alert-secondary jae-border-warning">' +
			'<p class="m-1">' + label + '</p>' +
		'</div>'
	);
}

function setRecordBlocksVisible(visible) {
	$('#recordBlocks').toggle(visible === true);
}

function formatWeeklyLessonLabel(setNumber, titleHtml) {
	if (setNumber != null && setNumber > 0) {
		return 'Weekly Lesson <strong>Set</strong> ' + setNumber + ' - ' + titleHtml;
	}
	return 'Weekly Lesson - ' + titleHtml;
}

function getLessonSet(academicWeek) {
	return parseInt(academicWeek, 10) - 1;
}

function resolveDisplaySet(academicWeek, records) {
	var lessonSet = getLessonSet(academicWeek);
	if (Array.isArray(records) && records.length > 0 && records[0].set) {
		return parseInt(records[0].set, 10);
	}
	return lessonSet > 0 ? lessonSet : null;
}

function getRecordedWeeklyLessons(academicWeek) {
	if (!academicWeek || academicWeek < 1) {
		return;
	}
	$('#recordBlocks').empty();
	setRecordBlocksVisible(false);
	$.ajax({
		url: '${pageContext.request.contextPath}/elearning/recorded/week/' + studentId + '/' + academicYear + '/' + academicWeek,
		type: 'GET',
		success: function(data) {
			var displaySet = resolveDisplaySet(academicWeek, data);
			if (!Array.isArray(data) || data.length === 0) {
				// For all grades, render nothing when no recorded lesson exists.
				$('#recordBlocks').empty();
				setRecordBlocksVisible(false);
				return;
			}

			setRecordBlocksVisible(true);
			$.each(data, function(index, record) {
				var videoUrl = record.address || '';
				var lessonTitle = stripFileExtension(record.title) || ('Weekly lesson ' + (index + 1));
				var safeTitle = escapeHtml(lessonTitle);
				var recordId = record.id || String(index + 1);
				var itemSet = record.set ? parseInt(record.set, 10) : displaySet;
				var blockId = 'recordLessonBlob_' + index;
				var sessionElement = $('<div id="' + blockId + '" class="recordLesson alert alert-primary jae-border-warning" style="pointer-events: auto; cursor: pointer;"></div>');
				sessionElement.attr('data-video-url', videoUrl);
				sessionElement.attr('data-record-title', lessonTitle);
				sessionElement.attr('data-record-file-name', record.title || '');
				sessionElement.attr('data-record-id', recordId);
				sessionElement.attr('data-lesson-set', itemSet != null ? itemSet : '');
				sessionElement.append(
					'<p id="recordBlock_' + recordId + '" class="m-1">' +
						formatWeeklyLessonLabel(itemSet, safeTitle) +
						'<i id="recordPlayIcon_' + recordId + '" class="bi bi-caret-right-square text-primary ml-2" style="font-size: 1.25rem;" title="Play Video"></i>' +
						'<span id="recordLessonId_' + recordId + '" style="visibility: hidden;">' + recordId + '</span>' +
					'</p>'
				);
				$('#recordBlocks').append(sessionElement);
				sessionElement.on('click', handleRecordLessonClick);
			});
		},
		error: function(xhr, status, error) {
			console.log('Error : ' + error);
			$('#recordBlocks').empty();
			setRecordBlocksVisible(false);
		}
	});
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display grade
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayGrade() {
	var numericPart = role.replace(/[\[\]]/g, ''); // replace '[' & ']' with an empty string
	var grade = gradeName(numericPart);
	return grade;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Clear password fields
////////////////////////////////////////////////////////////////////////////////////////////////////////
function clearPassword() {
	$("#newPassword").val('');
	$("#confirmPassword").val('');
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Click on Live section
////////////////////////////////////////////////////////////////////////////////////////////////////////
function handleRecordLessonClick(event) {
	const recordLesson = event.currentTarget;
	// set the videoUrl to the hidden input field
	document.getElementById("recordVideoUrl").value = recordLesson.getAttribute('data-video-url');
	document.getElementById("recordVideoId").value = recordLesson.getAttribute('data-record-id') || '0';
	document.getElementById("recordVideoFileName").value = recordLesson.getAttribute('data-record-file-name') || '';
	const titleValue = recordLesson.getAttribute('data-record-title') || '';
	const lessonSetValue = recordLesson.getAttribute('data-lesson-set') || '';
	document.getElementById("recordLessonDay").textContent = titleValue;
	document.getElementById("recordLessonSet").textContent = lessonSetValue;
	// Show confirmation dialog before calling handleLessonClick
	$('#recordLessonWarning').modal('show');
}

function startVideoTimer(fileName) {
	const safeFileName = String(fileName || '').trim();
	const sessionId = Number(document.getElementById('recordVideoId').value || 0);
	watchingId = sessionId;
	watchingFileName = safeFileName;
	watchedSeconds = 0;
	lastPlaybackPos = 0;
	$.ajax({
		url: '${pageContext.request.contextPath}/elearning/startWatch/' + studentId + '?fileName=' + encodeURIComponent(safeFileName),
		type: 'GET',
		error: function(xhr, status, error) {
			console.error('Error startWatch:', error);
		}
	});
}

// Returns true when the student has watched at least 80% of the video duration.
// Mirrors the backend duration-ratio rule so partial-but-sufficient watches are
// marked completed even when the student leaves before the natural 'ended' event.
function hasWatchedEnough() {
	var video = document.getElementById('lessonVideo');
	if (!video) {
		return false;
	}
	var duration = video.duration;
	if (!isFinite(duration) || duration <= 0) {
		return false;
	}
	return (watchedSeconds / duration) >= 0.8;
}

function endVideoTimer(completed) {
	if (!watchingId) {
		return;
	}
	var endId = watchingId;
	var endFileName = watchingFileName;
	var finalCompleted = (completed === true) || hasWatchedEnough();
	watchingId = 0;
	watchingFileName = '';
	var completedFlag = finalCompleted ? 'true' : 'false';
	$.ajax({
		url: '${pageContext.request.contextPath}/elearning/endWatch/' + studentId + '/' + endId + '?completed=' + completedFlag + '&fileName=' + encodeURIComponent(endFileName),
		type: 'GET',
		error: function(xhr, status, error) {
			console.error('Error endWatch:', error);
		}
	});
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Play video
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMedia(videoUrl) {
    endVideoTimer(false);

    // Get elements
    const video = document.getElementById('lessonVideo');
    const background = document.getElementById('lectureBackground');
    const zoomFrame = document.getElementById('zoomFrame');
    
    // get the videoUrl from the hidden input field
    const videoAddress = document.getElementById(videoUrl).value;
    console.log("Video Address:", videoAddress);
    
    // Hide background first
    background.style.display = 'none';
    
    // Check if it's a Zoom URL
    if (videoAddress.includes('zoom.us')) {
        console.log("Loading Zoom URL in iframe");
        // Hide video, show zoom frame
        video.style.display = 'none';
        zoomFrame.style.display = 'block';
        setVideoSkipControlsVisible(false);
        zoomFrame.src = videoAddress;
        
        // For Zoom, we can't autostart the meeting, but we can try to focus the iframe
        setTimeout(function() {
            zoomFrame.focus();
        }, 1000);
        
    } else if (videoAddress && videoAddress.trim() !== '') {
        // For regular video files
        console.log("Loading video file");
        // Hide zoom frame, show video
        zoomFrame.style.display = 'none';
        video.style.display = 'block';
        video.src = videoAddress;
        setVideoSkipControlsVisible(true);
        
        // Try to autoplay with sound
        video.load();
        startVideoTimer(document.getElementById('recordVideoFileName').value);
        video.play().then(function() {
            console.log("Video autoplay with sound successful");
        }).catch(function(error) {
            console.log("Autoplay with sound failed, this is normal due to browser policies:", error);
            // The video will still be ready to play when user clicks
        });
    }

    // Hide the media warning modal
    $('#recordLessonWarning').modal('hide');
}

function skipLessonVideo(seconds) {
	const video = document.getElementById('lessonVideo');
	if (!video || video.style.display === 'none' || !video.src) {
		return;
	}
	var duration = video.duration;
	if (!isFinite(duration) || duration <= 0) {
		return;
	}
	var targetTime = video.currentTime + seconds;
	if (targetTime < 0) {
		targetTime = 0;
	} else if (targetTime > duration) {
		targetTime = duration;
	}
	video.currentTime = targetTime;
}

function setVideoSkipControlsVisible(visible) {
	var skipControls = document.getElementById('videoSkipControls');
	if (skipControls) {
		skipControls.style.display = visible ? 'flex' : 'none';
	}
}

// Add these event listeners to prevent keyboard shortcuts and right-click
document.addEventListener('keydown', function(e) {
    if (e.key === 's' && (e.ctrlKey || e.metaKey)) {
        e.preventDefault();
        return;
    }
    var video = document.getElementById('lessonVideo');
    if (!video || video.style.display === 'none' || !video.src) {
        return;
    }
    if (e.key === 'ArrowLeft') {
        e.preventDefault();
        skipLessonVideo(-10);
    } else if (e.key === 'ArrowRight') {
        e.preventDefault();
        skipLessonVideo(10);
    }
});

document.addEventListener('contextmenu', function(e) {
    e.preventDefault();
});

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	Login Activity & Link to Connected Class
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function accessConnectedClass() {
	endVideoTimer(false);
	// login activity store
	$.ajax({
		url: '${pageContext.request.contextPath}/elearning/checkLogin/' + studentId, 
		type: 'GET'
	});	
    const url = '${pageContext.request.contextPath}/connected/lesson';
    window.location.href = url;
}

</script>    

<style>

span#studentName:hover {
	cursor: pointer;
}

.custom-icon {
font-size: 2rem; /* Adjust the size as needed */
}

/* Style for an additional container element */
.iframe-container {
	margin: 5px; /* Adjust the margin as needed */
}

.video-container {
    position: relative;
    width: 1000px;
    height: 400px;
    overflow: hidden;
}

.video-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;
}

.video-skip-controls {
    
	









	/*
	display: none;
    */
	display: none !important;









	

	justify-content: center;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 8px;
    width: 1000px;
    max-width: 100%;
}

.video-skip-btn {
    background: rgba(255, 255, 255, 0.92);
    color: #1a237e;
    border: 1px solid rgba(26, 35, 126, 0.25);
    border-radius: 20px;
    padding: 6px 12px;
    font-size: 0.8rem;
    font-weight: 600;
    min-width: 58px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    cursor: pointer;
    transition: background 0.2s ease, transform 0.2s ease;
}

.video-skip-btn:hover {
    background: #fff;
    transform: translateY(-1px);
}

/* Style for the iframe */
#lessonVideo {
    width: 100%;
    height: 100%;
    border: none;
    background: url('${pageContext.request.contextPath}/image/lecture.jpg') center center no-repeat;
    background-size: 60%;
}

/* Hide download button and context menu */
::-webkit-media-controls-download-button {
    display: none !important;
}

::-webkit-media-controls-enclosure {
    overflow: hidden !important;
}

::-webkit-media-controls-panel {
    width: calc(100% + 30px);
}

/* Elevated Card / Box Look */
.card, .alert, .modal-content {
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12); /* deeper, smoother shadow */
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}
 
/* Subtle lift on hover */
.card:hover, .alert:hover {
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.onlineLesson:hover, .recordLesson:hover {
  transform: scale(1.01);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.2);
  transition: all 0.3s ease;
  cursor: pointer;
}

.btn-connected-class {
  background: linear-gradient(to right, #3d5afe, #536dfe);
  color: white;
  font-weight: 600;
  border: none;
  padding: 12px 28px;
  font-size: 1rem;
  border-radius: 50px;
  box-shadow: 0 6px 15px rgba(61, 90, 254, 0.3);
  transition: all 0.3s ease;
}


.btn-connected-class:hover {
  background: linear-gradient(to right, #ffeb3b, #fff176); /* yellow gradient */
  color: #2c3e50;
  box-shadow: 0 8px 20px rgba(255, 235, 59, 0.4);
  transform: translateY(-2px);
}

.record-lesson-modal {
  border: 2px solid #ffc107;
  overflow: hidden;
}

.record-lesson-modal .modal-header {
  border-bottom: none;
  padding: 1rem 1.25rem 0.75rem;
}

.record-lesson-modal__subtitle {
  display: block;
  font-size: 0.8rem;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: rgba(0, 0, 0, 0.55);
  margin-bottom: 0.25rem;
}

.record-lesson-modal__icon-wrap {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 88px;
  height: 88px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e3f2fd 0%, #fff9c4 100%);
  box-shadow: 0 6px 18px rgba(26, 35, 126, 0.12);
  margin-bottom: 0.75rem;
}

.record-lesson-modal__icon-wrap img {
  width: 56px;
  height: 56px;
  border-radius: 8px;
}

.record-lesson-summary {
  background: linear-gradient(to right, #f8fbff 0%, #fffef5 100%);
  border-left: 4px solid #3d5afe;
  border-radius: 12px;
  padding: 1rem 1.1rem;
  margin-bottom: 1rem;
}

.record-lesson-summary__label {
  display: block;
  font-size: 0.82rem;
  color: #6c757d;
  margin-bottom: 0.15rem;
}

.record-lesson-summary__title {
  font-size: 1.15rem;
  font-weight: 700;
  color: #1a237e;
  margin-bottom: 0.45rem;
  word-break: break-word;
}

.record-lesson-set-badge {
  display: inline-block;
  background: #3d5afe;
  color: #fff;
  font-size: 0.78rem;
  font-weight: 600;
  padding: 0.25rem 0.65rem;
  border-radius: 999px;
}

.record-lesson-guidelines {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 12px;
  padding: 0.9rem 1rem;
}

.record-lesson-guidelines__title {
  font-weight: 700;
  color: #343a40;
  margin-bottom: 0.55rem;
}

.record-lesson-guidelines ul {
  margin-bottom: 0;
  padding-left: 1.15rem;
  color: #495057;
  font-size: 0.92rem;
}

.record-lesson-guidelines li + li {
  margin-top: 0.35rem;
}

.record-lesson-notice {
  margin-top: 0.9rem;
  padding: 0.65rem 0.75rem;
  background: #fff8e1;
  border-radius: 8px;
  border: 1px dashed #ffc107;
  font-size: 0.86rem;
  color: #5d4037;
}

.record-lesson-modal .modal-footer {
  border-top: 1px solid #eee;
  background: #fafafa;
}

body {
  background: url('${pageContext.request.contextPath}/image/online-background.jpg') no-repeat center center fixed;
  background-size: cover;
  min-height: 100vh;
  margin: 0;
  padding: 0;
}

/* Add these styles to hide download button and other controls */
video::-webkit-media-controls-enclosure {
    overflow:hidden;
}

video::-webkit-media-controls-panel {
    width: calc(100% + 30px);
}

video::-webkit-media-controls-download-button {
    display: none !important;
}

video::-internal-media-controls-download-button {
    display: none !important;
}

#lessonVideo {
    width: 100%;
    height: 100%;
    border: none;
}

#lessonVideo, #lectureBackground, #zoomFrame {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border: none;
}

/* Hide download button */
video::-webkit-media-controls-download-button {
    display: none !important;
}

video::-webkit-media-controls-enclosure {
    overflow: hidden !important;
}

video::-webkit-media-controls-panel {
    width: calc(100% + 30px);
}

@media (max-width: 768px) {
    .online-header {
        flex-direction: column;
        align-items: center;
        text-align: center;
        gap: 6px;
        padding: 0.5rem !important;
    }
    .online-header .content-container {
        margin-left: 0 !important;
    }
    .online-header__center {
        padding: 0 !important;
    }
    .online-header__center img {
        width: 40px !important;
    }
    .online-header__center .h2 {
        display: block;
        font-size: 1.2rem;
        margin-top: 4px;
    }
    .online-header__right table {
        margin: 0 auto;
    }
}
</style>

<div class="container-fluid pl-0 pr-0">
	<sec:authorize access="isAuthenticated()">
		<div class="card-body jae-background-color w-100 d-flex align-items-center justify-content-between online-header" style="margin: 0; padding: 0.2rem;">	
			<div class="content-container">
				<span class="card-text text-warning font-weight-bold font-italic h5" style="margin-left: 25px;" id="studentName" onclick="clearPassword();retrieveStudentInfo()">${firstName} ${lastName}</span>
				<span style="color: white;">&nbsp;&nbsp;(</span>
				<span class="card-text" id="studentGrade" name="studentGrade" style="color: white;"></span>
				<span style="color: white;">)  </span>
				<script>document.getElementById("studentGrade").textContent = displayGrade();</script>
			</div>
			<div class="card-body jae-background-color text-center online-header__center" style="padding: 1rem;">
				<img src="${pageContext.request.contextPath}/image/logo.png" style="filter: brightness(0) invert(1);width:65px;" >
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text-light align-middle h2">JAC-eLearning Student Lecture</span>           
			</div>
			<div class="online-header__right">
				<div style="display: flex; align-items: center; margin-top: 5px;">
					<table>
						<tr>
							<td>
								<span class="text-white">
									<c:set var="now" value="<%= new java.util.Date() %>" />
									Logged at <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm" />
								</span>
							</td>
							<td>
								<form:form action="${pageContext.request.contextPath}/online/logout" method="POST" id="logout" style="margin-bottom: 0px;">
									<button class="btn mr-1"><i class="bi bi-power custom-icon text-warning" style="font-size: 1.75rem;" title="Log Out"></i></button>
								</form:form>				
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</sec:authorize>
	
	<div class="container py-2">
        <div class="card shadow-sm rounded-4 mt-2">
            <div class="card-body text-center" style="padding-bottom: 0px;">
                <h3 class="card-title text-primary mt-2">Welcome to JAC-eLearning!</h3>
                <p class="text-muted">Your personal hub for recorded weekly lessons at James An College.</p>
                <div class="my-1">
					<div class="iframe-container" style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
						<div class="video-container">
							<div id="lectureBackground" style="width: 100%; height: 100%; background: url('${pageContext.request.contextPath}/image/lecture.jpg') center center no-repeat; background-size: 60%;"></div>
							<video id="lessonVideo" 
									style="width: 100%; height: 100%; border: none; display: none;"
									controls
									controlsList="nodownload"
									disablePictureInPicture
									oncontextmenu="return false"
									autoplay>
							</video>
							<iframe id="zoomFrame" 
									style="width: 100%; height: 100%; border: none; display: none;"
									allow="camera; microphone; fullscreen; autoplay">
							</iframe>
						</div>
						<div id="videoSkipControls" class="video-skip-controls">
							<button type="button" class="video-skip-btn" data-skip="-60" title="Rewind 60 seconds">-60s</button>
							<button type="button" class="video-skip-btn" data-skip="-30" title="Rewind 30 seconds">-30s</button>
							<button type="button" class="video-skip-btn" data-skip="-10" title="Rewind 10 seconds">-10s</button>
							<button type="button" class="video-skip-btn" data-skip="10" title="Forward 10 seconds">+10s</button>
							<button type="button" class="video-skip-btn" data-skip="30" title="Forward 30 seconds">+30s</button>
							<button type="button" class="video-skip-btn" data-skip="60" title="Forward 60 seconds">+60s</button>
						</div>
					</div>
					<div style="color: #ff0000; font-size: 0.9em; margin-bottom: 10px; font-style: italic;">
						<strong>LEGAL NOTICE:</strong> All content, materials, and intellectual property on this platform are owned by James An College Victoria.<br> 
						Unauthorized access, reproduction, or distribution is strictly prohibited and may result in legal action.
					</div>		
                </div>		
            </div>
        </div>
    </div>

	<div class="parent-container" style="display: flex; justify-content: center;">
		<div class="card-body" style="max-width: 80%; margin: auto;">
			<div id="recordBlocks">
			</div>
			<div class="text-right">
 				<button class="btn btn-connected-class" onclick="accessConnectedClass()">
					<i class="bi bi-box-arrow-in-right me-2"></i>&nbsp;&nbsp;&nbsp;Access To Connected Class
				</button>
			</div>
		</div>
	</div>
	
	<h6 class="text-center" style="position: fixed; bottom: 0; width: 100%;">
		2015 - <%=new java.util.Date().getYear() + 1900%>&copy;&nbsp; All rights reserved.&nbsp;&nbsp;
		<div class="copyright-font-color">James An College Victoria <span title="Application version">v${appVersion}</span></div>
	</h6>
		

</div>

<!-- Record Video Warning Modal -->
<div class="modal fade" id="recordLessonWarning" tabindex="-1" role="dialog" aria-labelledby="recordLessonModalTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content record-lesson-modal">
            <div class="modal-header bg-warning">
				<div class="w-100 text-center">
					<span class="record-lesson-modal__subtitle">JAC eLearning</span>
					<p id="recordLessonModalTitle" class="mb-0" style="font-size:18px;">
						<i class="bi bi-mortarboard-fill mr-1"></i>
						<strong>James An College Year <span class="text-danger" id="recordGrade" name="recordGrade"></span></strong>
					</p>
				</div>
			</div>
            <div class="modal-body px-4 pb-3">
                <div class="text-center">
					<div class="record-lesson-modal__icon-wrap">
                    	<img src="${pageContext.request.contextPath}/image/recorded.png" alt="Weekly lesson">
					</div>
                </div>

				<div class="record-lesson-summary">
					<span class="record-lesson-summary__label">You are about to watch</span>
					<div class="record-lesson-summary__title" id="recordLessonDay" name="recordLessonDay"></div>
					<span class="record-lesson-set-badge">Set <span id="recordLessonSet"></span></span>
				</div>

				<div class="record-lesson-guidelines">
					<div class="record-lesson-guidelines__title">
						<i class="bi bi-info-circle text-primary mr-1"></i>Before you start
					</div>
					<ul>
						<li>This weekly lesson matches your current academic schedule.</li>
						<li>Watch the full video to mark your lesson as completed.</li>
						
						
						
						<!--
						<li>Use the skip buttons below the player to move 10, 30, or 60 seconds.</li>
						-->
						
						
						<li>This content is for enrolled James An College students only.</li>
						<li>Recording, downloading, or sharing is not permitted.</li>
					</ul>
				</div>

				<div class="record-lesson-notice">
					<i class="bi bi-shield-check mr-1"></i>
					By selecting <strong>I agree &amp; Play</strong>, you confirm that you will use this material responsibly.
				</div>
            </div>
            <input type="hidden" id="recordVideoUrl" name="recordVideoUrl" value="">
            <input type="hidden" id="recordVideoId" name="recordVideoId" value="">
			<input type="hidden" id="recordVideoFileName" name="recordVideoFileName" value="">
            <div class="modal-footer">
				<button type="button" class="btn btn-primary" id="agreeMediaWarning" onclick="displayMedia('recordVideoUrl')">
					<i class="bi bi-play-circle mr-1"></i>I agree &amp; Play
				</button>
            	<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

 <!-- Edit Form Dialogue -->
 <div class="modal fade" id="editStudentModal" tabindex="-1" role="dialog" aria-labelledby="modalEditLabel" aria-hidden="true">	
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
					<header class="text-primary font-weight-bold">Student Information</header>
						<form id="studentEdit">
						<div class="form-row mt-2">
							<div class="col-md-4">
								<label for="editState" class="label-form">State</label>
                                <select class="form-control" id="editState" name="editState" disabled>
								</select>
							</div>
							<div class="col-md-5">
								<label for="editBranch" class="label-form">Branch</label> 
								<select class="form-control" id="editBranch" name="editBranch" disabled>
								</select>
							</div>
							<div class="col-md-3">
								<label for="editGrade" class="label-form">Grade</label> <select class="form-control" id="editGrade" name="editGrade" disabled>
								</select>
							</div>
							<!-- <div class="col-md-3">
								<label for="editRegisterDate" class="label-form">Registration</label> 
								<input type="text" class="form-control" id="editRegisterDate" name="editRegisterDate" readonly>
							</div> -->
						</div>	
						<div class="form-row mt-2">
							<div class="col-md-4">
								<label for="editId" class="label-form">ID:</label> <input type="text" class="form-control" id="editId" name="editId" readonly>
							</div>
							<div class="col-md-4">
								<label for="editFirstName" class="label-form">First Name:</label> <input type="text" class="form-control" id="editFirstName" name="editFirstName" readonly>
							</div>
							<div class="col-md-4">
								<label for="editLastName" class="label-form">Last Name:</label> <input type="text" class="form-control" id="editLastName" name="editLastName" readonly>
							</div>
						</div>
						<%--
						<div class="form-row mt-2">
							<div class="col-md-3">
								<label for="editGender" class="label-form">Gender</label> <select class="form-control" id="editGender" name="editGender" disabled>
									<option value="male">Male</option>
									<option value="female">Female</option>
								</select>
							</div>
							<div class="col-md-9">
								<label for="editAddress" class="label-form">Address</label> <input type="text" class="form-control" id="editAddress" name="editAddress" readonly>
							</div>
						</div>
						<div class="form-row">
							<div class="col-md-12 mt-4">
								<section class="fieldset rounded" style="padding: 10px;">
									<header class="label-form" style="font-size: 0.9rem!important;">Main Contact</header>
								<div class="row">
									<div class="col-md-8">
										<input type="text" class="form-control" id="editContact1" name="editContact1" readonly>
									</div>
									<div class="col-md-4">
										<select class="form-control" id="editRelation1" name="editRelation1" disabled>
											<option value="mother">Mother</option>
											<option value="father">Father</option>
											<option value="sibling">Sibling</option>
											<option value="other">Other</option>
										</select>
									</div>	
								</div>
								<div class="row mt-2">
									<div class="col-md-12">
										<input type="text" class="form-control" id="editEmail1" name="editEmail1" placeholder="Email" readonly>
									</div>
								</div>
								</section>
							</div>
						</div>
						<div class="form-row">
							<div class="col-md-12 mt-4">
								<section class="fieldset rounded" style="padding: 10px;">
									<header class="label-form" style="font-size: 0.9rem!important;">Sub Contact</header>
								<div class="row">
									<div class="col-md-8">
										<input type="text" class="form-control" id="editContact2" name="editContact2" readonly>
									</div>
									<div class="col-md-4">
										<select class="form-control" id="editRelation2" name="editRelation2" disabled>
											<option value="mother">Mother</option>
											<option value="father">Father</option>
											<option value="sibling">Sibling</option>
											<option value="other">Other</option>
										</select>
									</div>	
								</div>
								<div class="row mt-2">
									<div class="col-md-12">
										<input type="text" class="form-control" id="editEmail2" name="editEmail2" readonly>
									</div>
								</div>
								</section>
							</div>
						</div>
						--%>
						<div class="form-row mt-3">
							<div class="col-md-12 mt-4">
								<section class="fieldset rounded border-warning" style="padding: 10px; background-color:beige;">
									<header class="label-form" style="font-size: 1.0rem!important;"><strong>Password Reset</strong></header>
								<div class="row mt-2">
									<div class="col-md-5">
										<label>New Password</label>
									</div>
									<div class="col-md-7">
										<input type="password" class="form-control" id="newPassword" name="newPassword">
									</div>
								</div>
								<div class="row mt-2">
									<div class="col-md-5">
										<label>Confirm Password</label>
									</div>
									<div class="col-md-7">
										<input type="password" class="form-control" id="confirmPassword" name="confirmPassword">
									</div>
								</div>
								</section>
							</div>
						</div>
					</form>					
					<div class="d-flex justify-content-end">
						<button type="submit" class="btn btn-primary" onclick="updatePassword()">Update Password</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
					</div>
				</section>
			</div>
		</div>
	</div>
</div>

<!-- Success Alert -->
<div id="success-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-success alert-dialog-display jae-border-success">
			<i class="fa fa-check-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>

<!-- Warning Alert -->
<div id="warning-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-warning alert-dialog-display jae-border-warning">
			<i class="fa fa-exclamation-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>
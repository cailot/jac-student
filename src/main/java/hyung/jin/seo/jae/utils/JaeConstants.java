package hyung.jin.seo.jae.utils;

public interface JaeConstants {

	String ALL = "All";
	
	String CURRENT = "Current";
	
	String STOPPED = "Stopped";

	String ONSITE = "Onsite";

	String ONLINE = "Online";
	
	///////////////////////////////////////////////////////////////////////
	//
	//	DAY LIST
	//
	////////////////////////////////////////////////////////////////////////

	String MONDAY = "Monday";
	
	String TUESDAY = "Tuesday";
	
	String WEDNESDAY = "Wednesday";
	
	String THURSDAY = "Thursday";
	
	String FRIDAY = "Friday";
	
	String SATURDAY = "Saturday";
	
	String SUNDAY = "Sunday";

	///////////////////////////////////////////////////////////////////////
	//
	//	GRADE LIST
	//
	////////////////////////////////////////////////////////////////////////

	String P2 = "p2";
	
	String P3 = "p3";	
	
	String P4 = "p4";
	
	String P5 = "p5";
	
	String P6 = "p6";
	
	String S7 = "s7";
	
	String S8 = "s8";
	
	String S9 = "s9";
	
	String S10 = "s10";
	
	String S10E = "s10e";
	
	String TT6 = "tt6";
	
	String TT8 = "tt8";
	
	String TT8E = "tt8e";
	
	String SRW4 = "srw4";
	
	String SRW5 = "srw5";
	
	String SRW6 = "srw6";
	
	String SRW8 = "srw8";
	
	String JMSS = "jmss";
	
	String VCE = "vce";

	String TT6_CODE = "11";

	String TT8_CODE = "12";

	String MOCK_TEST_NO = "99";

	String NO_PREVIOUS_GRADE = "0";
	
	///////////////////////////////////////////////////////////////////////
	
	String STUDENT_LIST = "StudentList";
	
	String UPGRADE_LIST = "UpgradeList";

	String COURSE_LIST = "CourseList";
	
	String CLASS_LIST = "ClassList";

	String CYCLE_LIST = "CycleList";

	String BRANCH_LIST = "BranchList";
	
	String TEACHER_LIST = "TeacherList";

	String ONLINE_LIST = "OnlineList";
	
	String HOMEWORK_LIST = "HomeworkList";
	
	String EXTRAWORK_LIST = "ExtraworkList";
	
	String PRACTICE_LIST = "PracticeList";

	String TEST_LIST = "TestList";

	String ASSESSMENT_LIST = "AssessList";
	
	String VSSE = "VSSE";

	String ACADEMIC_CYCLES = "academicCycles";

	int ACADEMIC_START_COMMING_WEEKS = 29;
	
	String ACADEMIC_NEXT_YEAR_COURSE_SUFFIX = "<span class='text-success'> (Next Year)</span>";

	// String ACADEMIC_NEXT_YEAR_COURSE_SUFFIX = " (Next Year)";

	int ACADEMIC_NEXT_YEAR_COURSE_PRICE_INCREASE = 3;

	String NEW_ENROLMENT = "NEW";	

	///////////////////////////////////////////////////////////////////////
	//
	//	PAYMENT METHOD LIST
	//
	////////////////////////////////////////////////////////////////////////

	String CASH = "cash";

	String BANK = "bank";

	String CARD = "card";

	String CHEQUE = "cheque";

	String FULL_PAID = "Full";

	String OVERDUE = "Overdue";

	String DISCOUNT_FREE = "100%";

	String PARTIAL_PAID = "Partial";

	String ENROLMENT = "Enrolment";

	String OUTSTANDING = "Outstanding";

	String BOOK = "Book";

	String MATERIAL = "Material";

	String PAYMENT = "Payment";

	String PAYMENT_INVOICES = "invoices";

	String PAYMENT_ENROLMENTS = "enrolments";

	String PAYMENT_OUTSTANDINGS = "outstandings";

	String PAYMENT_MATERIALS = "materials";

	String PAYMENT_PAYMENTS = "payments";

	String PAYMENT_HEADER = "receiptHeader";

	String INVOICE_INFO = "invoiceInfo";

	String INVOICE_PAID_AMOUNT = "invoicePaidAmount";

	String INVOICE_BRANCH = "invoiceBranch";

	String STUDENT_INFO = "studentInfo";

	String STUDENT_ANSWER = "studentAnswer";

	String CORRECT_ANSWER = "correctAnswer";

	String CRITERIA_INFO = "criteriaInfo";

	String WEEK_HEADER = "weekHeader";

	String ATTENDANCE_INFO = "attendanceInfo";

	String TYPE_USER = "user";

	String TYPE_HEADER = "header";
	
	String TYPE_LIST = "list";

	String ATTEND_LIST_STUDENT_ID = "Student ID";

	String ATTEND_LIST_STUDENT_NAME = "Student Name";

	String ATTEND_LIST_CLASS_ID = "Class ID";

	String ATTEND_LIST_CLASS_NAME = "Class Name";

	String ATTEND_LIST_CLASS_DAY = "Class Day";

	String ATTEND_LIST_CLASS_GRADE = "Grade";

	String ATTEND_YES = "Y";

	String ATTEND_NO = "N";

	String ATTEND_PAUSE = "P";

	String ATTEND_OTHER = "O";

	String TEST_RESULT_INFO = "testResultInfo";

	String TEST_GROUP_INFO = "testGroupInfo";

	String TEST_TITLE_INFO = "testTitleInfo";

	String BRANCH_INFO = "branchInfo";

	String VOLUME_INFO = "volumeInfo";

	String STUDENT_ANSWER_CORRECT_COUNT = "studentAnswerCorrectCount";

	String TEST_ANSWER_TOTAL_COUNT = "testAnswerTotalCount";

	String STUDENT_SCORE = "studentScore";

	String TEST_ANSWERS = "testAnswers";

	String STUDENT_ANSWERS = "studentAnswers";

	String STUDENT_TEST_SCORE = "studentTestScore";

	String TEST_HIGHEST_SCORE = "testHighestScore";

	String TEST_LOWEST_SCORE = "testLowestScore";

	String TEST_AVERAGE_SCORE = "testAverageScore";

	String TEST_RESULT_HISTORY = "testResultHistory";

	////////////////////////////////////
	//
	//	Student Active or not
	//
	////////////////////////////////////

	int ACTIVE = 0;

	int INACTIVE = 1;

	////////////////////////////////////
	//
	//	Student Default Password
	//
	////////////////////////////////////

	String DEFAULT_PASSWORD = "123";

	////////////////////////////////////
	//
	//	Homework Type
	//
	////////////////////////////////////

	int VIDEO = 0;

	int PDF = 1;

	////////////////////////////////////
	//
	//	PRACTICE
	//
	////////////////////////////////////

	String PRACTICE_COMPLETE = "DONE";

	////////////////////////////////////
	//
	//	STUDENT MIGRATION
	//
	////////////////////////////////////

	String BATCH_TOTAL = "batchTotal";

	String BATCH_SUCCESS = "batchSuccess";

	String BATCH_LIST = "batchList";

	////////////////////////////////////
	//
	//	USER ROLE
	//
	////////////////////////////////////

	String ROLE_ADMIN = "Role_Administrator";

	String ROLE_STAFF = "Role_Staff";
	
	String ADMINISTRATOR = "Administrator";

	String STAFF = "Staff";

	////////////////////////////////////
	//
	//	SUBJECT
	//
	////////////////////////////////////
	String SUBJECT_MATH = "Maths";

	String SUBJECT_ENGLISH = "English";

	String SUBJECT_GA = "General Ability";

	String SUBJECT_GA_SHORT = "G.A";

	////////////////////////////////////
	//
	//	ACCESS FROM
	//
	////////////////////////////////////
	String ONLINE_FROM = "online";

	String CONNECTED_FROM = "connected";

	////////////////////////////////////
	//
	//	STATUS
	//
	////////////////////////////////////
	int STATUS_NOTHING = 0;

	int STATUS_PROCESSING = 1;

	int STATUS_COMPLETED = 2;

	////////////////////////////////////
	//
	//	WEEK
	//
	////////////////////////////////////
	int FIRST_WEEK = 1;

	int SECOND_WEEK = 2;

	int THIRD_WEEK = 3;

	////////////////////////////////////
	//
	//	TEST GROUP
	//
	////////////////////////////////////

	int MEGA_TEST = 1;

	int REVISION_TEST = 2;

	int EDU_TEST = 3;

	int ACER_TEST = 4;

	int MOCK_TEST = 5;
	
	////////////////////////////////////
	//
	//	OMR PROCESS
	//
	////////////////////////////////////

	String OMR_FILE_JPG = "jpg";

	String OMR_FILE_JPEG = "jpeg";

	String OMR_FILE_PDF = "pdf";

	String OMR_FOLDER = "omr";
	
}

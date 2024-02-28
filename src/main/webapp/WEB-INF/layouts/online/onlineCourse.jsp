<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<sec:authorize access="isAuthenticated()">

<sec:authentication var="role" property='principal.authorities'/>
<sec:authentication var="id" property="principal.username"/>
<sec:authentication var="firstName" property="principal.firstName"/>
<sec:authentication var="lastName" property="principal.lastName"/>
	<script>
		var role = '${role}';
		var numericGrade = role.replace(/[\[\]]/g, ''); // replace '[' & ']' with an empty string
		var studentId = '${id}';
		var firstName = '${firstName}';
		var lastName = '${lastName}';
		var academicYear;
    	var academicWeek;
	</script>
</sec:authorize>

<script>
$(function() {
	// to get the academic year and week
	$.ajax({
		url : '${pageContext.request.contextPath}/class/academy',
		method: "GET",
		success: function(response) {
			// save the response into the variable
			academicYear = response[0];
			academicWeek = response[1];
			// update the value of the academicWeek span element
			document.getElementById("academicWeek").innerHTML = academicWeek;
			// update online url
			getOnlineLive(studentId, academicYear, academicWeek);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Error : ' + errorThrown);
		}
	});
	// initialise state list when loading
	listState('#editState');
    listBranch('#editBranch');
	listGrade('#editGrade');
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
			$("#editEmail1").val(student.email1);
			$("#editEmail2").val(student.email2);
			$("#editRelation1").val(student.relation1);
			$("#editRelation2").val(student.relation2);
			$("#editAddress").val(student.address);
			$("#editContact1").val(student.contactNo1);
			$("#editContact2").val(student.contactNo2);
			$("#editState").val(student.state);
			$("#editBranch").val(student.branch);
			$("#editGrade").val(student.grade);
			$("#editGender").val(student.gender);
            var regDate = formatDate(student.registerDate);
			$("#editRegisterDate").val(regDate);
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
//		Retrieve Online Session Info
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function getOnlineLive(studentId, year, week) {
	$.ajax({
		url : '${pageContext.request.contextPath}/online/getSession/' + studentId + '/' + year + '/' + week,
		type : 'GET',
		success : function(data) {
			console.log(data);
			var url = data.address;
			// set the data-video-urlf attribute of the specified element
			$('#onlineLesson').attr('data-video-url', url);
			$('#onlineLessonDay').text(data.day);
			$('#onlineLessonStart').text(data.startTime);
			$('#onlineLessonEnd').text(data.endTime);	
			// set title elements
			$('#onlineLessonDayTitle').text(data.day);
			$('#onlineLessonStartTitle').text(data.startTime);
			$('#onlineLessonEndTitle').text(data.endTime);	
			// check recorded session
			determineLiveOrRecordedLesson();
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Retrieve Recorded Session Info
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function getRecordedSession(studentId, year, week) {
	$.ajax({
		url : '${pageContext.request.contextPath}/online/getSession/' + studentId + '/' + year + '/' + week,
		type : 'GET',
		success : function(data) {
			console.log(data);
			var url = data.address;
			// set the data-video-urlf attribute of the specified element
			$('#recordAcademicMinusOneWeek').attr('data-video-url', url);
			$('#recordAcademicMinusOneWeekDay').text(data.day);
			$('#recordAcademicMinusOneWeekStart').text(data.startTime);
			$('#recordAcademicMinusOneWeekEnd').text(data.endTime);	
			// display block
			document.getElementById("recordAcademicMinusOneWeekBlock").style.display = "block";
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
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
// 			Determine Online live/ Recorded lession
////////////////////////////////////////////////////////////////////////////////////////////////////////
function determineLiveOrRecordedLesson() {
  // Get the current date and time
  var now = new Date();
  // Get the time from the elements
  var lessonDay = document.getElementById("onlineLessonDayTitle").textContent;
  var lessonStart = document.getElementById("onlineLessonStartTitle").textContent;
  var lessonEnd = document.getElementById("onlineLessonEndTitle").textContent;
  //console.log(lessonDay + ' : ' + lessonStart + ' : ' + lessonEnd);
  // Convert the times to Date objects for comparison
  var lessonStartDate = getTimeForDayAndTime(lessonDay, lessonStart);
  var lessonEndDate = getTimeForDayAndTime(lessonDay, lessonEnd);
  // Compare the current time with the lesson times
  //console.log(now.getTime() + ':' + lessonStartDate + ' : ' + lessonEndDate);

  if (now.getTime() >= lessonStartDate && now.getTime() <= lessonEndDate) {
    console.log("Onair");
	// 1. turn on mic
    var micIcon = $('#micIcon');
    micIcon.removeClass('text-secondary').addClass('text-danger');
	// 2. disappear previous div
	document.getElementById("recordAcademicMinusOneWeekBlock").style.display = "none";  
  } else if (now.getTime() < lessonStartDate) {
    console.log("Before Onair");
	// 1. disable link
	document.getElementById("onlineLesson").setAttribute('data-video-url', '');
	// 2. enable recorded session
	getRecordedSession(studentId, academicYear, academicWeek-1);
	// 3. update label in recordedSessionBlock
	document.getElementById("academicMinusOneWeek").innerHTML = academicWeek-1;
	document.getElementById("recordedLessonInfo").textContent = 'Available until this ' + lessonDay + ', ' + lessonStart;
  } else {
    console.log("After onair");
	// 1. disable link
	document.getElementById("onlineLesson").setAttribute('data-video-url', '');
	// 2. enable recorded session
	getRecordedSession(studentId, academicYear, academicWeek);
	// 3. update label in recordedSessionBlock
	document.getElementById("academicMinusOneWeek").innerHTML = academicWeek;
	document.getElementById("recordedLessonInfo").textContent = 'Available until next ' + lessonDay + ', ' + lessonStart;
  }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Get time for live onair comparison
////////////////////////////////////////////////////////////////////////////////////////////////////////
function getTimeForDayAndTime(day, time) {
  // Create an array of days to map them to corresponding indices
  const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  // Get the current date
  const currentDate = new Date();
  // Find the index of the specified day
  const dayIndex = daysOfWeek.indexOf(day);
  if (dayIndex === -1) {
    console.error("Invalid day provided");
    return null;
  }
  // Calculate the difference between the current day and the specified day
  var dayOnMondayBase = currentDate.getDay();
  dayOnMondayBase = (dayOnMondayBase===0) ? 6 : dayOnMondayBase-1;
  let dayDifference = dayIndex - dayOnMondayBase;
  currentDate.setDate(currentDate.getDate() + dayDifference);
  // Parse the time string and set the hours and minutes
  const [hours, minutes] = time.split(':');
  currentDate.setHours(parseInt(hours, 10), parseInt(minutes, 10), 0, 0);
  // Return the getTime() value
  return currentDate.getTime();
}

</script>    

<style>

	p#onlineLesson:hover, p#recordAcademicMinusOneWeek:hover, span#studentName:hover {
        cursor: pointer;
    }
	
	.custom-icon {
    font-size: 2rem; /* Adjust the size as needed */
	}

	/* Style for an additional container element */
	.iframe-container {
		margin: 5px; /* Adjust the margin as needed */
	}

	/* Style for the iframe */
	#lessonVideo {
		width: 1000px;
		height: 550px;
		border: none;
		background: url('${pageContext.request.contextPath}/image/video-thumbnail.png') center center no-repeat;
		background-size: 40%;
	}

</style>
<div class="row">
    <div class="col-lg-12">
        <div class="card-body jae-background-color text-center">
            <img src="${pageContext.request.contextPath}/image/logo.png" style="filter: brightness(0) invert(1);width:75px;" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text-light align-middle h1">Jac-eLearning Student Lecture</span>           
        </div>
		<sec:authorize access="isAuthenticated()">
			<div class="card-body jae-background-color text-right" style="display: flex; align-items: center; justify-content: space-between; padding-top: 0px;">
				<div>
					<span class="card-text text-warning font-weight-bold font-italic h5" style="margin-left: 25px;" id="studentName" onclick="clearPassword();retrieveStudentInfo()">${firstName} ${lastName}</span>
					<span style="color: white;">&nbsp;&nbsp;(</span>
					<span class="card-text" id="studentGrade" name="studentGrade" style="color: white;"></span>
					<span style="color: white;">)  </span>
					<script>document.getElementById("studentGrade").textContent = displayGrade();</script>
				</div>
				<form:form action="${pageContext.request.contextPath}/online/logout" method="POST" id="logout">
					<button class="btn" style="margin-right: 20px;"><i class="bi bi-box-arrow-right custom-icon text-warning" title="Log Out"></i></button>
				</form:form>
			</div>
		</sec:authorize>
        <!-- HTML with additional container -->
		<div class="iframe-container">
			<iframe id="lessonVideo" src="" allow="autoplay; encrypted-media" allowfullscree]></iframe>
		</div>
		
		<div class="card-body">
            <div class="alert alert-info" role="alert">
				<p id="onlineLesson" data-video-url="" style="margin-left: 30px; margin-top: 20px;">
					Online Live Weekly Lesson <strong>Set</strong> <span id="academicWeek"></span>
					(<span id="onlineLessonDayTitle" name="onlineLessonDayTitle"></span>, <span id="onlineLessonStartTitle" name="onlineLessonStartTitle"></span> - <span id="onlineLessonEndTitle" name="onlineLessonEndTitle"></span>&nbsp;&nbsp;<i id="micIcon" name="micIcon" class="bi bi-mic-fill text-secondary h5" title="Live"></i>)
					&nbsp;<i class="bi bi-caret-right-square text-primary" title="Play Video"></i>
				</p>
            </div>
            <div id="recordAcademicMinusOneWeekBlock"class="alert alert-primary" role="alert">
                <p id="recordAcademicMinusOneWeek" data-video-url="" style="margin-left: 30px; margin-top: 20px;">
                    Recorded Lesson &nbsp;<strong>Set</strong> <span id="academicMinusOneWeek"></span>&nbsp;(<span id="recordedLessonInfo"></span>)
					<i class="bi bi-caret-right-square text-primary" title="Play Video"></i>    
                </p>
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
								<label for="editRegisterDate" class="label-form">Registration</label> 
								<input type="text" class="form-control" id="editRegisterDate" name="editRegisterDate" readonly>
							</div>
						</div>	
						<div class="form-row mt-2">
							<div class="col-md-3">
								<label for="editId" class="label-form">ID:</label> <input type="text" class="form-control" id="editId" name="editId" readonly>
							</div>
							<div class="col-md-4">
								<label for="editFirstName" class="label-form">First Name:</label> <input type="text" class="form-control" id="editFirstName" name="editFirstName" readonly>
							</div>
							<div class="col-md-3">
								<label for="editLastName" class="label-form">Last Name:</label> <input type="text" class="form-control" id="editLastName" name="editLastName" readonly>
							</div>
							<div class="col-md-2">
								<label for="editGrade" class="label-form">Grade</label> <select class="form-control" id="editGrade" name="editGrade" disabled>
								</select>
							</div>
						</div>
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
						<div class="form-row">
							<div class="col-md-12 mt-4">
								<section class="fieldset rounded" style="padding: 10px; background-color:beige;">
									<header class="label-form" style="font-size: 0.9rem!important;">Password Reset</header>
								<div class="row">
									<div class="col-md-5">
										<label>New Password</label>
									</div>
									<div class="col-md-7">
										<input type="text" class="form-control" id="newPassword" name="newPassword">
									</div>
								</div>
								<div class="row mt-2">
									<div class="col-md-5">
										<label>Confirm Password</label>
									</div>
									<div class="col-md-7">
										<input type="text" class="form-control" id="confirmPassword" name="confirmPassword">
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

<script>
    // get the online lesson element and the video iframe element
    const onlineLesson = document.getElementById('onlineLesson');
    const recordLesson = document.getElementById('recordAcademicMinusOneWeek');
    const lessonVideo = document.getElementById('lessonVideo');

    // function to show the media warning modal
    function showRealtimeWarningModal() {
        $('#realtimeWarning').modal('show');
    }
	// function to show the media warning modal
	function showRecordWarningModal() {
        $('#recordWarning').modal('show');
    }
    // add event listeners to the online lesson and recordAcademicWeek elements
    onlineLesson.addEventListener('click', () => {
		// set the videoUrl to the hidden input field
		document.getElementById("realtimeVideoUrl").value = onlineLesson.getAttribute('data-video-url');
        // Show confirmation dialog before calling handleLessonClick
        showRealtimeWarningModal();
    });

    recordLesson.addEventListener('click', () => {
        // set the videoUrl to the hidden input field
		document.getElementById("recordVideoUrl").value = recordLesson.getAttribute('data-video-url');
        // Show confirmation dialog before calling handleLessonClick
        showRecordWarningModal();
    });


	function displayMedia(videoUrl){
		// remove iframe inital background
		lessonVideo.style.background = 'none';
		// get the videoUrl from the hidden input field
		//const videoUrl = realtime ? document.getElementById("realtimeVideoUrl").value : document.getElementById("recordVideoUrl").value;
		const videoAddress = document.getElementById(videoUrl).value; 
		// set the video URL as the iframe's src attribute
		lessonVideo.setAttribute('src', videoAddress);
		// show the video by setting the iframe's display to block
		lessonVideo.style.display = 'block';
		// Hide the media warning modal
        $('#realtimeWarning').modal('hide');
        $('#recordWarning').modal('hide');
	}






	





</script>

<!-- Realtime Video Warning Modal -->
<div class="modal fade" id="realtimeWarning" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header bg-warning" style="display: block;">
				<p style="text-align: center; margin-bottom: 0;"><span style="font-size:18px"><strong>James An College Year <span class="text-danger" id="onlineGrade" name="onlineGrade"></span></strong></span></p>
				<script>
					document.getElementById("onlineGrade").textContent = displayGrade();       
				</script>
			</div>
            <div class="modal-body">
                <div style="text-align: center; margin-bottom: 20px;">
                    <img src="${pageContext.request.contextPath}/image/warning.png" style="width: 150px; height: 150px; border-radius: 5%;">
                </div>
                <!-- Add your warning message or content here -->
                <p ><strong>Class Time:</strong> Every <span id="onlineLessonDay" name="onlineLessonDay"></span>, <span id="onlineLessonStart" name="onlineLessonStart"></span> - <span id="onlineLessonEnd" name="onlineLessonEnd"></span></p>
                <ol>
                    <li>Each set should be completed prior to the 'online class'.</li>
                    <li><span class="text-danger"><strong>Do not turn on your Camera.</strong></span></li>
                    <li>
                        You can ask a question to the teacher if necessary. But please do not bring up irrelevant
                        topics or send dubious and unnecessary content. Anyone who does not respect the online
                        etiquette may be removed from the class at teacher or Head Office's discretion.
                    </li>
                    <li>
                        Please change your name to <strong>&#39;Full Name - JAC Branch&#39;</strong>, e.g. Ava Lee - Braybrook
                        <br>
                        - You can change your name before joining the class or 'rename' yourself after joining.</li>
                        
                    </li>
                    <li>
                        Please note JAC <span class="text-primary"><strong>&#39;Connected Class&#39; </strong></span> is still available for extra coverage.
                    </li>
                </ol>
            </div>
            <input type="hidden" id="realtimeVideoUrl" name="realtimeVideoUrl" value="">
            <div class="modal-footer">
				<button type="button" class="btn btn-primary" id="agreeMediaWarning" onclick="displayMedia('realtimeVideoUrl')">I agree</button>
            	<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- Record Video Warning Modal -->
<div class="modal fade" id="recordWarning" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header bg-warning" style="display: block;">
				<p style="text-align: center; margin-bottom: 0;"><span style="font-size:18px"><strong>James An College Year <span class="text-danger" id="recordGrade" name="recordGrade"></span></strong></span></p>
				<script>
					document.getElementById("recordGrade").textContent = displayGrade();       
				</script>
			</div>
            <div class="modal-body">
                <div style="text-align: center; margin-bottom: 20px;">
                    <img src="${pageContext.request.contextPath}/image/warning.png" style="width: 150px; height: 150px; border-radius: 5%;">
                </div>
                <!-- Add your warning message or content here -->
                <p><strong>Class Time:</strong> Every  <span id="recordAcademicMinusOneWeekDay" name="recordAcademicMinusOneWeekDay"></span>, <span id="recordAcademicMinusOneWeekStart" name="recordAcademicMinusOneWeekStart"></span> - <span id="recordAcademicMinusOneWeekEnd" name="recordAcademicMinusOneWeekEnd"></span></p></p>
				<p>
					Please note that the recorded video is available only for <span class="text-danger text-uppercase"><strong>a week</strong></span> until the following online lesson.
				</p>
            </div>
            <input type="hidden" id="recordVideoUrl" name="recordVideoUrl" value="">
            <div class="modal-footer">
				<button type="button" class="btn btn-primary" id="agreeMediaWarning" onclick="displayMedia('recordVideoUrl')">I agree</button>
            	<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- Success Alert -->
<div id="success-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-success alert-dialog-display">
			<i class="fa fa-check-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>

<!-- Warning Alert -->
<div id="warning-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-warning alert-dialog-display">
			<i class="fa fa-exclamation-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>


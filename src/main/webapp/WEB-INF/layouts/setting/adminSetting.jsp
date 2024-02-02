<script>
    var academicYear;
    var academicWeek;

    $(function() {
            // make an AJAX call on page load
            // to get the academic year and week
            $.ajax({
                url : '${pageContext.request.contextPath}/class/academy',
                method: "GET",
                success: function(response) {
                // save the response into the variable
                academicYear = response[0];
                academicWeek = response[1];

                // update the value of the academicYear span element
                // document.getElementById("academicYear").innerHTML = academicYear;
                // update the value of the academicWeek span element
                document.getElementById("academicWeek").innerHTML = academicWeek;
                document.getElementById("academicMinusOneWeek").innerHTML = academicWeek-1;
                document.getElementById("academicMinusTwoWeek").innerHTML = academicWeek-2;             
            },
                error: function(jqXHR, textStatus, errorThrown) {
                console.log('Error : ' + errorThrown);
                }
            });
    });
    // initialise state list when loading
    listState('#editState');
    listBranch('#editBranch');
	

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Retrieve Student Info
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function retrieveStudentInfo(std) {
	$.ajax({
		url : '${pageContext.request.contextPath}/student/get/' + std,
		type : 'GET',
		success : function(student) {
			$('#editStudentModal').modal('show');
			// Update display info
			 console.log(student);
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
		url : '${pageContext.request.contextPath}/student/updatePassword/' + id + '/' + confirmPwd,
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Clear password fields
////////////////////////////////////////////////////////////////////////////////////////////////////////
function clearPassword() {
	$("#newPassword").val('');
	$("#confirmPassword").val('');
}


</script>    

<style>

	p#onlineLesson:hover, p#recordAcademicWeek:hover, p#recordAcademicMinusOneWeek:hover, p#recordAcademicMinusTwoWeek:hover, span#studentName:hover {
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
        <div class="card-body bg-primary text-center">
            <img src="${pageContext.request.contextPath}/image/logo.png" style="filter: brightness(0) invert(1);width:75px;" >
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text-light h2">Jac-eLearning Student Lecture</span>           
        </div>
        <div class="card-body bg-primary text-right">
            <span class="card-text text-warning font-weight-bold font-italic" id="studentName" onclick="clearPassword();retrieveStudentInfo(130603)">Dylan Quach</span>
            <a href="#" class="btn btn-primary"><i class="bi bi-box-arrow-right custom-icon"></i></a>
        </div>
        <!-- HTML with additional container -->
		<div class="iframe-container">
			<iframe id="lessonVideo" src="" allow="autoplay; encrypted-media" allowfullscreen></iframe>
		</div>
		
		<div class="card-body">
            <div class="alert alert-info" role="alert">
                <p><strong>Week</strong> <span id="academicWeek"></span></p>
                <p id="onlineLesson" data-video-url="https://us02web.zoom.us/rec/play/ma2pfFazOsXqFla1dreILhb5Xjffq-85oAksTr9TgxjNdPfHDRKQMz7hcxuJrbpUaE6ofpw0wQ0WCt4s.qQEHvpWXF4BWgnru?canPlayFromShare=true&from=share_recording_detail&startTime=1706506287000&componentName=rec-play&originRequestUrl=https%3A%2F%2Fus02web.zoom.us%2Frec%2Fshare%2FmnB4w4HZI80oTYn_UyQCkveSxmITcw0Xs-Myw9pN4DUx4Dv-HrOaosI4si2jeOmr.32ShDyR6f2WnTe3j%3FstartTime%3D1706506287000">
                    <span style="margin-left: 30px;"> 
                        Online Weekly Lesson &nbsp;<i class="bi bi-caret-right-square text-primary" title="Play Video"></i>
                    </span>
                </p>
                <p id="recordAcademicWeek" data-video-url="https://us02web.zoom.us/j/81323157192">
                    <span style="margin-left: 30px;">Recorded Lesson &nbsp;<i class="bi bi-caret-right-square text-primary" title="Play Video"></i></span>
                </p> 
            </div>   
            <div class="alert alert-primary" role="alert">
                <p><strong>Week</strong> <span id="academicMinusOneWeek"></span></p>
                <p id="recordAcademicMinusOneWeek">
                    <span style="margin-left: 30px;">Recorded Lesson &nbsp;<i class="bi bi-caret-right-square text-primary" title="Play Video"></i></span>    
                </p>
            </div>
            <div class="alert alert-success" role="alert">
                <p><strong>Week</strong> <span id="academicMinusTwoWeek"></span></p>
                <p id="recordAcademicMinusTwoWeek">
                    <span style="margin-left: 30px;">Recorded Lesson &nbsp;<i class="bi bi-caret-right-square text-primary" title="Play Video"></i></span>
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
									<option value="p2">P2</option>
									<option value="p3">P3</option>
									<option value="p4">P4</option>
									<option value="p5">P5</option>
									<option value="p6">P6</option>
									<option value="s7">S7</option>
									<option value="s8">S8</option>
									<option value="s9">S9</option>
									<option value="s10">S10</option>
									<option value="s10e">S10E</option>
									<option value="tt6">TT6</option>
									<option value="tt8">TT8</option>
									<option value="tt8e">TT8E</option>
									<option value="srw4">SRW4</option>
									<option value="srw5">SRW5</option>
									<option value="srw6">SRW6</option>
									<option value="srw8">SRW8</option>
									<option value="jmss">JMSS</option>
									<option value="vce">VCE</option>
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
<!-- Your HTML code -->
<script>
    // get the online lesson element and the video iframe element
    const onlineLesson = document.getElementById('onlineLesson');
    const recordAcademicWeek = document.getElementById('recordAcademicWeek');
    const lessonVideo = document.getElementById('lessonVideo');

    // function to show the media warning modal
    function showMediaWarningModal() {
        $('#mediaWarning').modal('show');
    }

    // add event listeners to the online lesson and recordAcademicWeek elements
    onlineLesson.addEventListener('click', () => {
		// set the videoUrl to the hidden input field
		document.getElementById("videoUrl").value = onlineLesson.getAttribute('data-video-url');
        // Show confirmation dialog before calling handleLessonClick
        showMediaWarningModal();
    });

    recordAcademicWeek.addEventListener('click', () => {
        // set the videoUrl to the hidden input field
		document.getElementById("videoUrl").value = recordAcademicWeek.getAttribute('data-video-url');
        // Show confirmation dialog before calling handleLessonClick
        showMediaWarningModal();
    });


	function displayMedia(){
		// remove iframe inital background
		lessonVideo.style.background = 'none';
		// get the videoUrl from the hidden input field
		const videoUrl = document.getElementById("videoUrl").value;
		// set the video URL as the iframe's src attribute
		lessonVideo.setAttribute('src', videoUrl);
		// show the video by setting the iframe's display to block
		lessonVideo.style.display = 'block';
		// Hide the media warning modal
        $('#mediaWarning').modal('hide');
	}







// Add an event listener to the timeupdate event
lessonVideo.addEventListener("timeupdate", function() {
  // If the video is loaded and duration is known
  if (!isNaN(this.duration)) {
    // Calculate the percentage of the video that the user has watched
    var percentComplete = (this.currentTime / this.duration) * 100;
    // Display the percentage as a progress bar or as a number
    // You can use CSS to style the progress bar
    console.log("Percentage complete: " + percentComplete + "%");
  }
});

     
</script>

<!-- Video Warning Modal -->
<div class="modal fade" id="mediaWarning" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header bg-warning" style="display: block;">
				<p style="text-align: center; margin-bottom: 0;"><span style="font-size:18px"><strong>James An College Year <span class="text-danger">3</span></strong></span></p>
			</div>
            <div class="modal-body">
                <div style="text-align: center; margin-bottom: 20px;">
                    <img src="${pageContext.request.contextPath}/image/warning.png" style="width: 150px; height: 150px; border-radius: 5%;">
                </div>
                <!-- Add your warning message or content here -->
                <p ><strong>Class Time:</strong> Every Monday, 4:30 - 7:30 PM</p>
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
            <input type="hidden" id="videoUrl" name="videoUrl" value="">
            <div class="modal-footer">
				<button type="button" class="btn btn-primary" id="agreeMediaWarning" onclick="displayMedia()">I agree</button>
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


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
		// var academicYear;
    	// var academicWeek;
	</script>
</sec:authorize>

<script>

$(function() {
	// initialise state list when loading
	listState('#editState');
    listBranch('#editBranch');
	listGrade('#editGrade');
	// get available practice
	// $.ajax({
	// 	url : '/student/class/academy',
    //     method: "GET",
    //     success: function(response) {
    //         // save the response into the variable
    //         academicYear = response[0];
    //         academicWeek = response[1];
	// 	},
	// 	error : function(xhr, status, error) {
	// 		console.log('Error : ' + error);
	// 	}
	// });
});



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

</script>

<div class="container-fluid jae-header pt-3 pb-3">
<nav class="navbar">
	<div class="navbar_logo">
		<a href="${pageContext.request.contextPath}/connected/lesson">
			<img src="${pageContext.request.contextPath}/image/logo-cc-white.png" title="JAC Connected Class" style="filter: brightness(0) invert(1);width:65px;" >
		</a>
	</div>
	<ul class="navbar_menu">
		<!-- Homework -->
		<li class="nav-item dropdown">
			<a class="nav-link" href="#" role="button" aria-haspopup="true" aria-expanded="false">
				<i class="bi bi-pencil-square custom-icon mr-2"></i><span class="h4">Homework</span>
			</a>
			<div class="dropdown-menu">
				<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/engHomework">English Homework</a>
			  	<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/mathHomework">Mathematics Homework</a>
				<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/writeHomework">Writing Homework</a>
				<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/shortAnswer">Short Answer</a>
			</div>
		</li>
		<!-- Extra Materials -->
		<li class="nav-item dropdown">
			<a class="nav-link" href="${pageContext.request.contextPath}/connected/extraMaterial" role="button">
				<i class="bi bi-pencil-square custom-icon mr-2"></i><span class="h4">Extra Materials</span>
			</a>
		</li>
		<!-- Practice -->
		<li class="nav-item dropdown">
			<a class="nav-link" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<i class="bi bi-pencil-square custom-icon mr-2"></i><span class="h4">Practice</span>
			</a>			
			<div class="dropdown-menu">
				<!-- Mega Practice submenu -->
				<div class="dropdown-submenu">
					<a class="dropdown-item" href="#" id="megaPracticeDropdown" role="button" aria-haspopup="true" aria-expanded="false">
						Mega Practice
					</a>
					<div class="dropdown-menu" aria-labelledby="megaPracticeDropdown">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/practice/megaEng">MEGA English</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/practice/megaMath">MEGA Mathematics</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/practice/megaGA">MEGA General Ability</a>
					</div>
				</div>
				<!-- NAPLAN submenu -->
				<div class="dropdown-submenu">
					<a class="dropdown-item" href="#" id="naplanPracticeDropdown" role="button" aria-haspopup="true" aria-expanded="false">
						NAPLAN
					</a>
					<div class="dropdown-menu" aria-labelledby="naplanPracticeDropdown">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/practice/naplanLC">NAPLAN Language Conventions</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/practice/naplanMath">NAPLAN Mathematics</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/practice/naplanRead">NAPLAN Reading</a>
					</div>
				</div>
			</div>
		</li>

		<!-- Test -->
		<li class="nav-item dropdown">
			<a class="nav-link" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<i class="bi bi-pencil-square custom-icon mr-2"></i><span class="h4">Test</span> 
			</a>
			<div class="dropdown-menu">
				<!-- Mega Test submenu -->
				<div class="dropdown-submenu">
					<a class="dropdown-item" href="#" id="megaTestDropdown" role="button" aria-haspopup="true" aria-expanded="false">
						Mega Test
					</a>
					<div class="dropdown-menu" aria-labelledby="megaTestDropdown">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/test/megaEng">MEGA English</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/test/megaMath">MEGA Mathematics</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/test/megaGA">MEGA General Ability</a>
					</div>
				</div>
				<!-- Test Result submenu -->
				<div class="dropdown-submenu">
					<a class="dropdown-item" href="#" id="megaResultDropdown" role="button" aria-haspopup="true" aria-expanded="false">
						Mega Test Results
					</a>
					<div class="dropdown-menu" aria-labelledby="megaResultDropdown">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/result/megaVol1">MEGA Vol 1</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/result/megaVol2">MEGA Vol 2</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/result/megaVol3">MEGA Vol 3</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/result/megaVol4">MEGA Vol 4</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/connected/result/megaVol5">MEGA Vol 5</a>
					</div>
				</div>
			</div>
		</li>
		
		<!-- Link to Jac-eLearning -->
		<li class="nav-item dropdown">
			<a class="nav-link" href="${pageContext.request.contextPath}/online/lesson" role="button">
				<i class="bi bi-pencil-square custom-icon mr-2"></i><span class="h4">Jac-eLearning</span>
			</a>
		</li>

	</ul>
	<ul class="navbar_icon" style="margin: 0; padding: 0;">
		<sec:authorize access="isAuthenticated()">
			<div class="card-body jae-background-color text-right" style="display: flex; justify-content: space-between; padding-top: 20px;">
				<div>
					<span class="card-text text-warning font-weight-bold font-italic h5" style="margin-left: 25px; cursor: pointer;" id="studentName" onclick="clearPassword();retrieveStudentInfo()">${firstName} ${lastName}</span>
					<span style="color: white;">&nbsp;&nbsp;(</span>
					<span class="card-text h5" id="studentGrade" name="studentGrade" style="color: white;"></span>
					<span style="color: white;">)  </span>
					<script>document.getElementById("studentGrade").textContent = displayGrade();</script>
					&nbsp;&nbsp;
				</div>
				<form:form action="${pageContext.request.contextPath}/connected/logout" method="POST" id="logout" style="margin-bottom: 0px;">
					<button class="btn" style="margin-right: 20px;"><i class="bi bi-box-arrow-right custom-icon text-warning" title="Log Out"></i></button>
				</form:form>
			</div>
		</sec:authorize> 
	</ul>
</nav>
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


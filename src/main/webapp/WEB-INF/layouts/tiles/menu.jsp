<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="grade" value="" />
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="role" property='principal.authorities'/>
	<sec:authentication var="id" property="principal.username"/>
	<sec:authentication var="firstName" property="principal.firstName"/>
	<sec:authentication var="lastName" property="principal.lastName"/>
	<c:set var="grade" value="${role}" />
	<script>
		var role = '${role}';
		var numericGrade = role.replace(/[\[\]]/g, ''); // replace '[' & ']' with an empty string
		var studentId = '${id}';
		var firstName = '${firstName}';
		var lastName = '${lastName}';
		var sessionTimeoutMs = ${pageContext.session.maxInactiveInterval} * 1000;
		var inactivityTimerId = null;
		var sessionTimeoutRedirected = false;
		var contextPath = '${pageContext.request.contextPath}';
		// Determine if numericGrade is a number
		var isStudent = !isNaN(+numericGrade);
		// Get student grade when page loads
		var enrolGrade = numericGrade;

		function redirectToExpiredLogin() {
			if (sessionTimeoutRedirected) {
				return;
			}

			sessionTimeoutRedirected = true;
			var logoutForm = document.getElementById('logout');
			if (logoutForm) {
				logoutForm.submit();
				return;
			}

			// Fallback for unexpected markup state.
			window.location.replace(contextPath + '/connected/login?expired=true');
		}

		function resetInactivityTimer() {
			if (sessionTimeoutRedirected) {
				return;
			}

			if (inactivityTimerId) {
				clearTimeout(inactivityTimerId);
			}

			inactivityTimerId = setTimeout(function() {
				redirectToExpiredLogin();
			}, sessionTimeoutMs);
		}

		function startConnectedSessionMonitor() {
			// Fallback to 30 minutes when maxInactiveInterval is not available.
			if (!sessionTimeoutMs || sessionTimeoutMs <= 0) {
				sessionTimeoutMs = 30 * 60 * 1000;
			}

			resetInactivityTimer();
			$(document).on('click keydown scroll mousemove touchstart', function() {
				resetInactivityTimer();
			});
		}
		
		function updateMenuVisibility() {
			// Helper function to check if enrolGrade matches any of the specified grades
			function isGradeInList(grades) {
				return grades.some(grade => enrolGrade === grade);
			}
			
			// Homework menu visibility
			var homeworkGrades = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '11', '12'];
			if (isGradeInList(homeworkGrades)) {
				$('#homeworkMenu').show();
				$('#homeworkMenuDisabled').hide();
				
				// Show/hide homework sub-items based on grade
				if (isGradeInList(['1', '2', '3', '4', '5', '6', '7', '8', '9'])) {
					$('#engHomeworkItem, #mathHomeworkItem').show();
				} else {
					$('#engHomeworkItem, #mathHomeworkItem').hide();
				}
				
				if (isGradeInList(['2', '3', '4', '5'])) {
					$('#writeHomeworkItem').show();
				} else {
					$('#writeHomeworkItem').hide();
				}
				
				if (isGradeInList(['1', '2', '3', '4', '5', '6', '7', '8', '9', '11', '12'])) {
					$('#shortAnswerItem').show();
				} else {
					$('#shortAnswerItem').hide();
				}
			} else {
				$('#homeworkMenu').hide();
				$('#homeworkMenuDisabled').show();
			}
			
			// Practice menu visibility
			var practiceGrades = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '11', '12', '19'];
			if (isGradeInList(practiceGrades)) {
				$('#practiceMenu').show();
				$('#practiceMenuDisabled').hide();
				
				// Show/hide practice sub-items based on grade
				if (isGradeInList(['1', '2', '3', '4', '5'])) {
					$('#megaPracticeItem').show();
				} else {
					$('#megaPracticeItem').hide();
				}
				
				if (isGradeInList(['6', '7', '8', '9'])) {
					$('#revisionPracticeItem').show();
				} else {
					$('#revisionPracticeItem').hide();
				}
				
				if (isGradeInList(['2', '4', '6', '8'])) {
					$('#naplanItem').show();
				} else {
					$('#naplanItem').hide();
				}
				
				if (isGradeInList(['11', '12', '19'])) {
					$('#acerPracticeItem, #eduPracticeItem').show();
				} else {
					$('#acerPracticeItem, #eduPracticeItem').hide();
				}
			} else {
				$('#practiceMenu').hide();
				$('#practiceMenuDisabled').show();
			}
			
			// Test menu sub-items visibility
			// Test menu: code 1-5 = Primary (Mega), code 6-9 = Secondary (Revision/S7-S10)
			if (isGradeInList(['1', '2', '3', '4', '5'])) {
				$('#megaTestItem, #megaTestExplanationItem').show();
			} else {
				$('#megaTestItem, #megaTestExplanationItem').hide();
			}
			
			if (isGradeInList(['6', '7', '8', '9'])) {
				$('#revisionTestItem, #revisionTestExplanationItem').show();
			} else {
				$('#revisionTestItem, #revisionTestExplanationItem').hide();
			}
			
			if (isGradeInList(['11', '12', '19'])) {
				$('#classTestItem, #classTestExplanationItem').show();
				// During mock explanation period, TT8 students use the dedicated mock test explanation page.
				if (isGradeInList(['12']) && ${mockExplanationEnabled}) {
					$('#classTestExplanationItem').attr('href', '${pageContext.request.contextPath}/connected/test/mockTestExplanation');
				}
			} else {
				$('#classTestItem, #classTestExplanationItem').hide();
			}
			
			// Extra Materials menu visibility
			var extraMaterialsHiddenGrades = ['11', '12', '13', '19'];
			if (isGradeInList(extraMaterialsHiddenGrades)) {
				$('#extraMaterialsMenu').hide();
			} else {
				$('#extraMaterialsMenu').show();
			}
		}
		
		$(document).ready(function() {
			startConnectedSessionMonitor();

			if (isStudent) {
				// Initially hide all grade-dependent menus
				updateMenuVisibility();
				
				$.ajax({
					url: '${pageContext.request.contextPath}/connected/enrolGrade/' + studentId,
					type: 'GET',
					success: function(response) {
						// Ensure the grade has brackets for consistency with server-side logic
						// if (response && !response.startsWith('[')) {
						// 	enrolGrade = '[' + response + ']';
						// } else {
							enrolGrade = response;
						// }
						console.log('Enrol Grade: ' + enrolGrade);
						// Update menu visibility after getting enrolGrade
						updateMenuVisibility();
					},
					error: function(xhr, status, error) {
						console.error('Error getting student grade:', error);
					}
				});
			} else {
				// For non-students, show all menus
				$('#homeworkMenu, #practiceMenu').show();
				$('#homeworkMenuDisabled, #practiceMenuDisabled').hide();
				$('#engHomeworkItem, #mathHomeworkItem, #writeHomeworkItem, #shortAnswerItem').show();
				$('#megaPracticeItem, #revisionPracticeItem, #naplanItem, #acerPracticeItem, #eduPracticeItem').show();
			$('#megaTestItem, #megaTestExplanationItem, #revisionTestItem, #revisionTestExplanationItem, #classTestItem, #classTestExplanationItem').show();
			}
		});
	</script>
</sec:authorize>

<style>
  	.dropdown-toggle::after {
		display: none;
	}
	.dropdown:hover .dropdown-menu {
		display: block;
	}
	.nav-link-white {
		color: white !important;
	}
	/* Fixed styles for menu visibility */
	.navbar {
		width: 100%;
		display: flex;
		align-items: center;
	}
	.navbar_logo {
		padding: 0 20px;
	}
	.navbar-collapse {
		display: flex !important;
		flex: 1;
	}
	.navbar-nav {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		width: 100%;
	}
	.nav-item {
		padding: 0 15px;
		white-space: nowrap;
	}
	.nav-link {
		display: flex;
		align-items: center;
		color: white !important;
	}
	.custom-icon {
		margin-right: 5px;
	}
	.navbar_icon {
		margin-left: auto;
		display: flex;
		align-items: center;
	}
	.h4 {
		margin: 0;
		font-size: 1.2rem;
	}
	@media (max-width: 768px) {
		.navbar {
			flex-wrap: wrap;
			flex-direction: column;
			justify-content: center;
		}
		.navbar_logo {
			padding: 0 12px;
			margin-bottom: 6px;
		}
		.navbar_logo img {
			width: 36px !important;
		}
		.navbar-collapse {
			width: 100%;
			justify-content: center;
			order: 2;
		}
		.navbar-nav {
			flex-wrap: wrap;
			justify-content: center;
			text-align: center;
		}
		.nav-item {
			padding: 6px 10px;
		}
		.nav-item,
		.nav-link,
		#homeworkMenuDisabled,
		#practiceMenuDisabled {
			width: 100%;
			justify-content: center;
		}
		.navbar-nav .nav-item {
			flex: 0 0 100%;
		}
		.navbar-nav .nav-link .h4,
		#homeworkMenuDisabled .h4,
		#practiceMenuDisabled .h4 {
			display: inline-block;
		}
		.navbar_icon {
			width: 100%;
			margin: 6px 0 0;
			order: 3;
			justify-content: center;
		}
		.navbar_icon .card-body {
			width: 100%;
			flex-direction: column;
			gap: 4px;
		}
		#studentName,
		#studentGrade {
			font-size: 1rem;
		}
		.h4 {
			font-size: 1rem;
		}
	}
</style>

<script>
$(function() {
	// initialise state list when loading
	listState('#editState');
    listBranch('#editBranch');
	listGrade('#editGrade');
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display grade
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayGrade() {
	if(role === '[Administrator]') return 'Administrator';
	if(role === '[Staff]') return 'Staff';
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
</script>

<div class="jae-header w-100 pt-3 pb-3">
    <nav class="navbar navbar-expand-lg navbar-light px-3 px-lg-4 w-100 m-0">
        <div class="navbar_logo">
            <a href="${pageContext.request.contextPath}/connected/lesson">
                <img src="${pageContext.request.contextPath}/image/logo-cc.png" title="JAC Connected Class" style="filter: brightness(0) invert(1);width:50px;" >
            </a>
        </div>
        <div class="navbar-collapse">
            <ul class="navbar-nav">
				<!-- Homework -->
                <!-- Enabled homework menu (initially hidden) -->
                <li class="nav-item dropdown" id="homeworkMenu" style="display: none;">
                    <a class="nav-link dropdown-toggle" href="#" id="homeworkDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="bi bi-pencil-square custom-icon"></i>
                        <span class="h4">Homework</span>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="homeworkDropdown">
                        <a class="dropdown-item" id="engHomeworkItem" href="${pageContext.request.contextPath}/connected/engHomework" style="display: none;">English Homework</a>
                        <a class="dropdown-item" id="mathHomeworkItem" href="${pageContext.request.contextPath}/connected/mathHomework" style="display: none;">Mathematics Homework</a>
                        <a class="dropdown-item" id="writeHomeworkItem" href="${pageContext.request.contextPath}/connected/writeHomework" style="display: none;">Writing Homework</a>
                        <a class="dropdown-item" id="shortAnswerItem" href="${pageContext.request.contextPath}/connected/shortAnswer" style="display: none;">Short Answer</a>
                    </div>
                </li>
                <!-- Disabled homework menu (initially shown) -->
                <li class="nav-item" id="homeworkMenuDisabled">    
                    <i class="bi bi-pencil-square custom-icon"></i>
                    <span class="h4 text-white">Homework</span>
                </li>
                <!-- Extra Materials -->
                <li class="nav-item" id="extraMaterialsMenu">
                    <a class="nav-link" href="${pageContext.request.contextPath}/connected/extraMaterial">
                        <i class="bi bi-pencil-square custom-icon"></i>
                        <span class="h4">Extra Materials</span>
                    </a>
                </li>
				<!-- Practice -->                
				<!-- Enabled practice menu (initially hidden) -->
				<li class="nav-item dropdown" id="practiceMenu" style="display: none;">    
					<a class="nav-link dropdown-toggle" href="#" id="practiceDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="bi bi-pencil-square custom-icon"></i>
						<span class="h4">Practice</span>
					</a>
					<div class="dropdown-menu" aria-labelledby="practiceDropdown">
						<!-- Mega Practice submenu -->
						<a class="dropdown-item" id="megaPracticeItem" href="${pageContext.request.contextPath}/connected/practice/mega" style="display: none;">Mega Practice</a>
						<!-- Revision submenu -->
						<a class="dropdown-item" id="revisionPracticeItem" href="${pageContext.request.contextPath}/connected/practice/revision" style="display: none;">Revision Practice</a>
						<!-- Naplan submenu -->
						<a class="dropdown-item" id="naplanItem" href="${pageContext.request.contextPath}/connected/practice/naplan" style="display: none;">NAPLAN</a>
						<!-- Acer & Edu submenu -->
						<a class="dropdown-item" id="acerPracticeItem" href="${pageContext.request.contextPath}/connected/practice/acer" style="display: none;">ACER Practice</a>
						<a class="dropdown-item" id="eduPracticeItem" href="${pageContext.request.contextPath}/connected/practice/edu" style="display: none;">EDU Practice</a>
					</div>
				</li>
				<!-- Disabled practice menu (initially shown) -->
				<li class="nav-item" id="practiceMenuDisabled">    
					<i class="bi bi-pencil-square custom-icon"></i>
					<span class="h4 text-white">Practice</span>
				</li>
                <!-- Test -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="testDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="bi bi-pencil-square custom-icon"></i>
                        <span class="h4">Test</span>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="testDropdown">
                        <!-- Mega Test submenu -->
                        <a class="dropdown-item" id="megaTestItem" href="${pageContext.request.contextPath}/connected/test/mega" style="display: none;">Mega Test</a>
                        <a class="dropdown-item" id="megaTestExplanationItem" href="${pageContext.request.contextPath}/connected/test/megaExplanation" style="display: none;">Test Explanation</a>
                        <!-- Revision submenu -->
                        <a class="dropdown-item" id="revisionTestItem" href="${pageContext.request.contextPath}/connected/test/revision" style="display: none;">Revision Test</a>
                        <a class="dropdown-item" id="revisionTestExplanationItem" href="${pageContext.request.contextPath}/connected/test/revisionExplanation" style="display: none;">Test Explanation</a>
                        <!-- Acer,Edu submenu : TT6, JMSS, TT8 -->
                        <a class="dropdown-item" id="classTestItem" href="${pageContext.request.contextPath}/connected/test/edu" style="display: none;">Class Test</a>
                        <a class="dropdown-item" id="classTestExplanationItem" href="${pageContext.request.contextPath}/connected/test/ttExplanation" style="display: none;">Test Explanation</a>
                        <!-- Test Result submenu -->
                        <!-- OMR Result -->
                        <a id="recentResultLink" class="dropdown-item" href="#" download="TestResult.pdf">Recent Result</a>
                        <script>
                            document.addEventListener("DOMContentLoaded", async function () {
                                var recentResultLink = document.getElementById("recentResultLink");
                                var fileName = studentId + ".pdf";
                                // Extract branch code from studentId (2nd and 3rd digits)
                                var branchCode = studentId.substring(1, 3);
                                // Construct URL with dynamic branch code
                                var baseUrl = "https://jacstorage.blob.core.windows.net/work/test/" + branchCode + "/";
                                var fullUrl = baseUrl + fileName;

                                // Function to check if file exists
                                function checkFileExists(url) {
                                    return new Promise((resolve, reject) => {
                                        var xhr = new XMLHttpRequest();
                                        xhr.open('GET', url, true);
                                        xhr.onreadystatechange = function() {
                                            if (xhr.readyState === 4) {
                                                if (xhr.status === 200) {
                                                    resolve(true);
                                                } else {
                                                    resolve(false);
                                                }
                                            }
                                        };
                                        xhr.onerror = function() {
                                            resolve(false);
                                        };
                                        xhr.send();
                                    });
                                }

                                // Check file existence when page loads
                                const fileExists = await checkFileExists(fullUrl);
                                
                                if (fileExists) {
                                    // File exists, enable download through backend endpoint
                                    var downloadUrl = "${pageContext.request.contextPath}/result/download-azure-pdf/" + studentId;
                                    recentResultLink.href = downloadUrl;
                                    recentResultLink.style.color = "";
                                    recentResultLink.style.cursor = "pointer";
                                    recentResultLink.classList.remove("disabled");
                                    
                                    // Handle click for download
                                    recentResultLink.onclick = function(e) {
                                        e.preventDefault();
                                        window.location.href = downloadUrl;
                                        return false;
                                    };
                                } else {
                                    // File doesn't exist, disable link
                                    recentResultLink.href = "#";
                                    recentResultLink.style.color = "#999";
                                    recentResultLink.style.cursor = "not-allowed";
                                    recentResultLink.textContent = "Recent Result (Not Available)";
                                    recentResultLink.classList.add("disabled");
                                    
                                    // Prevent click when disabled
                                    recentResultLink.onclick = function(e) {
                                        e.preventDefault();
                                        return false;
                                    };
                                }
                            });
                        </script>
                    </div>
                </li>
                <!-- Jac-eLearning -->
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/online/lesson">
                        <i class="bi bi-pencil-square custom-icon"></i>
                        <span class="h4">JAC-eLearning</span>
                    </a>
                </li>
            </ul>
        </div>
        <ul class="navbar_icon" style="margin: 0; padding: 0;">
            <sec:authorize access="isAuthenticated()">
                <div class="card-body jae-background-color text-right" style="display: flex; align-items: center; justify-content: space-between; padding: 0;">
                    <div class="text-center">
                        <span class="card-text text-warning font-weight-bold font-italic h5" style="cursor: pointer;" id="studentName" onclick="if(isStudent) { clearPassword(); retrieveStudentInfo(); }">${firstName} ${lastName}</span>
                        <span class="text-white">&nbsp;&nbsp;[</span>
                        <span class="card-text h5" id="studentGrade" name="studentGrade" style="color: white;"></span>
                        <span class="text-white;">]&nbsp;&nbsp;</span>
                        <script>document.getElementById("studentGrade").textContent = displayGrade();</script>
                    </div>
                    <form:form action="${pageContext.request.contextPath}/connected/logout" method="POST" id="logout" style="margin: 0; display: flex; align-items: center;">
                        <button class="btn" style="padding: 0 20px 0 0;"><i class="bi bi-power custom-icon text-warning" title="Log Out"></i></button>
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

 
<style>
	/* Main menu (top-level) */
	.navbar-nav > li.nav-item > a.nav-link:hover,
	.navbar-nav > li.nav-item > a.nav-link:focus,
	.navbar-nav > li.nav-item > a.nav-link:hover .custom-icon,
	.navbar-nav > li.nav-item > a.nav-link:focus .custom-icon {
		color: #ffC107 !important;
		background-color: inherit !important;
	}
	/* Submenu (dropdown) */
	.navbar-nav li.nav-item.dropdown .dropdown-menu a.dropdown-item:hover,
	.navbar-nav li.nav-item.dropdown .dropdown-menu a.dropdown-item:focus,
	.navbar-nav li.nav-item.dropdown .dropdown-menu a.dropdown-item:hover .custom-icon,
	.navbar-nav li.nav-item.dropdown .dropdown-menu a.dropdown-item:focus .custom-icon {
		color: #ffC107 !important;
		background-color: #2d398e !important;
	}
	
	/* Disabled dropdown item */
	.navbar-nav li.nav-item.dropdown .dropdown-menu a.dropdown-item.disabled {
		color: #999 !important;
		background-color: transparent !important;
		cursor: not-allowed !important;
		pointer-events: none;
	}
	
	.navbar-nav li.nav-item.dropdown .dropdown-menu a.dropdown-item.disabled:hover {
		background-color: transparent !important;
		color: #999 !important;
	}
</style>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="hyung.jin.seo.jae.dto.StudentDTO"%>
<%@page import="hyung.jin.seo.jae.utils.JaeUtils"%>
<%@page import="java.util.Calendar"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.dataTables-1.13.4.min.css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/buttons.dataTables.min.css"></link>
<script src="${pageContext.request.contextPath}/js/jquery.dataTables-1.13.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/dataTables.buttons.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jszip.min.js"></script>
<script src="${pageContext.request.contextPath}/js/pdfmake.min.js"></script>
<script src="${pageContext.request.contextPath}/js/vfs_fonts.js"></script>
<script src="${pageContext.request.contextPath}/js/buttons.html5.min.js"></script>
<script src="${pageContext.request.contextPath}/js/buttons.print.min.js"></script>
 
  
<script>
$(document).ready(function () {
    $('#studentListTable').DataTable({
    	language: {
    		search: 'Filter:'
    	},
    	dom: 'Blfrtip',	
    	buttons: [
    		 'excelHtml5', 
             {
 	            extend: 'pdfHtml5',
 	            download: 'open',
 	            pageSize: 'A0'
 	        },
 	        'print'
        ],
		//pageLength: 20
    });
    
	$('table .password').on('click', function(){
		var username = $(this).parent().find('#username').val();
		$('#passwordModal #usernamepassword').val(username);
	});
	
	$("#addRegisterDate").datepicker({
		dateFormat: 'dd/mm/yy'
	});
	$("#editRegisterDate").datepicker({
		dateFormat: 'dd/mm/yy'
	});
	// initialise state list when loading
	listState('#listState');
	listState('#addState');
	listState('#editState');
	listBranch('#listBranch');
	listBranch('#addBranch');
	listBranch('#editBranch');
});

////////////////////////////////////////////////////////////////////////////////////////////////////
//		Register Student
////////////////////////////////////////////////////////////////////////////////////////////////////
function addStudent() {
	// Get from form data
	var std = {
		firstName : $("#addFirstName").val(),
		lastName : $("#addLastName").val(),
		email1 : $("#addEmail1").val(),
		email2 : $("#addEmail2").val(),
		relation1 : $("#addRelation1").val(),
		relation2 : $("#addRelation2").val(),
		address : $("#addAddress").val(),
		contactNo1 : $("#addContact1").val(),
		contactNo2 : $("#addContact2").val(),
		memo : $("#addMemo").val(),
		state : $("#addState").val(),
		branch : $("#addBranch").val(),
		grade : $("#addGrade").val(),
		gender : $("#addGender").val(),
		enrolmentDate : $("#addEnrolment").val()
	}
	// Send AJAX to server
	$.ajax({
        url : '${pageContext.request.contextPath}/student/register',
        type : 'POST',
        dataType : 'json',
        data : JSON.stringify(std),
        contentType : 'application/json',
        success : function() {
			// Display the success alert
            $('#success-alert .modal-body').text(
                    'New Student is registered successfully.');
            $('#success-alert').modal('show');
			$('#success-alert').on('hidden.bs.modal', function(e) {
				location.reload();
			});
        },
        error : function(xhr, status, error) {
            console.log('Error : ' + error);
        }
    });
	$('#registerStudentModal').modal('hide');
	// flush all registered data
	document.getElementById("studentRegister").reset();
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//		Update Student
////////////////////////////////////////////////////////////////////////////////////////////////////
function updateStudentInfo(){
	
	// get from formData
	var std = {
		id : $('#editId').val(),
		firstName : $("#editFirstName").val(),
		lastName : $("#editLastName").val(),
		email1 : $("#editEmail1").val(),
		email2 : $("#editEmail2").val(),
		address : $("#editAddress").val(),
		contactNo1 : $("#editContact1").val(),
		contactNo2 : $("#editContact2").val(),
		relation1 : $("#editRelation1").val(),
		relation2 : $("#editRelation2").val(),
		memo : $("#editMemo").val(),
		state : $("#editState").val(),
		branch : $("#editBranch").val(),
		grade : $("#editGrade").val(),
		gender : $("#editGender").val(),
		registerDate : $("#editRegisterDate").val()
	}
		
	// send query to controller
	$.ajax({
		url : '${pageContext.request.contextPath}/student/update',
		type : 'PUT',
		dataType : 'json',
		data : JSON.stringify(std),
		contentType : 'application/json',
		success : function(value) {
			// Display success alert
			$('#success-alert .modal-body').text('ID : ' + value.id + ' is updated successfully.');
			$('#success-alert').modal('show');
			// fetch data again
			$('#success-alert').on('hidden.bs.modal', function(e) {
				location.reload();
			});
			
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
	
	$('#editStudentModal').modal('hide');
	// flush all registered data
	document.getElementById("studentEdit").reset();
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//		De-activate Student
////////////////////////////////////////////////////////////////////////////////////////////////////
function inactivateStudent(id) {
	if(confirm("Are you sure you want to suspend this student?")){
		// send query to controller
		$.ajax({
			url : '${pageContext.request.contextPath}/student/inactivate/' + id,
			type : 'PUT',
			success : function(data) {
				// clear existing form
				$('#success-alert .modal-body').text(
						'ID : ' + id + ' is now suspended');
				$('#success-alert').modal('show');
				$('#success-alert').on('hidden.bs.modal', function(e) {
					location.reload();
				});
			},
			error : function(xhr, status, error) {
				console.log('Error : ' + error);
			}
		}); 
	}else{
		return;
	}
}


////////////////////////////////////////////////////////////////////////////////////////////////////
//		De-activate Student
////////////////////////////////////////////////////////////////////////////////////////////////////
function activateStudent(id) {
	if(confirm("Are you sure you want to re-activate this student?")){
		// send query to controller
		$.ajax({
			url : '${pageContext.request.contextPath}/student/activate/' + id,
			type : 'PUT',
			success : function(data) {
				// clear existing form
				$('#success-alert .modal-body').text(
						'ID : ' + id + ' is now re-activated');
				$('#success-alert').modal('show');
				$('#success-alert').on('hidden.bs.modal', function(e) {
					location.reload();
				});
			},
			error : function(xhr, status, error) {
				console.log('Error : ' + error);
			}
		}); 
	}else{
		return;
	}
}


////////////////////////////////////////////////////////////////////////////////////////////////////
//		Retrieve Student by User's click	
////////////////////////////////////////////////////////////////////////////////////////////////////
function retrieveStudentInfo(std) {
	// send query to controller
	$.ajax({
		url : '${pageContext.request.contextPath}/student/get/' + std,
		type : 'GET',
		success : function(student) {
			$('#editStudentModal').modal('show');
			// Update display info
			// console.log(student);
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
			$("#editMemo").val(student.memo);
			$("#editState").val(student.state);
			$("#editBranch").val(student.branch);
			$("#editGrade").val(student.grade);
			$("#editGender").val(student.gender);
			// Set date value
			var date = new Date(student.registerDate); // Replace with your date value
			$("#editRegisterDate").datepicker('setDate', date);
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}


</script>

<!-- Datepicker size change ... -->
<style>
	/* #studentListTable th, tr {
		padding: 15px;
	}
	#studentList .form-row {
  		margin-top: 20px;
		margin-bottom: 20px;
	}
	div.dataTables_filter {
		padding-top: 35px;
		padding-bottom: 35px;
	}
	div.dt-buttons {
		padding-top: 35px;
		padding-bottom: 10px;
	}
	div.dataTables_length{
		padding-top: 40px;
		padding-bottom: 10px;
	}
	tr { height: 50px }  */

</style>


<!-- List Body -->
<div class="row">
	<div class="modal-body">
		<form id="studentList" method="get" action="${pageContext.request.contextPath}/student/list">
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-1">
						<label for="listState" class="label-form">State</label> 
						<select class="form-control" id="listState" name="listState">
							<option value="All">All State</option>
						</select>
					</div>
					<div class="col-md-2">
						<label for="listBranch" class="label-form">Branch</label> 
						<select class="form-control" id="listBranch" name="listBranch">
							<option value="All">All Branch</option>
						</select>
					</div>
					<div class="col-md-1">
						<label for="listGrade" class="label-form">Grade</label> 
						<select class="form-control" id="listGrade" name="listGrade">
							<option value="All">All</option>
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
							<option value="jmss">JMSS</option>
							<option value="vce">VCE</option>
						</select>
					</div>
					<div class="col-md-1">
						<label for="listYear" class="label-form">Academic Year</label> 
						<select class="form-control" id="listYear" name="listYear">
							<%
								Calendar now = Calendar.getInstance();
								int currentYear = now.get(Calendar.YEAR);
							%>
							<!-- <option value="All">All</option> -->
							<option value="<%= currentYear %>"><%= currentYear %></option>
							<%
								// Adding the last five years
								for (int i = currentYear - 1; i >= currentYear - 5; i--) {
							%>
								<option value="<%= i %>"><%= i %></option>
							<%
							}
							%>
						</select>
					</div>
					<div class="col-md-2">
						<label for="listActive" class="label-form">Activated</label> 
						<select class="form-control" id="listActive" name="listActive">
							<!-- <option value="All">All Students</option> -->
							<option value="Current">Current Students</option>
							<option value="Stopped">Stopped Students</option>
						</select>
					</div>
					<div class="offset-md-2"></div>
					<div class="col mx-auto">
						<label class="label-form-white">Search</label> 
						<button type="submit" class="btn btn-primary btn-block"> <i class="bi bi-search"></i>&nbsp;Search</button>
					</div>
					<div class="col mx-auto">
						<label class="label-form-white">Registration</label> 
						<button type="button" class="btn btn-block btn-success" data-toggle="modal" data-target="#registerStudentModal"><i class="bi bi-plus"></i>&nbsp;Registration</button>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						<div class="table-wrap">
							<table id="studentListTable" class="table table-striped table-bordered" style="width: 100%;">
								<thead class="table-primary">
									<tr>
										<th>ID</th>
										<th>First Name</th>
										<th>Last Name</th>
										<th>Grade</th>
										<th>Gender</th>
										<th>Register Date</th>
										<th>Start</th>
										<th>End</th>
										<th>Main Email</th>
										<th>Main Contact</th>
										<th>Sub Email</th>
										<th>Sub Contact</th>
										<th data-orderable="false">Action</th>
									</tr>
								</thead>
								<tbody id="list-student-body">
								<c:choose>
									<c:when test="${StudentList != null}">
										<c:forEach items="${StudentList}" var="student">
											<tr>
												<td class="small ellipsis" id="studentId" name="studentId"><span><c:out value="${student.id}" /></span></td>
												<td class="small ellipsis text-truncate" style="max-width: 0; overflow: hidden;"><span><c:out value="${student.firstName}" /></span></td>
												<td class="small ellipsis text-truncate" style="max-width: 0; overflow: hidden;"><span><c:out value="${student.lastName}" /></span></td>
												<td class="small ellipsis"><span><c:out value="${fn:toUpperCase(student.grade)}" /></span></td>
												<td class="small ellipsis"><span style="text-transform: capitalize;"><c:out value="${fn:toLowerCase(student.gender)}" /></span></td>
												<td class="small ellipsis">
													<span>
														<fmt:parseDate var="studentRegistrationDate" value="${student.registerDate}" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${studentRegistrationDate}" pattern="dd/MM/yyyy" />
													</span>
												</td>
												<td class="small ellipsis"><span><c:out value="${student.startWeek}" /></span></td>
												<td class="small ellipsis"><span><c:out value="${student.endWeek}" /></span></td>	
												<td class="small ellipsis text-truncate" style="max-width: 0; overflow: hidden;"><span><c:out value="${student.email1}" /></span></td>
												<td class="small ellipsis text-truncate" style="max-width: 0; overflow: hidden;"><span><c:out value="${student.contactNo1}" /></span></td>
												<td class="small ellipsis text-truncate" style="max-width: 0; overflow: hidden;"><span><c:out value="${student.email2}" /></span></td>
												<td class="small ellipsis text-truncate" style="max-width: 0; overflow: hidden;"><span><c:out value="${student.contactNo2}" /></span></td>
												<td>
													<i class="bi bi-pencil-square text-primary" data-toggle="tooltip" title="Edit" onclick="retrieveStudentInfo('${student.id}')"></i>&nbsp;
													<a href="#passwordStudentModal" class="password" data-toggle="modal"><i class="bi bi-key text-warning" data-toggle="tooltip" title="Change Password"></i></a>&nbsp;
				 									<c:choose>
														<c:when test="${empty student.endDate}">
															<i class="bi bi-x-circle text-danger" data-toggle="tooltip" title="Suspend" onclick="inactivateStudent('${student.id}')"></i>
														</c:when>
														<c:otherwise>
															<i class="bi bi-arrow-clockwise text-success" data-toggle="tooltip" title="Activate" onclick="activateStudent('${student.id}')"></i>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									</c:when>
								</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<!-- Register Form Dialogue -->
<div class="modal fade" id="registerStudentModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
					<header class="text-primary font-weight-bold">Student Registration</header>
					<form id="studentRegister">
						<div class="form-row mt-2">
							<div class="col-md-4">
								<label for="addState" class="label-form">State</label> 
								<select class="form-control" id="addState" name="addState">
								</select>
							</div>
							<div class="col-md-5">
								<label for="addBranch" class="label-form">Branch</label> 
								<select class="form-control" id="addBranch" name="addBranch">
								</select>
							</div>
							<div class="col-md-3">
								<label for="addRegisterDate" class="label-form">Registration</label> 
								<input type="text" class="form-control datepicker" id="addRegisterDate" name="addRegisterDate" placeholder="Select Date" required>
							</div>
							<script>
								var today = new Date();
								var day = today.getDate();
								var month = today.getMonth() + 1; // Note: January is 0
								var year = today.getFullYear();
								var formattedDate = (day < 10 ? '0' : '') + day + '/' + (month < 10 ? '0' : '') + month + '/' + year;
								document.getElementById('addRegisterDate').value = formattedDate;
							</script>
						</div>
						<div class="form-row mt-2">
							<div class="col-md-5">
								<label for="addFirstName" class="label-form">First Name:</label> <input type="text" class="form-control" id="addFirstName" name="addFirstName">
							</div>
							<div class="col-md-4">
								<label for="addLastName" class="label-form">Last Name:</label> <input type="text" class="form-control" id="addLastName" name="addLastName">
							</div>
							<div class="col-md-3">
								<label for="addGrade" class="label-form">Grade</label> <select class="form-control" id="addGrade" name="addGrade">
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
								<label for="addGender" class="label-form">Gender</label> <select class="form-control" id="addGender" name="addGender">
									<option value="male">Male</option>
									<option value="female">Female</option>
								</select>
							</div>
							<div class="col-md-9">
								<label for="addAddress" class="label-form">Address</label> <input type="text" class="form-control" id="addAddress" name="addAddress">
							</div>
						</div>
						<div class="form-row">
							<div class="col-md-12 mt-4">
								<section class="fieldset rounded" style="padding: 10px;">
									<header class="label-form" style="font-size: 0.9rem!important;">Main Contact</header>
								<div class="row">
									<div class="col-md-8">
										<input type="text" class="form-control" id="addContact1" name="addContact1" placeholder="Contact No">
									</div>
									<div class="col-md-4">
										<select class="form-control" id="addRelation1" name="addRelation1">
											<option value="mother">Mother</option>
											<option value="father">Father</option>
											<option value="sibling">Sibling</option>
											<option value="other">Other</option>
										</select>
									</div>	
								</div>
								<div class="row mt-2">
									<div class="col-md-12">
										<input type="text" class="form-control" id="addEmail1" name="addEmail1" placeholder="Email">
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
										<input type="text" class="form-control" id="addContact2" name="addContact2" placeholder="Contact No">
									</div>
									<div class="col-md-4">
										<select class="form-control" id="addRelation2" name="addRelation2">
											<option value="mother">Mother</option>
											<option value="father">Father</option>
											<option value="sibling">Sibling</option>
											<option value="other">Other</option>
										</select>
									</div>
								</div>
								<div class="row mt-2">
									<div class="col-md-12">
										<input type="text" class="form-control" id="addEmail2" name="addEmail2" placeholder="Email">
									</div>
								</div>
								</section>
							</div>
						</div>
						<div class="form-row mt-3">
							<div class="col-md-12">
								<label for="addMemo" class="label-form">Memo</label>
								<textarea class="form-control" style="height: 200px;" id="addMemo" name="addMemo"></textarea>
							</div>
						</div>
					</form>
					<div class="d-flex justify-content-end">
						<button type="submit" class="btn btn-primary" onclick="addStudent()">Register</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
					</div>	
				</section>
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
					<header class="text-primary font-weight-bold">Student Edit</header>
						<form id="studentEdit">
						<div class="form-row mt-2">
							<div class="col-md-4">
								<label for="editState" class="label-form">State</label> <select class="form-control" id="editState" name="editState">
								</select>
							</div>
							<div class="col-md-5">
								<label for="editBranch" class="label-form">Branch</label> 
								<select class="form-control" id="editBranch" name="editBranch">
								</select>
							</div>
							<div class="col-md-3">
								<label for="editRegisterDate" class="label-form">Registration</label> 
								<input type="text" class="form-control datepicker" id="editRegisterDate" name="editRegisterDate" placeholder="dd/mm/yyyy">
							</div>
						</div>	
						<div class="form-row mt-2">
							<div class="col-md-3">
								<label for="editId" class="label-form">ID:</label> <input type="text" class="form-control" id="editId" name="editId" readonly>
							</div>
							<div class="col-md-4">
								<label for="editFirstName" class="label-form">First Name:</label> <input type="text" class="form-control" id="editFirstName" name="editFirstName">
							</div>
							<div class="col-md-3">
								<label for="editLastName" class="label-form">Last Name:</label> <input type="text" class="form-control" id="editLastName" name="editLastName">
							</div>
							<div class="col-md-2">
								<label for="editGrade" class="label-form">Grade</label> <select class="form-control" id="editGrade" name="editGrade">
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
								<label for="editGender" class="label-form">Gender</label> <select class="form-control" id="editGender" name="editGender">
									<option value="male">Male</option>
									<option value="female">Female</option>
								</select>
							</div>
							<div class="col-md-9">
								<label for="editAddress" class="label-form">Address</label> <input type="text" class="form-control" id="editAddress" name="editAddress">
							</div>
						</div>
					
						<div class="form-row">
							<div class="col-md-12 mt-4">
								<section class="fieldset rounded" style="padding: 10px;">
									<header class="label-form" style="font-size: 0.9rem!important;">Main Contact</header>
								<div class="row">
									<div class="col-md-8">
										<input type="text" class="form-control" id="editContact1" name="editContact1" placeholder="Contact No">
									</div>
									<div class="col-md-4">
										<select class="form-control" id="editRelation1" name="editRelation1">
											<option value="mother">Mother</option>
											<option value="father">Father</option>
											<option value="sibling">Sibling</option>
											<option value="other">Other</option>
										</select>
									</div>	
								</div>
								<div class="row mt-2">
									<div class="col-md-12">
										<input type="text" class="form-control" id="editEmail1" name="editEmail1" placeholder="Email">
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
										<input type="text" class="form-control" id="editContact2" name="editContact2" placeholder="Contact No">
									</div>
									<div class="col-md-4">
										<select class="form-control" id="editRelation2" name="editRelation2">
											<option value="mother">Mother</option>
											<option value="father">Father</option>
											<option value="sibling">Sibling</option>
											<option value="other">Other</option>
										</select>
									</div>	
								</div>
								<div class="row mt-2">
									<div class="col-md-12">
										<input type="text" class="form-control" id="editEmail2" name="editEmail2" placeholder="Email">
									</div>
								</div>
								</section>
							</div>
						</div>
						<div class="form-row mt-3">
							<div class="col-md-12">
								<label for="editMemo" class="label-form">Memo</label>
								<textarea class="form-control" style="height: 200px;" id="editMemo" name="editMemo"></textarea>
							</div>
						</div>
					</form>					
					<div class="d-flex justify-content-end">
						<button type="submit" class="btn btn-primary" onclick="updateStudentInfo()">Save</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
					</div>
				</section>
			</div>
		</div>
	</div>
</div>

<!--  Password Modal HTML -->
<div id="passwordStudentModal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<form method="POST" action="${pageContext.request.contextPath}/changePassword">
				<div class="modal-header">
					<h4 class="modal-title">Change Password</h4>
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label>Password</label> <input type="password" class="form-control" required="required" name="passwordpassword" id="passwordpassword" />
					</div>
					<div class="form-group">
						<label>Confirm Password</label> <input type="password" class="form-control" required="required" name="confirmPasswordpassword" id="confirmPasswordpassword"/>
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-info" onclick="return passwordChange();">Change Password</button> 
					<input type="button" class="btn btn-secondary" data-dismiss="modal" value="Cancel">
					<input type="hidden" name="usernamepassword" id="usernamepassword" />
				</div>
			</form>
		</div>
	</div>
</div>

<!-- Success Alert -->
<div id="success-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-success alert-dialog-display">
			<i class="bi bi-check-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>

<!-- Warning Alert -->
<div id="warning-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-warning alert-dialog-display">
			<i class="bi bi-exclamation-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>


<!-- Deactivate Dialogue -->
<div class="modal fade" id="deactivateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header btn-danger">
               <h4 class="modal-title text-white" id="myModalLabel"><i class="bi bi-exclamation-circle"></i>&nbsp;&nbsp;Student Suspend</h4>
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <p> Do you want to suspend this student?</p>	
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-danger" onclick="inactivateStudent()"><i class="bi bi-x"></i> Suspend</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="bi bi-check"></i> Close</button>
            </div>
    	</div>
	</div>
</div>

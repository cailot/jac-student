<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
    $('#courseListTable').DataTable({
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
	
	// Set default date format
	$.fn.datepicker.defaults.format = 'dd/mm/yyyy';

	$('.datepicker').datepicker({
		//format: 'dd/mm/yyyy',
		autoclose : true,
		todayHighlight : true
	});
});

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Register Course
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function addCourse() {
	// Get from form data
	var course = {
		name : $("#addName").val(),
		grade : $("#addGrade").val(),
		description : $("#addDescription").val(),
		price : $("#addPrice").val()
	}
	// console.log(course);
	
	// Send AJAX to server
	$.ajax({
		url : '${pageContext.request.contextPath}/class/registerCourse',
		type : 'POST',
		dataType : 'json',
		data : JSON.stringify(course),
		contentType : 'application/json',
		success : function(response) {
			// console.log(response);
			// Display the success alert
            $('#success-alert .modal-body').text('New Class is registered successfully.');
            $('#success-alert').modal('show');
			$('#success-alert').on('hidden.bs.modal', function(e) {
				location.reload();
			});
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
	$('#registerCourseModal').modal('hide');
	// flush all registered data
	document.getElementById("courseRegister").reset();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Retrieve Course
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function retrieveCourseInfo(courseId) {
	// console.log(courseId);
	// send query to controller
	$.ajax({
		url : '${pageContext.request.contextPath}/class/get/course/' + courseId,
		type : 'GET',
		success : function(course) {
			console.log(course);
			$("#editId").val(course.id);
			$("#editGrade").val(course.grade);
			$("#editName").val(course.name);
			$("#editPrice").val(course.price);
			$("#editDescription").val(course.description);
			$('#editClassModal').modal('show');
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Update Course
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function updateCourseInfo(){
	var courseId = $("#editId").val();
	// get from formData
	var course = {
		id : courseId,
		name : $("#editName").val(),
		grade : $("#editGrade").val(),
		price : $("#editPrice").val(),
		description : $("#editDescription").val()
	}
	
	console.log(course);
	// send query to controller
	$.ajax({
		url : '${pageContext.request.contextPath}/class/update/course',
		type : 'PUT',
		dataType : 'json',
		data : JSON.stringify(course),
		contentType : 'application/json',
		success : function(value) {
			// Display success alert
			$('#success-alert .modal-body').text('ID : ' + courseId + ' is updated successfully.');
			$('#success-alert').modal('show');
			$('#success-alert').on('hidden.bs.modal', function(e) {
				location.reload();
			});
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
	
	$('#editClassModal').modal('hide');
	// flush all registered data
	clearCourseForm("courseEdit");
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Clear class register form
/////////////////////////////////////////////////////////////////////////////////////////////////////////	
function clearCourseForm(elementId) {
	document.getElementById(elementId).reset();
}


</script>
<!-- List Body -->
<div class="row">
	<div class="modal-body">
		<form id="courseList" method="get" action="${pageContext.request.contextPath}/class/listCourse">
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-3">
						<select class="form-control" id="listGrade" name="listGrade">
							<option value="All">All Grade</option>
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
					<div class="offset-md-4"></div>
					<div class="col mx-auto">
						<button type="submit" class="btn btn-primary btn-block"> <i class="bi bi-search"></i>&nbsp;Search</button>
					</div>
					<div class="col mx-auto">
						<button type="button" class="btn btn-block btn-success" data-toggle="modal" data-target="#registerCourseModal"><i class="bi bi-plus"></i>&nbsp;New</button>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						<div class="table-wrap">
							<table id="courseListTable" class="table table-striped table-bordered"><thead class="table-primary">
									<tr>
										<th>Name</th>
										<th>Description</th>
										<th>Grade</th>
										<th>Price</th>
										<th data-orderable="false">Action</th>
									</tr>
								</thead>
								<tbody id="list-class-body">
								<c:choose>
									<c:when test="${CourseList != null}">
										<c:forEach items="${CourseList}" var="course">
											<tr>
												<td class="small ellipsis"><span><c:out value="${course.name}" /></span></td>
												<td class="small ellipsis"><span><c:out value="${course.description}" /></span></td>
												<td class="small ellipsis"><span><c:out value="${fn:toUpperCase(course.grade)}" /></span></td>
												<td class="small ellipsis"><span><c:out value="${course.price}" /></span></td>
												<td class="text-center">
													<i class="bi bi-pencil-square text-primary fa-lg" data-toggle="tooltip" title="Edit" onclick="retrieveCourseInfo('${course.id}')"></i>&nbsp;
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

<!-- Add Form Dialogue -->
<div class="modal fade" id="registerCourseModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
					<header class="text-primary font-weight-bold">Course Registration</header>
					<form id="courseRegister">
						<div class="form-group mt-3">
							<div class="form-row">
								<div class="col-md-3">
									<label for="addGrade" class="label-form">Grade</label>
									<select class="form-control" id="addGrade" name="addGrade">
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
								<div class="col-md-9">
									<label for="addName" class="label-form">Name</label> 
									<input type="text" class="form-control" id="addName" name="addName" placeholder="Name" title="Please enter Course name">
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="form-row">
								<div class="col-md-3">
									<label for="addPrice" class="label-form">Price</label> 
									<input type="text" class="form-control" id="addPrice" name="addPrice" placeholder="Price" title="Please enter Course price">
								</div>
								<div class="col-md-9">
									<label for="addDescription" class="label-form">Description</label> 
									<input type="text" class="form-control" id="addDescription" name="addDescription" placeholder="Description" title="Please enter Course description">
								</div>
							</div>
						</div>
					</form>
					<div class="d-flex justify-content-end">
						<button type="submit" class="btn btn-primary" onclick="addCourse()">Create</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-default btn-secondary" onclick="clearCourseForm('courseRegister')" data-dismiss="modal">Close</button>	
					</div>	
				</section>
			</div>
		</div>
	</div>
</div>

<!-- Edit Form Dialogue -->
<div class="modal fade" id="editClassModal" tabindex="-1" role="dialog" aria-labelledby="modalEditLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
					<header class="text-primary font-weight-bold">Class Edit</header>
			
				<form id="courseEdit">
					<div class="form-group">
						<div class="form-row">
							<div class="col-md-3">
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
							<div class="col-md-9">
								<label for="editName" class="label-form">Name</label> 
								<input type="text" class="form-control" id="editName" name="editName">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="form-row">
							<div class="form-group">
								<div class="form-row">
									<div class="col-md-3">
										<label for="editPrice" class="label-form">Price</label> 
										<input type="text" class="form-control" id="editPrice" name="editPrice">
									</div>
									<div class="col-md-9">
										<label for="editDescription" class="label-form">Description</label> 
										<input type="text" class="form-control" id="editDescription" name="editDescription">
									</div>
								</div>
							</div>
						</div>
					</div>
					<input type="hidden" id="editId" name="editId">
				</form>
				<div class="d-flex justify-content-end">
					<button type="submit" class="btn btn-primary" onclick="updateCourseInfo()">Save</button>&nbsp;&nbsp;
					<button type="button" class="btn btn-default btn-secondary" data-dismiss="modal">Close</button>	
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
			<i class="bi bi-check-circle fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		</div>
	</div>
</div>
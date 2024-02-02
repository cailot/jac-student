<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.Calendar" %>

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
		$('#cycleListTable').DataTable({
			language: {
				search: 'Filter:'
			},
		});

		$("#addStartDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#addEndDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#addVacationStartDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#addVacationEndDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#editStartDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#editEndDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#editVacationStartDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});
		$("#editVacationEndDate").datepicker({
			dateFormat: 'dd/mm/yy'
		});

	});

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//		Register Cycle
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	function addCycle() {
		// Get from form data
		var cycle = {
			year: $("#addYear").val(),
			description: $("#addDescription").val(),
			startDate: $("#addStartDate").val(),
			endDate: $("#addEndDate").val(),
			vacationStartDate: $("#addVacationStartDate").val(),
			vacationEndDate: $("#addVacationEndDate").val()
		}
		// Send AJAX to server
		$.ajax({
			url: '${pageContext.request.contextPath}/class/registerCycle',
			type: 'POST',
			dataType: 'json',
			data: JSON.stringify(cycle),
			contentType: 'application/json',
			success: function (student) {
				// Display the success alert
				$('#success-alert .modal-body').text(
					'New Cycle is registered successfully.');
				$('#success-alert').modal('show');
				$('#success-alert').on('hidden.bs.modal', function (e) {
					location.reload();
				});
			},
			error: function (xhr, status, error) {
				console.log('Error : ' + error);
			}
		});
		$('#registerCycleModal').modal('hide');
		// flush all registered data
		document.getElementById("cycleRegister").reset();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//		Retrieve Cycle
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	function retrieveCycleInfo(cycleId) {
		// send query to controller
		$.ajax({
			url: '${pageContext.request.contextPath}/class/get/cycle/' + cycleId,
			type: 'GET',
			success: function (cycle) {
				// console.log(cycle);
				$("#editId").val(cycle.id);
				$("#editYear").val(cycle.year);
				$("#editDescription").val(cycle.description);
				// Set date value
				var startDate = new Date(cycle.startDate); // Replace with your date value
				$("#editStartDate").datepicker('setDate', startDate);
				var endDate = new Date(cycle.endDate); // Replace with your date value
				$("#editEndDate").datepicker('setDate', endDate);
				var vacationStartDate = new Date(cycle.vacationStartDate); // Replace with your date value
				$("#editVacationStartDate").datepicker('setDate', vacationStartDate);
				var vacationEndDate = new Date(cycle.vacationEndDate); // Replace with your date value
				$("#editVacationEndDate").datepicker('setDate', vacationEndDate);
				// show dialog
				$('#editCycleModal').modal('show');
			},
			error: function (xhr, status, error) {
				console.log('Error : ' + error);
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//		Update Cycle
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	function updateCycleInfo() {
		var cycleId = $("#editId").val();
		// get from formData
		var cycle = {
			id: cycleId,
			year: $("#editYear").val(),
			description: $("#editDescription").val(),
			startDate: $("#editStartDate").val(),
			endDate: $("#editEndDate").val(),
			vacationStartDate: $("#editVacationStartDate").val(),
			vacationEndDate: $("#editVacationEndDate").val()
		}

		//console.log(cycle);
		// send query to controller
		$.ajax({
			url: '${pageContext.request.contextPath}/class/update/cycle',
			type: 'PUT',
			dataType: 'json',
			data: JSON.stringify(cycle),
			contentType: 'application/json',
			success: function (value) {
				// Display success alert
				$('#success-alert .modal-body').text(
					'ID : ' + cycleId + ' is updated successfully.');
				$('#success-alert').modal('show');
				$('#success-alert').on('hidden.bs.modal', function (e) {
					location.reload();
				});
			},
			error: function (xhr, status, error) {
				console.log('Error : ' + error);
			}
		});

		$('#editCycleModal').modal('hide');
		// flush all registered data
		clearFormData("cycleEdit");
	}

</script>

<!-- List Body -->
<div class="row">
	<div class="modal-body">
		<form id="classList" method="get" action="${pageContext.request.contextPath}/class/listCycle">
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-2">
						<select class="form-control" id="listYear" name="listYear">
							<%
								Calendar now = Calendar.getInstance();
								int currentYear = now.get(Calendar.YEAR);
								int nextYear = currentYear + 1;
							%>
							<option value="All">All</option>
							<option value="<%= nextYear %>"><%= nextYear %></option>
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
					<div class="offset-md-6"></div>
					<div class="col mx-auto">
						<button type="submit" class="btn btn-primary btn-block"> <i
								class="bi bi-search"></i>&nbsp;Search</button>
					</div>
					<div class="col mx-auto">
						<button type="button" class="btn btn-block btn-success" data-toggle="modal" data-target="#registerCycleModal">
							<i class="bi bi-plus"></i>&nbsp;New</button>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						<div class="table-wrap">
							<table id="cycleListTable" class="table table-striped table-bordered">
								<thead class="table-primary">
									<tr>
										<th>Year</th>
										<th>Description</th>
										<th>Start Date</th>
										<th>End Date</th>
										<th>Vacation Start</th>
										<th>Vacation End</th>
										<th data-orderable="false">Action</th>
									</tr>
								</thead>
								<tbody id="list-class-body">
									<c:choose>
										<c:when test="${CycleList != null}">
											<c:forEach items="${CycleList}" var="cycle">
												<tr>
													<td class="small ellipsis">
														<c:out value="${cycle.year}" />
													</td>
													<td class="small ellipsis"><span
															style="text-transform: capitalize;">
															<c:out value="${cycle.description}" />
														</span></td>
													<td class="small ellipsis">
														<fmt:parseDate var="cycleStartDate" value="${cycle.startDate}" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${cycleStartDate}" pattern="dd/MM/yyyy" />
													</td>
													<td class="small ellipsis">
														<fmt:parseDate var="cycleEndDate" value="${cycle.endDate}" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${cycleEndDate}" pattern="dd/MM/yyyy" />
													</td>
													<td class="small ellipsis">
														<fmt:parseDate var="cycleVacationStartDate" value="${cycle.vacationStartDate}" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${cycleVacationStartDate}" pattern="dd/MM/yyyy" />
													</td>
													<td class="small ellipsis">
														<fmt:parseDate var="cycleVactionEndDate" value="${cycle.vacationEndDate}" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${cycleVactionEndDate}" pattern="dd/MM/yyyy" />
													</td>
													<td class="text-center">
														<i class="bi bi-pencil-square text-primary fa-lg"
															data-toggle="tooltip" title="Edit"
															onclick="retrieveCycleInfo('${cycle.id}')"></i>&nbsp;
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
<div class="modal fade" id="registerCycleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
	aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
					<header class="text-primary font-weight-bold">Adademic Cycle Registration</header>

					<form id="cycleRegister">
						<div class="form-row mt-3">
							<div class="offset-md-1"></div>
							<div class="col-md-3">
								<label for="addYear" class="label-form">Academic Year</label> 
								<select class="form-control" id="addYear" name="addYear">
								<option value="<%= nextYear %>"><%= nextYear %></option>
								<option value="<%= currentYear %>"><%= currentYear %></option>
								<%
									for (int i = currentYear - 1; i >= currentYear - 3; i--) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
									}
								%>
								</select>
							</div>
							<div class="col-md-7">
								<label for="addDescription" class="label-form">Description</label> 
								<input type="text" class="form-control" id="addDescription" name="addDescription" placeholder="Description" title="Please enter cycle description">
							</div>
							<div class="offset-md-1"></div>
						</div>
						<div class="form-row mt-3">
							<div class="offset-md-1"></div>
							<div class="col-md-4">
								<label for="addStartDate" class="label-form">Start Date</label>
								<input type="text" class="form-control datepicker" id="addStartDate" name="addStartDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-2"></div>
							<div class="col-md-4">
								<label for="addEndDate" class="label-form">End Date</label>
								<input type="text" class="form-control datepicker" id="addEndDate" name="addEndDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-1"></div>
						</div>
						<div class="form-row mt-3">
							<div class="offset-md-1"></div>
							<div class="col-md-4">
								<label for="addVacationStartDate" class="label-form">Vacation Start</label>
								<input type="text" class="form-control datepicker" id="addVacationStartDate" name="addVacationStartDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-2"></div>
							<div class="col-md-4">
								<label for="addVacationEndDate" class="label-form">Vacation End</label>
								<input type="text" class="form-control datepicker" id="addVacationEndDate" name="addVacationEndDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-1"></div>
						</div>
						<script>
							var today = new Date();
							var day = today.getDate();
							var month = today.getMonth() + 1; // Note: January is 0
							var year = today.getFullYear();
							var formattedDate = (day < 10 ? '0' : '') + day + '/' + (month < 10 ? '0' : '') + month + '/' + year;
							document.getElementById('addStartDate').value = formattedDate;
							document.getElementById('addEndDate').value = formattedDate;
							document.getElementById('addVacationStartDate').value = formattedDate;
							document.getElementById('addVacationEndDate').value = formattedDate;
						</script>
					</form>
					<div class="d-flex justify-content-end">
						<button type="submit" class="btn btn-primary" onclick="addCycle()">Create</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-default btn-secondary" onclick="clearFormData('cycleRegister')" data-dismiss="modal">Close</button>
					</div>
				</section>
			</div>
		</div>
	</div>
</div>

<!-- Edit Form Dialogue -->
<div class="modal fade" id="editCycleModal" tabindex="-1" role="dialog" aria-labelledby="modalEditLabel"
	aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
					<header class="text-primary font-weight-bold">Academic Cycle Edit</header>

					<form id="cycleEdit">
						<div class="form-row mt-3">
							<div class="offset-md-1"></div>
							<div class="col-md-3">
								<label for="editYear" class="label-form">Academic Year</label> 
								<select class="form-control" id="editYear" name="editYear">
								<option value="<%= nextYear %>"><%= nextYear %></option>
								<option value="<%= currentYear %>"><%= currentYear %></option>
								<%
									for (int i = currentYear - 1; i >= currentYear - 5; i--) {
								%>
								<option value="<%= i %>"><%= i %></option>
								<%
									}
								%>
								</select>
							</div>
							<div class="col-md-7">
								<label for="editDescription" class="label-form">Description</label> 
								<input type="text" class="form-control" id="editDescription" name="editDescription" placeholder="Description" title="Please enter cycle description">
							</div>
							<div class="offset-md-1"></div>
						</div>
						<div class="form-row mt-3">
							<div class="offset-md-1"></div>
							<div class="col-md-4">
								<label for="editStartDate" class="label-form">Start Date</label>
								<input type="text" class="form-control datepicker" id="editStartDate" name="editStartDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-2"></div>
							<div class="col-md-4">
								<label for="editEndDate" class="label-form">End Date</label>
								<input type="text" class="form-control datepicker" id="editEndDate" name="editEndDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-1"></div>
						</div>
						<div class="form-row mt-3">
							<div class="offset-md-1"></div>
							<div class="col-md-4">
								<label for="editVacationStartDate" class="label-form">Vacation Start</label>
								<input type="text" class="form-control datepicker" id="editVacationStartDate" name="editVacationStartDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-2"></div>
							<div class="col-md-4">
								<label for="editVacationEndDate" class="label-form">Vacation End</label>
								<input type="text" class="form-control datepicker" id="editVacationEndDate" name="editVacationEndDate" placeholder="dd/mm/yyyy">
							</div>
							<div class="offset-md-1"></div>
						</div>
						<input type="hidden" id="editId" name="editId">
					</form>
					<div class="d-flex justify-content-end">
						<button type="submit" class="btn btn-primary" onclick="updateCycleInfo()">Save</button>&nbsp;&nbsp;
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
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
    $('#studentInvoiceTable').DataTable({
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
		order : [[1, 'desc'], [0, 'desc']], // order by invoiceId desc, id desc
		// sum for paid
		footerCallback: function (row, data, start, end, display) {
    		var api = this.api();
    		// Custom function to parse and sum values
			var parseAndSum = function (data) {
				var total = 0;
				for (var i = 0; i < data.length; i++) {
					var value = parseFloat(data[i].replace(/[^\d.-]/g, ''));
					if (!isNaN(value)) {
						total += value;
					}
				}
				return total;
			};
			// Total over all pages
			var totalOutstanding = parseAndSum(api.column(6, { search: 'applied' }).data());
			// Update footer
			$(api.column(6).footer()).html('Total Paid : <span class="text-primary">$' + totalOutstanding.toFixed(2) + '</span>');
		}		
    });
	// initialise state list when loading
	listState('#listState');
	listBranch('#listBranch');
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
//			Search Student with Keyword	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
function searchStudent() {
	// debugger;
	//warn if keyword is empty
	if ($("#studentKeyword").val() == '') {
		$('#warning-alert .modal-body').text('Please fill in Student Info before search');
		$('#warning-alert').modal('toggle');
		return;
	}
	// send query to controller
	$('#studentListResultTable tbody').empty();
	$.ajax({
		url : '${pageContext.request.contextPath}/student/search',
		type : 'GET',
		data : {
			keyword : $("#studentKeyword").val()
		},
		success : function(data) {
			// console.log('search - ' + data);
			if (data == '') {
				$('#warning-alert .modal-body').html('No record found with <b>' + $("#studentKeyword").val() + '</b>');
				$('#warning-alert').modal('toggle');
				clearStudentInfo();
				return;
			}
			$.each(data, function(index, value) {
				const cleaned = cleanUpJson(value);
				var row = $("<tr onclick='getInvoice(" + value.id + ")'>");		
				row.append($('<td>').text(value.id));
				row.append($('<td>').text(value.firstName));
				row.append($('<td>').text(value.lastName));
				row.append($('<td>').text(value.grade.toUpperCase()));
				row.append($('<td>').text((value.gender === "") ? "" : value.gender.slice(0, 1).toUpperCase() + value.gender.substring(1)));	
				row.append($('<td>').text(formatDate(value.registerDate)));
				row.append($('<td>').text(formatDate(value.endDate)));
				row.append($('<td>').text(value.email1));
				row.append($('<td>').text(value.contactNo1));
				row.append($('<td>').text(value.email2));
				row.append($('<td>').text(value.contactNo2));
				row.append($('<td>').text(value.address));
				$('#studentListResultTable > tbody').append(row);
			});
			$('#studentListResult').modal('show');
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//		Bring all Invoice by Student
////////////////////////////////////////////////////////////////////////////////////////////////////
function getInvoice(studentId) {
	$("#studentKeyword").val(studentId);
	$('#studentListResult').modal('hide');
	//warn if keyword is empty
	if (studentId == '') {
		$('#warning-alert .modal-body').text('Please fill in Student Info before search');
		$('#warning-alert').modal('toggle');
		return;
	}
	var form = document.getElementById("studentInvoice");
	form.submit();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
//		Display Payment History in another tab
//////////////////////////////////////////////////////////////////////////////////////////////////////
function displayPaymentHistory(studentId, firstName, lastName, invoiceId, paymentId){
	var url = '/invoice/receiptInfo?studentId=' + studentId + '&firstName=' + firstName + '&lastName=' + lastName + '&invoiceId=' + invoiceId + '&paymentId=' + paymentId;  
	var win = window.open(url, '_blank');
	win.focus();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
//		Display Add Modal
//////////////////////////////////////////////////////////////////////////////////////////////////////
function displayAddInfo(dataId, dataInfo){
    // console.log('displayAddInfo dataType : ' + dataType + ', dataId : ' + dataId);
	// document.getElementById("infoDataType").value = dataType;
	document.getElementById("infoDataId").value = dataId;
	document.getElementById("information").value = dataInfo;
	// display Receivable amount
    $('#infoModal').modal('toggle');
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
//		Add Information
//////////////////////////////////////////////////////////////////////////////////////////////////////
function addInformation(){
	var dataId = $('#infoDataId').val();
	var info = $('#information').val();
	
	let encodeInfo = encodeDecodeString(info).encoded;
	// console.log('addInformation check : ' + encodeInfo);

	$.ajax({
		url : '${pageContext.request.contextPath}/invoice/updateInfo/Payment/' + dataId,
		type : 'POST',
		data : encodeInfo,
		contentType : 'application/json',
		success : function(response) {
			// flush old data in the dialogue
			document.getElementById('showInformation').reset();
			// disappear information dialogue
			$('#infoModal').modal('toggle');
			// update memo <td> in invoiceListTable 
			$('#studentInvoiceTable > tbody > tr').each(function() {
				if ($(this).find('.payment-id').text() === dataId) {
					(isNotBlank(info)) ? $(this).find('.memo').html('<i class="bi bi-chat-square-text-fill text-primary" title="Internal Memo" onclick="displayAddInfo(' + dataId + ', \'' + encodeInfo + '\')"></i>') : $(this).find('.memo').html('<i class="bi bi-chat-square-text text-primary" title="Internal Memo" onclick="displayAddInfo(' + dataId + ', \'\')"></i>');		
				}
			});
		},
		error : function(xhr, status, error) {
			console.log('Error : ' + error);
		}
	});						
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//		Clear Student Info	
////////////////////////////////////////////////////////////////////////////////////////////////////
function clearStudentInfo() {
	$("#studentKeyword").val('');
	document.getElementById("studentInvoice").reset();
	var studentInfoDiv = document.getElementById("studentInfo");
	if (studentInfoDiv) {
  		studentInfoDiv.innerHTML = '';
		studentInfoDiv.style.display = 'none';
	}
	//studentInvoiceTable all rows remove
	$('#studentInvoiceTable').DataTable().clear().draw();
}

</script>

<style>
	#studentInvoiceTable th, tr {
		padding: 15px;
	}
	#studentInvoiceTable tfoot tr th {
    	border: none !important;
	}
	#studentInvoice .form-row {
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
	tr { height: 50px } 


</style>


<!-- List Body -->
<div class="row">
	<div class="modal-body">
		<form id="studentInvoice" method="get" action="${pageContext.request.contextPath}/invoice/history">
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-2">
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
					<div class="col-md-2">
						<label for="studentKeyword" class="label-form">Student Info</label> 
						<input type="text" class="form-control" style="background-color: #FCF7CA;" id="studentKeyword" name="studentKeyword" placeholder="Name or ID">
					</div>
					<!-- put blank col-md-2 -->
					<div class="offset-md-2">
					</div>
					<div class="col-md-2">
						<label class="label-form-white">Search</label> 
						<button type="button" class="btn btn-primary btn-block" onclick="return searchStudent()"><i class="bi bi-search"></i>&nbsp;&nbsp;Search</button>
					</div>
					<div class="col-md-2">
						<label class="label-form-white">Clear</label> 
						<button type="button" class="btn btn-block btn-success" onclick="clearStudentInfo()"><i class="bi bi-arrow-clockwise"></i>&nbsp;&nbsp;Clear</button>
					</div>
				</div>
			</div>
			<!-- Student Info-->
			<c:if test="${studentInfo != null}">
				<div id="studentInfo" class="alert alert-info">
					<c:set var="student" value="${studentInfo}" />
					<c:set var="studentId" value="${student.id}" />
					<c:set var="studentFirstName" value="${student.firstName}" />
					<c:set var="studentLastName" value="${student.lastName}" />
					<table style="width: 100%;">
						<colgroup>
							<col style="width: 33.33%;" />
							<col style="width: 33.33%;" />
							<col style="width: 33.33%;" />
						</colgroup>
						<tr>
							<td class="text-right">Student ID : <span class="font-weight-bold"><c:out value="${student.id}" /></span></td>
							<td class="text-center">Name : <span class="font-weight-bold"><c:out value="${student.firstName} ${student.lastName}" /></span></td>
							<td class="text-left">Grade : <span class="font-weight-bold text-uppercase"><c:out value="${student.grade}" /></span></td>
						</tr>
					</table>						
				</div>
			</c:if>
			<div class="form-group">
				<div class="form-row">
					<div class="col-md-12">
						<div class="table-wrap">
							<table id="studentInvoiceTable" class="table table-striped table-bordered" style="width: 100%;">
								<thead class="table-primary">
									<tr>
										<th style="display: none;">ID</th>
										<th class="small align-middle text-center">Invoice ID</th>
										<th class="small align-middle text-center">Payment Date</th>
										<th class="small align-middle text-center">Method</th>
										<th class="small align-middle text-center">Total</th>
										<th class="small align-middle text-center">Remaining</th>
										<th class="small align-middle text-center">Paid</th>
										<th class="small align-middle text-center">Enrolled Course Information</th>
										<th class="small align-middle text-center" data-orderable="false">Note</th>
										<th class="small align-middle text-center" data-orderable="false">Receipt</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${payments != null}">
										<c:forEach var="payment" items="${payments}">
											<tr>
												<td class="payment-id" style="display: none;">${payment.id}</td> <!-- invisible -->
												<td class="small align-middle text-center">${payment.invoiceId}</td>
												<td class="small align-middle text-center"> <!-- payment date with dd/MM/yyyy format -->
													<fmt:parseDate var="parsedDate" value="${payment.registerDate}" pattern="yyyy-MM-dd" />
													<fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
												</td>
												<td class="small text-capitalize align-middle"> <!-- payment method with first letter capitalized -->
													<c:out value="${payment.method}"/>	
												</td>
												<td class="small align-middle text-right"> <!-- payment total with 2 decimal places -->
													<fmt:formatNumber value="${payment.total}" pattern="#0.00" />
												</td>
												<td class="small align-middle text-right"> <!-- payment outstanding with 2 decimal places -->
													<fmt:formatNumber value="${payment.total - payment.amount}" pattern="#0.00" />
												</td>
												<td class="small align-middle text-right"> 
													<fmt:formatNumber value="${payment.amount}" pattern="#0.00" />
												</td>
												<!-- Display a property of each object -->
												<td class="small align-middle" style="white-space: nowrap; padding: 0px;">
													<table class="table-borderless">
														<c:forEach var="enrol" items="${payment.enrols}">
														<!-- if enrol is not free online, then display -->
														<c:if test="${enrol.online != true and enrol.discount != '100%'}">		
															<tr style="background-color : transparent !important;">
																<td class="small align-middle" style="white-space: nowrap;">[${enrol.grade.toUpperCase()}] ${enrol.name}&nbsp;</td>
																<td class="small align-middle" style="white-space: nowrap;">(${enrol.extra})</td>
															</tr>
														</c:if>
														</c:forEach>
													</table>
												</td>
												<c:set var="info" value="${payment.info}" />
												<td class="text-center align-middle memo">
													<!--check ${payment.info} is empty or not -->
													<c:choose>
														<c:when test="${not empty info}">
															<i class="bi bi-chat-square-text-fill text-primary" data-toggle="Note" title="Internal Memo" onclick="displayAddInfo('${payment.id}', '${payment.info}')"></i>
														</c:when>
														<c:otherwise>
															<i class="bi bi-chat-square-text text-primary" data-toggle="Note" title="Internal Memo" onclick="displayAddInfo('${payment.id}', '${payment.info}')"></i>
														</c:otherwise>
													</c:choose>
												</td>																						
												<td class="text-center align-middle">
													<i class="bi bi-calculator text-success" data-toggle="tooltip" title="Receipt" onclick="displayPaymentHistory('${studentId}', '${studentFirstName}', '${studentLastName}', '${payment.invoiceId}', '${payment.id}')"></i>
												</td> 
											</tr>
										</c:forEach>
									</c:if>
								</tbody>
								<tfoot>
									<tr>
										<th></th>
										<th></th>
										<th colspan="2"></th>
										<th colspan="3" class="text-right small"></th>
										<th colspan="2" class="text-right small"></th>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
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

<!-- Search Result Dialog -->
<div class="modal fade" id="studentListResult">
	<div class="modal-dialog modal-xl modal-dialog-centered">
	  <div class="modal-content">
		<div class="modal-header bg-primary text-white">
		  <h5 class="modal-title">&nbsp;<i class="bi bi-card-list"></i>&nbsp;&nbsp; Student List</h5>
		  <button type="button" class="close" data-dismiss="modal">
			<span>&times;</span>
		  </button>
		</div>
		<div class="modal-body table-wrap">
		  <table class="table table-striped table-bordered" id="studentListResultTable" data-header-style="headerStyle" style="font-size: smaller;">
			<thead class="thead-light">
			  <tr>
				<th data-field="id">ID</th>
				<th data-field="firstname">First Name</th>
				<th data-field="lastname">Last Name</th>
				<th data-field="grade">Grade</th>
				<th data-field="grade">Gender</th>
				<th data-field="startdate">Start Date</th>
				<th data-field="enddate">End Date</th>
				<th data-field="email">Main Email</th>
				<th data-field="contact1">Main Contact</th>
				<th data-field="email">Sub Email</th>
				<th data-field="contact2">Sub Contact</th>
				<th data-field="address">Address</th>
			  </tr>
			</thead>
			<tbody>
			</tbody>
		  </table>
		</div>
		<div class="modal-footer">
		  <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		</div>
	  </div>
	</div>
</div>

<!-- Info Dialogue -->
<div class="modal fade" id="infoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<section class="fieldset rounded border-primary">
				<header class="text-primary font-weight-bold">Information</header>
				<br>
				Please Add Internal Information
				<form id="showInformation">
					<div class="form-row mt-4">
						<div class="col-md-12">
							<textarea class="form-control" id="information" name="information" style="height: 8rem;"></textarea>
						</div>
					</div>
					<!-- <input type="hidden" id="infoDataType" name="infoDataType"></input> -->
					<input type="hidden" id="infoDataId" name="infoDataId"></input>
					<div class="d-flex justify-content-end mt-4">
						<button type="button" class="btn btn-primary" onclick="addInformation()">Save</button>&nbsp;&nbsp;
						<button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="document.getElementById('showInformation').reset();">Cancel</button>
					</div>
				</form>	
				</section>
			</div>
		</div>
	</div>
</div>




<style>
	.table-wrap {
	  overflow-x: auto;
	}
	#studentListResultTable th, #studentListResultTable td { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
	.form-group{
		margin-bottom: 30px;
	}
</style>

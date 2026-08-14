<style>
    .assessment-container {
        background: #f9f9f9; /* Softer background color */
        border-radius: 12px;
        padding: 2.5rem;
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
        max-width: 600px;
        width: 100%;
        text-align: center;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* Modern and clean font */
        margin: 2rem auto;
    }

    .assessment-container h1 {
        font-size: 2.2rem;
        margin-bottom: 1.5rem;
        font-weight: 700;
        color: #333; /* Darker color for better contrast */
    }

    .form-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 1.5rem;
    }

    .assessment-container select.form-control {
        width: 100%; /* Ensures dropdown takes full width of the container */
        min-width: 150px; /* Minimum width to ensure contents are visible */
        max-width: 300px; /* Maximum width for better control */
        padding: 0.5rem; /* Adequate padding */
        border-radius: 8px;
        border: 1px solid #ddd;
        font-size: 1rem;
        transition: border-color 0.3s ease;
    }

    .form-group {
        flex: 1;
        margin-right: 1rem;
        min-width: 150px; /* Minimum width for dropdowns to ensure content is visible */
    }

    .form-group:last-child {
        margin-right: 0; /* Removes margin for the last item */
    }
    
    .label-form {
        display: block;
        font-size: 0.75rem;
        font-weight: 500;
        margin-bottom: 0.5rem;
        color: #2D398E; /* Softer text color for labels */
        text-align: left;
    }

    .form-control {
        width: 100%;
        padding: 0.75rem;
        border-radius: 8px;
        border: 1px solid #ddd;
        font-size: 1rem;
        transition: border-color 0.3s ease;
    }

    .form-control:focus {
        border-color: #007bff; /* Blue border on focus */
        outline: none;
        box-shadow: 0 0 8px rgba(0, 123, 255, 0.2);
    }

    .btn-primary {
        font-size: 1rem;
        padding: 0.75rem 2.5rem;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 25px;
        cursor: pointer;
        transition: background-color 0.3s ease, transform 0.3s ease;
        box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
    }

    .btn-primary:hover {
        background-color: #0056b3;
        transform: translateY(-2px);
        box-shadow: 0 6px 12px rgba(0, 123, 255, 0.3);
    }

    .btn-primary:active {
        transform: translateY(0);
        box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
    }

    .mt-4 {
        margin-top: 1.5rem;
    }

    .mb-5 {
        margin-bottom: 2.5rem;
    }

    /* Confirmation Modal Styles */
    .confirmation-details {
        background-color: #f8f9fa;
        border-radius: 8px;
        padding: 1.5rem;
        margin: 1rem 0;
        border-left: 4px solid #007bff;
    }

    .confirmation-details p {
        margin-bottom: 0.75rem;
        font-size: 1.3rem;
    }

    .confirmation-details p:last-child {
        margin-bottom: 0;
    }

    .highlight-text {
        color: #007bff;
        font-weight: 700;
        font-size: 1.5rem;
    }

    .modal-header {
        background-color: #007bff;
        color: white;
        border-radius: 8px 8px 0 0;
    }

    .modal-header .modal-title {
        font-size: 1.4rem;
        font-weight: 600;
    }

    .modal-header .close {
        color: white;
        opacity: 0.8;
        font-size: 1.5rem;
    }

    .modal-header .close:hover {
        opacity: 1;
    }

    .modal-content {
        border-radius: 8px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        border: 3px solid #007bff;
    }

    .modal-body {
        padding: 2rem;
        font-size: 1.1rem;
    }

    .modal-footer {
        border-top: 1px solid #e9ecef;
        padding: 1rem 2rem;
    }

    .btn-confirm {
        background-color: #007bff !important;
        border-color: #007bff !important;
        color: #ffffff !important;
        padding: 0.5rem 2rem !important;
        font-weight: 600 !important;
        font-size: 1rem !important;
        border-radius: 4px !important;
        width: 140px !important;
        height: 45px !important;
        display: inline-flex !important;
        align-items: center !important;
        justify-content: center !important;
        gap: 8px !important;
        line-height: 1 !important;
        white-space: nowrap !important;
    }

    .btn-confirm:hover {
        background-color: #0056b3 !important;
        border-color: #0056b3 !important;
        transform: translateY(-1px) !important;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
    }

    .btn-cancel {
        background-color: #6c757d !important;
        border-color: #6c757d !important;
        color: #ffffff !important;
        padding: 0.5rem 2rem !important;
        font-weight: 600 !important;
        font-size: 1rem !important;
        border-radius: 4px !important;
        width: 140px !important;
        height: 45px !important;
        display: inline-flex !important;
        align-items: center !important;
        justify-content: center !important;
        gap: 8px !important;
        line-height: 1 !important;
        white-space: nowrap !important;
    }

    .btn-cancel:hover {
        background-color: #5a6268 !important;
        border-color: #545b62 !important;
        transform: translateY(-1px) !important;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
    }
</style>

<div class="assessment-container">
    <h1>Online Assessment</h1>
    <div class="form-row">
        <div class="form-group">
            <label for="listState" class="label-form">State</label>
            <select id="listState" name="listState" class="form-control" disabled>
                <option value="1">Victoria</option>
            </select>
        </div>
        <div class="form-group">
            <label for="listBranch" class="label-form">Branch</label>
            <select id="listBranch" name="listBranch" class="form-control">
                <option value="25">Balwyn</option>
                <option value="28">Bayswater</option>
                <option value="12">Box Hill</option>
                <option value="13">Braybrook</option>
                <option value="27">Caroline Springs</option>
                <option value="36">Clyde</option>
                <option value="30">Craigieburn</option>
                <option value="15">Cranbourne</option>
                <option value="16">Epping</option>
                <option value="33">Glenroy</option>
                <option value="17">Glen Waverley</option>
                <option value="32">Melton</option>
                <option value="31">Mernda</option>
                <option value="19">Mitcham</option>
                <option value="18">Narre Warren</option>
                <option value="34">Pakenham</option>
                <option value="29">Point Cook</option>
                <option value="20">Preston</option>
                <option value="22">Springvale</option>
                <option value="23">St.Albans</option>
                <option value="35">Tarneit</option>
                <option value="24">Werribee</option>               
            </select>
        </div>
        <div class="form-group">
            <label for="listGrade" class="label-form">Year</label>
            <select id="listGrade" name="listGrade" class="form-control">
                <option value="10">Year 1</option>
                <option value="1">Year 2</option>
                <option value="2">Year 3</option>
                <option value="3">Year 4</option>
                <option value="4">Year 5</option>
                <option value="5">Year 6</option>
                <option value="6">Year 7</option>
                <option value="7">Year 8</option>
                <option value="8">Year 9</option>
                <option value="9">Year 10</option>
            </select>
        </div>
    </div>
    <div class="form-row mt-4">
        <div class="form-group">
            <label for="firstName" class="label-form">First Name</label>
            <input type="text" id="firstName" class="form-control" placeholder="First Name">
        </div>
        <div class="form-group">
            <label for="lastName" class="label-form">Family Name</label>
            <input type="text" id="lastName" class="form-control" placeholder="Family Name">
        </div>
    </div>
    <div class="form-row mt-4 mb-5">
        <div class="form-group">
            <label for="email" class="label-form">Email</label>
            <input type="email" id="email" class="form-control" placeholder="Email">
        </div>
        <div class="form-group">
            <label for="contactNo" class="label-form">Mobile Number</label>
            <input type="text" id="contactNo" class="form-control" placeholder="Mobile Number">
        </div>
    </div>
    <button class="btn btn-primary" onclick="addGuest()">NEXT</button>
</div>

<!-- Validation Alert -->
<div id="validation-alert" class="modal fade">
    <div class="modal-dialog">
        <div class="alert alert-block alert-danger alert-dialog-display jae-border-danger">
            <i class="bi bi-exclamation-circle h5 mt-2"></i>&nbsp;&nbsp;<div class="modal-body"></div>
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        </div>
    </div>
</div>

<!-- Branch and Year Confirmation Modal -->
<div id="confirmation-modal" class="modal fade">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class="bi bi-check-circle-fill me-2"></i>
                    &nbsp;&nbsp;Confirm Assessment Details
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p class="text-center mb-4">Please confirm your selected details</p>
                <div class="confirmation-details">
                    <div class="row">
                        <div class="col-6">
                            <p class="mb-2"><strong>Branch:</strong></p>
                            <p class="highlight-text" id="confirm-branch"></p>
                        </div>
                        <div class="col-6">
                            <p class="mb-2"><strong>Year:</strong></p>
                            <p class="highlight-text" id="confirm-year"></p>
                        </div>
                    </div>
                </div>
                <p class="text-center mt-4 mb-0">
                    <small class="text-muted">Do you want to proceed with these details?</small>
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-confirm mr-2" id="confirm-proceed">
                    <i class="bi bi-check-circle mr-2"></i>Proceed
                </button>
                <button type="button" class="btn btn-secondary btn-cancel mr-2" data-dismiss="modal">
                    <i class="bi bi-x-circle mr-2"></i>Cancel
                </button>
                
            </div>
        </div>
    </div>
</div>

<script>
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      Register Guest Student
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function addGuest() {

    // Check if first name fields are not empty
    var firstName = document.getElementById('firstName');
	if(firstName.value== ""){
		$('#validation-alert .modal-body').text(
		'Please enter first name');
		$('#validation-alert').modal('show');
		$('#validation-alert').on('hidden.bs.modal', function () {
			firstName.focus();
		});
		return false;
	}

    // Check if last name fields are not empty
    var lastName = document.getElementById('lastName');
	if(lastName.value== ""){
		$('#validation-alert .modal-body').text(
		'Please enter last name');
		$('#validation-alert').modal('show');
		$('#validation-alert').on('hidden.bs.modal', function () {
			lastName.focus();
		});
		return false;
	}

    // Validate names: only letters and spaces allowed (no special characters like '-' or ',')
    var namePattern = /^[A-Za-z\s]+$/;
    if(!namePattern.test(firstName.value)){
        $('#validation-alert .modal-body').text(
        'First name can contain letters and spaces only');
        $('#validation-alert').modal('show');
        $('#validation-alert').on('hidden.bs.modal', function () {
            firstName.focus();
        });
        return false;
    }
    if(!namePattern.test(lastName.value)){
        $('#validation-alert .modal-body').text(
        'Family name can contain letters and spaces only');
        $('#validation-alert').modal('show');
        $('#validation-alert').on('hidden.bs.modal', function () {
            lastName.focus();
        });
        return false;
    }

    // Check if email fields are not empty
    var email = document.getElementById('email');
	if(email.value== ""){
		$('#validation-alert .modal-body').text(
		'Please enter email');
		$('#validation-alert').modal('show');
		$('#validation-alert').on('hidden.bs.modal', function () {
			email.focus();
		});
		return false;
	}

    // Validate email format
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if(!emailPattern.test(email.value)){
        $('#validation-alert .modal-body').text(
        'Please enter a valid email address');
        $('#validation-alert').modal('show');
        $('#validation-alert').on('hidden.bs.modal', function () {
            email.focus();
        });
        return false;
    }

    // Check if mobile fields are not empty
    var contactNo = document.getElementById('contactNo');
	if(contactNo.value== ""){
		$('#validation-alert .modal-body').text(
		'Please enter mobile number');
		$('#validation-alert').modal('show');
		$('#validation-alert').on('hidden.bs.modal', function () {
			contactNo.focus();
		});
		return false;
	}

    // Validate mobile: digits only
    var mobilePattern = /^\d+$/;
    if(!mobilePattern.test(contactNo.value)){
        $('#validation-alert .modal-body').text(
        'Mobile number must contain digits only');
        $('#validation-alert').modal('show');
        $('#validation-alert').on('hidden.bs.modal', function () {
            contactNo.focus();
        });
        return false;
    }

    // Show confirmation modal with selected Branch and Year
    showConfirmationModal();
}

function showConfirmationModal() {
    // Get selected values
    var selectedBranch = $("#listBranch option:selected").text();
    var selectedYear = $("#listGrade option:selected").text();
    
    // Update modal content
    $("#confirm-branch").text(selectedBranch);
    $("#confirm-year").text(selectedYear);
    
    // Show modal
    $("#confirmation-modal").modal('show');
}

function proceedWithSubmission() {
    // Get form data
    var guest = {
        state : $("#listState").val(),
        branch : $("#listBranch").val(),
        grade : $("#listGrade").val(),
        firstName : $("#firstName").val(),
        lastName : $("#lastName").val(),
        email : $("#email").val(),
        contactNo: $("#contactNo").val()
    }
    
    // Send AJAX to server
    $.ajax({
        url: '${pageContext.request.contextPath}/assessment/addGuest',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(guest),
        contentType: 'application/json',
        success: function (data) {
            // Navigate with id and grade
            window.location.href = '${pageContext.request.contextPath}/assessment/list?id=' + data.id + '&grade=' + data.grade;
        },
        error: function (xhr, status, error) {
            if(xhr.status == 417){
                $('#warning-alert .modal-body').text(xhr.responseJSON);
                $('#warning-alert').modal('show');
            }else{
                console.log('Error Details: ' + error);
            }
        }
    });
}

// Event listener for confirm proceed button
$(document).ready(function() {
    $('#confirm-proceed').on('click', function() {
        $('#confirmation-modal').modal('hide');
        proceedWithSubmission();
    });
});
</script>

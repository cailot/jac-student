<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Assessment</title>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8f9fa;
            margin: 0;
        }
        .assessment-container {
            background: white;
            border-radius: 10px;
            padding: 2rem;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            width: 100%;
            text-align: center;
        }
        .assessment-container h1 {
            font-size: 2rem;
            margin-bottom: 1rem;
            font-weight: bold;
        }
        .assessment-container p {
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }
        .assessment-container .btn {
            font-size: 1rem;
            padding: 0.5rem 2rem;
        }
        .form-group {
            text-align: left;
        }
        .label-form {
            display: block; /* Ensures the label behaves as a block-level element */
            text-align: left; /* Aligns the text to the left */
        }
    </style>

</head>
<body>
    <div class="assessment-container">
        <h1>Online Assessment</h1>
        <form>
            <div class="form-row">
                <div class="col-md-4">
                    <label for="listState" class="label-form">State</label>
                    <select id="listState" name="listState" class="form-control" disabled>
                        <option value="1">Victoria</option>
                    </select>
                </div>
                <div class="col-md-5">
                    <label for="listBranch" class="label-form">Branch</label>
                    <select id="listBranch" name="listBranch" class="form-control">
                        <option value="25">Balwyn</option>
                        <option value="28">Bayswater</option>
                        <option value="12">Box Hill</option>
                        <option value="13">Braybrook</option>
                        <option value="27">Caroline Springs</option>
                        <option value="14">Chadstone</option>
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
                        <option value="21">Richmond</option>
                        <option value="26">Rowville</option>
                        <option value="22">Springvale</option>
                        <option value="23">St.Albans</option>
                        <option value="24">Werribee</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="listGrade" class="label-form">Grade</label>
                    <select id="listGrade" name="listGrade" class="form-control">
                        <option value="1">P2</option>
                        <option value="2">P3</option>
                        <option value="3">P4</option>
                        <option value="4">P5</option>
                        <option value="5">P6</option>
                        <option value="6">S7</option>
                        <option value="7">S8</option>
                        <option value="8">S9</option>
                        <option value="9">S10</option>
                    </select>
                </div>
            </div>
            <div class="form-row mt-4">
                <div class="col-md-7">
                    <label for="firstName" class="label-form">First Name</label>
                    <input type="text" id="firstName" class="form-control" placeholder="First Name">
                </div>
                <div class="col-md-5">
                    <label for="familyName" class="label-form">Family Name</label>
                    <input type="text" id="familyName" class="form-control" placeholder="Family Name">
                </div>
            </div>
            <div class="form-row mt-4 mb-5">
                <div class="col-md-6">
                    <label for="email" class="label-form">Email</label>
                    <input type="email" id="email" class="form-control" placeholder="Email">
                </div>
                <div class="col-md-6">
                    <label for="mobileNumber" class="label-form">Mobile Number</label>
                    <input type="text" id="mobileNumber" class="form-control" placeholder="Mobile Number">
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/assessment/list" class="btn btn-primary" role="button">NEXT</a>
        </form>
    </div>

    <script>
        function nextStep() {
            // Add your next step logic here
            alert("Proceed to the next step.");
        }
    </script>
</body>
</html>
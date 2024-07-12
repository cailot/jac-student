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
        .assessment-container .btn {
            font-size: 1.25rem;
            padding: 0.5rem;
            margin: 0.5rem 0;
            width: 100%;
        }
        .btn.disabled {
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <div class="assessment-container">
        <h1>Online Assessment</h1>
        <button class="btn btn-primary" onclick="showWarning(1)">MATHS</button>
        <button class="btn btn-primary" onclick="showWarning(2)">ENGLISH</button>
        <button class="btn btn-primary" onclick="showWarning(3)">GA</button>
        
        <button class="btn btn-secondary disabled mt-3" id="selectionCount">0 / 3</button>
    </div>

    <script>

        
        let selectedCount = 0;
        // Extract 'id' and 'grade' from the current URL
let currentId = getQueryParam('id');
let currentGrade = getQueryParam('grade');
console.log(currentId + '--' + currentGrade);


        function selectSubject(button) {
            if (!button.classList.contains('selected')) {
                button.classList.add('selected');
                button.classList.add('btn-success');
                button.classList.remove('btn-primary');
                selectedCount++;
            } else {
                button.classList.remove('selected');
                button.classList.remove('btn-success');
                button.classList.add('btn-primary');
                selectedCount--;
            }
            updateSelectionCount();
        }

        function updateSelectionCount() {
            const selectionCount = document.getElementById('selectionCount');
            selectionCount.textContent = `${selectedCount} / 3`;
        }

    function getQueryParam(param) {
    var urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

window.showWarning = function(id) {
    // Show the warning modal
    $('#testWarningModal').modal('show');
    // Attach the click event handler to the "I agree" button
    $('#agreeTestWarning').one('click', function() {
        //displayAssessment(id);
        // if id = 1, link to /assessment/math, if id = 2, link to /assessment/english, if id = 3, link to /assessment/ga
        // Conditional redirection logic with 'id' and 'grade' appended
if (id == 1) {
    window.location.href = '${pageContext.request.contextPath}/assessment/math?id=' + currentId + '&grade=' + currentGrade;
} else if (id == 2) {
    window.location.href = '${pageContext.request.contextPath}/assessment/english?id=' + currentId + '&grade=' + currentGrade;
} else if (id == 3) {
    window.location.href = '${pageContext.request.contextPath}/assessment/ga?id=' + currentId + '&grade=' + currentGrade;
}
        
        $('#testWarningModal').modal('hide');
    });
}


function getQueryParam(param) {
    var urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

    </script>







<!--Test Warning Modal -->
<div class="modal fade" id="testWarningModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="border: 2px solid #ffc107; border-radius: 10px;">
            <div class="modal-header bg-warning" style="display: block;">
                <p style="text-align: center; margin-bottom: 0;"><span style="font-size:18px"><strong>Test Instruction for James An College Class</strong></span></p>
            </div>
            <div class="modal-body" style="background-color: #f8f9fa; border-radius: 5px; padding: 20px;">
                <div style="text-align: center; margin-bottom: 20px;">
                    <img src="${pageContext.request.contextPath}/image/test.png" style="width: 150px; height: 150px; border-radius: 5%;">
                </div>
                <!-- Add your warning message or content here -->
                <ol style="line-height: 1.6;">
                    <li><span class="text-primary"><strong>Test Duration</strong></span>
                        Ensure completion within the 30-minute time limit provided for the test.
                    </li>
                    <li><span class="text-primary"><strong>Single Attempt</strong></span>
                        Each student has a single opportunity to attempt the test, and once initiated, retakes are not permitted.
                    </li>
                    <li><span class="text-primary"><strong>Submission</strong></span>
                        Upon finishing the test, submit your answers using the "Submit" button; changes cannot be made thereafter.
                    </li>
                    <li><span class="text-primary"><strong>Feedback</strong></span>
                        Instantly view both your answers and the correct ones for each question immediately after submission, facilitating review and learning from mistakes.
                    </li>
                    <li><span class="text-primary"><strong>Test Results</strong></span>
                        Access detailed reports, including individual answers and class statistics providing insights into your performance relative to peers, under the 'Test Result' menu later.
                    </li>
                </ol>
                <p><strong>Please adhere to these guidelines to ensure a fair and effective assessment process. Good luck with your test!</strong></p>      
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="agreeTestWarning">I understand</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>
<style>
    /* Make the modal take 90% of the viewport height */
    .modal-dialog {
        display: flex;
        align-items: center; /* Vertically center modal */
        justify-content: center;
        height: 90dvh; /* iOS Safari friendly viewport height */
        margin-top: 2%;
    }
    @media (max-width: 768px) {
        .modal-dialog {
            max-width: 95%;
            width: 95%;
            margin: 1rem auto;
        }
    }

    .modal-content {
        height: 85dvh; /* Ensure the content takes most of viewport */
        overflow: hidden; /* Prevent content overflow */
        -webkit-overflow-scrolling: touch;
    }

    .modal-body {
        height: calc(100% - 120px); /* Adjust for header and footer height */
        overflow-y: auto; /* Enable scrolling for content */
        -webkit-overflow-scrolling: touch;
    }

    .topic-card {
        background-color: #d1ecf1; 
        padding: 20px; 
        border-radius: 10px; 
        box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); 
    }
    .modal-extra-large {
        max-width: 90%;
        max-height: 90%;
    }

    input[type="radio"]{
        transform: scale(2);
    }

    /* no square in check box */
    .custom-control-label::before, .custom-control-label::after {
        display: none;
    }

    .circle {
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 50%;
        width: 30px;
        height: 30px;
        border: 1px solid black;
    }

    .correct {
        color: white;
        background-color: red;
        border-color: red;
    }

    .student {
        color: white;
        background-color: blue;
        border-color: blue;
    }

    .answer {
        color: white;
        background-color: red;
        border-color: red;
    }
    
    .different {
        background-color: #FDEFB2;
    }

    .custom-badge {
        font-size: 1.0em;
        padding: 0.5em;
        margin-bottom: 1.0em;
    }

    .pdfViewerContainer,
        .col-md-9,
        .col-md-3,
        .modal-body > .row {
            height: 100%;
    }

    .answerSheet {
        overflow-y: auto;
        -webkit-overflow-scrolling: touch;
        flex-grow: 1;
        min-height: 0;
    }

    canvas.pdfCanvas {
        margin-top: 10px;
    }

    #testPdfCanvas {
        display: block;
        width: 100%; /* Stretchs to container width */
        height: auto;
        margin-top: 300px;
        position: relative !important;
    }

</style>

<script>

var pdfDoc = null;
let pageNum = 1;
let scale = 1.0; // Initial scale factor for zoom
let testSubmissionInProgress = false; // Prevent duplicate submissions (manual + auto-timeout)
let testSubmitRetryCount = 0; // Auto-retry budget for failed submissions (reset per test)
const TEST_SUBMIT_MAX_RETRY = 3;

window.showWarning = function(id, title) {
    // Show the warning modal
    $('#testWarningModal').modal('show');
    // Attach the click event handler to the "I agree" button
    $('#agreeTestWarning').one('click', function() {
        displayMaterial(id, false); // false indicates normal test mode
        $('#testWarningModal').modal('hide');
    });
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//          Load Test PDF
////////////////////////////////////////////////////////////////////////////////////////////////////////////
function loadTestPdf(pdfPath) {
    // Set the workerSrc before loading the PDF
    pdfjsLib.GlobalWorkerOptions.workerSrc = '${pageContext.request.contextPath}/js/pdf.worker-2.16.105.min.js';
    
    pdfjsLib.getDocument(pdfPath).promise.then(pdf => {
        pdfDoc = pdf;
        document.getElementById("testTotalPages").textContent = pdf.numPages;
        return pdf.getPage(1);
    }).then(page => {
        const container = document.querySelector('#testModal .pdfViewerContainer');
        if (container && container.clientWidth > 0) {
            const viewport = page.getViewport({ scale: 1.0 });
            scale = container.clientWidth / viewport.width;
        }
        renderTestPage(pageNum);
    }).catch(err => {
        console.error("Error loading test PDF for scaling: ", err);
    });
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//          Render Test PDF
////////////////////////////////////////////////////////////////////////////////////////////////////////////
function renderTestPage(num) {
    const canvas = document.getElementById("testPdfCanvas");
    const ctx = canvas.getContext("2d");

    // Clear the canvas to ensure previous rendering does not overlap
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    pdfDoc.getPage(num).then((page) => {
        const viewport = page.getViewport({ scale: scale });
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        const renderContext = {
            canvasContext: ctx,
            viewport: viewport,
        };

        // Render the page
        page.render(renderContext).promise.then(() => {
            document.getElementById("testCurrentPage").textContent = num;
            document.getElementById("testPrevPage").disabled = num <= 1;
            document.getElementById("testNextPage").disabled = num >= pdfDoc.numPages;
        }).catch((err) => {
            console.log("Error rendering page:", err);
        });
    }).catch((err) => {
        console.log("Error loading page:", err);
    });
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Pdf/Answer Sheet)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(testId, isViewOnly) {
    // Store isViewOnly in a variable that will be accessible in the callback
    var viewOnlyMode = isViewOnly;
    console.log('displayMaterial called with isViewOnly:', viewOnlyMode);
    
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/getTest/' + testId,
        method: "GET",
        success: function (test) {
            console.log(test);
            const pdfPath = test.pdfPath;
            $('#testModal').off('shown.bs.modal'); // Remove previous modal event

            $('#testModal').on('shown.bs.modal', function () {
                    
                // Start timer 30 mins (only if not in view-only mode)
                console.log('Test modal is shown, viewOnlyMode:', viewOnlyMode);
                if (!viewOnlyMode) {
                    console.log('Starting timer for normal mode');
                    testSubmissionInProgress = false;
                    testSubmitRetryCount = 0;
                    // Ensure timer is visible
                    var timerElement = document.querySelector('#timer');
                    if (timerElement) {
                        timerElement.style.display = 'block';
                        console.log('Timer element made visible');
                    } else {
                        console.log('Timer element not found!');
                    }
                    
                    // Clear any existing interval
                    if (window.timerInterval) {
                        clearInterval(window.timerInterval);
                    }
                    // Session keep-alive during the test: with no traffic for ~20 min, hosting
                    // (e.g. Azure App Service without Always On) can unload the app and drop all
                    // in-memory sessions, so the timeout auto-submit gets redirected to the login
                    // page. Ping an existing lightweight endpoint every 4 minutes to keep both
                    // the app and the session alive while the student is away on another tab.
                    if (window.sessionKeepAliveInterval) {
                        clearInterval(window.sessionKeepAliveInterval);
                    }
                    window.sessionKeepAliveInterval = setInterval(function () {
                        $.get('${pageContext.request.contextPath}/connected/sessionStatus');
                    }, 4 * 60 * 1000);
                } else {
                    console.log('Hiding timer for view-only mode');
                    // Hide timer in view-only mode
                    var timerElement = document.querySelector('#timer');
                    if (timerElement) {
                        timerElement.style.display = 'none';
                        console.log('Timer element hidden');
                    }
                }
                
                // Render the PDF
                pageNum = 1; // Reset page
                loadTestPdf(pdfPath);
                // Ensure event listeners are not duplicated
                document.getElementById("testPrevPage").onclick = () => {
                    if (pageNum > 1) {
                        pageNum--;
                        renderTestPage(pageNum);
                    }
                };
                document.getElementById("testNextPage").onclick = () => {
                    if (pageNum < pdfDoc.numPages) {
                        pageNum++;
                        renderTestPage(pageNum);
                    }
                };
                document.getElementById("testZoomIn").onclick = () => {
                    scale += 0.1;
                    // console.log('Zoom In: ', scale);
                    renderTestPage(pageNum);
                };
                document.getElementById("testZoomOut").onclick = () => {
                    if (scale > 0.1) {
                        scale -= 0.1;
                        // console.log('Zoom Out: ', scale);
                        renderTestPage(pageNum);
                    }
                };

                // Manipulate answer sheet
                var numAnswer = test.answerCount;
                var numQuestion = test.questionCount; // replace with the actual property name
                if (!viewOnlyMode) {
                    var timerDisplay = document.querySelector('#timerText');
                    startTimer(30 * 60, timerDisplay, testId, numQuestion);
                }
                var container = $('.answerSheet');
                container.empty(); // remove existing question elements
                
                // header
                var header = '<div class="h5 bg-primary" style="position: relative; display: flex; justify-content: center; align-items: center; color: #ffffff; text-align: center; margin-bottom: 20px; padding: 10px; background-color: #f8f9fa; border: 2px solid #e9ecef; border-radius: 5px;">'
                    + 'Answers&nbsp;&nbsp;<span id="chosenAnswerNum" name="chosenAnswerNum" class="text-warning" title="Student Answer">0</span>&nbsp;/&nbsp;<span id="numQuestion" name="numQuestion" title="Total Question">'+ numQuestion +'</span></div>';
                container.append(header);
                
                // Add view-only mode styling if applicable
                console.log('Applying styling, viewOnlyMode:', viewOnlyMode);
                if (viewOnlyMode) {
                    console.log('Applying view-only styling');
                    container.css({
                        'opacity': '0.5',
                        'pointer-events': 'none',
                        'background-color': '#f8f9fa'
                    });
                    // Disable all radio buttons in view-only mode
                    container.find('input[type="radio"]').prop('disabled', true);
                    console.log('Radio buttons disabled for view-only mode');
                } else {
                    console.log('Applying normal mode styling - ensuring answer sheet is enabled');
                    container.css({
                        'opacity': '1',
                        'pointer-events': 'auto',
                        'background-color': 'transparent'
                    });
                    // Also ensure all radio buttons are enabled
                    container.find('input[type="radio"]').prop('disabled', false);
                    console.log('Radio buttons enabled for normal mode');
                }
                
                for (var i = 1; i <= numQuestion; i++) {
                    var questionDiv = $('<div>').addClass('mt-5 mb-4');
                    var questionLabel = $('<div>').addClass('form-check form-check-inline h5 ml-1').text(' ' + i + '. ');
                    questionLabel.css('width', '30px');
                    questionDiv.append(questionLabel);
                
                    // Determine the options to display based on numAnswer
                    var options = ['A', 'B', 'C', 'D', 'E'].slice(0, numAnswer);
                
                    options.forEach(function(option, index) {
                        var optionDiv = $('<div>').addClass('form-check form-check-inline h5 ml-1');
                        var input = $('<input>').addClass('form-check-input mr-3 ml-1').attr({
                            type: 'radio',
                            name: 'inlineRadioOptions' + i,
                            id: 'inlineRadio' + i + (index + 1), // append the question number to the id
                            value: index + 1
                        });
                        var label = $('<label>').addClass('form-check-label').attr('for', 'inlineRadio' + i + (index + 1)).text(option);
                        optionDiv.append(input, label);
                        questionDiv.append(optionDiv);
                    });
                    container.append(questionDiv);
                }

                // Add event listener to radio buttons (only if not in view-only mode)
                if (!viewOnlyMode) {
                    $('.form-check-input').on('change', function() {
                        var chosenAnswerNum = $('input[type=radio]:checked').length;
                        $('#chosenAnswerNum').text(chosenAnswerNum);
                    });
                }
                
                // Add submit button (disabled in view-only mode)
                var submitButtonText = viewOnlyMode ? 'VIEW ONLY MODE' : 'SUBMIT';
                var submitButtonClass = viewOnlyMode ? 'btn btn-secondary w-100' : 'btn btn-primary w-100';
                var submitButtonDisabled = viewOnlyMode ? 'disabled' : '';
                console.log('Creating submit button - viewOnlyMode:', viewOnlyMode, 'text:', submitButtonText, 'class:', submitButtonClass, 'disabled:', submitButtonDisabled);
                var footer = '<div><button type="submit" class="' + submitButtonClass + '" onclick="checkAnswer(' + testId + ', ' +  numQuestion +')" ' + submitButtonDisabled + '>' + submitButtonText + '</button></div>';
                container.append(footer);
            });
            // console.log(practice);
            var setName = test.volume;
            if((TEST_GROUP == 1) || (TEST_GROUP == 2)){
                switch (test.volume) {
                    case 1:
                        setName = 'Vol.1';
                        break;
                    case 2:
                        setName = 'Vol.2';
                        break;
                    case 3:
                        setName = 'Vol.3';
                        break;
                    case 4:
                        setName = 'Vol.4';
                        break;
                    case 5:
                        setName = 'Vol.5';
                        break;
                }
            } 
            // Open the modal
            document.getElementById("testModalLabel").innerHTML = test.name + ' Test - Set <span class="text-warning">' + setName + "</span>";
            $('#testModal').modal('show');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("Error: " + errorThrown);
        },
    });
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Already Taken Dialog
////////////////////////////////////////////////////////////////////////////////////////////////////////
function alreadyTaken(testId, title) {
    console.log('Already taken test' + testId);
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/studentTestDate/' + studentId + '/' + testId,
        method: "GET",
        success: function(test) {
            document.getElementById("alreadyTitle").innerHTML = title; 
            document.getElementById("alreadyDate").innerHTML = test; 
            // Store testId for the view button
            $('#viewTestBtn').data('testId', testId);
            $('#takenWarningModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });   
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Submit Answer
////////////////////////////////////////////////////////////////////////////////////////////////////////
function checkAnswer(testId, numQuestion) {
    // Prevent duplicate submits caused by rapid clicks or timeout overlap
    if (testSubmissionInProgress) {
        return;
    }
    testSubmissionInProgress = true;

    // Disable the button and change its text
    var submitButton = document.querySelector('button[onclick*="checkAnswer"]');
    if (submitButton) {
        submitButton.disabled = true;
        submitButton.innerHTML = 'Submitting...';
        submitButton.style.backgroundColor = 'grey';
    }

    // Collect all the selected answers
    var answers = [];
    for (var i = 1; i <= numQuestion; i++) {
        var selectedOption = $('input[name=inlineRadioOptions' + i + ']:checked').val();
        var answer = parseInt(selectedOption) || 0; // Convert to integer and default to 0 if NaN
        answers.push({
            question: i,
            answer: answer
        });
    }
    //Make an AJAX call to send the data to the server
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/addStudentTest',
        method: 'POST',
        data: JSON.stringify({
            studentId : studentId,
            testId : testId,
            answers : answers
        }),
        contentType: 'application/json',
        success: function(response) {
            // When the login session has expired, Spring Security redirects this POST to the
            // login page and jQuery receives that HTML as a 200 "success". The answers are NOT
            // saved in that case, so verify the body is the real controller response first.
            var body = (typeof response === 'string') ? response : JSON.stringify(response);
            if (!body || body.indexOf('StudentTest') === -1) {
                console.log('Submission response is not from the test controller (session expired?)');
                testSubmissionInProgress = false;
                if (submitButton) {
                    submitButton.disabled = false;
                    submitButton.innerHTML = 'SUBMIT';
                    submitButton.style.backgroundColor = '';
                }
                $('#submitErrorNotice').remove();
                $('.answerSheet').append(
                    '<div id="submitErrorNotice" class="alert alert-danger mt-2" role="alert">' +
                    '<strong>Your answers are NOT saved.</strong> Your login session has expired. ' +
                    'Please open a NEW tab, log in again, then come back to this page and click SUBMIT. ' +
                    'Do NOT close or refresh this page, or your answers will be lost.' +
                    '</div>'
                );
                return;
            }
            testSubmitRetryCount = 0;
            $('#submitErrorNotice').remove();
            // pdf & answer sheet dialogue disappears
            $('#testModal').modal('hide');
            $('#success-alert .modal-body').html('Answer is successfully submitted.');
	        $('#success-alert').modal('show');

			// Attach an event listener to the success alert close event
			$('#success-alert').on('hidden.bs.modal', function () {
				// Reload the page after the success alert is closed
				location.href = window.location.pathname; // Passing true forces a reload from the server and not from the cache
			});

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
            // Allow retry if submission failed.
            testSubmissionInProgress = false;
            if (submitButton) {
                submitButton.disabled = false;
                submitButton.innerHTML = 'SUBMIT';
                submitButton.style.backgroundColor = '';
            }
            // Failure was previously logged to console only; surface it so the student
            // knows the answers are NOT saved yet and can press SUBMIT again.
            $('#submitErrorNotice').remove();
            $('.answerSheet').append(
                '<div id="submitErrorNotice" class="alert alert-danger mt-2" role="alert">' +
                'Submission failed. Your answers are NOT saved yet. Please click SUBMIT again.' +
                '</div>'
            );
            // Auto-retry a few times to cover transient failures (e.g. network not yet
            // reconnected right after the device wakes and the timeout auto-submit fires).
            if (testSubmitRetryCount < TEST_SUBMIT_MAX_RETRY && $('#testModal').is(':visible')) {
                testSubmitRetryCount++;
                setTimeout(function () {
                    if (!testSubmissionInProgress && $('#testModal').is(':visible')) {
                        checkAnswer(testId, numQuestion);
                    }
                }, 10000);
            }
        }
    });
}

</script>

<!-- Pop up Test modal -->
<div class="modal fade" id="testModal" tabindex="-1" role="dialog" aria-labelledby="testModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 85vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="testModalLabel"></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body bg-light p-0" style="height: 100%;">
                <div class="row m-0" style="height: 100%;">
                    
                    <!-- PDF LEFT PANEL -->
                    <div class="col-md-9 bg-white border d-flex flex-column" style="height: 100%;">
                        <!-- PDF Toolbar -->
                        <div class="pdf-toolbar p-2 border-bottom" style="flex-shrink: 0;">
                            <button id="testPrevPage">Previous</button>
                            <span>Page: <span id="testCurrentPage">1</span> / <span id="testTotalPages">1</span></span>
                            <button id="testNextPage">Next</button>
                            <button id="testZoomOut">-</button>
                            <button id="testZoomIn">+</button>
                        </div>
                        <!-- PDF Viewer Canvas -->
                        <div class="pdfViewerContainer flex-grow-1 overflow-auto" style="-webkit-overflow-scrolling: touch;">
                            <canvas id="testPdfCanvas" class="pdfCanvas" style="display: block; max-width: 100%; height: auto;"></canvas>
                        </div>
                    </div>
                    <!-- ANSWER SHEET PANEL -->
                    <div class="col-md-3 bg-white border d-flex flex-column p-3" style="height: 100%;">
                        <!-- Timer -->
                        <div id="timer" class="text-center mb-2" style="font-size: 20px; font-weight: bold;">
                            <i class="bi bi-stopwatch"></i>&nbsp;&nbsp;<span id="timerText"></span>
                        </div>
                        <!-- Answer Sheet (scrollable) -->
                        <div class="answerSheet flex-grow-1 overflow-auto" style="min-height: 0;"></div>
                    </div>

                </div>
            </div>

            <div class="modal-footer bg-dark text-white">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


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
                        Please complete the test within 30 minutes. When time is up, all currently marked answers will be submitted automatically.
                    </li>
                    <li><span class="text-primary"><strong>Single Attempt</strong></span>
                        Each student has a single opportunity to attempt the test, and once initiated, retakes are not permitted.
                    </li>
                    <li><span class="text-primary"><strong>Submission</strong></span>
                        Upon finishing the test, submit your answers using the "Submit" button; changes cannot be made thereafter.
                    </li>
                    <!-- <li><span class="text-primary"><strong>Feedback</strong></span>
                        Instantly view both your answers and the correct ones for each question immediately after submission, facilitating review and learning from mistakes.
                    </li> -->
                    <li><span class="text-primary"><strong>Test Results</strong></span>
                        Access detailed reports, including individual answers and class statistics providing insights into your performance relative to peers, under the 'Test Result' menu later.
                    </li>
                </ol>
                <br><br>
                <p><strong>Please adhere to these guidelines to ensure a fair and effective assessment process. Good luck with your test!</strong></p>      
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="agreeTestWarning">I understand</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- Already Taken Warning Modal -->
<div class="modal fade" id="takenWarningModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="border: 2px solid #ffc107; border-radius: 10px; height: 50vh;">
            <div class="modal-header bg-warning" style="display: block;">
                <p style="text-align: center; margin-bottom: 0;"><span style="font-size:18px"><strong>You have already taken this test !</strong></span></p>
            </div>
            <div class="modal-body" style="background-color: #f8f9fa; border-radius: 5px; padding: 20px;">
                <div style="text-align: center; margin-bottom: 20px;">
                    <img src="${pageContext.request.contextPath}/image/test-done.png" style="width: 150px; height: 150px; border-radius: 5%;">
                </div>
                <div class="alert alert-info" style="border-left: 4px solid #17a2b8; background-color: #e3f2fd;">
                    <strong><span class="text-primary" id="alreadyTitle">Retake Practice</span></strong>
                    <br>Our system has detected that you have already taken this test at <strong><span id="alreadyDate"></span></strong>.
                    <br>You will be able to check your results once the system is ready.
                    <br>However, you can still review the test for your reference.
                </div>                
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="viewTestBtn">
                    <i class="bi bi-eye"></i> View Test
                </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <i class="bi bi-x-circle"></i> Close
                </button>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function() {
        // stop the timer when the modal is hidden
        $('#testModal').on('hidden.bs.modal', function () {
            console.log('Test modal is hidden');
            var display = document.querySelector('#timerText');
            display.textContent = "";
            // Clear the interval to stop the timer
            if (window.timerInterval) {
                clearInterval(window.timerInterval);
                window.timerInterval = null;
            }
            // Stop the session keep-alive ping together with the timer
            if (window.sessionKeepAliveInterval) {
                clearInterval(window.sessionKeepAliveInterval);
                window.sessionKeepAliveInterval = null;
            }
        });
        
        // Handle View Test button click
        $('#viewTestBtn').on('click', function() {
            var testId = $(this).data('testId');
            if (testId) {
                // Close the modal first
                $('#takenWarningModal').modal('hide');
                // Display test in view-only mode (timer disabled, answer sheet disabled, submit disabled)
                displayMaterial(testId, true);
            }
        });
    });

    function startTimer(duration, display, testId, numQuestion) {
        // Wall-clock deadline instead of counting setInterval ticks: browsers throttle
        // or freeze timers in background tabs / device sleep, which used to pause the
        // countdown and prevent the auto-submit from ever firing.
        var deadline = Date.now() + duration * 1000;
        var minutes, seconds;
        display.textContent = ""; // Clear the timer display at the start of the function
        window.timerInterval = setInterval(function () {
            var remaining = Math.max(0, Math.ceil((deadline - Date.now()) / 1000));
            minutes = parseInt(remaining / 60, 10);
            seconds = parseInt(remaining % 60, 10);

            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;
            console.log(minutes + ":" + seconds);
            display.textContent = minutes + ":" + seconds;

            if (remaining <= 0) {
                clearInterval(window.timerInterval);
                window.timerInterval = null;
                display.textContent = "TIME'S UP";
                // Auto-submit with current answers at timeout in normal mode.
                // Reuses existing checkAnswer success flow (same popup + reload behavior).
                if (!testSubmissionInProgress && $('#testModal').is(':visible')) {
                    checkAnswer(testId, numQuestion);
                }
            }
        }, 1000);
    }
</script>
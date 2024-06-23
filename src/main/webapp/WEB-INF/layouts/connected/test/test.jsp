
<style>
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

</style>
<script>
$(document).ready(function() {
    $('#testModal').on('shown.bs.modal', function () {
        var time = 30 * 60, // 30 minutes in seconds
            display = document.querySelector('#timerText');

        // Clear any existing interval
        if (window.timerInterval) {
            clearInterval(window.timerInterval);
        }

        startTimer(time, display);
    });

    $('#testModal').on('hidden.bs.modal', function () {
        var display = document.querySelector('#timerText');
        display.textContent = "";
    });
});

function startTimer(duration, display) {
    var timer = duration, minutes, seconds;
    display.textContent = ""; // Clear the timer display at the start of the function
    window.timerInterval = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            clearInterval(window.timerInterval);
            display.textContent = "TIME'S UP";
        }
    }, 1000);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Pdf/Answer Sheet)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(testId, setNumber) {
    // set dialogSet value as setNumber
    document.getElementById("dialogSet").innerHTML = setNumber;  
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/getTest/' + testId,
        method: "GET",
        success: function(test) {
            //console.log(test);
            document.getElementById("pdfViewer").data = test.pdfPath;
            // manipulate answer sheet
            var numQuestion = test.questionCount; // replace with the actual property name
            var container = $('.answerSheet');
            container.empty(); // remove existing question elements
            // header
            var header = '<div class="h5 bg-primary" style="position: relative; display: flex; justify-content: center; align-items: center; color: #ffffff; text-align: center; margin-bottom: 20px; padding: 10px; background-color: #f8f9fa; border: 2px solid #e9ecef; border-radius: 5px;">'
            + 'Answers&nbsp;&nbsp;<span id="chosenAnswerNum" name="chosenAnswerNum" class="text-warning" title="Student Answer">0</span>&nbsp;/&nbsp;<span id="numQuestion" name="numQuestion" title="Total Question">'+ numQuestion +'</span></div>';
            container.append(header);
            for (var i = 1; i <= numQuestion; i++) {
                var questionDiv = $('<div>').addClass('mt-5 mb-4');
                var questionLabel = $('<div>').addClass('form-check form-check-inline h5 ml-1').text(' ' + i + '. ');
                questionLabel.css('width', '20px');
                questionDiv.append(questionLabel);
                ['A', 'B', 'C', 'D', 'E'].forEach(function(option, index) {
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

            // Add event listener to radio buttons
            $('.form-check-input').on('change', function() {
                var chosenAnswerNum = $('input[type=radio]:checked').length;
                $('#chosenAnswerNum').text(chosenAnswerNum);
            });
            var footer = '<div><button type="submit" class="btn btn-primary w-100" onclick="checkAnswer(' + testId + ', ' +  numQuestion +')">SUBMIT</button></div>';
            container.append(footer);

            // pop-up pdf & answer sheet
            $('#testModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });  
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Answer (Video/Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayAnswer(testId, setNumber) {
   // set dialogAnswerSet value as weekNumber
    document.getElementById("dialogAnswerSet").innerHTML = setNumber;
    document.getElementById("dialogAnswerPdfSet").innerHTML = setNumber;    
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/testAnswer/' + studentId + '/' + testId,
        method: "GET",
        success: function(test) {
            // console.log(test);
            if(test.videoPath==null || test.videoPath==''){ // display pdf with answer sheet
                document.getElementById("onlyPdfViewer").data = test.pdfPath;
                // manipulate answer sheet
                var studentAnswers = test.answers;
                var numQuestion = studentAnswers.length; // replace with the actual property name
                var container = $('.onlyPdfAnswerSheet');
                container.empty(); // remove existing question elements
                // header
                var header = '<div class="h5 bg-primary" style="position: relative; display: flex; justify-content: center; align-items: center; color: #ffffff; text-align: center; margin-bottom: 20px; padding: 10px; background-color: #f8f9fa; border: 2px solid #e9ecef; border-radius: 5px;">'
                + 'Total Questions : &nbsp;&nbsp;<span id="numQuestion" name="numQuestion" title="Total Question">'+ numQuestion +'</span></div>';
                container.append(header);
                for (var i = 0; i < numQuestion; i++) {
                    var questionDiv = $('<div>').addClass('mt-5 mb-4');
                    var questionLabel = $('<div>').addClass('form-check form-check-inline h5 ml-1').text(' ' + (i+1) + '. ');
                    // Set a consistent width for the question label container
                    questionLabel.css('width', '50px'); // Adjust the width as needed
                    questionDiv.append(questionLabel);
                    ['A', 'B', 'C', 'D', 'E'].forEach(function (option, index) {
                        var optionDiv = $('<div>').addClass('custom-control custom-control-inline h6');
                        var label = $('<label>').addClass('custom-control-label circle').attr('for', 'customCheck' + i + (index + 1)).text(option);
                        if (test.students[i] == index + 1 && test.answers[i].answer == index + 1) {
                            // If student's answer and correct answer are the same, add 'correct' class
                            label.addClass('correct');
                        } else if (test.students[i] == index + 1) {
                            // If only student's answer is this option, add 'student' class
                            label.addClass('student');
                        } else if (test.answers[i].answer == index + 1) {
                            // If only correct answer is this option, add 'answer' class
                            label.addClass('answer');
                        }
                        if (test.students[i] != test.answers[i].answer) {
                            // If student's answer and correct answer are different, add 'different' class to the question div
                            questionDiv.addClass('different');
                        }
                        optionDiv.append(label);
                        questionDiv.append(optionDiv);
                    });
                    container.append(questionDiv);    
                }
                // pop-up pdf & answer sheet
                $('#testAnswerPdfModal').modal('show');                

            }else{ // display video/pdf with answer sheet
                var videoPlayer = document.getElementById("answerVideoPlayer");
                videoPlayer.src = test.videoPath;
                document.getElementById("answerPdfViewer").data = test.pdfPath;
                // manipulate answer sheet
                var studentAnswers = test.students;
                var numQuestion = studentAnswers.length;
                var result = calculateScore(test.students, test.answers);
                var score = result.score;
                var countCorrect = result.numCorrect;
                var container = $('.resultSheet');
                container.empty(); // remove existing question elements
                // debugger;
                // header
                var header = '<div class="h5 bg-primary" style="position: relative; display: flex; justify-content: center; align-items: center; color: #ffffff; text-align: center; margin-bottom: 20px; padding: 10px; background-color: #f8f9fa; border: 2px solid #e9ecef; border-radius: 5px;">'
                + 'My Score : ' + score + '% (<span id="correctAnswerNum" name="correctAnswerNum" style="color:blue;" title="Student Answer">' + countCorrect + '</span>/<span id="answerNumQuestion" name="answerNumQuestion" style="color:red;" title="Correct Answer">'+ (numQuestion) +'</span>)</div>';
                container.append(header);
                for (var i = 0; i < numQuestion; i++) {
                    var questionDiv = $('<div>').addClass('mt-5 mb-4');
                    var questionLabel = $('<div>').addClass('form-check form-check-inline h5 ml-1').text(' ' + (i+1) + '. ');
                    // Set a consistent width for the question label container
                    questionLabel.css('width', '50px'); // Adjust the width as needed
                    questionDiv.append(questionLabel);
                    ['A', 'B', 'C', 'D', 'E'].forEach(function (option, index) {
                        var optionDiv = $('<div>').addClass('custom-control custom-control-inline h6');
                        var label = $('<label>').addClass('custom-control-label circle').attr('for', 'customCheck' + i + (index+1)).text(option);
                        if (test.students[i] == index+1 && test.answers[i].answer == index+1) {
                            // If student's answer and correct answer are the same, add 'correct' class
                            label.addClass('correct');
                        } else if (test.students[i] == index+1) {
                            // If only student's answer is this option, add 'student' class
                            label.addClass('student');
                        } else if (test.answers[i].answer == index+1) {
                            // If only correct answer is this option, add 'answer' class
                            label.addClass('answer');
                        }
                        if (test.students[i] != test.answers[i].answer) {
                            // If student's answer and correct answer are different, add 'different' class to the question div
                            questionDiv.addClass('different');
                        }
                        optionDiv.append(label);
                        questionDiv.append(optionDiv);
                    });
                    container.append(questionDiv);    
                }
                
                // pop-up pdf & answer sheet
                $('#testAnswerModal').modal('show');
            }    
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
        }
    });
}

//////////////////////////////////////////////////////////////////
// Calculate score by comparing student answers and answer sheet
//////////////////////////////////////////////////////////////////
function calculateScore(studentAnswers, answerSheet) {
    // Check if both arrays have the same length
    if (!studentAnswers || !answerSheet || studentAnswers.length !== answerSheet.length) {
        return 0;
    }
    var totalQuestions = answerSheet.length 
    // Iterate through the arrays and compare corresponding elements
    var correctAnswers = 0;
    for (var i = 0; i < totalQuestions; i++) {
        var studentAnswer = studentAnswers[i];
        var correctAnswer = answerSheet[i].answer;

        if (studentAnswer === correctAnswer) {
            correctAnswers++;
        }
    }
    // Calculate the final score as a percentage
    var score = (correctAnswers / totalQuestions) * 100;
    //var rounded = Math.round(score * 100) / 100;
    var rounded = Math.round(score);
    return {numCorrect: correctAnswers, score : rounded};
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
            <div class="modal-body bg-light">
                <div class="row">
                    <div class="col-md-9 bg-white p-3 border">
                        <object id="pdfViewer" data="" type="application/pdf" style="width: 100%; height: 80vh;">
                            <p>It appears you don't have a PDF plugin for this browser. No biggie... you can <a href="your_pdf_url">click here to download the PDF file.</a></p>
                        </object>
                    </div>
                    <div class="col-md-3 bg-white p-3 border" style="height: 85vh;">
                        <div style="display: flex; flex-direction: column; height: 100%;">
                            <!-- TIMER -->
                            <div id="timer" class="text-center" style="font-size: 20px; font-weight: bold;">
                                <i class="bi bi-stopwatch"></i>&nbsp;&nbsp;<span id="timerText"></span>
                            </div>
                            <!-- ANSWER SHEET -->
                            <div class="answerSheet" style="overflow-y: auto; flex-grow: 1;"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer bg-dark text-white">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- Pop up Answer modal for Pdf only-->
<div class="modal fade" id="testAnswerPdfModal" tabindex="-1" role="dialog" aria-labelledby="testAnswerPdfModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="testAnswerPdfModalLabel"></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body bg-light">
                <div class="row">
                    <div class="col-md-9 bg-white p-3 border">
                        <object id="onlyPdfViewer" data="" type="application/pdf" style="width: 100%; height: 80vh;">
                            <p>It appears you don't have a PDF plugin for this browser. No biggie... you can <a href="your_pdf_url">click here to download the PDF file.</a></p>
                        </object>
                    </div>
                    <div class="col-md-3 bg-white p-3 border" style="height: 85vh;">
                        <div style="display: flex; flex-direction: column; height: 100%;">
                            <!-- ANSWER SHEET -->
                            <div class="onlyPdfAnswerSheet" style="overflow-y: auto; flex-grow: 1;"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer bg-dark text-white">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- Pop up Answer modal for Video/Pdf-->
<div class="modal fade" id="testAnswerModal" tabindex="-1" role="dialog" aria-labelledby="testAnswerModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="testAnswerModalLabel"></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body bg-light">
                <div class="row">
                    <div class="col-md-6 d-flex flex-column justify-content-center bg-white p-3 border">
                        <div style="display: flex; flex-direction: column; height: 80vh;">
                            <video id="answerVideoPlayer" controls controlsList="nodownload" style="flex: 6;">
                                <source src="" type="video/mp4">
                            </video>
                            <div style="overflow-y: auto; flex: 1;"></div>
                            <div class="resultSheet" style="overflow-y: auto; flex: 3;">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 bg-white p-3 border">
                        <object id="answerPdfViewer" data="" type="application/pdf" style="width: 100%; height: 80vh;">
                            <p>It appears you don't have a PDF plugin for this browser. No biggie... you can <a href="your_pdf_url">click here to download the PDF file.</a></p>
                        </object>
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
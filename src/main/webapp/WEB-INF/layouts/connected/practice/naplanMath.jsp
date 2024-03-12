<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    .english-homework {
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

    .correct {
        color: red;
    }

    .student {
        color: blue;
    }

    .answer {
        color: red;
    }

    .different {
        background-color: #D1ECF1;
    }

    /*
    #stickyHeader {
        position: sticky;
        top: 0;
        background-color: white; 
    }
    */
    
</style>
<script>

const PRACTICE_TYPE = 4; // 4 is NAPLAN Math 
const MOVIE = 0;
const PDF = 1;
const DONE= 'DONE';
// console.log(studentId);
$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryPractice/' + studentId + '/' + PRACTICE_TYPE + '/' + numericGrade,
        method: "GET",
        success: function(data) {
            $.each(data, function(index, basket) {

				var title = basket.name;
                var id = basket.value;
                var icon = '<i class="bi bi-send h5 text-primary" title="unsubmitted yet"></i>';
                var cardBody = '<div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(' + id +  ', \'' +  title + '\');">'
                if (title.endsWith('DONE')) {
                    // title ends with 'DONE'
                    title = title.slice(0, -4);
                    icon = '<i class="bi bi-send-fill h5 text-primary" title="submitted"></i>';
                    cardBody = '<div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayAnswer(' + id +  ', \'' +  title + '\');">'
                }
                console.log(basket);
                var topicDiv = '<div class="col-md-4">'
                + cardBody
                + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin: 30px;">'
                + '<strong><span id="topicTitle">Set ' + title + '</span></strong>&nbsp;&nbsp;' + icon
                + '</p></div></div></div>';
                $('#topicContainer').append(topicDiv);    
			});
           
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Pdf/Answer Sheet)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(practiceId, setNumber) {
    // set dialogSet value as setNumber
    document.getElementById("dialogSet").innerHTML = setNumber;  
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/getPractice/' + practiceId,
        method: "GET",
        success: function(practice) {
            // console.log(practice);
            document.getElementById("pdfViewer").data = practice.pdfPath;
            // manipulate answer sheet
            var numQuestion = practice.questionCount; // replace with the actual property name
            var container = $('.answerSheet');
            container.empty(); // remove existing question elements
            // header
            var header = '<div style="font-size: 24px; color: #333; text-align: center; margin-bottom: 20px;">Answers <span id="chosenAnswerNum" name="chosenAnswerNum">0</span>/<span id="numQuestion" name="numQuestion">'+ numQuestion +'</span></div>';
            container.append(header);
            for (var i = 1; i <= numQuestion; i++) {
                var questionDiv = $('<div>').addClass('mt-4 mb-4');
                questionDiv.append($('<div>').addClass('form-check form-check-inline h5').text('Question ' + i + '. '));
                ['A', 'B', 'C', 'D', 'E'].forEach(function(option, index) {
                    var optionDiv = $('<div>').addClass('form-check form-check-inline h5');
                    var input = $('<input>').addClass('form-check-input mr-3 ml-2').attr({
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
            var footer = '<div><button type="submit" class="btn btn-primary w-100" onclick="checkAnswer(' + practiceId + ', ' +  numQuestion +')">SUBMIT</button></div>';
            container.append(footer);

            // pop-up pdf & answer sheet
            $('#practiceModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });  
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Answer (Video/Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayAnswer(practiceId, setNumber) {
   // set dialogSet value as weekNumber
    document.getElementById("dialogAnswerSet").innerHTML = setNumber;  
//     var year = document.getElementById("academicYear").value;
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/practiceAnswer/' + studentId + '/' + practiceId,
        method: "GET",
        success: function(value) {
            console.log(value);
            // Add this part for displaying played percentage
            var videoPlayer = document.getElementById("answerVideoPlayer");
            videoPlayer.src = value.videoPath;
            document.getElementById("answerPdfViewer").data = value.pdfPath;
            // manipulate answer sheet
            var answerNumQuestion = value.answers.length;
            var score = calculateScore(value.students, value.answers);
            var countCorrect = countCorrectAnswers(value.students, value.answers);
            var container = $('.resultSheet');
            container.empty(); // remove existing question elements
            // header
            var header = '<div id="stickyHeader" style="font-size: 24px; color: #333; text-align: center; margin-bottom: 20px;">You got ' + score + ' score (Answers <span id="correctAnswerNum" name="correctAnswerNum" class="student" title="Student Answer">' + countCorrect + '</span>/<span id="answerNumQuestion" name="answerNumQuestion" class="correct" title="Correct Answer">'+ (answerNumQuestion-1) +'</span>)<button onclick="retestRequest(' + value.practiceId + ')" style="margin-left: 20px;">Retake</button></div>';
            container.append(header);
            for (var i = 1; i < answerNumQuestion; i++) {
                var questionDiv = $('<div>').addClass('mt-4 mb-4');
                questionDiv.append($('<div>').addClass('form-check form-check-inline h5').text('Question ' + i + '. '));
                // Loop for the answers
                ['A', 'B', 'C', 'D', 'E'].forEach(function(option, index) {
                    var optionDiv = $('<div>').addClass('custom-control custom-checkbox custom-control-inline h5');
                    var input = $('<input>').addClass('custom-control-input').attr({
                        type: 'checkbox',
                        id: 'customCheck' + i + (index + 1),
                        value: index + 1,
                        checked: value.students[i] == index + 1 || value.answers[i] == index + 1
                        //disabled: true
                    }).on('click', function(e) {
                        e.preventDefault();
                    });
                    var label = $('<label>').addClass('custom-control-label').attr('for', 'customCheck' + i + (index + 1)).text(option);
                    if (value.students[i] == index + 1 && value.answers[i] == index + 1) {
                        // If student's answer and correct answer are the same, add 'correct' class
                        input.addClass('correct');
                        label.addClass('correct');
                    } else if (value.students[i] == index + 1) {
                        // If only student's answer is this option, add 'student' class
                        input.addClass('student');
                        label.addClass('student');
                    } else if (value.answers[i] == index + 1) {
                        // If only correct answer is this option, add 'answer' class
                        input.addClass('answer');
                        label.addClass('answer');
                    }
                    if (value.students[i] != value.answers[i]) {
                        // If student's answer and correct answer are different, add 'different' class to the question div
                        questionDiv.addClass('different');
                    }
                    optionDiv.append(input, label);
                    questionDiv.append(optionDiv);
                });

                container.append(questionDiv);
            }    

            // pop-up video & pdf
            $('#answerModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });   
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Submit Answer
////////////////////////////////////////////////////////////////////////////////////////////////////////
function checkAnswer(practiceId, numQuestion) {
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
        url: '${pageContext.request.contextPath}/connected/addStudentPractice',
        method: 'POST',
        data: JSON.stringify({
            studentId : studentId,
            practiceId : practiceId,
            answers : answers
        }),
        contentType: 'application/json',
        success: function(response) {


             // pdf & answer sheet dialogue disappears
             $('#practiceModal').modal('hide');



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

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Re-test
////////////////////////////////////////////////////////////////////////////////////////////////////////
function retestRequest(practiceId) {
    //Make an AJAX call to send the data to the server
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/deleteStudentPractice/' + studentId + '/' + practiceId,
        method: 'DELETE',
        success: function(response) {


             // pdf & answer sheet dialogue disappears
             $('#practiceModal').modal('hide');



            // $('#success-alert .modal-body').html('Upgrade to <span class="font-weight-bold text-danger">' + practiceId + '</span> is successfully updated.');
	        // $('#success-alert').modal('show');


			// // Attach an event listener to the success alert close event
			// $('#success-alert').on('hidden.bs.modal', function () {
				// Reload the page after the success alert is closed
				location.href = window.location.pathname; // Passing true forces a reload from the server and not from the cache
			// });

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
    var totalQuestions = answerSheet[0]; // Assuming the first element is the total count

    // Iterate through the arrays and compare corresponding elements
    var correctAnswers = 0;
    for (var i = 1; i <= totalQuestions; i++) {
        var studentAnswer = studentAnswers[i];
        var correctAnswer = answerSheet[i];

        if (studentAnswer === correctAnswer) {
            correctAnswers++;
        }
    }
    // Calculate the final score as a percentage
    var score = (correctAnswers / totalQuestions) * 100;
    var rounded = Math.round(score * 100) / 100;
    return rounded;
}

///////////////////////////////////////////////////////////////////////
// Count correct answers by comparing student answers and answer sheet
//////////////////////////////////////////////////////////////////////
function countCorrectAnswers(studentAnswers, answerSheet) {
    // Check if both arrays have the same length
    if (!studentAnswers || !answerSheet || studentAnswers.length !== answerSheet.length) {
        return 0;
    }
    var totalQuestions = answerSheet[0]; // Assuming the first element is the total count

    // Iterate through the arrays and compare corresponding elements
    var correctAnswers = 0;
    for (var i = 1; i <= totalQuestions; i++) {
        var studentAnswer = studentAnswers[i];
        var correctAnswer = answerSheet[i];

        if (studentAnswer === correctAnswer) {
            correctAnswers++;
        }
    }
    return correctAnswers;
}



</script>

<div class="col-md-12" style="padding: 30px;">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase;">NAPLAN MATH</h2>
    </div>
</div>

<div id="topicContainer" class="row"></div>

<!-- Pop up Practice modal -->
<div class="modal fade" id="practiceModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="exampleModalLabel">NAPLAN Math Practice - Set <span id="dialogSet" name="dialogSet" class="text-warning"></span></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            
            <div class="modal-body bg-light">
                <div class="row">
                    <div class="col-md-8 bg-white p-3 border">
                        <object id="pdfViewer" data="" type="application/pdf" style="width: 100%; height: 80vh;">
                            <p>It appears you don't have a PDF plugin for this browser. No biggie... you can <a href="your_pdf_url">click here to download the PDF file.</a></p>
                        </object>
                    </div>
                    <div class="col-md-4 bg-white p-3 border" style="height: 85vh;">
                        <div style="display: flex; flex-direction: column; height: 100%;">
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
<!-- Pop up Answer modal -->
<div class="modal fade" id="answerModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="exampleModalLabel">NAPLAN Math Practice - Set <span id="dialogAnswerSet" name="dialogAnswerSet" class="text-warning"></span></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body bg-light">
                <div class="row">
                    <div class="col-md-6 d-flex flex-column justify-content-center bg-white p-3 border">
                        <video id="answerVideoPlayer" controls controlsList="nodownload" style="width: 100%; height: 60%;">
                            <source src="" type="video/mp4">
                        </video>
                        <div style="overflow-y: auto; flex-grow: 1; height: 10%;"></div>
                        <div class="resultSheet" style="overflow-y: auto; flex-grow: 1; height: 30%;">
                            
                        
                        
                        
                        
                        
                        
                        
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


<!-- Success Alert -->
<div id="success-alert" class="modal fade">
	<div class="modal-dialog">
		<div class="alert alert-block alert-success alert-dialog-display">
			<i class="bi bi-check-circle-fill fa-2x"></i>&nbsp;&nbsp;<div class="modal-body"></div>
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


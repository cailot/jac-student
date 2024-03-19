
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
// 			Display Answer (Pdf/submitted Answer)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayAnswer(testId, setNumber) {
   // set dialogSet value as weekNumber
    // document.getElementById("dialogAnswerSet").innerHTML = setNumber;
    document.getElementById("dialogSet").innerHTML = setNumber;  
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/testAnswer/' + studentId + '/' + testId,
        method: "GET",
        success: function(test) {
            // console.log(test);

            document.getElementById("pdfViewer").data = test.pdfPath;
            // manipulate answer sheet
            var studentAnswers = test.answers;
            var numQuestion = studentAnswers.length; // replace with the actual property name
            var container = $('.answerSheet');
            container.empty(); // remove existing question elements
            // header
            var header = '<div class="h5 bg-primary" style="position: relative; display: flex; justify-content: center; align-items: center; color: #ffffff; text-align: center; margin-bottom: 20px; padding: 10px; background-color: #f8f9fa; border: 2px solid #e9ecef; border-radius: 5px;">'
            + 'Total Questions : &nbsp;&nbsp;<span id="numQuestion" name="numQuestion" title="Total Question">'+ numQuestion +'</span></div>';
            container.append(header);
            for (var i = 0; i < numQuestion; i++) {
                var questionDiv = $('<div>').addClass('mt-5 mb-4');
                var questionLabel = $('<div>').addClass('form-check form-check-inline h5 ml-1').text(' ' + (i+1) + '. ');
                questionLabel.css('width', '20px');
                questionDiv.append(questionLabel);
                ['A', 'B', 'C', 'D', 'E'].forEach(function(option, index) {
                    var optionDiv = $('<div>').addClass('form-check form-check-inline h5 ml-1');
                    var input = $('<input>').addClass('form-check-input mr-3 ml-1').attr({
                        type: 'radio',
                        name: 'inlineRadioOptions' + (i+1),
                        id: 'inlineRadio' + (i+1) + (index + 1), // append the question number to the id
                        value: index + 1,
                        disabled: true
                    });
                     // Check if the current option matches the student's answer for the current question
                    if (studentAnswers[i].answer === index + 1) {
                        input.attr('checked', true);
                    }
                    var label = $('<label>').addClass('form-check-label').attr('for', 'inlineRadio' + (i+1) + (index + 1)).text(option);
                    optionDiv.append(input, label);
                    questionDiv.append(optionDiv);
                });
                container.append(questionDiv);
            }

            // var footer = '<div><button type="submit" class="btn btn-primary w-100" onclick="checkAnswer(' + testId + ', ' +  numQuestion +')">SUBMIT</button></div>';
            // container.append(footer);

            // pop-up pdf & answer sheet
            $('#testModal').modal('show');

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

</script>

</script>
<!-- Pop up Test modal -->
<div class="modal fade" id="testModal" tabindex="-1" role="dialog" aria-labelledby="testModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
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

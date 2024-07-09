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

    .answerSheet {
        position: relative;
        height: 85vh; /* Example height */
        overflow-y: auto;
    }

</style>
<script>
/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Pdf/Answer Sheet)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function display(url) {
    document.getElementById("pdfViewer").data = url;
    // manipulate answer sheet
    var numQuestion = 20; // replace with the actual property name
    var container = $('.answerSheet');
    container.empty(); // remove existing question elements
    // header
    var header = '<div class="h5 bg-primary" style="position: sticky; top: 0; display: flex; justify-content: center; align-items: center; color: #ffffff; text-align: center; margin-bottom: 20px; padding: 10px; background-color: #f8f9fa; border: 2px solid #e9ecef; border-radius: 5px; z-index: 100;">'
    + 'Answers&nbsp;&nbsp;<span id="chosenAnswerNum" name="chosenAnswerNum" class="text-warning" title="Student Answer">0</span>&nbsp;/&nbsp;<span id="numQuestion" name="numQuestion" title="Total Question">'+ numQuestion +'</span></div>';
    container.prepend(header); // Use prepend to ensure the header is the first child of .answerSheet
    for (var i = 1; i <= numQuestion; i++) {
        var questionDiv = $('<div>').addClass('mt-5 mb-4');
        var questionLabel = $('<div>').addClass('form-check form-check-inline h5 ml-1').text(' ' + i + '. ');
        questionLabel.css('width', '30px');
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
    var footer = '<div><button type="submit" class="btn btn-primary w-100" onclick="checkAnswer(' + 1 + ', ' +  numQuestion +')">SUBMIT</button></div>';
    container.append(footer);

    // pop-up pdf & answer sheet
    $('#practiceModal').modal('show');
         
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



</script>

<!-- Assessment -->
<div class="" style="height: 90vh; width: 90%; margin: 0 auto;">
    <div class="bg-light">
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
</div>



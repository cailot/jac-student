<!-- Add PDF.js library -->
<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>

<style>
    input[type="radio"]{
        transform: scale(1.5);
        margin-right: 4px;
    }

    .pdfViewerContainer {
        height: 100%;
        overflow: auto;
        display: flex;
        justify-content: center;
        align-items: flex-start;
    }

    canvas.pdfCanvas {
        display: block;
        width: 100%;
        height: auto;
    }

    .answer-container {
        height: 100vh;
        display: flex;
        flex-direction: column;
        background: white;
        padding: 15px;
    }

    .answer-header {
        background: #007bff;
        color: white;
        padding: 10px;
        text-align: center;
        font-size: 1.2em;
        border-radius: 4px;
        margin-bottom: 15px;
    }

    .answerSheet {
        flex: 1;
        overflow-y: auto;
        padding: 0 10px;
        margin-bottom: 15px;
    }

    .question-row {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        height: 40px;
    }

    .question-number {
        width: 45px;
        font-weight: bold;
        font-size: 1.3em;
    }

    .options-group {
        display: flex;
        gap: 35px;
        align-items: center;
    }

    .form-check {
        margin: 0;
        display: flex;
        align-items: center;
        height: 100%;
    }

    .form-check-label {
        margin-left: 6px;
        font-size: 1.3em;
        font-weight: 500;
    }

    .submit-btn {
        background: #007bff;
        color: white;
        border: none;
        padding: 12px;
        width: 100%;
        font-size: 1.2em;
        border-radius: 4px;
        cursor: pointer;
    }

    .submit-btn:hover {
        background: #0056b3;
    }
</style>

<script>
var pdfDoc = null;
let pageNum = 1;
let scale = 1.5;

function loadPdf(pdfPath) {
    pdfjsLib.GlobalWorkerOptions.workerSrc = '${pageContext.request.contextPath}/js/pdf.worker-2.16.105.min.js';

    pdfjsLib.getDocument(pdfPath).promise.then((pdf) => {
        pdfDoc = pdf;
        document.getElementById("totalPages").textContent = pdf.numPages;
        renderPage(pageNum);
    });
}

function renderPage(num) {
    const canvas = document.getElementById("pdfCanvas");
    const ctx = canvas.getContext("2d");

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    pdfDoc.getPage(num).then((page) => {
        const viewport = page.getViewport({ scale: scale });
        canvas.height = viewport.height;
        canvas.width = viewport.width;

        const renderContext = {
            canvasContext: ctx,
            viewport: viewport,
        };

        page.render(renderContext).promise.then(() => {
            document.getElementById("currentPage").textContent = num;
            document.getElementById("prevPage").disabled = num <= 1;
            document.getElementById("nextPage").disabled = num >= pdfDoc.numPages;
        });
    });
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Pdf/Answer Sheet)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function display(url) {
    // Load PDF
    pageNum = 1;
    loadPdf(url);
    
    // Set up PDF navigation controls
    document.getElementById("prevPage").onclick = () => {
        if (pageNum > 1) {
            pageNum--;
            renderPage(pageNum);
        }
    };
    document.getElementById("nextPage").onclick = () => {
        if (pageNum < pdfDoc.numPages) {
            pageNum++;
            renderPage(pageNum);
        }
    };
    document.getElementById("zoomIn").onclick = () => {
        scale += 0.1;
        renderPage(pageNum);
    };
    document.getElementById("zoomOut").onclick = () => {
        if (scale > 0.1) {
            scale -= 0.1;
            renderPage(pageNum);
        }
    };

    // manipulate answer sheet
    var numQuestion = 20;
    var container = $('.answerSheet');
    container.empty();
    
    // Add header
    var header = '<div class="answer-header">Answers <span id="chosenAnswerNum">0</span> / <span id="numQuestion">'+ numQuestion +'</span></div>';
    container.parent().prepend(header);

    // Create answers
    for (var i = 1; i <= numQuestion; i++) {
        var questionDiv = $('<div>').addClass('question-row');
        var questionLabel = $('<div>').addClass('question-number').text(i + '.');
        var optionsGroup = $('<div>').addClass('options-group');
        
        ['A', 'B', 'C', 'D'].forEach(function(option) {
            var optionDiv = $('<div>').addClass('form-check');
            var input = $('<input>').addClass('form-check-input').attr({
                type: 'radio',
                name: 'question' + i,
                value: option
            });
            var label = $('<label>').addClass('form-check-label').text(option);
            optionDiv.append(input, label);
            optionsGroup.append(optionDiv);
        });
        
        questionDiv.append(questionLabel, optionsGroup);
        container.append(questionDiv);
    }

    $('.form-check-input').on('change', function() {
        var chosenAnswerNum = $('input[type=radio]:checked').length;
        $('#chosenAnswerNum').text(chosenAnswerNum);
    });

    // Add submit button
    container.after('<button class="submit-btn" onclick="checkAnswer()">SUBMIT</button>');
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Submit Answer
////////////////////////////////////////////////////////////////////////////////////////////////////////
var isSubmitting = false; // Flag to prevent double-click

function checkAnswer() {
    // Prevent double-click
    if (isSubmitting) {
        console.log('Already submitting, please wait...');
        return;
    }
    
    isSubmitting = true;
    
    // Disable submit button and show loading state
    $('.submit-btn').prop('disabled', true).text('SUBMITTING...');
    
    // Collect all the selected answers
    var answers = [];
    for (var i = 1; i <= 20; i++) {
        var selectedOption = $('input[name=question' + i + ']:checked').val();
        // Convert A,B,C,D to 1,2,3,4
        var answer = 0; // Default to 0 if nothing selected
        if (selectedOption) {
            answer = selectedOption.charCodeAt(0) - 'A'.charCodeAt(0) + 1;
        }
        answers.push({
            question: i,
            answer: answer
        });
    }
    //Make an AJAX call to send the data to the server
    $.ajax({
        url: '${pageContext.request.contextPath}/assessment/markAssessment',
        method: 'POST',
        data: JSON.stringify({
            studentId : studentId,
            assessId : assessId,
            answers : answers
        }),
        contentType: 'application/json',
        success: function(response) {
            // Redirect to the URL provided by the server
            if (response.redirectUrl) {
                var math = response.MAT ? true : false;
                var english = response.ENG ? true : false;
                var ga = response.GA ? true : false;
                window.location.href = response.redirectUrl+'?id='+studentId+'&grade='+grade +'&math='+math+'&english='+english+'&ga='+ga;
            } else {
                // Reload the page if no redirect URL is provided
                location.reload(true);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
            // Re-enable submit button on error
            isSubmitting = false;
            $('.submit-btn').prop('disabled', false).text('SUBMIT');
        }
    });
}

</script>

<!-- Assessment -->
<div style="height: 100vh; width: 100%; margin: 0;">
    <div class="h-100">
        <div class="row h-100 no-gutters">
            <div class="col-md-9 bg-white p-3">
                <div class="pdf-toolbar mb-2">
                    <button id="prevPage">Previous</button>
                    <span>Page: <span id="currentPage">1</span> / <span id="totalPages">1</span></span>
                    <button id="nextPage">Next</button>
                    <button id="zoomOut">-</button>
                    <button id="zoomIn">+</button>
                </div>
                <div class="pdfViewerContainer">
                    <canvas id="pdfCanvas" class="pdfCanvas"></canvas>
                </div>
            </div>
            <div class="col-md-3 p-0 h-100">
                <div class="answer-container">
                    <!-- Answer sheet content will be inserted here -->
                    <div class="answerSheet"></div>
                </div>
            </div>
        </div>
    </div>
</div>



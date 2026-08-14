<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>

<style>
    #homeworkModal .modal-dialog {
        max-width: 80%;
        height: 90vh;
    }
    #homeworkModal .modal-body {
        overflow-y: auto;
    }
    #homeworkModal .modal-body .row {
        min-height: 72vh;
        align-items: center;
    }
    #homeworkModal .material-panel {
        min-height: 62vh;
        justify-content: center;
    }
    @media (max-width: 768px) {
        #homeworkModal .modal-dialog {
            max-width: 95%;
            width: 95%;
            margin: 1rem auto;
        }
    }
</style>
<script>

const SUBJECT = 12; // 12 is Short Answer 
var weeksData = [];

$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/class/academy',
        method: "GET",
        success: function(response) {
            // save the response into the variable
            academicYear = response[0];
            academicWeek = parseInt(response[1]);
            console.log('NumericGrade ---> ' + numericGrade);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });

    console.log('Enrol Grade ---> ' + enrolGrade);
    // get week info
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/shortAnswerList/' + SUBJECT + "/" + studentId,
        method: "GET",
        success: function(response) {
            // save the response into the variable
            console.log(response);
            weeksData = response;
            displayCards();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

var pdfDoc = null;
let pageNum = 1;
let scale = 1.0;
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//          Load Practice PDF
////////////////////////////////////////////////////////////////////////////////////////////////////////////
function loadPdf(pdfPath) {
    // Set the workerSrc before loading the PDF
    pdfjsLib.GlobalWorkerOptions.workerSrc = '${pageContext.request.contextPath}/js/pdf.worker-2.16.105.min.js';
 
    pdfjsLib.getDocument(pdfPath).promise.then((pdf) => {
        pdfDoc = pdf;
        document.getElementById("totalPages").textContent = pdf.numPages;
        return pdf.getPage(pageNum); // pageNum is 1 on first load
    }).then(page => {
        // We are in 'shown.bs.modal', so the container is visible.
        const container = document.querySelector('.pdfViewerContainer');
        if (container && container.clientWidth > 0) {
            const viewport = page.getViewport({ scale: 1.0 });
            scale = container.clientWidth / viewport.width;
        }
        renderPage(pageNum);
    }).catch(err => {
        console.error("Error loading PDF for scaling: ", err);
    });
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//          Render Practice PDF
////////////////////////////////////////////////////////////////////////////////////////////////////////////
function renderPage(num) {
    const canvas = document.getElementById("pdfCanvas");
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
            document.getElementById("currentPage").textContent = num;
            document.getElementById("prevPage").disabled = num <= 1;
            document.getElementById("nextPage").disabled = num >= pdfDoc.numPages;
        }).catch((err) => {
            console.log("Error rendering page:", err);
        });
    }).catch((err) => {
        console.log("Error loading page:", err);
    });
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(homeworkId) {
    var year = document.getElementById("academicYear").value;
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/homework/' + homeworkId,
        method: "GET",
        success: function(value) {
            // console.log(value);
            // Render the PDF
            const pdfPath = value.pdfPath;
            $('#homeworkModal').off('shown.bs.modal'); // Remove previous modal event
            $('#homeworkModal').on('shown.bs.modal', function () {            
                pageNum = 1; // Reset page
                loadPdf(pdfPath);
                // Ensure event listeners are not duplicated
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
                    console.log('Zoom In: ', scale);
                    renderPage(pageNum);
                };
                document.getElementById("zoomOut").onclick = () => {
                    if (scale > 0.1) {
                        scale -= 0.1;
                        console.log('Zoom Out: ', scale);
                        renderPage(pageNum);
                    }
                };
            });
            // Open the modal
            document.getElementById("mediaModalLabel").innerHTML = 'Short Answer - Set <span class="text-warning">' + value.week + "</span>";
            $('#homeworkModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });  
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Create Card
////////////////////////////////////////////////////////////////////////////////////////////////////////
function createCard(weekData) {
    const card = document.createElement('div');
    card.className = 'col-md-6';
    const homeworkId = parseInt(weekData.id, 10);
    const isClickable = Number.isFinite(homeworkId) && homeworkId > 0;
    card.innerHTML = `
        <div class="card-body mx-auto" style="max-width: 75%;` + (isClickable ? ' cursor: pointer;' : '') + `" ` + (isClickable ? `onclick="displayMaterial(` + homeworkId + `)"` : '') + `>
            <div class="alert ` + (isClickable ? 'alert-info' : 'alert-secondary') + ` english-homework" role="alert" style="background-color: ` + (isClickable ? 'false' : 'lightgrey') + `;">
                <p id="` + weekData.week + `OnlineLesson" style="margin: 30px;">
                    <strong>Set</strong> <span>` + weekData.week +`</span>&nbsp;&nbsp;<i class="bi ` + (isClickable ? 'bi-journal-text h3 text-primary' : 'bi-lock-fill h3 text-secondary') + `"></i>
                </p>
            </div>
        </div>
    `;
    return card;
}

function createNextCard(week) {
    const card = document.createElement('div');
    card.className = 'col-md-6';
    card.innerHTML = `
        <div class="card-body mx-auto" style="max-width: 75%;">
            <div class="alert alert-secondary english-homework" role="alert" style="background-color: lightgrey;">
                <p style="margin: 30px;">
                    <strong>Set</strong> <span>` + week + `</span>
                    &nbsp;&nbsp;<i class="bi bi-lock-fill h3 text-secondary"></i>
                </p>
            </div>
        </div>
    `;
    return card;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Card
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayCards() {
    const container = document.getElementById('cardsContainer');
    weeksData.forEach(weekData => {
        const card = createCard(weekData);
        container.appendChild(card);
    });
    // week 1 short answer shows Set49 with Set1 locked; otherwise lock the next visible set.
    const weekNumbers = weeksData
        .map(item => parseInt(item.week, 10))
        .filter(Number.isFinite);
    let lockedWeek = academicWeek + 1;
    if (weekNumbers.includes(49)) {
        lockedWeek = 1;
    } else if (weekNumbers.length > 0) {
        lockedWeek = Math.max(...weekNumbers) + 1;
    }
    const nextCard = createNextCard(lockedWeek);
    container.appendChild(nextCard);
}

</script>
<input type="hidden" id="academicYear" name="academicYear" />
<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(247, 247, 161, 1);">Short Answer</h2>
    </div>
</div>
<div class="container mt-3" style="background: linear-gradient(to right, #f9f9d5 0%, #f7f7a1 100%); border-radius: 15px;">   
    <div id="cardsContainer" class="row mt-5"></div>
</div> 

<!-- Pop up Video modal -->
<div class="modal fade" id="homeworkModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"  data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 95vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="mediaModalLabel"></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body bg-light">
                <div class="row">
                    <div class="col-md-12 d-flex flex-column material-panel bg-white p-1 border">
                        <div class="pdf-toolbar">
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
                </div>
            </div>
            <div class="modal-footer bg-dark text-white">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

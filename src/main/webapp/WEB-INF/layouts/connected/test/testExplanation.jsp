<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>
<style>
    /* Make the modal take 90% of the viewport height */
    .modal-dialog {
        display: flex;
        align-items: center; /* Vertically center modal */
        justify-content: center;
        height: 90vh; /* 90% of the viewport height */
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
        height: 75vh; /* Ensure the content takes 90% height */
        overflow: hidden; /* Prevent content overflow */
    }

    .modal-body {
        height: calc(100% - 120px); /* Adjust for header and footer height */
        overflow-y: auto; /* Enable scrolling for content */
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

function loadPdfWhenReady(pdfPath, retryCount) {
    const container = document.querySelector('#explanationModal .pdfViewerContainer');
    const maxRetry = 10;
    const waitMs = 80;

    if (!container || container.clientWidth <= 0) {
        if (retryCount < maxRetry) {
            setTimeout(function () {
                loadPdfWhenReady(pdfPath, retryCount + 1);
            }, waitMs);
        } else {
            // Fallback: try rendering with default scale even if width cannot be measured
            loadPdf(pdfPath);
        }
        return;
    }

    loadPdf(pdfPath);
}

window.showWarning = function(id, title) {
    // Show the warning modal
    $('#testWarningModal').modal('show');
    // Attach the click event handler to the "I agree" button
    $('#agreeTestWarning').one('click', function() {
        displayMaterial(id, title);
        $('#testWarningModal').modal('hide');
    });
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//          Load PDF
////////////////////////////////////////////////////////////////////////////////////////////////////////////
function loadPdf(pdfPath) {
    // Set the workerSrc before loading the PDF
    pdfjsLib.GlobalWorkerOptions.workerSrc = '${pageContext.request.contextPath}/js/pdf.worker-2.16.105.min.js';
    
    pdfjsLib.getDocument(pdfPath).promise.then(pdf => {
        pdfDoc = pdf;
        document.getElementById("totalPages").textContent = pdf.numPages;
        return pdf.getPage(1);
    }).then(page => {
        const container = document.querySelector('#explanationModal .pdfViewerContainer');
        if (container && container.clientWidth > 0) {
            const viewport = page.getViewport({ scale: 1.0 });
            scale = container.clientWidth / viewport.width;
        }
        renderPage(pageNum);
    }).catch(err => {
        console.error("Error loading explanation PDF for scaling: ", err);
    });
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//          Render Test PDF
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
// 			Display Material (Video/Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(testId, title) {
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/getTestAnswer/' + testId,
        method: "GET",
        success: function (test) {
            console.log(test);
            const pdfPath = test.pdfPath;
            $('#explanationModal').off('shown.bs.modal'); // Remove previous modal event
            $('#explanationModal').on('shown.bs.modal', function () {            
                pageNum = 1; // Reset page
                loadPdfWhenReady(pdfPath, 0);
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
            // Add this part for displaying played percentage
            var videoPlayer = document.getElementById("videoPlayer");
            videoPlayer.src = test.videoPath;
            
            // Auto-play the video when it loads
            videoPlayer.addEventListener('loadeddata', function() {
                videoPlayer.play().catch(function(error) {
                    console.log('Auto-play failed:', error);
                });
            });

            // Stop the video when the modal is closed
            $('#explanationModal').on('hidden.bs.modal', function () {
                var videoPlayer = document.getElementById("videoPlayer");
                if (videoPlayer) {
                    videoPlayer.pause();
                    videoPlayer.src = ""; // Clear the video source
                }
            });

            // Open the modal
            document.getElementById("mediaModalLabel").innerHTML = '<span class="text-warning" style="font-weight: bold;">'+ title + '</span> Test Explanation';
            $('#explanationModal').modal('show');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("Error: " + errorThrown);
        },
    });
}

</script>

<!-- Pop up Test Explanation modal -->
<div class="modal fade" id="explanationModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"  data-backdrop="static" data-keyboard="false">
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
                    <div class="col-md-6 d-flex justify-content-center bg-white p-3 border">
                        <video id="videoPlayer" controls controlsList="nodownload" autoplay style="width: 100%; height: auto;">
                            <source src="" type="video/mp4">
                        </video>
                    </div>
                    <div class="col-md-6 bg-white p-1 border">
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
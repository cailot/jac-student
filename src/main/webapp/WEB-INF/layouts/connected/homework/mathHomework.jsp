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
    #homeworkModal .video-panel {
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

const SUBJECT = 2; // 2 is Math Homework 
var weeksData = [];

$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/class/academy',
        method: "GET",
        success: function(response) {
            // save the response into the variable
            academicYear = response[0];
            academicWeek = parseInt(response[1]);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });

    // get week info
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/subjectList/' + SUBJECT + "/" + studentId,
        method: "GET",
        success: function(response) {
            // save the response into the variable
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
// 			Display Material (Video/Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(homeworkId) {
    var year = document.getElementById("academicYear").value;
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/homework/' + homeworkId,
        method: "GET",
        success: function(value) {
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

            // Add this part for displaying played percentage
            var videoPlayer = document.getElementById("videoPlayer");
            videoPlayer.src = value.videoPath;
            
            // Auto-play the video when it loads
            videoPlayer.addEventListener('loadeddata', function() {
                videoPlayer.play().catch(function(error) {
                    console.log('Auto-play failed:', error);
                });
            });
            var progressPercentage = document.getElementById(homeworkId+"Percentage");
            var progressBar = document.getElementById(homeworkId+"Bar");
            // Define the event listener function
            var updateProgressBar = function() {
                var playedPercentage = Math.round((videoPlayer.currentTime / videoPlayer.duration) * 100);
                if(!playedPercentage || isNaN(playedPercentage)){
                    progressPercentage.innerHTML = "0%";
                    progressBar.style.width = "0%";
                } else {
                    progressPercentage.innerHTML = playedPercentage + "%";
                    progressBar.style.width = playedPercentage + "%";
                    progressBar.className = getProgressBarClass(playedPercentage);
                }
            }

            // Add the event listener when the video starts playing
            videoPlayer.addEventListener('timeupdate', updateProgressBar);

            var updateCalled = false;
            function updateProgress() {
                if (!updateCalled) {
                    updateCalled = true;
                    updateProgressOnServer(homeworkId, progressPercentage.innerHTML);
                }
            }

            videoPlayer.addEventListener("ended", updateProgress);

            // Remove the event listener when the modal is closed
            $('#homeworkModal').on('hidden.bs.modal', function () {
                videoPlayer.removeEventListener('timeupdate', updateProgressBar);
                // Pause and reset the video when modal is closed
                videoPlayer.pause();
                videoPlayer.src = ""; // Clear the video source
                updateProgress();
            });

            // Update progress on the server when the user navigates away or closes the browser
            window.addEventListener('beforeunload', updateProgress);

            // Open the modal
            document.getElementById("mediaModalLabel").innerHTML = 'Mathematics Homework Details - Set <span class="text-warning">' + value.week + "</span>";
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
    const weekNumber = parseInt(weekData.week, 10);
    const isWeekTwoMode = weeksData.some(item => parseInt(item.week, 10) === 2);
    const disableSet49InWeekTwo = isWeekTwoMode && weekNumber === 49;
    const isClickable = Number.isFinite(homeworkId) && homeworkId > 0 && !disableSet49InWeekTwo;
    card.innerHTML = `
        <div class="card-body mx-auto" style="max-width: 75%;` + (isClickable ? ' cursor: pointer;' : '') + `" ` + (isClickable ? `onclick="displayMaterial(` + homeworkId + `)"` : '') + `>
            <div class="alert ` + (isClickable ? 'alert-info' : 'alert-secondary') + ` english-homework" role="alert" style="background-color: ` + (isClickable ? 'false' : 'lightgrey') + `;">
                <p id="` + weekData.week + `OnlineLesson" style="margin: 30px;">
                    <strong>Set</strong> <span>` + weekData.week +`</span>&nbsp;&nbsp;<i class="bi ` + (isClickable ? 'bi-mortarboard-fill text-primary' : 'bi-lock-fill text-secondary') + ` h3"></i>
                </p>
                <div class="progress" style="margin: 30px;">
                    <div id="` + (isClickable ? weekData.id : ('locked_' + weekData.week)) + `Bar" class="`+ (isClickable ? getProgressBarClass(weekData.percentage) : 'progress-bar') +`" role="progressbar" style="width: ` + (isClickable ? weekData.percentage : 0) + `%;" aria-valuemin="0" aria-valuemax="100">
                        <span id="` + (isClickable ? weekData.id : ('locked_' + weekData.week)) + `Percentage" class="ml-auto ` + (isClickable ? 'pl-2' : 'text-secondary pl-2') + `">` + (isClickable ? weekData.percentage : 0) +`%</span>
                    </div>
                </div>
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
                <div class="progress" style="margin: 30px;">
                    <div class="progress-bar" role="progressbar" style="width: 0%;" aria-valuemin="0" aria-valuemax="100">
                        <span class="ml-auto text-secondary pl-2">0%</span>
                    </div>
                </div>
            </div>
        </div>
    `;
    return card;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Color Progress Bar
////////////////////////////////////////////////////////////////////////////////////////////////////////
function getProgressBarClass(percentage) {
    if (percentage < 30) {
        return 'progress-bar bg-danger'; // Red color for less than 30%
    } else if (percentage >= 30 && percentage <= 70) {
        return 'progress-bar bg-warning'; // Yellow color for 30% - 70%
    } else {
        return 'progress-bar bg-success'; // Green color for more than 70%
    }
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
    // Always lock the next set after the last valid (clickable) set.
    const isWeekTwoMode = weeksData.some(item => parseInt(item.week, 10) === 2);
    const validWeeks = weeksData
        .filter(item => {
            const weekNumber = parseInt(item.week, 10);
            const homeworkId = parseInt(item.id, 10);
            if (!Number.isFinite(weekNumber) || !Number.isFinite(homeworkId) || homeworkId <= 0) {
                return false;
            }
            const disableSet49InWeekTwo = isWeekTwoMode && weekNumber === 49;
            return !disableSet49InWeekTwo;
        })
        .map(item => parseInt(item.week, 10));
    const lastValidWeek = validWeeks.length > 0 ? Math.max(...validWeeks) : academicWeek;
    const lockedWeek = lastValidWeek >= 49 ? 1 : (lastValidWeek + 1);
    const nextCard = createNextCard(lockedWeek);
    container.appendChild(nextCard);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Update Progress
////////////////////////////////////////////////////////////////////////////////////////////////////////
function updateProgressOnServer(homeworkId, percentage){
    // Remove the '%' symbol and convert to integer
    var percentageValue = parseInt(percentage.replace('%', ''), 10);
    // console.log('Updating progress:', homeworkId, percentageValue, studentId);
   
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/updateHomeworkProgress',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            homeworkId: homeworkId,
            studentId: studentId,
            percentage: percentageValue
        }),
        success: function(response) {
            console.log('Progress updated successfully:', response);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error updating progress:', errorThrown);
        }
    });
}

</script>

<input type="hidden" id="academicYear" name="academicYear" />
<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(247, 247, 161, 1);">Maths Homework</h2>
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
                    <div class="col-md-6 d-flex justify-content-center video-panel bg-white p-3 border">
                        <video id="videoPlayer" controls controlsList="nodownload" autoplay style="width: 100%; height: auto;">
                            <source src="" type="video/mp4">
                        </video>
                    </div>
                    <div class="col-md-6 d-flex flex-column material-panel bg-white p-1 border">
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
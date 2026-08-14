<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>

<style>
    .topic-card {
        background-color: #d1ecf1; 
        padding: 20px; 
        border-radius: 10px; 
        box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); 
    }
    /* Define CSS styles for the decorated-header class */
    .decorated-header {
        font-size: 25px; /* Change the font size */
        color: #007bff; /* Change the text color */
        text-shadow: 1px 1px 2px #000; /* Add a text shadow for depth */
        font-weight: bold; /* Make the text bold */
        text-align: left; /* Center-align the text */
        margin-left: 50px;
        margin-bottom: 10px; /* Add some space below the header */
    }
    #materialModal .modal-dialog {
        max-width: 80%;
        height: 90vh;
    }
    #materialModal .modal-body {
        overflow-y: auto;
    }
    @media (max-width: 768px) {
        #materialModal .modal-dialog {
            max-width: 95%;
            width: 95%;
            margin: 1rem auto;
        }
    }
</style>
<script>
$(function() {
    // check if student or Admin/Staff
    if(isStudent){
        $.ajax({
            url : '${pageContext.request.contextPath}/connected/summaryExtrawork/' + studentId,
            method: "GET",
            success: function(data) {

                $.each(data, function(index, basket) {
                    var title = basket.title;
                    var id = basket.id;
                    var percentage = basket.percentage;

                console.log(basket);
                    var topicDiv = '<div class="col-md-4">'		
                    +  '<div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(' + id + ', \'' +  title + '\');">'
                    + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin: 30px;">'
                    + '<strong><span id="topicTitle">' + title + '</span></strong>&nbsp;&nbsp;<i class="bi bi-calculator h3 text-primary"></i></p>'
                    + '<div class="progress" style="margin: 30px;"><div id="' + id + 'topicPercentageBar" class="' + getProgressBarClass(percentage) +'" role="progressbar" style="width: '+ percentage +'%;" aria-valuemin="0" aria-valuemax="100">'
                    + '<span id="'+ id + 'topicPercentage" class="ml-auto">'+ percentage +'%</span></div></div></div></div></div>';
                    
                    $('#topicContainer').append(topicDiv);    
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('Error : ' + errorThrown);
            }
        });
    }else{ // Administrator or Staff
        // call all from P2 to S10    
        // Define an async function to make AJAX calls and return promises
        function fetchSummaryExtrawork(i) {
            return $.ajax({
                url: '${pageContext.request.contextPath}/connected/summaryExtraworkAll/' + i,
                method: "GET"
            }).then(data => {
                return { iteration: i, data: data }; // Return both iteration and data for ordering
            });
        }
        // Collect all promises
        let promises = [];
        for (let i = 1; i <= 9; i++) {
            promises.push(fetchSummaryExtrawork(i));
        }
        // Use Promise.all to wait for all AJAX calls to complete
        Promise.all(promises).then(results => {
            // Sort results by iteration to ensure correct order
            results.sort((a, b) => a.iteration - b.iteration).forEach(result => {
                var iteration = result.iteration;
                var data = result.data;
                var headerText = 'Year ' + (iteration + 1); // Adjusted to use iteration directly
                var headerDiv = '<div class="col-12"><h2 class="decorated-header">' + headerText + '</h2></div>';
                $('#topicContainer').append(headerDiv);
                $.each(data, function(index, basket) {
                    var title = basket.name;
                    var id = basket.value;
                    var percentage = basket.percentage;
                    var topicDiv = '<div class="col-md-4">'
                    + '<div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(' + id + ', \'' + title + '\');">'
                    + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin: 30px;">'
                    + '<strong><span id="topicTitle">' + title + '</span></strong>&nbsp;&nbsp;<i class="bi bi-calculator h3 text-primary"></i></p>'
                    + '<div class="progress" style="margin: 30px;"><div id="' + id + 'topicPercentageBar" class="" role="progressbar" style="width: 0%;" aria-valuemin="0" aria-valuemax="100">'
                    + '<span id="'+ id + 'topicPercentage" class="ml-auto">0%</span></div></div></div></div></div>';
            
                    $('#topicContainer').append(topicDiv);
                });
            });
        }).catch(error => console.error('Error fetching data:', error));
    }

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
function displayMaterial(id, title) {
    // set dialogTitle value
    document.getElementById("dialogTitle").innerHTML = title;  
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/getExtrawork/' + id,
        method: "GET",
        success: function(value) {

            console.log(value);

            // Add this part for displaying played percentage
            var videoPlayer = document.getElementById("videoPlayer");
            videoPlayer.src = value.videoPath;
            
            // Auto-play the video when it loads
            videoPlayer.addEventListener('loadeddata', function() {
                videoPlayer.play().catch(function(error) {
                    console.log('Auto-play failed:', error);
                });
            });

            var progressPercentage = document.getElementById(id + "topicPercentage");
            var progressBar = document.getElementById(id + "topicPercentageBar");

            // Define the event listener function
            var updateProgressBar = function() {
                var playedPercentage = Math.round((videoPlayer.currentTime / videoPlayer.duration) * 100);
                if (!playedPercentage || isNaN(playedPercentage)) {
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
                    updateProgressOnServer(id, progressPercentage.innerHTML);
                }
            }

            videoPlayer.addEventListener("ended", updateProgress);

            // Remove the event listener and stop the video when the modal is closed
            $('#materialModal').on('hidden.bs.modal', function () {
                videoPlayer.removeEventListener('timeupdate', updateProgressBar);
                // Pause and reset the video when modal is closed
                videoPlayer.pause();
                videoPlayer.src = ""; // Clear the video source
                updateProgress();
            });

            // Update progress on the server when the user navigates away or closes the browser
            window.addEventListener('beforeunload', updateProgress);

            // Render the PDF
            const pdfPath = value.pdfPath;
            $('#materialModal').off('shown.bs.modal'); // Remove previous modal event
            $('#materialModal').on('shown.bs.modal', function () {            
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

            // Pop-up video & pdf
            $('#materialModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error: ' + errorThrown);
        }
    });  
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
// 			Update Progress
////////////////////////////////////////////////////////////////////////////////////////////////////////
function updateProgressOnServer(extraworkId, percentage){
    // Remove the '%' symbol and convert to integer
    var percentageValue = parseInt(percentage.replace('%', ''), 10);
    // console.log('Updating progress:', homeworkId, percentageValue, studentId);
   
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/updateExtraworkProgress',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            extraworkId: extraworkId,
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

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(58, 232, 22, 1);">Math Topics</h2>
    </div>
</div>
<div class="container mt-3" style="background: linear-gradient(to right, #e6fde2 0%, #b9fba8 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5"></div>
</div>

<!-- Pop up Video modal -->
<div class="modal fade" id="materialModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="exampleModalLabel">Math Topics - <span id="dialogTitle" name="dialogTitle" class="text-warning"></span></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body bg-light">
                <div class="row align-items-center" style="height: 70vh; min-height: 400px;">
                    <div class="col-md-6 d-flex align-items-center justify-content-center bg-white p-3 border" style="height: 100%;">
                        <video id="videoPlayer" controls controlsList="nodownload" autoplay style="width: 100%; height: auto; max-height: 60vh;">
                            <source src="" type="video/mp4">
                        </video>
                    </div>
                    <div class="col-md-6 d-flex align-items-center justify-content-center bg-white p-3 border" style="height: 100%;">
                        <div style="width: 100%;">
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
            </div>
            <div class="modal-footer bg-dark text-white">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

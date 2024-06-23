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
</style>
<script>
$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryExtrawork/' + numericGrade,
        method: "GET",
        success: function(data) {

            $.each(data, function(index, basket) {
				var title = basket.name;
                var id = basket.value;
               // console.log(basket);
                var topicDiv = '<div class="col-md-4">'
                +  '<div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(' + id + ', \'' +  title + '\');">'
                + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin: 30px;">'
                + '<strong><span id="topicTitle">' + title + '</span></strong>&nbsp;&nbsp;<i class="bi bi-calculator h5 text-primary"></i></p>'
                + '<div class="progress" style="margin: 30px;"><div id="' + id + 'topicPercentageBar" class="" role="progressbar" style="width: 0%;" aria-valuemin="0" aria-valuemax="100">'
                + '<span id="'+ id + 'topicPercentage" class="ml-auto">0%</span></div></div></div></div></div>';
                $('#topicContainer').append(topicDiv);    
			});
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

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
            // Add this part for displaying played percentage
            var videoPlayer = document.getElementById("videoPlayer");
            videoPlayer.src = value.videoPath;

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
                    if (playedPercentage < 30) {
                        progressBar.className = 'progress-bar bg-danger'; // Red color for less than 30%
                    } else if (playedPercentage >= 30 && playedPercentage <= 70) {
                        progressBar.className = 'progress-bar bg-warning'; // Yellow color for 30% - 70%
                    } else {
                        progressBar.className = 'progress-bar bg-success'; // Green color for more than 70%
                    }
                }
            }

            // Add the event listener when the video starts playing
            videoPlayer.addEventListener('timeupdate', updateProgressBar);

            videoPlayer.addEventListener("ended", function() {
                // Video ended, you can perform additional actions if needed
                console.log("Video ended");
            });

            // Remove the event listener and stop the video when the modal is closed
            $('#materialModal').on('hidden.bs.modal', function () {
                videoPlayer.removeEventListener('timeupdate', updateProgressBar);
                videoPlayer.pause(); // Stop the video
                videoPlayer.currentTime = 0; // Reset the video time
            });

            // Force reload the PDF by removing and re-adding the object element
            var pdfContainer = document.getElementById("pdfViewer").parentNode;
            var oldPdfViewer = document.getElementById("pdfViewer");
            var newPdfViewer = document.createElement("object");
            newPdfViewer.id = "pdfViewer";
            newPdfViewer.data = value.pdfPath + '?t=' + new Date().getTime(); // Append timestamp to prevent caching
            newPdfViewer.type = "application/pdf";
            newPdfViewer.style.width = "100%";
            newPdfViewer.style.height = "80vh";
            newPdfViewer.innerHTML = '<p>It appears you don\'t have a PDF plugin for this browser. No biggie... you can <a href="' + value.pdfPath + '">click here to download the PDF file.</a></p>';
            pdfContainer.replaceChild(newPdfViewer, oldPdfViewer);

            // Pop-up video & pdf
            $('#materialModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error: ' + errorThrown);
        }
    });  
}


function displayMaterial1(id, title) {
    // set dialogTitle value
    document.getElementById("dialogTitle").innerHTML = title;  
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/getExtrawork/' + id,
        method: "GET",
        success: function(value) {
            // Add this part for displaying played percentage
            var videoPlayer = document.getElementById("videoPlayer");
            videoPlayer.src = value.videoPath;

            var progressPercentage = document.getElementById(id+"topicPercentage");
            var progressBar = document.getElementById(id+"topicPercentageBar");

            // Define the event listener function
            var updateProgressBar = function() {
                var playedPercentage = Math.round((videoPlayer.currentTime / videoPlayer.duration) * 100);
                if(!playedPercentage || isNaN(playedPercentage)){
                    progressPercentage.innerHTML = "0%";
                    progressBar.style.width = "0%";
                } else {
                    progressPercentage.innerHTML = playedPercentage + "%";
                    progressBar.style.width = playedPercentage + "%";
                    if(playedPercentage < 30){
                        progressBar.className = 'progress-bar bg-danger'; // Red color for less than 30%
                    } else if(playedPercentage >= 30 && playedPercentage <= 70){
                        progressBar.className = 'progress-bar bg-warning'; // Yellow color for 30% - 70%
                    } else {
                        progressBar.className = 'progress-bar bg-success'; // Green color for more than 70%
                    }
                }
            }

            // Add the event listener when the video starts playing
            videoPlayer.addEventListener('timeupdate', updateProgressBar);

            videoPlayer.addEventListener("ended", function() {
                // Video ended, you can perform additional actions if needed
                console.log("Video ended");
            });

            // Remove the event listener when the modal is closed
            $('#materialModal').on('hidden.bs.modal', function () {
                videoPlayer.removeEventListener('timeupdate', updateProgressBar);
            });

            // Force reload the PDF by appending a timestamp to the URL
            var pdfContainer = document.getElementById("pdfViewer").parentNode;
            var oldPdfViewer = document.getElementById("pdfViewer");
            var newPdfViewer = document.createElement("object");
            newPdfViewer.id = "pdfViewer";
            newPdfViewer.data = value.pdfPath + '?t=' + new Date().getTime(); // Append timestamp to prevent caching
            newPdfViewer.type = "application/pdf";
            newPdfViewer.style.width = "100%";
            newPdfViewer.style.height = "80vh";
            newPdfViewer.innerHTML = '<p>It appears you don\'t have a PDF plugin for this browser. No biggie... you can <a href="' + value.pdfPath + '">click here to download the PDF file.</a></p>';
            pdfContainer.replaceChild(newPdfViewer, oldPdfViewer);
              
            // pop-up video & pdf
            $('#materialModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
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
                <div class="row">
                    <div class="col-md-6 d-flex justify-content-center bg-white p-3 border">
                        <video id="videoPlayer" controls controlsList="nodownload" style="width: 100%; height: auto;">
                            <source src="" type="video/mp4">
                        </video>
                    </div>
                    <div class="col-md-6 bg-white p-3 border">
                        <object id="pdfViewer" data="" type="application/pdf" style="width: 100%; height: 80vh;">
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

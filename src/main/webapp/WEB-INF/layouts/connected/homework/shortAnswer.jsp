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
</style>
<script>

const SUBJECT = 12; // 12 is Short Answer 
const MOVIE = 0;
const PDF = 1;

$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/class/academy',
        method: "GET",
        success: function(response) {
            // save the response into the variable
            academicYear = response[0];
            academicWeek = response[1];
            // update the value of the academicWeek span element
            document.getElementById("academicYear").value = parseInt(academicYear);
            document.getElementById("minus2Week").innerHTML = parseInt(academicWeek)-2;
            document.getElementById("minus1Week").innerHTML = parseInt(academicWeek)-1;
            document.getElementById("academicWeek").innerHTML = parseInt(academicWeek);
            // document.getElementById("plus1Week").innerHTML = parseInt(academicWeek)+1;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Video/Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(weekNumber, elementId) {
    // set dialogSet value as weekNumber
    document.getElementById("dialogSet").innerHTML = weekNumber;  
    var year = document.getElementById("academicYear").value;
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/homework/' + SUBJECT + "/" + year + "/" + weekNumber,
        method: "GET",
        success: function(value) {
            // Add this part for displaying played percentage
            // var videoPlayer = document.getElementById("videoPlayer");
            // videoPlayer.src = value.videoPath;

            // var progressPercentage = document.getElementById(elementId);
            // var progressBar = document.getElementById(elementId+"Bar");

            // Define the event listener function
            // var updateProgressBar = function() {
            //     var playedPercentage = Math.round((videoPlayer.currentTime / videoPlayer.duration) * 100);
            //     if(!playedPercentage || isNaN(playedPercentage)){
            //         progressPercentage.innerHTML = "0%";
            //         progressBar.style.width = "0%";
            //     } else {
            //         progressPercentage.innerHTML = playedPercentage + "%";
            //         progressBar.style.width = playedPercentage + "%";
            //         if(playedPercentage < 30){
            //             progressBar.className = 'progress-bar bg-danger'; // Red color for less than 30%
            //         } else if(playedPercentage >= 30 && playedPercentage <= 70){
            //             progressBar.className = 'progress-bar bg-warning'; // Yellow color for 30% - 70%
            //         } else {
            //             progressBar.className = 'progress-bar bg-success'; // Green color for more than 70%
            //         }
            //     }
            // }

            // Add the event listener when the video starts playing
            // videoPlayer.addEventListener('timeupdate', updateProgressBar);

            // videoPlayer.addEventListener("ended", function() {
            //     // Video ended, you can perform additional actions if needed
            //     console.log("Video ended");
            // });

            // Remove the event listener when the modal is closed
            // $('#homeworkModal').on('hidden.bs.modal', function () {
            //     videoPlayer.removeEventListener('timeupdate', updateProgressBar);
            // });
            // console.log('no duration');
            document.getElementById("pdfViewer").data = value.pdfPath;
              
            // pop-up video & pdf
            $('#homeworkModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });  
}

</script>

<input type="hidden" id="academicYear" name="academicYear" />
<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(247, 247, 161, 1);">Short Answer</h2>
    </div>
</div>
<div class="container mt-3" style="background: linear-gradient(to right, #f9f9d5 0%, #f7f7a1 100%); border-radius: 15px;">
    <div class="row mt-5">
        <div class="col-md-6">
            <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(document.getElementById('minus2Week').textContent, 'm2Percentage')">
                <div class="alert alert-info english-homework" role="alert">
                    <p id="m2OnlineLesson" style="margin: 30px;">
                        <strong>Set</strong> <span id="minus2Week"></span>
                        &nbsp;&nbsp;<i class="bi bi-journal-text h5 text-primary"></i>
                    </p>
                    <%--
                    <div class="progress" style="margin: 30px;">
                        <div id="m2PercentageBar" class="" role="progressbar" style="width: 0%;" aria-valuemin="0" aria-valuemax="100">
                            <span id="m2Percentage" class="ml-auto">0%</span>
                        </div>
                    </div>
                    --%>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(document.getElementById('minus1Week').textContent, 'm1Percentage')">
                <div class="alert alert-info english-homework" role="alert">
                    <p id="m1OnlineLesson" style="margin: 30px;">
                        <strong>Set</strong> <span id="minus1Week"></span>
                        &nbsp;&nbsp;<i class="bi bi-journal-text h5 text-primary"></i>
                    </p>
                    <%--
                    <div class="progress" style="margin: 30px;">
                        <div id="m1PercentageBar" class="" role="progressbar" style="width: 0%;" aria-valuemin="0" aria-valuemax="100">
                            <span id="m1Percentage" class="ml-auto">0%</span>
                        </div>
                    </div>
                    --%>
                </div>
            </div>
        </div>
    </div>
    <div class="row mb-4">
        <!-- This Week -->
        <div class="col-md-6">
            <div class="card-body mx-auto" style="max-width: 75%;">
                <div class="alert alert-info english-homework" role="alert" style="background-color: lightgrey;">
                    <p style="margin: 30px;">
                        <strong>Set</strong> <span id="academicWeek"></span>
                    &nbsp;&nbsp;<i class="bi bi-lock-fill h5 text-secondary"></i>
                    </p>
                    <%--
                    <div class="progress" style="margin: 30px;">
                        <div class="progress-bar bg-warning" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                            <span class="ml-auto"></span>
                        </div>
                    </div>
                    --%>
                </div>
            </div>
        </div>
        <div class="offset-md-6"></div>
    </div>
</div>    

<!-- Pop up Video modal -->
<div class="modal fade" id="homeworkModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"  data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 80vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="exampleModalLabel">Short Answer - Set <span id="dialogSet" name="dialogSet" class="text-warning"></span></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body bg-light">
                <div class="row">
                    <%--
                    <div class="col-md-6 d-flex justify-content-center bg-white p-3 border">
                        <video id="videoPlayer" controls controlsList="nodownload" style="width: 100%; height: auto;">
                            <source src="" type="video/mp4">
                        </video>
                    </div>
                    --%>
                    <div class="col-md-12 bg-white p-3 border">
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

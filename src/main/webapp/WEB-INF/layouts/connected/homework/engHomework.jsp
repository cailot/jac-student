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

const SUBJECT = 1; // 1 is English 

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
            document.getElementById("plus1Week").innerHTML = parseInt(academicWeek)+1;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});


/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Assign Attribute (Video/Pdf url & duration)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function assignAttributes(weekNumber, elementId) {
    document.getElementById("videoPlayer").src = video; // 'https://djyb0v5s4sgfh.cloudfront.net/vaiim1/VIC_ENGLISH_WORKBOOK/English Workbook Volume4 level7/7_SE_33_1.mp4';
    document.getElementById("pdfViewer").src = pdf; //'https://vod.writingand.com/documents/pdf/2023/K3/2023_Yr3_Math_Mega_Test_vol_2_-_SC.pdf';
    // set dialogSet value as weekNumber
    document.getElementById("dialogSet").innerHTML = weekNumber;  
    var year = document.getElementById("academicYear").value;
    var week = document.getElementById("academicWeek").textContent;
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/homework/' + SUBJECT + "/" + year + "/" + week,
        method: "GET",
        success: function(response) {
            var video = response.video;
            var pdf = response.pdf;
            var duration = response.duration;

            // pop-up video & pdf
            $('#homeworkModal').modal('show');


        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });  
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Material (Video/Pdf)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayMaterial(weekNumber, video, pdf, duration) {
    document.getElementById("videoPlayer").src = video; // 'https://djyb0v5s4sgfh.cloudfront.net/vaiim1/VIC_ENGLISH_WORKBOOK/English Workbook Volume4 level7/7_SE_33_1.mp4';
    document.getElementById("pdfViewer").src = pdf; //'https://vod.writingand.com/documents/pdf/2023/K3/2023_Yr3_Math_Mega_Test_vol_2_-_SC.pdf';
    // set dialogSet value as weekNumber
    document.getElementById("dialogSet").innerHTML = weekNumber;  
    var year = document.getElementById("academicYear").value;
    var week = document.getElementById("academicWeek").textContent;
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/homework/' + SUBJECT + "/" + year + "/" + week,
        method: "GET",
        success: function(response) {
            var video = response.video;
            var pdf = response.pdf;
            var duration = response.duration;

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
<div class="col-md-12" style="padding: 30px;">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase;">English Homework</h2>
    </div>
</div>
<div class="col-md-6">
    <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(document.getElementById('minus2Week').textContent, document.getElementById('m2OnlineLesson').getAttribute('video-url'), document.getElementById('m2OnlineLesson').getAttribute('pdf-url'), document.getElementById('m2OnlineLesson').getAttribute('duration'))">
        <div class="alert alert-info english-homework" role="alert">
            <p id="m2OnlineLesson" video-url="https://djyb0v5s4sgfh.cloudfront.net/vaiim1/VIC_ENGLISH_WORKBOOK/English Workbook Volume4 level7/7_SE_33_1.mp4" 
                pdf-url="https://vod.writingand.com/documents/pdf/2023/K3/2023_Yr3_Math_Mega_Test_vol_2_-_SC.pdf" duration="123" style="margin: 30px;">
                <strong>Set</strong> <span id="minus2Week"></span>
                &nbsp;&nbsp;<i class="bi bi-mortarboard-fill h5 text-primary"></i>
            </p>
            <div class="progress" style="margin: 30px;">
                <div class="progress-bar bg-success" role="progressbar" style="width: 85%;" aria-valuenow="85" aria-valuemin="0" aria-valuemax="100">
                    <span class="ml-auto">85%</span>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="col-md-6">
    <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(document.getElementById('minus1Week').textContent)">
        <div class="alert alert-info english-homework" role="alert">
            <p id="m1OnlineLesson" video-url="" pdf-url="" duration="" style="margin: 30px;">
                <strong>Set</strong> <span id="minus1Week">34</span>
                &nbsp;&nbsp;<i class="bi bi-mortarboard-fill h5 text-primary"></i>
            </p>
            <div class="progress" style="margin: 30px;">
                <div class="progress-bar bg-warning" role="progressbar" style="width: 62%;" aria-valuenow="62" aria-valuemin="0" aria-valuemax="100">
                    <span class="ml-auto">62%</span>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="col-md-6">
    <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayMaterial(document.getElementById('academicWeek').textContent)">
        <div class="alert alert-info english-homework" role="alert">
            <p id="onlineLesson" video-url="" pdf-url="" duration="" style="margin: 30px;">
                <strong>Set</strong> <span id="academicWeek">35</span>
                &nbsp;&nbsp;<i class="bi bi-mortarboard-fill h5 text-primary"></i>
            </p>
            <div class="progress" style="margin: 30px;">
                <div class="progress-bar bg-danger" role="progressbar" style="width: 8%;" aria-valuenow="8" aria-valuemin="0" aria-valuemax="100">
                    <span class="ml-auto">8%</span>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="col-md-6">
    <div class="card-body mx-auto" style="max-width: 75%;">
        <div class="alert alert-info english-homework" role="alert">
            <p style="margin: 30px;">
                <strong>Set</strong> <span id="plus1Week">36</span>
                &nbsp;&nbsp;<i class="bi bi-mortarboard-fill h5 text-secondary"></i>
            </p>
            <div class="progress" style="margin: 30px;">
                <div class="progress-bar bg-warning" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                    <span class="ml-auto"></span>
                </div>
            </div>
        </div>
    </div>
</div>
      
<!-- Pop up Video modal -->
<div class="modal fade" id="homeworkModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="exampleModalLabel">English Homework Details - Set <span id="dialogSet" name="dialogSet" class="text-warning"></span></h5>
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
                        <embed id="pdfViewer" src="" type="application/pdf" style="width: 100%; height: 600px;" />
                    </div>
                </div>
            </div>
            <div class="modal-footer bg-dark text-white">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

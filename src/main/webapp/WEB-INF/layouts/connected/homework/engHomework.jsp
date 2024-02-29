<style>
    .english-homework {
        background-color: #d1ecf1; 
        padding: 20px; 
        border-radius: 10px; 
        box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); 
    }
</style>
<script>
$(function() {
    // to get the academic year and week
    $.ajax({
        url : '${pageContext.request.contextPath}/class/academy',
        method: "GET",
        success: function(response) {
            // save the response into the variable
            // academicYear = response[0];
            // academicWeek = response[1];
            // update the value of the academicWeek span element
            document.getElementById("academicYear").value = parseInt(response[0]);
            document.getElementById("minus2Week").innerHTML = parseInt(response[1])-2;
            document.getElementById("minus1Week").innerHTML = parseInt(response[1])-1;
            document.getElementById("academicWeek").innerHTML = parseInt(response[1]);
            document.getElementById("plus1Week").innerHTML = parseInt(response[1])+1;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display variables
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayAll() {
    var academicYear = document.getElementById("academicYear").value;
    var academicWeek = document.getElementById("academicWeek").textContent;
    console.log(numericGrade + ' - ' + studentId + ' - ' + firstName + ' - ' + lastName + ' - ' + academicYear + ' - ' + academicWeek); 

    document.getElementById("videoPlayer").src = 'https://djyb0v5s4sgfh.cloudfront.net/vaiim1/VIC_ENGLISH_WORKBOOK/English Workbook Volume4 level7/7_SE_33_1.mp4';
    var pdfUrl = "path/to/your/pdf"; // replace with the actual URL of your PDF
    document.getElementById("pdfViewer").src = pdfUrl;

    $('#homeworkModal').modal('show');

}



</script>

<input type="hidden" id="academicYear" name="academicYear" />
<div class="col-md-12" style="padding: 30px;">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase;">English Homework</h2>
    </div>
</div>
<div class="col-md-6">
    <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayAll()">
        <div class="alert alert-info english-homework" role="alert">
            <p id="onlineLesson" data-video-url="" style="margin: 30px;">
                <strong>Set</strong> <span id="minus2Week"></span>
                &nbsp;&nbsp;<i name="micIcon" class="bi bi-mortarboard-fill h5 text-primary"></i>
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
    <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayAll()">
        <div class="alert alert-info english-homework" role="alert">
            <p id="onlineLesson" data-video-url="" style="margin: 30px;">
                <strong>Set</strong> <span id="minus1Week">34</span>
                &nbsp;&nbsp;<i name="micIcon" class="bi bi-mortarboard-fill h5 text-primary"></i>
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
    <div class="card-body mx-auto" style="cursor: pointer; max-width: 75%;" onclick="displayAll()">
        <div class="alert alert-info english-homework" role="alert">
            <p id="onlineLesson" data-video-url="" style="margin: 30px;">
                <strong>Set</strong> <span id="academicWeek">35</span>
                &nbsp;&nbsp;<i name="micIcon" class="bi bi-mortarboard-fill h5 text-primary"></i>
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
            <p id="onlineLesson" data-video-url="" style="margin: 30px;">
                <strong>Set</strong> <span id="plus1Week">36</span>
                &nbsp;&nbsp;<i name="micIcon" class="bi bi-mortarboard-fill h5 text-secondary"></i>
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
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Homework Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-6">
                        <video id="videoPlayer" controls style="width: 100%; height: auto;">
                            <source src="" type="video/mp4">
                        </video>
                    </div>
                    <div class="col-md-6">
                        <embed id="pdfViewer" src="" type="application/pdf" style="width: 100%; height: 600px;" />
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

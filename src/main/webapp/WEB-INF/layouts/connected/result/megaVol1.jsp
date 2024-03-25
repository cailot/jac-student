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

</style>
<script>

const TEST_TYPE = '1,2,3' ;
const VOLUME = 1; // volume should be 1....
$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryTestResult/' + studentId + '/' + TEST_TYPE + '/' + numericGrade + '/' + VOLUME,
        method: "GET",
        success: function(data) {
            $.each(data, function(index, basket) {
                //console.log(basket);
                var title = basket.testTypeName;
                var testId = basket.id;
                var icon = '<i class="bi bi-filetype-pdf h5 text-primary" title="Test Results Report"></i>';
                var cardBody = '<div class="card-body mx-auto" style="cursor: pointer; max-width: 75%; min-width: 235px;" onclick="displayReport(' + testId +  ', \'' +  title + '\');">';
                var columnClass = data.length === 2 ? 'mr-5' : ''; // padding in case of 2 cards
                var topicDiv = '<div class="col-md-4 ' + columnClass + '">'
                + cardBody
                + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin: 30px;">'
                + '<strong><span id="topicTitle">' + title + '</span></strong>&nbsp;&nbsp;' + icon
                + '</p></div></div></div>';
                $('#topicContainer').append(topicDiv);    
			});
           
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Display Report (Pdf Sheet)
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayReport(studentTestId, title) {
    // set dialogSet value as title
    document.getElementById("dialogSet").innerHTML = title;  
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/studentTestResult/' + studentTestId,
        method: "GET",
        success: function(pdf) {
            // console.log(pdf);
            document.getElementById("pdfViewer").data = pdf;
            // pop-up report
            $('#studentResultModal').modal('show');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });  
}


</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">Mega Volume 1 Results</h2>
    </div>
</div>

<div class="container mt-3" style="background: linear-gradient(to right, #f1f3ff 0%, #b1b9f9 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5 justify-content-center"></div>
</div>

<!-- Pop up Test Result modal -->
<div class="modal fade" id="studentResultModal" tabindex="-1" role="dialog" aria-labelledby="resultModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-extra-large" role="document">
        <div class="modal-content" style="height: 90vh;">
            <div class="modal-header bg-primary text-white text-center">
                <h5 class="modal-title w-100" id="resultModalLabel">Mega Volume 1 - <span id="dialogSet" name="dialogSet" class="text-warning"></span></h5>
                <button type="button" class="close position-absolute" style="right: 1rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>            
            <div class="modal-body bg-light">
                <div class="row">
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

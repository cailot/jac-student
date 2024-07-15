<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>

const SUBJECT = 3; // 3 is GA
var assessId = 0;
var studentId = 0;
var grade = 0;

$(function() {

    studentId = getQueryParam('id');
    grade = getQueryParam('grade');
    

    $.ajax({
        url : '${pageContext.request.contextPath}/assessment/getAssessInfo/' + grade + '/' + SUBJECT,
        method: "GET",
        success: function(data) {
            //console.log(data);
            assessId = data.id;
            display(data.pdfPath);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

// get query parameter value
function getQueryParam(param) {
    var urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">James An General Ability Assessment</h2>
    </div>
</div>

<!-- Include test.jsp -->
<jsp:include page="assess.jsp" />
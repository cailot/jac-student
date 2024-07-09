<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>

const TEST_TYPE = 1; // 1 is MEGA English
const DONE= 'DONE';

$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/assessment/getPaper/' + 1 + '/' + 2,
        method: "GET",
        success: function(data) {
            console.log(data);
            display(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});


</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">James An Math Assessment</h2>
    </div>
</div>

<!-- Include test.jsp -->
<jsp:include page="assess.jsp" />
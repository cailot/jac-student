<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>
<script>

const TEST_GROUP = 5; // 5 is Mock Test

function renderTopics(data) {
    if (Array.isArray(data)) {
        data.sort(function(a, b) {
            if (a.week !== b.week) return a.week - b.week;
            return String(a.title || '').localeCompare(String(b.title || ''));
        });
    }

    $.each(data, function(index, basket) {
        var title = basket.title;
        var id = basket.id;
        var icon = '<i class="bi bi-chat-dots h5 text-primary" data-toggle="tooltip" title="Explanation Video"></i>';
        var cardBody = '<div class="card-body mx-auto text-center" style="cursor: pointer; max-width: 75%; min-width: 235px;" onclick="displayMaterial(' + id + ', \'' + title + '\');">'
        var topicDiv = '<div class="col-md-6">'
        + cardBody
        + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin-top: 30px; margin-bottom: 30px;">'
        + '<strong><span id="topicTitle" class="badge badge-primary custom-badge">' + title + '</span></strong>&nbsp;&nbsp;' + icon
        + '</p></div></div></div>';
        $('#topicContainer').append(topicDiv);
    });
    $('[data-toggle="tooltip"]').tooltip();
}

$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryMockTestExplanation/' + studentId,
        method: "GET",
        success: function(data) {
            renderTopics(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">Mock Test Explanation</h2>
    </div>
</div>

<div class="container mt-3" style="background: linear-gradient(to right, #f1f3ff 0%, #b1b9f9 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5 justify-content-center"></div>
</div>

<!-- Include test.jsp -->
<jsp:include page="testExplanation.jsp" />

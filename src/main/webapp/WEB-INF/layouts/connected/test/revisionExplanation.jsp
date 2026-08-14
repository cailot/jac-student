<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>
<script>

const TEST_GROUP = 2; // 2 is Revision

$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryTest4Explanation/' + TEST_GROUP + '/' + studentId,
        method: "GET",
        success: function(data) {
            $.each(data, function(index, basket) {                
               //console.log(basket);
                var title = basket.title;
                var id = basket.id;
                var week = basket.week;
                var setName = week;
                switch (week) {
                    case 1:
                        setName = ' Vol.1';
                        break;
                    case 2:
                        setName = ' Vol.2';
                        break;
                    case 3:
                        setName = ' Vol.3';
                        break;
                    case 4:
                        setName = ' Vol.4';
                        break;
                    case 5:
                        setName = ' Vol.5';
                        break;
                }
                var icon = '<i class="bi bi-chat-dots h5 text-primary" data-toggle="tooltip" title="Not Submitted Yet"></i>';
                var cardBody = '<div class="card-body mx-auto text-center" style="cursor: pointer; max-width: 75%; min-width: 235px;" onclick="displayMaterial(' + id + ', \'' + title + '\');">'
                var columnClass = data.length === 2 ? 'mr-5' : ''; // padding in case of 2 cards
                var topicDiv = '<div class="col-md-4 ' + columnClass + '">'
                + cardBody
                + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin-top: 30px; margin-bottom: 30px;">'
                + '<strong><span id="topicTitle" class="badge badge-primary custom-badge">' + title + '</span></strong><br>Set <strong><i>' + setName +'</i></strong>&nbsp;&nbsp;' + icon
                + '</p></div></div></div>';
                $('#topicContainer').append(topicDiv);    
            });
             // Reinitialize tooltips after content is added
             $('[data-toggle="tooltip"]').tooltip();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error : ' + errorThrown);
        }
    });
});

</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">Revision Test Explanation</h2>
    </div>
</div>

<div class="container mt-3" style="background: linear-gradient(to right, #f1f3ff 0%, #b1b9f9 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5 justify-content-center"></div>
</div>

<!-- Include test.jsp -->
<jsp:include page="testExplanation.jsp" />
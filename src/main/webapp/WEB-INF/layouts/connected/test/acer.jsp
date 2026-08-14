<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>

const TEST_GROUP = 4; // 4 is ACER
const DONE= 'DONE';
$(function() {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryTest/' + TEST_GROUP + '/' + studentId,
        method: "GET",
        success: function(data) {
            $.each(data, function(index, basket) {                
            //    console.log(basket);
                var title = basket.title;
                var id = basket.id;
                var week = basket.week;
                var setName = week;
                var icon = '<i class="bi bi-send h5 text-primary" data-toggle="tooltip" title="Not Submitted Yet"></i>';
                var cardBody = '<div class="card-body mx-auto text-center" style="cursor: pointer; max-width: 75%; min-width: 235px;" onclick="showWarning(' + id + ', \'' + title + '\');">'
                if (title.endsWith('DONE')) {
                    // title ends with 'DONE'
                    title = title.slice(0, -4);
                    icon = '<i class="bi bi-send-fill h5 text-primary" data-toggle="tooltip" title="You have already take test"></i>';
                    cardBody = '<div class="card-body mx-auto text-center" style="cursor: pointer; max-width: 75%; min-width: 235px;" onclick="alreadyTaken(' + id + ', \'' + title + '\');">'
                }
                var columnClass = data.length === 2 ? 'mr-5' : ''; // padding in case of 2 cards
                var topicDiv = '<div class="col-md-5 ' + columnClass + '">'
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
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">Acer Test</h2>
    </div>
</div>

<div class="container mt-3" style="background: linear-gradient(to right, #f1f3ff 0%, #b1b9f9 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5 justify-content-center"></div>
</div>

<!-- Include test.jsp -->
<jsp:include page="test.jsp" />
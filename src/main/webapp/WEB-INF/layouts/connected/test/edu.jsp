<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>

// 3=EDU, 4=ACER; 6=TT8 Class/Mega-style schedules (TestType.testGroup in DB)
var CLASS_TEST_GROUPS_BASE = [3, 4];
var TT8_GRADE_CODE = '12';
var TT8_EXTRA_CLASS_TEST_GROUP = 6;
// test.jsp expects a scalar TEST_GROUP; only 1 or 2 trigger Mega/Revision volume labels. Class Test (3/4/6) must stay outside that branch.
var TEST_GROUP = 3;
const DONE= 'DONE';

function classTestGroupParamForEnrolGrade(enrolGrade) {
    var g = (enrolGrade == null) ? '' : String(enrolGrade).trim();
    var groups = CLASS_TEST_GROUPS_BASE.slice();
    if (g === TT8_GRADE_CODE) {
        groups.push(TT8_EXTRA_CLASS_TEST_GROUP);
    }
    return groups.join(',');
}

function fetchAndRenderClassTests(testGroupParam) {
    $.ajax({
        url : '${pageContext.request.contextPath}/connected/summaryTest/' + testGroupParam + '/' + studentId,
        method: "GET",
        success: function(data) {
            $.each(data, function(index, basket) {                
            //    console.log(basket);
                var title = basket.title;
                var id = basket.id;
                var week = basket.week;
                var setName = week;
                switch (week) {
                    case 36:
                        setName = 'SIM 1';
                        break;
                    case 37:
                        setName = 'SIM 2';
                        break;
                    case 38:
                        setName = 'SIM 3';
                        break;
                    case 39:
                        setName = 'SIM 4';
                        break;
                    case 40:
                        setName = 'SIM 5';
                        break;
                }
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
}

$(function() {
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/enrolGrade/' + studentId,
        type: 'GET',
        success: function(response) {
            fetchAndRenderClassTests(classTestGroupParamForEnrolGrade(response));
        },
        error: function() {
            fetchAndRenderClassTests(classTestGroupParamForEnrolGrade(null));
        }
    });
});

</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">Class Test</h2>
    </div>
</div>

<div class="container mt-3" style="background: linear-gradient(to right, #f1f3ff 0%, #b1b9f9 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5 justify-content-center"></div>
</div>

<!-- Include test.jsp -->
<jsp:include page="test.jsp" />
<script src="${pageContext.request.contextPath}/js/pdf-2.16.105.min.js"></script>
<script>

// Match Class Test (edu.jsp): 3=EDU, 4=ACER; TT8 SIM uses testGroup 6
var EXPLANATION_TEST_GROUPS_BASE = [3, 4];
var TT8_GRADE_CODE = '12';
var TT8_EXTRA_CLASS_TEST_GROUP = 6;

function explanationGroupIdsForEnrolGrade(enrolGrade) {
    var g = (enrolGrade == null) ? '' : String(enrolGrade).trim();
    var groups = EXPLANATION_TEST_GROUPS_BASE.slice();
    if (g === TT8_GRADE_CODE) {
        groups.push(TT8_EXTRA_CLASS_TEST_GROUP);
    }
    return groups;
}

function mergeExplanationSummaries(groupIds, onDone) {
    var merged = [];
    var i = 0;
    function next() {
        if (i >= groupIds.length) {
            var seen = {};
            var unique = [];
            merged.forEach(function(item) {
                var k = item.id + '_' + item.week;
                if (!seen[k]) {
                    seen[k] = true;
                    unique.push(item);
                }
            });
            onDone(unique);
            return;
        }
        var grp = groupIds[i++];
        $.ajax({
            url: '${pageContext.request.contextPath}/connected/summaryTest4Explanation/' + grp + '/' + studentId,
            method: 'GET',
            success: function(data) {
                if (Array.isArray(data)) {
                    merged = merged.concat(data);
                }
                next();
            },
            error: function() {
                next();
            }
        });
    }
    next();
}

function renderTopics(data) {
    // Sort data by week and title
    if (Array.isArray(data)) {
        data.sort(function(a, b) {
            if (a.week !== b.week) return a.week - b.week;
            return String(a.title || '').localeCompare(String(b.title || ''));
        });
    }
    // Four cards: 2 per row (2×2). Otherwise keep three per row on md+.
    var colClass = (Array.isArray(data) && data.length === 4) ? 'col-md-6' : 'col-md-4';

    $.each(data, function(index, basket) {
        var title = basket.title;
        var id = basket.id;
        var week = basket.week;
        var setName = week;
        switch (week) {
            case 36:
                setName = ' SIM 1';
                break;
            case 37:
                setName = ' SIM 2';
                break;
            case 38:
                setName = ' SIM 3';
                break;
            case 39:
                setName = ' SIM 4';
                break;
            case 40:
                setName = ' SIM 5';
                break;
        }
        var icon = '<i class="bi bi-chat-dots h5 text-primary" data-toggle="tooltip" title="Not Submitted Yet"></i>';
        var cardBody = '<div class="card-body mx-auto text-center" style="cursor: pointer; max-width: 75%; min-width: 235px;" onclick="displayMaterial(' + id + ', \'' + title + '\');">'
        var topicDiv = '<div class="' + colClass + '">'
        + cardBody
        + '<div class="alert alert-info topic-card" role="alert"><p id="onlineLesson" style="margin-top: 30px; margin-bottom: 30px;">'
        + '<strong><span id="topicTitle" class="badge badge-primary custom-badge">' + title + '</span></strong><br>Set <strong><i>' + setName +'</i></strong>&nbsp;&nbsp;' + icon
        + '</p></div></div></div>';
        $('#topicContainer').append(topicDiv);
    });
    // Reinitialize tooltips after content is added
    $('[data-toggle="tooltip"]').tooltip();
}

$(function() {
    $.ajax({
        url: '${pageContext.request.contextPath}/connected/enrolGrade/' + studentId,
        type: 'GET',
        success: function(response) {
            mergeExplanationSummaries(explanationGroupIdsForEnrolGrade(response), renderTopics);
        },
        error: function() {
            mergeExplanationSummaries(explanationGroupIdsForEnrolGrade(null), renderTopics);
        }
    });
});

</script>

<div class="col-md-12 pt-3">
    <div class="card-body text-center">
        <h2 style="color: #6c757d; font-weight: bold; text-transform: uppercase; text-shadow: 2px 2px 4px rgba(168, 179, 247, 1);">Simulation Test Explanation</h2>
    </div>
</div>

<div class="container mt-3" style="background: linear-gradient(to right, #f1f3ff 0%, #b1b9f9 100%); border-radius: 15px;">
    <div id="topicContainer" class="row mt-5 mb-5 justify-content-center"></div>
</div>

<!-- Include test.jsp -->
<jsp:include page="testExplanation.jsp" />

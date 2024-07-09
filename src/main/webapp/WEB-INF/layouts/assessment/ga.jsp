<script>
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


ga
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
<title><tiles:getAsString name="title" /></title>
<link href="${pageContext.request.contextPath}/css/jae.css" rel="stylesheet" type="text/css"/>
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-4.3.1.min.css"/>	
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui-1.12.1.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.theme.min.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jae.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui-1.13.2.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-4.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle-4.5.3.min.js"></script>
	
<!-- Bootstrap Icons -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.min.css"/>

</head>
<body>
	<div class="container-fluid d-flex h-100 flex-column">
		<div class="row justify-content-center align-items-center">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
</body>
</html>
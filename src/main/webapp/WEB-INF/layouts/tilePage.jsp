<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en-US">
<head>
<title><tiles:getAsString name="title" /></title>
<!-- Favicon -->
<link rel="icon" href="${pageContext.request.contextPath}/image/favicon.ico" type="image/x-icon">

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, viewport-fit=cover">

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
<!-- Google Icons -->
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script>
	$(function() {
		// Enable Bootstrap tooltips
		$('[data-toggle="tooltip"]').tooltip();
	});
</script>
<style>
	/* Page background: static/assets/image/background.jpg served as /image/background.jpg (see JaeWebConfig) */
	html {
		min-height: 100%;
	}
	body {
		min-height: 100vh;
		margin: 0;
		color: #2b2f3a;
		background-color: #e8ebf4;
		background-image: url('${pageContext.request.contextPath}/image/background.jpg');
		background-size: cover;
		background-position: center top;
		background-repeat: no-repeat;
		background-attachment: fixed;
		overflow-x: hidden;
	}
	body > .jae-tile-shell {
		min-height: 100vh;
		background: transparent;
	}
	body > .jae-tile-shell > footer {
		background: rgba(248, 249, 252, 0.9);
		border-top: 1px solid rgba(45, 57, 142, 0.12);
	}
	/* Connected /connected/lesson welcome only (see JaeController) */
	body.jae-connected-landing {
		background-image: none;
		background-color: #f3f5fc;
		background-attachment: scroll;
	}
</style>
</head>
<body<c:if test="${connectedClassLanding}"> class="jae-connected-landing"</c:if>>
	<div class="jae-tile-shell d-flex flex-column w-100 mx-0 px-0" style="min-height: 100vh;">
		<tiles:insertAttribute name="menu" />
		<div class="row justify-content-center mx-0 w-100 px-3 px-lg-4">
			<tiles:insertAttribute name="body" />
		</div>
		<footer class="mt-auto w-100">
			<div class="row" style="padding: 15px 20px;">
				<div class="col-12 text-center" >
					<div style="color: #ff0000; font-size: 0.9em; margin-bottom: 10px; font-style: italic;">
						<strong>LEGAL NOTICE:</strong> All content, materials, and intellectual property on this platform are owned by James An College Victoria. 
						Unauthorized access, reproduction, or distribution is strictly prohibited and may result in legal action.
					</div>
					2015 - <%=new java.util.Date().getYear() + 1900%> &copy; All rights reserved.
					<div class="copyright-font-color">James An College Victoria <span title="Application version">v${appVersion}</span></div> 
				</div>
			</div>
		</footer>
	</div>
	<!-- Loading Spinner -->
	<div class="modal fade" id="loading-spinner" data-backdrop="static" data-keyboard="false" tabindex="-1">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content loading-spinner-content">
				<div class="modal-body text-center p-5">
					<div class="spinner-border text-primary" role="status" style="width: 4rem; height: 4rem;">
						<span class="sr-only">Loading...</span>
					</div>
					<div id="loading-message" class="mt-4 text-primary h4"></div>
				</div>
			</div>
		</div>
	</div>

	<!-- Global Right-Click Protection -->
	<script>
	$(document).ready(function() {
		// Disable right-click context menu globally
		$(document).on('contextmenu', function(e) {
			e.preventDefault();
			return false;
		});
		
		// Disable common developer tool keyboard shortcuts
		$(document).on('keydown', function(e) {
			// Disable F12 (Developer Tools)
			if (e.keyCode === 123) {
				e.preventDefault();
				return false;
			}
			// Disable Ctrl+Shift+I (Developer Tools)
			if (e.ctrlKey && e.shiftKey && e.keyCode === 73) {
				e.preventDefault();
				return false;
			}
			// Disable Ctrl+Shift+C (Inspect Element)
			if (e.ctrlKey && e.shiftKey && e.keyCode === 67) {
				e.preventDefault();
				return false;
			}
			// Disable Ctrl+U (View Source)
			if (e.ctrlKey && e.keyCode === 85) {
				e.preventDefault();
				return false;
			}
			// Disable Ctrl+Shift+J (Console)
			if (e.ctrlKey && e.shiftKey && e.keyCode === 74) {
				e.preventDefault();
				return false;
			}
		});
		
		// Disable text selection and dragging (optional)
		$(document).on('selectstart dragstart', function(e) {
			e.preventDefault();
			return false;
		});
	});
	</script>
</body>
</html>
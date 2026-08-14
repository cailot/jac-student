<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
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
<style>
    body {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
        background-color: #f8f9fa;
        margin: 0;
    }
    .header, .footer {
        background-color: #2d398e;
        color: white;
        padding: 1rem;
        text-align: center;
        position: fixed;
        width: 100%;
        z-index: 1000;
    }
    .header {
        top: 0;
    }
    .footer {
        bottom: 0;
    }
    .content {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 1rem;
        margin-top: 60px; /* Height of the header */
        margin-bottom: 60px; /* Height of the footer */
    }
    @media (max-width: 768px) {
        .header, .footer {
            position: static !important;
        }
        .content {
            margin-top: 0;
            margin-bottom: 0;
            padding-bottom: 1rem;
        }
        .footer {
            margin-top: 1rem;
        }
    }
</style>
</head>
<body>
    <div class="header">
        <img src="${pageContext.request.contextPath}/image/logo.png" style="filter: brightness(0) invert(1);width:45px;" class="mr-3"/><span class="h4">James An College Victoria</span>
    </div>

    <div class="content">
        <tiles:insertAttribute name="body" />
    </div>

    <div class="footer">
        James An College Victoria Head Office: 16A 77-79 Ashley St. Braybrook VIC 3019 Australia<br>
        <i class="bi bi-telephone"></i> (03) 9362 0051 &nbsp;&nbsp;&nbsp;<i class="bi bi-envelope"></i> assessment@jamesancollegevic.com.au<br>
        <div style="color: #ff0000; font-size: 0.9em; margin: 5px 0;">
            <strong>LEGAL NOTICE:</strong> All content, materials, and intellectual property on this platform are owned by James An College Victoria. 
            Unauthorized access, reproduction, or distribution is strictly prohibited and may result in legal action.
        </div>
        2015 - <%= new java.util.Date().getYear() + 1900 %>&copy;&nbsp; All rights reserved.&nbsp;&nbsp;
		<div class="copyright-font-color">James An College Victoria <span title="Application version">v${appVersion}</span></div> 
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
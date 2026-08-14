<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
</head>
<body>
	<div class="container-fluid d-flex h-100 flex-column">
		<div class="row justify-content-center align-items-center" >
			<tiles:insertAttribute name="body" />
		</div>
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
		
		// Session timeout monitoring
		startSessionMonitor();
	});
	
	// Session timeout monitoring function
	function startSessionMonitor() {
		var sessionTimeout = 210 * 60; // 210 minutes in seconds
		var sessionStartTime = new Date().getTime();
		var warningThreshold = 10 * 60; // 10 minutes before timeout
		var warningShown = false;
		var serverSessionValid = true;
		
		console.log('Session monitoring started. Timeout: 210 minutes');
		
		// Check server session status every 5 minutes
		var serverCheckInterval = setInterval(function() {
			$.ajax({
				url: '${pageContext.request.contextPath}/connected/sessionStatus',
				method: 'GET',
				success: function(response) {
					if (response.valid) {
						var serverRemainingTime = Math.floor(response.remainingTime);
						var hours = Math.floor(serverRemainingTime / 3600);
						var minutes = Math.floor((serverRemainingTime % 3600) / 60);
						var seconds = serverRemainingTime % 60;
						
						console.log('Server session remaining: ' + 
							(hours > 0 ? hours + 'h ' : '') + 
							minutes + 'm ' + 
							seconds + 's');
						
						// Show warning when server session is about to expire
						if (serverRemainingTime <= warningThreshold && !warningShown) {
							console.warn('SERVER SESSION WARNING: Only 10 minutes remaining!');
							warningShown = true;
						}
						
						serverSessionValid = true;
					} else {
						console.error('SERVER SESSION EXPIRED!');
						serverSessionValid = false;
						clearInterval(serverCheckInterval);
					}
				},
				error: function() {
					console.warn('Could not check server session status');
				}
			});
		}, 300000); // Check every 5 minutes
		
		// Check client-side session status every second
		var sessionInterval = setInterval(function() {
			var currentTime = new Date().getTime();
			var elapsedTime = Math.floor((currentTime - sessionStartTime) / 1000);
			var remainingTime = sessionTimeout - elapsedTime;
			
			if (remainingTime > 0 && serverSessionValid) {
				var hours = Math.floor(remainingTime / 3600);
				var minutes = Math.floor((remainingTime % 3600) / 60);
				var seconds = remainingTime % 60;
				
				// Show warning when 10 minutes remaining
				if (remainingTime <= warningThreshold && !warningShown) {
					console.warn('CLIENT SESSION WARNING: Only 10 minutes remaining!');
					warningShown = true;
				}
				
				// Log every 5 minutes or when less than 10 minutes remaining
				if (remainingTime % 300 === 0 || remainingTime <= 600) {
					console.log('Client session remaining: ' + 
						(hours > 0 ? hours + 'h ' : '') + 
						minutes + 'm ' + 
						seconds + 's');
				}
			} else if (!serverSessionValid) {
				console.error('CLIENT SESSION EXPIRED!');
				clearInterval(sessionInterval);
			}
		}, 1000); // Check every second for more accurate monitoring
		
		// Reset session timer on user activity
		$(document).on('click keypress scroll', function() {
			sessionStartTime = new Date().getTime();
			warningShown = false;
			console.log('Session activity detected - timer reset');
		});
	}
	</script>
</body>
</html>

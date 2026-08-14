<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>

$(document).ready(function() {
    // Show the welcome popup every time - DISABLED
    // $('#welcomeModal').modal('show');
	// $('#welcomeModal').modal('hide');
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Show Assessment Modal
////////////////////////////////////////////////////////////////////////////////////////////////////////
function displayAssessment() {
	$('#assessmentWarning').modal('show');
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
// 			Move to Assessment Start Page
////////////////////////////////////////////////////////////////////////////////////////////////////////
function navigateToAssessment() {
    window.location.href = '${pageContext.request.contextPath}/assessment/apply'; // Navigate to the new page
}
</script>

<style>

#background {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  overflow: hidden;
  position: relative;
}

.background-animation {
  width: 100%;
  height: 100vh;
  background-image: url('${pageContext.request.contextPath}/image/e-learning.png');
  background-size: cover;
}

.left-container {
	position: absolute;
	left: 0;
	width: 35%;
	height: 100vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.card-container {
	width: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
}

.spaced-list li {
    margin-bottom: 10px;
}

@media (max-width: 768px) {
  #background {
    height: auto;
    min-height: 100vh;
  }
  .background-animation {
    height: auto;
    min-height: 100vh;
    background-position: center;
  }
  .left-container {
    position: static;
    width: 100%;
    height: auto;
    padding: 24px 16px 48px;
  }
}
</style>
<div id="background" class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
	<div class="background-animation">
		<div class="left-container">
			<h3 class="text-white text-center" ><img src="${pageContext.request.contextPath}/image/logo.png"></img></h3>
			<h6 class="text-secondary text-center mb-4" style="background: #ffffff !important;">Sign In To James An College</h6>
			<div class="card-container">			
				<div class="row h-100 justify-content-center align-items-center">						
					<div class="card" style="border: 3px solid #2d398e; border-radius: 10px;">
						<h3 class="card-header text-white text-center" style="background: #2d398e !important;">Jac eLearning</h3>
						<div class="card-body">
							<form:form  action="${pageContext.request.contextPath}/online/processLogin" method="POST">
								<div class="row mb-1">
									<div class="col-md-12">
										<div class="form-group">
											<!-- Check for login error -->
											<c:if test="${param.error != null}">
												<div class="alert alert-danger col-xs-offset-1 col-xs-10">
													<c:choose>
														<c:when test="${param.error == 'payment'}">
															Payment not made for enrolment.
														</c:when>
														<c:when test="${param.error == 'enrolment'}">
															Enrolment is not valid for the current period.
														</c:when>
														<c:when test="${param.error == 'disabled'}">
															Account is disabled. Please contact the office.
														</c:when>
														<c:otherwise>
															Invalid username and password.
														</c:otherwise>
													</c:choose>
												</div>
											</c:if>
											<!-- Check for logout -->
											<c:if test="${param.logout != null}">
												<div class="alert alert-success col-xs-offset-1 col-xs-10">
												You have been logged out.
												</div>
											</c:if>
											<label>Username</label>
											<div class="input-group">
												<div class="input-group-prepend">
												<span class="input-group-text text-white" style="background: #2d398e !important;"><i class="bi bi-person-fill text-white" aria-hidden="true"></i></span>
												</div>
												<input type="text" class="form-control" name="username" placeholder="Enter your student ID" />
											</div>
											<div class="help-block with-errors text-danger">
											</div>
										</div>
									</div>
								</div>
								<div class="row mb-3">
									<div class="col-md-12">
										<div class="form-group">
											<label>Password</label>
											<div class="input-group">
												<div class="input-group-prepend">
												<span class="input-group-text text-white" style="background: #2d398e !important;"><i class="bi bi-unlock-fill text-white" aria-hidden="true"></i></span>
												</div>
												<input type="password" name="password" class="form-control" placeholder="Enter your password"/>
											</div>
											<div class="help-block with-errors text-danger"></div>
										</div>
									</div>
								</div>
								<div class="row mb-3">
									<div class="col-md-12">
										<input type="hidden" name="redirect" value="">
										<input type="submit" class="btn btn-lg btn-block text-white" style="background: #2d398e !important;" value="Login" name="submit">
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="text-primary text-right small">
											<a href="#" class="forgot-password-link">
												Forgot your password?
											</div>
										</div>
									</div>
								</div>
								<!-- <div class="row">
									<div class="col-md-12">
										<div class="text-primary text-right small">
											<a href="${pageContext.request.contextPath}/assessment/start">
												Assessment Test
											</a>	
										</div>
									</div>
								</div> -->
							</form:form>
						</div>
					</div>		
				</div>
			</div> <!-- end of card-container -->
			<h6 class="text-center" style="position: fixed; bottom: 0; width: 100%;">
				2015 - <%=new java.util.Date().getYear() + 1900%>&copy;&nbsp; All rights reserved.&nbsp;&nbsp;
				James An College
			</h6>		
		</div><!-- end of left-container-->
	</div>
</div>

<!-- Include Password Reset CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/password-reset.css">

<!-- Include Password Reset Modal -->
<jsp:include page="../password-reset-modal.jsp" />

<!-- Include Password Reset JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/password-reset.js"></script>

<!-- Assessment Warning Modal -->
<div class="modal fade" id="assessmentWarning" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" style="border: 2px solid #ffc107; border-radius: 10px;">
            <div class="modal-header bg-warning" style="display: block;">
				<p style="text-align: center; margin-bottom: 0;"><span style="font-size:18px"><strong>James An Online Assessment Test</strong></span></p>
			</div>
            <div class="modal-body">
                <div style="text-align: center; margin-bottom: 20px;">
                    <img src="${pageContext.request.contextPath}/image/assessment.png" style="width: 150px; height: 150px; border-radius: 5%;">
                </div>
                <!-- Add your warning message or content here -->
                <ul class="spaced-list">
                    <li>Thank you for taking the James An College Assessment Test.</li>
                    <li>
                        This assessment test is to help us understand how we can help you in your learning journey.
                    </li>
                    <li>
						Please be ready to enter your correct details so we can get back to you as soon as possible.</li>
                        
                    </li>
                    <li>
                        We look forward to having you join us!
                    </li>
                </ul>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-primary" id="agreeMediaWarning" onclick="navigateToAssessment()">Ready To Procceed</button>
            	<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<!-- Add the welcome modal -->
<div class="modal fade" id="welcomeModal" tabindex="-1" role="dialog" aria-labelledby="welcomeModalLabel" aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 50%;">
        <div class="modal-content">
            <div class="modal-header" style="background: #2d398e !important; color: white;">
                <h5 class="modal-title text-center" id="welcomeModalLabel" style="width: 100%;">Welcome to new JAC eLearning</h5>
                <button type="button" class="close text-white" data-bs-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <!-- Password Reset Guide -->
                <div class="alert alert-warning text-left">
                    <h6 class="alert-heading"><i class="bi bi-shield-lock"></i> Password Reset Guide</h6>
                    <p class="mb-2"><strong>Please reset your password once you login for the first time.</strong></p>
                    
                    <!-- Reset Guide Image -->
                    <div class="text-center mb-3">
                        <img src="${pageContext.request.contextPath}/image/reset-guide.png" alt="Password Reset Guide" class="img-fluid" style="max-width: 50%; border: 1px solid #ddd; border-radius: 5px;">
                    </div>
                    
                    <ol class="mb-0">
                        <li>Click your name on the top banner</li>
                        <li>This will pop up the Password Reset dialogue</li>
                        <li>Input your new password in the "New Password" field</li>
                        <li>Confirm your password in the "Confirm Password" field</li>
                        <li>Click the "Update Password" button</li>
                    </ol>
                </div>
                
                <div class="alert alert-info text-center">
                    <small>If you encounter any difficulties in resetting your password, please contact your branch.</small>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn text-white close-welcome-btn" style="background: #2d398e !important;">Get Started</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Backdrop -->
<div class="modal-backdrop fade show" id="welcomeModalBackdrop" style="display: none; z-index: 1040;"></div>

<!-- Welcome Modal Auto-show Script -->
<script>
$(document).ready(function() {
    console.log('Welcome modal script loaded');
    console.log('Modal element exists:', $('#welcomeModal').length > 0);
    console.log('localStorage welcomeModalShown:', localStorage.getItem('welcomeModalShown'));
    
    // localStorage 초기화 (테스트용) - DISABLED
    // localStorage.removeItem('welcomeModalShown');
    // console.log('localStorage cleared for testing');
    
    // Welcome Modal만 자동으로 표시 - DISABLED
    // if (localStorage.getItem('welcomeModalShown') !== 'true') {
    //     console.log('Welcome modal should be shown automatically');
    //     setTimeout(function() {
    //         console.log('Showing Welcome modal...');
    //         
    //         // 방법 1: Bootstrap modal 메서드
    //         try {
    //             $('#welcomeModal').modal('show');
    //             console.log('Bootstrap modal show called');
    //         } catch (e) {
    //             console.log('Bootstrap method failed:', e);
    //         }
    //         
    //         // 방법 2: CSS 직접 변경 (백업)
    //         setTimeout(function() {
    //             console.log('Applying CSS fallback method...');
    //             
    //             var modal = $('#welcomeModal');
    //             
    //             // 1. 인라인 스타일 완전 제거
    //             modal.removeAttr('style');
    //             console.log('Inline style removed in auto-show');
    //             
    //             // 2. CSS 강제 적용
    //             modal.css({
    //                 'display': 'block !important',
    //                 'opacity': '1 !important',
    //                 'visibility': 'visible !important',
    //                 'z-index': '9999 !important',
    //                 'position': 'fixed !important',
    //                 'top': '0 !important',
    //                 'left': '0 !important',
    //                 'width': '100% !important',
    //                 'height': '100% !important'
    //             });
    //             
    //             // 3. Bootstrap 클래스 제거 및 추가
    //             modal.removeClass('fade').addClass('show');
    //             $('body').addClass('modal-open');
    //             
    //             // 4. DOM 요소 직접 조작
    //             var modalElement = modal[0];
    //             modalElement.style.setProperty('display', 'block', 'important');
    //             modalElement.style.setProperty('opacity', '1', 'important');
    //             modalElement.style.setProperty('visibility', 'visible', 'important');
    //             
    //             console.log('CSS fallback applied');
    //             console.log('Modal display:', modal.css('display'));
    //             console.log('Modal opacity:', modal.css('opacity'));
    //             console.log('Modal z-index:', modal.css('z-index'));
    //             console.log('Modal inline style:', modal.attr('style'));
    //             
    //         }, 500);
    //         
    //     }, 1000);
    // } else {
    //     console.log('Welcome modal already shown before');
    // }
    
    // Welcome Modal 닫기 공통 함수
    function closeWelcomeModal() {
        localStorage.setItem('welcomeModalShown', 'true');
        console.log('Welcome modal closing');
        
        // Welcome modal 완전히 숨기기
        $('#welcomeModal').modal('hide');
        $('#welcomeModal').hide();
        $('#welcomeModal').removeClass('show');
        $('#welcomeModal').off();
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
        $('#welcomeModalBackdrop').remove();
        
        // Welcome modal 스타일 초기화
        $('#welcomeModal').css({
            'display': 'none',
            'opacity': '0',
            'visibility': 'hidden'
        });

        // DOM에서 완전히 제거하여 헤더/풋터 잔상 방지
        setTimeout(function(){
            $('#welcomeModal').remove();
        }, 0);
        
        // Password Reset modal 상태 초기화
        $('#passwordResetModal').modal('hide');
        $('#passwordResetModal').hide();
        $('#passwordResetModal').removeClass('show');
        
        console.log('Welcome modal closed and Password Reset modal state reset');
    }
    
    // Welcome Modal 닫기 시 localStorage 업데이트
    $('#welcomeModal').on('hidden.bs.modal', function(e) {
        closeWelcomeModal();
    });
    
    // X 버튼 클릭 이벤트
    $('#welcomeModal .close').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        closeWelcomeModal();
        console.log('Welcome modal closed via X button');
    });
    
    // Get Started 버튼 클릭 이벤트
    $('.close-welcome-btn').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        closeWelcomeModal();
        console.log('Welcome modal closed via Get Started button');
    });
    
    // ESC 키 이벤트
    $(document).on('keydown', function(e) {
        if (e.keyCode === 27 && $('#welcomeModal').hasClass('show')) {
            closeWelcomeModal();
        }
    });
    
    // ESC 키로 modal 완전히 닫기
    $(document).on('keydown', function(e) {
        if (e.keyCode === 27) { // ESC key
            closeWelcomeModal();
        }
    });
    

});
</script>
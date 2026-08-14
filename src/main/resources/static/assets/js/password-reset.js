// Password Reset Request JavaScript
$(document).ready(function() {
    // 페이지 로드 시 모달이 자동으로 열리지 않도록 강제로 숨김
    $('#passwordResetModal').modal('hide');
    
    // 자동 표시 시도 차단
    $('#passwordResetModal').off('show.bs.modal');
    
    // Forgot Password 링크 클릭 시에만 모달 표시
    $('.forgot-password-link').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        
        // 모달이 이미 열려있다면 닫기
        if ($('#passwordResetModal').hasClass('show')) {
            $('#passwordResetModal').modal('hide');
            return;
        }
        
        // 모달 열기
        $('#passwordResetModal').modal({
            show: true,
            backdrop: 'static',
            keyboard: false
        });
        
        // 폼 초기화
        if ($('#passwordResetForm').length > 0) {
            $('#passwordResetForm')[0].reset();
            $('.alert').remove();
            $('#sendRequestBtn').prop('disabled', true).removeClass('btn-primary').addClass('btn-secondary');
        }
    });
    
    // 모달 내부 클릭 시 이벤트 전파 방지
    
    $('#passwordResetModal').on('click', function(e) {
        e.stopPropagation();
    });
    
    // 모달 내부 요소 클릭 시 이벤트 전파 방지
    $('#passwordResetModal .modal-content').on('click', function(e) {
        e.stopPropagation();
    });
    
    // 입력 필드 검증 및 버튼 활성화/비활성화
    function validateForm() {
        // 요소가 존재하는지 확인
        var fullNameEl = $('#fullName');
        var studentIdEl = $('#studentId');
        var emailEl = $('#email');
        var sendRequestBtn = $('#sendRequestBtn');
        
        // 요소가 존재하지 않으면 함수 종료
        if (fullNameEl.length === 0 || studentIdEl.length === 0 || emailEl.length === 0 || sendRequestBtn.length === 0) {
            return;
        }
        
        var fullName = fullNameEl.val().trim();
        var studentId = studentIdEl.val().trim();
        var email = emailEl.val().trim();
        
        var isValid = fullName.length > 0 && studentId.length > 0 && email.length > 0;
        
        // Send Request 버튼 활성화/비활성화
        sendRequestBtn.prop('disabled', !isValid);
        
        // 버튼 스타일 변경
        if (isValid) {
            sendRequestBtn.removeClass('btn-secondary').addClass('btn-primary');
        } else {
            sendRequestBtn.removeClass('btn-primary').addClass('btn-secondary');
        }
        
        console.log('Form validation:', isValid, 'Name:', fullName, 'ID:', studentId, 'Email:', email);
    }
    
    // 입력 필드 변경 시마다 검증 실행 (요소가 존재할 때만)
    $(document).on('input', '#fullName, #studentId, #email', function() {
        if ($('#fullName').length > 0) {
            validateForm();
        }
    });
    
    // 페이지 로드 시 초기 검증 (요소가 존재할 때만)
    if ($('#fullName').length > 0) {
        validateForm();
    }
    
    // 비밀번호 재설정 요청 제출
    $('#passwordResetForm').on('submit', function(e) {
        e.preventDefault();
        
        // 폼 데이터 수집
        var formData = {
            email: $('#resetEmail').val(),
            studentId: $('#resetStudentId').val(),
            fullName: $('#resetFullName').val()
        };
        
        // 유효성 검사
        if (!formData.email || !formData.studentId || !formData.fullName) {
            showAlert('Please fill in all required fields.', 'danger');
            return;
        }
        

        
        // 이메일 형식 검사
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(formData.email)) {
            showAlert('Please enter a valid email address.', 'danger');
            return;
        }
        
        // 로딩 상태 표시
        $('#resetSubmitBtn').prop('disabled', true).html('<i class="fa fa-spinner fa-spin"></i> Sending...');
        
        // AJAX 요청
        $.ajax({
            url: '/password-reset/request',
            type: 'POST',
            data: formData,
            success: function(response) {
                if (response.success) {
                    // 모달 바로 닫기
                    $('#passwordResetModal').modal('hide');
                    
                    // 모달이 완전히 닫힌 후 페이지 상단에 성공 메시지 표시
                    $('#passwordResetModal').on('hidden.bs.modal', function() {
                        showPageSuccessMessage('Your password has been reset successfully! Please check your email for the new password.');
                        
                        // 이벤트 리스너 제거 (한 번만 실행되도록)
                        $('#passwordResetModal').off('hidden.bs.modal');
                    });
                    
                    // 폼 초기화
                    $('#passwordResetForm')[0].reset();
                    
                    // 버튼 상태 초기화
                    $('#sendRequestBtn').prop('disabled', true).removeClass('btn-primary').addClass('btn-secondary');
                    
                } else {
                    showAlert(response.message, 'danger');
                }
            },
            error: function(xhr, status, error) {
                showAlert('An error occurred while sending the request. Please try again.', 'danger');
                console.error('Error:', error);
            },
            complete: function() {
                $('#resetSubmitBtn').prop('disabled', false).html('Send Request');
            }
        });
    });
    
    // 모달이 닫힐 때 폼 초기화
    $('#passwordResetModal').on('hidden.bs.modal', function() {
        $('#passwordResetForm')[0].reset();
        $('.alert').remove();
        
        // 모달이 완전히 숨겨졌는지 확인
        if ($('#passwordResetModal').hasClass('show')) {
            $('#passwordResetModal').removeClass('show').modal('hide');
        }
        
        // 버튼 상태 초기화
        $('#sendRequestBtn').prop('disabled', true).removeClass('btn-primary').addClass('btn-secondary');
    });
    
    // 모달 닫기 버튼 클릭 시
    $('#passwordResetModal .close, #passwordResetModal .btn-secondary').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        $('#passwordResetModal').modal('hide');
    });
    
    // 페이지 로드 시 모달 상태 초기화
    $(window).on('load', function() {
        forceCloseModal();
    });
    
    // DOM이 완전히 로드된 후 모달 상태 확인 및 초기화
    $(document).ready(function() {
        forceCloseModal();
        
        // 추가 안전장치: 100ms 후 한 번 더 확인
        setTimeout(function() {
            forceCloseModal();
        }, 100);
    });
    
    // 모달 강제 닫기 함수
    function forceCloseModal() {
        var modal = $('#passwordResetModal');
        
        // 모달이 열려있다면 강제로 닫기
        if (modal.hasClass('show') || modal.is(':visible')) {
            modal.modal('hide');
            modal.removeClass('show');
        }
        
        // body에서 modal-open 클래스 제거
        $('body').removeClass('modal-open');
        
        // 모달 백드롭 제거
        $('.modal-backdrop').remove();
        
        // 모달 스타일 강제 적용
        modal.css({
            'display': 'none !important',
            'visibility': 'hidden !important',
            'opacity': '0 !important'
        });
    }
    
    // 모달 내부 알림 메시지 표시 함수
    function showAlert(message, type) {
        var alertHtml = '<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                       message +
                       '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                       '<span aria-hidden="true">&times;</span>' +
                       '</button>' +
                       '</div>';
        
        $('#passwordResetModal .modal-body').prepend(alertHtml);
        
        // 5초 후 자동으로 알림 제거
        setTimeout(function() {
            $('.alert').fadeOut();
        }, 5000);
    }
    
    // 페이지 중앙에 큰 성공 메시지(모달형) 표시 함수
    function showPageSuccessMessage(message) {
        // 기존 메시지/백드롭 제거
        $('.page-success-message, #pageSuccessBackdrop').remove();

        // 반투명 백드롭
        var backdrop = $('<div id="pageSuccessBackdrop"/>').css({
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            background: 'rgba(0,0,0,0.4)',
            zIndex: 9998
        });

        // 중앙 컨테이너
        var container = $('<div class="page-success-message alert alert-success" role="alert"/>').css({
            position: 'fixed',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            zIndex: 9999,
            minWidth: '420px',
            maxWidth: '90%',
            padding: '26px 30px',
            borderRadius: '10px',
            boxShadow: '0 10px 24px rgba(0,0,0,0.25)',
            textAlign: 'center',
            fontSize: '16px',
            lineHeight: 1.5,
            backgroundColor: '#d4edda',
            borderColor: '#c3e6cb'
        });

        var closeBtn = $('<button type="button" class="close" aria-label="Close"/>')
            .css({ position: 'absolute', top: '8px', right: '12px' })
            .html('<span aria-hidden="true">&times;</span>')
            .on('click', removeSuccessMessage);

        var title = $('<div/>').css({ fontSize: '20px', fontWeight: 700, marginBottom: '8px' })
            .html('<i class="fa fa-check-circle"></i> Password Reset');
        var body = $('<div/>').css({ marginBottom: '10px' })
            .html(message + '<br><small class="mt-1 d-block"><i class="fas fa-info-circle"></i> If you cannot find the email in your inbox, please also check your spam/junk folder.</small>');
        var ok = $('<button type="button" class="btn btn-success btn-sm">OK</button>').on('click', removeSuccessMessage);

        container.append(closeBtn, title, body, ok);
        $('body').append(backdrop).append(container);

        // 백드롭 클릭 시 닫기
        backdrop.on('click', removeSuccessMessage);

        function removeSuccessMessage() {
            container.fadeOut(150, function(){ $(this).remove(); });
            backdrop.fadeOut(150, function(){ $(this).remove(); });
        }
    }
});

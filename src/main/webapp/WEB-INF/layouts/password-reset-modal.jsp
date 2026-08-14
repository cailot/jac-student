<!-- Password Reset Request Modal -->
<div class="modal fade" id="passwordResetModal" tabindex="-1" role="dialog" aria-labelledby="passwordResetModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false" data-close="false">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header simple-header">
                <h5 class="modal-title" id="passwordResetModalLabel">
                    <i class="fas fa-key"></i> Password Reset Request
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            
            <div class="modal-body">
                <form id="passwordResetForm">
                    <div class="form-group">
                        <label for="resetFullName">
                            <i class="fas fa-user"></i> Full Name</span>
                        </label>
                        <input type="text" class="form-control" id="resetFullName" name="fullName" 
                               placeholder="Enter your full name" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="resetStudentId">
                            <i class="fas fa-id-card"></i> Student ID <span class="text-danger">*</span>
                        </label>
                        <input type="text" class="form-control" id="resetStudentId" name="studentId" 
                               placeholder="Enter your student ID" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="resetEmail">
                            <i class="fas fa-envelope"></i> Email Address <span class="text-danger">*</span>
                        </label>
                        <input type="email" class="form-control" id="resetEmail" name="email" 
                               placeholder="Enter your email address" required>
                    </div>
                    
                    <div class="fancy-info-box">
                        <div class="info-header">
                            <div class="info-icon-wrapper">
                                <i class="fas fa-shield-alt"></i>
                            </div>
                            <div class="info-title">Security Notice</div>
                        </div>
                        <div class="info-content">
                            <p>Your password will be automatically reset and sent to your email address.</p>
                            <div class="info-badges">
                                <span class="badge badge-primary">
                                    <i class="fas fa-user-check"></i> Identity Verification
                                </span>
                                <span class="badge badge-info">
                                    <i class="fas fa-envelope"></i> Email Match
                                </span>
                                <span class="badge badge-success">
                                    <i class="fas fa-key"></i> Auto Reset
                                </span>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <i class="fas fa-times"></i> Cancel
                </button>
                <button type="submit" form="passwordResetForm" class="btn btn-primary" id="resetSubmitBtn">
                    <i class="fas fa-paper-plane"></i> Send Request
                </button>
            </div>
        </div>
    </div>
</div>

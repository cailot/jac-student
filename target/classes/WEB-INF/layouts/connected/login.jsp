<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
#background {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 80vh;
    border-radius: 10px;
    background-color: rgba(0, 0, 0, 0.5);
    overflow: hidden; /* Ensure the animation stays within the background container */
    position: relative; /* Needed for absolute positioning of animation layers */
}

.background-animation {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url('${pageContext.request.contextPath}/image/cc-login.png');
    background-size: cover;
   /* animation: animateBackground 20s linear infinite; */
}

/*
@keyframes animateBackground {
    0% {
        transform: scale(1);
    }
    50% {
        transform: scale(1.5);
    }
    100% {
        transform: scale(1);
    }
}
*/
.card {
	border: none;
	border-radius: 0px;
	box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.5);
	transition: transform 0.3s ease;
	margin-bottom: 15px;
}

.card:hover {
	transform: translateY(-5px);
}

</style>

<div id="background" class="container mt-5">
    <div class="background-animation"></div>
    <div class="row h-100 justify-content-center align-items-center">		
		<div class="card">
            <h3 class="card-header bg-primary text-white text-center">Connected Class</h3>
			<div class="card-body">
                <form:form  action="${pageContext.request.contextPath}/connected/processLogin" method="POST">
                    <div class="row mb-1">
						<div class="col-md-12">
							<div class="form-group">
								<!-- Check for login error -->
								<c:if test="${param.error != null}">
									<div class="alert alert-danger col-xs-offset-1 col-xs-10">
									Invalid username and password.
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
									<span class="input-group-text bg-primary text-white"><i class="bi bi-person-fill text-white" aria-hidden="true"></i></span>
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
									<span class="input-group-text bg-primary text-white"><i class="bi bi-unlock-fill text-white" aria-hidden="true"></i></span>
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
							<input type="submit" class="btn btn-primary btn-lg btn-block text-white" value="Login" name="submit">
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="text-primary text-right small">
								<a href="mailto:jaccomvictoria@gmail.com?subject=Password%20Reset%20Request&body=Please%20send%20me%20instructions%20to%20reset%20my%20password.">
									Forgot your password?
								</a>	
							</div>
						</div>
					</div>
                </form:form>
            </div>
        </div>		
    </div>
</div>

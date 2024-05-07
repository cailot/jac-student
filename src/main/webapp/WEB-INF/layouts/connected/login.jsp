<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
  background-image: url('${pageContext.request.contextPath}/image/login-7.png');
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
</style>
<div id="background" class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
	<div class="background-animation">
		<div class="left-container">
			<h3 class="text-white text-center" ><img src="${pageContext.request.contextPath}/image/logo.png"></img></h3>
			<h6 class="text-secondary text-center mb-4" style="background: #ffffff !important;">Sign In To James An College</h6>
			<div class="card-container">			
				<div class="row h-100 justify-content-center align-items-center">						
					<div class="card">
						<h3 class="card-header text-white text-center" style="background: #2d398e !important;">Connected Class</h3>
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
			</div> <!-- end of card-container -->
			<h6 class="text-center" style="position: fixed; bottom: 0; width: 100%;">
				2015 - <%=new java.util.Date().getYear() + 1900%>&copy;&nbsp; All rights reserved.&nbsp;&nbsp;
				<div class="copyright-font-color">James An College</div>
			</h6>		
		</div><!-- end of left-container-->
	</div>
</div>

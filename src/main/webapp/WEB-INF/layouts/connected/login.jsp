<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row h-100 justify-content-center align-items-center bg m-5">
	<div class="card">
		<h4 class="card-header">Connected Class Login</h4>
		<div class="card-body">
			<form:form  action="${pageContext.request.contextPath}/connected/processLogin" method="POST">
				<div class="row">
					<div class="col-md-12">
						<div class="form-group">
						<!-- Check for login error -->
						<c:if test="${param.error != null}">
							<div class="alert alert-danger col-xs-offset-1 col-xs-10">
								Invalid username and password.</div>
						</c:if>
						<!-- Check for logout -->
						<c:if test="${param.logout != null}">
							<div class="alert alert-success col-xs-offset-1 col-xs-10">
								You have been logged out.</div>
						</c:if>
							<label>Username</label>
							<div class="input-group">
								<div class="input-group-prepend">
									<span class="input-group-text"><i class="bi bi-person-fill" aria-hidden="true"></i></span>
								</div>
								<input type="text" class="form-control" name="username" />
							</div>
							<div class="help-block with-errors text-danger"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="form-group">
							<label>Password</label>
							<div class="input-group">
								<div class="input-group-prepend">
									<span class="input-group-text"><i class="bi bi-unlock-fill" aria-hidden="true"></i></span>
								</div>
								<input type="password" name="password" class="form-control"/>
							</div>
							<div class="help-block with-errors text-danger"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<input type="hidden" name="redirect" value="">
						<input type="submit" class="btn btn-primary btn-lg btn-block" value="Login" name="submit">
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>


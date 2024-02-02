<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
.jae-header{
	padding : 0;
	/*background-color: #263343;*/
	background-color: #2d398e;
}
.navbar {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 8px 12px;
}
.navbar a {
	text-decoraton: none;
	color: white;
}
/* .navbar_logo{
	font-size: 24px;
	color: white;
} */
.navbar_menu{
	display: flex;
	list-style: none;
	padding-left: 0;
}
.navbar_menu li {
	padding: 8px 12px;
}
.navbar_menu li:hover{
	background-color: '#e9ecef';
	border-radius: 4px;
}
/* 
.navbar_icon{
	color: white;
	font-size: 24px;
	list-style: none;
	display: flex;
	padding-left: 0;
} */
.navbar_icon li {
	padding: 8px 12px;
}

@media screen and (max-width: 768px){
	.navbar{
		flex-direction: column;
		align-items: flex-start;
		padding: 8px 12px;
	}
	.navbar_menu{
		flex-direction: column;
		align-items: center;
		width: 100%;
	}
	.navbar_icon{
		justify-content: center;
		width: 100%;
		
	}

}
</style>
<div class="container-fluid jae-header">
<nav class="navbar">
	<div class="navbar_logo">
		<img src="${pageContext.request.contextPath}/image/logo.png" style="filter: brightness(0) invert(1);width:45px;" >
		James An College
	</div>
	<ul class="navbar_menu">
		

		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/admin" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			  Student
			</a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/studentAdmin">Administration</a>
			  	<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/studentList">Enrolment List</a>
				<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/studentInvoice">Invoice Record</a>
				<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/studentAttendance">Attendance</a>
			</div>
		</li>

		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/admin" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			  Course
			</a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/courseList">Course List</a>
			  	<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/classList">Class List</a>
			</div>
		</li>

		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/admin" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			  Teacher
			</a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
			  	<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/teacherList">Teacher List</a>
			</div>
		</li>

		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/admin" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			  User
			</a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
			  	<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/classList">Class List</a>
				<a class="dropdown-item" style="color: #212529;" href="#">Student Invoice</a>
			</div>
		</li>

		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/admin" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			  Setting
			</a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/cycle">Academic Cycle</a>
				<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/branch">Branch Management</a>
			  	<a class="dropdown-item" style="color: #212529;" href="${pageContext.request.contextPath}/setting">Admin Automation</a>
				<a class="dropdown-item" style="color: #212529;" href="#">Admin Property</a>
			</div>
		</li>

	</ul>
	<ul class="navbar_icon">
		<li><i class="fa fa-user-circle"></i></li>	
	</ul>
</nav>
</div>
 
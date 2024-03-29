<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
	

	.container {
		max-width: 80%;
		margin: 0 auto;
		background-color: #fff;
		border-radius: 10px;
		box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
		padding: 30px;
	}
	
	h1 {
		text-align: center;
		color: #007bff;
		margin-bottom: 30px;
	}
	
	.card {
		border: none;
		border-radius: 10px;
		box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.5);
		transition: transform 0.3s ease;
		margin-bottom: 20px;
	}

	.card:hover {
		transform: translateY(-5px);
	}

	.card-title {
		color: #007bff;
		margin-bottom: 10px;
	}

	.card-text {
		font-size: 18px;
	}

	.btn-start {
		background-color: #007bff;
		color: #fff;
		border: none;
		border-radius: 5px;
		padding: 10px 20px;
		font-size: 18px;
		cursor: pointer;
		transition: background-color 0.3s ease;
	}

	.btn-start:hover {
		background-color: #0056b3;
	}

	.jae-color {
		color: #2d398e;
	}
	
</style>

<div class="container mt-5" style="background-image: url('${pageContext.request.contextPath}/image/cc-welcome.jpg'); background-size: cover;">
	<h1 class="text-center font-italic jae-color mb-4" style="text-shadow: 5px 5px 5px rgba(170, 221, 238, 1);">Welcome to Connected Class!</h1>
	<p class="lead text-center text-secondary mb-5">Your ultimate learning companion at James An College</p>
	<div class="row">
		<div class="col-md-6">
			<div class="card mb-4">
				<div class="card-body">
					<h4 class="card-title">Homework</h4>
					<p class="card-text">Get ready to breeze through your homework like a pro! Our specially designed homework schedules are like secret maps that guide you through the exciting world of classroom learning. With fun and interactive assignments, you'll uncover new discoveries and deepen your understanding of what you learn in class.</p>
				</div>
			</div>
			<div class="card mb-4">
				<div class="card-body">
					<h4 class="card-title">Practice</h4>
					<p class="card-text">Get set to practice like a champ! Our practice sessions are like friendly competitions where you get to flex your brain muscles and show off your skills. With instant feedback and unlimited retries, you can conquer any challenge with confidence. Plus, get a head start on test prep with special practice tests designed just for you.</p>
				</div>
			</div>
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Jac-eLearning</h4>
					<p class="card-text">Need access to online classes? Look no further than Jac e-Learning! Whether it's live streams or recorded sessions, this link has everything you need to stay connected to your education. Join the online learning revolution and take control of your academic journey from anywhere, anytime. With Jac e-Learning, the classroom is always just a click away.</p>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="card mb-4">
				<div class="card-body">
					<h4 class="card-title">Extra Materials</h4>
					<p class="card-text">Think of this as your ultimate treasure trove of learning goodies! Dive into a world of extra materials that go beyond the classroom walls. From tackling tricky topics to exploring fascinating subjects, these resources are your secret weapon for turning challenges into triumphs. It's like having your own personal tutor right at your fingertips!</p>
				</div>
			</div>
			<div class="card mb-4">
				<div class="card-body">
					<h4 class="card-title">Test</h4>
					<p class="card-text">Put your knowledge to the test and celebrate your achievements! Stay on top of your game with our handy test schedule and unlock personalized scorecards. See how you stack up against classmates, track your progress with easy-to-download PDF results, and set new goals. Your journey to greatness starts here!</p>
				</div>
			</div>
			<div class="card border-primary mb-3">
				<div class="card-header bg-primary text-white text-center"></div>
				<div class="card-body text-primary">
					<p class="card-text text-center text-success font-italic">At Connected Class, learning is not just a journey - it's an adventure!</p>
					<p class="card-text text-left text-info font-italic" style="margin-bottom: 0px;">Buckle up and get ready to explore, discover, and unleash your full potentials.</p>
					<p class="h5 text-right font-weight-bold font-italic jae-color" style="margin-bottom: 0px;">Your amazing learning adventure starts here !! 
						<span style="display: inline-block; transform: rotate(45deg);">
							<i class="bi bi-airplane-fill text-warning h3"></i>
						</sapn>
					</p>
				</div>
				<div class="card-footer bg-primary text-white text-center"></div>
				
			</div>
		</div>
	</div>
</div>
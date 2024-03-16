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
		box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
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
	
</style>

<div class="container mt-5">
	<h1 class="text-center mb-4">Welcome to Connected Class!</h1>
	<p class="lead text-center mb-5">Your ultimate learning companion at James An College</p>

	<div class="row">
		<div class="col-md-6">
			<div class="card mb-4">
				<div class="card-body">
					<h2 class="card-title">Homework</h2>
					<p class="card-text">Get ready to breeze through your homework like a pro! Our specially designed homework schedules are like secret maps that guide you through the exciting world of classroom learning. With fun and interactive assignments, you'll uncover new discoveries and deepen your understanding of what you learn in class. Say goodbye to homework woes and hello to academic excellence!</p>
				</div>
			</div>
			<div class="card mb-4">
				<div class="card-body">
					<h2 class="card-title">Extra Materials</h2>
					<p class="card-text">Think of this as your ultimate treasure trove of learning goodies! Dive into a world of extra materials that go beyond the classroom walls. From tackling tricky topics to exploring fascinating subjects, these resources are your secret weapon for turning challenges into triumphs. It's like having your own personal tutor right at your fingertips! With our extensive collection of materials, there's no limit to what you can achieve.</p>
				</div>
			</div>
			<div class="card">
				<div class="card-body">
					<h2 class="card-title">Practice</h2>
					<p class="card-text">Get set to practice like a champ! Our practice sessions are like friendly competitions where you get to flex your brain muscles and show off your skills. With instant feedback and unlimited retries, you can conquer any challenge with confidence. Plus, get a head start on test prep with special practice tests designed just for you. Practice makes perfect, and with our tailored exercises, you'll be unstoppable!</p>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="card mb-4">
				<div class="card-body">
					<h2 class="card-title">Test</h2>
					<p class="card-text">Time to put your knowledge to the test! Whether it's class tests or special assessments, we've got you covered. Stay on top of your game with our handy test schedule and get ready to show off what you've learned. It's your chance to shine and showcase your awesome skills to the world! With our comprehensive testing platform, success is just a test away.</p>
				</div>
			</div>
			<div class="card">
				<div class="card-body">
					<h2 class="card-title">Test Result</h2>
					<p class="card-text">Get ready for the big reveal! After each test, unlock your personalized scorecard to celebrate your incredible achievements. See how you stack up against your classmates and discover areas where you can shine even brighter. With easy-to-download PDF results, you can track your progress and set new goals like a true superstar. Your journey to greatness starts with knowing where you stand, and our detailed results will guide you every step of the way.</p>
				</div>
			</div>
			<div class="card">
				<div class="card-body">
					<h2 class="card-title">Jac-eLearning</h2>
					<p class="card-text">Need access to online classes? Look no further than Jac e-Learning! Whether it's live streams or recorded sessions, this link has everything you need to stay connected to your education. Join the online learning revolution and take control of your academic journey from anywhere, anytime. With Jac e-Learning, the classroom is always just a click away.</p>
				</div>
			</div>
		</div>
	</div>
	<p class="text-center mt-5">At Connected Class, learning is not just a journey - it's an adventure! So buckle up and get ready to explore, discover, and unleash your full potential with us.<br>Your amazing learning adventure starts here!</p>
</div>
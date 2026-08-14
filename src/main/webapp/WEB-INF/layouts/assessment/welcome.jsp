<style>
    .assessment-container {
        background: #f9f9f9; /* Light gray background for a softer look */
        border-radius: 12px;
        padding: 2.5rem;
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
        max-width: 550px;
        margin: 2rem auto; /* Center the container */
        text-align: center;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* Modern font */
    }
    
    .assessment-container h1 {
        font-size: 2.2rem;
        margin-bottom: 1.2rem;
        font-weight: 700;
        color: #333; /* Darker color for better contrast */
    }
    
    .assessment-container p {
        font-size: 1.1rem;
        margin-bottom: 1.5rem;
        color: #555; /* Softer text color for better readability */
        line-height: 1.6;
    }
    
    .assessment-container .btn {
        font-size: 1rem;
        padding: 0.6rem 2.5rem;
        background-color: #0069d9;
        color: white;
        border: none;
        border-radius: 25px; /* Rounded button for a more modern look */
        text-decoration: none;
        transition: background-color 0.3s ease, transform 0.3s ease; /* Smooth transition */
        box-shadow: 0 4px 8px rgba(0, 105, 217, 0.2); /* Subtle shadow for depth */
    }
    
    .assessment-container .btn:hover {
        background-color: #0053a4; /* Darker shade on hover */
        transform: translateY(-2px); /* Lift effect on hover */
        box-shadow: 0 6px 12px rgba(0, 105, 217, 0.3); /* Enhanced shadow on hover */
    }
    
    .assessment-container .btn:active {
        transform: translateY(0); /* Button press effect */
        box-shadow: 0 4px 8px rgba(0, 105, 217, 0.2);
    }
</style>

<div class="assessment-container">
    <h1>Online Assessment</h1>

    <p>Thank you for participating in the James An College Assessment Test!</p>

    <p>This test helps us understand how we can best support your child's learning journey.</p>

    <p>Please provide accurate details so we can reach out to you promptly.</p>

    <p>We are excited about the opportunity to support your child's educational growth and look forward to welcoming you to our academic support!</p>
    
    <a href="${pageContext.request.contextPath}/assessment/apply" class="btn" role="button">NEXT</a>
</div>
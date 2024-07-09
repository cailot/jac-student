<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Assessment</title>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8f9fa;
            margin: 0;
        }
        .assessment-container {
            background: white;
            border-radius: 10px;
            padding: 2rem;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            text-align: center;
        }
        .assessment-container h1 {
            font-size: 2rem;
            margin-bottom: 1rem;
            font-weight: bold;
        }
        .assessment-container p {
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }
        .assessment-container .btn {
            font-size: 1rem;
            padding: 0.5rem 2rem;
        }
    </style>
</head>
<body>
    <div class="assessment-container">
        <h1>Online Assessment</h1>
        <p>Hello,</p>
        <p>Thank you for taking the James An College Assessment Test.</p>
        <p>This assessment test is to help us understand how we can help you in your learning journey.</p>
        <p>Please enter your correct details so we can get back to you as soon as possible.</p>
        <p>We look forward to having you join us!</p>
        <a href="${pageContext.request.contextPath}/assessment/apply" class="btn btn-primary" role="button">NEXT</a>
    </div>

    <script>
        function nextStep() {
            // Add your next step logic here
            alert("Proceed to the next step.");
        }
    </script>
</body>
</html>
// script.js

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');
    const signupLink = document.getElementById('signup-link');
    const loginLink = document.getElementById('login-link');
    const loginError = document.getElementById('login-error');
    const signupError = document.getElementById('signup-error');

    // Congratulatory Popup Elements
    const congratsPopup = document.getElementById('congrats-popup');
    const congratsMessageHeader = document.getElementById('congrats-message-header');
    const congratsMessageBody = document.getElementById('congrats-message-body');


    signupLink.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent default link behavior
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
        loginError.style.display = 'none'; // Hide errors when switching forms
        signupError.style.display = 'none';
        congratsPopup.style.display = 'none'; // Hide popup when switching forms
    });

    loginLink.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent default link behavior
        signupForm.style.display = 'none';
        loginForm.style.display = 'block';
        loginError.style.display = 'none'; // Hide errors when switching forms
        signupError.style.display = 'none';
        congratsPopup.style.display = 'none'; // Hide popup when switching forms
    });

    loginForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent actual form submission for this demo
        const email = loginForm.querySelector('#login-email').value;
        const password = loginForm.querySelector('#login-password').value;

        if (!email || !password) {
            loginError.textContent = "Please fill in all fields.";
            loginError.style.display = 'block';
            congratsPopup.style.display = 'none'; // Ensure popup is hidden on error
        } else {
            // Simulate successful login
            loginError.style.display = 'none';
            loginForm.reset();

            // Show congratulatory popup for login
            congratsMessageHeader.textContent = "Login Successful, Rockstar!";
            congratsMessageBody.textContent = "You're in and ready to make some noise.";
            congratsPopup.style.display = 'flex'; // Use flex to center the popup

            // Redirect to homepage after 5 seconds
            setTimeout(function() {
                window.location.href = 'index.html'; // Replace with your homepage URL if different
            }, 5000); // 5000 milliseconds = 5 seconds
        }
    });

    signupForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent actual form submission for this demo
        const email = signupForm.querySelector('#signup-email').value;
        const password = signupForm.querySelector('#signup-password').value;
        const confirmPassword = signupForm.querySelector('#confirm-password').value;

        if (!email || !password || !confirmPassword) {
            signupError.textContent = "Please fill in all fields.";
            signupError.style.display = 'block';
            congratsPopup.style.display = 'none'; // Ensure popup is hidden on error
        } else if (password !== confirmPassword) {
            signupError.textContent = "Passwords do not match.";
            signupError.style.display = 'block';
            congratsPopup.style.display = 'none'; // Ensure popup is hidden on error
        }  else {
            // Simulate successful signup
            signupError.style.display = 'none';
            signupForm.reset();

            // Show congratulatory popup for signup
            congratsMessageHeader.textContent = "Signup Successful, Rockstar!";
            congratsMessageBody.textContent = "Welcome to the stage! Get ready to rock.";
            congratsPopup.style.display = 'flex'; // Use flex to center the popup

            // Optionally switch back to login form after successful signup for better UX
            signupForm.style.display = 'none';
            loginForm.style.display = 'block';

            // Redirect to homepage after 5 seconds
            setTimeout(function() {
                window.location.href = 'index.html'; // Replace with your homepage URL if different
            }, 5000); // 5000 milliseconds = 5 seconds
        }
    });
});
document.addEventListener('DOMContentLoaded', () => {
    const authForm = document.getElementById('auth-form');
    const toggleLink = document.getElementById('toggle-link');
    const formTitle = document.getElementById('form-title');
    const submitBtn = document.getElementById('submit-btn');
    const confirmPasswordField = document.getElementById('confirm-password-field');
    const emailField = document.getElementById('email-field');
    const errorMsg = document.getElementById('error-msg');

    let isLogin = true;

    // Toggle between login and signup
    toggleLink.addEventListener('click', () => {
        isLogin = !isLogin;
        formTitle.textContent = isLogin ? 'Login' : 'Sign Up';
        submitBtn.textContent = isLogin ? 'Login' : 'Sign Up';
        toggleLink.textContent = isLogin
            ? "Don't have an account? Sign up"
            : "Already have an account? Login";

        confirmPasswordField.style.display = isLogin ? 'none' : 'block';
        emailField.style.display = isLogin ? 'none' : 'block';
        errorMsg.textContent = '';
    });

    authForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorMsg.textContent = '';

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const confirmPassword = document.getElementById('confirmPassword')?.value?.trim();
        const email = document.getElementById('email')?.value?.trim();

        if (!username || !password) {
            errorMsg.textContent = 'Username and password are required.';
            return;
        }

        if (!isLogin) {
            if (!email) {
                errorMsg.textContent = 'Email is required.';
                return;
            }
            if (!confirmPassword || confirmPassword !== password) {
                errorMsg.textContent = 'Passwords do not match.';
                return;
            }
        }

        const payload = isLogin
            ? { username, password }
            : { username, password, email };

        try {
             const response = await fetch(`/api/auth/${isLogin ? 'login' : 'signup'}`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });

            const data = await response.json();
            console.log('Response data:', data);

            if (!response.ok) {
                errorMsg.textContent = data.message || 'Something went wrong.';
                return;
            }

            // Save JWT token to localStorage
            if (data.data && data.data.token) {
                        localStorage.setItem('token', data.data.token);
                        console.log('Saved token to localStorage:', data.data.token);

                        // Redirect to chat UI
                        window.location.href = '/chat.html';
                    } else {
                        errorMsg.textContent = 'Token is missing in the response.';
                    }

        } catch (err) {
            console.error('Request failed:', err);
            errorMsg.textContent = 'Network error or server not responding.';
        }
    });
});

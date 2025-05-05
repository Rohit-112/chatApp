document.addEventListener('DOMContentLoaded', () => {
    const messageContainer = document.getElementById('message-container');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');
    const logoutButton = document.getElementById('logout-button');
    const loggedUser = document.getElementById('logged-user');

    // Get token and username from localStorage
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');

    // Set logged-in username
    loggedUser.textContent = username || 'Unknown User';

    // Handle user logout
    logoutButton.addEventListener('click', () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        window.location.href = '/auth.html'; // Redirect to login page
    });

    // WebSocket connection
    const socket = new SockJS('/ws-chat');
    const stompClient = Stomp.over(socket);

    // Connect to WebSocket server
    stompClient.connect({ Authorization: `Bearer ${token}` }, (frame) => {
        console.log('Connected: ' + frame);

        // Subscribe to the user's message queue
        stompClient.subscribe(`/user/${username}/queue/messages`, (messageOutput) => {
            // Display received message
            displayMessage(messageOutput.body, 'received');
        });
    }, (error) => {
        console.error('WebSocket error:', error);
    });

    // Display message in the UI
    const displayMessage = (message, type) => {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', type);
        messageElement.textContent = message;
        messageContainer.appendChild(messageElement);
        messageContainer.scrollTop = messageContainer.scrollHeight;
    };

    // Send message to the server
    sendButton.addEventListener('click', () => {
        const message = messageInput.value.trim();
        if (message) {
            // Display sent message
            displayMessage(message, 'sent');
            sendMessageToServer(message);
            messageInput.value = '';
        }
    });

    const sendMessageToServer = (message) => {
        if (stompClient.connected) {
            stompClient.send('/app/chat.sendMessage', { Authorization: `Bearer ${token}` }, JSON.stringify({ sender: username, receiver: 'anotherUser', message }));
        }
    };
});

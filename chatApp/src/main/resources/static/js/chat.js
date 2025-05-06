document.addEventListener('DOMContentLoaded', () => {
    const messageContainer = document.getElementById('message-container');
    const messageInput = document.getElementById('message-input');
    const sendButton = document.getElementById('send-button');
    const logoutButton = document.getElementById('logout-button');
    const loggedUser = document.getElementById('logged-user');

    const token = localStorage.getItem('token');
    console.log("Token from localStorage:", token);

    // Handle logout
    logoutButton.addEventListener('click', () => {
        localStorage.removeItem('token');
        window.location.href = '/auth.html';
    });

    // WebSocket connection
    console.log("Attempting to connect to WebSocket...");
    const socket = new SockJS("http://localhost:8081/ws-chat?token=" + token);
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);

        // Subscribe to user's private queue
        stompClient.subscribe('/user/queue/messages', (messageOutput) => {
            console.log("Message received:", messageOutput);
            displayMessage(messageOutput.body, 'received');
        });
    }, (error) => {
        console.error('WebSocket error:', error);
    });

    const displayMessage = (message, type) => {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', type);
        messageElement.textContent = message;
        messageContainer.appendChild(messageElement);
        messageContainer.scrollTop = messageContainer.scrollHeight;
    };

    sendButton.addEventListener('click', () => {
        const message = messageInput.value.trim();
        if (message) {
            displayMessage(message, 'sent');
            sendMessageToServer(message);
            messageInput.value = '';
        }
    });

    const sendMessageToServer = (message) => {
        const receiverUsername = prompt('Enter the username of the receiver:');
        if (stompClient.connected && receiverUsername) {
            stompClient.send('/app/chat.sendMessage',
                {},
                JSON.stringify({ receiver: receiverUsername, message }) // adjust if 'sender' needed
            );
        }
    };
});

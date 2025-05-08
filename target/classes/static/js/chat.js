let stompClient = null;
let selectedUsername = null;
let currentUsername = null;

document.addEventListener('DOMContentLoaded', () => {
  currentUsername = getUsernameFromToken(localStorage.getItem('token'));

  if (!currentUsername) {
    console.error("Could not extract username from token");
    return;
  }

  console.log("Logged in as:", currentUsername);
  // Proceed with WebSocket connection and user list fetching
  connectWebSocket();
  fetchUserList();

  document.getElementById('refreshUsers').addEventListener('click', () => {
    fetchUserList();
  });
});

// Function to extract username from JWT token
function getUsernameFromToken(token) {
  if (!token) return null;
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload?.sub;
}

function connectWebSocket() {
  const token = localStorage.getItem('token');
  console.log("Token from localStorage:", token);
  if (!token) {
    console.error("JWT Token missing");
    return;
  }

  const socketUrl = `ws://localhost:8081/ws?token=${token}`;
  console.log("Attempting to connect to WebSocket:", socketUrl);

  // Initialize stompClient with the WebSocket URL
  stompClient = Stomp.over(new WebSocket(socketUrl));

  // Open WebSocket connection
  stompClient.connect(
    { Authorization: `Bearer ${token}` },
    function (frame) {
      console.log('Connected: ' + frame);
      console.log('Connection state:', stompClient.connected ? 'Connected' : 'Not Connected');

      // Dynamically subscribe to the user-specific message queue
      stompClient.subscribe(`/user/${currentUsername}/queue/messages`, function (messageOutput) {
        const message = JSON.parse(messageOutput.body);
        displayIncomingMessage(message);
      });
    },
    function (error) {
      console.error('Connection error:', error);
    }
  );
}

// Fetch and display users
function fetchUserList() {
  const apiUrl = 'http://localhost:8081/api/auth/users';

  fetch(apiUrl)
    .then(response => response.json())
    .then(responseJson => {
      const users = responseJson.data;

      const userListContainer = document.getElementById('userList');
      userListContainer.innerHTML = '';

      users.forEach(user => {
        if (user.username === currentUsername) return;

        const userItem = document.createElement('li');
        userItem.textContent = `${user.username} (${user.online ? '🟢 Online' : '🔴 Offline'})`;
        userItem.classList.add('user-item');
        userItem.style.padding = '10px';
        userItem.style.cursor = 'pointer';
        userItem.style.borderBottom = '1px solid #ddd';

        userItem.addEventListener('click', () => {
          selectUser(user.username);
        });

        userListContainer.appendChild(userItem);
      });
    })
    .catch(error => console.error('Error fetching user list:', error));
}

function selectUser(username) {
  selectedUsername = username;
  const selectedUserNameElement = document.getElementById('selectedUserName');
  selectedUserNameElement.textContent = "Chat with: " + username;

  loadChatHistory(username);
}

function loadChatHistory(username) {
  const chatMessages = document.getElementById('chatMessages');
  chatMessages.innerHTML = `<div><strong>Chat history with ${username}:</strong></div>`;
}

function sendMessage() {
  const messageInput = document.getElementById('messageInput');
  const message = messageInput.value;

  if (message && selectedUsername && stompClient) {
    const chatMessage = {
      receiver: selectedUsername,
      message: message,
      timestamp: new Date().toISOString()
    };

    stompClient.send(`/app/chat/${selectedUsername}`, {}, JSON.stringify(chatMessage));
    const chatMessages = document.getElementById('chatMessages');
    chatMessages.innerHTML += `<div><strong>You:</strong> ${message}</div>`;
    messageInput.value = '';
  }
}

function displayIncomingMessage(message) {
  console.log("Received message:", message);
  const chatMessages = document.getElementById('chatMessages');
  chatMessages.innerHTML += `<div><strong>${message.senderName}:</strong> ${message.message}</div>`;

  // Scroll to the bottom of the chat window
  chatMessages.scrollTop = chatMessages.scrollHeight;
}

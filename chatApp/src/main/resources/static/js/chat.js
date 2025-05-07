let stompClient = null;
let selectedUsername = null;
let currentUsername = null;

document.addEventListener('DOMContentLoaded', () => {
  currentUsername = prompt("Enter your username (for test purpose only):");
  connectWebSocket();
  fetchUserList();
});

function connectWebSocket() {
const token = localStorage.getItem('token');
    console.log("Token from localStorage:", token)
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

      // Subscribe to a user-specific message queue
      stompClient.subscribe('/user/queue/messages', function (messageOutput) {
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

      users.forEach(username => {
        if (username === currentUsername) return; // Don't show self

        const userItem = document.createElement('li');
        userItem.textContent = username;
        userItem.classList.add('user-item');
        userItem.style.padding = '10px';
        userItem.style.cursor = 'pointer';
        userItem.style.borderBottom = '1px solid #ddd';

        userItem.addEventListener('click', () => {
          selectUser(username);
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
      senderName: currentUsername,
      receiver: selectedUsername,
      message: message,
      timestamp: new Date().toISOString()
    };

    stompClient.send("/app/chat/" + selectedUsername, {}, JSON.stringify(chatMessage));

    const chatMessages = document.getElementById('chatMessages');
    chatMessages.innerHTML += `<div><strong>You:</strong> ${message}</div>`;
    messageInput.value = '';
  }
}

function displayIncomingMessage(message) {
  const chatMessages = document.getElementById('chatMessages');
  chatMessages.innerHTML += `<div><strong>${message.senderName}:</strong> ${message.message}</div>`;
}

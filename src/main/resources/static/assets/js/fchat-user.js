'use strict';
const chatArea = document.querySelector('#chatArea');
const btnSendMessage = document.querySelector('#btnSendMessage');
const messageInput = document.querySelector('#message-input');
const contactBar = document.querySelector('#contactsSidebar');
const userId = document.querySelector('#userid');
let selectedUserId =null
let subscription = null;
let subscriptionMess = null;
let stompClient = null;
const socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, onConnected, onError);
let clientId = localStorage.getItem('username') || uuidv4();
localStorage.setItem('username', clientId);
// event.preventDefault();
const uID = userId == null ? clientId : userId.textContent;
contactBar.classList.add('hidden');
console.log(userId)
console.log(uID)
//click to conversion
function userItemClick(event) {
    //remove active user
    // document.querySelectorAll('.user-item').forEach(item => {
    //     item.classList.remove('active');
    // });
    chatArea.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    // clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    //new message
    // const nbrMsg = clickedUser.querySelector('.nbr-msg');
    // nbrMsg.classList.add('hidden');
    // nbrMsg.textContent = '0';
    console.log('userclick')
    // stompClient.send("/app/read", {}, JSON.stringify({
    //     senderId: username,
    //     recipientId: selectedUserId,
    // }));

}
function onConnected() {
    subscription = stompClient.subscribe(`/user/${uID}/queue/messages`, onMessageReceived);
    subscriptionMess = stompClient.subscribe(`/user/public`, onMessageReceived);
    findAndDisplayConversions().then();
}

async function findAndDisplayConversions() {
    const conversionsResponse = await fetch(`/conversions/${uID}`);
    let listUser = await conversionsResponse.json();
    console.log(uID)
    console.log(listUser);
    // const conversionsList = document.getElementById('conversions');
    // conversionsList.innerHTML = '';

    listUser.forEach(user => {
        addContact(user, '','','', new Date());
        // if (conversions.indexOf(user) < conversions.length - 1) {
        //     const separator = document.createElement('li');
        //     separator.classList.add('separator');
        //     conversionsList.appendChild(separator);
        // }
    });
}


function displayMessage(senderId, content, timesend) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('mb-2');
    const message = document.createElement('span');
    const time = document.createElement('span');
    time.textContent = new Date(timesend).toISOString().replace("T"," ").substring(0, 19);
    message.textContent = content;
    if (senderId === uID) {
        messageContainer.classList.add('text-right');
        message.classList.add('chat-bubble-right');
        time.classList.add('chat-bubble-right');
    } else {
        messageContainer.classList.add('text-left');
        message.classList.add('chat-bubble-left');
        time.classList.add('chat-bubble-left');
    }
    messageContainer.appendChild(message);
    messageContainer.appendChild(time);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${uID}/4`);
    const userChat = await userChatResponse.json();
    console.log(userChat +'user chat')
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content, chat.timestamp);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

function sendMessage(event) {
    const timestamp = new Date();
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: uID,
            recipientId: 4,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(uID, messageInput.value.trim(), timestamp);
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}

async function onMessageReceived(payload) {
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        console.log('display message', message.date);
        displayMessage(message.senderId, message.content, message.date);
        chatArea.scrollTop = chatArea.scrollHeight;
    }
    // if (selectedUserId) {
    //     document.querySelector(`#${selectedUserId}`).classList.add('active');
    // } else {
    //     messageForm.classList.add('hidden');
    // }

    // const notifiedUser = document.querySelector(`#${message.senderId}`);
    // if (notifiedUser && !notifiedUser.classList.contains('active')) {
    //     const nbrMsg = notifiedUser.querySelector('.nbr-msg');
    //     nbrMsg.classList.remove('hidden');
    //     nbrMsg.textContent = '';
    // }
}

// Function to create and add a contact item to the Contacts Sidebar
function addContact(id, name, lastMessage, avatarUrl, timestamp) {
    // Get the container where contacts are listed
    const contactsList = document.getElementById("contactsList");

    // Create the main list-group item div
    const contactItem = document.createElement("div");
    contactItem.classList.add("list-group-item", "d-flex", "align-items-center");
    contactItem.id = id
    // Create avatar image
    const avatar = document.createElement("img");
    avatar.src = avatarUrl;
    avatar.alt = `${name}'s avatar`;
    avatar.classList.add("rounded-circle", "mr-3");
    avatar.style.width = "40px"; // Set the size of the avatar

    // Create container for name and last message
    const contactInfo = document.createElement("div");

    // Add contact name
    const contactName = document.createElement("h6");
    contactName.classList.add("mb-0");
    contactName.textContent = name;

    // Add last message
    const messagePreview = document.createElement("small");
    messagePreview.classList.add("text-muted");
    messagePreview.textContent = lastMessage;

    // Append name and message to the contact info container
    contactInfo.appendChild(contactName);
    contactInfo.appendChild(messagePreview);

    // Create timestamp element
    const contactTime = document.createElement("small");
    contactTime.classList.add("text-muted", "ml-auto");
    contactTime.textContent = timestamp;

    // Append avatar, contact info, and timestamp to the main contact item
    contactItem.appendChild(avatar);
    contactItem.appendChild(contactInfo);
    contactItem.appendChild(contactTime);

     contactItem.addEventListener('click', userItemClick);

    // Add the complete contact item to the contacts list
    contactsList.appendChild(contactItem);
}

// // Example usage
// addContact("Sarah Connor", "I'll be back...", "https://via.placeholder.com/40", "12:45");
// addContact("John Wick", "Got any pencils?", "https://via.placeholder.com/40", "14:20");
// addContact("Ellen Ripley", "Stay frosty, people.", "https://via.placeholder.com/40", "11:15");

// Toggle chat window display
function toggleChatWindow() {
    const chatWindow = document.getElementById('chatWindow');
    chatWindow.classList.toggle('show');
}

function onError() {

}

btnSendMessage.addEventListener('click', sendMessage);
function formatDate(date) {
    var year = date.getFullYear().toString();
    var month = (date.getMonth() + 101).toString().substring(1);
    var day = (date.getDate() + 100).toString().substring(1);
    return month + '/' + day + '/' + year;
}
function formatTime(date) {
    var hours = date.getHours().toString();
    var minutes = date.getMinutes().toString();
    var seconds = date.getSeconds().toString();
    return hours + ':' + minutes + ':' + seconds;
}
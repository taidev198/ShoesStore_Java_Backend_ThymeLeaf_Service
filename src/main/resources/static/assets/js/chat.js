hideChat(0);
const chatArea = document.querySelector('#chat_converse');
const sendBtn = document.querySelector('#fab_send');
const messageInput = document.querySelector('#chatSend');
const userId = document.querySelector('#userid');
let stompClient = null;
let username = null;
let password = null;
let selectedUserId = null;
let subscription = null;
let subscriptionMess = null;
const socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, onConnected, onError);
let clientId = localStorage.getItem('username') || uuidv4();
localStorage.setItem('username', clientId);
// event.preventDefault();
const uID = userId == null ? clientId : userId.textContent;

$('#prime').click(function() {
    toggleFab();
});

function uuidv4() {
    return "10000000-1000-4000-8000-100000000000".replace(/[018]/g, c =>
        (+c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> +c / 4).toString(16)
    );
}
//Toggle chat and links
function toggleFab() {
    $('#chatSend').toggleClass('is-visible')
    $('.prime').toggleClass('zmdi-comment-outline');
    $('.prime').toggleClass('zmdi-close');
    $('.prime').toggleClass('is-active');
    $('.prime').toggleClass('is-visible');
    $('#prime').toggleClass('is-float');
    $('.chat').toggleClass('is-visible');
    hideChat(1);
    fetchAndDisplayUserChat().then();
}

$('#chat_fullscreen_loader').click(function(e) {
    $('.fullscreen').toggleClass('zmdi-window-maximize');
    $('.fullscreen').toggleClass('zmdi-window-restore');
    $('.chat').toggleClass('chat_fullscreen');
    $('.fab').toggleClass('is-hide');
    $('.header_img').toggleClass('change_img');
    $('.img_container').toggleClass('change_img');
    $('.chat_header').toggleClass('chat_header2');
    $('.fab_field').toggleClass('fab_field2');
    $('.chat_converse').toggleClass('chat_converse2');
    //$('#chat_converse').css('display', 'none');
    // $('#chat_body').css('display', 'none');
    // $('#chat_form').css('display', 'none');
    // $('.chat_login').css('display', 'none');
    // $('#chat_fullscreen').css('display', 'block');
});

function hideChat(hide) {
    switch (hide) {
        case 0:
            $('#chat_converse').css('display', 'none');
            $('#chat_body').css('display', 'none');
            $('#chat_form').css('display', 'none');
            $('.chat_login').css('display', 'block');
            $('.chat_fullscreen_loader').css('display', 'none');
            $('#chat_fullscreen').css('display', 'none');
            break;
        case 1:
            $('#chat_converse').css('display', 'block');
            $('#chat_body').css('display', 'none');
            $('#chat_form').css('display', 'none');
            $('.chat_login').css('display', 'none');
            $('.chat_fullscreen_loader').css('display', 'block');
            break;
    }
}

function onConnected() {
    subscription = stompClient.subscribe(`/user/${uID}/queue/messages`, onMessageReceived);
    subscriptionMess = stompClient.subscribe(`/user/public`, onMessageReceived);
    findAndDisplayConversions().then();
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${uID}/4`);
    console.log(userChatResponse)
    const userChat = await userChatResponse.json();
    console.log(uID)
    console.log(userChat)
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        console.log('sender' + chat.senderId);
        displayMessage(chat.senderId, chat.content, chat.timestamp);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

async function findAndDisplayConversions() {
    const conversionsResponse = await fetch(`/conversions/${uID}`);
    let conversions = await conversionsResponse.json();
    console.log(conversions);
    conversions = conversions.filter(user => user.username !== username);
    const conversionsList = document.getElementById('conversions');
    conversionsList.innerHTML = '';

    conversions.forEach(user => {
        appendUserElement(user, conversionsList);
        if (conversions.indexOf(user) < conversions.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            conversionsList.appendChild(separator);
        }
    });
}

//click to conversion
function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
    console.log('userclick')
    // stompClient.send("/app/read", {}, JSON.stringify({
    //     senderId: username,
    //     recipientId: selectedUserId,
    // }));

}

function displayMessage(senderId, content, timesend) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    console.log(uID)
    console.log(senderId)
    console.log(senderId === uID)
    if (senderId === uID) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    const time = document.createElement('p');
    time.textContent = new Date(timesend).toISOString().replace("T"," ").substring(0, 19);
    message.textContent = content;
    messageContainer.appendChild(message);
    messageContainer.appendChild(time);
    chatArea.appendChild(messageContainer);
}

async function onMessageReceived(payload) {
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (4 === message.senderId) {
        displayMessage(message.senderId, message.content, message.timestamp);
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


function onLogout() {
    // stompClient.send("/app/user.disconnectUser",
    //     {},
    //     JSON.stringify({username: username, password: password, status: 'OFFLINE'})
    // );
    // window.location.reload();
    subscription.unsubscribe();
    subscriptionMess.unsubscribe();
}

function onError(){

}
sendBtn.addEventListener('click', sendMessage, true);
messageInput.addEventListener('onkeypress', sendMessage, true);


$( '.friend-drawer--onhover' ).on( 'click',  function() {

    $( '.chat-bubble' ).hide('slow').show('slow');

});
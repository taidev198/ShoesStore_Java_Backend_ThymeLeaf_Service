// Toggle chat modal display
function toggleChat() {
    const chatModal = document.getElementById('chatModal');
    if ($(chatModal).hasClass('show')) {
        $(chatModal).modal('hide');
    } else {
        $(chatModal).modal('show');
    }
}

// Send message function
function sendMessage() {
    const chatInput = document.getElementById('chatInput');
    const chatBox = document.getElementById('chatBox');

    if (chatInput.value.trim() !== "") {
        const newMessage = document.createElement('p');
        newMessage.classList.add('chat-message');
        newMessage.textContent = chatInput.value;
        chatBox.appendChild(newMessage);
        chatInput.value = "";
    }
}
// Toggle chat window display
function toggleChatWindow() {
    const chatWindow = document.getElementById('chatWindow');
    chatWindow.classList.toggle('show');
}

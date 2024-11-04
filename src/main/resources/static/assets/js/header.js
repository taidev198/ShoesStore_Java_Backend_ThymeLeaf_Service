// var stompClient = localStorage.getItem("stompClient") || new SockJS('http://localhost:8080/ws');
// console.log(stompClient);
// if (!stompClient) {
//     console.log('set StompClient');
//     var socket = new SockJS('http://localhost:8080/ws');
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function(frame) {
//         console.log(frame+'-------------frame---------');
//         stompClient.subscribe('/user/4/notification', function(result) {
//             console.log(result);
//             show(JSON.parse(result.body));
//             console.log(result.body)
//         });
//     });
//     localStorage.setItem("stompClient", stompClient);
// }

// if (stompClient.activate()) {
//     stompClient.activate();
// }

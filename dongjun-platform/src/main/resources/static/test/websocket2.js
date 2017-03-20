console.log('enter the file!!!');
var socket = new SockJS('/portfolio');

/**
 * 建立成功的回调函数
 */
socket.onopen = function() {
    console.log('open');
};

/**
 * 服务器有消息返回的回调函数
 */
socket.onmessage = function(e) {
    console.log('message', e.data);
};

/**
 * websocket链接关闭的回调函数
 */
socket.onclose = function() {
    console.log('close');
};

var stompClient = Stomp.over(socket);
//console.log(stompClient);
stompClient.connect({}, function(frame) {
    stompClient.subscribe('/topic/greetings1',  function(data) { //只推送到用户的订阅地址
        console.log(data)
    });
//    stompClient.subscribe('/topic/greetings',  function(data) { //订阅地址
//        console.log(data)
//    });
});

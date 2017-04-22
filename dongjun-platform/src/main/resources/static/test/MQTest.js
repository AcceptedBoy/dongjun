//console.log('enter the file!!!');
//var socket = new SockJS('/portfolio');
//
///**
// * 建立成功的回调函数
// */
//socket.onopen = function() {
//    console.log('open');
//};
//
///**
// * 服务器有消息返回的回调函数
// */
//socket.onmessage = function(e) {
//    console.log('message', e.data);
//};
//
///**
// * websocket链接关闭的回调函数
// */
//socket.onclose = function() {
//    console.log('close');
//};
//
//var stompClient = Stomp.over(socket);
////console.log(stompClient);
//stompClient.connect({}, function(frame) {
//    stompClient.subscribe('/queue/user-001/hitch',  function(data) { //订阅地址
//        console.log(data)
//    });
//});
//

var client = Stomp.overTCP('localhost', 8161);  
var connectCallback = function(frame) {  
    var subscription = client.subscribe('/queue/user-001/hitch', onMessage);
    //subscription.unsubscribe();
};
var onMessage = function(message){  
    if (message.body) {
      console.log("得到信息" + message.body)
    } else {
      console.log("空信息？");
    }
};
var errorCallback = function(error){  
    console.log(error.headers.message);
};

client.connect('admin', '759486', connectCallback,connectCallback);


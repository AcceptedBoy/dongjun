var stompClient = null
dj.jsLoad('../../js/websocket/sockjs.min.js', function() {
	dj.jsLoad('../../js/websocket/storm.js', function() {
		var socket = new SockJS("/portfolio")
		stompClient = Stomp.over(socket)
		stompClient.connect({})
	})	
})
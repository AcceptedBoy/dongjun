(function() {
  var api = {
    read: '/dongjun/read_text',
    send: '/dongjun/send_text',
    stop: '/donjun/stop_read_text',
    queue: 'queue/read_text',
    infor: ''
  }

  var socket = new SockJS('/portfolio')
  var stompClient = Stomp.over(socket)
  var switchId = location.href.split('#')[1]

	function addItem(title, content) {
		$('.chat-board').append(`<div class="chat-item"><b>${title}：</b>${content}</div>`)
	}

  function startRead () {
    $.ajax({
      url: api.read,
      data: {
        switchId: switchId
      }
    })
  }

  function stopRead () {
    $.ajax({
      url: api.stop,
      data: {
        switchId: switchId
      }
    })
  }

  function setTitle (op) {
    $('.equipmentName').text(op.name)
    // ...
  }

  function askTitle () {
    $.ajax({
      url: '',
      data: {
        switchId: switchId
      },
      success: function(res) {
        if(res.success) {
          setTitle(res.text)
        }
      }
    })
  }

  stompClient.connect({}, function (frame) {
    stompClient.subscribe(api.queue, function(msg) {
      addItem('接收', JSON.parse(message.text))
    })
  })

  // 下面是给按钮添加事件
  $('#sendMsg').click(function (e) {
    // ajax
    var msg = $('.send-content').val()
    if(msg) {
      $.ajax({
        url: api.send,
        data: {
          text: $('.send-content').val(),
          switchId: switchId
        },
        success: function(res) {
          if(res.success) {
            addItem('发送', msg)
            $('.send-content').val('')
          }
        }
      })
    }
  })

  $('.send-content').keyup(function (e) {
    var event = e || window.e
    var msg = $('.send-content').val()
    if(event.keyCode == 13) {
      if(msg) {
        $.ajax({
          url: api.send,
          data: {
            text: $('.send-content').val(),
            switchId: switchId
          },
          success: function(res) {
            if(res.success) {
              addItem('发送', msg)
              $('.send-content').val('')
            }
          }
        })
      }
    }
  })

  $('#sendAll').click(function (e) {
    console.log('总召')
  })

  // 加载好页面后告诉后台开始订阅
  $(window).load(function () {
    startRead()
    askTitle()
  })

  window.onbeforeunload = function() {
    stopRead()
  }
})()
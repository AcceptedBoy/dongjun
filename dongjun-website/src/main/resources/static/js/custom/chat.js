(function() {
  var api = {
    read: '/dongjun/read_text',
    send: '/dongjun/send_text',
    stop: '/dongjun/stop_read_text',
    queue: '/queue/read_text',
    infor: ''
  }

  var socket = new SockJS('/portfolio')
  var stompClient = Stomp.over(socket)
  var switchId = ''
  var switchName = ''
  var switchNum = 0
  var showName = ''

  if(localStorage) {
    config = JSON.parse(localStorage.getItem('switchConfig'))
    if(config) {
      switchId = config.switchId
      switchName = config.switchName
      switchNum = config.switchNum
      showName = config.showName
    } else {
      console.error('客户端没有缓存到设备的id，请进入设备管理页面点击设备实时操控')
    }
  }

  // 载入监控窗口头部标题
  function showTitle () {
    $('.switchNum').text(switchNum)
    $('.switchName').text(switchName)
    $('.showName').text(showName)
  }

  // 给监控窗口加入一条聊天
	function addItem(title, content) {
		$('.chat-board').append(`<div class="chat-item"><b>${title}：</b>${content}</div>`)
	}

  // 在页面载入时通知后台这台设备开始接受推送
  function startRead () {
    $.ajax({
      url: api.read,
      data: {
        switchId: switchId
      }
    })
  }

  // 在页面卸载时通知后台这台设备结束接受推送
  function stopRead () {
    $.ajax({
      url: api.stop,
      data: {
        switchId: switchId
      }
    })
  }

  // 开始socket的连接
  stompClient.connect({}, function (frame) {
    stompClient.subscribe(api.queue, function(msg) {
      addItem('接收', JSON.parse(msg))
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
    event.preventDefault()
    var msg = $('.send-content').val()
    if(event.keyCode == 13) {
      if(msg) {
        $.ajax({
          url: api.send,
          type: 'POST',
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

  $('.return').click(function (e) {
    location.href = "/dongjun/high_voltage_switch_manager"
  })

  $('.return-font').click(function (e) {
    location.href = "/dongjun/high_voltage_switch_manager"
  })

  $('#sendAll').click(function (e) {
    console.log('总召')
  })

  // 加载好页面后告诉后台开始订阅
  $(window).load(function () {
    startRead()
    showTitle()
  })

  window.onbeforeunload = function() {
    stopRead()
  }
})()
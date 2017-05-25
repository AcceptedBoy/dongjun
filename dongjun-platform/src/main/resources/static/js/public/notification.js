/**
 * 寄生式继承 继承原型
 * 处理的不是对象，而是类的原型
 * @param subClass      子类
 * @param superClass    父类
 **/
function inheritPrototype(subClass, superClass) {
  // 声明一个过渡函数对象
  function F() {}
  // 过渡对象的原型继承父对象
  F.prototype = superClass.prototype
  // 返回过渡对象的一个实例， 该实例的原型继承了父对象
  // 复制一份父类的原型副本保存在p中
  var p = new F()
  // 修正因为重写子类原型导致子类的constructor属性被修改
  p.constructor = subClass
  // 设置子类的原型
  subClass.prototype = p
}

var NotifyBase = function(data) {
  this.el = document.createElement('div')
  this.el.className = 'notification_full_item '
  this.data = data && data.data || null
  this.setting = data
  this.id = data && data.id || ''
  // this.init()
}

NotifyBase.prototype = {
  constructor: NotifyBase,
  init: function() {
    throw new Error('重写init')
  },
  getEl: function() {
    return this.el
  },
  bindEvent: function() {
    throw new Error('重写init')
  },
  remove: function() {
    var item = this.el
    item.className += ' remove'
    setTimeout(function(){
      item.parentNode.removeChild(item)
    }, 600)
    return this
  }
}

NotifyBase.getTime = function() {
  var date = new Date()
  var h = date.getHours()
  h = h < 10 ? ('0' + h) : h
  var m = date.getMinutes()
  m = m < 10 ? ('0' + m) : m
  return h + ':' + m
}

/**
 * 普通通知
 * @param {Object} data 配置信息
 *                {
 *                  data: {},                      // 消息
 *                  type: 'danger'                 // '', 'danger', 'success'
 *                  simple： {                     // 内容匹配
 *                    title: 'name',               // 标题
 *                    description: 'hitchReason'   // 内容描述                      
 *                  },
 *                  clickFnc: function(data, Notify) {     // 点击的回调函数， 返回消息data
 *                  },
 *                  remove: function(data, Notify){}       // 删除                   
 *                }
 */
var Notify = function(data) {
  NotifyBase.call(this, data)
  this.el.className += ('base ' + (data.type || ''))
  this.clickFnc = data.fnc || ''
  this.simpleLabel = data.simple || { title: 'title', description: 'description'}
  this.closeBtn = document.createElement('div')
  this.closeBtn.className = 'notification_full_item_close'
  this.closeBtn.innerHTML = data.closeText || '×'
  this.contentNode = null
  this.init()
}

inheritPrototype(Notify, NotifyBase)
Notify.prototype.init = function() {
  console.log(this)
  var title = this.data[this.simpleLabel.title]
  var description = this.data[this.simpleLabel.description]
  var curTime = NotifyBase.getTime()
  this.contentNode = document.createElement('div')
  this.contentNode.className = 'notification_full_item_content'
  var domStr =  '<div class="content_title"> '+ title +' </div>' +
                '<div class="content_time"> '+ curTime +'</div>' +
                '<div class="content_description"> '+ description +' </div>'
  this.contentNode.innerHTML = domStr
  this.el.appendChild(this.contentNode)
  this.el.appendChild(this.closeBtn)
  this.bindEvent()
}

Notify.prototype.bindEvent = function() {
  var me = this
  me.closeBtn.onclick = me.remove.bind(me)
  if(me.moreUrl) {
    me.contentNode.onclick = function() {
      me.clickFnc(me.data, me)
    }
  }
}

Notify.prototype.remove = function() {
  NotifyBase.prototype.remove.call(this)
  if(this.setting.remove) {
    this.setting.remove(this.data, this)
  }
  return this
}

/**
 * 可查看详情通知
 * @param {Object} data 配置信息
 *                {
 *                  data: {                        // 消息
 *                    name: '名称',
 *                    hhhh: '哈哈',
 *                    hitchReason: '= ='
 *                  },    
 *                  type: 'danger'                 // '', 'danger', 'success'                  
 *                  simple： {                     // 内容匹配
 *                    title: 'name',               // 标题  
 *                    description: 'hitchReason'   // 内容描述                      
 *                  },
 *                  detail: {                      // 详情内容匹配
 *                    name: '匹配data里的名称',
 *                    hhhh: '不要哈哈'
 *                  },
 *                  remove: function(data, DetailNotify){}
 *                  icon: '!',                     // icon (可用img标签) 
 *                  enterText: '点击查看详情'       // 查看详情字段， 默认‘点击查看详情’            
 *                }
 */
var DetailNotify = function(data) {
  this.status = ''  // 'detail'
  this.detailLabel = data.detail
  this.__hasLoadDetail = false
  // 继承Notify
  Notify.call(this, data)
  this.el.className = this.el.className.replace(/\sbase/, '')
  // this.init()
}

// 原型 继承于 NotifyBase
inheritPrototype(DetailNotify, NotifyBase);

DetailNotify.prototype.init = function() {
  console.log('detail init: ')
  // 继承Notify的init
  Notify.prototype.init.call(this)
  // icon
  var iconNode = document.createElement('div')
  iconNode.className = 'notification_full_item_icon'
  iconNode.innerHTML = this.setting.icon
  this.el.insertBefore(iconNode, this.el.firstChild)
  // enter notice
  var enterNode = document.createElement('div')
  enterNode.className = 'content_enter'
  enterNode.innerHTML = this.setting.enterText || '点击查看详情'
  this.contentNode.appendChild(enterNode)
  // console.log('detail init: ' , this.contentNode)
}

DetailNotify.prototype.detail = function() {
  var node = this.el
  if(this.status == 'detail') {
    // 当前为详情节面
    // 更改状态
    this.status = ''
    // 删除detail
    node.className = node.className.replace(/\sdetail$/, '')
  } else {
    // 是否已经插入detail的dom元素
    if(!this.__hasLoadDetail) {
      node.appendChild(this.__createDetail())
      this.__hasLoadDetail = true
    }
    node.className += ' detail'
    this.status = 'detail'
  }
}

DetailNotify.prototype.__createDetail = function() {
  var detailContainer = document.createElement('div')
  detailContainer.className = 'notification_full_item_detailContainer'
  var domStr = ''
  var label, 
      content,
      detailLabel = this.detailLabel,
      data = this.data
  for(var d in detailLabel) {
    if(typeof data[d] === 'undefined') continue
    label = detailLabel[d].label || detailLabel[d]
    content = detailLabel[d].render ? detailLabel[d].render(data[d]) : data[d]
    domStr += '<div class="notification_full_item_detailRow">'+
                '<div class="detailRow_label">' + label + '</div>'+
                '<div class="detailRow_content">' + content + '</div>'+
              '</div>'
  }
  detailContainer.innerHTML = domStr
  return detailContainer
}

DetailNotify.prototype.bindEvent = function() {
  var me = this
  me.closeBtn.onclick = function(e) {
    var ev = e || window.event
    ev.stopPropagation()
    me.remove()
  }
  me.el.onclick = me.detail.bind(me)
}

DetailNotify.prototype.remove = function() {
  return Notify.prototype.remove.call(this)
}

var BtnNotify = function(data) {
  NotifyBase.call(this, data)
  this.el.className += ' more'
  this.el.innerHTML = data.text
  this.el.onclick = data.action
}
inheritPrototype(BtnNotify, NotifyBase)

var PlaceHoldNotify = function(data) {
  NotifyBase.call(this, data)
  this.el.className += ' placehold'
}
inheritPrototype(PlaceHoldNotify, NotifyBase)

var Notification = function() {
  // 储存每个通知标签
  var store = {
    notify: {},
    maxIndex: 0, // 当前notify数组中最大的下标
    length: 0,
    blanks: [],
    ignore: [],
    done: [],
    hasLoadDetail: []
  } 
  // 相关dom
  var doms = {
    root: null,
    wrapper: null,
    closeBtn: null,
    cover: null
  }
  // 限制显示通知数，超过限制显示btn
  var limit = {
    num: 100,
    text: '查看更多',
    href: '#',
    action: null
  }
  var status = {
    isHiding: true,
    isFlow: false
  }

  var icon = {
    danger: '!',
    success: 'O',
    normal: '·'
  }
  return {
    start: function(setting) {
      if(setting.limit) {
        for(var k in setting.limit) {
          limit[k] = setting.limit[k]
        }
      }
      this.__setUp().__listen()
    },
    getStore: function() {
      return store
    },
    __setUp: function() {
      // root
      var nFull = document.createElement('div')
      nFull.id = 'notification_full'
      nFull.className = 'hide'
      doms.root = nFull

      // container
      var container = document.createElement('div')
      container.className = 'notification_full_container'

      // cover
      var cover = document.createElement('div')
      cover.className = 'notification_full_cover hide'
      doms.cover = cover
      container.appendChild(cover)

      // wrapper
      var wrapper = document.createElement('div')
      wrapper.id = 'notification_full_wrapper'
      wrapper.className = 'notification_full_wrapper'
      // 增加最大高度以可滑动
      wrapper.style.maxHeight = document.body.clientHeight + 'px'
      doms.wrapper = wrapper
      container.appendChild(wrapper)

      // closeBtn
      var closeBtn = document.createElement('div')
      closeBtn.className = 'notification_full_close'
      closeBtn.innerHTML = '×'
      doms.closeBtn = closeBtn
      container.appendChild(closeBtn)

      nFull.appendChild(container)

      // insert
      document.body.appendChild(nFull)
      return this
    },
    __listen: function() {
      doms.closeBtn.onclick = this.clearNotify.bind(this)
    },
    clearNotify: function() {
      this.hide()
      setTimeout(function(){
        doms.wrapper.innerHTML = ''
        this.__resetStore()
      }.bind(this), 500)
    },
    /**
     * show
     * @return {[type]} [description]
     */
    show: function() {
      status.isHiding = false
      doms.root.className = 'show'
    },
    hide: function() {
      status.isHiding = true
      doms.root.className = 'hide'
    },
    showCover: function() {
      doms.cover.className = doms.cover.className.replace(/\shide$/, '')
    },
    hideCover: function() {
      doms.cover.className += ' hide'
    },
    __resetStore: function() {
      store.maxIndex = 0
      store.length = 0
      store.notify = []
      limit.__hasFlow = false
    },
    __createLimitBtn: function() {
      var docfrag = document.createDocumentFragment()
      var btn = new BtnNotify({
        text: limit.text,
        action: limit.action
      })
      docfrag.appendChild(btn.getEl())
      // var placehold = new PlaceHoldNotify()
      // docfrag.appendChild(placehold.getEl())
      return docfrag
    },
    /**
     * 创建通知
     * @param  {string} type 通知类型
     * @param  {object} data 对应通知所需数据
     * @return {HTMLElement}      通知对应Dom
     */
    __createNotify: function(type, data) {
      var notify = null
      var me = this
      // 增加删除回调函数
      function afterRemove(data, n) {
        delete store.notify[n.id]
        if((--store.length) === 0) {
          me.hide()
        }
      }
      if(data.remove) {
        var tempFn = data.remove
        data.remove = function(data, n) {
          tempFn(data, n)
          afterRemove(data, n)
        }
      } else {
        data.remove = function(data, n) {
          afterRemove(data, n)
        }
      }

      // 通知的id
      var nId = 'N_' + (store.maxIndex++)
      data.id = nId

      // 通知类型
      switch(type) {
        case 'detail': 
          data.icon = data.icon || icon[data.type] || icon['normal']
          notify = new DetailNotify(data)
          break
        case 'simple':
          notify = new Notify(data)
          break
      }

      // 储存
      store.notify[nId] = notify
      store.length++
      return notify.getEl()
    },
    /**
     * 发出带详情的通知
     * @param  {object or Array} data         DetailNotify的参数格式
     * @return {[DetailNotify]}               DetailNotify对象
     */
    notify: function(type, data) {
      var docfrag = document.createDocumentFragment()
      if(store.length >= limit.num) {
        if(status.isFlow) {
          return
        }
        status.isFlow = true
        docfrag.appendChild(this.__createLimitBtn())
      } else {
        var dataType = Object.prototype.toString.call(data)
        if(dataType === '[object Object]') {
          docfrag.appendChild(this.__createNotify(type, data))
        } else if(dataType === '[object Array]') {
          // 数组形式
          var len = limit.num - store.length
          if(len < data.length) {             // 超过限制条数
            data = data.slice(0, len)         // 去除超过限制的通知
            status.isFlow = true              // 已经超出
          }
          data.forEach(function(item) {
            docfrag.appendChild(this.__createNotify(type, data))
          }.bind(this))

          if(status.isFlow){                  // 超出限制
            docfrag.appendChild(this.__createLimitBtn())
          }
        } else {
          console.warn('仅支持数组和单个对象形式')
          return
        }
      }
      // doms.wrapper.appendChild(docfrag)
      doms.wrapper.insertBefore(docfrag, doms.wrapper.firstChild)
      if(status.isHiding) {
        this.show()
      }
    }
  }
}()

/* 
  Notification.start({
    // 设置限制条数10，超过则跳转
    limit: {
      num: 10,
      action: function() {
        location.pathname = 'templates/event/index.html'
      }
    }
  })
*/

/*
Notification.notify('detail', {
    simple: {
        title: 'name',
        description: 'hitchReason'
    },
    // 配置详情通知的显示内容
    detail: {
        name: '<script>eval(alert("handsome boy"))</script>',
        type: '设备类型',
        hitchReason: '报警原因',
        tag: {
            label: '传感器',
            render: function(data) {
                return data + ' 号'
            }
        },
        value: '报警值',
        maxHitchValue: '温度上限',
        minHitchValue: '温度下限'
    },
  data: data,
  type: 'success'
})
*/
;(function () {
  if (window.Cascader) {
    return
  }

  var Queue = function () {
    this.current = 0
    this.length = 0
  }

  Queue.prototype.push = function (msg) {
    Array.prototype.push.call(this, msg)
  }

  Queue.prototype.run = function (msg) {
    var a = Array.prototype.slice.call(arguments)
    var index = a.shift()
    // console.log('==================================')
    // console.log('run  ', index)
    // console.log('params', a)
    if (index > this.length - 1) {
      // console.log('over')
      return
    }
    this.current = index
    var cb = this.run.bind(this, ++this.current)
    a.push(cb)
    // console.log(a)
    this[index].apply(null, a)
  }

  Queue.prototype.clear = function () {
    this.length = 0
  }

  Queue.prototype.next = function (msg) {
    this[++this.current].apply(null, arguments)
  }

  function Cascader (opts) {
    this.id = opts.id || ''

    var queue = new Queue()
    
    this.Model = {
      opts: opts,
      container: opts.node,
      items: opts.items || [],
      nodes: [],
      load: function (index, value, cb) {
        var node = this.nodes[index]
        if (node.param && !value) {
          node.data = []
          node.value = undefined
          cb([])
          return
        }
        var data = {}
        data[node.param] = value
        $.ajax({
          type: 'GET',
          data: data,
          url: node.url,
          success: function (res) {
            var data = node.dataSrc ? res[node.dataSrc] : res
            node.data = data
            node.value = data[0] ? data[0].id : undefined
            cb(data)
            // cb(data)
          }, 
          error: function (res) {
            alert('请求失败')
          }
        }) 
      }
    }
    var Model = this.Model
    this.View = {
      M: Model,
      init: function () {
        var M = this.M
        var frag = document.createDocumentFragment()
        M.items.forEach(function(item, index) {
          var selDiv = document.createElement('div')
          selDiv.id = 'cascader-' + index
          selDiv.className = 'form-group ' + (item.class || 'span3')

          var selectId = item.id || ('select-' + index)
          var label = document.createElement('label')
          label.className = 'span3'
          label.setAttribute('for', selectId)
          label.innerHTML = item.label + ': '
          selDiv.appendChild(label)
          
          var select = document.createElement('select')
          select.className = 'span9 groups'
          select.id = selectId
          M.nodes.push({
            id: selectId,
            label: item.label,
            node: select,
            url: item.url,
            value: '',
            fnc: item.cb || function () {},
            param: item.param,
            dataSrc: item.dataSrc || ''
          })
          selDiv.appendChild(select)

          frag.appendChild(selDiv)
        })
        M.container.className += M.opts.inline ? ' form-inline' : ''
        M.container.appendChild(frag)
      },
      fill: function (node, data) {
        // console.log('fill !!!!!!!! ', node, data)
        var opts = ''
        if(!data || data.length == 0) {
          opts = '<option>当前没有数据</option>'
        } else {
          data.forEach(function(item, index) {
            opts += '<option value="' + item.id + '">' + item.name + '</option>'
          })
        }
        node.innerHTML = opts
      }
    }
    var View = this.View
    this.Ctrl = {
      M: Model,
      V: View,
      init: function () {
        this.V.init()
        this.__listen()
      },
      __listen: function () {
        var M = this.M,
            V = this.V
        M.nodes.forEach(function(select, index) {
          select.index = index

          // 每一个select都有的load方法
          select.load = function (select) {
            return function (value, cb) {
              if (typeof value === 'function' && !cb) {
                cb = value
                value = undefined
              }
              // var data = {}
              // data[select.param] = value
              // 获取数据
              M.load(select.index, value, function (optionData) {
                V.fill(select.node, optionData)
                var id = optionData[0] ? optionData[0].id : undefined
                if (select.fnc) {
                  // 自定义的回调函数
                  select.fnc(id, optionData)
                }
                if (cb) {
                  // 用于队列的下一个执行
                  cb(id)
                }
              })
            }
          }(select)

          // 推入事件队列
          queue.push(select.load)
          // select 事件监听
          select.node.onchange = function (e) {
            return function (select, value) {
              select.value = value
              queue.run((select.index + 1), value)
              select.fnc(value, select.data)
            }(select, this.value)
          }
        })

        this.run(0)
      },
      run: function (index, data) {
        if (!data && index > 0) {
          data = this.M.nodes[index-1].value
        }
        queue.run(index, data)
      }
    }

    this.init()
  }
  
  Cascader.prototype.init = function () {
    this.Ctrl.init()
  }

  Cascader.prototype.getValue = function (index) {
    return this.Model.nodes[index].value
  }

  Cascader.prototype.getNodes = function () {
    return this.Model.nodes
  }

  Cascader.prototype.run = function (index, data) {
    this.Ctrl.run(index, data)
  }
  
  window.Cascader = Cascader
}())

/*

  var cas = new Cascader({
    id: 'device',
    node: document.querySelector('#group_company_select'),
    items: [{
      url: api.group,
      label: '大组',
      dataSrc: 'data',
      class: 'span4',
      cb () {

      }
    }, {
      url: api.company,
      label: '公司',
      dataSrc: 'text',
      class: 'span4',
      param: 'groupId'
    }, {
      url: api.device_group,
      label: '设备',
      param: 'companyId',
      dataSrc: 'text',
      class: 'span4',
      cb (value) {
        console.log('设备请求完啦~', value)
      }
    }]
  })

 */


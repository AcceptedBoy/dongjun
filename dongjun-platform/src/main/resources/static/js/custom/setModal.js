/**
 * usage:
 *       创建出来之后建议用封装的在modal上的show方法和hide方法。
 *
 * var modal = $.modal({
 *  id: 'addModal',
 *  dom: '#app',
 *  title: '哈哈',
 *  formId: 'form',
 *  bodyArr: [{
 *   inputId: '123',        表单的id（可选）
 *   val: $(selector),      预设值（可选，传入dom节点的jq选择）
 *   allowBlank: true,      是否设置为可选填框（可选，默认为false）
 *   inputType: 'text',
 *   inputName: '123',
 *   label: '我去',
 *   reg: function(val, err) {   val是表单内容的值，return true表示通过，return false表示不通过(可选)
 *    if(/\.js$/.test(val)) {
 *      return true
 *    } else {
 *      err('请输入xxoo')        err回调函数，接受一个字符串，作为错误提示
 *      return false
 *    }
 *   }
 * }, {
 *   inputId: '2323',
 *   inputType: 'textarea',
 *   inputName: '2323',
 *   label: '我去',
 *   reg: function(val, err) {
 *
 *   }
 * }],
 * completeFn: function(e, val) {
 *   console.log(this)
 *   console.log(e)
 *   console.log(val)
 * },
 * completeText: '添加',
 * completeId: 'comfirm'
 * })
 */

(function() {
  function Modal(option) {
    if(this instanceof Modal) {
      this.eventId = option.completeId
      this.id = option.id || ''
      this.formId = option.formId || ''
      this.dom = option.dom || '#modal'
      this.completeFn = option.completeFn || function() {}
      this.header = new ModalHeader(option.title)
      this.body = new ModalBody(option.bodyArr, option.formId)
      this.footer = new ModalFooter(option.completeText, option.completeId)
      this.renderFn = []
      this.init()
    } else {
      return new Modal(option)
    }
  }

  Modal.prototype.init = function() {
    var self = this
    var body = this.body.init()
    this.renderFn = body.render
    var html = '<div id="'+ this.id +'" class="modal hide fade" tabindex="1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
    html += this.header.init() + body.html + this.footer.init() + '</div>'
    $(this.dom).append(html)

    $('#' + this.id).find('.input').focus(function(e) {
      $(this).attr('style', '').nextAll('.warnTips').text('')
    })

    /**
    * 给确定按钮添加事件
    */
    $('#' + this.eventId).on('click', function(e) {
      var isOk = true
      body.reg.forEach(function(fn) {
        if(!fn()) {
          isOk = false
        }
      })
      if(isOk) {
        var formVal = {}
        self.dealVal(formVal)
        self.completeFn.call(this, e, formVal)
        self.hide()
      }
    })

    return this
  }

  Modal.prototype.dealVal = function(obj) {
    var arr = $('#' + this.formId).find('.input')
    for(var i = 0; i < arr.length; i++) {
      if(arr[i].value) {
        obj[arr[i].name] = arr[i].value
      }
    }
  }

  Modal.prototype.show = function() {
    if(this.id) {
      var fns = this.renderFn
      for(var i = 0; i < fns.length; i++) {
        fns[i]()
      }
      $('#' + this.id).modal('show')
    }
  }

  Modal.prototype.hide = function() {
    if(this.id) {
      $('#' + this.id).modal('hide')
    }
  }

  function ModalHeader(title) {
    this.title = title
  }

  ModalHeader.prototype.init = function() {
    return '<div class="modal-header">'+
        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'+
        '<h3>' + this.title + '</h3>'+
    '</div>'
  }

  function ModalBody(dataArr, formId) {
    this.data = dataArr || []
    this.id = formId || ''
  }

  ModalBody.prototype.init = function() {
    var RegArr = []
    var valArr = []
    var html = '<div class="modal-body"><div id="'+ this.id +'" class="form-horizontal">'
    var length = this.data.length
    for(var i = 0; i < length; i++) {
      var inputData = new Input({
        formId: this.id,
        type: this.data[i].inputType,
        name: this.data[i].inputName,
        id: this.data[i].inputId,
        reg: this.data[i].reg,
        allowBlank: this.data[i].allowBlank,
        val: this.data[i].val
      })
      var data = inputData.init()
      html += '<div class="control-group">' +
          '<label class="control-label" for="'+ this.data[i].inputId +'">' + this.data[i].label + '</label>' +
          '<div class="controls">' +
              data.html +
          '</div>' +
      '</div>'
      RegArr.push(inputData.getReg())
      valArr.push(data.render)
    }
    html += '</div></div>'
    return {
      html: html,
      reg: RegArr,
      render: valArr
    }
  }

  function ModalFooter(text, completeId) {
    this.text = text || '确定'
    this.id = completeId || ''
  }

  ModalFooter.prototype.init = function() {
    return '<div class="modal-footer">' +
        '<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>' +
        '<button id="'+ this.id +'" class="btn btn-primary" aria-hidden="true">'+ this.text +'</button>' +
    '</div>'
  }

  function Input(option) {
    this.formId = option.formId
    this.type = option.type || 'text'
    this.name = option.name || ''
    this.id = option.id || ''
    this.reg = option.reg || function() { return true }
    this.me = ''
    this.allowBlank = option.allowBlank || false
    this.val = option.val || {
      text: function() {
        return ''
      }
    }
  }

  Input.prototype.chooseType = {
    text: function() {
      return {
        html: '<input class="input" type="text" name="'+ this.name +'" id="'+ this.id +'" />' + this.chooseType.warnTips,
        me: 'input'
      }
    },
    textarea: function() {
      return {
        html: '<textarea class="input" id="'+ this.id +'" name="'+ this.name +'" rows="3" cols="20" />' + this.chooseType.warnTips,
        me: 'textarea'
      }
    },
    // otherTypes
    //

    warnTips: '<span style="margin-left: 10px;" class="warnTips"></span>'
  }

  Input.prototype.getReg = function() {
    var self = this
    if(self.allowBlank) {
      return function() {
        if($('#' + self.formId).find(self.me + '[name='+ self.name +']').val()) {
          return self.reg.call(self, $('#' + self.formId).find(self.me + '[name='+ self.name +']').val(), self.error.bind(self))
        } else {
          return true
        }
      }
    } else {
      return function() {
        return self.reg.call(self, $('#' + self.formId).find(self.me + '[name='+ self.name +']').val(), self.error.bind(self))
      }
    }
  }

  Input.prototype.renderVal = function() {
    var self = this
    var value = this.val
    var test = Object.prototype.toString
    if(test.call(value) == '[object String]') {
      return function() {
        $('#' + self.formId).find(self.me + '[name='+ self.name +']').val(value).attr('style', '').nextAll('.warnTips').text('')
      }
    } else {
      return function() {
        $('#' + self.formId).find(self.me + '[name='+ self.name +']').val(value.text()).attr('style', '').nextAll('.warnTips').text('')
      }
    }
  }

  Input.prototype.error = function(text) {
    $('#' + this.formId).find(this.me + '[name='+ this.name +']').css({borderColor: '#f3b6b6'}).nextAll('.warnTips').text(text).css({color: 'red'})
  }

  Input.prototype.init = function() {
    var initData = this.chooseType[this.type].bind(this)()
    this.me = initData.me
    return {
      html: initData.html,
      render: this.renderVal()
    }
  }

  $.modal = Modal
}())

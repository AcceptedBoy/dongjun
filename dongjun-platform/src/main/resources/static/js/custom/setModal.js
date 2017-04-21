/**
 * usage:
 *
 * var modal = $.modal({
 *  id: 'addModal',
 *  dom: '#app',
 *  title: '哈哈',
 *  formId: 'form',
 *  bodyArr: [{
 *   inputId: '123',
 *   inputType: 'text',
 *   inputName: '123',
 *   label: '我去',
 *   reg: function(val, err) {   val是表单内容的值，return true表示通过，return false表示不通过
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
 * completeFn: function(e) {
 *   console.log(this)
 *   console.log(e)
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
      this.dom = option.dom || '#modal'
      this.completeFn = option.completeFn || function() {}
      this.header = new ModalHeader(option.title)
      this.body = new ModalBody(option.bodyArr, option.formId)
      this.footer = new ModalFooter(option.completeText, option.completeId)
    } else {
      return new Modal(option)
    }
  }

  Modal.prototype.init = function() {
    var self = this
    var body = this.body.init()
    var html = '<div id="'+ this.id +'" class="modal hide fade" tabindex="1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
    html += this.header.init() + body.html + this.footer.init() + '</div>'
    $(this.dom).append(html)

    $('#' + this.id).find('.input').focus(function(e) {
      $(this).attr('style', '')
      $(this).nextAll('.warnTips').text('')
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
        self.completeFn.call(this, e)
      }
    })

    return this
  }

  Modal.prototype.show = function() {
    if(this.id) {
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
    var html = '<div class="modal-body"><div id="'+ this.id +'" class="form-horizontal">'
    var length = this.data.length
    for(var i = 0; i < length; i++) {
      var inputData = new Input({
        type: this.data[i].inputType,
        name: this.data[i].inputName,
        id: this.data[i].inputId,
        reg: this.data[i].reg,
      })
      html += '<div class="control-group">' +
          '<label class="control-label" for="'+ this.data[i].inputId +'">' + this.data[i].label + '</label>' +
          '<div class="controls">' +
              inputData.init() +
          '</div>' +
      '</div>'
      RegArr.push(inputData.getReg())
    }
    html += '</div></div>'
    return {
      html: html,
      reg: RegArr
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
    this.type = option.type || 'text'
    this.name = option.name || ''
    this.id = option.id || ''
    this.reg = option.reg || function() { return true }
  }

  Input.prototype.chooseType = {
    text: function() {
      return '<input class="input" type="text" name="'+ this.name +'" id="'+ this.id +'" />' + this.chooseType.warnTips
    },
    textarea: function() {
      return '<textarea class="input" id="'+ this.id +'" name="'+ this.name +'" rows="3" cols="20" />' + this.chooseType.warnTips
    },
    // otherTypes
    //

    warnTips: '<span style="margin-left: 10px;" class="warnTips"></span>'
  }

  Input.prototype.getReg = function() {
    var self = this
    if(this.reg) {
      return function() {
        return self.reg.call(self, $('#' + self.id).val(), self.error.bind(self))
      }
    }
  }

  Input.prototype.error = function(text) {
    $('#' + this.id).css({borderColor: '#f3b6b6'})
    $('#' + this.id).nextAll('.warnTips').text(text).css({color: 'red', fontSize: '1.1em'})
  }

  Input.prototype.init = function() {
    return this.chooseType[this.type].bind(this)()
  }

  $.modal = Modal
}())

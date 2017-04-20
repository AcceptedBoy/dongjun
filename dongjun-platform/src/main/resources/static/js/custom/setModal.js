(function() {
  function Modal(option) {
    if(this instanceof Modal) {
      this.id = option.id || ''
      this.dom = option.dom || '#modal'
      this.header = new ModalHeader(option.title)
      this.body = new ModalBody(option.bodyArr, option.formId)
      this.footer = new ModalFooter(option.completeFn, option.completeText, option.completeCtx)
    } else {
      return new Modal(option)
    }
  }

  Modal.prototype.init = function() {
    var html = '<div id="'+ this.id +'" class="modal hide fade" tabindex="1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
    html += this.header.init() + this.body.init() + this.footer.init() + '</div>'
    $(this.dom).html(html)
  }

  function ModalHeader(title) {
    this.title = title
  }

  ModalHeader.prototype.init = function() {
    return '<div class="modal-header">'+
        '<button type="button" class="close" data-dismiss="modal"aria-hidden="true">×</button>'+
        '<h3>' + this.title + '</h3>'+
    '</div>'
  }

  function ModalBody(dataArr, formId) {
    this.data = dataArr
    this.id = formId
  }

  ModalBody.prototype.init = function() {
    var html = '<div class="modal-body"><div id="'+ this.data.id +'" class="form-horizontal">'
    var length = this.data.length
    for(var i = 0; i < length; i++) {
      if(this.data[i].inputType == 'textarea') {
        html += '<div class="control-group">' +
            '<label class="control-label" for="'+ this.data[i].inputId +'">' + this.data[i].label + '</label>' +
            '<div class="controls">' +
                '<textarea id="'+ this.data[i].inputId +'" name="'+ this.data[i].inputName +'" rows="3" cols="20" />' +
            '</div>' +
        '</div>'
      } else {
        html += '<div class="control-group">' +
            '<label class="control-label" for="'+ this.data[i].inputId +'">' + this.data[i].label + '</label>' +
            '<div class="controls">' +
                '<input type="'+ this.data[i].inputType +'" name="'+ this.data[i].inputName +'" id="'+ this.data[i].inputId +'" />' +
            '</div>' +
        '</div>'
      }
    }
    html += '</div></div>'
    return html
  }

  function ModalFooter(fn, text, ctx) {
    this.fn = fn
    this.text = text
    this.ctx = ctx
  }

  ModalFooter.prototype.init = function() {
    return '<div class="modal-footer">' +
        '<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>' +
        '<button id="add_switch_confirm_btn" class="btn btn-primary" aria-hidden="true" onclick="' + this.fn.bind(this.ctx) + '">'+ this.text +'</button>' +
    '</div>'
  }

  $.modal = Modal
}())

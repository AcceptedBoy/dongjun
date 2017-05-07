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

var Popup = function() {
	var confirm = null,
			alert = null
	return {
		Confirm: function(data) {
			if(!confirm) {
				confirm = new Confirm(data)
				return confirm
			} else {
				return confirm.reset(data).bindEvent().show()
			}
		},
		Alert: function(data) {
			if(!alert) {
				alert = new Alert(data)
				return alert
			} else {
				return alert.reset(data).bindEvent().show()
			}
		}
	}
}()
var PopupBase = function() {
	this.panel = null
	this.closeBtn = null
}

PopupBase.prototype = {
	constructor: PopupBase,
	init: function() {
		throw new Error('需重写init')
	},
	show: function() {
		throw new Error('重写show')
	},
	hide: function() {
		throw new Error('需重写hide')
	},
	destroy: function() {
		this.hide()
		setTimeout(function() {
			this.panel.parentNode.removeChild(this.panel)
		}.bind(this), 1000)
	},
	bindEvent: function() {
		throw new Error('重写bindEvent')
	},
	getPanel: function() {
		return this.panel
	}
}

/**
 * Alert
 * @param {object} data alert内容
 *                      {
 *                        title: '标题',
 *                        text: '内容',
 *                        successText: '确认按钮的字段',
 *                        fail(data, Alert) {
 *                        	// 关闭/取消时执行的函数                        
 *                        },
 *                        success(data, Alert) {
 *                        	// 确认执行的函数                        
 *                        }                       
 *                      }
 */
var Alert = function(data) {
	PopupBase.call(this)
	this.data = data
	this.header = null
	this.body = null
	this.footer = null
	this.panel = document.createElement('div')
	this.titleNode = document.createElement('h3')
	this.textNode = document.createElement('p')
	this.closeBtn = document.createElement('button')
	this.successBtn = document.createElement('button')
	this.successText = data.successText || '确认'
	this.fail = data.fail
	this.success = data.success
	this.init()
}

inheritPrototype(Alert, PopupBase)
Alert.prototype.init = function() {
	this.panel.className = 'modal hide fade'
	this.panel.setAttribute('tabindex', '-1')
	this.panel.setAttribute('role', 'dialog')
	this.panel.setAttribute('aria-labelledby', 'myModalLabel')
	this.panel.setAttribute('aria-hidden', 'true')
	// header
	this.panel.appendChild(this.__Header())
	// body
	this.panel.appendChild(this.__Body())
	// footer
	this.panel.appendChild(this.__Footer())

	this.bindEvent()
	document.body.appendChild(this.panel)
	this.show()
}

Alert.prototype.__Header = function() {
	var header = document.createElement('div')
	header.className = 'modal-header'
	this.closeBtn.className = 'close'
	this.closeBtn.dataset.dismiss = 'modal'
	this.closeBtn.setAttribute('aria-hidden', 'true')
	this.closeBtn.innerHTML = '×'
	header.appendChild(this.closeBtn)
	this.titleNode.innerHTML = this.data.title || '提示' 
	header.appendChild(this.titleNode)
	this.header = header
	return header
}

Alert.prototype.__Body = function() {
	var body = document.createElement('div')
	body.className = 'modal-body'
	this.textNode.innerHTML = this.data.text
	body.appendChild(this.textNode)
	this.body = body
	return body
}

Alert.prototype.__Footer = function() {
	var footer = document.createElement('div')
	footer.className = 'modal-footer'

	this.successBtn.innerHTML = this.successText || '确定'
	this.successBtn.className = 'btn btn-danger'
	this.successBtn.dataset.dismiss = 'modal'
	this.successBtn.setAttribute('aria-hidden', 'true')
	footer.appendChild(this.successBtn)
	this.footer = footer
	return footer
}

Alert.prototype.bindEvent = function() {
	this.closeBtn.onclick = this.fail
	this.successBtn.onclick = this.success
	return this
}

Alert.prototype.show = function() {
	$(this.panel).modal('show')
	return this
}

Alert.prototype.hide = function(data) {
	$(this.panel).modal('hide')
	return this
}

Alert.prototype.reset = function(data) {
	this.title = data.title
	this.text = data.text
	this.fail = data.fail
	this.successText = data.successText || '确认'
	this.titleNode.innerHTML = this.title
	this.textNode.innerHTML = this.text
	this.successBtn.innerHTML = this.successText
	return this
}

var Confirm = function(data) {
	this.cancelBtn = document.createElement('button')
	this.cancelText = data.cancelText || '取消'
	Alert.call(this, data)
}

inheritPrototype(Confirm, Alert)

Confirm.prototype.init = function() {
	Alert.prototype.init.call(this)
}

Confirm.prototype.__Footer = function() {
	Alert.prototype.__Footer.call(this)
	this.cancelBtn.innerHTML = this.cancelText
	this.cancelBtn.className = 'btn'
	this.cancelBtn.dataset.dismiss = 'modal'
	this.cancelBtn.setAttribute('aria-hidden', 'true')
	this.footer.appendChild(this.cancelBtn)
	return this.footer
}

Confirm.prototype.bindEvent = function() {
	this.closeBtn.onclick = this.fail
	this.cancelBtn.onclick = this.fail
	this.successBtn.onclick = this.success
	return this
}

Confirm.prototype.reset = function(data) {
	Alert.prototype.reset.call(this, data)
	this.cancelText = data.cancelText || '取消'
	this.cancelBtn.innerHTML = this.cancelText
	return this
}
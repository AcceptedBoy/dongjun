package com.gdut.dongjun.service.cxf.getSwitchGPRS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gdut.dongjun.service.cxf.po.SwitchGPRS;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSwitchGPRSResponse", propOrder = {
    "_return"
})
public class GetSwitchGPRSResponse {

    @XmlElement(name = "return")
    protected SwitchGPRS _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public SwitchGPRS getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturn(SwitchGPRS value) {
        this._return = value;
    }

}


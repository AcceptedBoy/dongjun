package com.gdut.dongjun.service.cxf.getCtxInstance;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gdut.dongjun.service.cxf.po.SwitchGPRS;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCtxInstanceResponse", propOrder = {
    "_return"
})
public class GetCtxInstanceResponse {

    @XmlElement(name = "return")
    protected List<SwitchGPRS> _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public List<SwitchGPRS> getReturn() {
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
    public void setReturn(List<SwitchGPRS> value) {
        this._return = value;
    }

}

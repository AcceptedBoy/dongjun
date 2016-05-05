package com.gdut.dongjun.service.cxf.getStatusbyId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.gdut.dongjun.service.cxf.po.HighVoltageStatus;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStatusbyIdResponse", propOrder = {
    "_return"
})
public class GetStatusbyIdResponse {

    @XmlElement(name = "return")
    protected HighVoltageStatus _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public HighVoltageStatus getReturn() {
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
    public void setReturn(HighVoltageStatus value) {
        this._return = value;
    }

}

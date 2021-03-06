package io.renren.modules.logistics.util.shiprate;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.7.14
 * 2015-06-08T18:02:43.265+08:00
 * Generated source version: 2.7.14
 * 
 */
@WebService(targetNamespace = "http://www.example.org/ShipRate/", name = "EIS")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface EIS {

    @WebResult(name = "addEISOrderResponse", targetNamespace = "http://www.example.org/ShipRate/", partName = "parameters")
    @WebMethod(action = "http://www.example.org/ShipRate/addEISOrder")
    public AddEISOrderResponse addEISOrder(
            @WebParam(partName = "parameters", name = "addEISOrderReqiest", targetNamespace = "http://www.example.org/ShipRate/")
                    AddEISOrderReqiest parameters
    );
}

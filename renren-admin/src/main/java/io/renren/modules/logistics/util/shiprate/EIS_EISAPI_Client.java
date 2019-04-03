
package io.renren.modules.logistics.util.shiprate;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 2.7.14
 * 2015-06-08T18:02:43.062+08:00
 * Generated source version: 2.7.14
 * 
 */
public final class EIS_EISAPI_Client {

    private static final QName SERVICE_NAME = new QName("http://www.example.org/ShipRate/", "EIS");

    private EIS_EISAPI_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = EIS_Service.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        EIS_Service ss = new EIS_Service(wsdlURL, SERVICE_NAME);
        EIS port = ss.getEISAPI();  
        
        {
        System.out.println("Invoking addEISOrder...");
            AddEISOrderReqiest _addEISOrder_parameters = null;
            AddEISOrderResponse _addEISOrder__return = port.addEISOrder(_addEISOrder_parameters);
        System.out.println("addEISOrder.result=" + _addEISOrder__return);


        }

        System.exit(0);
    }

}

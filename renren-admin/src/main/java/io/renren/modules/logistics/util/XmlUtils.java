package io.renren.modules.logistics.util;

import io.renren.modules.logistics.entity.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import java.util.List;

public class XmlUtils {



    /**
     * 根据List列表封装成XML数据
     * @return 返回封装后的数据
     */
    public static String getXmlFromList(List<Shipping> beanList) {
        {
            String result = "";

            Element root = new Element("AmazonEnvelope");
            Document doc = new Document(root);

            for (Shipping ship : beanList) { //先循环它
                Element elementHeader = new Element("Header");
                Header header=ship.getHeader();
                elementHeader.addContent(new Element("DocumentVersion").setText(header.getDocumentVersion()));
                elementHeader.addContent(new Element("MerchantIdentifier").setText(header.getMerchantIdentifier()));

                Element element = new Element("MessageType");
                element.setText(ship.getMessageType());
                Message message=ship.getMessage();
                Element element2 = new Element("Message");
                element2.addContent(new Element("MessageID").setText(message.getMessageID()));
                OrderFulfillment orderfu=message.getOrderFulfillment();
                Element element3 = new Element("OrderFulfillment");
                Element element4 = new Element("FulfillmentData");
                Element element5 = new Element("Item");
                element3.addContent(new Element("AmazonOrderID").setText(orderfu.getAmazonOrderID()));
                element3.addContent(new Element("FulfillmentDate").setText(orderfu.getFulfillmentDate()));
                FulfillmentData fuda=orderfu.getFulfillmentData();
                System.out.println(fuda.getCarrierName()+"==");
                element4.addContent(new Element("CarrierName").setText(fuda.getCarrierName()));
                element4.addContent(new Element("ShippingMethod").setText(fuda.getShippingMethod()));
                element4.addContent(new Element("ShipperTrackingNumber").setText(fuda.getShipperTrackingNumber()));
                List<Item> items=orderfu.getItems();
                for (Item item:items) {
                    element5.addContent(new Element("AmazonOrderItemCode").setText(item.getAmazonOrderItemCode()));
                    element5.addContent(new Element("Quantity").setText(item.getQuantity()));
                }

                element3.addContent(element4);
                element3.addContent(element5);
                element2.addContent(element3);

                root.addContent(elementHeader);
                root.addContent(element);
                root.addContent(element2);

            }

            XMLOutputter XMLOut = new XMLOutputter();

            try {
                /**
                 * 输出XML数据
                 */
                result = XMLOut.outputString(doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
      
  


}

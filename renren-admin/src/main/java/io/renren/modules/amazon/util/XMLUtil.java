package io.renren.modules.amazon.util;

import io.renren.modules.amazon.dto.ListOrdersResponseDto;
import io.renren.modules.amazon.dto.OrderDto;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMLUtil {

    public static ListOrdersResponseDto analysisListOrdersResponse(String responseXml){
        Document doc = null;
        ListOrdersResponseDto ordersResponseDto = new ListOrdersResponseDto();
        List<OrderDto> list = new ArrayList<>();
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(responseXml);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            Element listOrdersResultElt = rootElt.element("ListOrdersResult");
            ordersResponseDto.setNextToken(rootElt.element("NextToken").getText());
            Iterator<Element> orders = listOrdersResultElt.elementIterator("Orders");
            OrderDto orderDto = null;
            while (orders.hasNext()){
                orderDto = new OrderDto();
                Element element = (Element)orders.next();
                orderDto.setAmazonOrderId(element.elementText("AmazonOrderId"));
                orderDto.setPurchaseDate(element.elementText("PurchaseDate"));
                orderDto.setLastUpdateDate(element.elementText("LastUpdateDate"));
                orderDto.setOrderStatus(element.elementText("OrderStatus"));
                orderDto.setFulfillmentChannel(element.elementText("FulfillmentChannel"));
                orderDto.setSalesChannel(element.elementText("SalesChannel"));
                orderDto.setShipServiceLevel(element.elementText("ShipServiceLevel"));
                Element shippingAddressElt = element.element("ShippingAddress");
                orderDto.setName(shippingAddressElt.elementText("Name"));
                orderDto.setAddressLine1(shippingAddressElt.elementText("AddressLine1"));
                // TODO: 2018/11/20 所有可选项加判断
               /* orderDto.setName(shippingAddressElt.elementText("Name"));
                orderDto.setName(shippingAddressElt.elementText("Name"));
                orderDto.setName(shippingAddressElt.elementText("Name"));
                orderDto.setName(shippingAddressElt.elementText("Name"));
                orderDto.setName(shippingAddressElt.elementText("Name"));*/

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return ordersResponseDto;
    }
}

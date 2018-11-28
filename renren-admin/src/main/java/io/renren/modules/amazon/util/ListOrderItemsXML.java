package io.renren.modules.amazon.util;

import com.fasterxml.jackson.databind.ser.std.IterableSerializer;
import io.renren.modules.amazon.dto.ListOrderItemsByNextTokenResponseDto;
import io.renren.modules.amazon.dto.MoneyDto;
import io.renren.modules.amazon.dto.OrderItemDto;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListOrderItemsXML {

    @Test
    public void listOrderItems(){

        Document document =null;
        ListOrderItemsByNextTokenResponseDto listOrderItemsByNextTokenResponseDto= new ListOrderItemsByNextTokenResponseDto();
        List<OrderItemDto> orderItemDtos= new ArrayList<OrderItemDto>();
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read("C:\\Users\\asus\\Desktop\\ListOrderItemsByNextTokenResult1.xml");  //读取XML文件,获得document对象
            Element rootElement = document.getRootElement();//获取根标签
            Element listOrderItemsByNextTokenResult = rootElement.element("ListOrderItemsByNextTokenResult");//获取指定的标签
            Element nextToken = listOrderItemsByNextTokenResult.element("NextToken");//获取指定的标签下的指定标签
            if (nextToken!=null){
                listOrderItemsByNextTokenResponseDto.setNextToken(nextToken.getTextTrim());
            }
            Element amazonOrderId = listOrderItemsByNextTokenResult.element("AmazonOrderId");
            listOrderItemsByNextTokenResponseDto.setAmazonOrderId(amazonOrderId.getTextTrim());
            Element orderItems = listOrderItemsByNextTokenResult.element("OrderItems");
            List<Element> orderItemList = orderItems.elements();
            OrderItemDto orderItemDto =null;
            //遍历orderitems下的子标签
            for (Element orderItem : orderItemList) {
                orderItemDto= new OrderItemDto();
                Element asin = orderItem.element("ASIN");
                orderItemDto.setASIN(asin.getTextTrim());
                Element sellerSKU = orderItem.element("SellerSKU");
                if (sellerSKU!=null){
                    orderItemDto.setSellerSKU(sellerSKU.getTextTrim());
                }
                Element orderItemId = orderItem.element("OrderItemId");
                orderItemDto.setOrderItemId(orderItemId.getTextTrim());
                Element title = orderItem.element("Title");
                if (title!=null){
                    orderItemDto.setTitle(title.getTextTrim());
                }
                Element quantityOrdered = orderItem.element("QuantityOrdered");
                orderItemDto.setQuantityOrdered(Integer.valueOf(quantityOrdered.getTextTrim()));
                Element quantityShipped = orderItem.element("QuantityShipped");
                if (quantityShipped!=null){
                    orderItemDto.setQuantityShipped(Integer.valueOf(quantityShipped.getTextTrim()));
                }
                Element itemPrice = orderItem.element("ItemPrice");
                if (itemPrice!=null){
                    Element currencyCode = itemPrice.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = itemPrice.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setItemPrice(moneyDto);
                }
                Element shippingPrice = orderItem.element("ShippingPrice");
                if (shippingPrice!=null){
                    Element currencyCode = shippingPrice.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = shippingPrice.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setShippingPrice(moneyDto);
                }
                Element giftWrapTax = orderItem.element("GiftWrapTax");
                if (giftWrapTax!=null){
                    Element currencyCode = giftWrapTax.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = giftWrapTax.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setGiftWrapTax(moneyDto);
                }
                Element shippingDiscount = orderItem.element("ShippingDiscount");
                if (shippingDiscount!=null){
                    Element currencyCode = shippingDiscount.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = shippingDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setShippingDiscount(moneyDto);
                }
                Element promotionDiscount = orderItem.element("PromotionDiscount");
                if (promotionDiscount!=null){
                    Element currencyCode = promotionDiscount.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = promotionDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setPromotionDiscount(moneyDto);
                }
                Element promotionIds = orderItem.element("PromotionIds");
                if (promotionIds!=null){
                   orderItemDto.setPromotionIds(promotionIds.getTextTrim());
                }
                Element codFee = orderItem.element("CODFee");
                if (codFee!=null){
                    Element currencyCode = codFee.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = codFee.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setCODFee(moneyDto);
                }
                Element codFeeDiscount = orderItem.element("CODFeeDiscount");
                if (codFeeDiscount!=null){
                    Element currencyCode = codFeeDiscount.element("CurrencyCode");
                    MoneyDto moneyDto=new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = codFeeDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setCODFeeDiscount(moneyDto);
                }
                Element giftMessageText = orderItem.element("GiftMessageText");
                if (giftMessageText!=null){
                    orderItemDto.setGiftMessageText(giftMessageText.getTextTrim());
                }
                Element giftWrapLevel = orderItem.element("GiftWrapLevel");
                if (giftWrapLevel!=null){
                    orderItemDto.setGiftWrapLevel(giftWrapLevel.getTextTrim());
                }

                Element conditionNote = orderItem.element("ConditionNote");
                if (conditionNote!=null){
                    orderItemDto.setConditionNote(conditionNote.getTextTrim());
                }
                Element conditionId = orderItem.element("ConditionId");
                if (conditionId!=null){
                    orderItemDto.setConditionId(conditionId.getTextTrim());
                }
                Element conditionSubtypeId = orderItem.element("ConditionSubtypeId");
                if (conditionSubtypeId!=null) {
                    orderItemDto.setConditionSubtypeId(conditionSubtypeId.getTextTrim());
                }
                Element scheduledDeliveryStartDate = orderItem.element("ScheduledDeliveryStartDate");
                if (scheduledDeliveryStartDate!=null){
                    orderItemDto.setScheduledDeliveryStartDate(scheduledDeliveryStartDate.getTextTrim());
                }
                Element scheduledDeliveryEndDate = orderItem.element("ScheduledDeliveryEndDate");
                if (scheduledDeliveryEndDate!=null){
                    orderItemDto.setScheduledDeliveryEndDate(scheduledDeliveryEndDate.getTextTrim());
                }
                orderItemDtos.add(orderItemDto);
            }
            listOrderItemsByNextTokenResponseDto.setOrderItems(orderItemDtos);
            for (OrderItemDto orderItemDto1 : orderItemDtos) {
                System.out.println(orderItemDto1);
            }
            System.out.println(listOrderItemsByNextTokenResponseDto);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

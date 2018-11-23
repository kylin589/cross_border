package io.renren.modules.amazon.util;


import io.renren.modules.amazon.dto.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DomUtil {
    /**
     * @methodname: analysisListOrdersResponse Dom4j解析亚马逊订单返回的xml数据 工具类
     * @param: [responseXml]
     * @return: io.renren.modules.amazon.dto.ListOrdersResponseDto
     * @auther: jhy
     * @date: 2018/11/21 18:32
     */
    public static ListOrdersResponseDto analysisListOrdersResponse(String responseXml) {
        Document document = null;
        //订单请求的实体
        ListOrdersResponseDto ordersResponseDto = new ListOrdersResponseDto();
        //订单请求的实体
        List<OrderDto> list = new ArrayList<OrderDto>();
        try {
            // 将字符串转为XML
            document = DocumentHelper.parseText(responseXml);
            Element elemRoot = document.getRootElement();//获取到跟元素
            Element listOrdersResult = elemRoot.element("ListOrdersResult");//获取指定的元素
            Element nextToken = listOrdersResult.element("NextToken");//获取指定的元素
            if (nextToken != null) {
                String nextTokenTextTrim = nextToken.getTextTrim();
                ordersResponseDto.setNextToken(nextTokenTextTrim);
            }
            Element lastUpdatedBefore = listOrdersResult.element("LastUpdatedBefore");
            if (lastUpdatedBefore != null) {
                String lastUpdatedBeforeTextTrim = lastUpdatedBefore.getTextTrim();
                ordersResponseDto.setLastUpdatedBefore(lastUpdatedBeforeTextTrim);
            }
            Element createdBefore = listOrdersResult.element("CreatedBefore");
            if (createdBefore != null) {
                String createdBeforeTextTrim = createdBefore.getTextTrim();
                ordersResponseDto.setCreatedBefore(createdBeforeTextTrim);
            }

            Element orders = listOrdersResult.element("Orders");
            //Orders下面的子元素
            List<Element> orderList = orders.elements();
            OrderDto orderDto = null;
            //遍历Orders下面的子元素
            for (Element order : orderList) {
                orderDto = new OrderDto();
                Element amazonOrderId = order.element("AmazonOrderId");
                orderDto.setAmazonOrderId(amazonOrderId.getTextTrim());
                Element sellerOrderId = order.element("SellerOrderId");
                //根据名字获取到需要的数据，对所有的可选项加判断
                if (sellerOrderId != null) {
                    orderDto.setSellerOrderId(sellerOrderId.getTextTrim());
                }
                Element purchaseDate = order.element("PurchaseDate");
                orderDto.setPurchaseDate(purchaseDate.getTextTrim());
                Element lastUpdateDate = order.element("LastUpdateDate");
                orderDto.setLastUpdateDate(lastUpdateDate.getTextTrim());
                Element orderStatus = order.element("OrderStatus");
                orderDto.setOrderStatus(orderStatus.getTextTrim());
                Element fulfillmentChannel = order.element("FulfillmentChannel");
                if (fulfillmentChannel != null) {
                    orderDto.setFulfillmentChannel(fulfillmentChannel.getTextTrim());
                }
                Element salesChannel = order.element("SalesChannel");
                if (salesChannel != null) {
                    orderDto.setSalesChannel(salesChannel.getTextTrim());
                }
                Element orderChannel = order.element("OrderChannel");
                if (orderChannel != null) {
                    orderDto.setOrderChannel(orderChannel.getTextTrim());
                }
                Element shipServiceLevel = order.element("ShipServiceLevel");
                if (shipServiceLevel != null) {
                    orderDto.setShipServiceLevel(shipServiceLevel.getTextTrim());
                }
                Element shippingAddress = order.element("ShippingAddress");
                if (shippingAddress != null) {
                    Element name = shippingAddress.element("Name");
                    if (name != null) {
                        orderDto.setName(name.getTextTrim());
                    }
                    Element addressLine1 = shippingAddress.element("AddressLine1");
                    if (addressLine1 != null) {
                        orderDto.setAddressLine1(addressLine1.getTextTrim());
                    }
                    Element addressLine2 = shippingAddress.element("AddressLine2");
                    if (addressLine2 != null) {
                        orderDto.setAddressLine2(addressLine2.getTextTrim());
                    }
                    Element addressLine3 = shippingAddress.element("AddressLine3");
                    if (addressLine3 != null) {
                        orderDto.setAddressLine3(addressLine3.getTextTrim());
                    }
                    Element city = shippingAddress.element("City");
                    if (city != null) {
                        orderDto.setCity(city.getTextTrim());
                    }
                    Element county = shippingAddress.element("County");
                    if (county != null) {
                        orderDto.setCounty(county.getTextTrim());
                    }
                    Element district = shippingAddress.element("District");
                    if (district != null) {
                        orderDto.setDistrict(district.getTextTrim());
                    }
                    Element stateOrRegion = shippingAddress.element("StateOrRegion");
                    if (stateOrRegion != null) {
                        orderDto.setStateOrRegion(stateOrRegion.getTextTrim());
                    }
                    Element postalCode = shippingAddress.element("PostalCode");
                    if (postalCode != null) {
                        orderDto.setPostalCode(postalCode.getTextTrim());
                    }
                    Element countryCode = shippingAddress.element("CountryCode");
                    if (countryCode != null) {
                        orderDto.setCountryCode(countryCode.getTextTrim());
                    }
                    Element phone = shippingAddress.element("Phone");
                    if (phone != null) {
                        orderDto.setPhone(phone.getTextTrim());
                    }
                    Element addressType = shippingAddress.element("AddressType");
                    if (addressType != null) {
                        orderDto.setAddressType(addressType.getTextTrim());
                    }
                }
                Element orderTotal = order.element("OrderTotal");
                if (orderTotal != null) {
                    Element currencyCode = orderTotal.element("CurrencyCode");
                    if (currencyCode != null) {
                        orderDto.setCurrencyCode(currencyCode.getTextTrim());
                    }
                    Element amount = orderTotal.element("Amount");
                    if (amount != null) {
                        orderDto.setAmount(amount.getTextTrim());
                    }
                }
                Element numberOfItemsShipped = order.element("NumberOfItemsShipped");
                if (numberOfItemsShipped != null) {
                    orderDto.setNumberOfItemsShipped(numberOfItemsShipped.getTextTrim());
                }
                Element numberOfItemsUnshipped = order.element("NumberOfItemsUnshipped");
                if (numberOfItemsUnshipped != null) {
                    orderDto.setNumberOfItemsUnshipped(numberOfItemsUnshipped.getTextTrim());
                }
                Element paymentMethod = order.element("PaymentMethod");
                if (paymentMethod != null) {
                    orderDto.setPaymentMethod(paymentMethod.getTextTrim());
                }
                Element paymentMethodDetails = order.element("PaymentMethodDetails");
                if (paymentMethodDetails != null) {
                    Element paymentMethodDetail = paymentMethodDetails.element("PaymentMethodDetail");
                    if (paymentMethodDetail != null) {
                        orderDto.setPaymentMethodDetail(paymentMethodDetail.getTextTrim());
                    }
                }
                Element marketplaceId = order.element("MarketplaceId");
                orderDto.setMarketplaceId(marketplaceId.getTextTrim());
                Element buyerEmail = order.element("BuyerEmail");
                if (buyerEmail != null) {
                    orderDto.setBuyerEmail(buyerEmail.getTextTrim());
                }
                Element buyerName = order.element("BuyerName");
                if (buyerName != null) {
                    orderDto.setBuyerName(buyerName.getTextTrim());
                }
                Element shipmentServiceLevelCategory = order.element("ShipmentServiceLevelCategory");
                if (shipmentServiceLevelCategory != null) {
                    orderDto.setShipmentServiceLevelCategory(shipmentServiceLevelCategory.getTextTrim());
                }
                Element shippedByAmazonTFM = order.element("ShippedByAmazonTFM");
                if (shippedByAmazonTFM != null) {
                    orderDto.setShippedByAmazonTFM(shippedByAmazonTFM.getTextTrim());
                }
                Element orderType = order.element("OrderType");
                orderDto.setOrderType(orderType.getTextTrim());
                Element earliestShipDate = order.element("EarliestShipDate");
                orderDto.setEarliestShipDate(earliestShipDate.getTextTrim());
                Element latestShipDate = order.element("LatestShipDate");
                orderDto.setLatestShipDate(latestShipDate.getTextTrim());
                Element earliestDeliveryDate = order.element("EarliestDeliveryDate");
                if (earliestDeliveryDate != null) {
                    orderDto.setEarliestDeliveryDate(earliestDeliveryDate.getTextTrim());
                }
                Element latestDeliveryDate = order.element("LatestDeliveryDate");
                if (lastUpdateDate != null) {
                    orderDto.setLatestDeliveryDate(lastUpdateDate.getTextTrim());
                }
                list.add(orderDto);
            }
            ordersResponseDto.setOrders(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return ordersResponseDto;
    }

    /**
     * @methodname: analysisListOrderItemsByNextTokenResponse 解析亚马逊商品订单商品
     * @param: [responseXml]
     * @return: io.renren.modules.amazon.dto.ListOrderItemsByNextTokenResponseDto
     * @auther: jhy
     * @date: 2018/11/22 17:11
     */
    public static ListOrderItemsByNextTokenResponseDto analysisListOrderItemsByNextTokenResponse(String responseXml) {
        Document document = null;
        ListOrderItemsByNextTokenResponseDto listOrderItemsByNextTokenResponseDto= new ListOrderItemsByNextTokenResponseDto();
        List<OrderItemDto> orderItemDtos= new ArrayList<OrderItemDto>();
        try {
            // 将字符串转为XML
            document = DocumentHelper.parseText(responseXml);
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
                //根据名字获取到需要的数据，对所有的可选项加判断
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
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return listOrderItemsByNextTokenResponseDto;
    }
}

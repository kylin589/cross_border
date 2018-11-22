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
                if (shippedByAmazonTFM!=null){
                    orderDto.setShippedByAmazonTFM(shippedByAmazonTFM.getTextTrim());}
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

}

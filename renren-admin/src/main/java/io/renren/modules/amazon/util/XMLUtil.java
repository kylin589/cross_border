package io.renren.modules.amazon.util;

import io.renren.modules.amazon.dto.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {

    public static AnalysisFeedSubmissionResultDto analysisFeedSubmissionResult(String xmlPath) {
        AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto = new AnalysisFeedSubmissionResultDto();
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File(xmlPath));
            Element amazonEnvelope = document.getRootElement();
            Element message = amazonEnvelope.element("Message");
            analysisFeedSubmissionResultDto.setMessageContent(message.asXML());
            Element processingReport = message.element("ProcessingReport");
            analysisFeedSubmissionResultDto.setFeedSubmissionId(processingReport.element("DocumentTransactionID").getTextTrim());
            Element processingSummary = processingReport.element("ProcessingSummary");
            String messagesProcessedStr = processingSummary.element("MessagesProcessed").getTextTrim();
            analysisFeedSubmissionResultDto.setMessagesProcessed(Integer.parseInt(messagesProcessedStr));
            String messagesSuccessfulStr = processingSummary.element("MessagesSuccessful").getTextTrim();
            analysisFeedSubmissionResultDto.setMessagesSuccessful(Integer.parseInt(messagesSuccessfulStr));
            String messagesWithErrorStr = processingSummary.element("MessagesWithError").getTextTrim();
            analysisFeedSubmissionResultDto.setMessagesWithError(Integer.parseInt(messagesWithErrorStr));
            String messagesWithWarningStr = processingSummary.element("MessagesWithWarning").getTextTrim();
            analysisFeedSubmissionResultDto.setMessagesWithWarning(Integer.parseInt(messagesWithWarningStr));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return analysisFeedSubmissionResultDto;
    }

    /**
     * 将文档对象写入对应的文件中
     *
     * @param document 文档对象
     * @param path     写入文档的路径
     * @throws IOException
     */
    public final static void writeXMLToFile(Document document, String path) throws IOException {
        if (document == null || path == null) {
            return;
        }
        FileWriter fileWiter = new FileWriter(path);
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(fileWiter, format);
        writer.write(document);
        writer.close();
        fileWiter.close();
    }

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
            if(orders!=null ){
                //Orders下面的子元素
                List<Element> orderList = orders.elements();
                //遍历Orders下面的子元素
                for (Element order : orderList) {
                    OrderDto orderDto = new OrderDto();//封装订单Order的POJO
                    Element amazonOrderId = order.element("AmazonOrderId");
                    orderDto.setAmazonOrderId(amazonOrderId.getTextTrim());//封装订单亚马逊amazonOrderId


                    Element purchaseDate = order.element("PurchaseDate");
                    orderDto.setPurchaseDate(purchaseDate.getTextTrim());//封装购买日期

                    Element lastUpdateDate = order.element("LastUpdateDate");
                    orderDto.setLastUpdateDate(lastUpdateDate.getTextTrim());//封装最新的更新日期

                    Element orderStatus = order.element("OrderStatus");
                    orderDto.setOrderStatus(orderStatus.getTextTrim());//封装订单状态

                    Element fulfillmentChannel = order.element("FulfillmentChannel");
                    if (fulfillmentChannel != null) {
                        orderDto.setFulfillmentChannel(fulfillmentChannel.getTextTrim());//封装订单配送方式：亚马逊配送 (AFN) 或卖家自行配送 (MFN)。
                    }

                    Element salesChannel = order.element("SalesChannel");
                    if (salesChannel != null) {
                        orderDto.setSalesChannel(salesChannel.getTextTrim());//封装订单中第一件商品的销售渠道。
                    }

                    Element shipServiceLevel = order.element("ShipServiceLevel");
                    if (shipServiceLevel != null) {
                        orderDto.setShipServiceLevel(shipServiceLevel.getTextTrim());//封装货件服务水平。
                    }

                    Element shippingAddress = order.element("ShippingAddress");
                    if (shippingAddress != null) {
                        Element name = shippingAddress.element("Name");
                        if (name != null) {
                            orderDto.setName(name.getTextTrim());//封装收件人姓名

                        }
                        Element addressLine1 = shippingAddress.element("AddressLine1");
                        if (addressLine1 != null) {
                            orderDto.setAddressLine1(addressLine1.getTextTrim());//封装收件人地址1
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
                            orderDto.setCity(city.getTextTrim());//封装收件人城市
                        }
                   /* Element county = shippingAddress.element("County");
                    if (county != null) {
                        orderDto.setCounty(county.getTextTrim());
                    }*/
                   /* Element district = shippingAddress.element("District");
                    if (district != null) {
                        orderDto.setDistrict(district.getTextTrim());
                    }*/
                        Element stateOrRegion = shippingAddress.element("StateOrRegion");
                        if (stateOrRegion != null) {
                            orderDto.setStateOrRegion(stateOrRegion.getTextTrim());//封装省/自治区/直辖市或地区
                        }

                        Element postalCode = shippingAddress.element("PostalCode");
                        if (postalCode != null) {
                            orderDto.setPostalCode(postalCode.getTextTrim());//封装邮政编码。
                        }
                        Element countryCode = shippingAddress.element("CountryCode");
                        if (countryCode != null) {
                            orderDto.setCountryCode(countryCode.getTextTrim());//封装国家/地区代码。
                        }
                        Element phone = shippingAddress.element("Phone");
                        if (phone != null) {
                            orderDto.setPhone(phone.getTextTrim());//封装收件人联系电话
                        }
                   /* Element addressType = shippingAddress.element("AddressType");//地址类型
                    if (addressType != null) {
                        orderDto.setAddressType(addressType.getTextTrim());
                    }*/
                    }
                    Element orderTotal = order.element("OrderTotal");
                    if (orderTotal != null) {
                        Element currencyCode = orderTotal.element("CurrencyCode");
                        if (currencyCode != null) {
                            orderDto.setCurrencyCode(currencyCode.getTextTrim());//封装货币代码
                        }
                        Element amount = orderTotal.element("Amount");
                        if (amount != null) {
                            orderDto.setAmount(amount.getTextTrim());//封装货币金额
                        }
                    }
                    Element numberOfItemsShipped = order.element("NumberOfItemsShipped");
                    if (numberOfItemsShipped != null) {
                        orderDto.setNumberOfItemsShipped(numberOfItemsShipped.getTextTrim());//封装已配送的商品数量
                    }

                    Element numberOfItemsUnshipped = order.element("NumberOfItemsUnshipped");
                    if (numberOfItemsUnshipped != null) {
                        orderDto.setNumberOfItemsUnshipped(numberOfItemsUnshipped.getTextTrim());//封装未配送的商品数量
                    }
                    Element paymentMethod = order.element("PaymentMethod");
                    if (paymentMethod != null) {
                        orderDto.setPaymentMethod(paymentMethod.getTextTrim());//封装订单的主要付款方式
                    }
                    Element marketplaceId = order.element("MarketplaceId");
                    orderDto.setMarketplaceId(marketplaceId.getTextTrim());//封装订单生成所在商城的匿名编码

                    Element buyerEmail = order.element("BuyerEmail");
                    if (buyerEmail != null) {
                        orderDto.setBuyerEmail(buyerEmail.getTextTrim());//封装买家邮编
                    }
                    Element buyerName = order.element("BuyerName");
                    if (buyerName != null) {
                        orderDto.setBuyerName(buyerName.getTextTrim());//封装买家姓名。
                    }
                    Element shipmentServiceLevelCategory = order.element("ShipmentServiceLevelCategory");
                    if (shipmentServiceLevelCategory != null) {
                        orderDto.setShipmentServiceLevelCategory(shipmentServiceLevelCategory.getTextTrim());//封装订单的配送服务级别分类
                    }
                    Element shippedByAmazonTFM = order.element("ShippedByAmazonTFM");
                    if (shippedByAmazonTFM != null) {
                        orderDto.setShippedByAmazonTFM(shippedByAmazonTFM.getTextTrim());//封装指明订单配送方是否是亚马逊配送 (Amazon TFM) 服务。
                    }
                    Element orderType = order.element("OrderType");
                    orderDto.setOrderType(orderType.getTextTrim());//封装订单类型

                    Element earliestShipDate = order.element("EarliestShipDate");
                    orderDto.setEarliestShipDate(earliestShipDate.getTextTrim());//封装您承诺的订单发货时间范围的第一天

                    Element latestShipDate = order.element("LatestShipDate");
                    orderDto.setLatestShipDate(latestShipDate.getTextTrim());//封装您承诺的订单发货时间范围的最后一天。

                    Element earliestDeliveryDate = order.element("EarliestDeliveryDate");
                    if (earliestDeliveryDate != null) {
                        orderDto.setEarliestDeliveryDate(earliestDeliveryDate.getTextTrim());//您承诺的订单送达时间范围的第一天。
                    }

                    Element latestDeliveryDate = order.element("LatestDeliveryDate");
                    if (latestDeliveryDate != null) {
                        orderDto.setLatestDeliveryDate(latestDeliveryDate.getTextTrim());//封装您承诺的订单送达时间范围的最后一天
                    }

               /* Element paymentMethodDetails = order.element("PaymentMethodDetails");
                if (paymentMethodDetails != null) {
                    Element paymentMethodDetail = paymentMethodDetails.element("PaymentMethodDetail");
                    if (paymentMethodDetail != null) {
                        orderDto.setPaymentMethodDetail(paymentMethodDetail.getTextTrim());
                    }
                }*/

                    list.add(orderDto);
                }

            }


            ordersResponseDto.setOrders(list);
        } catch (DocumentException e) {
            System.out.println(e.getMessage()+"############################");
        }
        return ordersResponseDto;
    }

    /**
     * @methodname: analysisListOrdersByNextTokenResponse Dom4j解析亚马逊NextToken订单返回的xml数据 工具类
     * @param: [responseXml]
     * @return: io.renren.modules.amazon.dto.ListOrdersResponseDto
     * @auther: jhy
     * @date: 2018/11/21 18:32
     */
    public static ListOrdersResponseDto analysisListOrdersByNextTokenResponse(String responseXml) {
        Document document = null;
        //订单请求的实体
        ListOrdersResponseDto ordersResponseDto = new ListOrdersResponseDto();
        //订单请求的实体
        List<OrderDto> list = new ArrayList<OrderDto>();
        try {
            // 将字符串转为XML
            document = DocumentHelper.parseText(responseXml);
            Element elemRoot = document.getRootElement();//获取到跟元素
            Element listOrdersResult = elemRoot.element("ListOrdersByNextTokenResult");//获取指定的元素
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
            if(orders!=null){
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
        ListOrderItemsByNextTokenResponseDto listOrderItemsByNextTokenResponseDto = new ListOrderItemsByNextTokenResponseDto();
        List<OrderItemDto> orderItemDtos = new ArrayList<OrderItemDto>();
        try {
            // 将字符串转为XML
            document = DocumentHelper.parseText(responseXml);
            Element rootElement = document.getRootElement();//获取根标签
            Element listOrderItemsByNextTokenResult = rootElement.element("ListOrderItemsByNextTokenResult");//获取指定的标签
            Element nextToken = listOrderItemsByNextTokenResult.element("NextToken");//获取指定的标签下的指定标签
            if (nextToken != null) {
                listOrderItemsByNextTokenResponseDto.setNextToken(nextToken.getTextTrim());
            }
            Element amazonOrderId = listOrderItemsByNextTokenResult.element("AmazonOrderId");
            listOrderItemsByNextTokenResponseDto.setAmazonOrderId(amazonOrderId.getTextTrim());
            Element orderItems = listOrderItemsByNextTokenResult.element("OrderItems");
            List<Element> orderItemList = orderItems.elements();
            OrderItemDto orderItemDto = null;
            //遍历orderitems下的子标签
            for (Element orderItem : orderItemList) {
                orderItemDto = new OrderItemDto();
                Element asin = orderItem.element("ASIN");
                orderItemDto.setASIN(asin.getTextTrim());
                Element sellerSKU = orderItem.element("SellerSKU");
                //根据名字获取到需要的数据，对所有的可选项加判断
                if (sellerSKU != null) {
                    orderItemDto.setSellerSKU(sellerSKU.getTextTrim());
                }
                Element orderItemId = orderItem.element("OrderItemId");
                orderItemDto.setOrderItemId(orderItemId.getTextTrim());
                Element title = orderItem.element("Title");
                if (title != null) {
                    orderItemDto.setTitle(title.getTextTrim());
                }
                Element quantityOrdered = orderItem.element("QuantityOrdered");
                orderItemDto.setQuantityOrdered(Integer.valueOf(quantityOrdered.getTextTrim()));
                Element quantityShipped = orderItem.element("QuantityShipped");
                if (quantityShipped != null) {
                    orderItemDto.setQuantityShipped(Integer.valueOf(quantityShipped.getTextTrim()));
                }
                Element itemPrice = orderItem.element("ItemPrice");
                if (itemPrice != null) {
                    Element currencyCode = itemPrice.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = itemPrice.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setItemPrice(moneyDto);
                }
                Element shippingPrice = orderItem.element("ShippingPrice");
                if (shippingPrice != null) {
                    Element currencyCode = shippingPrice.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = shippingPrice.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setShippingPrice(moneyDto);
                }
                Element giftWrapTax = orderItem.element("GiftWrapTax");
                if (giftWrapTax != null) {
                    Element currencyCode = giftWrapTax.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = giftWrapTax.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setGiftWrapTax(moneyDto);
                }
                Element shippingDiscount = orderItem.element("ShippingDiscount");
                if (shippingDiscount != null) {
                    Element currencyCode = shippingDiscount.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = shippingDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setShippingDiscount(moneyDto);
                }
                Element promotionDiscount = orderItem.element("PromotionDiscount");
                if (promotionDiscount != null) {
                    Element currencyCode = promotionDiscount.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = promotionDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setPromotionDiscount(moneyDto);
                }
                Element promotionIds = orderItem.element("PromotionIds");
                if (promotionIds != null) {
                    orderItemDto.setPromotionIds(promotionIds.getTextTrim());
                }
                Element codFee = orderItem.element("CODFee");
                if (codFee != null) {
                    Element currencyCode = codFee.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = codFee.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setCODFee(moneyDto);
                }
                Element codFeeDiscount = orderItem.element("CODFeeDiscount");
                if (codFeeDiscount != null) {
                    Element currencyCode = codFeeDiscount.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = codFeeDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setCODFeeDiscount(moneyDto);
                }
                Element giftMessageText = orderItem.element("GiftMessageText");
                if (giftMessageText != null) {
                    orderItemDto.setGiftMessageText(giftMessageText.getTextTrim());
                }
                Element giftWrapLevel = orderItem.element("GiftWrapLevel");
                if (giftWrapLevel != null) {
                    orderItemDto.setGiftWrapLevel(giftWrapLevel.getTextTrim());
                }

                Element conditionNote = orderItem.element("ConditionNote");
                if (conditionNote != null) {
                    orderItemDto.setConditionNote(conditionNote.getTextTrim());
                }
                Element conditionId = orderItem.element("ConditionId");
                if (conditionId != null) {
                    orderItemDto.setConditionId(conditionId.getTextTrim());
                }
                Element conditionSubtypeId = orderItem.element("ConditionSubtypeId");
                if (conditionSubtypeId != null) {
                    orderItemDto.setConditionSubtypeId(conditionSubtypeId.getTextTrim());
                }
                Element scheduledDeliveryStartDate = orderItem.element("ScheduledDeliveryStartDate");
                if (scheduledDeliveryStartDate != null) {
                    orderItemDto.setScheduledDeliveryStartDate(scheduledDeliveryStartDate.getTextTrim());
                }
                Element scheduledDeliveryEndDate = orderItem.element("ScheduledDeliveryEndDate");
                if (scheduledDeliveryEndDate != null) {
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

    /**
     * @methodname: analysisListOrderItemsByNextTokenResponse 解析亚马逊商品订单商品
     * @param: [responseXml]
     * @return: io.renren.modules.amazon.dto.ListOrderItemsByNextTokenResponseDto
     * @auther: fanwei
     * @date: 2018/11/22 17:11
     */
    public static ListOrderItemsByNextTokenResponseDto analysisListOrderItemsByNextTokenResponseFanWei(String responseXml) {
        Document document = null;
        ListOrderItemsByNextTokenResponseDto listOrderItemsByNextTokenResponseDto = new ListOrderItemsByNextTokenResponseDto();
        List<OrderItemDto> orderItemDtos = new ArrayList<OrderItemDto>();
        try {
            // 将字符串转为XML
            document = DocumentHelper.parseText(responseXml);
            Element rootElement = document.getRootElement();//获取根标签
            Element ListOrderItemsResult = rootElement.element("ListOrderItemsResult");//获取指定的标签
            Element nextToken = ListOrderItemsResult.element("NextToken");//获取指定的标签下的指定标签
            if (nextToken != null) {
                listOrderItemsByNextTokenResponseDto.setNextToken(nextToken.getTextTrim());
            }
            Element amazonOrderId = ListOrderItemsResult.element("AmazonOrderId");
            listOrderItemsByNextTokenResponseDto.setAmazonOrderId(amazonOrderId.getTextTrim());
            Element orderItems = ListOrderItemsResult.element("OrderItems");
            List<Element> orderItemList = orderItems.elements();
            OrderItemDto orderItemDto = null;
            //遍历orderitems下的子标签
            for (Element orderItem : orderItemList) {
                orderItemDto = new OrderItemDto();
                Element asin = orderItem.element("ASIN");
                orderItemDto.setASIN(asin.getTextTrim());
                Element sellerSKU = orderItem.element("SellerSKU");
                //根据名字获取到需要的数据，对所有的可选项加判断
                if (sellerSKU != null) {
                    orderItemDto.setSellerSKU(sellerSKU.getTextTrim());
                }
                Element orderItemId = orderItem.element("OrderItemId");
                orderItemDto.setOrderItemId(orderItemId.getTextTrim());
                Element title = orderItem.element("Title");
                if (title != null) {
                    orderItemDto.setTitle(title.getTextTrim());
                }
                Element quantityOrdered = orderItem.element("QuantityOrdered");
                orderItemDto.setQuantityOrdered(Integer.valueOf(quantityOrdered.getTextTrim()));
                Element quantityShipped = orderItem.element("QuantityShipped");
                if (quantityShipped != null) {
                    orderItemDto.setQuantityShipped(Integer.valueOf(quantityShipped.getTextTrim()));
                }
                Element itemPrice = orderItem.element("ItemPrice");
                if (itemPrice != null) {
                    Element currencyCode = itemPrice.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = itemPrice.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setItemPrice(moneyDto);
                }
                Element shippingPrice = orderItem.element("ShippingPrice");
                if (shippingPrice != null) {
                    Element currencyCode = shippingPrice.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = shippingPrice.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setShippingPrice(moneyDto);
                }
                Element giftWrapTax = orderItem.element("GiftWrapTax");
                if (giftWrapTax != null) {
                    Element currencyCode = giftWrapTax.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = giftWrapTax.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setGiftWrapTax(moneyDto);
                }
                Element shippingDiscount = orderItem.element("ShippingDiscount");
                if (shippingDiscount != null) {
                    Element currencyCode = shippingDiscount.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = shippingDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setShippingDiscount(moneyDto);
                }
                Element promotionDiscount = orderItem.element("PromotionDiscount");
                if (promotionDiscount != null) {
                    Element currencyCode = promotionDiscount.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = promotionDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setPromotionDiscount(moneyDto);
                }
                Element promotionIds = orderItem.element("PromotionIds");
                if (promotionIds != null) {
                    orderItemDto.setPromotionIds(promotionIds.getTextTrim());
                }
                Element codFee = orderItem.element("CODFee");
                if (codFee != null) {
                    Element currencyCode = codFee.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = codFee.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setCODFee(moneyDto);
                }
                Element codFeeDiscount = orderItem.element("CODFeeDiscount");
                if (codFeeDiscount != null) {
                    Element currencyCode = codFeeDiscount.element("CurrencyCode");
                    MoneyDto moneyDto = new MoneyDto();
                    moneyDto.setCurrencyCode(currencyCode.getTextTrim());
                    Element amount = codFeeDiscount.element("Amount");
                    moneyDto.setAmount(amount.getTextTrim());
                    orderItemDto.setCODFeeDiscount(moneyDto);
                }
                Element giftMessageText = orderItem.element("GiftMessageText");
                if (giftMessageText != null) {
                    orderItemDto.setGiftMessageText(giftMessageText.getTextTrim());
                }
                Element giftWrapLevel = orderItem.element("GiftWrapLevel");
                if (giftWrapLevel != null) {
                    orderItemDto.setGiftWrapLevel(giftWrapLevel.getTextTrim());
                }

                Element conditionNote = orderItem.element("ConditionNote");
                if (conditionNote != null) {
                    orderItemDto.setConditionNote(conditionNote.getTextTrim());
                }
                Element conditionId = orderItem.element("ConditionId");
                if (conditionId != null) {
                    orderItemDto.setConditionId(conditionId.getTextTrim());
                }
                Element conditionSubtypeId = orderItem.element("ConditionSubtypeId");
                if (conditionSubtypeId != null) {
                    orderItemDto.setConditionSubtypeId(conditionSubtypeId.getTextTrim());
                }
                Element scheduledDeliveryStartDate = orderItem.element("ScheduledDeliveryStartDate");
                if (scheduledDeliveryStartDate != null) {
                    orderItemDto.setScheduledDeliveryStartDate(scheduledDeliveryStartDate.getTextTrim());
                }
                Element scheduledDeliveryEndDate = orderItem.element("ScheduledDeliveryEndDate");
                if (scheduledDeliveryEndDate != null) {
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

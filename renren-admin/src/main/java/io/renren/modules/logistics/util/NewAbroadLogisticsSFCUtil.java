package io.renren.modules.logistics.util;
import io.renren.modules.logistics.util.shiprate.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 三态测试接口类
 */
public final class NewAbroadLogisticsSFCUtil {

	//客户id
	public final static String userId="W3053";

	//客户密钥
	public final static String appKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClOIqNB2SmB1tI8u7CHd0eM/HqbqQTXn8qvvMOrji7LewY96QS3PYgRoP3c8DQ7C0izUIAsif5VY0IPWmMlDc1hO1jiARWwWVHESjUpKxJFUgIlwFMF5TWkiTNNUjbBSqDXNFgc7IP8yNMyAqRxdiixkesFUDlAzig1NM1cYhCQwIDAQAB";
	//用户授权令牌
	public final static String token="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDjThuB+YKTDP/EYxx8ZBHOUB7fk3iSTBhvdbPMOUaHur0fpKYoyA2G7gER/yAi/VyUer7/cFinLC+zHRRK8C1jHCtB7HW3BEn3Phpk1xwNWU0G9g8rMxB1+vBFk9ya/f0dehjY05DR27xKr6XelLqrxo+66w9RcQjVO7QxY8TtrQIDAQAB";


	private static final QName SERVICE_NAME = new QName(
			"http://www.example.org/ShipRate/", "ShipRate");

    /**
     * 获取运费明细
	 * @return
     */
	public static Map<String,String> getFeeByOrderCode(String[] args){
		// TODO Auto-generated method stub
		URL wsdlURL = ShipRate_Service.WSDL_LOCATION;
//		if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
//			File wsdlFile = new File(args[0]);
//			try {
//				if (wsdlFile.exists()) {
//					wsdlURL = wsdlFile.toURI().toURL();
//				} else {
//					wsdlURL = new URL(args[0]);
//				}
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
		ShipRate_Service ss = new ShipRate_Service(wsdlURL, SERVICE_NAME);
		ShipRate port = ss.getShipRateSOAP();

		Map<String,String> map=new HashMap<>();
		{
			try {
			System.out.println("getFeeByOrderCode...");
				HeaderRequest _getFeeByOrderCode_headerRequest =new HeaderRequest();
				_getFeeByOrderCode_headerRequest.setAppKey(appKey);
				_getFeeByOrderCode_headerRequest.setToken(token);
				_getFeeByOrderCode_headerRequest.setUserId(userId);

				String orderCode = "SFC5WW3053904020003";//AE订单编号
				Holder<String> _getFeeByOrderCode_orderCode =
				new Holder<>(
						orderCode);
                 Holder<String> _getFeeByOrderCode_ask = new
				 Holder<String>();
				 Holder<String> _getFeeByOrderCode_sysTime =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_msg = new
				 Holder<String>();
				 Holder<String> _getFeeByOrderCode_baseFee =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_regFee = new
				 Holder<String>();
				 Holder<String> _getFeeByOrderCode_dealFee =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_insurance =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_totalFee =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_currencyCode
				 = new Holder<String>();
				 Holder<String>
				 _getFeeByOrderCode_chargebackTime = new
				 Holder<String>();
				 Holder<String>
				 _getFeeByOrderCode_chargebackWorkDay = new
				 Holder<String>();
				 Holder<String> _getFeeByOrderCode_shipTypeCode
				 = new Holder<String>();
				 Holder<String> _getFeeByOrderCode_subShipType
				 = new Holder<String>();
				 Holder<String> _getFeeByOrderCode_waybillCode
				 = new Holder<String>();
				 Holder<String> _getFeeByOrderCode_discount =
				 new Holder<String>();
				 Holder<java.util.List<OtherFee>>
				 _getFeeByOrderCode_otherFee = new
				 Holder<java.util.List<OtherFee>>();
				 Holder<String>
				 _getFeeByOrderCode_originBaseFee = new
				 Holder<String>();
				 Holder<String> _getFeeByOrderCode_originAddons
				 = new Holder<String>();
				 Holder<String> _getFeeByOrderCode_stDealFee =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_stRegFee =
				 new Holder<String>();
				 Holder<String> _getFeeByOrderCode_feeWeight =
				 new Holder<String>();



				 port.getFeeByOrderCode(_getFeeByOrderCode_headerRequest,
				 _getFeeByOrderCode_orderCode, _getFeeByOrderCode_ask,
				 _getFeeByOrderCode_sysTime, _getFeeByOrderCode_msg,
				 _getFeeByOrderCode_baseFee, _getFeeByOrderCode_regFee,
				 _getFeeByOrderCode_dealFee, _getFeeByOrderCode_insurance,
				 _getFeeByOrderCode_totalFee,
				 _getFeeByOrderCode_currencyCode,
				 _getFeeByOrderCode_chargebackTime,
				 _getFeeByOrderCode_chargebackWorkDay,
				 _getFeeByOrderCode_shipTypeCode,
				 _getFeeByOrderCode_subShipType,
				 _getFeeByOrderCode_waybillCode,
				 _getFeeByOrderCode_discount, _getFeeByOrderCode_otherFee,
				 _getFeeByOrderCode_originBaseFee,
				 _getFeeByOrderCode_originAddons,
				 _getFeeByOrderCode_stDealFee, _getFeeByOrderCode_stRegFee,
				 _getFeeByOrderCode_feeWeight);



			} catch (Exception e) {
				map.put("code","false");
				map.put("msg","系统繁忙，网络异常,请稍后尝试");
			}
			return map;
		}

	}

    /**
	 * 更新订单状态
	 */
    public static void updateOrderStatus(String[] args){
		URL wsdlURL = ShipRate_Service.WSDL_LOCATION;
//		if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
//			File wsdlFile = new File(args[0]);
//			try {
//				if (wsdlFile.exists()) {
//					wsdlURL = wsdlFile.toURI().toURL();
//				} else {
//					wsdlURL = new URL(args[0]);
//				}
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
		ShipRate_Service ss = new ShipRate_Service(wsdlURL, SERVICE_NAME);
		ShipRate port = ss.getShipRateSOAP();
		System.out.println("getFeeByOrderCode...");
		HeaderRequest _getFeeByOrderCode_headerRequest =new HeaderRequest();
		_getFeeByOrderCode_headerRequest.setAppKey(appKey);
		_getFeeByOrderCode_headerRequest.setToken(token);
		_getFeeByOrderCode_headerRequest.setUserId(userId);
        String code="WW3053904020002";
		UpdateOrderStatus updateOrderStatus=new UpdateOrderStatus();
		Holder<String> _getFeeByOrderCode_ask = new
				Holder<String>();
		Holder<String> _getFeeByOrderCode_msg = new
				Holder<String>();
		Holder<javax.xml.datatype.XMLGregorianCalendar> _getFeeByOrderCode_sysTime =
				new Holder<javax.xml.datatype.XMLGregorianCalendar>();

		UpdateOrderStatusInfoArray updateOrderStatusInfoArray=new UpdateOrderStatusInfoArray();
		updateOrderStatusInfoArray.setOrderStatus("deleted");
		updateOrderStatus.setHeaderRequest(_getFeeByOrderCode_headerRequest);
		updateOrderStatus.setUpdateOrderInfo(updateOrderStatusInfoArray);
		port.updateOrderStatus(_getFeeByOrderCode_headerRequest,updateOrderStatusInfoArray,code,_getFeeByOrderCode_ask,_getFeeByOrderCode_msg,_getFeeByOrderCode_sysTime);
		System.out.println("删除成功！！！");
	}

	/**
	 * 获取跟踪号
	 * @param args
	 * @return
	 */
	public static Map<String,String> searchOrder(String[] args){
		// TODO Auto-generated method stub
		URL wsdlURL = ShipRate_Service.WSDL_LOCATION;
//		if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
//			File wsdlFile = new File(args[0]);
//			try {
//				if (wsdlFile.exists()) {
//					wsdlURL = wsdlFile.toURI().toURL();
//				} else {
//					wsdlURL = new URL(args[0]);
//				}
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
		ShipRate_Service ss = new ShipRate_Service(wsdlURL, SERVICE_NAME);
		ShipRate port = ss.getShipRateSOAP();
		{
			System.out.println("Invoking searchOrder...");
			Map<String,String> map=new HashMap<>();
			HeaderRequest _headerRequest = new HeaderRequest();
			SearchOrderRequestInfoArray _SearchOrdersRequestInfo = new SearchOrderRequestInfoArray();
			SearchOrderRequest _searchOrdersRequest = new SearchOrderRequest();
			_headerRequest.setAppKey(appKey);
			_headerRequest.setToken(token);
			_headerRequest.setUserId(userId);
			_searchOrdersRequest.setHeaderRequest(_headerRequest);

			// 拼商品信息

			try {
				_SearchOrdersRequestInfo.setOrderCode("WW3053904020002");
				_searchOrdersRequest.setSearchOrderRequestInfo(_SearchOrdersRequestInfo);
				SearchOrderResponse _searchOrder__return = port
						.searchOrder(_searchOrdersRequest);
                 OrderInfoArray orderInfoArrays=_searchOrder__return.getOrderInfo();
				System.out.println(orderInfoArrays.getOrderCode());
				System.out.println(orderInfoArrays.getTrackNumber());
				System.out.println(orderInfoArrays.getOrderStatus()+orderInfoArrays.getOrderStatusCn());

			} catch (Exception e) {
				map.put("code","false");
				map.put("msg","系统繁忙，网络异常,请稍后尝试");
			}
			return map;
		}

	}



	/**
	 * 推送订单
	 * @param
	 * @return
	 */
	public static Map<String,String> pushOrder(AddOrderRequestInfoArray addOrderRequestInfo){
		// TODO Auto-generated method stub
		URL wsdlURL = ShipRate_Service.WSDL_LOCATION;
		ShipRate_Service ss = new ShipRate_Service(wsdlURL, SERVICE_NAME);
		ShipRate port = ss.getShipRateSOAP();
		{
			System.out.println("Invoking addOrder...");
			Map<String,String> map=new HashMap<>();
			HeaderRequest _headerRequest = new HeaderRequest();
			AddOrderRequest _addOrdersRequest = new AddOrderRequest();
			_headerRequest.setAppKey(appKey);
			_headerRequest.setToken(token);
			_headerRequest.setUserId(userId);
			_addOrdersRequest.setHeaderRequest(_headerRequest);
			try {
				_addOrdersRequest.setAddOrderRequestInfo(addOrderRequestInfo);
				AddOrderResponse _addOrder__return = port
						.addOrder(_addOrdersRequest);
				System.out.println("addOrder->result="
						+ _addOrder__return.getOrderActionStatus());
				System.out.println("addOrder->getCustomerOrderNo="
						+ _addOrder__return.getCustomerOrderNo());
				System.out.println("addOrder->getNote="
						+ _addOrder__return.getNote());
				System.out.println("addOrder->getTrackingNumber="
						+ _addOrder__return.getTrackingNumber());
				System.out.println("addOrder->getOrderCode="
						+ _addOrder__return.getOrderCode());
				System.out.println("addOrder->getOperatingTime="
						+ _addOrder__return.getOperatingTime());
				System.out.println("addOrder->getAe_code="
						+ _addOrder__return.getAe_code());
				System.out.println("addOrder->getTrackingNumberUsps="
						+ _addOrder__return.getTrackingNumberUsps());
				if("N".equals(_addOrder__return.getOrderActionStatus())){
					map.put("code","false");
					map.put("msg",_addOrder__return.getNote());
				}else if("Y".equals(_addOrder__return.getOrderActionStatus())){
					//三态物流运单号
					map.put("orderCode",_addOrder__return.getOrderCode());
					//三态物流追踪号
					map.put("trackingNumber",_addOrder__return.getTrackingNumberUsps());
				}
			} catch (Exception e) {
                  map.put("code","false");
                  map.put("msg","系统繁忙，网络异常,请稍后尝试");
			}
			return map;
		}

	}

    /**
	 * 更新订单
	 */
	public static Map<String,String> updateOrder(String orderCode,String orderStatus){
		// TODO Auto-generated method stub
		URL wsdlURL = ShipRate_Service.WSDL_LOCATION;
//		if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
//			File wsdlFile = new File(args[0]);
//			try {
//				if (wsdlFile.exists()) {
//					wsdlURL = wsdlFile.toURI().toURL();
//				} else {
//					wsdlURL = new URL(args[0]);
//				}
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
		ShipRate_Service ss = new ShipRate_Service(wsdlURL, SERVICE_NAME);
		ShipRate port = ss.getShipRateSOAP();
		{
			System.out.println("Invoking updateOrder...");
			Map<String,String> map=new HashMap<>();
			HeaderRequest _headerRequest = new HeaderRequest();
			AddOrderRequestInfoArray updateOrderInfo=new AddOrderRequestInfoArray();
			Holder<String> _getFeeByOrderCode_ask = new
					Holder<String>();
			Holder<javax.xml.datatype.XMLGregorianCalendar> _getFeeByOrderCode_sysTime =
					new Holder<javax.xml.datatype.XMLGregorianCalendar>();
			Holder<String> _getFeeByOrderCode_msg = new
					Holder<String>();
			Holder<String> alert=new Holder<String>();

		/*	GoodsDetailsArray goodsDetailsArray=new GoodsDetailsArray();
			List<GoodsDetailsArray>  _goodsDetails = new ArrayList<>();*/
			_headerRequest.setAppKey(appKey);
			_headerRequest.setToken(token);
			_headerRequest.setUserId(userId);
//			updateOrder.setHeaderRequest(_headerRequest);
		/*	updateOrderInfo.setCustomerOrderNo("123066");// 订单编号
			updateOrderInfo.setShipperAddressType(2);
			updateOrderInfo.setShippingMethod("EUEXP1");
			updateOrderInfo.setShipperName("zhangsan");
			updateOrderInfo.setRecipientName("test");
			updateOrderInfo.setRecipientCountry("CN");
			updateOrderInfo.setRecipientCity("London");
			updateOrderInfo.setRecipientState("OP");
			updateOrderInfo.setRecipientEmail("test@google.com");
			updateOrderInfo.setRecipientPhone("3242342342423");
			updateOrderInfo.setRecipientZipCode("NE1 1YB");*/
			updateOrderInfo.setOrderStatus(orderStatus);
			/*updateOrderInfo.setRecipientAddress("sdfsdf sdafsf");
			updateOrderInfo.setRecipientCountry("UK");
			updateOrderInfo.setRecipientCity("CF");
			updateOrderInfo.setGoodsQuantity("2");//订单中的货品数量
			updateOrderInfo.setGoodsDeclareWorth("4");//s申报总价值
			updateOrderInfo.setGoodsDescription("sdfsda dsf ");
			updateOrderInfo.setShippingWorth((float) 2.0);
			updateOrderInfo.setPieceNumber("3");
			updateOrderInfo.setEvaluate("5");//保险价格必须大于等于申报总价值且小于申报总价值的150%
			updateOrderInfo.setTaxesNumber("125698");//6-12位数字
			updateOrderInfo.setIsRemoteConfirm("0");*/
			// 拼商品信息
		/*	int sellnum = 1;// 某一订单在db_sell里的数量

			for (int i = 0; i < sellnum; i++) {
				goodsDetailsArray.setDetailDescription("ghgdjhgj");
				goodsDetailsArray.setDetailDescriptionCN("商品中文sss名");//必须包含中文字符
				goodsDetailsArray.setDetailCustomLabel("SKU NAME");
				goodsDetailsArray.setDetailQuantity("2");//货品总数量
				goodsDetailsArray.setDetailWorth("2");//货品中每个货物的价格
				goodsDetailsArray.setPriceTag("12");
				goodsDetailsArray.setHsCode("15633");
				goodsDetailsArray.setEnMaterial("dsdasd");
				goodsDetailsArray.setCnMaterial("daaaf");
				_goodsDetails.add(goodsDetailsArray);

			}
*/
			try {
//				updateOrder.setUpdateOrderInfo(updateOrderInfo);

			 port.updateOrder(_headerRequest,updateOrderInfo,orderCode,_getFeeByOrderCode_ask,_getFeeByOrderCode_msg,_getFeeByOrderCode_sysTime,alert);
				System.out.println("修改订单成功！！");

			} catch (Exception e) {
				map.put("code","false");
				map.put("msg","系统繁忙，网络异常,请稍后尝试");
			}
			return map;
		}

	}


	/**
	 * 打印订单物流接口
	 * @param orderId
	 * @param printType
	 * @param printFileType
	 * @param printSize
	 * @param printSort
	 * @return
	 */
	public static String print(String orderId,int printType,String printFileType,int printSize,int printSort){
		String url="http://www.sendfromchina.com/api/label?orderCodeList="+orderId+"&printType="+printType+"&print_type="+printFileType+"&printSize="+printSize+"&printSort="+printSort;

		return url;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		pushOrder();
//		String url=print("WW3053904010001",1,"pdf",1,1);
//		System.out.println(url);
//		searchOrder(args);
//		getFeeByOrderCode(args);
//		updateOrder("","");
//		updateOrderStatus(args);
	}
}

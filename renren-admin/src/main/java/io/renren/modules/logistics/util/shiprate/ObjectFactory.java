
package io.renren.modules.logistics.util.shiprate;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.shiprate package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetClassTypesResponse_QNAME = new QName("http://www.example.org/ShipRate/", "getClassTypesResponse");
    private final static QName _GetWarehouseRequest_QNAME = new QName("http://www.example.org/ShipRate/", "getWarehouseRequest");
    private final static QName _GetWarehouseResponce_QNAME = new QName("http://www.example.org/ShipRate/", "getWarehouseResponce");
    private final static QName _GetRateByModeResponse_QNAME = new QName("http://www.example.org/ShipRate/", "getRateByModeResponse");
    private final static QName _GetRatesByTypeResponse_QNAME = new QName("http://www.example.org/ShipRate/", "getRatesByTypeResponse");
    private final static QName _GetShipTypesResponse_QNAME = new QName("http://www.example.org/ShipRate/", "getShipTypesResponse");
    private final static QName _OutRates_QNAME = new QName("http://www.example.org/ShipRate/", "outRates");
    private final static QName _GetCountriesResponse_QNAME = new QName("http://www.example.org/ShipRate/", "getCountriesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.shiprate
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetWarehouseShipTypeResponse }
     * 
     */
    public GetWarehouseShipTypeResponse createGetWarehouseShipTypeResponse() {
        return new GetWarehouseShipTypeResponse();
    }

    /**
     * Create an instance of {@link Shiptype }
     * 
     */
    public Shiptype createShiptype() {
        return new Shiptype();
    }

    /**
     * Create an instance of {@link Warehouses }
     * 
     */
    public Warehouses createWarehouses() {
        return new Warehouses();
    }

    /**
     * Create an instance of {@link AddOrderRequest }
     * 
     */
    public AddOrderRequest createAddOrderRequest() {
        return new AddOrderRequest();
    }

    /**
     * Create an instance of {@link HeaderRequest }
     * 
     */
    public HeaderRequest createHeaderRequest() {
        return new HeaderRequest();
    }

    /**
     * Create an instance of {@link AddOrderRequestInfoArray }
     * 
     */
    public AddOrderRequestInfoArray createAddOrderRequestInfoArray() {
        return new AddOrderRequestInfoArray();
    }

    /**
     * Create an instance of {@link Classtypes }
     * 
     */
    public Classtypes createClasstypes() {
        return new Classtypes();
    }

    /**
     * Create an instance of {@link UpdateOrderResponse }
     * 
     */
    public UpdateOrderResponse createUpdateOrderResponse() {
        return new UpdateOrderResponse();
    }

    /**
     * Create an instance of {@link SearchOrderRequest }
     * 
     */
    public SearchOrderRequest createSearchOrderRequest() {
        return new SearchOrderRequest();
    }

    /**
     * Create an instance of {@link SearchOrderRequestInfoArray }
     * 
     */
    public SearchOrderRequestInfoArray createSearchOrderRequestInfoArray() {
        return new SearchOrderRequestInfoArray();
    }

    /**
     * Create an instance of {@link UpdateOrderStatusResponse }
     * 
     */
    public UpdateOrderStatusResponse createUpdateOrderStatusResponse() {
        return new UpdateOrderStatusResponse();
    }

    /**
     * Create an instance of {@link GetFeeByOrderCodeResponse }
     * 
     */
    public GetFeeByOrderCodeResponse createGetFeeByOrderCodeResponse() {
        return new GetFeeByOrderCodeResponse();
    }

    /**
     * Create an instance of {@link OtherFee }
     * 
     */
    public OtherFee createOtherFee() {
        return new OtherFee();
    }

    /**
     * Create an instance of {@link Countries }
     * 
     */
    public Countries createCountries() {
        return new Countries();
    }

    /**
     * Create an instance of {@link SearchOrderResponse }
     * 
     */
    public SearchOrderResponse createSearchOrderResponse() {
        return new SearchOrderResponse();
    }

    /**
     * Create an instance of {@link OrderInfoArray }
     * 
     */
    public OrderInfoArray createOrderInfoArray() {
        return new OrderInfoArray();
    }

    /**
     * Create an instance of {@link ReSetLabel }
     * 
     */
    public ReSetLabel createReSetLabel() {
        return new ReSetLabel();
    }

    /**
     * Create an instance of {@link ReSetLabelInfoArray }
     * 
     */
    public ReSetLabelInfoArray createReSetLabelInfoArray() {
        return new ReSetLabelInfoArray();
    }

    /**
     * Create an instance of {@link BatchAddOrdersResponse }
     * 
     */
    public BatchAddOrdersResponse createBatchAddOrdersResponse() {
        return new BatchAddOrdersResponse();
    }

    /**
     * Create an instance of {@link AddOrderResponseData }
     * 
     */
    public AddOrderResponseData createAddOrderResponseData() {
        return new AddOrderResponseData();
    }

    /**
     * Create an instance of {@link UpdateOrderStatus }
     * 
     */
    public UpdateOrderStatus createUpdateOrderStatus() {
        return new UpdateOrderStatus();
    }

    /**
     * Create an instance of {@link UpdateOrderStatusInfoArray }
     * 
     */
    public UpdateOrderStatusInfoArray createUpdateOrderStatusInfoArray() {
        return new UpdateOrderStatusInfoArray();
    }

    /**
     * Create an instance of {@link GetRatesByType }
     * 
     */
    public GetRatesByType createGetRatesByType() {
        return new GetRatesByType();
    }

    /**
     * Create an instance of {@link GetRatesByTypeRequestInfo }
     * 
     */
    public GetRatesByTypeRequestInfo createGetRatesByTypeRequestInfo() {
        return new GetRatesByTypeRequestInfo();
    }

    /**
     * Create an instance of {@link GetWarehouseShipTypeRequest }
     * 
     */
    public GetWarehouseShipTypeRequest createGetWarehouseShipTypeRequest() {
        return new GetWarehouseShipTypeRequest();
    }

    /**
     * Create an instance of {@link GetWarehouseShipTypeRequestInfoArray }
     * 
     */
    public GetWarehouseShipTypeRequestInfoArray createGetWarehouseShipTypeRequestInfoArray() {
        return new GetWarehouseShipTypeRequestInfoArray();
    }

    /**
     * Create an instance of {@link Rates }
     * 
     */
    public Rates createRates() {
        return new Rates();
    }

    /**
     * Create an instance of {@link GetCountriesRequest }
     * 
     */
    public GetCountriesRequest createGetCountriesRequest() {
        return new GetCountriesRequest();
    }

    /**
     * Create an instance of {@link UpdateOrder }
     * 
     */
    public UpdateOrder createUpdateOrder() {
        return new UpdateOrder();
    }

    /**
     * Create an instance of {@link GetInventoryRequest }
     * 
     */
    public GetInventoryRequest createGetInventoryRequest() {
        return new GetInventoryRequest();
    }

    /**
     * Create an instance of {@link GetInventoryRequestInfoArray }
     * 
     */
    public GetInventoryRequestInfoArray createGetInventoryRequestInfoArray() {
        return new GetInventoryRequestInfoArray();
    }

    /**
     * Create an instance of {@link ReSetLabelResponse }
     * 
     */
    public ReSetLabelResponse createReSetLabelResponse() {
        return new ReSetLabelResponse();
    }

    /**
     * Create an instance of {@link AddWarehouseOrderRequest }
     * 
     */
    public AddWarehouseOrderRequest createAddWarehouseOrderRequest() {
        return new AddWarehouseOrderRequest();
    }

    /**
     * Create an instance of {@link AddWarehouseOrderRequestInfoArray }
     * 
     */
    public AddWarehouseOrderRequestInfoArray createAddWarehouseOrderRequestInfoArray() {
        return new AddWarehouseOrderRequestInfoArray();
    }

    /**
     * Create an instance of {@link AddOrderResponse }
     * 
     */
    public AddOrderResponse createAddOrderResponse() {
        return new AddOrderResponse();
    }

    /**
     * Create an instance of {@link AddEISOrderResponse }
     * 
     */
    public AddEISOrderResponse createAddEISOrderResponse() {
        return new AddEISOrderResponse();
    }

    /**
     * Create an instance of {@link Rate }
     * 
     */
    public Rate createRate() {
        return new Rate();
    }

    /**
     * Create an instance of {@link GetRateByMode }
     * 
     */
    public GetRateByMode createGetRateByMode() {
        return new GetRateByMode();
    }

    /**
     * Create an instance of {@link GetRateByModeRequestInfo }
     * 
     */
    public GetRateByModeRequestInfo createGetRateByModeRequestInfo() {
        return new GetRateByModeRequestInfo();
    }

    /**
     * Create an instance of {@link GetShipTypesRequest }
     * 
     */
    public GetShipTypesRequest createGetShipTypesRequest() {
        return new GetShipTypesRequest();
    }

    /**
     * Create an instance of {@link GetRates }
     * 
     */
    public GetRates createGetRates() {
        return new GetRates();
    }

    /**
     * Create an instance of {@link GetRatesRequestInfo }
     * 
     */
    public GetRatesRequestInfo createGetRatesRequestInfo() {
        return new GetRatesRequestInfo();
    }

    /**
     * Create an instance of {@link BatchAddOrders }
     * 
     */
    public BatchAddOrders createBatchAddOrders() {
        return new BatchAddOrders();
    }

    /**
     * Create an instance of {@link UpdateOrderVolumeWeightResponse }
     * 
     */
    public UpdateOrderVolumeWeightResponse createUpdateOrderVolumeWeightResponse() {
        return new UpdateOrderVolumeWeightResponse();
    }

    /**
     * Create an instance of {@link GetInventoryResponse }
     * 
     */
    public GetInventoryResponse createGetInventoryResponse() {
        return new GetInventoryResponse();
    }

    /**
     * Create an instance of {@link GetWarehouseRequestType }
     * 
     */
    public GetWarehouseRequestType createGetWarehouseRequestType() {
        return new GetWarehouseRequestType();
    }

    /**
     * Create an instance of {@link AddWarehouseOrderResponse }
     * 
     */
    public AddWarehouseOrderResponse createAddWarehouseOrderResponse() {
        return new AddWarehouseOrderResponse();
    }

    /**
     * Create an instance of {@link GetFeeByOrderCode }
     * 
     */
    public GetFeeByOrderCode createGetFeeByOrderCode() {
        return new GetFeeByOrderCode();
    }

    /**
     * Create an instance of {@link GetClassTypesRequest }
     * 
     */
    public GetClassTypesRequest createGetClassTypesRequest() {
        return new GetClassTypesRequest();
    }

    /**
     * Create an instance of {@link Shiptypes }
     * 
     */
    public Shiptypes createShiptypes() {
        return new Shiptypes();
    }

    /**
     * Create an instance of {@link AddEISOrderReqiest }
     * 
     */
    public AddEISOrderReqiest createAddEISOrderReqiest() {
        return new AddEISOrderReqiest();
    }

    /**
     * Create an instance of {@link AddEISOrderInfo }
     * 
     */
    public AddEISOrderInfo createAddEISOrderInfo() {
        return new AddEISOrderInfo();
    }

    /**
     * Create an instance of {@link UpdateOrderVolumeWeightRequest }
     * 
     */
    public UpdateOrderVolumeWeightRequest createUpdateOrderVolumeWeightRequest() {
        return new UpdateOrderVolumeWeightRequest();
    }

    /**
     * Create an instance of {@link UpdateOrderVolumeWeight }
     * 
     */
    public UpdateOrderVolumeWeight createUpdateOrderVolumeWeight() {
        return new UpdateOrderVolumeWeight();
    }

    /**
     * Create an instance of {@link VolumeWeightArray }
     * 
     */
    public VolumeWeightArray createVolumeWeightArray() {
        return new VolumeWeightArray();
    }

    /**
     * Create an instance of {@link ProductDetailsArray }
     * 
     */
    public ProductDetailsArray createProductDetailsArray() {
        return new ProductDetailsArray();
    }

    /**
     * Create an instance of {@link Warehouse_Type }
     * 
     */
    public Warehouse_Type createWarehouse_Type() {
        return new Warehouse_Type();
    }

    /**
     * Create an instance of {@link EISOrderProducts }
     * 
     */
    public EISOrderProducts createEISOrderProducts() {
        return new EISOrderProducts();
    }

    /**
     * Create an instance of {@link GoodsDetailsArray }
     * 
     */
    public GoodsDetailsArray createGoodsDetailsArray() {
        return new GoodsDetailsArray();
    }

    /**
     * Create an instance of {@link Classname }
     * 
     */
    public Classname createClassname() {
        return new Classname();
    }

    /**
     * Create an instance of {@link Classtype }
     * 
     */
    public Classtype createClasstype() {
        return new Classtype();
    }

    /**
     * Create an instance of {@link Country }
     * 
     */
    public Country createCountry() {
        return new Country();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Classtypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getClassTypesResponse")
    public JAXBElement<Classtypes> createGetClassTypesResponse(Classtypes value) {
        return new JAXBElement<Classtypes>(_GetClassTypesResponse_QNAME, Classtypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetWarehouseRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getWarehouseRequest")
    public JAXBElement<GetWarehouseRequestType> createGetWarehouseRequest(GetWarehouseRequestType value) {
        return new JAXBElement<GetWarehouseRequestType>(_GetWarehouseRequest_QNAME, GetWarehouseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Warehouses }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getWarehouseResponce")
    public JAXBElement<Warehouses> createGetWarehouseResponce(Warehouses value) {
        return new JAXBElement<Warehouses>(_GetWarehouseResponce_QNAME, Warehouses.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getRateByModeResponse")
    public JAXBElement<Rate> createGetRateByModeResponse(Rate value) {
        return new JAXBElement<Rate>(_GetRateByModeResponse_QNAME, Rate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getRatesByTypeResponse")
    public JAXBElement<Rates> createGetRatesByTypeResponse(Rates value) {
        return new JAXBElement<Rates>(_GetRatesByTypeResponse_QNAME, Rates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Shiptypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getShipTypesResponse")
    public JAXBElement<Shiptypes> createGetShipTypesResponse(Shiptypes value) {
        return new JAXBElement<Shiptypes>(_GetShipTypesResponse_QNAME, Shiptypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rates }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "outRates")
    public JAXBElement<Rates> createOutRates(Rates value) {
        return new JAXBElement<Rates>(_OutRates_QNAME, Rates.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Countries }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/ShipRate/", name = "getCountriesResponse")
    public JAXBElement<Countries> createGetCountriesResponse(Countries value) {
        return new JAXBElement<Countries>(_GetCountriesResponse_QNAME, Countries.class, null, value);
    }

}

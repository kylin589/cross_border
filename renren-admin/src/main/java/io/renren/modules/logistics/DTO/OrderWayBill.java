package io.renren.modules.logistics.DTO;

public class OrderWayBill {
   private String CustomerOrderId;
   private String Status;
   private String OrderId;
   private String TrackStatus;
   private String Feedback;
   private String AgentNumber;
   private String WayBillNumber;
   private String SenderInfoStatus;
   private String LabelUrl[];
   private String TrackingNumber;

    public String getCustomerOrderId() {
        return CustomerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        CustomerOrderId = customerOrderId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getTrackStatus() {
        return TrackStatus;
    }

    public void setTrackStatus(String trackStatus) {
        TrackStatus = trackStatus;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public String getAgentNumber() {
        return AgentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        AgentNumber = agentNumber;
    }

    public String getWayBillNumber() {
        return WayBillNumber;
    }

    public void setWayBillNumber(String wayBillNumber) {
        WayBillNumber = wayBillNumber;
    }

    public String getSenderInfoStatus() {
        return SenderInfoStatus;
    }

    public void setSenderInfoStatus(String senderInfoStatus) {
        SenderInfoStatus = senderInfoStatus;
    }

    public String[] getLabelUrl() {
        return LabelUrl;
    }

    public void setLabelUrl(String[] labelUrl) {
        LabelUrl = labelUrl;
    }

    public String getTrackingNumber() {
        return TrackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        TrackingNumber = trackingNumber;
    }
}

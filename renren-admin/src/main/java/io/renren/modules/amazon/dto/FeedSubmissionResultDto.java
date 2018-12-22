package io.renren.modules.amazon.dto;

public class FeedSubmissionResultDto {

    /**
     * 上传id
     */
    private Long uploadId;

    /**
     * xml提交id
     */
    private String feedSubmissionId;

    /**
     * xml提交类型
     */
    private String feedType;

    /**
     * xml处理状态
     */
    private Integer feedProcessingStatus;

    /**
     * xml处理状态
     */
    private String resultXmlPath;

    /**
     * xml内容
     */
    private String xmlContent;

    /**
     * md5校验和
     */
    private String md5Checksum;

    public String getMd5Checksum() {
        return md5Checksum;
    }

    public void setMd5Checksum(String md5Checksum) {
        this.md5Checksum = md5Checksum;
    }

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    public String getFeedSubmissionId() {
        return feedSubmissionId;
    }

    public void setFeedSubmissionId(String feedSubmissionId) {
        this.feedSubmissionId = feedSubmissionId;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Integer getFeedProcessingStatus() {
        return feedProcessingStatus;
    }

    public void setFeedProcessingStatus(Integer feedProcessingStatus) {
        this.feedProcessingStatus = feedProcessingStatus;
    }

    public String getResultXmlPath() {
        return resultXmlPath;
    }

    public void setResultXmlPath(String resultXmlPath) {
        this.resultXmlPath = resultXmlPath;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }
}

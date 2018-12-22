package io.renren.modules.amazon.dto;

/**
 * @author zjrit
 */
public class FeedSubmissionInfoDto {

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
    private String feedProcessingStatus;

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

    public String getFeedProcessingStatus() {
        return feedProcessingStatus;
    }

    public void setFeedProcessingStatus(String feedProcessingStatus) {
        this.feedProcessingStatus = feedProcessingStatus;
    }
}

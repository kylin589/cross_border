package io.renren.modules.amazon.dto;

public class AnalysisFeedSubmissionResultDto {

    private String feedSubmissionId;

    private Integer messagesProcessed;

    private Integer messagesSuccessful;

    private Integer messagesWithError;

    private Integer messagesWithWarning;

    private String MessageContent;

    public String getFeedSubmissionId() {
        return feedSubmissionId;
    }

    public void setFeedSubmissionId(String feedSubmissionId) {
        this.feedSubmissionId = feedSubmissionId;
    }

    public Integer getMessagesProcessed() {
        return messagesProcessed;
    }

    public void setMessagesProcessed(Integer messagesProcessed) {
        this.messagesProcessed = messagesProcessed;
    }

    public Integer getMessagesSuccessful() {
        return messagesSuccessful;
    }

    public void setMessagesSuccessful(Integer messagesSuccessful) {
        this.messagesSuccessful = messagesSuccessful;
    }

    public Integer getMessagesWithError() {
        return messagesWithError;
    }

    public void setMessagesWithError(Integer messagesWithError) {
        this.messagesWithError = messagesWithError;
    }

    public Integer getMessagesWithWarning() {
        return messagesWithWarning;
    }

    public void setMessagesWithWarning(Integer messagesWithWarning) {
        this.messagesWithWarning = messagesWithWarning;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }
}

package io.renren.modules.util;

import com.google.common.annotations.VisibleForTesting;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class Dom4jDemo {


    /**
     * @methodname: main Dom4j解析xml
     * @param: [args]
     * @return: void
     * @auther: jhy
     * @date: 2018/11/14 8:52
     */
    public static void main(String[] args) throws Exception {
        SAXReader sax = new SAXReader();//dom4j解析，SaxReader加载xml文档获得Document
        Document document = sax.read("C:\\Users\\asus\\Desktop\\ProductsFeedSubmissionResult.xml");//获取到文档对象
        Element elemRoot = document.getRootElement();//获取到跟元素
        Element header = elemRoot.element("Header");
        Element merchantIdentfier = header.element("MerchantIdentifier");
        //获取标签的名字
        String merchantIdentfierName = merchantIdentfier.getName();
        //获取标签体的内容
        String merchantIdentfierText = merchantIdentfier.getTextTrim();
        System.out.println(merchantIdentfierName + ":" + merchantIdentfierText);
        Element message = elemRoot.element("Message");
        Element messageID = message.element("MessageID");
        //获取标签的名字
        String messageIDName = messageID.getName();
        //获取标签体的内容
        String messageIDText = messageID.getTextTrim();
        System.out.println(messageIDName + ":" + messageIDText);

        Element processingReport = message.element("ProcessingReport");
        Element documentTransactionID = processingReport.element("DocumentTransactionID");
        //获取标签的名字
        String documentTransactionIDName = documentTransactionID.getName();
        //获取标签体的内容
        String documentTransactionIDText = documentTransactionID.getTextTrim();
        System.out.println(documentTransactionIDName + ":" + documentTransactionIDText);
        Element statusCode = processingReport.element("StatusCode");
        //获取标签的名字
        String statusCodeName = statusCode.getName();
        //获取标签体的内容
        String statusCodeText = statusCode.getTextTrim();
        System.out.println(statusCodeName + ":" + statusCodeText);

        Element processingSummary = processingReport.element("ProcessingSummary");
        Element messagesProcessed = processingSummary.element("MessagesProcessed");
        //获取标签的名字
        String messagesProcessedName = messagesProcessed.getName();
        //获取标签体的内容
        String messagesProcessedText = messagesProcessed.getTextTrim();
        System.out.println(messagesProcessedName + ":" + messagesProcessedText);
        Element messagesSuccessful = processingSummary.element("MessagesSuccessful");
        //获取标签的名字
        String messagesSuccessfulName = messagesSuccessful.getName();
        //获取标签体的内容
        String messagesSuccessfulText = messagesSuccessful.getTextTrim();
        System.out.println(messagesSuccessfulName + ":" + messagesSuccessfulText);
        Element messagesWithError = processingSummary.element("MessagesWithError");
        //获取标签的名字
        String messagesWithErrorName = messagesWithError.getName();
        //获取标签体的内容
        String messagesWithErrorText = messagesWithError.getTextTrim();
        System.out.println(messagesWithErrorName + ":" + messagesWithErrorText);
        Element messagesWithWarning = processingSummary.element("MessagesWithWarning");
        //获取标签的名字
        String messagesWithWarningName = messagesWithWarning.getName();
        //获取标签体的内容
        String messagesWithWarningText = messagesWithWarning.getTextTrim();
        System.out.println(messagesWithWarningName + ":" + messagesWithWarningText);
        if (messagesWithErrorText .equals("0")) {
            System.out.println("没有错误");
        }else {
            //获取Result标签的结果集
            List<Element> result = processingReport.elements("Result");
            for (Element element : result) {
                Element resultCode=element.element("ResultCode");
                String resultCodText= resultCode.getTextTrim();

                List<Element> elemList = element.elements();
                if (resultCodText.equals("Error"))
                    for (Element ele : elemList) {
                        String eleName = ele.getName();
                        String eleText = ele.getTextTrim();
                        System.out.println(eleName + ":" + eleText);
                    }
            }

        }

        if (messagesWithWarningText.equals("0")) {
            System.out.println("没有警告");
        }else {
            //获取Result标签的结果集
            List<Element> result = processingReport.elements("Result");
            for (Element element : result) {
                Element resultCode=element.element("ResultCode");
                String resultCodText= resultCode.getTextTrim();
                List<Element> elemList = element.elements();
                if (resultCodText.equals("Warning"))
                    for (Element ele : elemList) {
                        String eleName = ele.getName();
                        String eleText = ele.getTextTrim();
                        System.out.println(eleName + ":" + eleText);
                    }
            }
        }
    }
}


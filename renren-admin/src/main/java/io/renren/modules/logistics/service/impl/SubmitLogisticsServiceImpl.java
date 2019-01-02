package io.renren.modules.logistics.service.impl;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import io.renren.modules.amazon.util.ContentMD5Util;
import io.renren.modules.logistics.service.SubmitLogisticsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:30
 * @Description:
 */

@Service
@Component
public class SubmitLogisticsServiceImpl implements SubmitLogisticsService{
    // TODO: 2018/12/27 步骤一：同步上传数据
    @Override
    public String submitFeed(String serviceURL, String merchantId, String sellerDevAuthToken, String feedType, String filePath) {

        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws", "1.1", config);

        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(sellerDevAuthToken);
        request.setFeedType(feedType);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String md5 = "";
        try {
            md5 = ContentMD5Util.computeContentMD5HeaderValue(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setContentMD5(md5);
        request.setFeedContent(fileInputStream);
        return invokeSubmitFeed(service, request);
    }
    // TODO: 2018/12/27 同步上传数据 发送请求，获得响应
    public static String  invokeSubmitFeed(MarketplaceWebService service,SubmitFeedRequest request)
    {
        try {
            SubmitFeedResponse response = service.submitFeed(request);
            System.out.println("SubmitFeed Action Response");
            System.out.print("    SubmitFeedResponse");
            if (response.isSetSubmitFeedResult()) {
                System.out.print("        SubmitFeedResult");
                SubmitFeedResult submitFeedResult = response
                        .getSubmitFeedResult();
                if (submitFeedResult.isSetFeedSubmissionInfo()) {
                    System.out.print("            FeedSubmissionInfo");
                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult
                            .getFeedSubmissionInfo();
                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                        System.out.print("                FeedSubmissionId");
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedSubmissionId());
                        return feedSubmissionInfo.getFeedSubmissionId();
                    }
                    if (feedSubmissionInfo.isSetFeedType()) {
                        System.out.print("                FeedType");
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedType());
                    }
                    if (feedSubmissionInfo.isSetSubmittedDate()) {
                        System.out.print("                SubmittedDate");
                        System.out.print("                    "
                                + feedSubmissionInfo.getSubmittedDate());
                    }
                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                        System.out
                                .print("                FeedProcessingStatus");
                        System.out.print("                    "
                                + feedSubmissionInfo.getFeedProcessingStatus());
                    }
                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
                        System.out
                                .print("                StartedProcessingDate");
                        System.out
                                .print("                    "
                                        + feedSubmissionInfo
                                        .getStartedProcessingDate());
                    }
                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
                        System.out
                                .print("                CompletedProcessingDate");
                        System.out.print("                    "
                                + feedSubmissionInfo
                                .getCompletedProcessingDate());
                    }
                }
            }
            if (response.isSetResponseMetadata()) {
                System.out.print("        ResponseMetadata");
                ResponseMetadata responseMetadata = response
                        .getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    System.out.print("            RequestId");
                    System.out.print("                "
                            + responseMetadata.getRequestId());
                }
            }
            System.out.println(response.getResponseHeaderMetadata());
        } catch (MarketplaceWebServiceException ex) {
            System.out.print("XML: " + ex.getXML());
        }
        return null;
    }


    // TODO: 2018/12/27 步骤二：得到上传数据列表
    @Override
    public List<String> getFeedSubmissionList(String serviceURL, String merchantId, String sellerDevAuthToken, String feedSubmissionId) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws", "1.1", config);
        GetFeedSubmissionListRequest request = new GetFeedSubmissionListRequest();
        List<GetFeedSubmissionListRequest> requests = new ArrayList<GetFeedSubmissionListRequest>();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(sellerDevAuthToken);
        requests.add(request);
        return invokeGetFeedSubmissionList(service, requests);

    }
    // TODO: 2018/12/27 得到上传数据列表 发送请求，获得响应
    public List<String> invokeGetFeedSubmissionList(MarketplaceWebService service, List<GetFeedSubmissionListRequest> requests) {
        List<String> feedSubmissionIds = new ArrayList<>();
        List<Future<GetFeedSubmissionListResponse>> responses = new ArrayList<Future<GetFeedSubmissionListResponse>>();
        for (GetFeedSubmissionListRequest request : requests) {
            responses.add(service.getFeedSubmissionListAsync(request));
        }
        for (Future<GetFeedSubmissionListResponse> future : responses) {
            while (!future.isDone()) {
                try {
                    Thread.sleep(2*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                GetFeedSubmissionListResponse response = future.get();
                if (response.isSetGetFeedSubmissionListResult()) {
                    List<FeedSubmissionInfo> feedSubmissionInfoList = response.getGetFeedSubmissionListResult().getFeedSubmissionInfoList();
                    for (FeedSubmissionInfo feedSubmissionInfo : feedSubmissionInfoList) {
                        feedSubmissionIds.add(feedSubmissionInfo.getFeedSubmissionId());
                    }
                }
            } catch (Exception e) {
                if (e.getCause() instanceof MarketplaceWebServiceException) {
                    MarketplaceWebServiceException exception = MarketplaceWebServiceException.class.cast(e.getCause());
                    System.out.print("XML: " + exception.getXML());
                } else {
                    e.printStackTrace();
                }
            }
        }
        return feedSubmissionIds;
    }


    //步骤三： 得到返回结果
    @Override
    public void getFeedSubmissionResult(String serviceURL, String merchantId, String sellerDevAuthToken, String feedSubmissionId) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws", "1.1", config);
        /*List<GetFeedSubmissionResultRequest>    requests=new ArrayList<GetFeedSubmissionResultRequest>();*/
        GetFeedSubmissionResultRequest request = new GetFeedSubmissionResultRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(sellerDevAuthToken);
        request.setFeedSubmissionId(feedSubmissionId);
        try {
            OutputStream processingResult = new FileOutputStream( "feedSubmissionResult.xml" );
            request.setFeedSubmissionResultOutputStream( processingResult );
        }catch (FileNotFoundException e){
            e.getStackTrace();
        }
/*
        requests.add(request);
*/
        String md5 = null;
        while(md5 == null){
            md5 = invokeGetFeedSubmissionResult(service, request);
            System.out.println("响应结果:=============================="+md5);
        }
    }

    public static String invokeGetFeedSubmissionResult(MarketplaceWebService service, GetFeedSubmissionResultRequest request) {
        String md5 = null;
        try {
            GetFeedSubmissionResultResponse response = service.getFeedSubmissionResult(request);
            System.out.println("=============================xml格式数据："+response.toXML());
            System.out.println ("GetFeedSubmissionResult Action Response");
            System.out.print("    GetFeedSubmissionResultResponse");
            System.out.println();
            System.out.print("    GetFeedSubmissionResultResult");
            System.out.println();
            System.out.print("            MD5Checksum");
            System.out.println();
            System.out.print(" 成功：               " + response.getGetFeedSubmissionResultResult().getMD5Checksum());
            md5 = response.getGetFeedSubmissionResultResult().getMD5Checksum();
            System.out.println();
            if (response.isSetResponseMetadata()) {
                System.out.print("        ResponseMetadata");
                System.out.println();
                ResponseMetadata  responseMetadata = response.getResponseMetadata();
                if (responseMetadata.isSetRequestId()) {
                    System.out.print("            RequestId");
                    System.out.println();
                    System.out.print("                " + responseMetadata.getRequestId());
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("Feed Processing Result");
            System.out.println( request.getFeedSubmissionResultOutputStream().toString() );
            System.out.println(response.getResponseHeaderMetadata());
        } catch (MarketplaceWebServiceException ex) {
            System.out.print("XML: " + ex.getXML());

        }
        return md5;
    }

}

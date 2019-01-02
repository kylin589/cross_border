package io.renren.modules.logistics.service;

import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:29
 * @Description:
 */
public interface SubmitLogisticsService {
    // TODO: 2018/12/27 同步上传数据
    String  submitFeed(String serviceURL,String merchantId,String sellerDevAuthToken,String feedType,String filePath);

    // TODO: 2018/12/27 得到数据上传列表
    List<String> getFeedSubmissionList(String serviceURL, String merchantId, String sellerDevAuthToken, String feedSubmissionId);

    // TODO: 2018/12/27 得到返回结果
    void getFeedSubmissionResult(String serviceURL,String merchantId,String sellerDevAuthToken,String feedSubmissionId);

}

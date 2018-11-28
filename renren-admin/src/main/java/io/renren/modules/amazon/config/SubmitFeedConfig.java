package io.renren.modules.amazon.config;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import org.springframework.beans.factory.annotation.Value;

public class SubmitFeedConfig {

    @Value(("${mws-config.access-key}"))
    private static String accessKey;

    @Value(("${mws-config.secret-key}"))
    private static String secretKey;

    @Value(("${mws-config.app-name}"))
    private static String appName;

    @Value(("${mws-config.app-version}"))
    private static String appVersion;

    /**
     * The endpoint for region service and version.
     * ex: serviceURL = MWSEndpoint.NA_PROD.toString();
     */
    // private String serviceURL = "https://mws.amazonservices.co.uk";

    /**
     * The client, lazy initialized. Async client is also a sync client.
     */
    private static MarketplaceWebService service = null;


    public static synchronized MarketplaceWebService getAsyncService(String serviceURL) {
        if (service == null) {
            MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
            config.setServiceURL(serviceURL);
            config.setMaxAsyncThreads(35);
            service = new MarketplaceWebServiceClient(accessKey, secretKey,
                    appName, appVersion, config);
        }
        return service;
    }

}

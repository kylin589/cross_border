/*******************************************************************************
 * Copyright 2009-2018 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * Marketplace Web Service Orders
 * API Version: 2013-09-01
 * Library Version: 2018-08-01
 * Generated: Wed Aug 29 10:45:06 PDT 2018
 */
package io.renren.modules.amazon.config;

import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersAsyncClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersClient;
import com.amazonservices.mws.orders._2013_09_01.MarketplaceWebServiceOrdersConfig;
import com.amazonservices.mws.sellers.MWSEndpoint;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration for MarketplaceWebServiceOrders samples.
 */
public class MarketplaceWebServiceOrdersSampleConfig {

    /** Developer AWS access key. */
    @Value(("${mws-config.access-key}"))
    private static String accessKey;

    /** Developer AWS secret key. */
    @Value(("${mws-config.secret-key}"))
    private static String secretKey;

    /** The client application name. */
    @Value(("${mws-config.app-name}"))
    private static String appName;

    /** The client application version. */
    @Value(("${mws-config.app-version}"))
    private static String appVersion;

    /**
     * The endpoint for region service and version.
     * ex: serviceURL = MWSEndpoint.NA_PROD.toString();
     */
    // private String serviceURL = "https://mws.amazonservices.co.uk";

    /** The client, lazy initialized. Async client is also a sync client. */
    private static MarketplaceWebServiceOrdersAsyncClient client = null;


    public static synchronized MarketplaceWebServiceOrdersAsyncClient getAsyncClient(String serviceURL) {
        if (client==null) {
            MarketplaceWebServiceOrdersConfig config = new MarketplaceWebServiceOrdersConfig();
            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            client = new MarketplaceWebServiceOrdersAsyncClient(accessKey, secretKey,
                    appName, appVersion, config, null);
        }
        return client;
    }

}

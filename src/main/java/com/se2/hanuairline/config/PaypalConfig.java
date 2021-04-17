package com.se2.hanuairline.config;

import java.util.HashMap;
import java.util.Map;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class PaypalConfig {

//    @Value("${paypal.client.id}")
    private String clientId="AYGF6pNoBh5rMVrDD6NGSEjzdg5aGlLxzjyhkM-rze56Dc6SuG1J1iudtVIwfE2Xiykl_ktoEdCT_gDS";
//    @Value("${paypal.client.secret}")
    private String clientSecret="EFaI-RqCEOZtiUeu3a8RYfMxkL4d18Xmgb-OD8lztZGmLWhbrJWiZImiD1ewAfj5gQOxCzptQ0RZ4xL1";
//    @Value("${paypal.mode}")
    private String mode="sandbox";

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential(){
        return new OAuthTokenCredential(clientId,clientSecret,paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }


}

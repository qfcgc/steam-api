package com.steamext.steam.api.logic.client.http;

import com.steamext.steam.api.logic.PropertiesUtils;
import com.steamext.steam.api.logic.TestConfig;
import com.steamext.steam.api.logic.client.http.parser.MarketPageHTMLParser;
import com.steamext.steam.api.logic.client.http.parser.UserInfoSteamHTMLParser;
import com.steamext.steam.api.logic.client.http.request.GetMarketPageSteamHttpRequest;
import com.steamext.steam.api.logic.client.http.request.GetRsaKeySteamHttpRequest;
import com.steamext.steam.api.logic.client.http.request.GetUserInfoSteamHttpRequest;
import com.steamext.steam.api.logic.client.http.request.LoginSteamHttpRequest;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.model.requestmodel.UserCredentials;
import com.steamext.steam.api.model.requestmodel.UserLoginInfo;
import com.steamext.steam.api.model.requestmodel.UserPageInfo;
import com.steamext.steam.api.model.responsemodel.RsaDataContainer;
import com.steamext.steam.api.model.responsemodel.StartMarketPageResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SteamHttpClientIT {
    private SteamHttpClient client;
    private UserLoginInfo loginInfo;

    @Autowired
    private PropertiesUtils properties;

    @BeforeAll
    public void init() {
        client = new SteamHttpClient();
    }

    @Test
    @Order(1)
    public void testGettingRsaKey() throws SteamHttpClientException {
        RsaDataContainer data = client.execute(new GetRsaKeySteamHttpRequest(
                        (String) properties.getData().get("it.username")).getRequest(),
                RsaDataContainer.class);

        assertNotNull(data);
        assertNotNull(data.getPublicKeyMod());
        assertNotNull(data.getPublicKeyExp());
        assertNotNull(data.getTimestamp());
        assertNotNull(data.getTokenGId());
    }

    @Test
    @Order(2)
    public void testLogin() throws SteamHttpClientException {
        UserCredentials credentials = new UserCredentials(
                (String) properties.getData().get("it.username"),
                (String) properties.getData().get("it.password"));

        RsaDataContainer rsaDataContainer =
                client.execute(new GetRsaKeySteamHttpRequest(credentials.getUsername()));
        loginInfo = client.execute(new LoginSteamHttpRequest(credentials, rsaDataContainer));

        assertNotNull(loginInfo);
        assertTrue(loginInfo.isLoginComplete());
        assertTrue(loginInfo.isSuccess());
        assertNotNull(loginInfo.getTransferParameters());
        assertNotNull(loginInfo.getTransferParameters().getAuth());
        assertNotNull(loginInfo.getTransferParameters().getSteamId());
        assertNotNull(loginInfo.getTransferParameters().getTokenSecure());
    }

    @Test
    @Order(3)
    public void testGettingUserInfo() throws SteamHttpClientException {
        UserPageInfo userInfo = client.execute(new GetUserInfoSteamHttpRequest(
                loginInfo.getTransferParameters().getSteamId()), new UserInfoSteamHTMLParser<>());

        assertNotNull(userInfo);
    }

    @Test
    @Order(4)
    public void testGettingMarketPage() throws SteamHttpClientException {
        StartMarketPageResponse response = client.execute(new GetMarketPageSteamHttpRequest(),
                new MarketPageHTMLParser());


        assertNotNull(response);
//        assertTrue(marketPage.contains((String) properties.getData().get("it.username")));
    }
}

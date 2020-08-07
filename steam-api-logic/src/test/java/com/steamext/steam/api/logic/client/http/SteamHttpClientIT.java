package com.steamext.steam.api.logic.client.http;

import com.steamext.steam.api.logic.PropertiesUtils;
import com.steamext.steam.api.logic.TestConfig;
import com.steamext.steam.api.logic.client.http.parser.MarketPageHTMLParser;
import com.steamext.steam.api.logic.client.http.parser.TradeElementPageHTMLParser;
import com.steamext.steam.api.logic.client.http.parser.UserInfoSteamHTMLParser;
import com.steamext.steam.api.logic.client.http.request.*;
import com.steamext.steam.api.logic.entry.SteamGuardCodeProvider;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.logic.model.requestmodel.UserCredentials;
import com.steamext.steam.api.logic.model.responsemodel.*;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.JsonTradeElementsContainer;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElement;
import com.steamext.steam.api.model.requestmodel.UserPageInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SteamHttpClientIT {
    private SteamHttpClient client;
    private UserLoginInfo loginInfo;

    @Autowired
    private PropertiesUtils properties;

    @Autowired(required = false)
    private SteamGuardCodeProvider steamGuardCodeProvider;

    @BeforeAll
    public void init() {
        client = SteamHttpClient.builder().build();
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

    /**
     * Execute login to steam account.
     * If account has steam guard you can change the value of steam guard in debug.
     */
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

        if (!loginInfo.isSuccess()) {
            String steamGuardCode = "_MOCK"; //Use debug to insert real value
            if (steamGuardCodeProvider.getSteamGuardCode() != null) {
                //it is not supposed to be executed in test
                steamGuardCode = steamGuardCodeProvider.getSteamGuardCode();
            }
            credentials = new UserCredentials(
                    credentials.getUsername(),
                    credentials.getPassword(),
                    steamGuardCode,
                    loginInfo.getEmailSteamId());

            rsaDataContainer =
                    client.execute(new GetRsaKeySteamHttpRequest(credentials.getUsername()));
            loginInfo = client.execute(new LoginSteamHttpRequest(credentials, rsaDataContainer));
        }

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

    /**
     * Test for getting start market page and extraction info from it.
     */
    @Test
    @Order(3)
    public void testGettingMarketPage() throws SteamHttpClientException {
        StartMarketPageResponse response = client.execute(new GetMarketPageSteamHttpRequest(),
                new MarketPageHTMLParser());


        assertNotNull(response, "client must return non-null response value");
        if (response.getRestrictions() != null) {
            validateRestrictions(response.getRestrictions());
        }
    }

    /**
     * Test for getting trade elements for existing game.
     */
    @Test
    @Order(3)
    public void testGettingTradeElementsByAppId() throws SteamHttpClientException {
        String csGoAppId = "730";
        JsonTradeElementsContainer response = client.execute(
                new GetPageWithNTradeElementsByAppIdSteamHttpRequest(csGoAppId));

        assertNotNull(response);
        assertNotEquals(0, response.getTotalCount());
        assertNotNull(response.getTradeElements());
        assertNotEquals(0, response.getTradeElements().size());
        assertNotNull(response.getSearchData());
        assertNotEquals(0, response.getSearchData().getTotalCount());
    }

    /**
     * Test for getting trade elements for non-existing game.
     */
    @Test
    @Order(3)
    public void testGettingTradeElementsByNotExistingAppId() throws SteamHttpClientException {
        String noExistingApp = "1";
        JsonTradeElementsContainer response = client.execute(
                new GetPageWithNTradeElementsByAppIdSteamHttpRequest(noExistingApp));

        assertNotNull(response);
        assertEquals(0, response.getTotalCount());
        assertNotNull(response.getTradeElements());
        assertEquals(0, response.getTradeElements().size());
        assertNotNull(response.getSearchData());
        assertEquals(0, response.getSearchData().getTotalCount());
    }

    @Test
    @Order(3)
    public void testGettingTradeElement() throws SteamHttpClientException {
        int gameId = 753;
        String tradeElementId = "570-Bounty%20Hunter";
        ParsedTradeElement response = client.execute(
                new GetTradeElementSteamHttpRequest(tradeElementId, gameId),
                new TradeElementPageHTMLParser());

        assertNotNull(response);
        assertNotNull(response.getItemName());
        assertEquals("Bounty Hunter", response.getItemName());
    }

    /**
     * Market restriction must contain at least one field filled.
     * It will fail test if any of market restriction has all field empty (null).
     *
     * @param restrictions market restrictions object
     */
    private void validateRestrictions(MarketRestrictions restrictions) {
        for (MarketRestriction restriction : restrictions.getRestrictions()) {
            assertFalse(
                    restriction.getRestrictionHelpLink() == null
                            && restriction.getRestrictionText() == null,
                    "Market restriction must contain at least one non-null field");
        }
    }
}

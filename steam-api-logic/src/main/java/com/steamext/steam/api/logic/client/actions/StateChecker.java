package com.steamext.steam.api.logic.client.actions;

import org.springframework.stereotype.Service;

@Service
public class StateChecker {
    public boolean isLogin(String page) {
        String LOGIN_REGEXP = "<a class=\"global_action_link\" href=\"https://steamcommunity.com/login" +
                "/home/?goto=login%2Fhome%2F%3Fgoto%3D\">login</a>";
        return !page.contains(LOGIN_REGEXP);
    }
}

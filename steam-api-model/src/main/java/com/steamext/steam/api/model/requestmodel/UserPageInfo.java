package com.steamext.steam.api.model.requestmodel;

import lombok.Data;

@Data
public class UserPageInfo {
    private String username;
    private String realname;
    private String address;
    private int level;
}

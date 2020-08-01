package com.steamext.steam.api.model.requestmodel;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageInfo implements Serializable {
    private String username;
    private String realname;
    private String address;
    private int level;
}

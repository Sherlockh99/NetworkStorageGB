package org.sherlock.netty.server;

import lombok.Getter;
import org.sherlock.netty.common.dto.BasicRequest;

@Getter
public class User implements BasicRequest {
    private final String login;
    private final String password;

    private boolean authorization;

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    @Override
    public String getType() {
        return "user";
    }

    public void setAuthorization(boolean authorization) {
        this.authorization = authorization;
    }
}

package org.sherlock.netty.common.dto;


public class AuthRequest implements BasicRequest {

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
    public Boolean getResult() {
        return result;
    }
    private Boolean result;

    public AuthRequest(String login, String password) {
        this.login = login;
        this.password = password;

    }

    @Override
    public String getType() {
        return "auth";
    }

}

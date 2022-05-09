package org.sherlock.netty.server.autorization;

import org.sherlock.netty.server.User;

public class DBAuthService implements AuthService{

    @Override
    public boolean checkedLogin(String login, String password) {
        return SQLHandler.checkedLogin(login, password);
    }

    /*
    @Override
    public boolean registration(String login, String password, String nickname) {
        return SQLHandler.registration(login, password, nickname);
    }

    @Override
    public boolean changeNick(String oldNickName, String newNickname) {
        return SQLHandler.changeNick(oldNickName, newNickname);
    }
*/

}

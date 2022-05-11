package org.sherlock.netty.server.autorization;

public class DBAuthService implements AuthService{

    @Override
    public boolean isCurrentLogin(String login, String password) {
        return SQLHandler.isCurrentLogin(login, password);
    }


    @Override
    public boolean registration(String login, String password) {
        return SQLHandler.registration(login, password);
    }

    /*
    @Override
    public boolean changeNick(String oldNickName, String newNickname) {
        return SQLHandler.changeNick(oldNickName, newNickname);
    }
*/

}

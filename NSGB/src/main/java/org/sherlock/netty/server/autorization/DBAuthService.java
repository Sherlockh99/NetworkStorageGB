package org.sherlock.netty.server.autorization;

import org.sherlock.netty.server.User;

public class DBAuthService implements AuthService{

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
         if (SQLHandler.checkedLogin(login, password)){
             return new User(login,password);
         }
        return null;
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

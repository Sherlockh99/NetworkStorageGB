package org.sherlock.netty.server.autorization;

import org.sherlock.netty.common.dto.AuthRequest;
import org.sherlock.netty.server.User;

public interface AuthService {
    /**
     * Получение никнейма по логину и паролю
     * возвращает никнейм если учетка есть
     * null если пары логин пароль не нашлось
     * */
     User getUserByLoginAndPassword(String login, String password);
    /**
     * Регистрация нового пользователя
     * при успешной регистрации ( логин и никнейм не заняты ) вернет true
     * иначе вернет false
     * */
    //boolean registration(String login, String password, String nickname);

    /*
    boolean changeNick(String oldNickname, String newNickname);
    default void disconnect() {
    }
     */

}

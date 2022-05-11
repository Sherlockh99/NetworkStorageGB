package org.sherlock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sherlock.netty.ControllerRegistry;
import org.sherlock.netty.client.Network;
import org.sherlock.netty.server.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private User user;

    @FXML
    TextField loginField;

    @FXML
    public PasswordField passwordField;

    @FXML
    private Label badLogin;
    @FXML
    private Label busyLogin;

    private final Network network = Network.getInstance();

    @FXML
    public void onExitButtonClick(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
    }
    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) throws InterruptedException{
        autorization(false);
    }

    @FXML
    public void onRegistrationButtonClick(ActionEvent actionEvent) throws InterruptedException{
        autorization(true);
    }

    public void autorization(boolean registration) throws InterruptedException{
        String log = loginField.getText();

        if (log == null || log.isEmpty() || log.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не указали логин", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String pass = passwordField.getText();
        if (pass == null || pass.isEmpty() || pass.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не указан пароль", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        user = new User(log, pass,registration);
        network.sendRequest(user);

    }

    public void setVisibleBadLogin(){
        badLogin.setVisible(true);
        busyLogin.setVisible(false);
    }

    public void setVisibleBusyRegistration(){
        badLogin.setVisible(false);
        busyLogin.setVisible(true);
    }

    public void closeNetwork(){
        network.close();
    }

}
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


    private final Network network = Network.getInstance();
    private User user;

    @FXML
    TextField loginField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Label badLogin;

    @FXML
    public void btnExitAction(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
    }
    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException{

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

        user = new User(log,pass);
        network.sendRequest(user);
    }

    public void onRegistrationButtonClick(ActionEvent actionEvent) {
        badLogin.setVisible(true);
    }

    public void setVisibleBadLogin(){
        badLogin.setVisible(true);
    }
}
package org.sherlock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.sherlock.netty.ControllerRegistry;
import org.sherlock.netty.client.Network;
import org.sherlock.netty.common.dto.AuthRequest;
import org.sherlock.netty.common.dto.BasicRequest;
import org.sherlock.netty.server.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {


    private final Network network = Network.getInstance();
    private User user;

    @FXML
    TextField login, password;

    @FXML
    public void btnExitAction(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }

    @FXML
    private void btnAuth() throws IOException, InterruptedException {
        String log = login.getText();
        String pass = password.getText();
        if (log == null || log.isEmpty() || log.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не указали логин", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        user = new User(log,pass);
        network.sendRequest(user);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
    }

}
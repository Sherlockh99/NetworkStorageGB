package org.sherlock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {


    //private final Network network = Network.getInstance();
    @FXML
    TextField login, password;

    @FXML
    public void btnExitAction(ActionEvent actionEvent) {
        //network.close();
        Platform.exit();
    }

    @FXML
    private void btnAuth() throws IOException, InterruptedException {
        /*

        String log = login.getText();
        String pass = password.getText();
        if (log == null || log.isEmpty() || log.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не указали логин", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        BasicRequest request = new AuthRequest(log, pass);
        network.sendRequest(request);

         */
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ControllerRegistry.register(this);
    }

}
package org.sherlock.netty.client;

import org.sherlock.App;
import org.sherlock.ServerPanelController;
import org.sherlock.netty.ControllerRegistry;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClientService {

    public void loginSuccessful() {
        try {
            App.setRoot("secondary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putServerFileList(List<File> serverItemsList) {
        ServerPanelController controllerObject =
                (ServerPanelController) ControllerRegistry.getControllerObject(ServerPanelController.class);
        controllerObject.renderServerFileList(serverItemsList);
    }

}
package org.sherlock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import org.sherlock.netty.ControllerRegistry;
import org.sherlock.netty.client.Network;
import org.sherlock.netty.common.Enums;
import org.sherlock.netty.common.dto.PartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.sherlock.netty.client.Network.MB_20;


public class MainController implements Initializable {

    @FXML
    VBox leftPanel, rightPanel;

    private Network network;

    public void btnExitAction(ActionEvent actionEvent) {

//        LoginController controllerObject =
//                (LoginController) ControllerRegistry.getControllerObject(LoginController.class);
//        controllerObject.closeNetwork();

        network.close();
        Platform.exit();
    }

    public void copyBthAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        ServerPanelController rightPC = (ServerPanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename()==null && rightPC.getSelectedFilename()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Do not choose file", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(leftPC.getSelectedFilename()!=null){
            copyFileToServer(leftPC);
        }else{
            System.out.println("Copy file from Server to PC");
        }

        /*
        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFilename()!=null){
            srcPC = leftPC;
            dstPC = rightPC;
        }

        if(rightPC.getSelectedFilename()!=null){
            srcPC = rightPC;
            dstPC = leftPC;
        }



        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());
        //copyFiles(srcPath,dstPath);
        //dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        */
    }

    private void copyFiles(Path srcPath,Path dstPath){
        try {
            Files.copy(srcPath, dstPath);
            if(srcPath.toFile().isDirectory()){
                try (DirectoryStream<Path> files = Files.newDirectoryStream(srcPath)) {
                    for (Path path : files) {
                        copyFiles(path,dstPath.resolve(path.getFileName().toString()));
                    }
                }
            }
            /*
            Files.walkFileTree(srcPath,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    return super.visitFile(file, attrs);
                }
            });*/

        } catch (IOException e) {
            //throw new RuntimeException(e);
            Alert alert = new Alert(Alert.AlertType.ERROR,"Не удалось скопировать указанный файл", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);

        LoginController controllerObject =
                (LoginController) ControllerRegistry.getControllerObject(LoginController.class);
        network = controllerObject.getNetwork();

    }

    private void copyFileToServer(PanelController panelController){
        System.out.println("Copy file from PC to Server");
        Path srcPath = Paths.get(panelController.getCurrentPath(),panelController.getSelectedFilename());
        saw(srcPath, this::sendDataToServer);
    }

    private void sendDataToServer(byte[] bytes){

        try {
            PartFile partFile = new PartFile(bytes);
            network.sendRequest(partFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //PartFile partFile = new PartFile(bytes);

    }

    private void saw(Path path, Consumer<byte[]> filePartConsumer){
        byte[] filePart = new byte[MB_20];
        try(FileInputStream fileInputStream = new FileInputStream(path.toFile())){
            while (fileInputStream.read(filePart) != -1){
                filePartConsumer.accept(filePart);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
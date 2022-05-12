package org.sherlock;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.sherlock.netty.ControllerRegistry;
import org.sherlock.netty.client.Network;
import org.sherlock.netty.common.Enums;
import org.sherlock.netty.common.dto.GetFileListRequest;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerPanelController implements Initializable {

    @FXML
    //TableView<FileInfoServer> filesTableR;
    TableView<FileInfo> filesTableR;

    @FXML
    TextField pathFieldR;

    private Network network;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LoginController controllerObject =
                (LoginController) ControllerRegistry.getControllerObject(LoginController.class);
        network = controllerObject.getNetwork();

        ControllerRegistry.register(this);

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> filenameColumn = new TableColumn<>("Имя");
        filenameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        filenameColumn.setPrefWidth(240);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(120);

        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>(){
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item==null || empty){
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if(item==-1){
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });

        fileSizeColumn.setPrefWidth(120);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Дата изменения");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);

        filesTableR.getColumns().addAll(fileTypeColumn, filenameColumn, fileSizeColumn, fileDateColumn);
        filesTableR.getSortOrder().add(fileTypeColumn);

        filesTableR.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if(filesTableR.getSelectionModel().getSelectedItem().getType()== FileInfo.FileType.DIRECTORY){
                        sendRequestListFileServer(filesTableR.getSelectionModel().getSelectedItem().getFileName());
                    }
                }
            }
        });
    }

    private void sendRequestListFileServer(String fileName){
        GetFileListRequest getFileListRequest = new GetFileListRequest(fileName);
        try {
            network.sendRequest(getFileListRequest);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateServerList(Path path, List<File> serverItemsList) {
        filesTableR.getItems().clear();
        List<FileInfo> serverFileList = serverItemsList.stream()
                .map(File::toPath)
                .map(FileInfo::new)
                .collect(Collectors.toList());
        filesTableR.getItems().addAll(serverFileList);
        filesTableR.sort();
    }



    public void btnPathUpActionR(ActionEvent actionEvent) {
        try {
            network.sendRequest(Enums.LEVEL_UP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void renderServerFileList(List<File> serverItemsList, String actualDirectory) {
        pathFieldR.setText(actualDirectory);
        updateServerList(Paths.get(".", "root-dir"), serverItemsList);

    }

}
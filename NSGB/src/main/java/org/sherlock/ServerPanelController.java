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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerPanelController implements Initializable {

    @FXML
    TableView<FileInfo> filesTable;

    @FXML
    TextField pathField;

    private Network network;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ControllerRegistry.register(this);
        LoginController controllerObject =
                (LoginController) ControllerRegistry.getControllerObject(LoginController.class);
        network = controllerObject.getNetwork();

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

        filesTable.getColumns().addAll(fileTypeColumn, filenameColumn, fileSizeColumn, fileDateColumn);
        filesTable.getSortOrder().add(fileTypeColumn);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if(filesTable.getSelectionModel().getSelectedItem().getType()== FileInfo.FileType.DIRECTORY){
                        sendRequestListFileServer(filesTable.getSelectionModel().getSelectedItem().getFileName());
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

    public void updateServerList(List<File> serverItemsList) {
        filesTable.getItems().clear();
        List<FileInfo> serverFileList = serverItemsList.stream()
                .map(File::toPath)
                .map(FileInfo::new)
                .collect(Collectors.toList());
        filesTable.getItems().addAll(serverFileList);
        filesTable.sort();
    }

    public void btnPathUpActionR(ActionEvent actionEvent) {
        try {
            network.sendRequest(Enums.LEVEL_UP);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void renderServerFileList(List<File> serverItemsList, String actualDirectory) {
        pathField.setText(actualDirectory);
        updateServerList(serverItemsList);
    }

    public String getSelectedFilename(){
        if(!filesTable.isFocused()){
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }
}
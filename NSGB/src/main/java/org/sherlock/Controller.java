package org.sherlock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Controller {

    @FXML
    VBox leftPanel, rightPanel;

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void copyBthAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename()==null && rightPC.getSelectedFilename()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Не выбран файл", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFilename()!=null){
            srcPC = leftPC;
            dstPC = rightPC;
        }

        if(rightPC.getSelectedFilename()!=null){
            srcPC = rightPC;
            dstPC = leftPC;
        }


        Path srcPath = Paths.get(srcPC.getCurrentPath(),srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());
        copyFiles(srcPath,dstPath);
        dstPC.updateList(Paths.get(dstPC.getCurrentPath()));

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
}
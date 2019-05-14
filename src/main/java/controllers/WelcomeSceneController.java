package controllers;

import input.InputDialogue;
import input.InputFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Model;

import java.io.File;
import java.io.IOException;

public class WelcomeSceneController {

    private void createNewNotebook() throws IOException {
        String notebookName = InputDialogue.promptUser("Create New Notebook", new InputFilter(""), 60);
        Model.getInstance().createNewNotebook(notebookName);
    }

    public void displayEditor(ActionEvent actionEvent) throws IOException {
        createNewNotebook();
        Parent editorLayout = FXMLLoader.load(getClass().getResource("/fxml/EditorScene.fxml"));
        Scene editorScene = new Scene(editorLayout);
        Stage parentStage = (Stage) ((Node) actionEvent.getTarget()).getScene().getWindow();
        parentStage.setResizable(true);
        parentStage.setMaximized(true);
        parentStage.setScene(editorScene);

    }

    public void openNotebook(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open A Notebook");
        ExtensionFilter nbkExtensionFilter = new ExtensionFilter("Notebook (*.nbk)", "*.nbk");
        fileChooser.getExtensionFilters().add(nbkExtensionFilter);
        File fileToOpen = fileChooser.showOpenDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        Model.getInstance().openNotebook(fileToOpen);
    }
}

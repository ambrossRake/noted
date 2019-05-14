package controllers;

import input.InputDialogue;
import input.InputFilter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.Model;
import notebook.Note;
import notebook.Notebook;
import notebook.Section;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    public MenuBar titleBar;
    public TreeView<String> notebookExplorer;
    public TextArea textArea;
    private Note currentlySelectedNote = null;
    private Model editorModel;

    public EditorController() {
        editorModel = Model.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleBar.setUseSystemMenuBar(true);
        refreshNotebookExplorer();
    }

    private void createTreeItems(TreeItem<String> parentTreeItem, ArrayList<notebook.Node> parentNode) {
        for (notebook.Node childNode : parentNode) {
            if (childNode instanceof Section) {
                TreeItem<String> sectionTreeItem = new TreeItem<>(childNode.getTitle());
                parentTreeItem.getChildren().add(sectionTreeItem);
                createTreeItems(sectionTreeItem, ((Section) childNode).getChildren());
            } else if (childNode instanceof Note) {
                TreeItem<String> noteTreeItem = new TreeItem<>(childNode.getTitle());
                parentTreeItem.getChildren().add(noteTreeItem);
            }
        }
    }

    private void refreshNotebookExplorer() {

        Notebook notebook = editorModel.getNotebook();
        notebookExplorer.setRoot(null);

        TreeItem<String> root = new TreeItem<>(notebook.getTitle());
        root.setExpanded(true);
        notebookExplorer.setRoot(root);

        createTreeItems(root, notebook.getChildren());

        notebookExplorer.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Node node = event.getPickResult().getIntersectedNode();
            if ((node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                String treeItemName = (String) ((TreeCell) node).getTreeItem().getValue();
                Note selectedNote = (Note) notebook.getNode(treeItemName);
                if (selectedNote != null) {
                    currentlySelectedNote = selectedNote;
                    textArea.setText(selectedNote.getText());
                }
            }
        });

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (currentlySelectedNote != null) {
                currentlySelectedNote.updateText(textArea.getText());
            }
        });
    }

    public void saveNoteBookAs(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Notebook As...");
        ExtensionFilter nbkExtensionFilter = new ExtensionFilter("Notebook (*.nbk)", "*.nbk");
        fileChooser.getExtensionFilters().add(nbkExtensionFilter);
        File fileToSaveTo = fileChooser.showSaveDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        editorModel.saveNotebook(fileToSaveTo);
    }

    public void createNewSection() throws IOException {
        String sectionName = InputDialogue.promptUser("Create New Notebook", new InputFilter(""), 60);
        editorModel.addNewSection(sectionName);
        refreshNotebookExplorer();
    }

    public void createNewNote() throws IOException {
        String noteName = InputDialogue.promptUser("Create New Notebook", new InputFilter(""), 60);
        editorModel.addNewNote(noteName);
        refreshNotebookExplorer();
    }

}

package controllers;

import input.InputDialogue;
import input.InputFilter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.Model;
import notebook.Note;
import notebook.Notebook;
import notebook.Section;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

	public MenuBar titleBar;
	public TreeView<String> notebookExplorer;
	public BorderPane borderPane;
	private StyleClassedTextArea textArea;
	private Model model;
	private notebook.Node currentlySelectedNode;

	public EditorController() {
		model = Model.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titleBar.setUseSystemMenuBar(true);
		notebookExplorer.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> handleSelectedNode(newValue)));
		setupRichTextArea();
		refreshNotebookExplorer();
	}

	private void setupRichTextArea() {
		textArea = new StyleClassedTextArea();
		borderPane.setCenter(textArea);
		textArea.textProperty().addListener((observable, oldValue, newValue) -> model.updateNoteText(currentlySelectedNode, newValue));
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

		Notebook notebook = model.getNotebook();
		notebookExplorer.setRoot(null);

		TreeItem<String> root = new TreeItem<>(notebook.getTitle());
		root.setExpanded(true);
		notebookExplorer.setRoot(root);

		createTreeItems(root, notebook.getChildren());

	}

	// Returns a string list containing the names of a TreeItem's ancestry
	private ArrayList<String> generateFullNameFromTreeItem(TreeItem<String> treeItem, ArrayList<String> fullName) {
		fullName.add(treeItem.getValue());
		TreeItem<String> parent = treeItem.getParent();
		if (parent != null) {
			generateFullNameFromTreeItem(parent, fullName);
		}

		return fullName;
	}

	private void handleSelectedNode(TreeItem<String> newValue) {
		if (newValue != null) {
			ArrayList<String> fullName = generateFullNameFromTreeItem(newValue, new ArrayList<>());
			Collections.reverse(fullName);
			fullName.remove(0);
			currentlySelectedNode = model.getNotebook().getNode(fullName, model.getNotebook().getChildren());
		}
		if (currentlySelectedNode instanceof Note) {
			String noteText = ((Note) currentlySelectedNode).getText();
			if (noteText != null) {
				textArea.replaceText(noteText);
			}
		}
	}

	public void saveNoteBookAs(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Notebook As...");
		ExtensionFilter nbkExtensionFilter = new ExtensionFilter("Notebook (*.nbk)", "*.nbk");
		fileChooser.getExtensionFilters().add(nbkExtensionFilter);
		File fileToSaveTo = fileChooser.showSaveDialog(((MenuItem) actionEvent.getTarget()).getParentPopup());
		if (fileToSaveTo != null) {
			model.saveNotebook(fileToSaveTo);
		}
	}

	public void createNewSection() throws IOException {
		InputFilter nodeInputFilter;

		if (currentlySelectedNode instanceof Section) {
			nodeInputFilter = model.createNodeInputFilter(((Section) currentlySelectedNode).getChildren());
		} else {
			nodeInputFilter = model.createNodeInputFilter(model.getNotebook().getChildren());
		}

		String sectionName = InputDialogue.promptUser("Create New Section", "Section Name:", nodeInputFilter, 60);
		if (sectionName != null) {

			model.addNewSection(currentlySelectedNode, sectionName);
			refreshNotebookExplorer();
		}
	}

	public void createNewNote() throws IOException {
		InputFilter nodeInputFilter;

		if (currentlySelectedNode instanceof Section) {
			nodeInputFilter = model.createNodeInputFilter(((Section) currentlySelectedNode).getChildren());
		} else {
			nodeInputFilter = model.createNodeInputFilter(model.getNotebook().getChildren());
		}

		String noteName = InputDialogue.promptUser("Create New Note", "Note Name:", nodeInputFilter, 60);
		if (noteName != null) {
			model.addNewNote(currentlySelectedNode, noteName);
			refreshNotebookExplorer();
		}
	}

}

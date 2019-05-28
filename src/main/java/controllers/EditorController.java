package controllers;

import input.InputDialogue;
import input.InputFilter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
	public TabPane tabPane;
	private Model model;
	private notebook.Node currentlySelectedNode;

	public EditorController() {
		model = Model.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		titleBar.setUseSystemMenuBar(true);
		notebookExplorer.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> handleSelectedNode(newValue)));
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
			Note currentNote = (Note) currentlySelectedNode;
			openNewNoteTab(currentNote);
		}
	}

	// Creates a new tab with an embedded StyleClassedTextArea
	private void openNewNoteTab(Note note) {
		if (!note.getHasActiveTab()) {
			Tab tab = new Tab(currentlySelectedNode.getTitle());
			StyleClassedTextArea richTextArea = new StyleClassedTextArea();
			richTextArea.replaceText(note.getText());
			tab.setContent(richTextArea);
			tab.setOnClosed((e) -> onTabClose(note, richTextArea));
			tabPane.getTabs().add(tab);
			note.setHasActiveTab(true);
		}
	}

	private void onTabClose(Note note, StyleClassedTextArea richTextArea) {
		model.updateNoteText(note, richTextArea.getText());
		deselectCurrentlySelectedNode();
		note.setHasActiveTab(false);
	}

	private void deselectCurrentlySelectedNode() {
		notebookExplorer.getSelectionModel().clearSelection();
		currentlySelectedNode = null;
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
		InputFilter nodeInputFilter = getNodeInputFilter();
		String sectionName = InputDialogue.promptUser("Create New Section", "Section Name:", nodeInputFilter, 60);
		if (sectionName != null) {

			model.addNewSection(currentlySelectedNode, sectionName);
			refreshNotebookExplorer();
		}
	}

	private InputFilter getNodeInputFilter() {
		InputFilter nodeInputFilter;
		if (currentlySelectedNode instanceof Section) {
			nodeInputFilter = model.createNodeInputFilter(((Section) currentlySelectedNode).getChildren());
		} else if (currentlySelectedNode.getParentNode() instanceof Section) {
			nodeInputFilter = model.createNodeInputFilter(((Section) currentlySelectedNode.getParentNode()).getChildren());
		} else {
			nodeInputFilter = model.createNodeInputFilter(model.getNotebook().getChildren());
		}
		return nodeInputFilter;
	}

	public void createNewNote() throws IOException {
		System.out.println(currentlySelectedNode);
		InputFilter nodeInputFilter = getNodeInputFilter();
		String noteName = InputDialogue.promptUser("Create New Note", "Note Name:", nodeInputFilter, 60);
		if (noteName != null) {
			model.addNewNote(currentlySelectedNode, noteName);
			refreshNotebookExplorer();
		}
	}

}

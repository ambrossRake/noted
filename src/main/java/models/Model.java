package models;

import input.InputFilter;
import notebook.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;

public class Model {

	private static Model model = null;
	private NotebookWriter notebookWriter;
	private NotebookReader notebookReader;

	private Notebook notebook;

	private Model() {

		try {
			notebookWriter = new NotebookWriter();
			notebookReader = new NotebookReader();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	public void createNewNotebook(String name) {
		notebook = new Notebook(name);
		notebook.addNode(new Note("Untitled"));
	}

	public Notebook getNotebook() {
		return notebook;
	}

	public void saveNotebook(File fileToSaveTo) {
		notebookWriter.createNewXMLFileFromNotebook(fileToSaveTo, notebook);
	}

	public void addNewSection(Node parentNode, String sectionName) {
		if (parentNode instanceof Section) {
			((Section) parentNode).addNode(new Section(sectionName));
		} else {
			notebook.addNode(new Section(sectionName));
		}
	}

	public void addNewNote(Node parentNode, String noteName) {
		if (parentNode instanceof Section) {
			((Section) parentNode).addNode(new Note(noteName));
		} else {
			notebook.addNode(new Note(noteName));
		}
	}

	public void openNotebook(File file) {
		notebook = notebookReader.createNewNotebookFromXMLFile(file);
	}

	public void updateNoteText(Note note, String newValue) {
		note.updateText(newValue);
	}

	public InputFilter createNodeInputFilter(ArrayList<Node> childNodes) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\\b(");
		for (Node node : childNodes) {
			stringBuilder.append(node.getTitle());
			stringBuilder.append("|");
		}
		stringBuilder.append(")\\b");

		return new InputFilter(stringBuilder.toString(), "*An item already exists with that name");
	}
}

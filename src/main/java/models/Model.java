package models;

import notebook.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

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
        notebook.addNote(new Note("Untitled"));
    }

    public Notebook getNotebook() {
        return notebook;
    }

    public void saveNotebook(File fileToSaveTo) {
        notebookWriter.createNewXMLFileFromNotebook(fileToSaveTo, notebook);
    }

    public void addNewSection(Node parentNode, String sectionName) {
        if (parentNode instanceof Section) {
            ((Section) parentNode).addSection(new Section(sectionName));
        } else {
            notebook.addSection(new Section(sectionName));
        }
    }

    public void addNewNote(Node parentNode, String noteName) {
        if (parentNode instanceof Section) {
            ((Section) parentNode).addNote(new Note(noteName));
        } else {
            notebook.addNote(new Note(noteName));
        }
    }

    public void openNotebook(File file) {
        notebook = notebookReader.createNewNotebookFromXMLFile(file);
    }

    public void updateNoteText(Node node, String newValue) {
        if (node instanceof Note) {
            ((Note) node).updateText(newValue);
        }
    }
}

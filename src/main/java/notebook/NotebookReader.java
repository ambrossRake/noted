package notebook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NotebookReader {

    private DocumentBuilder documentBuilder;
    private Document document;

    public NotebookReader() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    private void createNotebookNodes(Element parentElement, ArrayList<Node> parentNode) {
        NodeList nodeList = parentElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node currentNode = nodeList.item(i);
            if (currentNode.getNodeName().equals("Section")) {
                Section section = new Section(currentNode.getAttributes().getNamedItem("Title").getNodeValue());
                parentNode.add(section);
                createNotebookNodes((Element) currentNode, section.getChildren());
            } else if (currentNode.getNodeName().equals("Note")) {
                Note note = new Note(currentNode.getAttributes().getNamedItem("Title").getNodeValue());
                parentNode.add(note);
            }

        }
    }

    public Notebook createNewNotebookFromXMLFile(File file) {
        try {
            document = documentBuilder.parse(file);
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            Notebook notebook = new Notebook(root.getAttribute("Title"));
            createNotebookNodes(root, notebook.getChildren());
            return notebook;
        } catch (SAXException | IOException e) {
            return null;
        }
    }

}

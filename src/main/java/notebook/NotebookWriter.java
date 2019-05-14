package notebook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class NotebookWriter {

    private DocumentBuilder documentBuilder;
    private Document document;

    public NotebookWriter() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    private void createXMLElements(Element parentElement, ArrayList<Node> parentNode) {
        for (Node childNode : parentNode) {
            if (childNode instanceof Section) {
                Element sectionElement = document.createElement("Section");
                sectionElement.setAttribute("Title", childNode.getTitle());
                parentElement.appendChild(sectionElement);
                createXMLElements(sectionElement, ((Section) childNode).getChildren());
            } else if (childNode instanceof Note) {
                Element noteElement = document.createElement("Note");
                noteElement.setAttribute("Title", childNode.getTitle());

                Element textElement = document.createElement("Text");
                textElement.appendChild(document.createTextNode(((Note) childNode).getText()));
                noteElement.appendChild(textElement);
                parentElement.appendChild(noteElement);
            }
        }
    }

    public void createNewXMLFileFromNotebook(File file, Notebook notebook) {
        if (!file.exists()) {
            try {
                Files.createDirectories(file.toPath().getParent());
                Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            document = documentBuilder.newDocument();
            Element root = document.createElement("Notebook");
            root.setAttribute("Title", notebook.getTitle());
            document.appendChild(root);

            createXMLElements(root, notebook.getChildren());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}

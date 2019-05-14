package notebook;

import java.util.ArrayList;

public class Notebook {

    private ArrayList<Node> childNodes;
    private String title;

    public Notebook(String title) {
        childNodes = new ArrayList<>();
        this.title = title;
    }


    private Node getNode(String name, ArrayList<Node> root) {
        for (Node childNode : root) {
            if (childNode.getTitle().equals(name)) return childNode;
            if (childNode instanceof Section) {
                getNode(name, ((Section) childNode).getChildren());
            }
        }
        return null;
    }

    public Node getNode(String name) {
        for (Node childNode : childNodes) {
            if (childNode.getTitle().equals(name)) return childNode;
            if (childNode instanceof Section) {
                getNode(name, ((Section) childNode).getChildren());
            }
        }
        return null;
    }

    public ArrayList<Node> getChildren() {
        return childNodes;
    }

    public void addNote(Note note) {
        childNodes.add(note);
    }

    public void addSection(Section section) {
        childNodes.add(section);
    }

    public String getTitle() {
        return title;
    }
}

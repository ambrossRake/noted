package notebook;

import java.util.ArrayList;

public class Notebook extends Node {

    private ArrayList<Node> childNodes;

    public Notebook(String title) {
        super(title);
        childNodes = new ArrayList<>();
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
                return getNode(name, ((Section) childNode).getChildren());
            }
        }
        return this;
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

}

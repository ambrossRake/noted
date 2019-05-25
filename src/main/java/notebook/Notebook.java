package notebook;

import java.util.ArrayList;

public class Notebook extends Node {

	private ArrayList<Node> childNodes;

	public Notebook(String title) {
		super(title);
		childNodes = new ArrayList<>();
	}


	public Node getNode(ArrayList<String> fullName, ArrayList<Node> parentNode) {
		for (Node node : parentNode) {
			if (!fullName.isEmpty() && node.getTitle().equals(fullName.get(0))) {
				if (node instanceof Section && !((Section) node).getChildren().isEmpty()) {
					fullName.remove(0);
					return getNode(fullName, ((Section) node).getChildren());
				}
				return node;
			}
		}
		return this;
	}

	public ArrayList<Node> getChildren() {
		return childNodes;
	}

	public void addNode(Node node) {
		node.setParentNode(this);
		childNodes.add(node);
	}

}

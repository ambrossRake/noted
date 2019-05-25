package notebook;

public abstract class Node {

    private String title;
	private Node parentNode;

    Node(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

	public Node getParentNode() {
		return parentNode;
	}

	void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}


}

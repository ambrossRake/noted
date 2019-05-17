package notebook;

import java.util.ArrayList;

public class Section extends Node {

	private ArrayList<Node> childNodes;

	public Section(String name) {
		super(name);
		childNodes = new ArrayList<>();
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

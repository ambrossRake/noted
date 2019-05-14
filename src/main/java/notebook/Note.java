package notebook;

public class Note extends Node {
    private String text;

    public Note(String title) {
        super(title);
    }

    public void updateText(String newText) {
        text = newText;
    }

    public String getText() {
        return text;
    }
}

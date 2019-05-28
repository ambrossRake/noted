package notebook;


public class Note extends Node {
    private String text = "";
    private Boolean hasActiveTab = false;

    public Note(String title) {
        super(title);
    }

    public void updateText(String newText) {
        text = newText;
    }

    public String getText() {
        return text;
    }

    public Boolean getHasActiveTab() {
        return hasActiveTab;
    }

    public void setHasActiveTab(Boolean hasActiveTab) {
        this.hasActiveTab = hasActiveTab;
    }
}

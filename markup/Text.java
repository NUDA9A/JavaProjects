package markup;

public class Text implements Markdownable{
    private final String text;
    public Text(String str) {
        this.text = str;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(text);
    }

    @Override
    public void toBBCode(StringBuilder sb) {
        sb.append(text);
    }
}

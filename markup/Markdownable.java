package markup;

public interface Markdownable {
    void toMarkdown(StringBuilder sb);

    void toBBCode(StringBuilder sb);
}

package markup;

import java.util.List;

public abstract class AbstractMarkDown implements Markdownable{
    protected List<Markdownable> list;
    public AbstractMarkDown (List<Markdownable> list) {
        this.list = list;
    }


    protected abstract String toBBCodeTagOpen();

    protected abstract String toBBCodeTagClose();

    protected abstract String markdownTag();

    public void toMarkdown(StringBuilder sb) {
        sb.append(this.markdownTag());
        for (Markdownable md : list) {
            md.toMarkdown(sb);
        }
        sb.append(this.markdownTag());
    }

    public void toBBCode (StringBuilder sb) {
        sb.append(this.toBBCodeTagOpen());
        for (Markdownable md : list) {
            md.toBBCode(sb);
        }
        sb.append(this.toBBCodeTagClose());
    }
}

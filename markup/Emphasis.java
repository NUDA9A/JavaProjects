package markup;

import java.util.List;

public class Emphasis extends AbstractMarkDown {


    public Emphasis(List<Markdownable> list) {
        super(list);
    }

    @Override
    protected String toBBCodeTagOpen() {
        return "[i]";
    }

    @Override
    protected String toBBCodeTagClose() {
        return "[/i]";
    }

    @Override
    protected String markdownTag() {
        return "*";
    }
}

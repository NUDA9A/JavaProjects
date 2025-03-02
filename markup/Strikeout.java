package markup;

import java.util.List;

public class Strikeout extends AbstractMarkDown {
    public Strikeout(List<Markdownable> list) {
        super(list);
    }

    @Override
    protected String toBBCodeTagOpen() {
        return "[s]";
    }

    @Override
    protected String toBBCodeTagClose() {
        return "[/s]";
    }

    @Override
    protected String markdownTag() {
        return "~";
    }

}

package markup;

import java.util.List;

public class Strong extends AbstractMarkDown {
    public Strong(List<Markdownable> list) {
        super(list);
    }

    @Override
    protected String toBBCodeTagOpen() {
        return "[b]";
    }

    @Override
    protected String toBBCodeTagClose() {
        return "[/b]";
    }

    @Override
    protected String markdownTag() {
        return "__";
    }

}

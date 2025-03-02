package markup;

import java.util.List;

public class Paragraph extends AbstractMarkDown{
    public Paragraph (List<Markdownable> list) {
        super(list);
    }

    @Override
    protected String toBBCodeTagOpen() {
        return "";
    }

    @Override
    protected String toBBCodeTagClose() {
        return "";
    }

    @Override
    protected String markdownTag() {
        return "";
    }

}

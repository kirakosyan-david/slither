package am.example.slither.error;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class CustomErrorPage extends WebPage {

    public CustomErrorPage() {
        add(new Label("message", "Oops! Something went wrong."));
    }
}

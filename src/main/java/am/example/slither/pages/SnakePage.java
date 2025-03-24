package am.example.slither.pages;

import am.example.slither.dto.SnakeDto;
import am.example.slither.service.SnakeService;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.File;
import java.util.Objects;

public class SnakePage extends WebPage implements IMarkupResourceStreamProvider {

    @SpringBean
    private SnakeService snakeService;

    private SnakeDto snakeDto;

    private boolean isSaving = false;

    public SnakePage() {
        snakeService.startGame();
        this.snakeDto = snakeService.getSnakeDto();

        Form<SnakeDto> form = new Form<>("snakeForm", new CompoundPropertyModel<>(snakeDto)) {
            @Override
            protected void onSubmit() {
                snakeService.onFoodEaten();
                snakeService.saveSnake(snakeDto);
            }
        };

        form.setOutputMarkupId(false);

        Label lengthLabel = new Label("length", new PropertyModel<>(snakeDto, "length"));
        lengthLabel.setOutputMarkupId(true);

        Label rankLabel = new Label("rank", new PropertyModel<>(snakeDto, "rank"));
        rankLabel.setOutputMarkupId(true);

        form.add(lengthLabel);
        form.add(rankLabel);

        form.add(new AjaxFormSubmitBehavior("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (isSaving) return;
                isSaving = true;

                snakeService.onFoodEaten();
                snakeDto = snakeService.getSnakeDto();
                target.add(lengthLabel);
                target.add(rankLabel);

                target.appendJavaScript("updateGameState(" + snakeDto.getLength() + ", " + snakeDto.getRank() + ");");

                isSaving = false;
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                System.out.println("AJAX ошибка!");
                isSaving = false;
            }
        });

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        String contextPath = getRequestCycle().getRequest().getContextPath();

        response.render(CssHeaderItem.forUrl(contextPath + "/css/snakeStyles.css"));

        response.render(JavaScriptHeaderItem.forUrl(contextPath + "/js/scriptSnake.js").setDefer(true));
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> aClass) {
        return new FileResourceStream(
                new File(Objects.requireNonNull(getClass().getResource("/templates/snake.html")).getFile())
        );
    }
}

package am.example.slither.pages;

import am.example.slither.entity.User;
import am.example.slither.service.UserService;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.File;
import java.util.Objects;

public class LoginPage extends WebPage implements IMarkupResourceStreamProvider {


    @SpringBean
    private UserService userService;

    public LoginPage() {

        Form<User> form = new Form<>("loginForm", new CompoundPropertyModel<>(new User())) {
            @Override
            protected void onSubmit() {
                try {
                    User userDto = getModelObject();
                    boolean loginUser = userService.loginUser(
                            userDto.getEmail(),
                            userDto.getPassword());

                    if (loginUser) {
                        success("Вход выполнен успешно!");
                        setResponsePage(SnakePage.class);
                    }else {
                        error("Неверный email или пароль");
                    }
                } catch (Exception e) {
                    error("Ошибка: " + e.getMessage());
                }
            }
        };

        form.add(new EmailTextField("email"));
        form.add(new PasswordTextField("password"));
        form.add(new Button("button"));

        add(form);

        add(new BookmarkablePageLink<>("registerLink", RegisterPage.class));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("/css/styles.css"));
    }


    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> aClass) {
        return new FileResourceStream(
                new File(Objects.requireNonNull(getClass().getResource("/templates/login.html")).getFile())
        );
    }
}

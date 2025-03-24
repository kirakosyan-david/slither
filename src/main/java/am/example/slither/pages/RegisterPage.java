package am.example.slither.pages;

import am.example.slither.dto.UserRegisterDto;
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
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.File;
import java.util.Objects;

public class RegisterPage extends WebPage implements IMarkupResourceStreamProvider {

    @SpringBean
    private UserService userService;

    public RegisterPage() {

        Form<UserRegisterDto> form = new Form<>("registerForm", new CompoundPropertyModel<>(new UserRegisterDto())) {
            @Override
            protected void onSubmit() {
                try {
                    UserRegisterDto registerDto = getModelObject();
                    userService.registerUser(
                            registerDto.getUsername(),
                            registerDto.getEmail(),
                            registerDto.getPassword());

                    success("Регистрация успешна!");
                    setResponsePage(LoginPage.class);
                } catch (Exception e) {
                    error("Ошибка: " + e.getMessage());
                }
            }
        };

        form.add(new RequiredTextField<>("username"));
        form.add(new EmailTextField("email"));
        form.add(new PasswordTextField("password"));
        form.add(new Button("button"));

        add(form);

        add(new BookmarkablePageLink<>("loginLink", LoginPage.class));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forUrl("/css/styles.css"));
    }


    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> aClass) {
        return new FileResourceStream(
                new File(Objects.requireNonNull(getClass().getResource("/templates/register.html")).getFile())
        );
    }
}

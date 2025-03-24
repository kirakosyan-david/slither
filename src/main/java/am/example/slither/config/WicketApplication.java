package am.example.slither.config;

import am.example.slither.error.CustomErrorPage;
import am.example.slither.pages.RegisterPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WicketApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return RegisterPage.class;
    }

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getApplicationSettings().setInternalErrorPage(CustomErrorPage.class);
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        mountPage("/index", RegisterPage.class);
    }
}

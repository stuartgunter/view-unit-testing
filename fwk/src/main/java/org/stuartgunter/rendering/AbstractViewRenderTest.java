package org.stuartgunter.rendering;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Think of this class as the Controller in MVC --- only in this case it's controlling the test.
 *
 * This test requires a {@link ViewResolver} to be configured in the application context.
 */
@ContextConfiguration(locations = "/fwk-context.xml", loader = MockServletContextLoader.class)
public abstract class AbstractViewRenderTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ViewResolver viewResolver;
    @Resource
    private LocaleResolver localeResolver;

    private final Locale locale;

    /**
     * Creates a new instance with the system default locale.
     */
    public AbstractViewRenderTest() {
        this(null);
    }

    /**
     * Creates a new instance with the specified locale. If locale is null, it will default to the system default.
     * @param locale The locale to test against
     */
    public AbstractViewRenderTest(Locale locale) {
        this.locale = (locale == null) ? Locale.getDefault() : locale;
    }

    /**
     * Defines the Model to be passed to the template.
     *
     * @param model The Spring Model to be bound with the template
     * @return The name of the template to render (as it would be returned from the request handler)
     */
    protected abstract String given(Model model);

    /**
     * Asserts that the rendered template conforms to expectations.
     *
     * @param html The rendered HTML content as a Jsoup {@link Document}
     */
    protected abstract void then(Document html);

    /**
     * Configures the model. Renders the template. Executes assertions.
     */
    @Test
    public final void execute() throws Exception {
        // given
        final Model model = new BindingAwareModelMap();
        final String template = given(model);

        // when
        final Document html = render(template, model);

        // then
        then(html);
    }

    /**
     * Renders the template in the given locale, binding the given model
     * @param model The model to bind to the view
     * @return The rendered HTML content as a Jsoup {@link Document}
     * @throws Exception
     */
    private Document render(String template, Model model) throws Exception {
        final View view = viewResolver.resolveViewName(template, locale);

        final MockHttpServletRequest request = prepareRequest();
        final MockHttpServletResponse response = prepareResponse();

        view.render(model.asMap(), request, response);

        return Jsoup.parse(response.getContentAsString());
    }

    /**
     * Prepares the {@link MockHttpServletResponse}
     * @return The prepared MockHttpServletResponse
     */
    private MockHttpServletResponse prepareResponse() {
        return new MockHttpServletResponse();
    }

    /**
     * Prepares the {@link MockHttpServletRequest}
     * @return The prepared MockHttpServletRequest
     */
    private MockHttpServletRequest prepareRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
        request.addPreferredLocale(locale);
        return request;
    }
}

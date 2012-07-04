package org.stuartgunter.rendering.app.pages;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.stuartgunter.rendering.app.SomeComponent;
import org.testng.annotations.BeforeMethod;
import org.stuartgunter.rendering.AbstractViewRenderTest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@ContextConfiguration(locations = "/view-context.xml")
public class PageTest extends AbstractViewRenderTest {

    @Mock
    private SomeComponent mockComponent;

    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);

        when(mockComponent.getSomeText()).thenReturn("mocked-text");
    }

    @Override
    public String given(Model model) {
        Map<String, String> item = new HashMap<String, String>();
        item.put("id", "some-id");
        item.put("text", "some-text");

        model.addAttribute("item", item);
        model.addAttribute("component", mockComponent);

        return "page";
    }

    @Override
    public void then(Document html) {
        Element item = html.body().getElementById("some-id");
        assertNotNull(item);
        assertEquals("key: some-text", item.text());

        Element component = html.body().getElementById("component");
        assertNotNull(component);
        assertEquals("mocked-text", component.text());

        Elements included = html.body().getElementsByClass("included");
        assertNotNull(included);
        assertEquals(1, included.size());

        Element div = included.get(0);
        assertEquals("included", div.text());

        verify(mockComponent).doSomething();
    }
}

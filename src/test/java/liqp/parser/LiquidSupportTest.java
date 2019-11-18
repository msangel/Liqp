package liqp.parser;

import liqp.RenderSettings;
import liqp.Template;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class LiquidSupportTest {
    public static class Target implements LiquidSupport {

        public Target() {
        }

        private String val;
        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        @Override
        public Map<String, Object> toLiquid() {
            HashMap<String, Object> data = new HashMap<>();
            data.put("val", "OK");
            return data;
        }
    }

    static class Foo {
        public String a = "A";
    }

    @Test
    public void renderMapWithPojosExistedThrowError() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("foo", new Foo());
        String fooA = Template.parse("{{foo.a}}").render(data);

        assertThat(fooA, is(""));
    }

    @Test
    public void renderMapWithPojosWithNewRenderingSettings() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("foo", new Foo());
        String fooA = Template.parse("{{foo.a}}").withRenderSettings(new RenderSettings.Builder().withEvaluateMode(RenderSettings.EvaluateMode.EAGER).build()).render(data);

        assertThat(fooA, is("A"));
    }

    @Test
    public void renderMapWithPojosWithMarkingInspectable() {
        Map<String, Object> data = new HashMap<String, Object>();
        class FooWrapper extends Foo implements Inspectable {}
        data.put("foo", new FooWrapper());
        String fooA = Template.parse("{{foo.a}}").render(data);

        assertThat(fooA, is("A"));
    }

    @Test
    public void testLiquidSupport() {
        // given
        Target inspect = new Target();
        inspect.setVal("not this");
        Map<String, Object> in = new HashMap<>();
        in.put("a", inspect);

        // when
        String res = Template.parse("{{a.val}}").render(in);

        // then
        assertEquals("OK", res);
    }

    @Test
    public void renderLiquidSupportWithNewRenderingSettings() {
        Target inspect = new Target();
        inspect.setVal("not this");
        Map<String, Object> in = new HashMap<>();
        in.put("a", inspect);

        String fooA = Template.parse("{{a.val}}").withRenderSettings(new RenderSettings.Builder().withEvaluateMode(RenderSettings.EvaluateMode.EAGER).build()).render(in);

        assertThat(fooA, is("OK"));
    }
}

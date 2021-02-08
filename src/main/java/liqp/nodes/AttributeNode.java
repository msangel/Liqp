package liqp.nodes;

import liqp.TemplateContext;
import liqp.spi.BasicTypesSupport;

public class AttributeNode implements LNode {

    private LNode key;
    private LNode value;

    public AttributeNode(LNode key, LNode value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Object render(TemplateContext context) {

        return new Object[]{
                key.render(context),
                BasicTypesSupport.restoreObject(context, value.render(context))
        };
    }
}

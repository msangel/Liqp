package liqp.nodes;

import liqp.LValue;
import liqp.TemplateContext;
import liqp.spi.BasicTypesSupport;

public class NEqNode implements LNode {

    private LNode lhs;
    private LNode rhs;

    public NEqNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(TemplateContext context) {

        Object a = BasicTypesSupport.restoreObject(context, lhs.render(context));
        Object b = BasicTypesSupport.restoreObject(context, rhs.render(context));

        return !LValue.areEqual(a, b);

    }
}

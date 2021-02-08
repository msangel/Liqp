package liqp.nodes;

import liqp.LValue;
import liqp.TemplateContext;
import liqp.spi.BasicTypesSupport;

import java.util.Map;

public class EqNode implements LNode {

    private LNode lhs;
    private LNode rhs;

    public EqNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(TemplateContext context) {

        Object a = BasicTypesSupport.restoreObject(context, lhs.render(context));
        Object b = BasicTypesSupport.restoreObject(context, rhs.render(context));

        return LValue.areEqual(a, b);

    }
}

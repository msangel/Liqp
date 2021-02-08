package liqp.nodes;

import liqp.LValue;
import liqp.TemplateContext;
import liqp.spi.BasicTypesSupport;

public class LtNode extends LValue implements LNode {

    private LNode lhs;
    private LNode rhs;

    public LtNode(LNode lhs, LNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object render(TemplateContext context) {

        Object a = BasicTypesSupport.restoreObject(context, lhs.render(context));
        Object b = BasicTypesSupport.restoreObject(context, rhs.render(context));

        return (a instanceof Number) && (b instanceof Number) &&
                super.asNumber(a).doubleValue() < super.asNumber(b).doubleValue();
    }
}

package liqp.filters.where;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import liqp.LValue;
import liqp.parser.Flavor;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by vasyl.khrystiuk on 10/09/2019.
 */
public abstract class WhereImpl extends LValue {

    protected final ObjectMapper mapper;
    protected final Flavor flavor;

    protected WhereImpl(ObjectMapper mapper, Flavor flavor) {
        this.mapper = mapper;
        this.flavor = flavor;
    }

    public abstract Object apply(Object value, Object... params);


}

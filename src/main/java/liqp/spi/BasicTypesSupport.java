package liqp.spi;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import liqp.RenderSettings.EvaluateMode;
import liqp.TemplateContext;
import liqp.filters.date.CustomDateFormatRegistry;
import liqp.filters.date.CustomDateFormatSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class provide generic abstract implementation of {@link TypesSupport}.
 * Also it provides helpers for restoring objects from
 * form of maps and arrays.
 * Everything is converted to maps and arrays with {@link EvaluateMode#EAGER},
 * because this is a way of jackson mappers works in this code:
 * <code>
 *     mapper.convertValue(value, Map.class)
 * </code>
 * so this is tool for restoring objects back.
 * Built-in usage: restore DateTime objects.
 */
public abstract class BasicTypesSupport implements TypesSupport {

    @SuppressWarnings("rawtypes")
    private static final Map<String, TypeConvertor> typeRegistry = new ConcurrentHashMap<>();

    protected<T> void registerType(SimpleModule module, final Class<T> clazz, final TypeConvertor<T> typeGenerator) {
        module.addSerializer(new StdSerializer<T>(clazz) {
            @Override
            public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeStartObject();
                gen.writeBooleanField("@supportedTypeMarker", Boolean.TRUE);
                gen.writeStringField("@type", clazz.getName());
                gen.writeFieldName("@data");
                typeGenerator.serialize(gen, value);
                gen.writeEndObject();
            }
        });
        typeRegistry.put(clazz.getName(), typeGenerator);
    }

    protected void addCustomDateType(CustomDateFormatSupport<?> typeSupport) {
        CustomDateFormatRegistry.add(typeSupport);
    }

    /**
     * In case if the object's type is use special internal formatting - restore the object in it initial form.
     * Otherwise return input back.
     */
    public static Object restoreObject(TemplateContext context, Object obj) {
        if (obj == null) {
            return null;
        }
        if (! (obj instanceof Map)) {
            return obj;
        }
        //noinspection rawtypes
        Map mapObj = (Map) obj;
        if (!Boolean.TRUE.equals(mapObj.get("@supportedTypeMarker"))) {
            return obj;
        }
        Object typeName = mapObj.get("@type");
        if (!(typeName instanceof String)) {
            // improperly formatted objects will be returned as is
            return obj;
        }
        //noinspection rawtypes
        TypeConvertor typeConvertor = typeRegistry.get(typeName);
        if (typeConvertor == null) {
            // missing type converted will be treated as error
            // and cause fallback to returning object as is
            return obj;
        }
        Object dataMapObj = mapObj.get("@data");
        if (!(dataMapObj instanceof Map)) {
            return obj;
        }
        //noinspection rawtypes
        return typeConvertor.deserialize(context, (Map)dataMapObj);
    }


}

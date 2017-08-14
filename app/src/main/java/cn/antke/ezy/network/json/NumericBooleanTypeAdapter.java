package cn.antke.ezy.network.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by litzuhsien on 4/8/15.
 *
 */
public class NumericBooleanTypeAdapter implements JsonDeserializer<Boolean>, JsonSerializer<Boolean> {
	@Override
	public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		int jsonValue = json.getAsInt();
		return jsonValue == 1 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.equals(Boolean.FALSE) ? 0 : 1);
	}
}

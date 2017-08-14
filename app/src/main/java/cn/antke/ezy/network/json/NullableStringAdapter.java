package cn.antke.ezy.network.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by litzuhsien on 4/15/15.
 *
 */
public class NullableStringAdapter implements JsonDeserializer<String> {
	@Override
	public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String value = json.getAsString();
		if (value == null || value.isEmpty()) {
			return null;
		} else {
			return value;
		}
	}
}

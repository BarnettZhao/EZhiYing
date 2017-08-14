package cn.antke.ezy.network.json;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * Created by litzuhsien on 2/5/15.
 */
public final class GsonObjectDeserializer {

	public static LinkedHashMap deserialize(JsonElement jsonElement) {
		Gson gson = produceGson();
		return gson.fromJson(jsonElement, LinkedHashMap.class);
	}

	public static <T> T deserialize(JsonElement jsonElement, Class<T> type) {
		Gson gson = produceGson();
		return gson.fromJson(jsonElement, type);
	}

	public static <T> T deserialize(String jsonString, Class<T> type) {
		Gson gson = produceGson();
		return gson.fromJson(jsonString, type);
	}

	private static final String DefaultDateFormatPattern = "yyyyMMddHHmmss";

	public static Gson produceGson() {

		Gson gson = new GsonBuilder()
				.setFieldNamingStrategy(new FieldNamingStrategy() {
					@Override
					public String translateName(Field field) {
						JsonAttribute annotation = field.getAnnotation(JsonAttribute.class);
						if (annotation == null) {
							SerializedName serializedName = field.getAnnotation(SerializedName.class);
							if (serializedName == null) {
								return field.getName();
							} else {
								return serializedName.value();
							}
						}
						String jsonAttributeName = annotation.value();
						return jsonAttributeName;
					}
				})
				.enableComplexMapKeySerialization()
				.registerTypeAdapter(Boolean.class, new NumericBooleanTypeAdapter())
//                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
				.registerTypeAdapter(String.class, new NullableStringAdapter())
				.setDateFormat(DefaultDateFormatPattern)
				.create();
		return gson;
	}

}

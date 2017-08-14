package cn.antke.ezy.network.json;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by litzuhsien on 3/17/15.
 *
 */
public class GsonObjectSerializer {
	private static final String DefaultDateFormatPattern = "yyyyMMdd";

	public JsonElement serialize(Serializable object) {
		Gson gson = produceGson();
		JsonElement jsonElement = gson.toJsonTree(object);
		return jsonElement;
	}

	public static Gson produceGson() {
		Gson gson = new GsonBuilder().setDateFormat(DefaultDateFormatPattern)
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
				.setDateFormat(DefaultDateFormatPattern)
				.create();
		return gson;
	}
}

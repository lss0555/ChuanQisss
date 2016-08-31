package Utis;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public final class GsonUtils {

	public static <T> T parseJSON(String json, Class<T> clazz) {
		Gson gson = new Gson();
		T info = gson.fromJson(json, clazz);
		return info;
	}
	
	/**
	 */
	public static <T> T parseJSONArray(String jsonArr, Type type) {
		Gson gson = new Gson();
		T infos = gson.fromJson(jsonArr, type);
		return infos;
	}
	
	
	
	private GsonUtils(){}
}

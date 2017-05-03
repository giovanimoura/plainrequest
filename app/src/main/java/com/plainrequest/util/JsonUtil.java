package com.plainrequest.util;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.plainrequest.annotation.ExcludeJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Classe para manipulação de json
 *
 * @author Giovani Moura
 */
public class JsonUtil {

    private static final String TAG = "PLAINREQUEST";

    /**
     * Converte um objeto para uma string json
     *
     * @param obj Entity
     * @return string json
     */
    public static String objToJson(Object obj) {
        String result = "";
        try {
            result = new JSONObject(createGson().toJson(obj)).toString();
        } catch (JSONException e) {
            print(e.getMessage());
        }
        return result;
    }

    /**
     * Converte uma lista para uma string json
     *
     * @param list List
     * @return string json
     */
    public static String listToJson(List list) {
        String result = "";
        try {
            result = new JSONArray(createGson().toJson(list)).toString();
        } catch (JSONException e) {
            print(e.getMessage());
        }
        return result;
    }

    /**
     * Retorna um valor string de um determinado atributo do json
     *
     * @param nameFindJson Campo para ser localizado e desserializado
     * @param json         String json
     * @return
     */
    public static String toStringFromNameObject(String nameFindJson, String json) {
        String result = "";
        try {
            JSONObject jo = (JSONObject) new JSONTokener(json).nextValue();
            result = jo.get(nameFindJson).toString();
        } catch (JSONException e) {
            print(e.getMessage());
        }
        return result;
    }

    /**
     * Converte string json para um objeto
     *
     * @param json      String json
     * @param classType Tipo da classe do objeto de retorno
     * @param <T>
     * @return Generico - objeto conforme classType
     */
    public static <T> T toObject(String json, Class classType) {
        return (T) createGson().fromJson(json, classType);
    }

    /**
     * Converte string json para um objeto
     *
     * @param nameFindJson Campo para ser localizado e desserializado
     * @param json         String json
     * @param classType    Tipo da classe do objeto de retorno
     * @param <T>
     * @return Generico - objeto conforme classType
     */
    public static <T> T toObject(String nameFindJson, String json, Class classType) {
        String result = "";
        try {
            JSONObject jo = (JSONObject) new JSONTokener(json).nextValue();
            result = jo.get(nameFindJson).toString();
        } catch (JSONException e) {
            print(e.getMessage());
        }
        return (T) createGson().fromJson(result, classType);
    }

    /**
     * Converte string json para uma lista
     *
     * @param json
     * @param typeToken
     * @return
     */
    public static List toList(String json, TypeToken typeToken) {
        return toList("", json, typeToken.getType());
    }

    /**
     * Converte string json para uma lista
     *
     * @param nameFindJson Campo para ser localizado e desserializado
     * @param json         String json
     * @param superClass   Tipo da classe do objeto de retorno
     * @return List
     */
    public static List toList(String nameFindJson, String json, Type superClass) {
        String result = "";
        try {
            Object objJson = new JSONTokener(json).nextValue();

            if (objJson instanceof JSONArray) {
                result = new JSONArray(json).toString();
            } else {
                JSONObject jo = (JSONObject) objJson;
                result = jo.getJSONArray(nameFindJson).toString();
            }

        } catch (JSONException e) {
            print(e.getMessage());
        }
        return createGson().fromJson(result, superClass);
    }

    /**
     * Converte string json para um map
     *
     * @param json       String json
     * @param superClass Tipo da classe do objeto de retorno
     * @return
     */
    public static Map toMap(String json, Type superClass) {
        return createGson().fromJson(json, superClass);
    }

    private static Gson createGson() {
        GsonBuilder builder = new GsonBuilder();

        // Registra o tipo Date
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(date.getTime());
            }
        })
        .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        })
        .addSerializationExclusionStrategy(new SerializationExclusionStrategy())
        .addDeserializationExclusionStrategy(new DeserializationExclusionStrategy());

        return builder.create();
    }

    private static class SerializationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            ExcludeJson excludeJson = field.getAnnotation(ExcludeJson.class);
            return excludeJson != null && !excludeJson.serialize();
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static class DeserializationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            ExcludeJson excludeJson = field.getAnnotation(ExcludeJson.class);
            return excludeJson != null && !excludeJson.deserialize();
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static void print(String msg) {
        Log.e(TAG, "Error JSON: " + msg);
    }
}

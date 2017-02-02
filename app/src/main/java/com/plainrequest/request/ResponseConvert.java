package com.plainrequest.request;

import com.plainrequest.model.Settings;
import com.plainrequest.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Giovani Moura
 */
class ResponseConvert {

    private Type superClass;

    public <T> T convert(T response, Settings settings, Type superClass) {
        this.superClass = superClass;

        // Define o formato para campos Date do Gson
        JsonUtil.registerDateFormat(settings.dateFormat, settings.dateFormatSerializer, settings.dateFormatDeserializer);

        if (isList()) {
            response = (T) JsonUtil.toList(settings.nameFindJson, response.toString(), superClass);
        } else if (isMap()) {
            response = (T) JsonUtil.toMap(response.toString(), superClass);
        } else if (isObject()) {
            if (settings.nameFindJson != null)
                response = JsonUtil.toObject(settings.nameFindJson, response.toString(), (Class) superClass);
            else
                response = JsonUtil.toObject(response.toString(), (Class) superClass);
        }

        return response;
    }

    private boolean isList() {
        try {
            Type type = getRawType();
            return type.equals(List.class) || type.equals(ArrayList.class);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMap() {
        try {
            return getRawType().equals(Map.class);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isObject() {
        return !superClass.equals(JSONObject.class) && !superClass.equals(JSONArray.class) && !superClass.equals(String.class);
    }

    private Type getRawType() {
        try {
            return ((ParameterizedType) superClass).getRawType();
        } catch (Exception e) {
            return null;
        }
    }
}

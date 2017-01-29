package com.plainrequest.request;

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

    public <T> T convert(T response, String nameFindJson, Type superClass) {
        this.superClass = superClass;

        if (isList()) {
//            Class sClass = null;
//            Type typeClass = ((ParameterizedType) superClass).getActualTypeArguments()[0]; // Tipo da Classe no list

//            if (typeClass instanceof ParameterizedType) {
//                ParameterizedType parameterizedType = ((ParameterizedType) typeClass);
//                if (parameterizedType != null) {
//                    sClass = (Class) parameterizedType.getRawType(); // Map
//                }
//            } else {
//                sClass = (Class) typeClass;
//            }

            response = (T) JsonUtil.toList(nameFindJson, response.toString(), superClass);

        } else if (isMap()) {
            response = (T) JsonUtil.toMap(response.toString(), superClass);

        } else if (isObject()) {
            if (nameFindJson != null)
                response = JsonUtil.toObject(nameFindJson, response.toString(), (Class) superClass);
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

package ru.netdedicated;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by artemz on 10.12.16.
 */
public class JsonUtil {
    public static String toJson(Object object){
        return new Gson().toJson(object);
    }
    public static ResponseTransformer json(){
        return JsonUtil::toJson;
    }
}

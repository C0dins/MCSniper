package net.mcsniper.utils;

import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class APIUtil {
    static OkHttpClient client = new OkHttpClient();

    public static long getDroptime(String name){
        Request getRequest = new Request.Builder()
                .url("https://api.star.shopping/droptime/" + name)
                .header("User-Agent", "Sniper")
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body().string());
            if(json.get("unix") != null) {
                return (long) json.get("unix");
            } else {
                return 0;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static int getSearches(String name){
        Request getRequest = new Request.Builder()
                .url("http://api.droptime.cc/droptime/" + name)
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body().string());
            return Integer.valueOf(String.valueOf(json.get("searches")));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static boolean canNameChange(String token){
        Request getRequest = new Request.Builder()
                .url("https://api.minecraftservices.com/minecraft/profile/namechange")
                .header("Authorization", "Bearer " + token)
                .build();
        try{
            Response response = client.newCall(getRequest).execute();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.body().string());
            return Boolean.valueOf(String.valueOf(json.get("nameChangeAllowed")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void changeSkin(String token){
        //Object
        JSONObject json = new JSONObject();
        json.put("url", "http://textures.minecraft.net/texture/22748b3b2d1e1090dff757240761865aa052f3d0698e6f8dfd7e18bf54bec418");
        json.put("variant", "slim");

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json.toString());

        Request getRequest = new Request.Builder()
                .url("https://api.minecraftservices.com/minecraft/profile/skins")
                .header("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try{
            Response response = client.newCall(getRequest).execute();
            if(response.code() == 200){
                Logger.log(LogType.SUCCESS, "Successfully changed Skin!");
            } else {
                Logger.log(LogType.ERROR, "Error while changing skin!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

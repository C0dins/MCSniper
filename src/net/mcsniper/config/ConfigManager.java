package net.mcsniper.config;

import lombok.Getter;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class ConfigManager  {

    private String configPath = "config.json";
    @Getter private int delay;
    @Getter private boolean announce, skinchange, rateLimitBypass, logTimes;

    public ConfigManager(){
        refresh();
        Logger.log(LogType.CONFIG, "Successfully loaded config!");
    }

    public void refresh() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(configPath));

            JSONObject jsonObject = (JSONObject) obj;
            delay = Integer.parseInt(String.valueOf(jsonObject.get("magicDelay")));
            rateLimitBypass = (boolean) jsonObject.get("rateLimitBypass");
            announce = (boolean) jsonObject.get("announce");
            skinchange = (boolean) jsonObject.get("skinchange");
            logTimes = (boolean) jsonObject.get("logTimes");

            } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package net.mcsniper.logs;

import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LogManager {

    public LogManager(){
        Logger.log(LogType.GENERAL, "Successfully setup log manager!");
    }
    public void createLogs(String name, ArrayList<Integer> codes, ArrayList<String> sends, ArrayList<String> recvs) {
        try {
            String msg = "Target Name: " + name + "\n";

            for (int i = 0; i < sends.size(); i++) {
                int code = codes.get(i);
                String send = sends.get(i);
                String recv = recvs.get(i);

                if(code == 200){
                    msg += "[" + code + "] " + send + " --> " + recv + " [HIT]\n";
                } else if(code == 401){
                    msg += "[" + code + "] " + send + " --> " + recv + " [UNAUTH]\n";
                } else if(code == 403){
                    msg += "[" + code + "] " + send + " --> " + recv + " [FAIL]\n";
                } else if(code == 429){
                    msg += "[" + code + "] " + send + " --> " + recv + " [RL]\n";
                } else if(code == 500 || code == 502 || code == 503 || code == 504){
                    msg += "[" + code + "] " + send + " --> " + recv + " [LAG]\n";
                }
            }
            FileWriter logWritter = new FileWriter("%name%.txt".replace("%name%", name));
            logWritter.write(msg);
            logWritter.close();
            Logger.log(LogType.SUCCESS, "Saved logs for %name%".replace("%name%", name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
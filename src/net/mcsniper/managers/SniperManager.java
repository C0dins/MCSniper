package net.mcsniper.managers;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import lombok.Getter;
import net.mcsniper.tasks.SniperTask;
import net.mcsniper.utils.APIUtil;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SniperManager {
    @Getter private ArrayList<SniperTask> tasks;

    public SniperManager(){
        tasks = new ArrayList<SniperTask>();
        Logger.log(LogType.GENERAL, "Successfully setup snipe manager!");
    }

    public void startSnipe(String name) throws ParseException, MicrosoftAuthenticationException, InterruptedException, FileNotFoundException {
        long droptime = APIUtil.getDroptime(name);

        if(droptime == 0){
            Logger.log(LogType.ERROR, "That name is not dropping.");
            return;
        }

        SniperTask task = new SniperTask(name, droptime, tasks.size() + 1);
        Thread thread = new Thread(task);
        tasks.add(task);
        thread.start();

    }
}

package net.mcsniper.tasks;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import lombok.Getter;
import lombok.Setter;
import net.mcsniper.Main;
import net.mcsniper.managers.ProxyManager;
import net.mcsniper.objects.Account;
import net.mcsniper.utils.AuthUtil;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

@Getter
@Setter
public class SniperTask implements Runnable {

    private long droptime;
    private long id;
    private String target, successEmail;
    private ArrayList<String> tokens, sends, recvs;
    private ArrayList<Integer> codes;
    private boolean isCanceled;
    private ArrayList<String> testing = new ArrayList<>();

    public SniperTask(String name, long droptime, long id){
        this.target = name;
        this.successEmail = "";
        this.droptime = droptime;
        this.id = id;
        this.tokens = new ArrayList<>();
        this.codes = new ArrayList<>();
        this.sends = new ArrayList<>();
        this.recvs = new ArrayList<>();
        this.isCanceled = false;
    }
    @Override
    public void run() {

        //Get accounts // auth;
        ArrayList<Account> accounts = Main.getDatabaseManager().getAccountsMS();
        Logger.log(LogType.GENERAL, "Starting Auth Sequence");
        try {
            tokens = AuthUtil.auth(droptime, accounts);
        } catch (MicrosoftAuthenticationException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Get proxies
        ArrayList<String> proxies = null;
        try {
            proxies = ProxyManager.getProxies();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Thread> threads = new ArrayList<Thread>();

        int proxyIndex = 0;
        for(int i = 0; i < tokens.size(); i++) {
            if (i != 0 && i % 2 == 0) {
                proxyIndex++;
            }
            for (int r = 0; r < 2; r++) {
                String token = tokens.get(i);
                String proxy = proxies.get(proxyIndex).split(":")[0];
                int port = Integer.parseInt(proxies.get(proxyIndex).split(":")[1]);
                RequestRunnable req = new RequestRunnable(id, target, droptime, token.split("-MCSNIPER-")[0], token.split("-MCSNIPER-")[1], proxy, port);
                threads.add(new Thread(req));
            }
        }

        Logger.log(LogType.GENERAL, "Loaded %requests% requests!".replace("%requests%", String.valueOf(threads.size())));
        Main.getDatabaseManager().addSnipe(target, droptime);

        //Start threads
        for(Thread t : threads){
            t.start();
        }

        try {
            Thread.sleep((droptime - Instant.now().getEpochSecond() + 5) * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Log Saving

        if(Main.getConfigManager().isLogTimes()){
            Main.getLogManager().createLogs(target, Main.getCodes(), Main.getSends(), Main.getRecvs());
            Main.getCodes().clear();
            Main.getSends().clear();
            Main.getRecvs().clear();
            Logger.log(LogType.GENERAL, "Finished with snipe on " + target);
            if(Main.getDatabaseManager().getSnipedCode(target) == 1){
                Logger.log(LogType.SUCCESS, "Account should be on " + successEmail);
            } else {
                Main.getDatabaseManager().failSnipe(target);
            }
        }
    }

    public void cancel(){
        this.isCanceled = true;
    }

    public long getId(){
        return id;
    }
}

package net.mcsniper.utils;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.mcsniper.Main;
import net.mcsniper.objects.Account;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;

public class AuthUtil {

    public static ArrayList<String> auth(Long droptime, ArrayList<Account> accounts) throws MicrosoftAuthenticationException, InterruptedException, ParseException {
        ArrayList<String> tokens = new ArrayList<>();
        int counter = 0;

        for(Account account : accounts) {
            //DB Caching
            String oldToken = Main.getDatabaseManager().getToken(account.getEmail());
            if (oldToken != null) {
                String[] chunks = oldToken.split("\\.");
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(new String(Base64.getUrlDecoder().decode(chunks[1])));
                long exp = (long) json.get("exp");
                if (exp - droptime > 0) {
                    if (APIUtil.canNameChange(oldToken)) {
                        tokens.add(account.getEmail() + "-MCSNIPER-" + oldToken);
                        Main.getDatabaseManager().setToken(account.getEmail(), oldToken);
                        Main.getDatabaseManager().reserveAccount(account.getEmail());
                        Logger.log(LogType.AUTH, "Successfully logged into " + account.getEmail() + " via save!");
                        counter++;
                        Thread.sleep(2 * 1000);
                    } else {
                        Logger.log(LogType.AUTH, account.getEmail() + " unable to namechange.");
                    }
                } else {
                    String token = "";
                    try {
                        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                        MicrosoftAuthResult result = authenticator.loginWithCredentials(account.getEmail(), account.getPassword());
                        token = result.getAccessToken();
                        if (APIUtil.canNameChange(token)) {
                            tokens.add(account.getEmail() + "-MCSNIPER-" + token);
                            Main.getDatabaseManager().setToken(account.getEmail(), token);
                            Main.getDatabaseManager().reserveAccount(account.getEmail());
                            Logger.log(LogType.AUTH, "Successfully logged into " + account.getEmail());
                            counter++;
                            Thread.sleep(30 * 1000);
                        } else {
                            Logger.log(LogType.AUTH, account.getEmail() + " unable to namechange.");
                        }
                    } catch (MicrosoftAuthenticationException e){
                        Logger.log(LogType.ERROR, "while logging into " + account.getEmail());
                        e.printStackTrace();
                    }
                }
            } else {
                //Auth with ratelimit
                String token = "";
                try {
                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    MicrosoftAuthResult result = authenticator.loginWithCredentials(account.getEmail(), account.getPassword());
                    token = result.getAccessToken();
                    if (APIUtil.canNameChange(token)) {
                        tokens.add(account.getEmail() + "-MCSNIPER-" + token);
                        Main.getDatabaseManager().setToken(account.getEmail(), token);
                        Main.getDatabaseManager().reserveAccount(account.getEmail());
                        Logger.log(LogType.AUTH, "Successfully logged into " + account.getEmail());
                        counter++;
                        Thread.sleep(30 * 1000);
                    } else {
                        Logger.log(LogType.AUTH, account.getEmail() + " unable to namechange.");
                    }
                } catch (MicrosoftAuthenticationException e){
                    Logger.log(LogType.ERROR, "while logging into " + account.getEmail());
                    e.printStackTrace();
                }
            }
        }
        Logger.log(LogType.AUTH, "Successfully authed " + counter + " accounts.");
        return tokens;
    }
}

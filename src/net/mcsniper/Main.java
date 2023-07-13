package net.mcsniper;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import lombok.Getter;
import lombok.Setter;
import net.mcsniper.bot.DiscordBot;
import net.mcsniper.config.ConfigManager;
import net.mcsniper.database.DatabaseManager;
import net.mcsniper.logs.LogManager;
import net.mcsniper.managers.ProxyManager;
import net.mcsniper.managers.SniperManager;
import net.mcsniper.tasks.RequestRunnable;
import net.mcsniper.utils.APIUtil;
import net.mcsniper.utils.AuthUtil;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import net.mcsniper.webhooks.WebhookManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


public class Main {
    @Getter private static DatabaseManager databaseManager;
    @Getter private static SniperManager manager;
    @Getter private static WebhookManager webhookManager;
    @Getter private static LogManager logManager;
    @Getter private static ConfigManager configManager;

    @Getter private static ArrayList<String> sends = new ArrayList<>();
    @Getter private static ArrayList<String> recvs = new ArrayList<>();
    @Getter private static ArrayList<Integer> codes = new ArrayList<>();

    public static void main(String[] args) throws LoginException, InterruptedException, FileNotFoundException {
        Logger.printLogo();
        databaseManager = new DatabaseManager("jdbc:mysql://127.0.0.1:3306/mcsniper?autoReconnect=true", "sniper", "test123");

        DiscordBot bot = new DiscordBot();
        Thread botThread = new Thread(bot);
        botThread.start();

        manager = new SniperManager();
        logManager = new LogManager();
        webhookManager = new WebhookManager();
        configManager = new ConfigManager();


    }

}

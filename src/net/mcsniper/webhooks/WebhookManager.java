package net.mcsniper.webhooks;

import net.mcsniper.utils.APIUtil;
import net.mcsniper.utils.DiscordWebhook;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class WebhookManager {

    private String hook = "yourwebhookhere";

    public WebhookManager(){
        Logger.log(LogType.GENERAL, "Successfully setup webhook manager!");
    }
    public void announceSnipe(String name){
        DiscordWebhook webhook = new DiscordWebhook(hook);
        webhook.setContent("<@&968238415973855242>");
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("**New Snipe** :gun:")
                .setColor(Color.RED)
                .addField("Name", name, false)
                .addField("Searches", String.valueOf(APIUtil.getSearches(name)), false)
                .setThumbnail("https://mc-heads.net/avatar/" + name)
                .setFooter("MCSniper @ " + new SimpleDateFormat("MM/dd/yyyy").format(new Date()), "https://media.discordapp.net/attachments/946111858962759700/968522863617404938/UUI.png"));
        try {
            webhook.execute();
            Logger.log(LogType.SUCCESS, "Sent webhook!");
        } catch (IOException e) {
            Logger.log(LogType.ERROR, "While sending webhook");
            throw new RuntimeException(e);
        }
    }
}

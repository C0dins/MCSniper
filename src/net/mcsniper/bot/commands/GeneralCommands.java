package net.mcsniper.bot.commands;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mcsniper.Main;
import net.mcsniper.managers.SniperManager;
import net.mcsniper.objects.Account;
import net.mcsniper.tasks.SniperTask;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileNotFoundException;

public class GeneralCommands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("snipe")) {
            String name = event.getOption("name").getAsString();
            try {
                Main.getManager().startSnipe(name);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (MicrosoftAuthenticationException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            event.reply("Successfully queued " + name).queue();

        } else if(event.getName().equals("unqueue")) {
            String name = event.getOption("name").getAsString();
            Main.getDatabaseManager().removeSnipe(name);
            for(SniperTask task : Main.getManager().getTasks()){
                if(task.getTarget() == name){
                    task.cancel();
                }
            }
            event.reply("Successfully removed " + name + " from queue!").queue();

        } else if(event.getName().equals("info")) {
            String msg = "";
            for(String snipe : Main.getDatabaseManager().getQueuedList()){
                msg += snipe + "\n";
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("MCSniper", null);
            eb.setColor(Color.red);
            eb.addField("Total Successful Snipes: ", String.valueOf(Main.getDatabaseManager().getTotalSnipedNames()), false);
            eb.addField("Total Failed Snipes: ", String.valueOf(Main.getDatabaseManager().getTotalFailedSnipes()), false);
            eb.addField("Microsoft Accounts", String.valueOf(Main.getDatabaseManager().getTotalAccountsMS()), false);
            eb.addField("Queued Names:", msg, true);
            event.replyEmbeds(eb.build()).queue();
        }
    }
}

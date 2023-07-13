package net.mcsniper.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mcsniper.Main;
import net.mcsniper.objects.Account;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import org.jetbrains.annotations.NotNull;

public class AccountManageCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("addmfa")) {
            String combo = event.getOption("combo").getAsString();
            String email = combo.split(":")[0];
            String password = combo.split(":")[1];
            Main.getDatabaseManager().addAccount(email, password);
            event.reply("Successfully added " + email).queue();

        } else if(event.getName().equals("removemfa")) {
            String email = event.getOption("email").getAsString();
            Main.getDatabaseManager().deleteAccount(email);
            event.reply("Successfully removed " + email).queue();

        } else if(event.getName().equals("resetreserve")) {
            int counter = 0;
            for(String account : Main.getDatabaseManager().getReservedAccounts()){
                Main.getDatabaseManager().resumeAccount(account);
                counter++;
            }

            event.reply("Successfully removed reserve off " + counter + " accounts!").queue();

        } else if(event.getName().equals("reserveaccount")) {
            String email = event.getOption("email").getAsString();
            Main.getDatabaseManager().reserveAccount(email);
            event.reply("Successfully reserved " + email).queue();

        } else if(event.getName().equals("resumeaccount")) {
            String email = event.getOption("email").getAsString();
            Main.getDatabaseManager().resumeAccount(email);
            event.reply("Successfully resumed " + email).queue();
        }
    }
}

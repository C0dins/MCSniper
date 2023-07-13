package net.mcsniper.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.mcsniper.bot.commands.AccountManageCommand;
import net.mcsniper.bot.commands.GeneralCommands;
import net.mcsniper.bot.listeners.ReadyListener;

import javax.security.auth.login.LoginException;

public class DiscordBot implements Runnable{

    private JDA client;

    @Override
    public void run() {
        try {
            client = JDABuilder.createDefault("none of your business")
                    .setStatus(OnlineStatus.ONLINE)
                    .setActivity(Activity.listening("Snipe requests!"))
                    .addEventListeners(new ReadyListener())
                    .addEventListeners(new AccountManageCommand())
                    .addEventListeners(new GeneralCommands())
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

        Guild guild = client.getGuildById("938904713989083286");
        guild.upsertCommand("addmfa", "Add Microsoft account to database")
                .addOption(OptionType.STRING, "combo", "Account combo", true).queue();
        guild.upsertCommand("removemfa", "Removes Microsoft account from database")
                .addOption(OptionType.STRING, "email", "Account email", true).queue();
        guild.upsertCommand("resetreserve", "Resets reserve off all accounts!").queue();
        guild.upsertCommand("reserveaccount", "Reserves an account")
                .addOption(OptionType.STRING, "email", "Account email", true).queue();
        guild.upsertCommand("resumeaccount", "Resumes an account")
                .addOption(OptionType.STRING, "email", "Account email", true).queue();
        guild.upsertCommand("snipe", "Queue a snipe!")
                .addOption(OptionType.STRING, "name", "name you want to snipe", true).queue();
        guild.upsertCommand("unqueue", "unqueue a snipe!")
                .addOption(OptionType.STRING, "name", "name you want to unqueue", true).queue();
        guild.upsertCommand("info", "Info on the sniper!").queue();
    }
}

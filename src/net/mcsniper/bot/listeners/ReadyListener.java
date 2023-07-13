package net.mcsniper.bot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.mcsniper.utils.LogType;
import net.mcsniper.utils.Logger;
import org.jetbrains.annotations.NotNull;

public class ReadyListener implements EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent)
            Logger.log(LogType.GENERAL, event.getJDA().getSelfUser().getName() + " is now online!");
    }
}

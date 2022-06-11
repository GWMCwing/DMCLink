package net.fabricmc.example.discordserver;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    private final DiscordBot client;

    MessageListener(DiscordBot _client){
        client = _client;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        client.LOGGER.info("Recieved: " + event.getMessage().getContentRaw());
        String msg = event.getMessage().getContentRaw();
        msg = "<" + event.getAuthor().getName() + "> " + msg;
        client.sendToServerChat(msg,ChatType.CHAT);
        super.onMessageReceived(event);
    }
}

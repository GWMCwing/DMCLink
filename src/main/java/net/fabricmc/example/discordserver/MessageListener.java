package net.fabricmc.example.discordserver;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    private final DiscordBot client;
    private final String CHAT_ID;

    MessageListener(DiscordBot _client, String _CHAT_ID){
        client = _client; CHAT_ID = _CHAT_ID;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(! event.getChannel().getId().equals(CHAT_ID)) return;
        //TODO Remove \n from the message or send as two message
        //TODO Ignore pin action and slash commands
        //TODO handle mentions
        client.LOGGER.info("Recieved: " + event.getMessage().getContentRaw());
        String msg = event.getMessage().getContentRaw();
        msg = "<" + event.getAuthor().getName() + "> " + msg;
        client.sendToServerChat(msg,ChatType.CHAT);
        super.onMessageReceived(event);
    }
}

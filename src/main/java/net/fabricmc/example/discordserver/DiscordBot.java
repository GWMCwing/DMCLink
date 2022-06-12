package net.fabricmc.example.discordserver;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.fabricmc.example.mcserver.MCServer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.w3c.dom.Text;

import javax.security.auth.login.LoginException;

public class DiscordBot extends ListenerAdapter {
    public final Logger LOGGER;
    private JDA client;
    private boolean initializeSuccessful = false;
    private static String CHAT_ID;
    private MCServer server;
    public DiscordBot(final Logger logger, final String TOKEN, final String _CHAT_ID, final MCServer _server){
        LOGGER = logger;
        if(TOKEN == null || _CHAT_ID == null || TOKEN.equals("null") || _CHAT_ID.equals("null")) {
            LOGGER.error("Default TOKEN and CHAT_ID is not set, Discord integration is Disabled\nPlease check the values provided and reload");
            return;
        }
        CHAT_ID = _CHAT_ID;
        server = _server;
        try {
            client = JDABuilder.createDefault(TOKEN)
                    .build();
        } catch (LoginException e) {
            LOGGER.error("Unable to initialize Discord Bot");
            throw new RuntimeException(e);
        }
        client.addEventListener(new MessageListener(this, CHAT_ID));
        initializeSuccessful = true;
    }
    public boolean sendToDiscordChat(String message, ChatType type){
        if(!initializeSuccessful) return false;
        //
        LOGGER.debug("Trying to send message " + message);
        TextChannel chatChannel = client.getChannelById(TextChannel.class,CHAT_ID);
        if (chatChannel == null) throw new AssertionError();
        chatChannel.sendMessage(message).queue();
        return true;
    }
    public boolean sendToServerChat(String message, ChatType type){
        if(server == null) return false;
        server.sendToServer(message,type);
        return true;
    }
    public void setServer(MCServer _server){
        server = _server;
    }


}

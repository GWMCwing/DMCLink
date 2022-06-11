package net.fabricmc.example.mcserver;

import net.fabricmc.example.discordserver.ChatType;
import net.minecraft.server.MinecraftServer;

public class MCServer {
    MinecraftServer server;
    public MCServer(MinecraftServer _server){
        server = _server;
    }
    private boolean executeCommandServer(String string){
        server.getCommandManager().execute(server.getCommandSource(),string);
        return true;
    }
    public boolean sendToServer(String msg, ChatType type){
        // execute do not need tellraw
        return executeCommandServer("tellraw @a {\"text\":\""+msg+"\"}");
    }
}

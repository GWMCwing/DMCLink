package net.fabricmc.example;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.example.discordserver.ChatType;
import net.fabricmc.example.discordserver.DiscordBot;
import net.fabricmc.example.mcserver.MCServer;
import net.fabricmc.example.util.SimpleConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordMCLink implements DedicatedServerModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	private static String CONFIG_STRING = "DiscordMCLink_Config";
	public static final Logger LOGGER = LoggerFactory.getLogger("DiscordMCLink");
	private static SimpleConfig CONFIG = SimpleConfig.of(CONFIG_STRING).provider(
			(String filename) ->{
				return "#config\n#Double quote is not required for string\nTOKEN=null\nCHAT_ID=null";
			}
	).request();
	private static final String TOKEN = CONFIG.getOrDefault("TOKEN",null);
	private static final String CHAT_ID = CONFIG.getOrDefault("CHAT_ID",null);
	private static MCServer server;
	private static DiscordBot discordBot = null;
	private static boolean initializeFail = false;
	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//? get server instance
		ServerTickEvents.START_SERVER_TICK.register(
				server1 ->{
					server = new MCServer(server1);
					//! bot is loaded after the server start
					if(discordBot == null && !initializeFail)
						try {
							discordBot = new DiscordBot(LOGGER, TOKEN, CHAT_ID, server);
						} catch(RuntimeException e){
							LOGGER.error("Unable to initialize discord bot");
							initializeFail = true;
						}

				}
		);

		ServerMessageEvents.CHAT_MESSAGE.register(
				(message, sender, typeKey) -> {
					// only global chat is captured
					if(typeKey == MessageType.CHAT) {
						String msg, playerName;
						msg = message.raw().signedContent().getContent().toString();
						msg = msg.substring("literal{".length(),msg.length()-1);
						playerName = sender.getEntityName();
						LOGGER.debug("Chat message: <" + playerName + "> " + msg);
						discordBot.setServer(server);
						try {
							discordBot.sendToDiscordChat("<" + playerName + "> " + msg, ChatType.CHAT);
						} catch(AssertionError e){
							LOGGER.warn("Cannot send message, wrong channel!");
						}

					}
				}
		);
	}

	
}

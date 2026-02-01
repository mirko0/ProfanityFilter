package com.mcodelogic.profanity;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import com.mcodelogic.profanity.commands.ReloadProfanityWords;
import com.mcodelogic.profanity.config.KConfig;
import com.mcodelogic.profanity.event.PlayerChatListener;
import com.mcodelogic.profanity.manager.ProfanityManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KMain extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.getLogger();
    private final Config<KConfig> pluginConfiguration;
    private final ProfanityManager profanityManager;
    private final PlayerChatListener chatEvent;
    public KMain(@NonNullDecl JavaPluginInit init) {
        super(init);
        pluginConfiguration = this.withConfig(Constants.PLUGIN_NAME_FULL, KConfig.CODEC);
        this.profanityManager = new ProfanityManager(pluginConfiguration, getDataDirectory());
        this.chatEvent = new PlayerChatListener(profanityManager);
    }

    @Override
    protected void setup() {
        super.setup();
        LOGGER.at(Level.INFO).log("Initializing " + Constants.PLUGIN_NAME_FULL + " Plugin...");
        LOGGER.at(Level.INFO).log("Saving Configuration");
        try {
            if (!new File(getDataDirectory().resolve("ProfanityFilter.json").toUri()).exists()) {
                pluginConfiguration.save();
            }
            pluginConfiguration.load();
        } catch (Exception e) {
            Logger.getLogger(KMain.class.getName()).log(Level.SEVERE, "Error while loading configuration", e);
        }
    }

    @Override
    protected void start() {
        LOGGER.at(Level.INFO).log("Starting " + Constants.PLUGIN_NAME_FULL + " Plugin...");
        getEventRegistry().registerGlobal(PlayerChatEvent.class, chatEvent::onPlayerChat);
        getCommandRegistry().registerCommand(new ReloadProfanityWords(profanityManager, getDataDirectory()));
    }

    @Override
    protected void shutdown() {
        LOGGER.at(Level.INFO).log("Stopping " + Constants.PLUGIN_NAME_FULL + " Plugin...");
    }

    public KConfig getConfiguration() {
        return pluginConfiguration.get();
    }
}

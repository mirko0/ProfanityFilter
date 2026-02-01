package com.mcodelogic.profanity.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.mcodelogic.profanity.manager.ProfanityManager;
import com.mcodelogic.profanity.manager.ProfanityWordLoader;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.nio.file.Path;


public class ReloadProfanityWords extends CommandBase {
    private ProfanityManager manager;
    private Path baseDir;
    public ReloadProfanityWords(ProfanityManager manager, Path baseDir) {
        super("reloadprofanitywords", "Reloads the profanity words from the data folder.");
        requirePermission("profanityfilter.reload");
        this.manager = manager;
        this.baseDir = baseDir;
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        manager.loadWords(ProfanityWordLoader.load(baseDir));
        commandContext.sendMessage(Message.raw("Successfully reloaded profanity words."));
    }
}

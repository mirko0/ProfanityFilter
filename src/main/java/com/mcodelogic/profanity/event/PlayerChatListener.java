package com.mcodelogic.profanity.event;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.mcodelogic.profanity.manager.ProfanityManager;

import java.awt.*;

public class PlayerChatListener {

    private ProfanityManager manager;

    public PlayerChatListener(ProfanityManager manager) {
        this.manager = manager;
    }

    public void onPlayerChat(PlayerChatEvent event) {
        PlayerRef sender = event.getSender();
        Ref<EntityStore> ref = sender.getReference();
        if (ref == null || !ref.isValid()) return;
//        Store<EntityStore> store = ref.getStore();
//        Player player = store.getComponent(ref, Player.getComponentType());
//        if (player == null) return;
//        if (player.hasPermission(manager.getConfig().get().getBypassPermission())) return;
        String content = event.getContent();
        boolean blocked = manager.checkMessage(content);

        if (blocked) {
            event.setCancelled(true);
            sender.sendMessage(Message.raw("Hey, you are not allowed to use profanity words in chat!").color(Color.RED));
        }
    }


}

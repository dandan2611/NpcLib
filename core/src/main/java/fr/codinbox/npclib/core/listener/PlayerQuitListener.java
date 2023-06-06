package fr.codinbox.npclib.core.listener;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerQuitListener implements Listener {

    private final NpcHolder holder;

    public PlayerQuitListener(NpcHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    private void onJoin(PlayerQuitEvent event) {
        var player = event.getPlayer();

        holder.getRenderedNpcs(player).forEach(npc -> Objects.requireNonNull(npc.getViewer(player.getUniqueId())).setRendered(false));
    }

}

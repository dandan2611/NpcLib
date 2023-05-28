package fr.codinbox.npclib.core.listener;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final NpcHolder holder;

    public PlayerQuitListener(NpcHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    private void onJoin(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var location = player.getLocation();

        holder.getNpcsInWorld(location.getWorld()).forEach(npc -> holder.updateVisibility(npc, player));
    }

}

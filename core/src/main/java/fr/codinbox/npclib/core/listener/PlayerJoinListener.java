package fr.codinbox.npclib.core.listener;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final NpcHolder holder;

    public PlayerJoinListener(NpcHolder holder) {
        this.holder = holder;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var location = player.getLocation();

        holder.getNpcsInWorld(location.getWorld()).forEach(npc -> holder.updateVisibility(npc, player));
    }

}

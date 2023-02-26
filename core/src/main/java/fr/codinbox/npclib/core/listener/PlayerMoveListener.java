package fr.codinbox.npclib.core.listener;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final NpcHolder holder;

    public PlayerMoveListener(NpcHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var location = player.getLocation();

        holder.getNpcsInWorld(location.getWorld()).forEach(npc -> holder.tickNpc(npc, player));
    }

}

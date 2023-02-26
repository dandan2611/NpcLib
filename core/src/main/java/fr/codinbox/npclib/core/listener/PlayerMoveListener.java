package fr.codinbox.npclib.core.listener;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final NpcHolder holder;

    private final HashMap<UUID, Location> lastLocations = new HashMap<>();

    public PlayerMoveListener(NpcHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        var location = player.getLocation();

        if (!lastLocations.containsKey(player.getUniqueId()))
            lastLocations.put(player.getUniqueId(), location);
        var lastLocation = this.lastLocations.get(player.getUniqueId());
        var sameWorld = lastLocation.getWorld() == location.getWorld();
        if (sameWorld && lastLocation.distance(location) < 1.0D)
            return;
        this.lastLocations.put(player.getUniqueId(), location);

        holder.getNpcsInWorld(location.getWorld()).forEach(npc -> holder.tickNpc(npc, player));
    }

    @EventHandler
    private void onDimensionChange(PlayerChangedWorldEvent event) {
        var player = event.getPlayer();
        var location = player.getLocation();
        this.lastLocations.put(player.getUniqueId(), location);
        holder.getShownNpcs(player).forEach(npc -> holder.setRendered(npc, player, false));
        holder.getNpcsInWorld(location.getWorld()).forEach(npc -> holder.tickNpc(npc, player));
    }

}

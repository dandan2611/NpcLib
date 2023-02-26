package fr.codinbox.npclib.core.impl.npc.holder;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.core.NpcLibPlugin;
import fr.codinbox.npclib.core.impl.npc.NpcImpl;
import fr.codinbox.npclib.core.listener.PlayerJoinListener;
import fr.codinbox.npclib.core.listener.PlayerMoveListener;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class NpcHolderImpl implements NpcHolder {

    @NotNull
    private final Plugin plugin;

    @NotNull
    private final HashMap<Integer, Npc> npcs;

    private final HashMap<World, Set<Npc>> worldNpcs;

    private final HashMap<UUID, Set<Npc>> playerRenderedNpcs;

    public NpcHolderImpl(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.npcs = new HashMap<>();
        this.worldNpcs = new HashMap<>();
        this.playerRenderedNpcs = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), plugin);
    }

    private int generateNpcId() {
        int id = 100000; // TODO: REWORK
        while (this.npcs.containsKey(id)) id++;
        return id;
    }

    @Override
    public @NotNull Npc createNpc(@NotNull NpcConfig config) {
        int id = this.generateNpcId();
        UUID uuid = UUID.randomUUID();
        var npc = new NpcImpl(config.getLocation(), config.getSkin(), id, uuid, config.isGlobal());
        this.npcs.put(npc.getEntityId(), npc);
        this.worldNpcs.computeIfAbsent(npc.getLocation().getWorld(), k -> new HashSet<>()).add(npc);
        return npc;
    }

    @Override
    public void destroyNpc(@NotNull Npc npc) {

    }

    @Override
    public @NotNull Set<@NotNull Npc> getNpcs() {
        return null;
    }

    @Override
    public @NotNull Set<@NotNull Npc> getNpcsInWorld(@NotNull World world) {
        return this.worldNpcs.getOrDefault(world, Set.of());
    }

    @Override
    public boolean isRendered(@NotNull Npc npc, @NotNull Player player) {
        return this.playerRenderedNpcs.getOrDefault(player.getUniqueId(), Set.of()).contains(npc);
    }

    @Override
    public void setRendered(@NotNull Npc npc, @NotNull Player player, boolean rendered) {
        if (rendered) this.playerRenderedNpcs.computeIfAbsent(player.getUniqueId(), k -> Set.of()).add(npc);
        else this.playerRenderedNpcs.computeIfAbsent(player.getUniqueId(), k -> Set.of()).remove(npc);
    }

    @Override
    public void tickNpc(@NotNull Npc npc, @NotNull Player player) {
        if (!npc.isGlobal() && npc.getViewers().contains(player.getUniqueId()))
            return;

        var playerLocation = player.getLocation();
        var npcLocation = npc.getLocation();
        var distance = playerLocation.getWorld().getKey().equals(npcLocation.getWorld().getKey())
                ? playerLocation.distance(npcLocation)
                : Double.MAX_VALUE;

        if (distance <= NpcLibPlugin.RENDER_DISTANCE && !this.isRendered(npc, player)) {
            NpcLibPlugin.station().createPlayerInfoPacket(npc, null, player);
            NpcLibPlugin.station().createPlayerSpawnPacket(npc, null, player);
        } else {
            // TODO: If npc is shown, remove it
        }
    }

}

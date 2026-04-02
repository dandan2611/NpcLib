package fr.codinbox.npclib.core.impl.npc.holder;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.core.impl.npc.NpcImpl;
import fr.codinbox.npclib.core.impl.packet.NpcPacket;
import fr.codinbox.npclib.core.impl.packet.listener.NpcInteractionPacketListener;
import fr.codinbox.npclib.core.listener.PlayerJoinListener;
import fr.codinbox.npclib.core.listener.PlayerMoveListener;
import fr.codinbox.npclib.core.listener.PlayerQuitListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class NpcHolderImpl implements NpcHolder {

    private static final AtomicInteger NEXT_NPC_ID = new AtomicInteger(Integer.MAX_VALUE / 2);
    private static final ConcurrentLinkedQueue<Integer> FREE_IDS = new ConcurrentLinkedQueue<>();

    @NotNull
    private final Plugin plugin;

    @NotNull
    private final ConcurrentHashMap<Integer, Npc> npcs;

    private final ConcurrentHashMap<World, Set<Npc>> worldNpcs;

    private final ConcurrentHashMap<UUID, Long> interactions = new ConcurrentHashMap<>();

    private final NpcHolderImpl instance = this;

    public NpcHolderImpl(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.npcs = new ConcurrentHashMap<>();
        this.worldNpcs = new ConcurrentHashMap<>();

        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), plugin);

        PacketEvents.getAPI()
                    .getEventManager()
                    .registerListener(new NpcInteractionPacketListener(plugin, this), PacketListenerPriority.LOWEST);

        new BukkitRunnable() {
            @Override
            public void run() {
                updateNpcs();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private int generateNpcId() {
        Integer recycled = FREE_IDS.poll();
        return recycled != null ? recycled : NEXT_NPC_ID.getAndIncrement();
    }

    /**
     * Update NPCs lookAt
     */
    private void updateNpcs() {
        this.npcs.values().forEach(npc -> {
            if (npc.isLookingAtPlayer()) {
                final var viewers = npc.getViewers().values().stream().filter(NpcViewer::isRendered);
                viewers.forEach(viewer -> {
                    final var player = Bukkit.getPlayer(viewer.getUuid());
                    if (player == null)
                        return;
                    final var location = player.getLocation();
                    if (npc.getLocation().distance(location) > npc.getLookingAtPlayerRange())
                        return;

                    final var direction = location.toVector().subtract(npc.getLocation().toVector()).normalize();
                    npc.getLocation().setDirection(direction);

                    NpcPacket.HEAD_ROTATION.send(PacketEvents.getAPI(), player, npc);
                });
            }
        });
    }

    @Override
    public @NotNull Npc createNpc(@NotNull NpcConfig config) {
        int id = this.generateNpcId();
        var npc = new NpcImpl(this, id, config);
        this.npcs.put(npc.getEntityId(), npc);
        this.worldNpcs.computeIfAbsent(npc.getWorld(), k -> ConcurrentHashMap.newKeySet()).add(npc);
        // Update npc for players in the same world
        config.getLocation().getWorld().getPlayers().forEach(npc::renderFor);
        return npc;
    }

    @Override
    public void destroyNpc(@NotNull Npc npc) {
        this.npcs.remove(npc.getEntityId());
        this.worldNpcs.computeIfAbsent(npc.getWorld(), k -> ConcurrentHashMap.newKeySet()).remove(npc);
        npc.getViewers().values().forEach(viewer -> viewer.setRendered(false));
        FREE_IDS.offer(npc.getEntityId());
    }

    @Override
    public @NotNull Set<@NotNull Npc> getNpcs() {
        return Set.copyOf(this.npcs.values());
    }

    @Override
    public @NotNull Set<@NotNull Npc> getNpcsInWorld(@NotNull World world) {
        return Set.copyOf(this.worldNpcs.getOrDefault(world, Set.of()));
    }

    @Override
    public boolean isRendered(@NotNull Npc npc, @NotNull Player player) {
        return npc.isRenderedFor(player);
    }

    @Override
    public void updateVisibility(@NotNull Npc npc, @NotNull Player player) {
        npc.renderFor(player.getUniqueId());
    }

    @Override
    public @NotNull Set<@NotNull Npc> getRenderedNpcs(@NotNull Player player) {
        return this.getRenderedNpcs(player.getUniqueId());
    }

    @Override
    public @NotNull Set<@NotNull Npc> getRenderedNpcs(@NotNull UUID player) {
        return this.npcs.values()
                        .stream()
                        .filter(npc -> npc.isRenderedFor(player))
                        .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Set<@NotNull UUID> getRenderedPlayers(@NotNull Npc npc) {
        return npc.getRenderers();
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

}

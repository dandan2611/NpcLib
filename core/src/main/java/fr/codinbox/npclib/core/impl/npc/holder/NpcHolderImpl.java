package fr.codinbox.npclib.core.impl.npc.holder;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
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

    private final HashMap<UUID, Long> interactions = new HashMap<>();

    private final NpcHolderImpl instance = this;

    public NpcHolderImpl(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.npcs = new HashMap<>();
        this.worldNpcs = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), plugin);

        ProtocolLibrary.getProtocolManager().addPacketListener(new NpcInteractionPacketListener(plugin, this));

        new BukkitRunnable() {
            @Override
            public void run() {
                updateNpcs();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private int generateNpcId() {
        int id = 100000; // TODO: Properly generate npc id
        while (this.npcs.containsKey(id)) id++;
        return id;
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
                    location.setDirection(direction);

                    var packet = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);

                    packet.getIntegers().write(0, npc.getEntityId());
                    packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

                    packet = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
                    packet.getIntegers().write(0, npc.getEntityId());
                    packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
                    packet.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
                });
            }
        });
    }

    @Override
    public @NotNull Npc createNpc(@NotNull NpcConfig config) {
        int id = this.generateNpcId();
        UUID uuid = UUID.randomUUID();
        var npc = new NpcImpl(this, id, uuid, config);
        this.npcs.put(npc.getEntityId(), npc);
        this.worldNpcs.computeIfAbsent(npc.getWorld(), k -> new HashSet<>()).add(npc);
        return npc;
    }

    @Override
    public void destroyNpc(@NotNull Npc npc) {
        this.npcs.remove(npc.getEntityId());
        this.worldNpcs.computeIfAbsent(npc.getWorld(), k -> new HashSet<>()).remove(npc);
        npc.getViewers().values().forEach(viewer -> viewer.setRendered(false));
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
        return this.npcs.values().stream()
                .filter(npc -> npc.isRenderedFor(player))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Set<@NotNull UUID> getRenderedPlayers(@NotNull Npc npc) {
        return npc.getRenderers();
    }

}

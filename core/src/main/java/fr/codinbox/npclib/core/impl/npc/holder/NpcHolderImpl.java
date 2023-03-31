package fr.codinbox.npclib.core.impl.npc.holder;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.holder.NpcHolderConfiguration;
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

    private final HashMap<UUID, Long> interactions = new HashMap<>();

    private final NpcHolderImpl instance = this;

    private final NpcHolderConfiguration configuration;

    public NpcHolderImpl(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.npcs = new HashMap<>();
        this.worldNpcs = new HashMap<>();
        this.configuration = new NpcHolderConfigurationImpl();

        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), plugin);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                var packet = event.getPacket();
                var id = packet.getIntegers().read(0);
                var action = packet.getEnumEntityUseActions().read(0);
                var player = event.getPlayer();

                if (interactions.containsKey(player.getUniqueId())
                        && System.currentTimeMillis() - interactions.get(player.getUniqueId()) < 100) {
                    event.setCancelled(true);
                    return;
                }
                interactions.put(player.getUniqueId(), System.currentTimeMillis());

                final NpcClickedEvent.InteractionType interactionType = switch (action.getAction()) {
                    case ATTACK -> NpcClickedEvent.InteractionType.ATTACK;
                    case INTERACT -> NpcClickedEvent.InteractionType.INTERACT;
                    case INTERACT_AT -> NpcClickedEvent.InteractionType.INTERACT_AT;
                };

                getNpcsInWorld(event.getPlayer().getWorld()).stream().filter(npc -> npc.getEntityId() == id).forEach(npc -> {
                    var e = new NpcClickedEvent(npc, player, instance, interactionType);
                    npc.callClickedListeners(e);
                    event.setCancelled(true);
                });
            }
        });
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
        var npc = new NpcImpl(this, id, uuid, config);
        this.npcs.put(npc.getEntityId(), npc);
        this.worldNpcs.computeIfAbsent(npc.getLocationReactive().get().getWorld(), k -> new HashSet<>()).add(npc);
        return npc;
    }

    @Override
    public void destroyNpc(@NotNull Npc npc) {
        this.npcs.remove(npc.getEntityId());
        this.worldNpcs.computeIfAbsent(npc.getLocationReactive().get().getWorld(), k -> new HashSet<>()).remove(npc);
        npc.getViewersReactive().values().forEach(viewer -> {
            viewer.render();;
        });
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
    public void performChecks(@NotNull Npc npc, @NotNull Player player) {
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
    public @NotNull NpcHolderConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public @NotNull Set<@NotNull UUID> getRenderedPlayers(@NotNull Npc npc) {
        return npc.getRenderers();
    }

}

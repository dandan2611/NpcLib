package fr.codinbox.npclib.core.impl.packet.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class NpcInteractionPacketListener extends PacketAdapter {

    private final @NotNull NpcHolder holder;

    private final @NotNull HashMap<UUID, Long> interactions = new HashMap<>();

    public NpcInteractionPacketListener(@NotNull Plugin plugin, @NotNull NpcHolder holder) {
        super(plugin, PacketType.Play.Client.USE_ENTITY);
        this.holder = holder;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        var packet = event.getPacket();
        var id = packet.getIntegers().read(0);
        var action = packet.getEnumEntityUseActions().read(0);
        var player = event.getPlayer();

        if (interactions.containsKey(player.getUniqueId())
                && System.currentTimeMillis() - interactions.get(player.getUniqueId()) < 100) {
            //event.setCancelled(true);
            return;
        }
        interactions.put(player.getUniqueId(), System.currentTimeMillis());

        final NpcClickedEvent.InteractionType interactionType = switch (action.getAction()) {
            case ATTACK -> NpcClickedEvent.InteractionType.ATTACK;
            case INTERACT -> NpcClickedEvent.InteractionType.INTERACT;
            case INTERACT_AT -> NpcClickedEvent.InteractionType.INTERACT_AT;
        };

        holder.getNpcsInWorld(event.getPlayer().getWorld()).stream().filter(npc -> npc.getEntityId() == id).forEach(npc -> {
            var e = new NpcClickedEvent(npc, player, holder, interactionType);
            npc.callClickedListeners(e);
            //event.setCancelled(true);
        });
    }

}

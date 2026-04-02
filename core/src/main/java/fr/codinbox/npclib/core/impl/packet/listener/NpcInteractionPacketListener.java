package fr.codinbox.npclib.core.impl.packet.listener;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class NpcInteractionPacketListener implements PacketListener {

    private final @NotNull NpcHolder holder;

    private final @NotNull HashMap<UUID, Long> interactions = new HashMap<>();

    public NpcInteractionPacketListener(@NotNull Plugin plugin, @NotNull NpcHolder holder) {
        this.holder = holder;
    }

    @Override
    public void onPacketReceive(final @NotNull PacketReceiveEvent event) {
        final User user = event.getUser();

        if (Client.INTERACT_ENTITY != event.getPacketType())
            return;

        final WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
        final int id = packet.getEntityId();
        final InteractAction action = packet.getAction();

        if (interactions.containsKey(user.getUUID())
            && System.currentTimeMillis() - interactions.get(user.getUUID()) < 100) {
            // event.setCancelled(true);
            return;
        }
        interactions.put(user.getUUID(), System.currentTimeMillis());

        final NpcClickedEvent.InteractionType interactionType = switch (action) {
            case ATTACK -> NpcClickedEvent.InteractionType.ATTACK;
            case INTERACT -> NpcClickedEvent.InteractionType.INTERACT;
            case INTERACT_AT -> NpcClickedEvent.InteractionType.INTERACT_AT;
        };

        final Player player = event.getPlayer();

        holder.getNpcsInWorld(player.getWorld()).stream().filter(npc -> npc.getEntityId() == id).forEach(npc -> {
            var e = new NpcClickedEvent(npc, player, holder, interactionType);
            npc.callClickedListeners(e);
            event.setCancelled(true);
        });
    }

}

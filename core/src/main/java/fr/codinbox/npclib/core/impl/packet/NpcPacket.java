package fr.codinbox.npclib.core.impl.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import fr.codinbox.npclib.api.npc.Npc;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface NpcPacket {

    NpcPacket PLAYER_INFO_ADD = (protocolManager, player, npc) -> {
        var profile = new WrappedGameProfile(npc.getUUID(), npc.getName());
        profile.getProperties().clear();
        if (npc.getSkin() != null) {
            profile.getProperties().put("textures",
                    new WrappedSignedProperty("textures",
                            npc.getSkin().getValue(),
                            npc.getSkin().getSignature()));
        }

        var packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0,
                Set.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER, EnumWrappers.PlayerInfoAction.UPDATE_LISTED));
        packet.getPlayerInfoDataLists().write(1, Collections.singletonList(
                new PlayerInfoData(
                        npc.getUUID(),
                        0,
                        false,
                        EnumWrappers.NativeGameMode.CREATIVE,
                        profile,
                        null
                )
        ));
        protocolManager.sendServerPacket(player, packet);
    };

    NpcPacket PLAYER_INFO_REMOVE = (protocolManager, player, npc) -> {
        var packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        packet.getUUIDLists()
                .write(0, Collections.singletonList(npc.getUUID()));
        protocolManager.sendServerPacket(player, packet);
    };

    NpcPacket PLAYER_SPAWN = (protocolManager, player, npc) -> {
        var location = npc.getLocation();
        var packet = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        packet.getIntegers().write(0, npc.getEntityId());
        packet.getUUIDs().write(0, npc.getUUID());
        packet.getDoubles().write(0 ,location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());
        packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        packet.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        protocolManager.sendServerPacket(player, packet);
    };

    NpcPacket PLAYER_DESPAWN = (protocolManager, player, npc) -> {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getModifier().write(0, new IntArrayList(new int[]{npc.getEntityId()}));
        protocolManager.sendServerPacket(player, packet);
    };

    NpcPacket HEAD_ROTATION = (protocolManager, player, npc) -> {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        var location = npc.getLocation();

        packet.getIntegers().write(0, npc.getEntityId());
        packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        protocolManager.sendServerPacket(player, packet);

        packet = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
        packet.getIntegers().write(0, npc.getEntityId());
        packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        packet.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        protocolManager.sendServerPacket(player, packet);
    };

    NpcPacket ENTITY_METADATA = (protocolManager, player, npc) -> {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, npc.getEntityId());
        final var watcher = new WrappedDataWatcher();
        final var serializer = WrappedDataWatcher.Registry.get(Byte.class);
        final var object = new WrappedDataWatcher.WrappedDataWatcherObject(17, serializer);
        watcher.setObject(object, npc.getDisplayedSkinParts().getValue());

        final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        watcher.getWatchableObjects().forEach(wrappedDataValue -> {
            final WrappedDataWatcher.WrappedDataWatcherObject obh = wrappedDataValue.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(
                    obh.getIndex(),
                    obh.getSerializer(),
                    wrappedDataValue.getRawValue()
            ));
        });

        packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        protocolManager.sendServerPacket(player, packet);
    };

    void send(@NotNull ProtocolManager protocolManager, @NotNull Player player, @NotNull Npc npc);

}

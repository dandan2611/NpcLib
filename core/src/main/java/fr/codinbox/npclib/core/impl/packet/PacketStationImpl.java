package fr.codinbox.npclib.core.impl.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.packet.PacketStation;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public class PacketStationImpl implements PacketStation<PacketContainer> {

    @Override
    public boolean createPlayerInfoPacket(@NotNull Npc npc,
                                          @Nullable Consumer<PacketContainer> preprocessor,
                                          @NotNull Player player) {
        var profile = new WrappedGameProfile(npc.getUUID(), npc.getNameReactive().get());
        profile.getProperties().clear();
        profile.getProperties().put("textures",
                new WrappedSignedProperty("textures",
                        npc.getSkinReactive().get().getValue(),
                        npc.getSkinReactive().get().getSignature()));

        var packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0,
                Set.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER, EnumWrappers.PlayerInfoAction.UPDATE_LISTED));
        packet.getPlayerInfoDataLists().write(1, Collections.singletonList(
                new PlayerInfoData(
                        npc.getUUID(),
                        0,
                        false,
                        EnumWrappers.NativeGameMode.ADVENTURE,
                        profile,
                        null,
                        null
                )
        ));
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    @Override
    public boolean createPlayerSpawnPacket(@NotNull Npc npc,
                                           @Nullable Consumer<PacketContainer> preprocessor,
                                           @NotNull Player player) {
        var packet = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        packet.getIntegers().write(0, npc.getEntityId());
        packet.getUUIDs().write(0, npc.getUUID());
        packet.getDoubles().write(0, npc.getLocationReactive().get().getX());
        packet.getDoubles().write(1, npc.getLocationReactive().get().getY());
        packet.getDoubles().write(2, npc.getLocationReactive().get().getZ());
        packet.getBytes().write(0, (byte) (npc.getLocationReactive().get().getYaw() * 256.0F / 360.0F));
        packet.getBytes().write(1, (byte) (npc.getLocationReactive().get().getPitch() * 256.0F / 360.0F));
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    @Override
    public boolean createPlayerInfoRemovePacket(@NotNull Npc npc,
                                                @Nullable Consumer<PacketContainer> preprocessor,
                                                @NotNull Player player) {
        var packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        packet.getUUIDLists()
                .write(0, Collections.singletonList(npc.getUUID()));
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    @Override
    public boolean createPlayerDespawnPacket(@NotNull Npc npc,
                                             @Nullable Consumer<PacketContainer> preprocessor,
                                             @NotNull Player player) {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getModifier().write(0, new IntArrayList(new int[]{npc.getEntityId()}));
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    @Override
    public boolean createEntityAnimationPacket(@NotNull Npc npc,
                                               @NotNull AnimationType animationType,
                                               @Nullable Consumer<PacketContainer> preprocessor,
                                               @NotNull Player player) {
        var packet = new PacketContainer(PacketType.Play.Server.ANIMATION);
        packet.getIntegers().write(0, npc.getEntityId());
        packet.getIntegers().write(1, animationType.getId());
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    @Override
    public boolean createEntityHeadRotationPacket(@NotNull Npc npc, @Nullable Consumer<PacketContainer> preprocessor, @NotNull Player player) {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        var location = npc.getLocationReactive().get();

        packet.getIntegers().write(0, npc.getEntityId());
        packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

    @Override
    public boolean createEntityMetadataPacket(@NotNull Npc npc,
                                              @Nullable Consumer<PacketContainer> preprocessor,
                                              @NotNull Player player) {
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        // Display all skin parts
        byte finalByte = 0;
        for (var i = 0; i < 8; i++)
            finalByte |= 1 << i;
        packet.getModifier().writeDefaults();
        packet.getIntegers().write(0, npc.getEntityId());
        var watcher = new WrappedDataWatcher();
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(17, WrappedDataWatcher.Registry.get(Byte.class)), finalByte);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

}

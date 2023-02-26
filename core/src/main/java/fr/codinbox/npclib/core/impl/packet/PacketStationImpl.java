package fr.codinbox.npclib.core.impl.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.packet.PacketStation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public class PacketStationImpl implements PacketStation<PacketContainer> {

    @Override
    public boolean createPlayerInfoPacket(@NotNull Npc npc,
                                          @Nullable Consumer<PacketContainer> preprocessor,
                                          @NotNull Player player) {
        var profile = new WrappedGameProfile(npc.getUUID(), "");
        profile.getProperties().clear();
        profile.getProperties().put("textures",
                new WrappedSignedProperty("textures",
                        npc.getSkin().getValue(),
                        npc.getSkin().getSignature()));

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
        packet.getDoubles().write(0, npc.getLocation().getX());
        packet.getDoubles().write(1, npc.getLocation().getY());
        packet.getDoubles().write(2, npc.getLocation().getZ());
        packet.getBytes().write(0, (byte) (npc.getLocation().getYaw() * 256.0F / 360.0F));
        packet.getBytes().write(1, (byte) (npc.getLocation().getPitch() * 256.0F / 360.0F));
        if (preprocessor != null)
            preprocessor.accept(packet);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }

}
package fr.codinbox.npclib.core.impl.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.name.NpcName;
import fr.codinbox.npclib.api.npc.name.NpcNameFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;

public final class NpcNamePacket {

    private static final int DEFAULT_BACKGROUND_COLOR = 0x40000000;

    private NpcNamePacket() {
    }

    public static void spawn(Player player, Npc npc, int entityId, UUID entityUuid, NpcName name) {
        Location location = npc.getLocation();
        var packet = new WrapperPlayServerSpawnEntity(
                entityId,
                Optional.of(entityUuid),
                EntityTypes.TEXT_DISPLAY,
                new Vector3d(
                        location.getX() + name.getOffsetX(),
                        location.getY() + name.getOffsetY(),
                        location.getZ() + name.getOffsetZ()
                ),
                0.0f,
                0.0f,
                0.0f,
                0,
                Optional.empty()
        );
        send(player, packet);
        sendMetadata(player, entityId, name, name.getFrames().getFirst(), true);
    }

    public static void updateFrame(Player player, int entityId, NpcName name, NpcNameFrame frame) {
        sendMetadata(player, entityId, name, frame, false);
    }

    public static void destroy(Player player, int entityId) {
        send(player, new WrapperPlayServerDestroyEntities(entityId));
    }

    private static void sendMetadata(Player player, int entityId, NpcName name, NpcNameFrame frame, boolean initial) {
        send(player, new WrapperPlayServerEntityMetadata(entityId, metadata(name, frame, initial)));
    }

    static List<EntityData<?>> metadata(NpcName name, NpcNameFrame frame, boolean initial) {
        List<EntityData<?>> data = new ArrayList<>();
        data.add(new EntityData<>(8, EntityDataTypes.INT, 0));
        data.add(new EntityData<>(9, EntityDataTypes.INT, name.getInterpolationDuration()));
        addTransformation(data, frame.transformation());
        if (initial) {
            data.add(new EntityData<>(15, EntityDataTypes.BYTE, billboardId(name.getBillboard())));
            data.add(new EntityData<>(17, EntityDataTypes.FLOAT, name.getViewRange()));
            data.add(new EntityData<>(24, EntityDataTypes.INT, name.getLineWidth()));
            data.add(new EntityData<>(25, EntityDataTypes.INT, name.getBackgroundColor() == null
                    ? DEFAULT_BACKGROUND_COLOR
                    : name.getBackgroundColor().asARGB()));
            data.add(new EntityData<>(27, EntityDataTypes.BYTE, styleFlags(name)));
        }
        data.add(new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, frame.text()));
        data.add(new EntityData<>(26, EntityDataTypes.BYTE, frame.opacity()));
        return data;
    }

    private static void addTransformation(List<EntityData<?>> data, Transformation transformation) {
        org.joml.Vector3f translation = transformation.getTranslation();
        org.joml.Vector3f scale = transformation.getScale();
        data.add(new EntityData<>(11, EntityDataTypes.VECTOR3F,
                new Vector3f(translation.x, translation.y, translation.z)));
        data.add(new EntityData<>(12, EntityDataTypes.VECTOR3F,
                new Vector3f(scale.x, scale.y, scale.z)));
        data.add(new EntityData<>(13, EntityDataTypes.QUATERNION, quaternion(transformation.getLeftRotation())));
        data.add(new EntityData<>(14, EntityDataTypes.QUATERNION, quaternion(transformation.getRightRotation())));
    }

    private static Quaternion4f quaternion(Quaternionf quaternion) {
        return new Quaternion4f(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }

    private static byte billboardId(Display.Billboard billboard) {
        return switch (billboard) {
            case FIXED -> 0;
            case VERTICAL -> 1;
            case HORIZONTAL -> 2;
            case CENTER -> 3;
        };
    }

    private static byte styleFlags(NpcName name) {
        int flags = 0;
        if (name.isShadowed())
            flags |= 1;
        if (name.isSeeThrough())
            flags |= 2;
        if (name.isDefaultBackground())
            flags |= 4;
        if (name.getAlignment() == TextDisplay.TextAlignment.LEFT)
            flags |= 8;
        if (name.getAlignment() == TextDisplay.TextAlignment.RIGHT)
            flags |= 16;
        return (byte) flags;
    }

    private static void send(Player player, Object packet) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

}

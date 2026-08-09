package fr.codinbox.npclib.api.npc.name;

import net.kyori.adventure.text.Component;
import java.util.Objects;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

/**
 * A frame of an animated NPC name.
 *
 * @param text the Adventure text displayed during the frame
 * @param durationTicks the frame duration in server ticks
 * @param transformation the display transformation reached by interpolation
 * @param opacity the signed Minecraft text opacity, where {@code -1} is fully opaque
 */
public record NpcNameFrame(
        @NotNull Component text,
        int durationTicks,
        @NotNull Transformation transformation,
        byte opacity
) {

    /**
     * Validate the frame values.
     */
    public NpcNameFrame {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(transformation, "transformation");
        if (durationTicks < 1) {
            throw new IllegalArgumentException("The frame duration must be at least one tick");
        }
    }

}

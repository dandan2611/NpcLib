package fr.codinbox.npclib.api.npc.animation;

import fr.codinbox.npclib.api.npc.Npc;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface NpcAnimation {

    @NotNull Npc getNpc();

    /**
     * Get the animation type.
     *
     * @return the animation type
     */
    @NotNull AnimationType getAnimationType();

    /**
     * Add all npc viewers to be able to see the animation.
     * <br>
     * You can still remove some viewers with {@link #withoutViewers(UUID...)} or {@link #withoutViewer(UUID)}.
     *
     * @return the animation
     */
    @NotNull NpcAnimation withAllViewers();

    /**
     * Add viewers to be able to see the animation.
     *
     * @param viewers the viewers
     * @return the animation
     */
    @NotNull NpcAnimation withViewers(@NotNull UUID... viewers);

    /**
     * Add viewers to be able to see the animation.
     *
     * @param viewers the viewers
     * @return the animation
     */
    @NotNull NpcAnimation withViewers(@NotNull Player... viewers);

    /**
     * Add a viewer to be able to see the animation.
     *
     * @param viewer the viewer
     * @return the animation
     */
    @NotNull NpcAnimation withViewer(@NotNull UUID viewer);

    /**
     * Add a viewer to be able to see the animation.
     *
     * @param viewer the viewer
     * @return the animation
     */
    @NotNull NpcAnimation withViewer(@NotNull Player viewer);

    /**
     * Remove viewers from the animation.
     *
     * @param viewers the viewers
     * @return the animation
     */
    @NotNull NpcAnimation withoutViewers(@NotNull UUID... viewers);

    /**
     * Remove viewers from the animation.
     *
     * @param viewers the viewers
     * @return the animation
     */
    @NotNull NpcAnimation withoutViewers(@NotNull Player... viewers);

    /**
     * Remove a viewer from the animation.
     *
     * @param viewer the viewer
     * @return the animation
     */
    @NotNull NpcAnimation withoutViewer(@NotNull UUID viewer);

    /**
     * Remove a viewer from the animation.
     *
     * @param viewer the viewer
     * @return the animation
     */
    @NotNull NpcAnimation withoutViewer(@NotNull Player viewer);

    /**
     * Play the animation.
     */
    default void play() {
        this.getNpc().playAnimation(this);
    }

}

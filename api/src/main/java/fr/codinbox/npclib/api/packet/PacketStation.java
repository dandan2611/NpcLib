package fr.codinbox.npclib.api.packet;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface PacketStation<T> {

    /**
     * Create a packet to spawn the NPC for the given player.
     *
     * @param npc the NPC to announce
     * @param preprocessor the packet preprocessor
     * @param player the player to spawn the NPC for
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createPlayerInfoPacket(@NotNull Npc npc,
                                   @Nullable Consumer<T> preprocessor,
                                   @NotNull Player player);

    /**
     * Create a packet to spawn the NPC for the given player.
     *
     * @param npc the NPC to spawn
     * @param preprocessor the packet preprocessor
     * @param player the player to spawn the NPC for
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createPlayerSpawnPacket(@NotNull Npc npc,
                                    @Nullable Consumer<T> preprocessor,
                                    @NotNull Player player);

    /**
     * Create a packet to remove the player info from the NPC for the given player.
     *
     * @param npc the NPC to remove the infos from
     * @param preprocessor the packet preprocessor
     * @param player the player to remove the infos from
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createPlayerInfoRemovePacket(@NotNull Npc npc,
                                         @Nullable Consumer<T> preprocessor,
                                         @NotNull Player player);

    /**
     * Create a packet to despawn the NPC for the given player.
     *
     * @param npc the NPC to despawn
     * @param preprocessor the packet preprocessor
     * @param player the player to despawn the NPC for
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createPlayerDespawnPacket(@NotNull Npc npc,
                                      @Nullable Consumer<T> preprocessor,
                                      @NotNull Player player);

    /**
     * Create a packet to play an animation for the given player.
     *
     * @param npc the NPC to play the animation for
     * @param animationType the animation to play
     * @param preprocessor the packet preprocessor
     * @param player the player to play the animation for
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createEntityAnimationPacket(@NotNull Npc npc,
                                        @NotNull AnimationType animationType,
                                        @Nullable Consumer<T> preprocessor,
                                        @NotNull Player player);

    /**
     * Create a packet to change the NPC's head rotation for the given player.
     *
     * @param npc the NPC to change the head rotation for
     * @param preprocessor the packet preprocessor
     * @param player the player to change the head rotation for
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createEntityHeadRotationPacket(@NotNull Npc npc,
                                           @Nullable Consumer<T> preprocessor,
                                           @NotNull Player player);

    /**
     * Create a metadata packet to send all the NPC's metadata to the given player.
     *
     * @param npc the NPC to send the metadata for
     * @param preprocessor the packet preprocessor
     * @param player the player to send the metadata to
     * @return true if the packet has been created and sent, false otherwise
     */
    boolean createEntityMetadataPacket(@NotNull Npc npc,
                                       @Nullable Consumer<T> preprocessor,
                                       @NotNull Player player);

}

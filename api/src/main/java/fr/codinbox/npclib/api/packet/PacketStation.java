package fr.codinbox.npclib.api.packet;

import fr.codinbox.npclib.api.npc.Npc;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface PacketStation<T> {

    /**
     * Create a packet to spawn the NPC for the given NPC.
     *
     * @param npc the NPC to announce
     * @param preprocessor the packet preprocessor
     * @param player the player to spawn the NPC for
     * @return true if the packet has been created ant sent, false otherwise
     */
    boolean createPlayerInfoPacket(@NotNull Npc npc,
                                   @Nullable Consumer<T> preprocessor,
                                   @NotNull Player player);

    /**
     * Create a packet to spawn the NPC for the given NPC.
     *
     * @param npc the NPC to spawn
     * @param preprocessor the packet preprocessor
     * @param player the player to spawn the NPC for
     * @return true if the packet has been created ant sent, false otherwise
     */
    boolean createPlayerSpawnPacket(@NotNull Npc npc,
                                    @Nullable Consumer<T> preprocessor,
                                    @NotNull Player player);

}

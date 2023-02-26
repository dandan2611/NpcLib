package fr.codinbox.npclib.api.npc.holder;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A NPC Holder is an entity able to create, manage and destroy NPCs.
 */
public interface NpcHolder {

    @NotNull Npc createNpc(@NotNull NpcConfig config);

    void destroyNpc(@NotNull Npc npc);

    @NotNull Set<@NotNull Npc> getNpcs();

    @NotNull Set<@NotNull Npc> getNpcsInWorld(@NotNull World world);

    boolean isRendered(@NotNull Npc npc, @NotNull Player player);

    void setRendered(@NotNull Npc npc, @NotNull Player player, boolean rendered);

    void tickNpc(@NotNull Npc npc, @NotNull Player player);

    //void hideNpc(@NotNull Npc npc, @NotNull Player player);

}

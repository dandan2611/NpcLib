package fr.codinbox.npclib.api.npc.viewer;

import fr.codinbox.npclib.api.npc.Npc;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NpcRenderLogic {

    boolean shouldBeRendered(@NotNull Npc npc, @NotNull Player player, @Nullable NpcViewer viewer);

}

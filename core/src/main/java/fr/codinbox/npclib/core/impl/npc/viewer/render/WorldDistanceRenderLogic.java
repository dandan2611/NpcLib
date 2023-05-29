package fr.codinbox.npclib.core.impl.npc.viewer.render;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldDistanceRenderLogic implements NpcRenderLogic {

    @Override
    public boolean shouldBeRendered(@NotNull Npc npc, @NotNull Player player, @Nullable NpcViewer viewer) {
        return player.getWorld().equals(npc.getWorld())
                && player.getLocation().distance(npc.getLocation()) <= npc.getRenderDistance();
    }

}

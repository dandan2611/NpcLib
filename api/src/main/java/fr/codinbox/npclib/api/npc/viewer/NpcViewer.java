package fr.codinbox.npclib.api.npc.viewer;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface NpcViewer {

    @NotNull Npc getNpc();

    @NotNull UUID getUuid();

    @Nullable Player player();

    boolean isRendered();

    void render();

    void playAnimation(@NotNull AnimationType animationType);

}

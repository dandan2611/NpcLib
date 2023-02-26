package fr.codinbox.npclib.api.npc.event;

import org.jetbrains.annotations.NotNull;

public interface NpcClickedListener {

    void onNpcClicked(@NotNull NpcClickedEvent event);

}

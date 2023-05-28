package fr.codinbox.npclib.api.npc.event;

import org.jetbrains.annotations.NotNull;

/**
 * A listener for npc clicked events.
 */
public interface NpcClickedListener {

    /**
     * Called when a npc is clicked.
     *
     * @param event the event
     */
    void onNpcClicked(@NotNull NpcClickedEvent event);

}

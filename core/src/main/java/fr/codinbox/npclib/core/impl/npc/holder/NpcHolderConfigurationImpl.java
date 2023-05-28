package fr.codinbox.npclib.core.impl.npc.holder;

import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.core.impl.reactive.ReactiveImpl;

public class NpcHolderConfigurationImpl implements NpcHolderConfiguration {

    public static final int DEFAULT_NPC_VIEW_DISTANCE = 64;

    private final Reactive<Integer> npcViewDistance = new ReactiveImpl<>(DEFAULT_NPC_VIEW_DISTANCE);

    @Override
    public Reactive<Integer> getNpcViewDistance() {
        return this.npcViewDistance;
    }

}

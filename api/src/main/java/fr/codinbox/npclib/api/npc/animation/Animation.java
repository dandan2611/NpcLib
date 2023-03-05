package fr.codinbox.npclib.api.npc.animation;

public enum Animation {

    SWING_MAIN_ARM(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRITICAL_EFFECT(4),
    MAGIC_CRITICAL_EFFECT(5);

    private final int id;

    Animation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

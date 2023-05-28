package fr.codinbox.npclib.api.npc.animation;

/**
 * NPC animation types. This represents a possible entity animation.
 * <br>
 * See the <a href="https://wiki.vg/Protocol#Animation">protocol</a> for more information.
 */
public enum AnimationType {

    /**
     * Swing the main arm (punch).
     */
    SWING_MAIN_ARM(0),

    /**
     * Take damage.
     */
    TAKE_DAMAGE(1),

    /**
     * Leave bed.
     */
    LEAVE_BED(2),

    /**
     * Swing the offhand arm (punch).
     */
    SWING_OFFHAND(3),

    /**
     * Critical effect.
     */
    CRITICAL_EFFECT(4),

    /**
     * Magic critical effect.
     */
    MAGIC_CRITICAL_EFFECT(5);

    private final int id;

    /**
     * Create a new animation type.
     *
     * @param id the animation id
     */
    AnimationType(int id) {
        this.id = id;
    }

    /**
     * Get the animation id.
     *
     * @return the animation id
     */
    public int getId() {
        return id;
    }

}

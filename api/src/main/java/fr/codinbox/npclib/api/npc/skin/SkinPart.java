package fr.codinbox.npclib.api.npc.skin;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the skin parts.
 */
public final class SkinPart {

    /**
     * The cape.
     */
    public static final byte CAPE = 0x01;

    /**
     * The jacket.
     */
    public static final byte JACKET = 0x02;

    /**
     * The left sleeve.
     */
    public static final byte LEFT_SLEEVE = 0x04;

    /**
     * The right sleeve.
     */
    public static final byte RIGHT_SLEEVE = 0x08;

    /**
     * The left pants leg.
     */
    public static final byte LEFT_PANTS_LEG = 0x10;

    /**
     * The right pants leg.
     */
    public static final byte RIGHT_PANTS_LEG = 0x20;

    /**
     * The hat.
     */
    public static final byte HAT = 0x40;

    private byte value;

    /**
     * Internal constructor. Do not use.
     */
    private SkinPart(byte value) {
        this.value = value;
    }

    /**
     * Returns an empty skin parts.
     *
     * @return an empty skin parts
     */
    public static @NotNull SkinPart empty() {
        return new SkinPart((byte) 0);
    }

    /**
     * Returns a skin parts with all parts.
     *
     * @return a skin parts with all parts
     */
    public static @NotNull SkinPart all() {
        return new SkinPart((byte) (HAT | CAPE | JACKET | LEFT_SLEEVE | RIGHT_SLEEVE | LEFT_PANTS_LEG | RIGHT_PANTS_LEG));
    }

    /**
     * Returns a skin parts with the specified parts.
     *
     * @param value the parts
     * @return a skin parts with the specified parts
     */
    public static @NotNull SkinPart of(byte value) {
        return new SkinPart(value);
    }

    /**
     * Apply the specified part to the skin parts.
     *
     * @param part the part
     * @return the skin parts
     */
    public @NotNull SkinPart with(byte part) {
        value |= part;
        return this;
    }

    /**
     * Remove the specified part from the skin parts.
     *
     * @param part the part
     * @return the skin parts
     */
    public @NotNull SkinPart without(byte part) {
        value &= ~part;
        return this;
    }

    /**
     * Returns whether the skin parts contain the specified part.
     *
     * @param part the part
     * @return whether the skin parts contain the specified part
     */
    public boolean has(byte part) {
        return (value & part) == part;
    }

    /**
     * Returns the skin parts value.
     *
     * @return the skin parts value
     */
    public byte getValue() {
        return value;
    }

}

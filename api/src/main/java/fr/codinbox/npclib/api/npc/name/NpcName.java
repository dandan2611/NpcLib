package fr.codinbox.npclib.api.npc.name;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

/**
 * Describes a virtual text display used as an NPC name.
 */
public final class NpcName {

    private final List<NpcNameFrame> frames;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final int interpolationDuration;
    private final float viewRange;
    private final int lineWidth;
    private final Display.Billboard billboard;
    private final TextDisplay.TextAlignment alignment;
    private final Color backgroundColor;
    private final boolean shadowed;
    private final boolean seeThrough;
    private final boolean defaultBackground;

    private NpcName(Builder builder) {
        if (builder.frames.isEmpty()) {
            throw new IllegalArgumentException("An NPC name must contain at least one frame");
        }
        this.frames = List.copyOf(builder.frames);
        this.offsetX = builder.offsetX;
        this.offsetY = builder.offsetY;
        this.offsetZ = builder.offsetZ;
        this.interpolationDuration = builder.interpolationDuration;
        this.viewRange = builder.viewRange;
        this.lineWidth = builder.lineWidth;
        this.billboard = builder.billboard;
        this.alignment = builder.alignment;
        this.backgroundColor = builder.backgroundColor;
        this.shadowed = builder.shadowed;
        this.seeThrough = builder.seeThrough;
        this.defaultBackground = builder.defaultBackground;
    }

    /**
     * Create a static NPC name with default display settings.
     *
     * @param text the displayed text
     * @return the NPC name
     */
    public static @NotNull NpcName of(@NotNull Component text) {
        return builder().frame(text, 1).build();
    }

    /**
     * Create an NPC name builder.
     *
     * @return a new builder
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Get the animation frames.
     * @return the animation frames
     */
    public @NotNull List<NpcNameFrame> getFrames() {
        return this.frames;
    }

    /**
     * Get the X offset.
     * @return the horizontal X offset from the NPC
     */
    public double getOffsetX() {
        return this.offsetX;
    }

    /**
     * Get the Y offset.
     * @return the vertical offset from the NPC
     */
    public double getOffsetY() {
        return this.offsetY;
    }

    /**
     * Get the Z offset.
     * @return the horizontal Z offset from the NPC
     */
    public double getOffsetZ() {
        return this.offsetZ;
    }

    /**
     * Get the interpolation duration.
     * @return the transformation interpolation duration in ticks
     */
    public int getInterpolationDuration() {
        return this.interpolationDuration;
    }

    /**
     * Get the view range.
     * @return the TextDisplay view range multiplier
     */
    public float getViewRange() {
        return this.viewRange;
    }

    /**
     * Get the line width.
     * @return the maximum text line width
     */
    public int getLineWidth() {
        return this.lineWidth;
    }

    /**
     * Get the billboard behavior.
     * @return the TextDisplay billboard behavior
     */
    public @NotNull Display.Billboard getBillboard() {
        return this.billboard;
    }

    /**
     * Get the text alignment.
     * @return the text alignment
     */
    public @NotNull TextDisplay.TextAlignment getAlignment() {
        return this.alignment;
    }

    /**
     * Get the background color.
     * @return the background color, or {@code null} for the default background
     */
    public @Nullable Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Check whether the text has a shadow.
     * @return whether the text has a shadow
     */
    public boolean isShadowed() {
        return this.shadowed;
    }

    /**
     * Check whether the text is visible through blocks.
     * @return whether the text is visible through blocks
     */
    public boolean isSeeThrough() {
        return this.seeThrough;
    }

    /**
     * Check whether the default background is used.
     * @return whether the client default text background is used
     */
    public boolean isDefaultBackground() {
        return this.defaultBackground;
    }

    /**
     * Builds an immutable NPC name description.
     */
    public static final class Builder {

        private final List<NpcNameFrame> frames = new ArrayList<>();
        private double offsetX;
        private double offsetY = 2.25;
        private double offsetZ;
        private int interpolationDuration;
        private float viewRange = 1.0f;
        private int lineWidth = 200;
        private Display.Billboard billboard = Display.Billboard.CENTER;
        private TextDisplay.TextAlignment alignment = TextDisplay.TextAlignment.CENTER;
        private Color backgroundColor;
        private boolean shadowed;
        private boolean seeThrough;
        private boolean defaultBackground = true;

        private Builder() {
        }

        /**
         * Add a text frame with an identity transformation and full opacity.
         *
         * @param text the frame text
         * @param durationTicks the frame duration
         * @return this builder
         */
        public @NotNull Builder frame(@NotNull Component text, int durationTicks) {
            return this.frame(text, durationTicks, identityTransformation(), (byte) -1);
        }

        /**
         * Add a text and transformation frame.
         *
         * @param text the frame text
         * @param durationTicks the frame duration
         * @param transformation the target transformation
         * @param opacity the target text opacity
         * @return this builder
         */
        public @NotNull Builder frame(
                @NotNull Component text,
                int durationTicks,
                @NotNull Transformation transformation,
                byte opacity
        ) {
            this.frames.add(new NpcNameFrame(text, durationTicks, transformation, opacity));
            return this;
        }

        /**
         * Set the display position relative to the NPC.
         *
         * @param x the X offset
         * @param y the Y offset
         * @param z the Z offset
         * @return this builder
         */
        public @NotNull Builder offset(double x, double y, double z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
            return this;
        }

        /**
         * Set the native display interpolation duration.
         *
         * @param ticks the duration in ticks
         * @return this builder
         */
        public @NotNull Builder interpolationDuration(int ticks) {
            if (ticks < 0) {
                throw new IllegalArgumentException("The interpolation duration cannot be negative");
            }
            this.interpolationDuration = ticks;
            return this;
        }

        /**
         * Set the TextDisplay view range multiplier.
         *
         * @param viewRange the positive view range
         * @return this builder
         */
        public @NotNull Builder viewRange(float viewRange) {
            if (viewRange <= 0) {
                throw new IllegalArgumentException("The view range must be positive");
            }
            this.viewRange = viewRange;
            return this;
        }

        /**
         * Set the maximum text line width.
         *
         * @param lineWidth the positive line width
         * @return this builder
         */
        public @NotNull Builder lineWidth(int lineWidth) {
            if (lineWidth < 1) {
                throw new IllegalArgumentException("The line width must be positive");
            }
            this.lineWidth = lineWidth;
            return this;
        }

        /**
         * Set how the display faces viewers.
         *
         * @param billboard the billboard behavior
         * @return this builder
         */
        public @NotNull Builder billboard(@NotNull Display.Billboard billboard) {
            this.billboard = billboard;
            return this;
        }

        /**
         * Set the text alignment.
         *
         * @param alignment the text alignment
         * @return this builder
         */
        public @NotNull Builder alignment(@NotNull TextDisplay.TextAlignment alignment) {
            this.alignment = alignment;
            return this;
        }

        /**
         * Set a custom background color.
         *
         * @param backgroundColor the color, or {@code null} for the client default
         * @return this builder
         */
        public @NotNull Builder backgroundColor(@Nullable Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            this.defaultBackground = backgroundColor == null;
            return this;
        }

        /**
         * Set whether the text has a shadow.
         *
         * @param shadowed whether text is shadowed
         * @return this builder
         */
        public @NotNull Builder shadowed(boolean shadowed) {
            this.shadowed = shadowed;
            return this;
        }

        /**
         * Set whether the text is visible through blocks.
         *
         * @param seeThrough whether text is visible through blocks
         * @return this builder
         */
        public @NotNull Builder seeThrough(boolean seeThrough) {
            this.seeThrough = seeThrough;
            return this;
        }

        /**
         * Set whether the client default background is used.
         *
         * @param defaultBackground whether the client default background is used
         * @return this builder
         */
        public @NotNull Builder defaultBackground(boolean defaultBackground) {
            this.defaultBackground = defaultBackground;
            return this;
        }

        /**
         * Build the NPC name.
         * @return the immutable NPC name
         */
        public @NotNull NpcName build() {
            return new NpcName(this);
        }

        private static Transformation identityTransformation() {
            return new Transformation(
                    new Vector3f(),
                    new AxisAngle4f(),
                    new Vector3f(1.0f),
                    new AxisAngle4f()
            );
        }

    }

}

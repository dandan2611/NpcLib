package fr.codinbox.npclib.api;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The NpcLib API.
 */
public abstract class NpcLib {

    private static NpcLib API;

    /**
     * Get the NpcLib API.
     *
     * @return the NpcLib API
     */
    public static @NotNull NpcLib api() {
        if (API == null)
            throw new IllegalStateException("NpcLib is not initialized");
        return API;
    }

    /**
     * Set the NpcLib API.
     * <br>
     * This method should only be called by the NpcLib implementation and not reused after the API is initialized.
     *
     * @param api the NpcLib API
     */
    public static void setApi(@NotNull NpcLib api) {
        if (API != null)
            throw new IllegalStateException("NpcLib is already initialized");
        API = api;
    }

    /**
     * Create a new NPC holder.
     *
     * @param plugin the plugin responsible for the holder
     * @return the NPC holder
     */
    @NotNull
    public abstract NpcHolder createHolder(@NotNull Plugin plugin);

    /**
     * Get the NPC holder of a plugin.
     *
     * @param plugin the plugin
     * @return the NPC holder of the plugin in an optional, or an empty optional if the plugin has no holder
     */
    @NotNull
    public abstract Optional<@NotNull NpcHolder> getPluginHolder(@NotNull Plugin plugin);

}

package fr.codinbox.npclib.api;

import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.packet.PacketStation;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class NpcLib {

    private static NpcLib API;

    public static NpcLib api() {
        if (API == null)
            throw new IllegalStateException("NpcLib is not initialized");
        return API;
    }

    public static void setApi(NpcLib api) {
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

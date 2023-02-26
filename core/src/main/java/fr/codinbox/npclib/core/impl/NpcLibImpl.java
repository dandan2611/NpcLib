package fr.codinbox.npclib.core.impl;

import fr.codinbox.npclib.api.NpcLib;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.core.impl.npc.holder.NpcHolderImpl;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class NpcLibImpl extends NpcLib {

    @NotNull
    private final HashMap<Plugin, NpcHolder> holders;

    public NpcLibImpl() {
        this.holders = new HashMap<>();
    }

    @Override
    @NotNull
    public NpcHolder createHolder(@NotNull Plugin plugin) {
        if (holders.containsKey(plugin))
            throw new IllegalStateException("Holder already exists for plugin " + plugin.getName());
        var holder = new NpcHolderImpl(plugin);
        holders.put(plugin, holder);
        return holder;
    }

    @Override
    public @NotNull Optional<@NotNull NpcHolder> getPluginHolder(@NotNull Plugin plugin) {
        return Optional.ofNullable(holders.get(plugin));
    }

    public @NotNull Set<NpcHolder> getHolders() {
        return Set.copyOf(holders.values());
    }

}

package fr.codinbox.npclib.core;

import fr.codinbox.npclib.api.NpcLib;
import fr.codinbox.npclib.core.impl.NpcLibImpl;
import java.util.UUID;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.java.JavaPlugin;

public class NpcLibPlugin extends JavaPlugin {

    private static NpcLibPlugin INSTANCE;

    private NpcLibImpl npcLib;

    private final ConcurrentHashMap<UUID, Long> interactions = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Enabling plugin");
        INSTANCE = this;
        this.npcLib = new NpcLibImpl();
        NpcLib.setApi(npcLib);
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        if (this.npcLib != null) {
            this.npcLib.getHolders().forEach(holder -> Set.copyOf(holder.getNpcs()).forEach(holder::destroyNpc));
        }
        INSTANCE = null;
    }

    public static NpcLibPlugin instance() {
        return INSTANCE;
    }

    public static NpcLibImpl impl() {
        return INSTANCE.npcLib;
    }

    public NpcLibImpl getNpcLib() {
        return npcLib;
    }

}

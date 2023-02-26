package fr.codinbox.npclib.core;

import fr.codinbox.npclib.api.NpcLib;
import fr.codinbox.npclib.core.impl.NpcLibImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class NpcLibPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling plugin");
        NpcLib.setApi(new NpcLibImpl());
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {

    }

}

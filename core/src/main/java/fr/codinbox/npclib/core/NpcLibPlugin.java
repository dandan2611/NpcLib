package fr.codinbox.npclib.core;

import fr.codinbox.npclib.api.NpcLib;
import fr.codinbox.npclib.core.impl.NpcLibImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NpcLibPlugin extends JavaPlugin {

    private static NpcLibPlugin INSTANCE;

    private NpcLibImpl npcLib;

    private PacketStationImpl packetStation;

    private final ConcurrentHashMap<UUID, Long> interactions = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Enabling plugin");
        INSTANCE = this;
        this.npcLib = new NpcLibImpl();
        this.packetStation = new PacketStationImpl();
        NpcLib.setApi(npcLib);
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {

    }

    public static NpcLibPlugin instance() {
        return INSTANCE;
    }

    public static NpcLibImpl impl() {
        return INSTANCE.npcLib;
    }

    public static PacketStationImpl station() {
        return INSTANCE.packetStation;
    }

    public NpcLibImpl getNpcLib() {
        return npcLib;
    }

}

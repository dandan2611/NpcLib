package fr.codinbox.npclib.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.codinbox.npclib.api.NpcLib;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.core.impl.NpcLibImpl;
import fr.codinbox.npclib.core.impl.packet.PacketStationImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

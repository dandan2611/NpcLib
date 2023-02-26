package fr.codinbox.npclib.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.codinbox.npclib.api.NpcLib;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.core.impl.NpcLibImpl;
import fr.codinbox.npclib.core.impl.packet.PacketStationImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NpcLibPlugin extends JavaPlugin {

    public static final int RENDER_DISTANCE = 64;

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

        // TODO: TESTS

        var holder = NpcLib.api().createHolder(this);
        var npc = holder.createNpc(NpcConfig.create(new Location(Bukkit.getWorld("world"), 17, -60, 8), new Skin() {
            @Override
            public String getValue() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY2ODI2MzQ3MDQzNSwKICAicHJvZmlsZUlkIiA6ICJkZjY2OWIyOGFmNWE0MTNjODFhNjcwOGQ0ZDIyM2FlNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJlbW9fbGFuZyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zYThkYmYxYmUxMGEwY2JiODc4ZDMyMzQ3NzE3MTU0NTI2YmI1M2EzZDVjYTdlYmM4NDI3OTQ0ZGZmNmI1NzAiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            }

            @Override
            public String getSignature() {
                return "s+JoXENLla6CrRFUBmjkEJDGmZawffUwGufF2M+Ibn8viEZIofnrIwCxyCPRtYTWvFSlOPxj7hJBGs48mraW7AezUWyXhG/TeR5tMbNQ6la3+ZRMgsXxtbQwukPdjxkjbyY+fa15QHU9K7SscUkBBoUx/FVQI/LZW/eHTngYeFrnXQNz5XGIe7SB3rUkCcw6vaQ8YG0IWJO0DWkA7dzPFlAK4nPae19YuFIWeTtL9PsvDl5SGx/NNLNOPq7BFAF67NFwv3k5h1yPSdJzdZtqHI2SNNjC2360bU52WJC8LHv5zTEUPiBcHA3TDQuWlo9SZTNEsE9FO5E6FN0MzNJJ++UojSYJShcePXJc492yakl/z6vVI5EPir2JpnMIdrKJaF3mRUyBRLvqIqmHHLXS/vrufJoW0pzigsM9s8n88vxpFfCWKIGlyO9mne5/63OcepWFDOizkqwFz+Kpa5kDqUHvKK6Pl+l+U06vCMTwNPVr6lurA+tiApxZxTYU4H5qtpQWNT1a6TNnQFW6nRkWUs5/Er+2Vfm7A0gtvXQiRZjP0gdeyLrm95GVdrfhGq+ZU9wy8v+7Q5V1JC0cMM7rTnOcjWAVGPEunKFV0e2dR7KjyhzL/8vOUHbw5l1xSRMRkn5li3r6oWd+BhyuXsQZhmsP+GvXOmG+UHPO0JjrefY=";
            }
        }).setGlobal(true));

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                var packet = event.getPacket();
                var id = packet.getIntegers().read(0);
                var action = packet.getEnumEntityUseActions().read(0);
                var player = event.getPlayer();

                if (interactions.containsKey(player.getUniqueId())
                        && System.currentTimeMillis() - interactions.get(player.getUniqueId()) < 100) {
                    event.setCancelled(true);
                    return;
                }
                interactions.put(player.getUniqueId(), System.currentTimeMillis());

                System.out.println(action.getAction());
                //System.out.println("Packet received: " + id + " " + action);
                holder.getNpcsInWorld(event.getPlayer().getWorld()).stream().filter(npc -> npc.getEntityId() == id).forEach(npc -> {
                    System.out.println("NPC clicked");
                    event.setCancelled(true);
                });
            }
        });

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

package fr.codinbox.npclib.core.impl.npc.equipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.codinbox.npclib.api.npc.Npc;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class NpcEquipmentImplTest {

    @Test
    void clearingHandsNotifiesViewers() {
        AtomicInteger updates = new AtomicInteger();
        NpcEquipmentImpl equipment = new NpcEquipmentImpl(npcCountingUpdates(updates));

        equipment.setMainHand(null);
        equipment.setOffHand(null);

        assertEquals(2, updates.get());
        assertNull(equipment.getMainHand());
        assertNull(equipment.getOffHand());
    }

    private static Npc npcCountingUpdates(AtomicInteger updates) {
        return (Npc) Proxy.newProxyInstance(
                Npc.class.getClassLoader(),
                new Class<?>[]{Npc.class},
                (proxy, method, arguments) -> {
                    if (method.getName().equals("updateEquipment")) {
                        updates.incrementAndGet();
                    }
                    return null;
                }
        );
    }

}

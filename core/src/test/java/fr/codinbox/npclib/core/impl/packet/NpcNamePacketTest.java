package fr.codinbox.npclib.core.impl.packet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import fr.codinbox.npclib.api.npc.name.NpcName;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

class NpcNamePacketTest {

    @Test
    void staticNameHasOneValidatedFrame() {
        NpcName name = NpcName.of(Component.text("Line one\nLine two"));

        assertEquals(1, name.getFrames().size());
        assertEquals(Component.text("Line one\nLine two"), name.getFrames().getFirst().text());
        assertThrows(IllegalArgumentException.class,
                () -> NpcName.builder().frame(Component.empty(), 0));
    }

    @Test
    void animatedNameKeepsAllFramesAndInterpolationSettings() {
        NpcName name = NpcName.builder()
                .frame(Component.text("First"), 5)
                .frame(Component.text("Second"), 5)
                .interpolationDuration(3)
                .build();

        assertEquals(2, name.getFrames().size());
        assertEquals(3, name.getInterpolationDuration());
        assertEquals(Component.text("Second"), name.getFrames().get(1).text());
    }

}

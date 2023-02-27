package fr.codinbox.npclib.test.core.reactive;

import fr.codinbox.npclib.core.impl.reactive.ReactiveImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReactiveImplTest {

    @Test
    public void testEmpty() {
        Integer i = null;
        var reactive = new ReactiveImpl<>(i);
        assertNull(reactive.get());
    }

    @Test
    public void testInteger() {
        var reactive = new ReactiveImpl<Integer>(0);
        assertEquals(0, reactive.get());
        reactive.set(1);
        assertEquals(1, reactive.get());
    }

    @Test
    public void testString() {
        var reactive = new ReactiveImpl<String>("test");
        assertEquals("test", reactive.get());
        reactive.set("test2");
        assertEquals("test2", reactive.get());
    }

    @Test
    public void testBind() {
        var reactive = new ReactiveImpl<Integer>(0);
        var reactive2 = new ReactiveImpl<Integer>(1);
        reactive.bind(reactive2);
        assertEquals(1, reactive.get());
        reactive2.set(2);
        assertEquals(2, reactive.get());
    }

    @Test
    public void testListener() {
        var reactive = new ReactiveImpl<>(0);
        reactive.addListener((reactive1, previousValue, newValue) -> {
            assertEquals(0, previousValue);
            assertEquals(1, newValue);
        });
        reactive.set(1);
    }

    @Test
    public void testListenerWithBind() {
        var reactive = new ReactiveImpl<>(0);
        var reactive2 = new ReactiveImpl<>(1);
        reactive.addListener((reactive1, previousValue, newValue) -> {
            assertEquals(0, previousValue);
            assertEquals(1, newValue);
        });
        reactive.bind(reactive2);
    }

}

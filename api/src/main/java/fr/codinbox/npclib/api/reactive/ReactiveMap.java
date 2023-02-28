package fr.codinbox.npclib.api.reactive;

import java.util.Map;

public interface ReactiveMap<K, V> extends Reactive<Map<K, V>>, Map<K, V>, Listenable<Map<K, V>> {

}

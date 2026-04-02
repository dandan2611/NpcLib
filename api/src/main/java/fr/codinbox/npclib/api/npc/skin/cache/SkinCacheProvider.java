package fr.codinbox.npclib.api.npc.skin.cache;

import fr.codinbox.npclib.api.npc.skin.Skin;

import java.util.UUID;

/**
 * A cache provider specialized for skin lookups by UUID.
 */
public interface SkinCacheProvider extends CacheProvider<UUID, Skin> {

}

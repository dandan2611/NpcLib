# NpcLib

NpcLib is a library for creating NPCs in Minecraft. 
It is designed to be easy to use and to be able to be used in any Bukkit plugin.

## Features

| Feature | Description                                         | Implemented |
| --- |-----------------------------------------------------| --- |
| Creation | Create NPCs with a name, skin, and location         | ✅ |
| Interaction | Interact with NPCs by clicking on them              | ✅ |
| Removal | Remove NPCs from the world                          | ❌ |
| Movement | Move NPCs around the world                          | ❌ |
| Animation | Play animations on NPCs                             | ❌ |
| Configuration | Configure NPCs with a complete configuration object | ❌ |

## Installation

### Plugin

[![Release - 1.19.3](https://img.shields.io/badge/Release-1.19.3-2ea44f)](https://git.codinbox.fr/api/v4/projects/29/jobs/artifacts/master/raw/core/build/libs/npclib-1.0.0-SNAPSHOT-all.jar?job=build-deploy)

### API: Gradle

```kotlin
repositories {
    maven(url = "https://git.codinbox.fr/api/v4/projects/29/packages/maven") {
        name = "npclib"
    }
}

dependencies {
    compileOnly("fr.codinbox.npclib:api:1.0.0-SNAPSHOT")
}
```

## Usage

Do not forget to put the `depends: [NpcLib]` in your plugin.yml.

Create a new NpcHolder, the NpcHolder is responsible for managing NPCs for a specific plugin.
I recommend creating the NpcHolder in the onEnable method of your plugin.

````java
class MyPlugin extends JavaPlugin {
    private NpcHolder npcHolder;
    
    @Override
    public void onEnable() {
        this.npcHolder = NpcLib.api().createHolder(this);
    }
}
````

You can then use your holder in order to create and manage your NPCs!

````java
class MyNpcSpawner {
    private static final String SKIN_TEXTURE = null; // Place your skin texture here
    private static final String SKIN_SIGNATURE = null; // Place your skin signature here
    
    private final NpcHolder npcHolder;
    
    public MyNpcManager(NpcHolder npcHolder) {
        this.npcHolder = npcHolder;
    }
    
    public void spawnNpc() {
        Location location = new Location(Bukkit.getWorld("world"), 0, 0, 0); // The NPC location
        Skin skin = Skin.fromValueAndSignature(SKIN_TEXTURE, SKIN_SIGNATURE); // The NPC skin
        
        Npc npc = this.npcHolder.createNpc(
                NpcConfig.create(location, skin)
                        .setGlobal(true) // Display the NPC to all players
                        .setRenderDistance(64) // Override the holder's configuration render distance
        );
        // From now, every player will see the NPC
        npc.getClickedListeners().add(event -> {
           var player = event.getPlayer();
           player.sendMessage("You clicked on the NPC!");
        });
    }
}
````
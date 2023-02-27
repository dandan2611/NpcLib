# NpcLib

![](https://git.codinbox.fr/codinbox/npclib/badges/master/pipeline.svg)
![](https://img.shields.io/badge/Supported%20versions-1.19.3%2B-9cf)
[![Release - 1.19.3](https://img.shields.io/badge/Release-1.19.3-2ea44f)](https://git.codinbox.fr/api/v4/projects/29/jobs/artifacts/master/raw/core/build/libs/npclib-1.0.0-SNAPSHOT-all.jar?job=build-deploy)

**The [GitHub repository](https://github.com/dandan2611/NpcLib) associated with this project is a mirror of the official [GitLab repository](https://git.codinbox.fr/codinbox/npclib).**

NpcLib is a library for creating NPCs in Minecraft. 
It is designed to be easy to use and to be able to be used in any Bukkit plugin.

## Features

| Feature      | Description                                                                                                |
|--------------|------------------------------------------------------------------------------------------------------------|
| NPCs         | 🏗️ Create/Delete NPCs with a name, skin and position                                                      |
| Interactions | 🎧 Listen to click events on NPCs                                                                          |
| Reactivity   | 🚀 Listen to every events related to NPCs and their properties (see [Reactive objects](#reactive-objects)) |

### What's coming next?

- [ ] Add support for animations

## Installation

1. Install latest version of [ProtocolLib](https://github.com/dmulloy2/ProtocolLib) on your server.
2. Download the latest [release](https://git.codinbox.fr/api/v4/projects/29/jobs/artifacts/master/raw/core/build/libs/npclib-1.0.0-SNAPSHOT-all.jar?job=build-deploy) and put it in your server `plugins` folder.
3. Import the NpcLib API in your project using [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/).
4. Put the `depends: [NpcLib]` in your plugin.yml.

### Example of importing the API with Gradle (Kotlin)

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

### Reactive objects

A reactive object is an object that can have a value assigned of a specific type.
Developers can bind listeners to a reactive object in order to be notified when the value changes.

Reactive objects can also be bound to each other, in this case, when the value of one reactive object changes, the value of the other reactive object will be updated.

In this library, every property that constitutes an NPC is a reactive object, this allows you to listen to every change made to the NPC and edit it in real time.
This also allows the library to update NPCs in real time when a property changes.

### NPC Holders

A NPC holder is an object that manages the creation and deletion of NPCs for a specific plugin.
A signe plugin can only have one NPC holder.

In order to create an NPC holder, you must use the `NpcLib.api().createHolder(Plugin)` method.
It is recommended to create the holder in the `onEnable` method of your plugin.

### Creating NPCs

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

After creating the NPC, the holder will automatically manage the lifecycle of the NPC :)
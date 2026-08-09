# NpcLib

![](https://img.shields.io/badge/Supported%20version-26.2-9cf)
![Build](https://github.com/dandan2611/npclib/actions/workflows/build.yml/badge.svg)

NpcLib is a library for creating NPCs in Minecraft. 
It is designed to be easy to use and to be able to be used in any Bukkit plugin.

## Features

| Feature      | Description                                                                                                |
|--------------|------------------------------------------------------------------------------------------------------------|
| NPCs         | Create/Delete NPCs with a skin, equipment and position                                                     |
| Names        | Viewer-specific Adventure names with colors, multiple lines and TextDisplay animations                    |
| Interactions | Listen to click events on NPCs                                                                             |

## Installation

1. Run Paper 26.2 with Java 25 or newer.
2. Install PacketEvents 2.13.0 or newer on your server.
3. Download the latest [release](https://github.com/dandan2611/NpcLib/releases/latest) and put it in your server `plugins` folder.
4. Import the NpcLib API in your project using [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/).
5. Put the `depends: [NpcLib]` in your plugin.yml.

### Example of importing the API with Gradle (Kotlin)

```kotlin
repositories {
    maven(url = "https://nexus.codinbox.fr/repository/maven-public/")
}

dependencies {
    compileOnly("fr.codinbox.npclib:api:4.0.0")
}
```

## Usage

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
    
    public MyNpcSpawner(NpcHolder npcHolder) {
        this.npcHolder = npcHolder;
    }
    
    public void spawnNpc() {
        Location location = new Location(Bukkit.getWorld("world"), 0, 0, 0); // The NPC location
        Skin skin = Skin.fromValueAndSignature(SKIN_TEXTURE, SKIN_SIGNATURE); // The NPC skin
        
        Npc npc = this.npcHolder.createNpc(
                NpcConfig.create(location, skin)
                        .setGlobal(true) // Display the NPC to all players
                        .setRenderDistance(64) // The NPC can be seen from 64 blocks away
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

### Custom names

Custom names are virtual TextDisplays. The provider is evaluated independently for each viewer, and Adventure components support colors and multiple lines.

```java
Npc npc = npcHolder.createNpc(
        NpcConfig.create(location, skin)
                .setCustomName(player -> NpcName.builder()
                        .frame(Component.text("Hello " + player.getName(), NamedTextColor.GOLD), 20)
                        .frame(Component.text("Shop", NamedTextColor.AQUA)
                                .append(Component.newline())
                                .append(Component.text("Right click", NamedTextColor.GRAY)), 20)
                        .interpolationDuration(5)
                        .offset(0, 2.25, 0)
                        .build())
);
```

`setCustomName` hides the vanilla player nameplate by default. Use `setNameVisible(false)` without a custom name to only hide it. Call `npc.updateCustomName()` after viewer-specific data changes to evaluate the provider again.

Each frame can also receive a Bukkit `Transformation` and an opacity through the four-argument `frame` overload. Transformations and opacity are interpolated over the configured duration.

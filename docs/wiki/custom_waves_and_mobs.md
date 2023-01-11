# Custom Waves

An entry for an arena in the configuration file should automatically be created after you have created an arena.
 **Note** that the custom waves _should_ work with modded mobs.
## Explanation of various settings:

### Single Waves
Single waves occur only once and always take priority over recurrent waves. 

### Recurrent Waves
Recurrent waves are waves that can occur more than once.

### Frequency
The frequency of a wave decides how often a wave **can** spawn. Waves have two frequencies, the default frequency and the current frequency. Every time a new wave starts, the current frequency of all recurrent waves is lowered by one. When a wave's frequency reaches 0, it has a chance to be selected. Once a wave has been selected, its frequency is reset to the original frequency that it had.

### Priority
The priority of a wave can be used to tell the arena which wave to select if two or more waves have the same **current** frequency. The wave with the highest priority will be selected. A random wave is selected if the possible waves have the same priority. Use this option carefully, especially if you intend to have waves with both a frequency of 1 and a priority higher than 1 as it can completely stop some waves from being selected. This can be used if you don't want any of the default waves to be selected.

### Fixed
Setting fixed to true makes the exact amount of mobs in the "monsters" section above it spawn. If set to false, it will instead weigh the spawns meaning that the mobs with the highest number will spawn more often. It is generally recommended to use this for single waves only, assuming that you want the single wave to have a specific amount of mobs.

### Use Custom Spawns
Can be used to override the mobs of **all default** waves. Its use is not recommended as it will make mob spawns completely random. It is instead recommended to add custom mob entries in the "monsters" section of a custom wave.

# Sample Configuration

```json
{
  "Arenas": {
    "your_arena_name": {
      "Use custom spawns": false,
      "Monsters": [
        "minecraft:zombie",
        "minecraft:husk"
      ],
      "singleWaves": [
        {
          "wave": 20,
          "type": "BOSS",
          "monsters": {
            "minecraft:wolf": 5,
            "minecraft:witch": 10
          },
          "fixed": true
        }
      ],
      "recurrentWaves": [
        {
          "wave": 16,
          "frequency": 4,
          "priority": 1,
          "type": "DEFAULT",
          "monsters": {
            "minecraft:husk": 6
          },
          "fixed": false
        },
        {
          "wave": 10,
          "frequency": 1,
          "priority": 1,
          "type": "DEFAULT",
          "monsters": {
            "minecraft:skeleton": 5,
            "minecraft:wolf": 1,
            "minecraft:zombie": 3,
            "minecraft:pillager": 6,
            "minecraft:creeper": 1
          },
          "fixed": false
        },
        {
          "wave": 10,
          "frequency": 2,
          "priority": 1,
          "type": "SWARM",
          "monsters": {
            "minecraft:skeleton": 4,
            "minecraft:wolf": 2
          },
          "fixed": false
        },
        {
          "wave": 10,
          "frequency": 5,
          "priority": 1,
          "type": "SWARM",
          "monsters": {
            "minecraft:witch": 5,
            "minecraft:wolf": 1
          },
          "fixed": false
        }
      ],
      "finalWave": 0
    },
    "your_arena_name_2": {
      "Use custom spawns": false,
      "Monsters": [
        "minecraft:zombie",
        "minecraft:husk"
      ],
      "singleWaves": [
        {
          "wave": 1,
          "type": "SWARM",
          "monsters": {
            "minecraft:zombie": 3
          },
          "fixed": true
        }
      ],
      "recurrentWaves": [
        {
          "wave": 16,
          "frequency": 4,
          "priority": 1,
          "type": "DEFAULT",
          "monsters": {
            "minecraft:husk": 10,
            "minecraft:witch": 2,
            "minecraft:wolf": 3
          },
          "fixed": false
        },
        {
          "wave": 10,
          "frequency": 1,
          "priority": 1,
          "type": "DEFAULT",
          "monsters": {
            "minecraft:skeleton": 5,
            "minecraft:wolf": 1,
            "minecraft:zombie": 3,
            "minecraft:pillager": 6,
            "minecraft:creeper": 1
          },
          "fixed": false
        },
        {
          "wave": 10,
          "frequency": 2,
          "priority": 2,
          "type": "SWARM",
          "monsters": {
            "minecraft:skeleton": 5,
            "minecraft:wolf": 2
          },
          "fixed": false
        },
        {
          "wave": 3,
          "frequency": 5,
          "priority": 3,
          "type": "SWARM",
          "monsters": {
            "minecraft:spider": 1,
            "minecraft:cave_spider": 3,
            "minecraft:wolf": 3
          },
          "fixed": false
        }
      ],
      "finalWave": 0
    }
  }
}
```
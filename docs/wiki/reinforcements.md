# Configuring Reinforcements

**Difficulty: Hard**

## Default reinforcements section for any arena
```json
      "reinforcements": [
        {
          "wave": 5,
          "items": {
            "all": [
              "{Count:1,id:\"minecraft:golden_apple\"}",
              "{Count:4,id:\"minecraft:cooked_beef\"}"
            ]
          },
          "recurrent": true
        }
      ]
```



## Example: Adding reinforcements for class "archer" on wave 3 and every 5th wave

First open the arenaconfigs.json file in the config/mobarena folder. Find the arena that you want to configure reinforcements for.

Use the default wave 5 section or add a new section containing the *wave, items, recurrent* settings by copying the default section and editing the wave number to your liking. Go to the items section and add a new entry called "archer" in it and follow the same format as the "all" entry.

To add reinforcements items, it is recommended to copy item data from the class you want in the classes.json file because manually writing in the format that is used can quickly lead to errors.

```json
      "reinforcements": [
        {
          "wave": 5,
          "items": {
            "all": [
              "{Count:1,id:\"minecraft:golden_apple\"}",
              "{Count:4,id:\"minecraft:cooked_beef\"}"
            ],
            "archer": [
              "{Count:2,id:\"minecraft:tipped_arrow\",tag:{Potion:\"minecraft:strong_poison\"}}",
              "{Count:16,id:\"minecraft:arrow\"}"
            ]
          },
          "recurrent": true
        },
        {
          "wave": 3,
          "items": {
            "archer": [
              "{Count:8,id:\"minecraft:tipped_arrow\",tag:{Potion:\"minecraft:strong_healing\"}}",
              "{Count:8,id:\"minecraft:tipped_arrow\",tag:{Potion:\"minecraft:strong_harming\"}}"
            ]
          },
          "recurrent": false
        }
      ]
```
This configuration results in:
- **All** arena players getting 1 golden apple and 4 cooked beef every 5th wave.
- Archers getting 2 strong poison arrows and 16 normal arrows every 5th wave.
- Archers getting 8 strong healing arrows and 8 strong harming arrows on the 3rd wave.


Notes: 
* Recurrent and one-time reinforcements for the same wave need to be separate, meaning you will need to add two entries for wave 3 if you want both one-time and recurrent reinforcements on that wave. For example, this can be achieved using the example above and changing "wave: 3" to "wave: 5".
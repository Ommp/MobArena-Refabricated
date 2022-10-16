# About
Rewards are items that players can receive after completing waves. They are given to the players after all players die. It will also be given to a player that leaves assuming the player has completed enough waves.

# Adding rewards to an arena
Rewards are added using the command `/ma rewards add *wave* *arena*` whilst holding any item.

**Example**:
Enter the command `/ma rewards add 5 FireArena` whilst holding a golden apple. A player that now plays in _FireArena_ will receive two golden apples after leaving or dying on wave 11. If the player only made it to wave 6, only one golden apple will be granted.

**Multiple rewards can be given for the same wave**. All rewards will then be granted.

## Note when adding rewards
The amount of the item you're holding matters when adding rewards. If you had held 64 golden apples instead of just one when using the command in the example earlier, 64 golden apples would be the reward for every 5th wave. That would mean 128 golden apples if the player left on wave 11.

# Removing rewards
Currently the only way to remove a reward, is to remove all rewards for a wave in an arena.
It's easier to show how to do this with some examples:

`/ma rewards delete 5 FireArena`

`/ma rewards delete 3 Rome`

Any players playing in _FireArena_ will no longer receive rewards for every 5th wave unless new ones are added. Players in the arena _Rome_ will no longer receive rewards for every 3rd wave.



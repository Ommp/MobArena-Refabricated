# Arena Setup
### The setup of an arena requires at least:
* a physical arena
* adding the arena to the database via commands
* warps for lobby, arena, exit, spectator
* adding points at the corners of the arena

#### It is also recommended to have:
* classes (such as warrior, archer, tank, etc.)
* custom spawn points for the mobs. If you don't define spawn points it will default to the arena warp.
* rewards



# The Process:
Open the chat in-game and do the following:


* */mobarena create name* - Add an arena to the database

**NOTE: "/mobarena" can be replaced with "/ma"**

Travel to your lobby
* */mobarena set lobby name* - set the lobby warp. Keep in mind that the direction you look is also saved!

Travel to the other warps and use the set commands
* */mobarena set arena name* - set the arena warp
* */mobarena set spectator name* - set the spectator warp
* */mobarena set exit name* - set the exit warp

Add as many mob spawn points as desired
* */mobarena addmobspawn name*

Set points at opposite arena corners. This is required so that mobs and players get teleported back into the arena if they somehow end up outside it.
* */ma set p1 name* - set corner 1
* */ma set p2 name* - set corner 2

Now you should be able to play in the arena by using the join command:
* */mobarena join name*



## Other Commands
### Arena Countdown
* */ma set countdown number name* - Set the countdown before the first wave starts after all players have been teleported to the arena.

Example:
`/ma set countdown 3 swamparena` - Sets the countdown to 3 for the arena called *swamparena*

Note: Players are able to join the lobby during this time which will reset the countdown until that player has readied up.


### Min and Max Players
* */ma set minPlayers number name* - Set the minimum players required before an arena can start

Example:
`/ma set minPlayers 2 FireArena` - Sets the minimum players to 2 for the arena called *FireArena*

* */ma set maxPlayers number name* - Set the maximum players (fighters) that can join an arena

Example:
`/ma set maxPlayers 5 FireArena` - Sets the maximum players to 5 for the arena called *FireArena*

### Enable or Disable Experience Orbs
* */ma set xpEnabled number name* - Enable or disable experience orbs. 1 is true, 0 is false.

  `/ma set xpEnabled 1 FireArena` - Enables experience orbs in *FireArena*

  `/ma set xpEnabled 0 FireArena` - Disables experience orbs in *FireArena*

### Force class selection
* */ma set forceclass number name* - Enable or disable classes being mandatory. 1 is true, 0 is false.

  `/ma set xpEnabled 1 FireArena` - Force classes in *FireArena*

  `/ma set xpEnabled 0 FireArena` - Don't force classes in *FireArena*
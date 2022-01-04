package mobarena.commands.user;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Warp;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class QueueCommands {
    private static ServerCommandSource source;
    private static ServerPlayerEntity Player;
    private static String arenaName;
    static ArrayList<UUID> joinedPlayers = new ArrayList<>();
    static ArrayList<UUID> readyPlayers = new ArrayList<>();
    static ArrayList<UUID> activePlayers = new ArrayList<>();


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("majoin")
                .then(CommandManager.argument("arenaname", StringArgumentType.word())
                        .executes(context -> {
                            source = context.getSource();
                            Player = source.getPlayer();

                            arenaName = StringArgumentType.getString(context, "arenaname");

                            if (Warp.getLobbyWarp(arenaName) != null) {
                                Warp lobbyWarp = Warp.getLobbyWarp(arenaName);
                                if (Player.isSleeping()) Player.wakeUp(true, true);
                                //TODO check for world
                                joinedPlayers.add(Player.getUuid());
                                Player.networkHandler.requestTeleport(lobbyWarp.x, lobbyWarp.y, lobbyWarp.z, lobbyWarp.yaw, lobbyWarp.pitch);
                                return 1;
                            } else if(activePlayers.size() >= 1){
                                source.sendFeedback(new LiteralText("Arena is busy: there are still players in that arena."), false);
                                return 0;
                            }

                            else {
                                source.sendFeedback(new LiteralText("Arena or warp does not exist!"), false);
                                return 0;
                            }
                        })));

        dispatcher.register(CommandManager.literal("maready")
                .executes(context -> {
                    if (Warp.getArenaWarp(arenaName) != null) {
                        Warp arenaWarp = Warp.getArenaWarp(arenaName);

                        if (Player.isSleeping()) Player.wakeUp(true, true);

                        readyPlayers.add(Player.getUuid());
                        joinedPlayers.remove(Player.getUuid());

                        if(joinedPlayers.isEmpty() & readyPlayers.size() >= 1) {
                            Player.networkHandler.requestTeleport(arenaWarp.x, arenaWarp.y, arenaWarp.z, arenaWarp.yaw, arenaWarp.pitch);
                            activePlayers.add(Player.getUuid());
                            readyPlayers.clear();

                            BlockPos mobSpawnPointTest = new BlockPos(Player.getBlockPos());
                            BlockPos mobSpawnPointTest2 = new BlockPos(Player.getBlockPos().getX()+3, Player.getBlockPos().getY()+1, Player.getBlockPos().getZ()+2);

                            List<BlockPos> mobSpawnPointList = new ArrayList<>();
                            mobSpawnPointList.add(mobSpawnPointTest);
                            mobSpawnPointList.add(mobSpawnPointTest2);

                            BlockPos randomSpawnPoint = mobSpawnPointList.get(ThreadLocalRandom.current().nextInt(mobSpawnPointList.size()));

                            ZombieEntity zombieEntity = EntityType.ZOMBIE.create(source.getWorld());
                            zombieEntity.refreshPositionAndAngles(randomSpawnPoint, 0.0F, 0.0F);
                            World world = Player.world;
                            world.spawnEntity(zombieEntity);
                        }
                        return 1;
                    } else {
                        source.sendFeedback(new LiteralText("Warp has not been set up or is unavailable"), false);
                        return 0;
                    }
                }));

        dispatcher.register(CommandManager.literal("maleave")
                .executes(context -> {
                    if (Warp.getExitWarp(arenaName) != null) {
                        Warp exitWarp = Warp.getExitWarp(arenaName);
                        if (Player.isSleeping()) Player.wakeUp(true, true);
                        //TODO check for world
                        activePlayers.remove(Player.getUuid());
                        Player.networkHandler.requestTeleport(exitWarp.x, exitWarp.y, exitWarp.z, exitWarp.yaw, exitWarp.pitch);
                        return 1;
                    } else {
                        source.sendFeedback(new LiteralText("Warp has not been set up or is unavailable"), false);
                        return 0;
                    }
                }));
    }
}

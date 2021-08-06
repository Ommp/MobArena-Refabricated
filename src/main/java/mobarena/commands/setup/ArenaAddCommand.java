package mobarena.commands.setup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.Command;
import mobarena.ArenaLocation;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class ArenaAddCommand {

public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("createarena")
                    .then(CommandManager.argument("arenaname", StringArgumentType.word())
                        .then(CommandManager.argument("x_1", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("y_1", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("z_1", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("x_2", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("y_2", DoubleArgumentType.doubleArg())
                        .then(CommandManager.argument("z_2", DoubleArgumentType.doubleArg())

                        .executes(context -> {
                                    final ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity senderPlayer = source.getPlayer();

                                        String arenaName = StringArgumentType.getString(context, "arenaname");
                                        double x_1 = DoubleArgumentType.getDouble(context, "x_1");
                                        double y_1 = DoubleArgumentType.getDouble(context, "y_1");
                                        double z_1 = DoubleArgumentType.getDouble(context, "z_1");
                                        double x_2 = DoubleArgumentType.getDouble(context, "x_2");
                                        double y_2 = DoubleArgumentType.getDouble(context, "y_2");
                                        double z_2 = DoubleArgumentType.getDouble(context, "z_2");
//                                      ArenaLocation pos1 = new ArenaLocation(senderPlayer.world.getRegistryKey(), x_1, y_1, z_1);
                                        ArenaLocation pos2 = new ArenaLocation(senderPlayer.world.getRegistryKey(), 80, 85, 100);

                                    System.out.println("Created an arena with name " + arenaName + " at: ");
//                                    System.out.println(pos1.pos.y);
//                                    System.out.println(pos1.dim.getValue());


                            return 1;
                    })))))))));
        }

    }

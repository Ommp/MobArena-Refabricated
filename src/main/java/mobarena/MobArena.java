package mobarena;


import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.world.World.OVERWORLD;


public class MobArena implements ModInitializer {

    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger("mobarena");


	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initalised MobArena Mod for Minecraft v1.16");

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
//			String Position1 = {"a", "b", "c"};
			dispatcher
					.register(
							CommandManager.literal("createarena")
									.executes(context -> {
//										String arenaName = StringArgumentType.getString(context, "arenaname");
//										World world = context.getSource().getEntityOrThrow().getEntityWorld();
//										BlockPos pos1 = context.getSource().getEntityOrThrow().getBlockPos();
										ArenaLocation pos1 = new ArenaLocation(OVERWORLD, 60, 65, 80);
										ArenaLocation pos2 = new ArenaLocation(OVERWORLD, 80, 85, 100);
										System.out.println("Created an arena at: ");
										System.out.println(pos1);
										return 1;
									}));
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher
					.register(
							CommandManager.literal("deletearena").then(CommandManager.argument("name", StringArgumentType.word()))
									.executes(context -> {
										System.out.println("Deleted arena!");

										return 1;
									}));

		});
	}
	}

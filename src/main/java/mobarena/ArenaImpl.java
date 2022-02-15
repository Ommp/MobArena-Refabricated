package mobarena;

import mobarena.region.ArenaRegion;
import mobarena.steps.PlayerJoinArena;
import mobarena.steps.PlayerSpecArena;
import mobarena.steps.Step;
import mobarena.steps.StepFactory;
import mobarena.util.Slugs;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ArenaImpl implements Arena {

     private MobArena mod;
     private String name;
     private String slug;
     private ServerWorld world;
     private BlockPos lobbyWarp;

     private Set<ServerPlayerEntity> arenaPlayers, lobbyPlayers, specPlayers, deadPlayers, readyPlayers;
     private Map<ServerPlayerEntity,ArenaPlayer> arenaPlayerMap;

     private List<Arena> arenas;
     private Map<ServerPlayerEntity, Arena> arenaMap;

     private ArenaRegion region;


     // Actions
     private Map<ServerPlayerEntity, Step> histories;
     private StepFactory playerJoinArena;
     private StepFactory playerSpecArena;

     private boolean enabled, protect, running, edit;

     private Set<Block>             blocks;

     private MonsterManager monsterManager;

//     Temporarily used until I figure out how to access "the world" without using a method on a player running a command
     public ArenaImpl(MobArena mod, String name) {
          this.mod = mod;
          this.name = name;
          this.slug     = Slugs.create(name);
          this.region = new ArenaRegion(this);
          this.arenaPlayerMap = new HashMap<>();
          this.arenaPlayers   = new HashSet<>();
          this.lobbyPlayers   = new HashSet<>();
          this.readyPlayers   = new HashSet<>();
          this.specPlayers    = new HashSet<>();
          this.deadPlayers    = new HashSet<>();


          // Actions
          this.histories = new HashMap<>();
          this.playerJoinArena = PlayerJoinArena.create(this);
          this.playerSpecArena = PlayerSpecArena.create(this);

          this.enabled = true;
          this.running = false;
          this.edit = false;

          this.blocks       = new HashSet<>();

          this.monsterManager = new MonsterManager();
     }


     public ArenaImpl(MobArena mod, String name, ServerWorld world) {
          this.mod = mod;
          this.name = name;
          this.world = world;
          this.slug     = Slugs.create(name);
          this.region = new ArenaRegion(this);
          this.arenaPlayerMap = new HashMap<>();
          this.arenaPlayers   = new HashSet<>();
          this.lobbyPlayers   = new HashSet<>();
          this.readyPlayers   = new HashSet<>();
          this.specPlayers    = new HashSet<>();
          this.deadPlayers    = new HashSet<>();


          // Actions
          this.histories = new HashMap<>();
          this.playerJoinArena = PlayerJoinArena.create(this);
          this.playerSpecArena = PlayerSpecArena.create(this);

          this.enabled = true;
          this.running = false;
          this.edit = false;

          this.blocks       = new HashSet<>();

          this.monsterManager = new MonsterManager();
     }

     @Override
     public String getName() {
          return name;
     }

     @Override
     public void setName(String name) {
          this.name = name;
     }

     @Override
     public ServerWorld getWorld() {
          return world;
     }

     @Override
     public void setWorld(ServerWorld world) {
          this.world = world;
     }


     @Override
     public BlockPos getLobbyWarp() {
          return lobbyWarp;
     }

     @Override
     public void setLobbyWarp(BlockPos lobbyWarp) {
          this.lobbyWarp = lobbyWarp;
     }

     @Override
     public List<ServerPlayerEntity> getAllPlayers(){
          List<ServerPlayerEntity> result = new LinkedList<>();
          result.addAll(arenaPlayers);
          result.addAll(lobbyPlayers);
          result.addAll(specPlayers);

          return result;
     }

     @Override
     public Collection<ArenaPlayer> getArenaPlayerSet()
     {
          return arenaPlayerMap.values();
     }

     @Override
     public List<ServerPlayerEntity> getNonreadyPlayers()
     {
          List<ServerPlayerEntity> result = new LinkedList<>();
          result.addAll(lobbyPlayers);
          result.removeAll(readyPlayers);
          return result;
     }

     @Override
     public Set<ServerPlayerEntity> getReadyPlayersInLobby() {
          return Collections.unmodifiableSet(readyPlayers);
     }

     @Override
     public Set<ServerPlayerEntity> getSpectators() {
          return Collections.unmodifiableSet(specPlayers);
     }

     @Override
     public Set<ServerPlayerEntity> getPlayersInArena() {
          return Collections.unmodifiableSet(arenaPlayers);
     }


     @Override
     public MobArena getMod() {
          return mod;
     }

     @Override
     public ArenaRegion getRegion() {
          return region;
     }

     @Override
     public boolean playerJoin(ServerPlayerEntity p, BlockPos loc) {

          specPlayers.remove(p);


          Step step = playerJoinArena.create(p);
          try {
               step.run();
          } catch (Exception e) {
               MobArena.LOGGER.error("Player " + p.getName() + " couldn't join arena " + name);
               return false;
          }
          histories.put(p, step);

          lobbyPlayers.add(p);
          mod.getArenaMaster().addPlayer(p, this);
          arenaPlayerMap.put(p, new ArenaPlayer(p, this, mod));

          return true;
     }

     @Override
     public void playerReady(ServerPlayerEntity p) {
          readyPlayers.add(p);

          startArena();
     }

     @Override
     public boolean startArena() {

          arenaPlayers.addAll(lobbyPlayers);
          lobbyPlayers.clear();
          readyPlayers.clear();

          if (arenaPlayers.isEmpty()) {
               return false;
          }

          // Teleport players, give full health, initialize map
          for (ServerPlayerEntity p : arenaPlayers) {
               // Remove player from spec list to avoid invincibility issues
               if (inSpec(p)) {
                    specPlayers.remove(p);
                    System.out.println("[MobArena] Player " + p.getName() + " joined the arena from the spec area!");
                    System.out.println("[MobArena] Invincibility glitch attempt stopped!");
               }

               p.teleport(region.getArenaWarp().getX(), region.getArenaWarp().getY(), region.getArenaWarp().getZ());


          }
          return true;
     }

     @Override
     public boolean endArena() {
          if (!running || !arenaPlayers.isEmpty()) {
               return false;
          }

          running = false;

          cleanup();

          return true;
     }

     @Override
     public boolean isRunning() {
          return running;
     }

     private void cleanup() {
//          removeMonsters();
//          removeBlocks();
//          removeEntities();
          clearPlayers();
     }

     private void clearPlayers() {
          arenaPlayers.clear();
          arenaPlayerMap.clear();
          lobbyPlayers.clear();
          readyPlayers.clear();
     }

     @Override
     public boolean inArena(ServerPlayerEntity p) {
          return arenaPlayers.contains(p);
     }

     @Override
     public boolean inLobby(ServerPlayerEntity p) {
          return lobbyPlayers.contains(p);
     }

     @Override
     public boolean inSpec(ServerPlayerEntity p) {
          return specPlayers.contains(p);
     }

     @Override
     public boolean isDead(ServerPlayerEntity p) {
          return deadPlayers.contains(p);
     }

     @Override
     public boolean inEditMode() {
          return edit;
     }

     @Override
     public void setEditMode(boolean value) {
          edit = value;
     }


     @Override
     public Set<Block> getBlocks() {
          return blocks;
     }

     @Override
     public void addBlock(Block b) {
          blocks.add(b);
     }

     @Override
     public boolean removeBlock(Block b) {
          return blocks.remove(b);
     }

     @Override
     public void forceEnd() {
          List<ServerPlayerEntity> players = getAllPlayers();
          if (players.isEmpty()) {
               return;
          }

//          players.forEach(this::playerLeave);
          cleanup();
     }


     @Override
     public MonsterManager getMonsterManager() {
          return monsterManager;
     }

     @Override
     public boolean isEnabled() {
          return enabled;
     }

}
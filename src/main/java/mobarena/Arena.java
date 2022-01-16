package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class Arena {

     private String name;
     private ServerWorld world;
     private Location lobbyWarp;

     private Set<ServerPlayerEntity> arenaPlayers, lobbyPlayers, specPlayers, deadPlayers, readyPlayers;
     private Map<ServerPlayerEntity,ArenaPlayer> arenaPlayerMap;



//     Set<ServerPlayerEntity> playersInSpec;


     public Arena(String name, ServerWorld world) {
          if (world == null)
               throw new NullPointerException("[MobArena] ERROR! World for arena '" + name + "' does not exist!");

          this.name = name;
          this.world = world;
          this.arenaPlayerMap = new HashMap<>();
          this.arenaPlayers   = new HashSet<>();
          this.lobbyPlayers   = new HashSet<>();
          this.readyPlayers   = new HashSet<>();
          this.specPlayers    = new HashSet<>();
          this.deadPlayers    = new HashSet<>();
     }


//     Temporarily used until I figure out how to access "the world" without using a method on a player running a command
     public Arena(String name) {
          this.name = name;
          this.arenaPlayerMap = new HashMap<>();
          this.arenaPlayers   = new HashSet<>();
          this.lobbyPlayers   = new HashSet<>();
          this.readyPlayers   = new HashSet<>();
          this.specPlayers    = new HashSet<>();
          this.deadPlayers    = new HashSet<>();
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public ServerWorld getWorld() {
          return world;
     }

     public void setWorld(ServerWorld world) {
          this.world = world;
     }

     public Location getLobbyWarp() {
          return lobbyWarp;
     }

     public void setLobbyWarp(Location lobbyWarp) {
          this.lobbyWarp = lobbyWarp;
     }

     public List<ServerPlayerEntity> getAllPlayers(){
          List<ServerPlayerEntity> result = new LinkedList<>();
          result.addAll(arenaPlayers);
          result.addAll(lobbyPlayers);
          result.addAll(specPlayers);

          return result;
     }

     public Collection<ArenaPlayer> getArenaPlayerSet()
     {
          return arenaPlayerMap.values();
     }

     public List<ServerPlayerEntity> getNonreadyPlayers()
     {
          List<ServerPlayerEntity> result = new LinkedList<>();
          result.addAll(lobbyPlayers);
          result.removeAll(readyPlayers);
          return result;
     }

     public Set<ServerPlayerEntity> getReadyPlayersInLobby() {
          return Collections.unmodifiableSet(readyPlayers);
     }

     public Set<ServerPlayerEntity> getSpectators() {
          return Collections.unmodifiableSet(specPlayers);
     }

     public Set<ServerPlayerEntity> getPlayersInArena() {
          return Collections.unmodifiableSet(arenaPlayers);
     }





}
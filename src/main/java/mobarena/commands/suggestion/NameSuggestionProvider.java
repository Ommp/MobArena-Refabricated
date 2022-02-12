//https://github.com/OverlordsIII/VillagerNames/blob/0c316b5d53a7aba2b2296b0fe419422e484fe06a/src/main/java/io/github/overlordsiii/villagernames/command/suggestion/NameSuggestionProvider.java
package mobarena.commands.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mobarena.MobArena;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NameSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

    protected Set<String> arenaNames;

    public static class Arena extends NameSuggestionProvider {
        public Arena() {
            this.arenaNames = MobArena.config.arenas.arenaList.keySet();
        }
    }

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
            String string = builder.getRemaining();
            sortNamesByString(string).forEach(builder::suggest);
            return builder.buildFuture();
        }

        private ArrayList<String> sortNamesByString(String currentArg){
            ArrayList<String> suggestionsBasedOnCurrentArg = new ArrayList<>();
            arenaNames.forEach((string) -> {
                if (string.indexOf(currentArg) == 0){
                    suggestionsBasedOnCurrentArg.add(string);
                }
            });
            return suggestionsBasedOnCurrentArg;
        }

    }

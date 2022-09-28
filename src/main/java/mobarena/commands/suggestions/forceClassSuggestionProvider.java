package mobarena.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class forceClassSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        getBoolean().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private ArrayList<Integer> getBoolean() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        return list;
    };

}

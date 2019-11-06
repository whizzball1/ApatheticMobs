package whizzball1.apatheticmobs.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import whizzball1.apatheticmobs.data.WhitelistData;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

//Implementation due to CraftTweaker
public class ApatheticCommands {
    public static LiteralArgumentBuilder<CommandSource> root = Commands.literal("apathy");

    private static final Map<String, CommandImpl> COMMANDS = new TreeMap<>(String::compareTo);

    public static void init(CommandDispatcher<CommandSource> dispatcher) {
        CommandImpl whitelistImpl = new CommandImpl("whitelist", "Help", context -> {
            String string = "'/apathy whitelist get' to see whitelisted players, '/apathy whitelist add/remove [playername]' to add or remove players";
            send(new StringTextComponent(string), context.getSource());
        return 0;});

        LiteralArgumentBuilder<CommandSource> whitelistCommand = Commands.literal(whitelistImpl.getName());
        whitelistCommand.executes(whitelistImpl.getCaller()::executeCommand);

        CommandImpl getImpl = new CommandImpl("get", "Lists whitelisted players", context -> {
            WhitelistData data = WhitelistData.get(context.getSource().getWorld());
            MinecraftServer server = context.getSource().getServer();
            StringBuilder b = new StringBuilder();
            for (UUID i : data.playerSet) {
                b.append(server.getPlayerProfileCache().getProfileByUUID(i).getName());
                b.append(", ");
            }
            b.reverse();
            try {
                b.delete(0, 2);
            } catch (StringIndexOutOfBoundsException e) {

            }
            b.reverse();
            send(new StringTextComponent(b.toString()), context.getSource());
        return 0;});

        LiteralArgumentBuilder<CommandSource> getCommand = Commands.literal(getImpl.getName())
                .executes(getImpl.getCaller()::executeCommand);
        whitelistCommand.then(getCommand);


//        registerCommand(COMMANDS.get("whitelist"), new CommandImpl("get", "Lists whitelisted players", context -> {
//            WhitelistData data = WhitelistData.get(context.getSource().getWorld());
//            MinecraftServer server = context.getSource().getServer();
//            StringBuilder b = new StringBuilder();
//            for (UUID i : data.playerSet) {
//                b.append(server.getPlayerProfileCache().getProfileByUUID(i).getName());
//                b.append(", ");
//            }
//            b.reverse();
//            try {
//                b.delete(0, 2);
//            } catch (StringIndexOutOfBoundsException e) {
//
//            }
//            b.reverse();
//            send(new StringTextComponent(b.toString()), context.getSource());
//            return 0;
//        }));

        LiteralArgumentBuilder<CommandSource> litAdd = Commands.literal("add");
        litAdd.then(Commands.argument("player", EntityArgument.player()).executes(context -> {
            GameProfile gameProfile = EntityArgument.getPlayer(context, "player").getGameProfile();
            UUID id = gameProfile.getId();
            WhitelistData data = WhitelistData.get(context.getSource().getWorld());
            if (!data.playerSet.contains(id)) {
                data.playerSet.add(id);
                data.markDirty();
            } else {
                send(new TranslationTextComponent("commands.apatheticmobs.aw.playerfound"), context.getSource());
            }
            send(new StringTextComponent(gameProfile.getId().toString()), context.getSource());
        return 0;}));
        whitelistCommand.then(litAdd);

        LiteralArgumentBuilder<CommandSource> litRemove = Commands.literal("remove");
        litRemove.then(Commands.argument("player", EntityArgument.player()).executes(context -> {
            GameProfile gameProfile = EntityArgument.getPlayer(context, "player").getGameProfile();
            UUID id = gameProfile.getId();
            WhitelistData data = WhitelistData.get(context.getSource().getWorld());
            if (data.playerSet.contains(id)) {
                data.playerSet.remove(id);
                data.markDirty();
            } else {
                send(new TranslationTextComponent("commands.apatheticmobs.aw.playerfailed"), context.getSource());
            }
        return 0;}));
        whitelistCommand.then(litRemove);
        root.then(whitelistCommand);
        //COMMANDS.forEach((s, command) -> registerCommandInternal(root, command));
        dispatcher.register(root);

//        CommandDispatcher<CommandSource> p_198494_0_ = dispatcher;
//        p_198494_0_.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("give").requires((p_198496_0_) -> {
//            return p_198496_0_.hasPermissionLevel(2);
//        })).then(Commands.argument("targets", EntityArgument.players()).then(((RequiredArgumentBuilder)Commands.argument("item", ItemArgument.item()).executes((p_198493_0_) -> {
//            return giveItem((CommandSource)p_198493_0_.getSource(), ItemArgument.getItem(p_198493_0_, "item"), EntityArgument.getPlayers(p_198493_0_, "targets"), 1);
//        })).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes((p_198495_0_) -> {
//            return giveItem((CommandSource)p_198495_0_.getSource(), ItemArgument.getItem(p_198495_0_, "item"), EntityArgument.getPlayers(p_198495_0_, "targets"), IntegerArgumentType.getInteger(p_198495_0_, "count"));
//        })))));
    }

    public static void registerCommand(CommandImpl command) {
        COMMANDS.put(command.getName(), command);
    }

    public static void registerCommand(String command, CommandImpl subCommand) {
        COMMANDS.get(command).getSubCommands().put(subCommand.getName(), subCommand);
    }

    public static void registerCommand(CommandImpl command, CommandImpl subCommand) {
        command.getSubCommands().put(subCommand.getName(), subCommand);
    }

    private static void registerCommandInternal(LiteralArgumentBuilder<CommandSource> root, CommandImpl command) {
        LiteralArgumentBuilder<CommandSource> litCommand = Commands.literal(command.getName());
        if(!command.getSubCommands().isEmpty()) {
            command.getSubCommands().forEach((s, command1) -> registerCommandInternal(litCommand, command1));
        }
        root.then(litCommand.executes(command.getCaller()::executeCommand));

    }

    private static void send(TextComponent component, CommandSource source) {
        source.sendFeedback(component, true);
    }

    private static void send(TextComponent component, PlayerEntity player) {
        player.sendMessage(component);
    }

    public static class CommandImpl implements Comparable<CommandImpl> {

        private final String name;
        private final String description;
        private final CommandCaller caller;
        private final Map<String, CommandImpl> subCommands;

        public CommandImpl(String name, String description, CommandCaller caller) {
            this.name = name;
            this.description = description;
            this.caller = caller;
            this.subCommands = new TreeMap<>();
        }

        public String getName() {
            return name;
        }

        public CommandCaller getCaller() {
            return caller;
        }


        public String getDescription() {
            return description;
        }

        public void registerSubCommand(CommandImpl subCommand) {
            this.subCommands.put(subCommand.getName(), subCommand);
        }

        public Map<String, CommandImpl> getSubCommands() {
            return subCommands;
        }

        @Override
        public int compareTo(CommandImpl o) {
            return getName().compareTo(o.getName());
        }
    }

    public static interface CommandCaller {

        int executeCommand(CommandContext<CommandSource> context) throws CommandSyntaxException;

    }
}

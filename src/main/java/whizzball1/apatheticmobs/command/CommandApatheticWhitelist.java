//package whizzball1.apatheticmobs.command;
//
//import com.mojang.authlib.GameProfile;
//import net.minecraft.command.CommandBase;
//import net.minecraft.command.CommandException;
//import net.minecraft.command.ICommandSender;
//import net.minecraft.command.WrongUsageException;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
//import net.minecraftforge.common.config.Config;
//import net.minecraftforge.common.config.ConfigElement;
//import net.minecraftforge.common.config.ConfigManager;
//import whizzball1.apatheticmobs.data.WhitelistData;
//
//import java.util.*;
//
//public class CommandApatheticWhitelist extends CommandBase {
//
//    private List<String> subCommands = new ArrayList<>();
//
//    public CommandApatheticWhitelist() {
//        subCommands.add("get");
//        subCommands.add("add");
//        subCommands.add("remove");
//    }
//
//    public String getName() {
//        return "apatheticwhitelist";
//    }
//
//    public String getUsage(ICommandSender sender) {
//        return "commands.apatheticmobs.aw.usage";
//    }
//
//    public int getRequiredPermissionLevel() {
//        return 2;
//    }
//
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//        boolean test = args.length != 2;
//        if (test && !args[0].equals("get")) {
//            throw new WrongUsageException(getUsage(sender));
//        }
//        GameProfile gameProfile = server.getPlayerProfileCache().getGameProfileForUsername(test ? "t" : args[1]);
//        if (gameProfile == null) {
//            throw new CommandException("commands.apatheticmobs.aw.playerfailed");
//        }
//        UUID id = gameProfile.getId();
//        WhitelistData data = WhitelistData.get(sender.getEntityWorld());
//        switch (args[0]) {
//            case "get":
//                StringBuilder b = new StringBuilder();
//                for (UUID i : data.playerSet) {
//                    b.append(server.getPlayerProfileCache().getProfileByUUID(i).getName());
//                    b.append(", ");
//                }
//                b.reverse();
//                try {
//                    b.delete(0, 2);
//                } catch (StringIndexOutOfBoundsException e) {
//
//                }
//                b.reverse();
//                sender.sendMessage(new StringTextComponent(b.toString()));
//                break;
//            case "add":
//                if (!data.playerSet.contains(id)) {
//                    data.playerSet.add(id);
//                    data.markDirty();
//                } else {
//                    sender.sendMessage(new TranslationTextComponent("commands.apatheticmobs.aw.playerfound"));
//                }
//                break;
//            case "remove":
//                if (data.playerSet.contains(id)) {
//                    data.playerSet.remove(id);
//                    data.markDirty();
//                } else {
//                    sender.sendMessage(new TranslationTextComponent("commands.apatheticmobs.aw.playerfailed"));
//                }
//                break;
//        }
//    }
//}

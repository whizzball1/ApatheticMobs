package whizzball1.apatheticmobs.command;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.ConfigManager;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.*;

public class CommandApatheticWhitelist extends CommandBase {

    private List<String> subCommands = new ArrayList<>();

    public CommandApatheticWhitelist() {
        subCommands.add("get");
        subCommands.add("add");
        subCommands.add("remove");
    }

    public String getName() {
        return "apatheticwhitelist";
    }

    public String getUsage(ICommandSender sender) {
        return "commands.apatheticmobs.aw.usage";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 2) {
            throw new WrongUsageException(getUsage(sender));
        }
        GameProfile gameProfile = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
        if (gameProfile == null) {
            throw new CommandException("commands.apatheticmobs.aw.playerfailed");
        }
        UUID id = gameProfile.getId();
        switch (args[0]) {
            case "get":
                sender.sendMessage(new TextComponentString(id.toString()));
        }
    }
}

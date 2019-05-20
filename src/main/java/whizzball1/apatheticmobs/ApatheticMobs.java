package whizzball1.apatheticmobs;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.event.RegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.capability.RevengeCapFactory;
import whizzball1.apatheticmobs.capability.RevengeStorage;
import whizzball1.apatheticmobs.command.ModCommands;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;
import whizzball1.apatheticmobs.handlers.ApatheticHandler;
import whizzball1.apatheticmobs.handlers.RuleHandler;
import whizzball1.apatheticmobs.rules.DifficultyLockRule;
import whizzball1.apatheticmobs.rules.Rules;
import whizzball1.apatheticmobs.rules.TargeterTypeRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Mod(
        modid = ApatheticMobs.MOD_ID,
        name = ApatheticMobs.MOD_NAME,
        version = ApatheticMobs.VERSION,
        dependencies = "required-after:forge@[14.23.5.2824,)"
)
public class ApatheticMobs {

    public static final String MOD_ID = "apatheticmobs";
    public static final String MOD_NAME = "ApatheticMobs";
    public static final String VERSION = "1.4.1";

    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static final Random random = new Random();

    public static final Rules rules = new Rules();

    @CapabilityInject(IRevengeCap.class)
    public static final Capability<IRevengeCap> REVENGE_CAPABILITY = null;



    @Mod.Instance(MOD_ID)
    public static ApatheticMobs INSTANCE;


    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        //DoWhatYouWant.makeTheFile();
        CapabilityManager.INSTANCE.register(IRevengeCap.class, RevengeStorage.STORAGE, new RevengeCapFactory());
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ApatheticHandler());
        MinecraftForge.EVENT_BUS.register(new RuleHandler());
    }


    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        ModCommands.registerCommands(event);

    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent e) {

        DifficultyLockRule.allowedDifficulties.clear();
        Collections.addAll(DifficultyLockRule.allowedDifficulties, ApatheticConfig.rules.difficulties);

        TargeterTypeRule.exclusions.clear();
        Arrays.asList(ApatheticConfig.rules.exclusions).forEach(t -> TargeterTypeRule.exclusions.add(new ResourceLocation(t)));
        TargeterTypeRule.inclusions.clear();
        Arrays.asList(ApatheticConfig.rules.inclusions).forEach(t -> TargeterTypeRule.inclusions.add(new ResourceLocation(t)));

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            World world = server.getEntityWorld();
            DifficultyLockRule.difficultyMatch(world, true);
            if (!world.isRemote) {
                WhitelistData.get(world);
            }
        }

    }


}

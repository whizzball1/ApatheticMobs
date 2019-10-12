package whizzball1.apatheticmobs;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.capability.RevengeCapFactory;
import whizzball1.apatheticmobs.capability.RevengeStorage;
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

@Mod("apatheticmobs")
public class ApatheticMobs {

    public static final String MOD_ID = "apatheticmobs";
    public static final String MOD_NAME = "ApatheticMobs";
    public static final String VERSION = "1.4.1";

    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static final Random random = new Random();

    public static final Rules rules = new Rules();

    @CapabilityInject(IRevengeCap.class)
    public static final Capability<IRevengeCap> REVENGE_CAPABILITY = null;



    public ApatheticMobs() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarted);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ApatheticConfig::onReload);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ApatheticConfig.COMMON_CONFIG);
        ApatheticConfig.loadConfig(ApatheticConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("apatheticmobs-common.toml"));
        MinecraftForge.EVENT_BUS.register(new ApatheticHandler());
        MinecraftForge.EVENT_BUS.register(new RuleHandler());
    }

    public void setup(FMLCommonSetupEvent event) {
        //DoWhatYouWant.makeTheFile();
        CapabilityManager.INSTANCE.register(IRevengeCap.class, RevengeStorage.STORAGE, new RevengeCapFactory());
    }

//    @Mod.EventHandler
//    public void serverStarting(FMLServerStartingEvent event) {
//        ModCommands.registerCommands(event);
//
//    }

    public void serverStarted(FMLServerStartedEvent e) {

        DifficultyLockRule.allowedDifficulties.clear();
        ApatheticConfig.COMMON.difficulties.get().forEach(t -> DifficultyLockRule.allowedDifficulties.add(t));

        TargeterTypeRule.exclusions.clear();
        ApatheticConfig.COMMON.exclusions.get().forEach(t -> TargeterTypeRule.exclusions.add(new ResourceLocation(t)));
        TargeterTypeRule.inclusions.clear();
        ApatheticConfig.COMMON.inclusions.get().forEach(t -> TargeterTypeRule.inclusions.add(new ResourceLocation(t)));

        MinecraftServer server = e.getServer();
        if (server != null) {
            World world = server.getWorld(DimensionType.OVERWORLD);
            DifficultyLockRule.difficultyMatch(world, true);
            if (!world.isRemote) {
                WhitelistData.get(world);
            }
        }

    }


}

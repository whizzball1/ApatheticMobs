package whizzball1.apatheticmobs.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.data.WhitelistData;
import whizzball1.apatheticmobs.rules.DifficultyLockRule;
import whizzball1.apatheticmobs.rules.TargeterTypeRule;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class ApatheticConfig {

    public static final String CATEGORY_RULES = "rules";
    public static final String CATEGORY_BOSS = "boss_rules";

    public static final ForgeConfigSpec COMMON_CONFIG;

    public static class Common {
        public final BooleanValue revenge;
        public final BooleanValue revengeTime;
        public final IntValue revengeTimer;

        public final BooleanValue blacklist;
        public final ConfigValue<List<? extends String>> exclusions;
        public final ConfigValue<List<? extends String>> inclusions;

        public final BooleanValue difficultyLock;
        public final ConfigValue<List<? extends String>> difficulties;
        public final BooleanValue playerWhitelist;

        public final BooleanValue bossOption;

        public final BooleanValue dragonFlies;
        public final BooleanValue dragonSits;
        public final BooleanValue witherRevenge;
        public final BooleanValue witherAttacks;

        public final BooleanValue gaia;
        public final BooleanValue chaosProjectiles;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Basic Rules").push(CATEGORY_RULES);

            revenge = builder.comment("If true, mobs will attack you back; if false, they will never attack you back.")
                    .worldRestart()
                    .translation("text.apatheticmobs.config.revenge")
                    .define("revenge", false);
            revengeTime = builder.comment("If you have revenge enabled: false -> eternal revenge; true -> temporary revenge.")
                    .translation("text.apatheticmobs.config.revengetime")
                    .define("revenge_time", true);
            revengeTimer = builder.comment("If you have the revenge timer enabled, this is how many ticks it will last.",
                    "Just because the timer ends doesn't mean the mob will stop attacking you; it may take a bit.",
                    "The minimum is 20 ticks (1 second) and the maximum is 10000 ticks (500 seconds). Default is 10 seconds.")
                    .translation("text.apatheticmobs.config.revengetimer")
                    .defineInRange("revenge_timer", 200, 20, 10000);

            blacklist = builder.comment("If true, mod will not affect mobs in exclusions; if false, mod will only affect mobs in inclusions.")
                    .translation("text.apatheticmobs.config.blacklist")
                    .define("Blacklist or Whitelist Option", true);
            exclusions = builder.comment("List of entities that will attack the player if blacklist is enabled.")
                    .translation("text.apatheticmobs.config.exclusions")
                    .defineList("exclusions", Arrays.asList("minecraft:cow"), o -> o instanceof String);
            inclusions = builder.comment("List of entities that will not attack the player if blacklist is disabled.")
                    .translation("text.apatheticmobs.config.inclusions")
                    .defineList("inclusions", Arrays.asList("minecraft:zombie"), o -> o instanceof String);

            difficultyLock = builder.comment("If true, mod will only work on listed difficulties; if false, it will always work.")
                    .translation("text.apatheticmobs.config.difficultylock")
                    .define("difficulty_lock", false);
            difficulties = builder.comment("List of difficulties in which the mod will work if difficultyLock is set to true.")
                    .translation("text.apatheticmobs.config.difficulties")
                    .define("difficulties", Arrays.asList("easy"), o -> o instanceof String);

            playerWhitelist = builder.comment("If true, mod will only cause mobs to ignore players whitelisted by the command apatheticwhitelist.")
                    .translation("text.apatheticmobs.config.playerwhitelist")
                    .define("player_whitelist", false);

            builder.pop();

            builder.comment("Boss Rules").push(CATEGORY_BOSS);

            bossOption = builder.comment("If true, any mobs with the flag 'isBoss' will be ignored by this mod. So Eldritch Guardian, for example.")
                    .translation("text.apatheticmobs.config.bossoption")
                    .define("boss_option", false);

            dragonFlies = builder.comment("If true, ender dragon will attack the player when flying.")
                    .translation("text.apatheticmobs.config.dragonflies")
                    .worldRestart()
                    .define("dragon_flies", false);
            dragonSits = builder.comment("If true, ender dragon will attack the player when sitting—you should set this to true so you can get dragon's breath!")
                    .translation("text.apatheticmobs.config.dragonsits")
                    .worldRestart()
                    .define("dragon_sits", false);
            witherRevenge = builder.comment("If true, wither will take revenge.")
                    .translation("text.apatheticmobs.config.witherrevenge")
                    .worldRestart()
                    .define("wither_revenge", false);
            witherAttacks = builder.comment("If true, wither will attack you; if false, wither will neither attack you nor, well, anything.")
                    .translation("text.apatheticmobs.config.witherattacks")
                    .worldRestart()
                    .define("wither_attacks", false);

            gaia = builder.comment("If true, Gaia Guardian will send attacks at you and the magic floor will exist. If false, it will only spawn mobs.")
                    .translation("text.apatheticmobs.config.gaia")
                    .define("gaia", false);
            chaosProjectiles = builder.comment("If true, chaos dragon will send projectiles at you. If false, it will not.")
                    .translation("text.apatheticmobs.config.chaosprojectiles")
                    .define("chaos_projectiles", false);

            builder.pop();
        }

    }

    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_CONFIG = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading e) {
        DifficultyLockRule.allowedDifficulties.clear();
        ApatheticConfig.COMMON.difficulties.get().forEach(t -> DifficultyLockRule.allowedDifficulties.add(t));

        TargeterTypeRule.exclusions.clear();
        ApatheticConfig.COMMON.exclusions.get().forEach(t -> TargeterTypeRule.exclusions.add(new ResourceLocation(t)));
        TargeterTypeRule.inclusions.clear();
        ApatheticConfig.COMMON.inclusions.get().forEach(t -> TargeterTypeRule.inclusions.add(new ResourceLocation(t)));

        DifficultyLockRule.configReloaded = true;
        ApatheticMobs.logger.info("Config reloaded!");
    }
}

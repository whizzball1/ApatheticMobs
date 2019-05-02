package whizzball1.apatheticmobs.config;

import net.minecraftforge.common.config.Config;
import whizzball1.apatheticmobs.ApatheticMobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Config(modid=ApatheticMobs.MOD_ID)
public class ApatheticConfig {

    @Config.Name("Basic Rules")
    public static RulesCategory rules = new RulesCategory();
    @Config.Name("Boss Rules")
    public static BossCategory bossRules = new BossCategory();


    public static class RulesCategory {

        @Config.Name("Revenge Option")
        @Config.Comment("If true, mobs will attack you back; if false, they will never attack you back.")
        @Config.RequiresWorldRestart
        public boolean revenge = false;

        @Config.Name("Revenge Timer Option")
        @Config.Comment("If you have revenge enabled: false -> eternal revenge; true -> temporary revenge.")
        public boolean revengeTime = false;

        @Config.Name("Revenge Timer Value")
        @Config.Comment({
                "If you have the revenge timer enabled, this is how many ticks it will last.",
                "Just because the timer ends doesn't mean the mob will stop attacking you; it may take a bit.",
                "The minimum is 20 ticks (1 second) and the maximum is 10000 ticks (500 seconds). Default is 10 seconds."
        })
        @Config.RangeInt(min = 20, max = 10000)
        public int revengeTimer = 200;

        @Config.Name("Blacklist or Whitelist Option")
        @Config.Comment("If true, mod will not affect mobs in exclusions; if false, mod will only affect mobs in inclusions.")
        public boolean blacklist = true;

        @Config.Name("Excluded Mobs")
        @Config.Comment("List of entities that will attack the player if blacklist is enabled.")
        public String[] exclusions = new String[] {"minecraft:cow"};

        @Config.Name("Included Mobs")
        @Config.Comment("List of entities that will not attack the player if blacklist is disabled.")
        public String[] inclusions = new String[] {"minecraft:zombie"};

        @Config.Name("Difficulty Lock Option")
        @Config.Comment("If true, mod will only work on listed difficulties; if false, it will always work.")
        public boolean difficultyLock = false;

        @Config.Name("Allowed Difficulties")
        @Config.Comment("List of difficulties in which the mod will work if difficultyLock is set to true.")
        public String[] difficulties = new String[] {"easy"};

        @Config.Name("Player Whitelist Option (Untested)")
        @Config.Comment("If true, mod will only cause mobs to ignore players whitelisted by the command apatheticwhitelist.")
        public boolean playerWhitelist = false;

    }

    public static class BossCategory {
        @Config.Name("Dragon Flying Attack Option")
        @Config.Comment("If true, ender dragon will attack the player when flying.")
        @Config.RequiresWorldRestart
        public boolean dragonFlies = false;

        @Config.Name("Dragon Sitting Attack Option")
        @Config.Comment("If true, ender dragon will attack the player when sitting—you should set this to true so you can get dragon's breath!")
        @Config.RequiresWorldRestart
        public boolean dragonSits = false;

        @Config.Name("Wither Revenge Option")
        @Config.Comment("If true, wither will take revenge.")
        @Config.RequiresWorldRestart
        public boolean witherRevenge = false;

        @Config.Name("General Boss Option")
        @Config.Comment("If true, any mobs with the flag 'isBoss' will be ignored by this mod. So Eldritch Guardian, for example.")
        public boolean bossOption = false;



        @Config.Name("Wither Attack Option")
        @Config.Comment("If true, wither will attack you; if false, wither will neither attack you nor, well, anything")
        @Config.RequiresWorldRestart
        public boolean witherAttacks = false;

        @Config.Name("Gaia Attack Option")
        @Config.Comment("If true, Gaia Guardian will send attacks at you and the magic floor will exist. If false, it will only spawn mobs.")
        public boolean gaia = false;

        @Config.Name("Chaos Dragon Projectile Option")
        @Config.Comment("If true, chaos dragon will send projectiles at you. If false, it will not.")
        public boolean chaosProjectiles = false;

    }
}

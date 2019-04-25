package whizzball1.apatheticmobs.config;

import net.minecraftforge.common.config.Config;
import whizzball1.apatheticmobs.ApatheticMobs;

@Config(modid=ApatheticMobs.MOD_ID)
public class ApatheticConfig {

    @Config.Name("Basic Rules")
    public static RulesCategory rules = new RulesCategory();


    private static class RulesCategory {

        @Config.Name("Revenge Option")
        @Config.Comment("If true, mobs will attack you back; if false, they will never attack you back.")
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
        @Config.Comment("If true, mod will only cause mobs to ignore players listed in playerList.")
        public boolean playerWhitelist = false;

        @Config.Name("List of Whitelisted Players")
        @Config.Comment("List of players whom mobs will ignore.")
        public String[] playerList = new String[] {"Player501"};
    }
}

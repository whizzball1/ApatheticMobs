//package whizzball1.apatheticmobs.config;
//
//import net.minecraftforge.common.config.Configuration;
//import net.minecraftforge.fml.common.Loader;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//public class DoWhatYouWant {
//    //Implementation taken from ProfHugo's NoDamI (No Damage Immunity)
//    private static Configuration config = null;
//
//    public static boolean blacklist, playerWhitelist, revenge, revengeTime, difficultyLock;
//    public static boolean dragonFlies, dragonSits;
//    public static boolean witherRevenge, witherAttacks;
//    public static boolean gaia;
//    public static String[] exclusions, inclusions, difficulties, playerList;
//    public static int revengeTimer;
//    public static Set<String> playerSet;
//
//    public static void makeTheFile() {
//        File configFile = new File(Loader.instance().getConfigDir(), "apatheticmobs.cfg");
//        config = new Configuration(configFile);
//        writeTheRules();
//    }
//
//    public static Configuration readTheRules() {
//        return config;
//    }
//
//    private static void writeTheRules() {
//        revenge = config.getBoolean("revenge", "rules", false,
//                "if true, mobs will attack you back; if false, they will never attack you back");
//        revengeTime = config.getBoolean("revengeTimerEnabled", "rules", false,
//                "if you have revenge enabled: false -> eternal revenge; true -> temporary revenge.");
//        revengeTimer = config.getInt("revengeTimer", "rules", 200, 20, 10000,
//                "if you have revenge timer enabled, this is how many ticks the timer will last. Note that just because"
//        + " the timer ends doesn't mean the mob will stop attacking immediately, but it will eventually");
//        blacklist = config.getBoolean("blacklist", "rules", true,
//                "if true, mod will ignore excluded mobs; if false, mod will only adjust included mobs");
//        playerWhitelist = config.getBoolean("playerWhitelist", "rules", false,
//                "if true, mod will only cause mobs to ignore players listed in playerList (untested so far!)");
//        playerList = config.getStringList("playerList", "rules", new String[] {"Player501"},
//                "list of players who mobs will ignore");
//        playerSet = new HashSet<>(Arrays.asList(playerList));
//        difficultyLock = config.getBoolean("difficultyLock", "rules", false,
//                "if true, mod will only work on listed difficulties; if false, it will always work");
//        exclusions = config.getStringList("exclusions", "rules", new String[] {"minecraft:cow"},
//                "list of entities that will still attack the player");
//        inclusions = config.getStringList("inclusions", "rules", new String[] {"minecraft:zombie"},
//                "list of entities that will not attack the player");
//        difficulties = config.getStringList("difficulties", "rules", new String[] {"easy"},
//                "list of difficulties in which the mod will work if difficultyLock is set to true");
//        dragonFlies = config.getBoolean("dragonfly", "ender_dragon", false,
//                "if true, ender dragon will attack the player when flying");
//        dragonSits = config.getBoolean("dragonsit", "ender_dragon", false,
//                "if true, ender dragon will attack the player when sitting—you should set this to true so you can get dragon's breath!");
//        witherRevenge = config.getBoolean("witherRevenge", "wither", false,
//                "if true, wither will take revenge");
//        witherAttacks = config.getBoolean("witherAttacks", "wither", false,
//                "if true, wither will attack you; if false, wither will neither attack you nor, well, anything");
//        gaia = config.getBoolean("gaiaGuardian", "compatibility", false,
//                "if true, Gaia Guardian will shoot its attacks at you; if false, you'll only get the magic floor" +
//                " and the mobs, which will ignore you");
//
//        if (config.hasChanged()) {
//            config.save();
//        }
//    }
//}

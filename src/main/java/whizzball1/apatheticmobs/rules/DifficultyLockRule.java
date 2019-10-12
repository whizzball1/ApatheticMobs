package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.HashSet;
import java.util.Set;

public class DifficultyLockRule extends Rule {

    public static boolean isCorrectDifficulty;
    public static Set<String> allowedDifficulties = new HashSet<>();
    //If the config has been reloaded this will be set to true
    public static boolean configReloaded = false;

    public boolean shouldExecute(Entity ent) {
        if (!ApatheticConfig.COMMON.difficultyLock.get()) return false;
        return true;
    }

    public int priority() {
        return 4;
    }

    public boolean execute(Entity ent) {
        return difficultyMatch(ent.getEntityWorld(), true);
    }

    public Set<String> allowedModules() {
        return null;
    }

    public static boolean difficultyMatch(World world, boolean force) {
        if (force) {
            boolean yes = false;
            String currentDifficulty = idToDifficulty(world.getDifficulty().getId());
            for (String difficulty : ApatheticConfig.COMMON.difficulties.get()) {
                if (currentDifficulty.equals(difficulty)) {
                    yes = true;
                }
            }
            isCorrectDifficulty = yes;
            configReloaded = false;
            return yes;
        } else {
             return isCorrectDifficulty;
        }
    }

    public static void difficultyChange(Difficulty difficulty) {
        if (allowedDifficulties.contains(idToDifficulty(difficulty.getId()))) {
            isCorrectDifficulty = true;
        } else isCorrectDifficulty = false;
    }

    public static String idToDifficulty(int id) {
        switch (id) {
            case 0:
                return "peaceful";
            case 1:
                return "easy";
            case 2:
                return "normal";
            case 3:
                return "hard";
            default:
                return "hard";
        }
    }
}

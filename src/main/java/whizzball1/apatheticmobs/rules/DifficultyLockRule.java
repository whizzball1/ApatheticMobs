package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DifficultyLockRule extends Rule {

    public static boolean isCorrectDifficulty;
    public static Set<String> allowedDifficulties = new HashSet<>();

    public boolean shouldExecute(Entity ent) {
        if (!ApatheticConfig.rules.difficultyLock) return false;
        return true;
    }

    public int priority() {
        return 4;
    }

    public boolean execute(Entity ent) {
        return difficultyMatch(ent.getEntityWorld(), false);
    }

    public Set<String> allowedModules() {
        return null;
    }

    public static boolean difficultyMatch(World world, boolean force) {
        if (force) {
            boolean yes = false;
            String currentDifficulty = idToDifficulty(world.getDifficulty().getDifficultyId());
            for (String difficulty : ApatheticConfig.rules.difficulties) {
                if (currentDifficulty.equals(difficulty)) {
                    yes = true;
                }
            }
            isCorrectDifficulty = yes;
            return yes;
        } else {
             return isCorrectDifficulty;
        }
    }

    public static void difficultyChange(EnumDifficulty difficulty) {
        if (allowedDifficulties.contains(idToDifficulty(difficulty.getDifficultyId()))) {
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

package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.Set;

public class BossRule extends Rule {
    @Override
    public boolean shouldExecute(Entity ent) {
        if (!ApatheticConfig.bossRules.bossOption) return false;
        return true;
    }

    @Override
    public boolean execute(Entity ent) {
        return ent.isNonBoss();
    }

    public int priority() {
        return 1;
    }

    public Set<String> allowedModules() {
        return null;
    }
}

package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.boss.WitherEntity;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WitherHandler {

    public static Map<UUID, WitherHandler> handlers = new HashMap<>();
    int tickCount = 0;

    public WitherEntity wither;
    public WitherHandler(WitherEntity e) {
        wither = e;
        if (!ApatheticConfig.COMMON.witherRevenge.get()) {
            wither.goalSelector.goals.removeIf(t-> t.getGoal() instanceof RangedAttackGoal);
            wither.targetSelector.goals.removeIf(t->t.getGoal() instanceof NearestAttackableTargetGoal);
        }
        if (!ApatheticConfig.COMMON.witherAttacks.get()) {
            wither.NOT_UNDEAD = t->false;
        }
    }

    public static void createNewHandler(WitherEntity e) {
        UUID id = e.getUniqueID();
        if (handlers.containsKey(id)) return;
        handlers.put(id, new WitherHandler(e));
    }

    public static void removeHandler(WitherEntity e) {
        ApatheticMobs.logger.info("removing dragon Handler!");
        WitherHandler handler = handlers.get(e.getUniqueID());
        handlers.remove(e.getUniqueID());
        if (!(handler == null)) handler.removeWither();
    }

    public void removeWither() {
        this.wither = null;
    }

    public void tick() {
        for (int i = 1; i < 3; ++i) {
            wither.updateWatchedTargetId(i, 0);
            wither.idleHeadUpdates[i - 1] = 0;
        }
        wither.setAttackTarget(null);
        tickCount++;
        if (tickCount == 8) {
            for (PrioritizedGoal et : wither.goalSelector.goals) {
                if (et.getGoal() instanceof RangedAttackGoal) {
                    et.getGoal().resetTask();
                }
            }
        }
    }
}
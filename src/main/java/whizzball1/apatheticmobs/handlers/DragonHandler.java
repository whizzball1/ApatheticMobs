package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.lang.ref.WeakReference;
import java.util.*;

public class DragonHandler {

    public static Map<UUID, DragonHandler> handlers = new HashMap<>();

    public WeakReference<EnderDragonEntity> dragon;

    public DragonHandler(EnderDragonEntity e) {
        dragon = new WeakReference<EnderDragonEntity>(e);
    }

    public static void createNewHandler(EnderDragonEntity e) {
        UUID id = e.getUniqueID();
        if (handlers.containsKey(id)) return;
        handlers.put(id, new DragonHandler(e));
    }

    public static void removeHandler(EnderDragonEntity e) {
        ApatheticMobs.logger.info("removing dragon Handler!");
        DragonHandler handler = handlers.get(e.getUniqueID());
        handlers.remove(e.getUniqueID());
        if (!(handler == null)) handler.removeDragon();
    }

    public void removeDragon() {
        this.dragon = null;
    }

    public void tick() {
        if (dragon == null || dragon.get() == null || !dragon.get().isAlive()) {
            removeHandler(dragon.get());
            return;
        }
        //Put config arguments here for each phase you want to block! Especially SittingFlaming.
        IPhase currentPhase = this.dragon.get().getPhaseManager().getCurrentPhase();
        //ApatheticMobs.logger.info(currentPhase.getType().toString());
        if (currentPhase.getType() == PhaseType.STRAFE_PLAYER || currentPhase.getType() == PhaseType.CHARGING_PLAYER
        && !ApatheticConfig.COMMON.dragonFlies.get()) {
            this.dragon.get().getPhaseManager().setPhase(PhaseType.LANDING_APPROACH);
        }
        if (currentPhase.getType() == PhaseType.SITTING_FLAMING || currentPhase.getType() == PhaseType.SITTING_ATTACKING
        && !ApatheticConfig.COMMON.dragonSits.get()) {
            this.dragon.get().getPhaseManager().setPhase(PhaseType.SITTING_SCANNING);
        }
    }

}

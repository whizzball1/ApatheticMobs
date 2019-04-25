package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseStrafePlayer;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.DoWhatYouWant;

import java.util.*;

public class DragonHandler {

    public static Map<UUID, DragonHandler> handlers = new HashMap<>();

    public EntityDragon dragon;

    public DragonHandler(EntityDragon e) {
        dragon = e;
    }

    public static void createNewHandler(EntityDragon e) {
        UUID id = e.getUniqueID();
        if (handlers.containsKey(id)) return;
        handlers.put(id, new DragonHandler(e));
    }

    public static void removeHandler(EntityDragon e) {
        ApatheticMobs.logger.info("removing dragon Handler!");
        DragonHandler handler = handlers.get(e.getUniqueID());
        handlers.remove(e.getUniqueID());
        if (!(handler == null)) handler.removeDragon();
    }

    public void removeDragon() {
        this.dragon = null;
    }

    public void tick() {
        //Put config arguments here for each phase you want to block! Especially SittingFlaming.
        IPhase currentPhase = this.dragon.getPhaseManager().getCurrentPhase();
        //ApatheticMobs.logger.info(currentPhase.getType().toString());
        if (currentPhase.getType() == PhaseList.STRAFE_PLAYER || currentPhase.getType() == PhaseList.CHARGING_PLAYER
        && !DoWhatYouWant.dragonFlies) {
            this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
        }
        if (currentPhase.getType() == PhaseList.SITTING_FLAMING || currentPhase.getType() == PhaseList.SITTING_ATTACKING
        && !DoWhatYouWant.dragonSits) {
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
        }
    }

}

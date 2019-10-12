package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.Set;

public class RevengeRule extends Rule {


    public boolean shouldExecute(Entity ent) {
        LivingEntity elb = (LivingEntity) ent;
        if (!ApatheticConfig.COMMON.revenge.get()) return false;
        LazyOptional<IRevengeCap> capOp = elb.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null);
        if (capOp.isPresent()) {
            IRevengeCap cap = capOp.orElse(null);
            if (elb.getRevengeTarget() == null && !cap.isVengeful()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean execute(Entity ent) {
        //ApatheticMobs.logger.info("Executing revenge rule!");
        LivingEntity elb = (LivingEntity) ent;
        IRevengeCap cap = elb.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null).orElse(null);
        if (elb.getRevengeTarget() != null) {
            cap.setVengeful(true, elb);
            return false;
        } else if (cap.isVengeful()) {
            if (!revengeOver(cap, elb)) return false;
            cap.setVengeful(false, elb);
            cap.setTimer(0);
        }
        return true;
    }

    public int priority() {
        return 1;
    }

    public Set<String> allowedModules() {
        return null;
    }

    public boolean revengeOver(IRevengeCap capability, LivingEntity entity) {
        if (!ApatheticConfig.COMMON.revengeTime.get()) return false;
        if (entity.ticksExisted - capability.getTimer() > ApatheticConfig.COMMON.revengeTimer.get()) {
            return true;
        }
        return false;
    }


}

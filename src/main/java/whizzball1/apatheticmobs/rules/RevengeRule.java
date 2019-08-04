package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.Set;

public class RevengeRule extends Rule {


    public boolean shouldExecute(Entity ent) {
        EntityLivingBase elb = (EntityLivingBase) ent;
        if (!ApatheticConfig.rules.revenge) return false;
        if (!ent.hasCapability(ApatheticMobs.REVENGE_CAPABILITY, null)) return false;
        IRevengeCap cap = elb.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null);
        if (elb.getRevengeTarget() == null && !cap.isVengeful()) {
            //ApatheticMobs.logger.info("I have failed!");
            return false;
        }
        //ApatheticMobs.logger.info(cap.isVengeful());
        return true;
    }

    public boolean execute(Entity ent) {
        //ApatheticMobs.logger.info("Executing revenge rule!");
        EntityLivingBase elb = (EntityLivingBase) ent;
        IRevengeCap cap = elb.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null);
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
        return 4;
    }

    public Set<String> allowedModules() {
        return null;
    }

    public boolean revengeOver(IRevengeCap capability, EntityLivingBase entity) {
        if (!ApatheticConfig.rules.revengeTime) return false;
        if (entity.ticksExisted - capability.getTimer() > ApatheticConfig.rules.revengeTimer) {
            return true;
        }
        return false;
    }


}

package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.rules.Rule;

public class RuleHandler {

    @SubscribeEvent
    public void apathy(LivingSetAttackTargetEvent e) {
        if (e.getTarget() instanceof EntityPlayer) {
            for (Rule rule : ApatheticMobs.rules.defaultRules) {
                if (rule.shouldExecute(e.getEntity())) if (!rule.execute(e.getEntity())) {
                    return;
                }
            }
            EntityLivingBase entity = e.getEntityLiving();
            if (entity instanceof EntityLiving) {
                ((EntityLiving) entity).setAttackTarget(null);
                entity.setRevengeTarget(null);
            }
        }
    }

}

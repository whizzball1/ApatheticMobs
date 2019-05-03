package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;

import javax.annotation.Nullable;
import java.util.Set;

public class PlayerWhitelistRule extends Rule {

    public boolean shouldExecute(Entity ent) {
        if (!ApatheticConfig.rules.playerWhitelist) return false;
        if (!(((EntityLiving) ent).getAttackTarget() instanceof EntityPlayer)) return false;
        return true;
    }

    public int priority() {
        return 3;
    }

    public boolean execute(Entity ent) {
        EntityPlayer ep = (EntityPlayer) ((EntityLiving) ent).getAttackTarget();
        if (WhitelistData.get(ep.getEntityWorld()).playerSet.contains(ep.getUniqueID())) return true;
        return false;
    }

    @Nullable
    public Set<String> allowedModules() {
        return null;
    }




}

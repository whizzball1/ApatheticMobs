package whizzball1.apatheticmobs.capability;

import net.minecraft.entity.EntityLivingBase;
import whizzball1.apatheticmobs.config.ApatheticConfig;

public class RevengeCapability implements IRevengeCap {
    private boolean revenge = false;
    public int revengeTimer = 0;

    @Override
    public boolean isVengeful() {
        return revenge;
    }

    @Override
    public void setVengeful(boolean venge, EntityLivingBase entity) {
        revenge = venge;
        if (venge && ApatheticConfig.rules.revengeTime && entity.ticksExisted - revengeTimer < ApatheticConfig.rules.revengeTimer)
            setTimer(entity.ticksExisted);
    }

    @Override
    public void setVengeful(boolean venge) {
        revenge = venge;
    }

    @Override
    public void setTimer(int timer) {
        this.revengeTimer = timer;
    }

    @Override
    public int getTimer() {
        return revengeTimer;
    }
}

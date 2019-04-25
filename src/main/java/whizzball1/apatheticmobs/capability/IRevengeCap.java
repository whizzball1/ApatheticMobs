package whizzball1.apatheticmobs.capability;

import net.minecraft.entity.EntityLivingBase;

public interface IRevengeCap {

    public boolean isVengeful();
    public void setVengeful(boolean venge, EntityLivingBase entity);
    public void setVengeful(boolean venge);
    public void setTimer(int timer);
    public int getTimer();
}

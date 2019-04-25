package whizzball1.apatheticmobs.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RevengeStorage implements Capability.IStorage<IRevengeCap> {

    public static Capability.IStorage<IRevengeCap> STORAGE = new RevengeStorage();

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRevengeCap> capability, IRevengeCap instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("venge", new NBTTagInt(instance.isVengeful()? 1 : 0));
        nbt.setTag("timer", new NBTTagInt(instance.getTimer()));
        return nbt;
    }

    @Override
    public void readNBT(Capability<IRevengeCap> capability, IRevengeCap instance, EnumFacing side, NBTBase nbt) {
        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound nbtc = (NBTTagCompound) nbt;
            if (nbtc.hasKey("venge")) if (nbtc.getInteger("venge") == 1) {
                instance.setVengeful(true);
            } else instance.setVengeful(false);
            if (nbtc.hasKey("timer")) instance.setTimer(nbtc.getInteger("timer"));
        } else if (nbt instanceof NBTTagInt) {
            NBTTagInt nbti = (NBTTagInt) nbt;
            if (nbti.getInt() == 1) {
                instance.setVengeful(true);
            } else instance.setVengeful(false);
        } else throw new IllegalStateException("Revenge capability should be NBTTagInt!");
    }
}

package whizzball1.apatheticmobs.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RevengeStorage implements Capability.IStorage<IRevengeCap> {

    public static Capability.IStorage<IRevengeCap> STORAGE = new RevengeStorage();

    @Nullable
    @Override
    public INBT writeNBT(Capability<IRevengeCap> capability, IRevengeCap instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("venge", new IntNBT(instance.isVengeful()? 1 : 0));
        nbt.put("timer", new IntNBT(instance.getTimer()));
        return nbt;
    }

    @Override
    public void readNBT(Capability<IRevengeCap> capability, IRevengeCap instance, Direction side, INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            CompoundNBT nbtc = (CompoundNBT) nbt;
            if (nbtc.contains("venge")) if (nbtc.getInt("venge") == 1) {
                instance.setVengeful(true);
            } else instance.setVengeful(false);
            if (nbtc.contains("timer")) instance.setTimer(nbtc.getInt("timer"));
        } else if (nbt instanceof IntNBT) {
            IntNBT nbti = (IntNBT) nbt;
            if (nbti.getInt() == 1) {
                instance.setVengeful(true);
            } else instance.setVengeful(false);
        } else throw new IllegalStateException("Revenge capability should be NBTTagInt!");
    }
}

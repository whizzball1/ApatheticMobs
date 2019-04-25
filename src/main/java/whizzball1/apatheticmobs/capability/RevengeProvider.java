package whizzball1.apatheticmobs.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import whizzball1.apatheticmobs.ApatheticMobs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RevengeProvider implements ICapabilitySerializable<NBTTagInt> {

    public static final ResourceLocation NAME = new ResourceLocation(ApatheticMobs.MOD_ID, "revenge");

    private IRevengeCap innerCap = ApatheticMobs.REVENGE_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ApatheticMobs.REVENGE_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ApatheticMobs.REVENGE_CAPABILITY) {
            return ApatheticMobs.REVENGE_CAPABILITY.cast(innerCap);
        }
        else return null;
    }

    @Override
    public NBTTagInt serializeNBT() {
        return new NBTTagInt(innerCap.isVengeful() ? 1 : 0);
    }

    @Override
    public void deserializeNBT(NBTTagInt nbt) {
        NBTTagInt revenge = (NBTTagInt) nbt;
        if (revenge.getInt() == 1) {
            innerCap.setVengeful(true);
        } else innerCap.setVengeful(false);
    }
}

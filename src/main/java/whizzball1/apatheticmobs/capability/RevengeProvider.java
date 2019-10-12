package whizzball1.apatheticmobs.capability;

import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import whizzball1.apatheticmobs.ApatheticMobs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RevengeProvider implements ICapabilitySerializable<IntNBT> {

    public static final ResourceLocation NAME = new ResourceLocation(ApatheticMobs.MOD_ID, "revenge");

    private IRevengeCap innerCap = ApatheticMobs.REVENGE_CAPABILITY.getDefaultInstance();

    private LazyOptional<IRevengeCap> opt;

    public RevengeProvider() {
        opt = LazyOptional.of(() -> innerCap);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return ApatheticMobs.REVENGE_CAPABILITY.orEmpty(capability, opt);
    }

    @Override
    public IntNBT serializeNBT() {
        return new IntNBT(innerCap.isVengeful() ? 1 : 0);
    }

    @Override
    public void deserializeNBT(IntNBT nbt) {
        IntNBT revenge = (IntNBT) nbt;
        if (revenge.getInt() == 1) {
            innerCap.setVengeful(true);
        } else innerCap.setVengeful(false);
    }
}

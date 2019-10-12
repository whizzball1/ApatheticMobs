package whizzball1.apatheticmobs.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import whizzball1.apatheticmobs.ApatheticMobs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class WhitelistData extends WorldSavedData {

    public static WhitelistData data;
    public static String dataName = ApatheticMobs.MOD_ID + "_data";
    public static Supplier<WhitelistData> dataSupplier = () -> get();
    public Set<UUID> playerSet = new HashSet<>();

    public WhitelistData(String name) {
        super(name);
    }

    //Default Supplier
    public static WhitelistData get() {
        return new WhitelistData(dataName);
    }

    /*Active Supplier?
    * Make sure this world is always a server world!*/
    public static WhitelistData get(World world) {
        DimensionSavedDataManager manager = world.getServer().getWorld(DimensionType.OVERWORLD).getSavedData();
        return manager.getOrCreate(dataSupplier, dataName);
    }

    public CompoundNBT write(CompoundNBT compound) {
        ApatheticMobs.logger.info("writing to NBT!");
        ListNBT playerList = new ListNBT();
        for (UUID player : playerSet) {
            CompoundNBT newCompound = new CompoundNBT();
            newCompound.putUniqueId("UUID", player);
            playerList.add(newCompound);
        }
        compound.put("List", playerList);
        return compound;
    }

    public void read(CompoundNBT compound) {
        ApatheticMobs.logger.info("reading from NBT!");
        ListNBT playerList = compound.getList("List", 10);
        playerList.iterator().forEachRemaining(t -> playerSet.add(((CompoundNBT) t).getUniqueId("UUID")));
    }



}

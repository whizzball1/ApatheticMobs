package whizzball1.apatheticmobs.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import whizzball1.apatheticmobs.ApatheticMobs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WhitelistData extends WorldSavedData {

    public static WhitelistData data;
    public static String dataName = ApatheticMobs.MOD_ID + "_data";
    public Set<UUID> playerSet = new HashSet<>();

    public WhitelistData(String name) {
        super(name);
        this.markDirty();
    }

    public static WhitelistData get(World world) {
        WhitelistData instance = (WhitelistData) world.getMapStorage().getOrLoadData(WhitelistData.class, dataName);
        if (instance == null) instance = new WhitelistData(dataName);
        return instance;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList playerList = new NBTTagList();
        for (UUID player : playerSet) {
            NBTTagCompound newCompound = new NBTTagCompound();
            newCompound.setUniqueId("UUID", player);
            playerList.appendTag(newCompound);
        }
        compound.setTag("List", playerList);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList playerList = compound.getTagList("List", 10);
        playerList.iterator().forEachRemaining(t -> playerSet.add(((NBTTagCompound) t).getUniqueId("UUID")));
    }

}

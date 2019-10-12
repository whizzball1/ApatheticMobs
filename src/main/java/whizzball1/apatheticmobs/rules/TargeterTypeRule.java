package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import whizzball1.apatheticmobs.config.ApatheticConfig;

import java.util.HashSet;
import java.util.Set;

public class TargeterTypeRule extends Rule {

    public static Set<ResourceLocation> exclusions = new HashSet<>();
    public static Set<ResourceLocation> inclusions = new HashSet<>();

    public boolean shouldExecute(Entity ent) {
        return true;
    }

    public boolean execute(Entity ent) {
        if (!(ent instanceof MobEntity)) {
            return false;
        }
        boolean yes = ApatheticConfig.COMMON.blacklist.get() ?
                !exclusions.contains(ForgeRegistries.ENTITIES.getKey(ent.getType())) :
                inclusions.contains(ForgeRegistries.ENTITIES.getKey(ent.getType()));
        return yes;
    }

    public int priority() {
        return 2;
    }

    public Set<String> allowedModules() {
        return null;
    }

}

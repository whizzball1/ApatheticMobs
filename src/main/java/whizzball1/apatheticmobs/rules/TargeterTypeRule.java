package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
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
        if (!(ent instanceof EntityLiving)) {
            return false;
        }
        boolean yes = ApatheticConfig.rules.blacklist ?
                !exclusions.contains(EntityList.getKey(ent.getClass())) :
                inclusions.contains(EntityList.getKey(ent.getClass()));
        return yes;
    }

    public int priority() {
        return 2;
    }

    public Set<String> allowedModules() {
        return null;
    }

}

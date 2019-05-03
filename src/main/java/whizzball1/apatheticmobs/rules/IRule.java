package whizzball1.apatheticmobs.rules;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.Set;

public interface IRule {
    public boolean shouldExecute(Entity ent);

    public boolean execute(Entity ent);

    public int priority();

    @Nullable
    public Set<String> allowedModules();


}

package whizzball1.apatheticmobs.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.botania.common.entity.EntityMagicLandmine;
import vazkii.botania.common.entity.EntityMagicMissile;

public class BotaniaHandler {
    @SubscribeEvent
    public void gaiaSpawns(EntityJoinWorldEvent e) {
        ResourceLocation key = EntityList.getKey(e.getEntity());
        if (key.equals(new ResourceLocation("botania", "magic_missile"))
        || key.equals(new ResourceLocation("botania", "magic_landmine"))) {
            e.setCanceled(true);
        }
    }

}

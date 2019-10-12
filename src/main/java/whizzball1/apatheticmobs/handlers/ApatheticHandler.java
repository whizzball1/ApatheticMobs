package whizzball1.apatheticmobs.handlers;

import com.google.common.collect.Iterables;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.core.jmx.Server;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.capability.RevengeProvider;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;
import whizzball1.apatheticmobs.rules.DifficultyLockRule;
import whizzball1.apatheticmobs.rules.TargeterTypeRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class ApatheticHandler {

//    @SubscribeEvent
//    public void apathy(LivingSetAttackTargetEvent e) {
//            if (ApatheticConfig.COMMON.difficultyLock.get()) {
//                if (!(difficultyMatch(e))) return;
//            }
//            LivingEntity entity = e.getEntityLiving();
//            if (isCorrectPlayer(e.getTarget()) && entity instanceof MobEntity && doI(entity)) {
//                ((MobEntity) entity).setAttackTarget(null);
//                entity.setRevengeTarget(null);
//            }
//    }



    @SubscribeEvent
    public void ignoreDamage(LivingDamageEvent e) {
        if (!e.getEntityLiving().getEntityWorld().isRemote) {
            if (e.getEntityLiving() instanceof PlayerEntity) {
                if (e.getSource().getDamageType().equals("mob")) {
                    if (e.getSource().getTrueSource() instanceof SlimeEntity) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkSpecialSpawns(LivingSpawnEvent.SpecialSpawn e) {
        if (!e.getEntityLiving().getEntityWorld().isRemote) {
            if (e.getEntityLiving() instanceof EnderDragonEntity) {
                DragonHandler.createNewHandler((EnderDragonEntity) e.getEntityLiving());
                ApatheticMobs.logger.info("A dragon has spawned!");
            } else if (e.getEntityLiving() instanceof WitherEntity) {
                WitherHandler.createNewHandler((WitherEntity) e.getEntityLiving());
                ApatheticMobs.logger.info("A wither has spawned!");
            }
        }

    }

    @SubscribeEvent
    public void checkSpawns(EntityJoinWorldEvent e) {
        Entity ent = e.getEntity();
        if (!ent.getEntityWorld().isRemote) if (ent instanceof MobEntity){
            if (!ApatheticConfig.COMMON.revenge.get()) {
                //ApatheticMobs.logger.info(EntityList.getKey(ent) + "cancelling revenge");
                ((MobEntity) ent).goalSelector.goals.removeIf(t->t.getGoal() instanceof HurtByTargetGoal);
                //((EntityLiving) ent).tasks.executingTaskEntries.removeIf(t->t.action instanceof EntityAIHurtByTarget);
            }
            if (ent instanceof EnderDragonEntity) {
                DragonHandler.createNewHandler((EnderDragonEntity)ent);
                ApatheticMobs.logger.info("A dragon has spawned!");
            } else if (ent instanceof WitherEntity) {
                WitherHandler.createNewHandler((WitherEntity) ent);
                ApatheticMobs.logger.info("A wither has spawned!");
            }
            ResourceLocation key = ForgeRegistries.ENTITIES.getKey(ent.getType());
            if (key != null) {
                if (!ApatheticConfig.COMMON.gaia.get()) {
                    if (key.equals(new ResourceLocation("botania", "magic_missile"))
                            || key.equals(new ResourceLocation("botania", "magic_landmine"))) {
                        e.setCanceled(true);
                    }
                }
//                if (key.equals(new ResourceLocation("mightyenderchicken", "ent_EggBomb"))) {
//                    e.setCanceled(true);
//                }
//                if (!ApatheticConfig.COMMON.chaosProjectiles.get())
//                    if (key.equals(new ResourceLocation("draconicevolution", "GuardianProjectile"))) {
//                        e.setCanceled(true);
//                }
            }
        }
    }

    @SubscribeEvent
    public void addCap(AttachCapabilitiesEvent<Entity> e) {
        if (ApatheticConfig.COMMON.revenge.get()) if (e.getObject() instanceof MobEntity) if (!e.getObject().getEntityWorld().isRemote) {
            e.addCapability(RevengeProvider.NAME, new RevengeProvider());
        }
    }

    @SubscribeEvent
    public void dragonUnload(WorldEvent.Unload e) {
        if (!e.getWorld().getWorld().isRemote) {
            for (Entity i : ((ServerWorld)e.getWorld()).getEntities(EntityType.ENDER_DRAGON,t->true)) {
                DragonHandler.removeHandler((EnderDragonEntity) i);
            }
            for (Entity i : ((ServerWorld)e.getWorld()).getEntities(EntityType.WITHER, t->true)) {
                WitherHandler.removeHandler((WitherEntity)i);
            }
        }
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent e) {
        if (!e.getEntity().getEntityWorld().isRemote) {
            Entity en = e.getEntity();
            if (en instanceof EnderDragonEntity) {
                DragonHandler.removeHandler((EnderDragonEntity)en);
            } else if (en instanceof WitherEntity) {
                WitherHandler.removeHandler((WitherEntity)en);
            }
        }

    }

    @SubscribeEvent
    public void tickHandler(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote) {
            int i = ApatheticMobs.random.nextInt(5);
            if (i == 4) {
                if (DragonHandler.handlers.size() > 0) {
                    for (UUID id : DragonHandler.handlers.keySet()) {
                        DragonHandler.handlers.get(id).tick();
                    }
                }
            }
            if (!ApatheticConfig.COMMON.witherAttacks.get()){
                for (UUID id : WitherHandler.handlers.keySet()) {
                    WitherHandler.handlers.get(id).tick();
                }

            }
        }

    }

    @SubscribeEvent
    public void onDifficultyChanged(DifficultyChangeEvent e) {
        DifficultyLockRule.difficultyChange(e.getDifficulty());
        ApatheticMobs.logger.info("Difficulty changed!");
    }


//    public boolean doI(LivingEntity entity) {
//        boolean yes;
//        if (!entity.isNonBoss() && ApatheticConfigOld.bossRules.bossOption) return false;
//        if (ApatheticConfigOld.rules.blacklist) {
//            yes = true;
//            for (String id : ApatheticConfigOld.rules.exclusions) {
//                ResourceLocation loc = ForgeRegistries.ENTITIES.getKey(entity.getType());
//                if (loc.toString().equals(id)) {
//                    yes = false;
//                    break;
//                }
//            }
//        } else {
//            yes = false;
//            for (String id : ApatheticConfigOld.rules.inclusions) {
//                ResourceLocation loc = ForgeRegistries.ENTITIES.getKey(entity.getType());
//                if (loc.toString().equals(id)) {
//                    yes = true;
//                    break;
//                }
//            }
//        }
//        if (yes && ApatheticConfigOld.rules.revenge) {
//            IRevengeCap capability = null;
//            if (entity.hasCapability(ApatheticMobs.REVENGE_CAPABILITY, null)) {
//                capability = entity.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null);
//            }
//            if (entity.getRevengeTarget() != null) {
//                yes = false;
//                if (capability != null) capability.setVengeful(true, entity);
//            } else if (capability != null) if (capability.isVengeful()) {
//                if (!revengeOver(capability, entity)) {
//                    yes = false;
//                } else {
//                    capability.setVengeful(false, entity);
//                    capability.setTimer(0);
//                }
//            }
//        }
//        if (yes && !ApatheticConfigOld.rules.revenge && entity.getRevengeTarget() != null) entity.setRevengeTarget(null);
//        return yes;
//    }

    public boolean isCorrectPlayer(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity ep = (PlayerEntity) entity;
            if (ApatheticConfig.COMMON.playerWhitelist.get()) {
                if (WhitelistData.get(entity.getEntityWorld()).playerSet.contains(ep.getUniqueID())) {
                    return true;
                } else return false;
            } else return true;
        } else return false;
    }

    public String idToDifficulty(int id) {
        switch (id) {
            case 0:
                return "peaceful";
            case 1:
                return "easy";
            case 2:
                return "normal";
            case 3:
                return "hard";
            default:
                return "hard";
        }
    }

    public boolean difficultyMatch(LivingEvent e) {
        boolean yes = false;
        String currentDifficulty = idToDifficulty(e.getEntityLiving().getEntityWorld().getDifficulty().getId());
        for (String difficulty : ApatheticConfig.COMMON.difficulties.get()) {
            if (currentDifficulty.equals(difficulty)) {
                yes = true;
            }
        }
        return yes;
    }

    public boolean revengeOver(IRevengeCap capability, LivingEntity entity) {
        if (!ApatheticConfig.COMMON.revengeTime.get()) return false;
        if (entity.ticksExisted - capability.getTimer() > ApatheticConfig.COMMON.revengeTimer.get()) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
        if (e.getModID().equals(ApatheticMobs.MOD_ID)) {
            //ConfigManager.sync(ApatheticMobs.MOD_ID, Config.Type.INSTANCE);

            DifficultyLockRule.allowedDifficulties.clear();
            ApatheticConfig.COMMON.difficulties.get().forEach(t -> DifficultyLockRule.allowedDifficulties.add(t));

            TargeterTypeRule.exclusions.clear();
            ApatheticConfig.COMMON.exclusions.get().forEach(t -> TargeterTypeRule.exclusions.add(new ResourceLocation(t)));
            TargeterTypeRule.inclusions.clear();
            ApatheticConfig.COMMON.inclusions.get().forEach(t -> TargeterTypeRule.inclusions.add(new ResourceLocation(t)));

            DifficultyLockRule.configReloaded = true;
            ApatheticMobs.logger.info("Config reloaded!");
        }
    }

}

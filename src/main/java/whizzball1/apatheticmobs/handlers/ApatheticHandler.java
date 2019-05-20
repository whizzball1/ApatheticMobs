package whizzball1.apatheticmobs.handlers;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import whizzball1.apatheticmobs.ApatheticMobs;
import whizzball1.apatheticmobs.capability.IRevengeCap;
import whizzball1.apatheticmobs.capability.RevengeProvider;
import whizzball1.apatheticmobs.config.ApatheticConfig;
import whizzball1.apatheticmobs.data.WhitelistData;
import whizzball1.apatheticmobs.rules.DifficultyLockRule;
import whizzball1.apatheticmobs.rules.TargeterTypeRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class ApatheticHandler {

    //@SubscribeEvent
    public void apathy(LivingSetAttackTargetEvent e) {
            if (ApatheticConfig.rules.difficultyLock) {
                if (!(difficultyMatch(e))) return;
            }
            EntityLivingBase entity = e.getEntityLiving();
            if (isCorrectPlayer(e.getTarget()) && entity instanceof EntityLiving && doI(entity)) {
                ((EntityLiving) entity).setAttackTarget(null);
                entity.setRevengeTarget(null);
            }
    }



    @SubscribeEvent
    public void ignoreDamage(LivingDamageEvent e) {
        if (!e.getEntityLiving().getEntityWorld().isRemote) {
            if (e.getEntityLiving() instanceof EntityPlayer) {
                if (e.getSource().getDamageType().equals("mob")) {
                    if (e.getSource().getTrueSource() instanceof EntitySlime) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkSpecialSpawns(LivingSpawnEvent.SpecialSpawn e) {
        if (!e.getEntityLiving().getEntityWorld().isRemote) {
            if (e.getEntityLiving() instanceof EntityDragon) {
                DragonHandler.createNewHandler((EntityDragon) e.getEntityLiving());
                ApatheticMobs.logger.info("A dragon has spawned!");
            } else if (e.getEntityLiving() instanceof EntityWither) {
                WitherHandler.createNewHandler((EntityWither) e.getEntityLiving());
                ApatheticMobs.logger.info("A wither has spawned!");
            }
        }

    }

    @SubscribeEvent
    public void checkSpawns(EntityJoinWorldEvent e) {
        Entity ent = e.getEntity();
        if (!ent.getEntityWorld().isRemote) if (ent instanceof EntityLiving){
            if (!ApatheticConfig.rules.revenge) {
                //ApatheticMobs.logger.info(EntityList.getKey(ent) + "cancelling revenge");
                ((EntityLiving) ent).targetTasks.taskEntries.removeIf(t->t.action instanceof EntityAIHurtByTarget);
                //((EntityLiving) ent).tasks.executingTaskEntries.removeIf(t->t.action instanceof EntityAIHurtByTarget);
            }
            if (ent instanceof EntityDragon) {
                DragonHandler.createNewHandler((EntityDragon)ent);
                ApatheticMobs.logger.info("A dragon has spawned!");
            } else if (ent instanceof EntityWither) {
                WitherHandler.createNewHandler((EntityWither) ent);
                ApatheticMobs.logger.info("A wither has spawned!");
            }
            ResourceLocation key = EntityList.getKey(ent);
            if (key != null) {
                if (!ApatheticConfig.bossRules.gaia) {
                    if (key.equals(new ResourceLocation("botania", "magic_missile"))
                            || key.equals(new ResourceLocation("botania", "magic_landmine"))) {
                        e.setCanceled(true);
                    }
                }
                if (key.equals(new ResourceLocation("mightyenderchicken", "ent_EggBomb"))) {
                    e.setCanceled(true);
                }
                if (!ApatheticConfig.bossRules.chaosProjectiles)
                    if (key.equals(new ResourceLocation("draconicevolution", "GuardianProjectile"))) {
                        e.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void addCap(AttachCapabilitiesEvent<Entity> e) {
        if (ApatheticConfig.rules.revenge) if (e.getObject() instanceof EntityLiving) if (!e.getObject().getEntityWorld().isRemote) {
            e.addCapability(RevengeProvider.NAME, new RevengeProvider());
        }
    }

    @SubscribeEvent
    public void worldCap(AttachCapabilitiesEvent<World> e) {

    }

    @SubscribeEvent
    public void dragonUnload(WorldEvent.Unload e) {
        if (!e.getWorld().isRemote) {
            for (EntityDragon i : e.getWorld().getEntities(EntityDragon.class, t->true)) {
                DragonHandler.removeHandler(i);
            }
            for (EntityWither i : e.getWorld().getEntities(EntityWither.class, t->true)) {
                WitherHandler.removeHandler(i);
            }
        }
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent e) {
        if (!e.getEntity().getEntityWorld().isRemote) {
            Entity en = e.getEntity();
            if (en instanceof EntityDragon) {
                DragonHandler.removeHandler((EntityDragon)en);
            } else if (en instanceof EntityWither) {
                WitherHandler.removeHandler((EntityWither)en);
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
            if (!ApatheticConfig.bossRules.witherAttacks){
                for (UUID id : WitherHandler.handlers.keySet()) {
                    WitherHandler.handlers.get(id).tick();
                }

            }
        }

    }

    @SubscribeEvent
    public void onDifficultyChanged(DifficultyChangeEvent e) {
        DifficultyLockRule.difficultyChange(e.getDifficulty());
    }


    public boolean doI(EntityLivingBase entity) {
        boolean yes;
        if (!entity.isNonBoss() && ApatheticConfig.bossRules.bossOption) return false;
        if (ApatheticConfig.rules.blacklist) {
            yes = true;
            for (String id : ApatheticConfig.rules.exclusions) {
                ResourceLocation loc = EntityList.getKey(entity.getClass());
                if (loc.toString().equals(id)) {
                    yes = false;
                    break;
                }
            }
        } else {
            yes = false;
            for (String id : ApatheticConfig.rules.inclusions) {
                ResourceLocation loc = EntityList.getKey(entity.getClass());
                if (loc.toString().equals(id)) {
                    yes = true;
                    break;
                }
            }
        }
        if (yes && ApatheticConfig.rules.revenge) {
            IRevengeCap capability = null;
            if (entity.hasCapability(ApatheticMobs.REVENGE_CAPABILITY, null)) {
                capability = entity.getCapability(ApatheticMobs.REVENGE_CAPABILITY, null);
            }
            if (entity.getRevengeTarget() != null) {
                yes = false;
                if (capability != null) capability.setVengeful(true, entity);
            } else if (capability != null) if (capability.isVengeful()) {
                if (!revengeOver(capability, entity)) {
                    yes = false;
                } else {
                    capability.setVengeful(false, entity);
                    capability.setTimer(0);
                }
            }
        }
        if (yes && !ApatheticConfig.rules.revenge && entity.getRevengeTarget() != null) entity.setRevengeTarget(null);
        return yes;
    }

    public boolean isCorrectPlayer(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            if (ApatheticConfig.rules.playerWhitelist) {
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
        String currentDifficulty = idToDifficulty(e.getEntityLiving().getEntityWorld().getDifficulty().getDifficultyId());
        for (String difficulty : ApatheticConfig.rules.difficulties) {
            if (currentDifficulty.equals(difficulty)) {
                yes = true;
            }
        }
        return yes;
    }

    public boolean revengeOver(IRevengeCap capability, EntityLivingBase entity) {
        if (!ApatheticConfig.rules.revengeTime) return false;
        if (entity.ticksExisted - capability.getTimer() > ApatheticConfig.rules.revengeTimer) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
        if (e.getModID().equals(ApatheticMobs.MOD_ID)) {
            ConfigManager.sync(ApatheticMobs.MOD_ID, Config.Type.INSTANCE);

            DifficultyLockRule.allowedDifficulties.clear();
            Collections.addAll(DifficultyLockRule.allowedDifficulties, ApatheticConfig.rules.difficulties);

            TargeterTypeRule.exclusions.clear();
            Arrays.asList(ApatheticConfig.rules.exclusions).forEach(t -> TargeterTypeRule.exclusions.add(new ResourceLocation(t)));
            TargeterTypeRule.inclusions.clear();
            Arrays.asList(ApatheticConfig.rules.inclusions).forEach(t -> TargeterTypeRule.inclusions.add(new ResourceLocation(t)));

        }
    }

}

package net.fabricmc.example;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.Projectile;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Minuteman extends PillagerEntity implements Ally {
    MinecraftClientExtended ExtCli;
    int cscover;
    int AP;
    int at;
    ArrayList<String> availableMoves;
    List<String> animlines;
    public Minuteman(EntityType<? extends PillagerEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
        ExtCli = ((MinecraftClientExtended)MinecraftClient.getInstance());
        AP = 2;
        at = 0;
        cscover = new Integer(0);
        animlines = Collections.emptyList();
        prepareAnimation();
        availableMoves = new ArrayList<>();
        availableMoves.add("CrossbowShoot");
        availableMoves.add("CrossbowSuppress");
        availableMoves.add("PickUp");
    }
    public boolean isAngryAt(PlayerEntity playerEntity_1) {
        return false;
    }
    protected void initGoals() {
        this.targetSelector.add(2, new FollowTargetGoal(this, HostileEntity.class, true));
        this.goalSelector.add(3, new CrossbowDefendGoal(this, 1.0D, 8.0F));
        this.goalSelector.add(4, new LookAtEnemyGoal(this, VillagerEntity.class, 30.0F));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PillagerEntity.class, 15.0F));
    }

    @Override
    public ArrayList<String> availableMoves() {
        return availableMoves;
    }

    public int getAP() { return AP; }

    public void spendAP(int amount) { AP -= amount; }

    public double shortDistance() { return 4.0D; }

    public double longDistance() { return 8.0D; }

    public void refresh() {
        AP = 2;
        //Update any specials cooldown here.
    }

    public int isCovered() {
        //System.out.println("CoverStatus : " + this + " : " + CoverStatus);
        //return cs.cover;
        ///*

        return this.cscover;
        //*/

         //return 1;
    }
    public void setCovered() {
        //System.out.println(this + " is being set true.");
        //System.out.println("Setting Cover for " + this + " : " + i);
        //cs.cover++;
        this.cscover = new Integer(2);
    }
    public Ally.State getState2() {
        if (this.isCharging()) {
            return Ally.State.CROSSBOW_CHARGE;
        }
        if (ExtCli.getTacticalHud() != null && ExtCli.getTacticalHud().focusedAlly != null && ((Minuteman)(ExtCli.getTacticalHud().focusedAlly)).getUuid() == this.getUuid()) {
            if (ExtCli.getTacticalHud().status == TacticalHud.Status.Selection) {
                //Do the general animation, personality animation 1
                return Ally.State.COVERED;
            }
            if (ExtCli.getTacticalHud().status == TacticalHud.Status.Attacking) {
                //Do attack prepare animation, personality animation 2. Looking at enemies.
                return Ally.State.ATTACKING;
            }
            if (ExtCli.getTacticalHud().status == TacticalHud.Status.Defending) {
                //Do suppression animation?
            }
            if (ExtCli.getTacticalHud().status == TacticalHud.Status.Helping) {
                //Same as Attacking, but cycle through allies instead.
            }
            if (ExtCli.getTacticalHud().status == TacticalHud.Status.Moving) {
                //Randomly select from a pool of animations for movement, depending on type of movement, injury status, single direction movement, etc.
            }
            if (ExtCli.getTacticalHud().status == TacticalHud.Status.EnemyTurn) {
                //Whatever position or animation when it is enemy turn. Is covered?
                return Ally.State.COVERED;
            }
        }
        return this.isAttacking() ? Ally.State.ATTACKING : Ally.State.CROSSED;
    }
    /*
    Need some sort of function that decides, when attack is definite, to set the read handle for the animation, read everything to Buffer.
    Parse the buffer, and put into an array of modelContainer objects.
    The 'askAnimation()' function asks for the currently pointed to modelContainer to return. The iterator is determined either by call, timer, or tick.
     */
///*
    public void prepareAnimation() {
        if (isCovered() > 0) {
            System.out.println("INV!");
        }
        else {
            System.out.println("Not covered!");
        }
        animlines = Collections.emptyList();
        System.out.println(animlines + " is animlines.");
        try {
            animlines = Files.readAllLines(Paths.get("testanimation"), StandardCharsets.UTF_8);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if (animlines.size() > 0) {
            System.out.println(animlines.get(0));
        }
    }
    public ModelContainer askAnimation() {
        //Gives the current yaw, pitch, rotationPointX, rotationPointZ.
        int syncTick = at;
        CuboidData rightArm = new CuboidData();
        CuboidData leftArm = new CuboidData();
        CuboidData head = new CuboidData();
        CuboidData torso = new CuboidData();
        CuboidData rightLeg = new CuboidData();
        CuboidData leftLeg = new CuboidData();


        if (animlines.size() > syncTick) {
            String[] params = animlines.get(syncTick).split("[ ]+");
            if (params[0].equals("shoot")) { //Eventually replace with map of lambda functions?
                Vec3d v = this.getRotationVector();
                new LlamaSpitEntity(world,this.x-0.2,this.y+1.5,this.z,v.x,v.y,v.z);
                ArrowEntity a = new ArrowEntity(world,this.x,this.y+1,this.z);
                a.setVelocity(v.x,v.y,v.z);
                world.spawnEntity(a);
                Projectile projectile_2 = new ArrowEntity(world,this.x,this.y+1,this.z);

                projectile_2.setVelocity( v.x,  v.y+1,  v.z, 3.2f, 1.0f);
                world.spawnEntity((Entity) projectile_2);
                at++;
            } else {
                rightArm.pitch = Float.parseFloat(params[0]);
                rightArm.yaw = Float.parseFloat(params[1]);
                rightArm.rotationPointX = Float.parseFloat(params[2]);
                rightArm.rotationPointZ = Float.parseFloat(params[3]);
                rightArm.x = Float.parseFloat(params[4]);
                rightArm.z = Float.parseFloat(params[5]);
                leftArm.pitch = Float.parseFloat(params[6]);
                leftArm.yaw = Float.parseFloat(params[7]);
                leftArm.rotationPointX = Float.parseFloat(params[8]);
                leftArm.rotationPointZ = Float.parseFloat(params[9]);
                leftArm.x = Float.parseFloat(params[10]);
                leftArm.z = Float.parseFloat(params[11]);
                head.pitch = Float.parseFloat(params[12]);
                head.yaw = Float.parseFloat(params[13]);
                head.rotationPointX = Float.parseFloat(params[14]);
                head.rotationPointZ = Float.parseFloat(params[15]);
                head.x = Float.parseFloat(params[16]);
                head.z = Float.parseFloat(params[17]);
                torso.pitch = Float.parseFloat(params[18]);
                torso.yaw = Float.parseFloat(params[19]);
                torso.rotationPointX = Float.parseFloat(params[20]);
                torso.rotationPointZ = Float.parseFloat(params[21]);
                torso.x = Float.parseFloat(params[22]);
                torso.z = Float.parseFloat(params[23]);
                rightLeg.pitch = Float.parseFloat(params[24]);
                rightLeg.yaw = Float.parseFloat(params[25]);
                rightLeg.rotationPointX = Float.parseFloat(params[26]);
                rightLeg.rotationPointZ = Float.parseFloat(params[27]);
                rightLeg.x = Float.parseFloat(params[28]);
                rightLeg.z = Float.parseFloat(params[29]);
                leftLeg.pitch = Float.parseFloat(params[30]);
                leftLeg.yaw = Float.parseFloat(params[31]);
                leftLeg.rotationPointX = Float.parseFloat(params[32]);
                leftLeg.rotationPointZ = Float.parseFloat(params[33]);
                leftLeg.x = Float.parseFloat(params[34]);
                leftLeg.z = Float.parseFloat(params[35]);
                at++;
            }
        } else {
            ((MinecraftClientExtended)MinecraftClient.getInstance()).getTacticalHud().status = TacticalHud.Status.Selection;
            at = 0;
        }

        return new ModelContainer(rightArm, leftArm, head, torso, rightLeg, leftLeg);
    }
    //*/
}

package net.fabricmc.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import java.util.ArrayList;

public class TacticalMode {
    MinecraftClient client;
    public Iterable<Entity> allEntities;
    public ArrayList<Ally> allies;
    public ArrayList<Entity> enemies;
    int it;
    Ally prevFocusAlly;
    Ally FocusedAlly;
    int enemyIt;
    Vec3d location;
    Vec3d perTick;
    Vec3d original;
    TacticalHud thud;
    public TacticalMode() {
        System.out.println("Tactical turned on!");
        client = MinecraftClient.getInstance();
        allEntities = null;
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
        it = 0;
        prevFocusAlly = null;
        FocusedAlly = null;
        enemyIt = 0;
        location = null;
        perTick = null;
        newTurn();
        if (allies.size() > 0) {
            client.getServer().getCommandManager().execute(client.getServer().getPlayerManager().getPlayer(client.player.getUuid()).getCommandSource(), "gamemode spectator");
            original = client.player.getPos();
            thud = null;
        } else {
            ((MinecraftClientExtended) client).setTacticalMode(null);
            ((MinecraftClientExtended)client).setTacticalHud(null);
            ((MinecraftClientExtended)client).setTactical(0);
        }

    }


    public void scan() {
        allEntities = client.getServer().getWorld(client.player.dimension).getEntities(HostileEntity.class,client.player.getBoundingBox().expand(10.0,10.0,10.0));
        //allEntities = client.world.getEntities();
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
        for (Entity entity : allEntities) {
            if (entity.isAlive()) {
                if (entity instanceof Ally) {
                    allies.add((Ally) entity);
                } else if (entity instanceof HostileEntity) {
                    enemies.add(entity);
                }
            }
        }
        System.out.println(allies.size() + " is allies.size().");
    }

    public Ally nextFocus() { //Always called as first 'next' call.
        if (allies.size() == 0) {
            System.out.println("allies is 0!");
            return null;
        }
        for (int i = 0; i < allies.size(); i++) {
            ((Entity)allies.get(i)).setGlowing(false);
        }
        Entity entity = null;
        for (int i = 0; i < allies.size(); i++) {
            it = it % allies.size();
            if (allies.get(it) != null) {
                entity = (Entity)allies.get(it);
                ((Entity)allies.get(it)).setGlowing(true);
                it++;
                break;
            }
            it++;
        }
        if (entity == null) {
            location = null;
            return null;
        } else {
            location = new Vec3d(entity.x, entity.y + 3, entity.z);
            perTick = new Vec3d((location.x - client.player.x) / 10, (location.y - client.player.y) / 10, (location.z - client.player.z) / 10);
            FocusedAlly = (Ally)entity;
            return FocusedAlly;
        }
    }

    public Ally nextAvailable() { //NextFocus always called before this one.
        scan();
        System.out.println("nextAvailable called.");
        if (allies.size() == 0) {
            System.out.println("allies is 0!");
            return null;
        }
        for (int i = 0; i < allies.size(); i++) {
            //System.out.println("Cover status: " +(allies.get(i)).isCovered());
            ((Entity)allies.get(i)).setGlowing(false);
        }
        Entity entity = null;
        
        for (int i = 0; i < allies.size(); i++) {
            it = it % allies.size();
            if (allies.get(it) != null && allies.get(it).getAP() > 0) {
                entity = (Entity)allies.get(it);
                entity.setGlowing(true);
                System.out.println("Lighting up " + entity);
                it++;
                break;
            }
            it++;
        }
        if (entity == null) {
            location = null;
            return null;
        } else {
                double x;
                double z;
                double height = client.player.y - entity.y;
                if (client.player.x + height < entity.x) {
                    x = entity.x - height;
                } else if (client.player.x - height > entity.x) {
                    x = entity.x + height;
                } else {
                    x = client.player.x;
                }

                if (client.player.z + height < entity.z) {
                    z = entity.z - height * 0.5;
                } else if (client.player.z - height > entity.z) {
                    z = entity.z + height * 0.5;
                } else {
                    z = client.player.z;
                }

                location = new Vec3d(x, entity.y + 3, z);

                //location = new Vec3d(entity.x, entity.y + 3, entity.z);
                perTick = new Vec3d((location.x - client.player.x) / 10, (location.y - client.player.y) / 10, (location.z - client.player.z) / 10);

                FocusedAlly = (Ally) entity;
                return (Ally)entity;
            }
    }

    public Entity nextEnemy(Ally ally) {
        if (ally != prevFocusAlly) {
            prevFocusAlly = ally;
            enemyIt = 0;
        }
        return enemies.get(enemyIt);
    }


    public Vec3d askLoc() { //This is TacticalMode's 'tick'.
        /*
        North is -z;
        East is +x;
        West is -x;
        South is +z;
        */
        TacticalHud thud = ((MinecraftClientExtended)client).getTacticalHud();
        if (thud != null) {
            //System.out.println("yaw: " + client.player.yaw);
            if (thud.status == TacticalHud.Status.Moving && FocusedAlly != null) {
                //Vec3d v = new Vec3d(thud.prevBlockPos.getX(), thud.prevBlockPos.getY(), thud.prevBlockPos.getZ());
                //((Entity)FocusedAlly).attemptSprintingParticles();
                ((Entity)FocusedAlly).setPosition(thud.prevBlockPos.getX() + 0.5, thud.prevBlockPos.getY() + 1, thud.prevBlockPos.getZ() + 0.5);

                thud.updateTeam();
                /*
                Should have a list of blockpos, or a 'path', which is iterated through.
                */

                /*
                if (v.y > FocusedAlly.y) {
                    FocusedAlly.setPosition(v.x + 0.5, v.y + 1, v.z + 0.5);
                    thud.status = Status.Selection;
                }
                else {
                    if (FocusedAlly.isInsideWall() || FocusedAlly.isInLava()) {
                        FocusedAlly.setPosition(v.x + 0.5, v.y + 1, v.z + 0.5);
                    } else if ((FocusedAlly.getPos().x - v.x) > 0.5) { //Ally needs to go West.
                        FocusedAlly.setPosition(FocusedAlly.x - 0.2, FocusedAlly.y, FocusedAlly.z);
                        FocusedAlly.setHeadYaw(90);
                        //FocusedAlly.changeLookDirection(90.0D,90.0D); //horiz, vert
                    } else if ((FocusedAlly.getPos().x - v.x) < 0.2) { //Ally needs to go East.     //here.
                        FocusedAlly.setPosition(FocusedAlly.x + 0.2, FocusedAlly.y, FocusedAlly.z);
                        FocusedAlly.setHeadYaw(270);
                    } else if ((FocusedAlly.getPos().z - v.z) > 0.5) { //Ally needs to go North.
                        FocusedAlly.setPosition(FocusedAlly.x, FocusedAlly.y, FocusedAlly.z - 0.2);
                        FocusedAlly.setHeadYaw(180);
                    } else if ((FocusedAlly.getPos().z - v.z) < 0.2) { //Ally needs to go South.  //here
                        FocusedAlly.setPosition(FocusedAlly.x, FocusedAlly.y, FocusedAlly.z + 0.2);
                        FocusedAlly.setHeadYaw(0);
                    } else {
                        thud.status = Status.Selection;
                    }
                }
                */
            }
        }

        if (location == null) {
            return null;
        }
        ///Vec3d newLoc = new Vec3d(client.player.x + perTick.x, client.player.y + perTick.y, client.player.z + perTick.z);
        Vec3d newLoc;
        if (location.y > client.player.y) {
            newLoc = new Vec3d(client.player.x + perTick.x, client.player.y + perTick.y, client.player.z + perTick.z);
        } else {
            newLoc = new Vec3d(client.player.x + perTick.x, client.player.y, client.player.z + perTick.z);
        }
        /*
        double Width = (location.y > client.player.y ? (location.y - client.player.y): (client.player.y - location.y));
        System.out.println("Width is " + Width);
         */
        if (location.distanceTo(newLoc) < location.distanceTo(client.player.getPos())){
            //return newLoc;
            return null;
        }
        //client.player.setPositionAndAngles(location.x,location.y,location.z,0,90);
        location = null;
        return null;
    }


    public void end() {
        System.out.println("Tactical turned off!");
        for (int i = 0; i < allies.size(); i++) {
            ((Entity)allies.get(i)).setGlowing(false);
        }
        ///*
        client.player.setPositionAndAngles(original.x,original.y,original.z,0.0F,0.0F);
        ((MinecraftClientExtended) client).setTacticalMode(null);
        ((MinecraftClientExtended)client).setTacticalHud(null);
        client.getServer().getCommandManager().execute(client.getServer().getPlayerManager().getPlayer(client.player.getUuid()).getCommandSource(),"gamemode survival");
        //*/
    }

    public void newTurn() {
        scan();
        for (int i=0; i < allies.size(); i++) {
            allies.get(i).refresh();
        }
    }

}
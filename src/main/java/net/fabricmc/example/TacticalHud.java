package net.fabricmc.example;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;

import java.util.ArrayList;
import java.util.Arrays;


public class TacticalHud extends DrawableHelper {
    TacticalMode tmode;
    KeyBinding tacticalKey;
    MinecraftClient client;
    Ally focusedAlly;
    TacticalAbilities Abilities;
    public Status status;
    BlockPos prevBlockPos;
    BlockState original;
    BlockPos currblockPos;

    public TacticalHud(TacticalMode newtmode) {
        client = MinecraftClient.getInstance();
        tacticalKey = ((OptionsExtended) client.options).getTacticalKey();
        tmode = newtmode;
        status = Status.Selection;
        focusedAlly = tmode.nextAvailable();
        Abilities = new TacticalAbilities();
        prevBlockPos = null;
        original = null;
        currblockPos = null;
    }

    public void handleInputEvents() {
        if (tmode.allies.size() == 0) { //Should have unreferenced itself in its constructor,
            System.out.println("No allies?");
            //tmode.end();
            return;
        }
        if (tacticalKey.wasPressed()) {
            tmode.end();
            System.out.println("tactical being set");
            ((MinecraftClientExtended)client).setTactical(0);
        }
        if (status == Status.Selection) {
            renderGeneral();
            if (client.options.keyPlayerList.wasPressed()) {
                System.out.println("tab key was pressed while tactical is on and in TacticalHud.");
                focusedAlly = tmode.nextAvailable();
                if (focusedAlly == null) {
                    System.out.println("Player turn over.");
                    status = Status.EnemyTurn;
                }
            }
            if (client.options.keyUse.wasPressed()) {
                if (prevBlockPos != null) {
                    if (prevBlockPos.isWithinDistance(((Entity)focusedAlly).getPos(),focusedAlly.shortDistance())) {
                        status = Status.Moving;
                        focusedAlly.spendAP(1);
                    } else if (prevBlockPos.isWithinDistance(((Entity)focusedAlly).getPos(),focusedAlly.longDistance())) {
                        status = Status.Moving;
                        focusedAlly.spendAP(2);
                    }
                }
            }
              int numMoves = (focusedAlly).availableMoves().size();
            for (int i=0; i <= numMoves; i++) {
                if (client.options.keysHotbar[i].wasPressed()) {
                    Abilities.A.get((focusedAlly).availableMoves().get(i)).run(); //If attacking, changes thud's status to attacking.
                    //Waits for user to press either escape (returns, changes status back to 'selection') or press 'enter' (Accesses the focusedEnemy from thud/tmode, and sets multiple locations, waiting for tmode.location to equal nul again to set the next location.
                    //Set location and status and deduct the appropriate AP. If AP == 0, then remove this from available allies and tmode.nextAvailable().
                }
            }
        } else {
            if (status == Status.Attacking) {
                if (client.options.keyPlayerList.wasPressed()) {
                    System.out.println("tab key was pressed while tactical is on and in TacticalHud.");
                    //tmode.nextEnemy(focusedAlly);
                }
            }
            else if (status == Status.Defending) {
                //Suppression does not need this.
                //Just spends the remaining AP.
                //SHOULD ONLY BE SET IF NEED FURTHER ACTION.
            }
            else if (status == Status.Helping) {
                //Point to enemies.
                if (client.options.keyPlayerList.wasPressed()) {
                    System.out.println("tab key was pressed while tactical is on and in TacticalHud.");
                    focusedAlly = tmode.nextFocus();
                }
            }
        }
    }
    public  void renderGeneral() {
        if (focusedAlly == null) {
            return;
        }
        if (status == Status.Selection) {
            //renderMoveTiles(focusedAlly);
            ClientWorld world = MinecraftClient.getInstance().world;
            this.blit(10, 10, 0, 0, 10, 10);
            float float_1 = client.player.pitch;
            float float_2 = client.player.yaw;
            Vec3d vec3d_1 = client.player.getCameraPosVec(1.0F);
            float float_3 = MathHelper.cos(-float_2 * 0.017453292F - 3.1415927F);
            float float_4 = MathHelper.sin(-float_2 * 0.017453292F - 3.1415927F);
            float float_5 = -MathHelper.cos(-float_1 * 0.017453292F);
            float float_6 = MathHelper.sin(-float_1 * 0.017453292F);
            float float_7 = float_4 * float_5;
            float float_9 = float_3 * float_5;
            double double_1 = 5.0D;
            Vec3d vec3d_2 = vec3d_1.add((double)float_7 * 15.0D, (double)float_6 * 15.0D, (double)float_9 * 15.0D);
            HitResult result = world.rayTrace(new RayTraceContext(vec3d_1, vec3d_2, RayTraceContext.ShapeType.OUTLINE, RayTraceContext.FluidHandling.NONE, client.player));
            if (result.getType() == HitResult.Type.BLOCK) {
                currblockPos = ((BlockHitResult) result).getBlockPos();
                if (prevBlockPos != null) {
                    if (!prevBlockPos.equals(currblockPos)) {
                        world.setBlockState(prevBlockPos, original);
                        original = world.getBlockState(currblockPos);
                        prevBlockPos = currblockPos;
                    } else {
                        if  (prevBlockPos.isWithinDistance(((Entity)focusedAlly).getPos(),focusedAlly.shortDistance())) {
                            world.setBlockState(prevBlockPos, ExampleMod.CloseDistance.getDefaultState());
                        } else if (prevBlockPos.isWithinDistance(((Entity)focusedAlly).getPos(),focusedAlly.longDistance())) {
                            world.setBlockState(prevBlockPos, ExampleMod.RunDistance.getDefaultState());
                        } else {
                            world.setBlockState(prevBlockPos, Blocks.GLASS.getDefaultState());
                        }
                    }
                } else {
                    original = world.getBlockState(currblockPos);
                    prevBlockPos = currblockPos;
                }
            }





        } else if (status == Status.Attacking) {
            //System.out.println("Attacking!");
            //Point to each next hostileentity (after checking it isn't allyentity), starting from closest(?).
            //tmode.prepareAttack(currentAlly); //Scans enemies and makes array, from closest to farthest. Moves camera behind player.
            //enemyRef = tmode.nextEnemy(); //Angles camera towards next enemy in array of enemies. Also makes enemy glow. Returns reference to pointed entity.
            //System.out.println(currentAlly.name);
            //System.out.println(currentAlly.attack - enemyRef.defense);
            //System.out.println(enemyRef.remainingHealth);
            //System.out.println(enemyRef.status);
        } else if (status == Status.Defending) {
            //Point to each next allyentity. Itself too?
        } else if (status == Status.Helping) {

        }
    }
    public void updateTeam() {
        if (checkCover(prevBlockPos)) {
            System.out.println("Covered!");
            ((Minuteman)focusedAlly).setCovered();
            //System.out.println(focusedAlly + " " + focusedAlly.isCovered() + " Confirm.");
        } else {
            //System.out.println("In the Open!");
            //focusedAlly.setCovered(a);
        }
        if (focusedAlly.getAP() <= 0) {
            System.out.println(focusedAlly + " is done");
            focusedAlly = tmode.nextAvailable();
            if (focusedAlly == null) {
                System.out.println("Player turn over.");
                status = Status.EnemyTurn;
            } else {
                status = Status.Selection;
            }
        } else {
            status = Status.Selection;
        }
    }

    public boolean checkCover(BlockPos bp) {
        ArrayList<BlockPos> proximity = new ArrayList<>(Arrays.asList(bp.east(), bp.west(), bp.north(), bp.south()));
        for (BlockPos blockPos : proximity) {
            if (client.world.getBlockState(blockPos.up()).isOpaque()) {
                System.out.println("is Opaque!");
                return true;
            }
        }
        return false;
    }
    public enum Status {
        Selection,
        Attacking,
        Defending,
        Helping,
        Moving,
        EnemyTurn
    }














/*
    public void ShowDist(BlockPos blockPos,int blocksRemaining) {
        ClientWorld world = MinecraftClient.getInstance().world;              //blue,purple,yellow,whjte

        boolean upperblock = world.getBlockState(blockPos.up().up()).getBlock() != Blocks.AIR;
        boolean lowerblock = world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR;
        boolean groundblock = world.getBlockState(blockPos).getBlock() != Blocks.AIR;
        boolean basementblock = world.getBlockState(blockPos.down()).getBlock() != Blocks.AIR;

        if (upperblock) {
            return;
        }
        int blocksLeft = blocksRemaining - 1;
        if (lowerblock) {
            if (blocksLeft < 0) {
                world.setBlockState(blockPos.up(),Blocks.COMMAND_BLOCK.getDefaultState(),0);
                return;
            }
            else {
                //showdist on higher block
                ShowDist(blockPos.up().east(), blocksLeft);
                ShowDist(blockPos.up().west(), blocksLeft);
                ShowDist(blockPos.up().north(), blocksLeft);
                ShowDist(blockPos.up().south(), blocksLeft);
            }
        }
        if (groundblock) {
            if (blocksLeft < 0) {
                world.setBlockState(blockPos,Blocks.COMMAND_BLOCK.getDefaultState(),0);
                return;
            }
            else {
                //showdist on groundblock
                ShowDist(blockPos.east(), blocksLeft);
                ShowDist(blockPos.west(), blocksLeft);
                ShowDist(blockPos.north(), blocksLeft);
                ShowDist(blockPos.south(), blocksLeft);
            }
        }
        if (basementblock) {
            if (blocksLeft < 0) {
                world.setBlockState(blockPos.down(),Blocks.COMMAND_BLOCK.getDefaultState(),0);
                return;
            }
            else {
                //showdist on basementblock
                ShowDist(blockPos.down().east(), blocksLeft);
                ShowDist(blockPos.down().west(), blocksLeft);
                ShowDist(blockPos.down().north(), blocksLeft);
                ShowDist(blockPos.down().south(), blocksLeft);
            }
        }
    }
    */
}

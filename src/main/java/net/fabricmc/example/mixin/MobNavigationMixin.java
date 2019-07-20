package net.fabricmc.example.mixin;

/*

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(MobNavigation.class)
public class MobNavigationMixin extends EntityNavigation {
    private boolean avoidSunlight;

    public MobNavigationMixin(MobEntity mobEntity_1, World world_1) {
        super(mobEntity_1, world_1);
    }


    public PathNodeNavigator createPathNodeNavigator(int int_1) {
        this.nodeMaker = new LandPathNodeMaker();
        this.nodeMaker.setCanEnterOpenDoors(true);
        return new PathNodeNavigator(this.nodeMaker, int_1);
    }

    public boolean isAtValidPosition() {
        return this.entity.onGround || this.isInLiquid() || this.entity.hasVehicle();
    }

    public Vec3d getPos() {
        return new Vec3d(this.entity.x, (double)this.method_6362(), this.entity.z);
    }












    public Path findPathTo(BlockPos blockPos_1) {
            int dx = blockPos_1.getX() - (int)this.entity.x;
            int dy = blockPos_1.getY() - (int)this.entity.y;
            int dz = blockPos_1.getZ() - (int)this.entity.z;
            ArrayList<PathNode> nodes = new ArrayList<>();
            if (dx > 0) {
                for (int i = 0; i < dx; i++) {
                    nodes.add(new PathNode((int) this.entity.x + i, (int) this.entity.y, (int) this.entity.z));
                }
            } else {
                dx = dx * -1;
                for (int i = 0; i < dx; i++) {
                    nodes.add(new PathNode((int) this.entity.x - i, (int) this.entity.y, (int) this.entity.z));
                }
            }
            if (dz > 0) {
                for (int i = 0; i < dz; i++) {
                    nodes.add(new PathNode((int) this.entity.x, (int) this.entity.y, (int) this.entity.z + i));
                }
            } else {
                dz = dz * -1;
                for (int i = 0; i < dz; i++) {
                    nodes.add(new PathNode((int) this.entity.x, (int) this.entity.y, (int) this.entity.z - i));
                }
            }
            Path a = new Path(nodes);
            if (entity instanceof PillagerEntity) {
                System.out.println(a);
            }
            return new Path(nodes);
    }




    public Path findPathTo(Entity entity_1) {
        return this.findPathTo(new BlockPos(entity_1));
    }

    private int method_6362() {
        if (this.entity.isInsideWater() && this.canSwim()) {
            int int_1 = MathHelper.floor(this.entity.getBoundingBox().minY);
            Block block_1 = this.world.getBlockState(new BlockPos(this.entity.x, (double)int_1, this.entity.z)).getBlock();
            int int_2 = 0;

            do {
                if (block_1 != Blocks.WATER) {
                    return int_1;
                }

                ++int_1;
                block_1 = this.world.getBlockState(new BlockPos(this.entity.x, (double)int_1, this.entity.z)).getBlock();
                ++int_2;
            } while(int_2 <= 16);

            return MathHelper.floor(this.entity.getBoundingBox().minY);
        } else {
            return MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D);
        }
    }

    public void method_6359() {
        super.method_6359();
        if (this.avoidSunlight) {
            if (this.world.isSkyVisible(new BlockPos(this.entity.x, this.entity.getBoundingBox().minY + 0.5D, this.entity.z))) {
                return;
            }

            for(int int_1 = 0; int_1 < this.currentPath.getLength(); ++int_1) {
                PathNode pathNode_1 = this.currentPath.getNode(int_1);
                if (this.world.isSkyVisible(new BlockPos(pathNode_1.x, pathNode_1.y, pathNode_1.z))) {
                    this.currentPath.setLength(int_1);
                    return;
                }
            }
        }

    }

    public boolean canPathDirectlyThrough(Vec3d vec3d_1, Vec3d vec3d_2, int int_1, int int_2, int int_3) {
        int int_4 = MathHelper.floor(vec3d_1.x);
        int int_5 = MathHelper.floor(vec3d_1.z);
        double double_1 = vec3d_2.x - vec3d_1.x;
        double double_2 = vec3d_2.z - vec3d_1.z;
        double double_3 = double_1 * double_1 + double_2 * double_2;
        if (double_3 < 1.0E-8D) {
            return false;
        } else {
            double double_4 = 1.0D / Math.sqrt(double_3);
            double_1 *= double_4;
            double_2 *= double_4;
            int_1 += 2;
            int_3 += 2;
            if (!this.method_6364(int_4, MathHelper.floor(vec3d_1.y), int_5, int_1, int_2, int_3, vec3d_1, double_1, double_2)) {
                return false;
            } else {
                int_1 -= 2;
                int_3 -= 2;
                double double_5 = 1.0D / Math.abs(double_1);
                double double_6 = 1.0D / Math.abs(double_2);
                double double_7 = (double)int_4 - vec3d_1.x;
                double double_8 = (double)int_5 - vec3d_1.z;
                if (double_1 >= 0.0D) {
                    ++double_7;
                }

                if (double_2 >= 0.0D) {
                    ++double_8;
                }

                double_7 /= double_1;
                double_8 /= double_2;
                int int_6 = double_1 < 0.0D ? -1 : 1;
                int int_7 = double_2 < 0.0D ? -1 : 1;
                int int_8 = MathHelper.floor(vec3d_2.x);
                int int_9 = MathHelper.floor(vec3d_2.z);
                int int_10 = int_8 - int_4;
                int int_11 = int_9 - int_5;

                do {
                    if (int_10 * int_6 <= 0 && int_11 * int_7 <= 0) {
                        return true;
                    }

                    if (double_7 < double_8) {
                        double_7 += double_5;
                        int_4 += int_6;
                        int_10 = int_8 - int_4;
                    } else {
                        double_8 += double_6;
                        int_5 += int_7;
                        int_11 = int_9 - int_5;
                    }
                } while(this.method_6364(int_4, MathHelper.floor(vec3d_1.y), int_5, int_1, int_2, int_3, vec3d_1, double_1, double_2));

                return false;
            }
        }
    }

    private boolean method_6364(int int_1, int int_2, int int_3, int int_4, int int_5, int int_6, Vec3d vec3d_1, double double_1, double double_2) {
        int int_7 = int_1 - int_4 / 2;
        int int_8 = int_3 - int_6 / 2;
        if (!this.method_6367(int_7, int_2, int_8, int_4, int_5, int_6, vec3d_1, double_1, double_2)) {
            return false;
        } else {
            for(int int_9 = int_7; int_9 < int_7 + int_4; ++int_9) {
                for(int int_10 = int_8; int_10 < int_8 + int_6; ++int_10) {
                    double double_3 = (double)int_9 + 0.5D - vec3d_1.x;
                    double double_4 = (double)int_10 + 0.5D - vec3d_1.z;
                    if (double_3 * double_1 + double_4 * double_2 >= 0.0D) {
                        PathNodeType pathNodeType_1 = this.nodeMaker.getPathNodeType(this.world, int_9, int_2 - 1, int_10, this.entity, int_4, int_5, int_6, true, true);
                        if (pathNodeType_1 == PathNodeType.WATER) {
                            return false;
                        }

                        if (pathNodeType_1 == PathNodeType.LAVA) {
                            return false;
                        }

                        if (pathNodeType_1 == PathNodeType.OPEN) {
                            return false;
                        }

                        pathNodeType_1 = this.nodeMaker.getPathNodeType(this.world, int_9, int_2, int_10, this.entity, int_4, int_5, int_6, true, true);
                        float float_1 = this.entity.getPathNodeTypeWeight(pathNodeType_1);
                        if (float_1 < 0.0F || float_1 >= 8.0F) {
                            return false;
                        }

                        if (pathNodeType_1 == PathNodeType.DAMAGE_FIRE || pathNodeType_1 == PathNodeType.DANGER_FIRE || pathNodeType_1 == PathNodeType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean method_6367(int int_1, int int_2, int int_3, int int_4, int int_5, int int_6, Vec3d vec3d_1, double double_1, double double_2) {
        Iterator var12 = BlockPos.iterate(new BlockPos(int_1, int_2, int_3), new BlockPos(int_1 + int_4 - 1, int_2 + int_5 - 1, int_3 + int_6 - 1)).iterator();

        BlockPos blockPos_1;
        double double_3;
        double double_4;
        do {
            if (!var12.hasNext()) {
                return true;
            }

            blockPos_1 = (BlockPos)var12.next();
            double_3 = (double)blockPos_1.getX() + 0.5D - vec3d_1.x;
            double_4 = (double)blockPos_1.getZ() + 0.5D - vec3d_1.z;
        } while(double_3 * double_1 + double_4 * double_2 < 0.0D || this.world.getBlockState(blockPos_1).canPlaceAtSide(this.world, blockPos_1, BlockPlacementEnvironment.LAND));

        return false;
    }

    public void setCanPathThroughDoors(boolean boolean_1) {
        this.nodeMaker.setCanPathThroughDoors(boolean_1);
    }

    public boolean canEnterOpenDoors() {
        return this.nodeMaker.canEnterOpenDoors();
    }

    public void setAvoidSunlight(boolean boolean_1) {
        this.avoidSunlight = boolean_1;
    }
}
*/
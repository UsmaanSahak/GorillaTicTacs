package net.fabricmc.example.mixin;

import net.fabricmc.example.Ally;
import net.fabricmc.example.Minuteman;
import net.fabricmc.example.ModelContainer;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EvilVillagerEntityModel;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.util.AbsoluteHand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EvilVillagerEntityModel.class)
public class EvilVillagerEntityModelMixin<T extends IllagerEntity> extends EntityModel<T> {

    @Shadow @Final
    protected  Cuboid field_3422;
    @Shadow @Final
    private  Cuboid field_3419;
    @Shadow @Final
    protected  Cuboid field_3425;
    @Shadow @Final
    protected  Cuboid field_3423;
    @Shadow @Final
    protected  Cuboid field_3420;
    @Shadow @Final
    protected  Cuboid field_3418;
    @Shadow @Final
    private  Cuboid field_3421;
    @Shadow @Final
    protected  Cuboid field_3426;
    @Shadow @Final
    protected  Cuboid field_3417;
    @Shadow
    private float field_3424;

    public void method_17094(T illagerEntity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6) {
        this.field_3422.yaw = float_4 * 0.017453292F;
        this.field_3422.pitch = float_5 * 0.017453292F;
        this.field_3423.rotationPointY = 3.0F;
        this.field_3423.rotationPointZ = -1.0F;
        this.field_3423.pitch = -0.75F;
        if (this.isRiding) {
            this.field_3426.pitch = -0.62831855F;
            this.field_3426.yaw = 0.0F;
            this.field_3426.roll = 0.0F;
            this.field_3417.pitch = -0.62831855F;
            this.field_3417.yaw = 0.0F;
            this.field_3417.roll = 0.0F;
            this.field_3420.pitch = -1.4137167F;
            this.field_3420.yaw = 0.31415927F;
            this.field_3420.roll = 0.07853982F;
            this.field_3418.pitch = -1.4137167F;
            this.field_3418.yaw = -0.31415927F;
            this.field_3418.roll = -0.07853982F;
        } else {
            this.field_3426.pitch = MathHelper.cos(float_1 * 0.6662F + 3.1415927F) * 2.0F * float_2 * 0.5F;
            this.field_3426.yaw = 0.0F;
            this.field_3426.roll = 0.0F;
            this.field_3417.pitch = MathHelper.cos(float_1 * 0.6662F) * 2.0F * float_2 * 0.5F;
            this.field_3417.yaw = 0.0F;
            this.field_3417.roll = 0.0F;
            this.field_3420.pitch = MathHelper.cos(float_1 * 0.6662F) * 1.4F * float_2 * 0.5F;
            this.field_3420.yaw = 0.0F;
            this.field_3420.roll = 0.0F;
            this.field_3418.pitch = MathHelper.cos(float_1 * 0.6662F + 3.1415927F) * 1.4F * float_2 * 0.5F;
            this.field_3418.yaw = 0.0F;
            this.field_3418.roll = 0.0F;
        }

        if (illagerEntity_1 instanceof Ally) {
            Ally.State AllyState = ((Ally) illagerEntity_1).getState2();
            float float_9;
            if (AllyState == Ally.State.ATTACKING) {
///*
                ModelContainer modelContainer = ((Ally) illagerEntity_1).askAnimation();
                this.field_3426.yaw = modelContainer.rightArm.yaw;
                this.field_3426.pitch = modelContainer.rightArm.pitch;
                this.field_3426.rotationPointX = modelContainer.rightArm.rotationPointX;
                this.field_3426.rotationPointZ = modelContainer.rightArm.rotationPointZ;
                this.field_3426.x = modelContainer.rightArm.x;
                this.field_3426.z = modelContainer.rightArm.z;
                this.field_3417.yaw = modelContainer.leftArm.yaw;
                this.field_3417.pitch = modelContainer.leftArm.pitch;
                this.field_3417.rotationPointX = modelContainer.leftArm.rotationPointX;
                this.field_3417.rotationPointZ = modelContainer.leftArm.rotationPointZ;
                this.field_3426.x = modelContainer.leftArm.x;
                this.field_3426.z = modelContainer.leftArm.z;
                this.field_3422.yaw = modelContainer.head.yaw;
                this.field_3422.pitch = modelContainer.head.pitch;
                this.field_3422.rotationPointX = modelContainer.head.rotationPointX;
                this.field_3422.rotationPointZ = modelContainer.head.rotationPointZ;
                this.field_3426.x = modelContainer.head.x;
                this.field_3426.z = modelContainer.head.z;
                this.field_3425.yaw = modelContainer.torso.yaw;
                this.field_3425.pitch = modelContainer.torso.pitch;
                this.field_3425.rotationPointX = modelContainer.torso.rotationPointX;
                this.field_3425.rotationPointZ = modelContainer.torso.rotationPointZ;
                this.field_3426.x = modelContainer.torso.x;
                this.field_3426.z = modelContainer.torso.z;
                this.field_3420.yaw = modelContainer.rightLeg.yaw;
                this.field_3420.pitch = modelContainer.rightLeg.pitch;
                this.field_3420.rotationPointX = modelContainer.rightLeg.rotationPointX;
                this.field_3420.rotationPointZ = modelContainer.rightLeg.rotationPointZ;
                this.field_3420.x = modelContainer.rightLeg.x;
                this.field_3420.z = modelContainer.rightLeg.z;
                this.field_3418.yaw = modelContainer.leftLeg.yaw;
                this.field_3418.pitch = modelContainer.leftLeg.pitch;
                this.field_3418.rotationPointX = modelContainer.leftLeg.rotationPointX;
                this.field_3418.rotationPointZ = modelContainer.leftLeg.rotationPointZ;
                this.field_3418.x = modelContainer.leftLeg.x;
                this.field_3418.z = modelContainer.leftLeg.z;

            } else if (AllyState == Ally.State.SPELLCASTING) {
                this.field_3426.rotationPointZ = 0.0F;
                this.field_3426.rotationPointX = -5.0F;
                this.field_3417.rotationPointZ = 0.0F;
                this.field_3417.rotationPointX = 5.0F;
                this.field_3426.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.25F;
                this.field_3417.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.25F;
                this.field_3426.roll = 2.3561945F;
                this.field_3417.roll = -2.3561945F;
                this.field_3426.yaw = 0.0F;
                this.field_3417.yaw = 0.0F;
            } else if (AllyState == Ally.State.BOW_AND_ARROW) {
                this.field_3426.yaw = -0.1F + this.field_3422.yaw;
                this.field_3426.pitch = -1.5707964F + this.field_3422.pitch;
                this.field_3417.pitch = -0.9424779F + this.field_3422.pitch;
                this.field_3417.yaw = this.field_3422.yaw - 0.4F;
                this.field_3417.roll = 1.5707964F;
            } else if (AllyState == Ally.State.CROSSBOW_HOLD) {
                this.field_3426.yaw = -0.3F + this.field_3422.yaw;
                this.field_3417.yaw = 0.6F + this.field_3422.yaw;
                this.field_3426.pitch = -1.5707964F + this.field_3422.pitch + 0.1F;
                this.field_3417.pitch = -1.5F + this.field_3422.pitch;
            } else if (AllyState == Ally.State.COVERED) {
            /*
                this.field_3418.pitch = 0.0F; //left leg
                this.field_3418.z = 0.1F;
                this.field_3420.pitch = -0.5F; //right leg
                this.field_3420.z = 0.1F;
                //this.field_3422.pitch = 0.5F; //head
                this.field_3422.y = 0.4F;
                this.field_3422.z = -0.7F;
                this.field_3425.pitch = 0.8F; //torso
                this.field_3425.z = -0.5F;
                this.field_3425.y = 0.3F;
                this.field_3417.z = -0.5F; //left arm
                this.field_3417.y = 0.3F;
                this.field_3417.yaw = 0.6F + this.field_3422.yaw;
                this.field_3417.pitch = -0.5F + this.field_3422.pitch;
                this.field_3426.z = -0.5F; //right arm
                this.field_3426.y = 0.3F;
                this.field_3426.yaw = -0.8F + this.field_3422.yaw;
                this.field_3426.pitch = -0.5707964F + this.field_3422.pitch + 0.1F;
             //*/
                this.field_3426.yaw = -0.3F + this.field_3422.yaw;
                this.field_3417.yaw = 1.0F + this.field_3422.yaw;
                this.field_3426.rotationPointX = -5.0F;
                this.field_3417.rotationPointX = 5.0F;
                this.field_3426.pitch = -0.5707964F + this.field_3422.pitch + -0.3F;
                this.field_3417.pitch = -1.0F + this.field_3422.pitch;
                this.field_3420.rotationPointX = -2.0F;
                this.field_3418.rotationPointX = 2.0F;

            } else if (AllyState == Ally.State.CROSSBOW_CHARGE) {
                this.field_3426.yaw = -0.8F; //right arm
                this.field_3426.pitch = -0.97079635F;
                float_9 = MathHelper.clamp(this.field_3424, 0.0F, 25.0F);
                this.field_3417.yaw = MathHelper.lerp(float_9 / 25.0F, 0.4F, 0.85F);
                this.field_3417.pitch = MathHelper.lerp(float_9 / 25.0F, this.field_3417.pitch, -1.5707964F);
            } else if (AllyState == Ally.State.CELEBRATING) {
                this.field_3426.rotationPointZ = 0.0F;
                this.field_3426.rotationPointX = -5.0F;
                this.field_3426.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.05F;
                this.field_3426.roll = 2.670354F;
                this.field_3426.yaw = 0.0F;
                this.field_3417.rotationPointZ = 0.0F;
                this.field_3417.rotationPointX = 5.0F;
                this.field_3417.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.05F;
                this.field_3417.roll = -2.3561945F;
                this.field_3417.yaw = 0.0F;
            }
        } else {
            IllagerEntity.State illagerEntity$State_1 = illagerEntity_1.getState();
            float float_9;
            if (illagerEntity$State_1 == IllagerEntity.State.ATTACKING) {
                float_9 = MathHelper.sin(this.handSwingProgress * 3.1415927F);
                float float_8 = MathHelper.sin((1.0F - (1.0F - this.handSwingProgress) * (1.0F - this.handSwingProgress)) * 3.1415927F);
                this.field_3426.roll = 0.0F;
                this.field_3417.roll = 0.0F;
                this.field_3426.yaw = 0.15707964F;
                this.field_3417.yaw = -0.15707964F;
                Cuboid var10000;
                if (illagerEntity_1.getMainHand() == AbsoluteHand.RIGHT) {
                    this.field_3426.pitch = -1.8849558F + MathHelper.cos(float_3 * 0.09F) * 0.15F;
                    this.field_3417.pitch = -0.0F + MathHelper.cos(float_3 * 0.19F) * 0.5F;
                    var10000 = this.field_3426;
                    var10000.pitch += float_9 * 2.2F - float_8 * 0.4F;
                    var10000 = this.field_3417;
                    var10000.pitch += float_9 * 1.2F - float_8 * 0.4F;
                } else {
                    this.field_3426.pitch = -0.0F + MathHelper.cos(float_3 * 0.19F) * 0.5F;
                    this.field_3417.pitch = -1.8849558F + MathHelper.cos(float_3 * 0.09F) * 0.15F;
                    var10000 = this.field_3426;
                    var10000.pitch += float_9 * 1.2F - float_8 * 0.4F;
                    var10000 = this.field_3417;
                    var10000.pitch += float_9 * 2.2F - float_8 * 0.4F;
                }

                var10000 = this.field_3426;
                var10000.roll += MathHelper.cos(float_3 * 0.09F) * 0.05F + 0.05F;
                var10000 = this.field_3417;
                var10000.roll -= MathHelper.cos(float_3 * 0.09F) * 0.05F + 0.05F;
                var10000 = this.field_3426;
                var10000.pitch += MathHelper.sin(float_3 * 0.067F) * 0.05F;
                var10000 = this.field_3417;
                var10000.pitch -= MathHelper.sin(float_3 * 0.067F) * 0.05F;
            } else if (illagerEntity$State_1 == IllagerEntity.State.SPELLCASTING) {
                this.field_3426.rotationPointZ = 0.0F;
                this.field_3426.rotationPointX = -5.0F;
                this.field_3417.rotationPointZ = 0.0F;
                this.field_3417.rotationPointX = 5.0F;
                this.field_3426.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.25F;
                this.field_3417.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.25F;
                this.field_3426.roll = 2.3561945F;
                this.field_3417.roll = -2.3561945F;
                this.field_3426.yaw = 0.0F;
                this.field_3417.yaw = 0.0F;
            } else if (illagerEntity$State_1 == IllagerEntity.State.BOW_AND_ARROW) {
                this.field_3426.yaw = -0.1F + this.field_3422.yaw;
                this.field_3426.pitch = -1.5707964F + this.field_3422.pitch;
                this.field_3417.pitch = -0.9424779F + this.field_3422.pitch;
                this.field_3417.yaw = this.field_3422.yaw - 0.4F;
                this.field_3417.roll = 1.5707964F;
            } else if (illagerEntity$State_1 == IllagerEntity.State.CROSSBOW_HOLD) {
                this.field_3426.yaw = -0.3F + this.field_3422.yaw;
                this.field_3417.yaw = 0.6F + this.field_3422.yaw;
                this.field_3426.pitch = -1.5707964F + this.field_3422.pitch + 0.1F;
                this.field_3417.pitch = -1.5F + this.field_3422.pitch;
            } else if (illagerEntity$State_1 == IllagerEntity.State.CROSSBOW_CHARGE) {
                if (illagerEntity_1 instanceof Ally) {
                    this.field_3426.yaw = -0.8F;
                    this.field_3426.pitch = -0.97079635F;
                    this.field_3417.pitch = -0.97079635F;
                    float_9 = MathHelper.clamp(this.field_3424, 0.0F, 25.0F);
                    this.field_3417.yaw = MathHelper.lerp(float_9 / 25.0F, 0.4F, 0.85F);
                    this.field_3417.pitch = MathHelper.lerp(float_9 / 25.0F, this.field_3417.pitch, -1.5707964F);
                } else  {
                    this.field_3426.yaw = -0.8F;
                    this.field_3426.pitch = -0.97079635F;
                    this.field_3417.pitch = -0.97079635F;
                    float_9 = MathHelper.clamp(this.field_3424, 0.0F, 25.0F);
                    this.field_3417.yaw = MathHelper.lerp(float_9 / 25.0F, 0.4F, 0.85F);
                    this.field_3417.pitch = MathHelper.lerp(float_9 / 25.0F, this.field_3417.pitch, -1.5707964F);
                }

            } else if (illagerEntity$State_1 == IllagerEntity.State.CELEBRATING) {
                this.field_3426.rotationPointZ = 0.0F;
                this.field_3426.rotationPointX = -5.0F;
                this.field_3426.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.05F;
                this.field_3426.roll = 2.670354F;
                this.field_3426.yaw = 0.0F;
                this.field_3417.rotationPointZ = 0.0F;
                this.field_3417.rotationPointX = 5.0F;
                this.field_3417.pitch = MathHelper.cos(float_3 * 0.6662F) * 0.05F;
                this.field_3417.roll = -2.3561945F;
                this.field_3417.yaw = 0.0F;
            }

        }

        }
}

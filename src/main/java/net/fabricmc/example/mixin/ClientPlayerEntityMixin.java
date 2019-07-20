package net.fabricmc.example.mixin;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.fabricmc.example.MinecraftClientExtended;
import net.fabricmc.example.TacticalMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipe.book.ClientRecipeBook;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.JumpingMount;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.packet.ClientCommandC2SPacket;
import net.minecraft.stat.StatHandler;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow @Final
    public  ClientPlayNetworkHandler networkHandler;
    @Shadow @Final
    private  StatHandler stats;
    @Shadow @Final
    private  ClientRecipeBook recipeBook;
    @Shadow
    private final List<ClientPlayerTickable> tickables = Lists.newArrayList();
    @Shadow
    private int clientPermissionLevel = 0;
    @Shadow
    private double lastX;
    @Shadow
    private double lastBaseY;
    @Shadow
    private double lastZ;
    @Shadow
    private float lastYaw;
    @Shadow
    private float lastPitch;

    @Shadow
    private boolean lastOnGround;
    @Shadow
    private boolean lastIsHoldingSneakKey;
    @Shadow
    private boolean lastSprinting;
    @Shadow
    private int field_3923;
    @Shadow
    private boolean field_3918;
    @Shadow
    private String serverBrand;
    @Shadow
    public Input input;
    @Shadow @Final
    protected  MinecraftClient client;
    @Shadow
    protected int field_3935;
    @Shadow
    public int field_3921;
    @Shadow
    public float renderYaw;
    @Shadow
    public float renderPitch;
    @Shadow
    public float lastRenderYaw;
    @Shadow
    public float lastRenderPitch;
    @Shadow
    private int field_3938;
    @Shadow
    private float field_3922;
    @Shadow
    public float nextNauseaStrength;
    @Shadow
    public float lastNauseaStrength;
    @Shadow
    private boolean field_3915;
    @Shadow
    private Hand activeHand;
    @Shadow
    private boolean riding;
    @Shadow
    private boolean lastAutoJump = true;
    @Shadow
    private int field_3934;
    @Shadow
    private boolean field_3939;
    @Shadow
    private int field_3917;
    @Shadow
    private void updateNausea() {}
    @Shadow
    private boolean method_20623() { return true; }
    @Shadow
    public boolean isInSneakingPose() { return true; }

    @Shadow
    protected void pushOutOfBlocks(double double_1, double double_2, double double_3) {}

    @Shadow
    protected boolean isCamera() { return true; }
    @Shadow
    public boolean hasJumpingMount() { return true; }
    @Shadow
    public float method_3151() {return 0.0F;}
    @Shadow
    protected void startRidingJump() {}

    public ClientPlayerEntityMixin(ClientWorld clientWorld_1, GameProfile gameProfile_1) {
        super(clientWorld_1, gameProfile_1);
    }


    Vec3d travel;
        @Inject(at = @At("RETURN"), method="<init>*")
    private void constructor(MinecraftClient minecraftClient_1, ClientWorld clientWorld_1, ClientPlayNetworkHandler clientPlayNetworkHandler_1, StatHandler statHandler_1, ClientRecipeBook clientRecipeBook_1, CallbackInfo info) {
        travel = null;
    }


    public void tickMovement() {
            //HERE
        if (((MinecraftClientExtended)client).getTacticalMode() != null) {
            travel = ((MinecraftClientExtended)client).getTacticalMode().askLoc();
            if (travel != null) {
                client.player.setPosition(travel.x, travel.y, travel.z);
            }
        }


        ++this.field_3921;
        if (this.field_3935 > 0) {
            --this.field_3935;
        }

        this.updateNausea();
        boolean boolean_1 = this.input.jumping;
        boolean boolean_2 = this.input.sneaking;
        boolean boolean_3 = this.method_20623();
        boolean boolean_4 = this.isInSneakingPose() || this.shouldLeaveSwimmingPose();
        this.input.tick(boolean_4, this.isSpectator());
        this.client.getTutorialManager().onMovement(this.input);
        Input var10000;
        if (this.isUsingItem() && !this.hasVehicle()) {
            var10000 = this.input;
            var10000.movementSideways *= 0.2F;
            var10000 = this.input;
            var10000.movementForward *= 0.2F;
            this.field_3935 = 0;
        }

        boolean boolean_5 = false;
        if (this.field_3934 > 0) {
            --this.field_3934;
            boolean_5 = true;
            this.input.jumping = true;
        }

        if (!this.noClip) {
            BoundingBox boundingBox_1 = this.getBoundingBox();
            this.pushOutOfBlocks(this.x - (double)this.getWidth() * 0.35D, boundingBox_1.minY + 0.5D, this.z + (double)this.getWidth() * 0.35D);
            this.pushOutOfBlocks(this.x - (double)this.getWidth() * 0.35D, boundingBox_1.minY + 0.5D, this.z - (double)this.getWidth() * 0.35D);
            this.pushOutOfBlocks(this.x + (double)this.getWidth() * 0.35D, boundingBox_1.minY + 0.5D, this.z - (double)this.getWidth() * 0.35D);
            this.pushOutOfBlocks(this.x + (double)this.getWidth() * 0.35D, boundingBox_1.minY + 0.5D, this.z + (double)this.getWidth() * 0.35D);
        }

        boolean boolean_6 = (float)this.getHungerManager().getFoodLevel() > 6.0F || this.abilities.allowFlying;
        if ((this.onGround || this.isInWater()) && !boolean_2 && !boolean_3 && this.method_20623() && !this.isSprinting() && boolean_6 && !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS)) {
            if (this.field_3935 <= 0 && !this.client.options.keySprint.isPressed()) {
                this.field_3935 = 7;
            } else {
                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && (!this.isInsideWater() || this.isInWater()) && this.method_20623() && boolean_6 && !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && this.client.options.keySprint.isPressed()) {
            this.setSprinting(true);
        }

        if (this.isSprinting()) {
            boolean boolean_7 = !this.input.method_20622() || !boolean_6;
            boolean boolean_8 = boolean_7 || this.horizontalCollision || this.isInsideWater() && !this.isInWater();
            if (this.isSwimming()) {
                if (!this.onGround && !this.input.sneaking && boolean_7 || !this.isInsideWater()) {
                    this.setSprinting(false);
                }
            } else if (boolean_8) {
                this.setSprinting(false);
            }
        }

        if (this.abilities.allowFlying) {
            if (this.client.interactionManager.isFlyingLocked()) {
                if (!this.abilities.flying) {
                    this.abilities.flying = true;
                    this.sendAbilitiesUpdate();
                }
            } else if (!boolean_1 && this.input.jumping && !boolean_5) {
                if (this.field_7489 == 0) {
                    this.field_7489 = 7;
                } else if (!this.isSwimming()) {
                    this.abilities.flying = !this.abilities.flying;
                    this.sendAbilitiesUpdate();
                    this.field_7489 = 0;
                }
            }
        }

        if (this.input.jumping && !boolean_1 && !this.onGround && this.getVelocity().y < 0.0D && !this.isFallFlying() && !this.abilities.flying) {
            ItemStack itemStack_1 = this.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack_1.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemStack_1)) {
                this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
        }

        this.field_3939 = this.isFallFlying();
        if (this.isInsideWater() && this.input.sneaking) {
            this.method_6093();
        }

        int int_2;
        if (this.isInFluid(FluidTags.WATER)) {
            int_2 = this.isSpectator() ? 10 : 1;
            this.field_3917 = MathHelper.clamp(this.field_3917 + int_2, 0, 600);
        } else if (this.field_3917 > 0) {
            this.isInFluid(FluidTags.WATER);
            this.field_3917 = MathHelper.clamp(this.field_3917 - 10, 0, 600);
        }

        if (this.abilities.flying && this.isCamera()) {
            int_2 = 0;
            if (this.input.sneaking) {
                var10000 = this.input;
                var10000.movementSideways = (float)((double)var10000.movementSideways / 0.3D);
                var10000 = this.input;
                var10000.movementForward = (float)((double)var10000.movementForward / 0.3D);
                --int_2;
            }

            if (this.input.jumping) {
                ++int_2;
            }

            if (int_2 != 0) {
                this.setVelocity(this.getVelocity().add(0.0D, (double)((float)int_2 * this.abilities.getFlySpeed() * 3.0F), 0.0D));
            }
        }

        if (this.hasJumpingMount()) {
            JumpingMount jumpingMount_1 = (JumpingMount)this.getVehicle();
            if (this.field_3938 < 0) {
                ++this.field_3938;
                if (this.field_3938 == 0) {
                    this.field_3922 = 0.0F;
                }
            }

            if (boolean_1 && !this.input.jumping) {
                this.field_3938 = -10;
                jumpingMount_1.setJumpStrength(MathHelper.floor(this.method_3151() * 100.0F));
                this.startRidingJump();
            } else if (!boolean_1 && this.input.jumping) {
                this.field_3938 = 0;
                this.field_3922 = 0.0F;
            } else if (boolean_1) {
                ++this.field_3938;
                if (this.field_3938 < 10) {
                    this.field_3922 = (float)this.field_3938 * 0.1F;
                } else {
                    this.field_3922 = 0.8F + 2.0F / (float)(this.field_3938 - 9) * 0.1F;
                }
            }
        } else {
            this.field_3922 = 0.0F;
        }

        super.tickMovement();
        if (this.onGround && this.abilities.flying && !this.client.interactionManager.isFlyingLocked()) {
            this.abilities.flying = false;
            this.sendAbilitiesUpdate();
        }

    }



}

package net.fabricmc.example;
import java.util.EnumSet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CrossbowDefendGoal<T extends HostileEntity & RangedAttackMob & CrossbowUser> extends Goal {
    private final T actor;
    private CrossbowDefendGoal.Stage stage;
    private final double speed;
    private final float squaredRange;
    private int field_6592;
    private int field_16529;

    public CrossbowDefendGoal(T hostileEntity_1, double double_1, float float_1) {
        this.stage = CrossbowDefendGoal.Stage.UNCHARGED;
        this.actor = hostileEntity_1;
        this.speed = double_1;
        this.squaredRange = float_1 * float_1;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        return this.method_19996() && this.isEntityHoldingCrossbow() && ((MinecraftClientExtended) MinecraftClient.getInstance()).getTacticalHud() == null;
    }

    private boolean isEntityHoldingCrossbow() {
        return this.actor.isHolding(Items.CROSSBOW);
    }

    public boolean shouldContinue() {
        return this.method_19996() && (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isEntityHoldingCrossbow();
    }

    private boolean method_19996() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.actor.setTarget((LivingEntity)null);
        this.field_6592 = 0;
        if (this.actor.isUsingItem()) {
            this.actor.clearActiveItem();
            ((CrossbowUser)this.actor).setCharging(false);
            CrossbowItem.setCharged(this.actor.getActiveItem(), false);
        }

    }

    public void tick() {
        LivingEntity livingEntity_1 = this.actor.getTarget();
        if (livingEntity_1 != null) {
            boolean boolean_1 = this.actor.getVisibilityCache().canSee(livingEntity_1);
            boolean boolean_2 = this.field_6592 > 0;
            if (boolean_1 != boolean_2) {
                this.field_6592 = 0;
            }

            if (boolean_1) {
                ++this.field_6592;
            } else {
                --this.field_6592;
            }

            double double_1 = this.actor.squaredDistanceTo(livingEntity_1);
            boolean boolean_3 = (double_1 > (double)this.squaredRange || this.field_6592 < 5) && this.field_16529 == 0;
            if (boolean_3) {
                this.actor.getNavigation().startMovingTo(livingEntity_1, this.isUncharged() ? this.speed : this.speed * 0.5D);
            } else {
                this.actor.getNavigation().stop();
            }

            this.actor.getLookControl().lookAt(livingEntity_1, 30.0F, 30.0F);
            if (this.stage == CrossbowDefendGoal.Stage.UNCHARGED) {
                if (!boolean_3) {
                    this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
                    this.stage = CrossbowDefendGoal.Stage.CHARGING;
                    ((CrossbowUser)this.actor).setCharging(true);
                }
            } else if (this.stage == CrossbowDefendGoal.Stage.CHARGING) {
                if (!this.actor.isUsingItem()) {
                    this.stage = CrossbowDefendGoal.Stage.UNCHARGED;
                }

                int int_1 = this.actor.getItemUseTime();
                ItemStack itemStack_1 = this.actor.getActiveItem();
                if (int_1 >= CrossbowItem.getPullTime(itemStack_1)) {
                    this.actor.stopUsingItem();
                    this.stage = CrossbowDefendGoal.Stage.CHARGED;
                    this.field_16529 = 20 + this.actor.getRand().nextInt(20);
                    ((CrossbowUser)this.actor).setCharging(false);
                }
            } else if (this.stage == CrossbowDefendGoal.Stage.CHARGED) {
                --this.field_16529;
                if (this.field_16529 == 0) {
                    this.stage = CrossbowDefendGoal.Stage.READY_TO_ATTACK;
                }
            } else if (this.stage == CrossbowDefendGoal.Stage.READY_TO_ATTACK && boolean_1) {
                ((RangedAttackMob)this.actor).attack(livingEntity_1, 1.0F);
                ItemStack itemStack_2 = this.actor.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
                CrossbowItem.setCharged(itemStack_2, false);
                this.stage = CrossbowDefendGoal.Stage.UNCHARGED;
            }

        }
    }

    private boolean isUncharged() {
        return this.stage == CrossbowDefendGoal.Stage.UNCHARGED;
    }

    static enum Stage {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;

        private Stage() {
        }
    }
}

package net.fabricmc.example.mixin.Goals;

import net.fabricmc.example.MinecraftClientExtended;
import net.fabricmc.example.TacticalHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TrackTargetGoal.class)
public class TrackTargetGoalMixin {
    @Shadow @Final
    protected  MobEntity mob;
    @Shadow @Final
    protected  boolean checkVisibility;
    @Shadow @Final
    private  boolean checkCanNavigate;
    @Shadow
    private int canNavigateFlag;
    @Shadow
    private int checkCanNavigateCooldown;
    @Shadow
    private int timeWithoutVisibility;
    @Shadow
    protected LivingEntity target;
    @Shadow
    protected int maxTimeWithoutVisibility;
    @Shadow
    protected double getFollowRange() {return 1.0;}

    TacticalHud thud;
    public boolean shouldContinue() {
        thud = ((MinecraftClientExtended)MinecraftClient.getInstance()).getTacticalHud();
        if (thud != null && thud.status != TacticalHud.Status.EnemyTurn) {
            return false;
        }
        LivingEntity livingEntity_1 = this.mob.getTarget();
        if (livingEntity_1 == null) {
            livingEntity_1 = this.target;
        }

        if (livingEntity_1 == null) {
            return false;
        } else if (!livingEntity_1.isAlive()) {
            return false;
        } else {
            AbstractTeam abstractTeam_1 = this.mob.getScoreboardTeam();
            AbstractTeam abstractTeam_2 = livingEntity_1.getScoreboardTeam();
            if (abstractTeam_1 != null && abstractTeam_2 == abstractTeam_1) {
                return false;
            } else {
                double double_1 = this.getFollowRange();
                if (this.mob.squaredDistanceTo(livingEntity_1) > double_1 * double_1) {
                    return false;
                } else {
                    if (this.checkVisibility) {
                        if (this.mob.getVisibilityCache().canSee(livingEntity_1)) {
                            this.timeWithoutVisibility = 0;
                        } else if (++this.timeWithoutVisibility > this.maxTimeWithoutVisibility) {
                            return false;
                        }
                    }

                    if (livingEntity_1 instanceof PlayerEntity && ((PlayerEntity)livingEntity_1).abilities.invulnerable) {
                        return false;
                    } else {
                        this.mob.setTarget(livingEntity_1);
                        return true;
                    }
                }
            }
        }
    }

}

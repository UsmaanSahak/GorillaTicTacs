package net.fabricmc.example;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.EnumSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.Goal.Control;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

public class LookAtEnemyGoal extends Goal { //Will be default. Aggressive variant (points out), Ready variant (Checks item), Pumped variant (Randomly presses ctrl repeately, uncommon), Regular variant (none.)
    protected final MobEntity mob;
    protected Entity target;
    protected final float range;
    private int lookTime;
    private final float chance;
    protected final Class<? extends LivingEntity> targetType;
    protected final TargetPredicate targetPredicate;

    public LookAtEnemyGoal(MobEntity mobEntity_1, Class<? extends LivingEntity> class_1, float float_1) {
        this(mobEntity_1, class_1, float_1, 0.02F);
    }

    public LookAtEnemyGoal(MobEntity mobEntity_1, Class<? extends LivingEntity> class_1, float float_1, float float_2) {
        this.mob = mobEntity_1;
        this.targetType = class_1;
        this.range = float_1;
        this.chance = float_2;
        this.setControls(EnumSet.of(Control.LOOK));
        if (class_1 == PlayerEntity.class) {
            this.targetPredicate = (new TargetPredicate()).setBaseMaxDistance((double)float_1).includeTeammates().includeInvulnerable().ignoreEntityTargetRules().setPredicate((livingEntity_1) -> {
                return EntityPredicates.rides(mobEntity_1).test(livingEntity_1);
            });
        } else {
            this.targetPredicate = (new TargetPredicate()).setBaseMaxDistance((double)float_1).includeTeammates().includeInvulnerable().ignoreEntityTargetRules();
        }

    }

    public boolean canStart() {
        if (this.mob.getRand().nextFloat() >= this.chance) {
            return false;
        } else {
            if (this.mob.getTarget() != null) {
                this.target = this.mob.getTarget();
            }

            if (this.targetType == PlayerEntity.class) {
                this.target = this.mob.world.getClosestPlayer(this.targetPredicate, this.mob, this.mob.x, this.mob.y + (double)this.mob.getStandingEyeHeight(), this.mob.z);
            } else {
                this.target = this.mob.world.getClosestEntity(this.targetType, this.targetPredicate, this.mob, this.mob.x, this.mob.y + (double)this.mob.getStandingEyeHeight(), this.mob.z, this.mob.getBoundingBox().expand((double)this.range, 3.0D, (double)this.range));
            }

            return this.target != null;
        }
    }

    public boolean shouldContinue() {
        if (!this.target.isAlive()) {
            return false;
        } else if (this.mob.squaredDistanceTo(this.target) > (double)(this.range * this.range)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    public void start() {
        this.lookTime = 40 + this.mob.getRand().nextInt(40);
    }

    public void stop() {
        this.target = null;
    }

    public void tick() {
        this.mob.getLookControl().method_20248(this.target.x, this.target.y + (double)this.target.getStandingEyeHeight(), this.target.z);
        //--this.lookTime;
    }
}

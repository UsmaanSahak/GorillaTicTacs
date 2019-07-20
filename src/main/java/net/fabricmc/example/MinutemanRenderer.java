package net.fabricmc.example;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.util.Identifier;

public class MinutemanRenderer extends IllagerEntityRenderer<Minuteman> {
    private static final Identifier SKIN = new Identifier("textures/entity/illager/pillager.png");
    private static final Identifier AIM_SKIN = new Identifier("us","textures/entity/allies/pillager3.png");

    public MinutemanRenderer(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1);
        this.addFeature(new HeldItemFeatureRenderer(this));
    }
    protected Identifier method_4092(PillagerEntity pillagerEntity_1) {
        return SKIN;
    }

    @Override
    protected Identifier getTexture(Minuteman minuteman) {
        return minuteman.ExtCli.getTacticalHud() != null && minuteman.ExtCli.getTacticalHud().status == TacticalHud.Status.Attacking ? AIM_SKIN : SKIN;
    }
}

package net.fabricmc.example;


import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class TacticalAbilities {
    public static Map<String, Runnable> A;
    public TacticalAbilities() {
        A = new HashMap<>();
        A.put("CrossbowShoot", () -> {
            System.out.println("Shooting Crossbow!");
            TacticalMode tmode = ((MinecraftClientExtended)MinecraftClient.getInstance()).getTacticalMode();
            ((MinecraftClientExtended)MinecraftClient.getInstance()).getTacticalHud().status = TacticalHud.Status.Attacking;
            Vec3d pos = tmode.location = ((Entity)tmode.FocusedAlly).getPos();
            tmode.location = new Vec3d(pos.x + 2, pos.y, pos.z + 2);
        });
        A.put("CrossbowSuppress", () ->
                System.out.println("Suppressive fire!")
        );
        A.put("WakeUp", () ->
                System.out.println("Picked back up!")
        );
    }

}

/*
package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.GameRendererExtended;
import net.fabricmc.example.MinecraftClientExtended;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlProgramManager;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererExtended {
    @Shadow
    private static final Logger LOGGER = LogManager.getLogger();
    @Shadow
    private static final Identifier RAIN_LOC = new Identifier("textures/environment/rain.png");
    @Shadow
    private static final Identifier SNOW_LOC = new Identifier("textures/environment/snow.png");
    @Shadow @Final
    private  MinecraftClient client;
    @Shadow @Final
    private  ResourceManager resourceContainer;
    @Shadow
    private final Random random = new Random();
    @Shadow
    private float viewDistance;
    @Shadow @Final
    public  FirstPersonRenderer firstPersonRenderer;
    @Shadow @Final
    private  MapRenderer mapRenderer;
    @Shadow
    private int ticks;
    @Shadow
    private float movementFovMultiplier;
    @Shadow
    private float lastMovementFovMultiplier;
    @Shadow
    private float skyDarkness;
    @Shadow
    private float lastSkyDarkness;
    @Shadow
    private boolean renderHand = true;
    @Shadow
    private boolean blockOutlineEnabled = true;
    @Shadow
    private long lastWorldIconUpdate;
    @Shadow
    private long lastWindowFocusedTime = SystemUtil.getMeasuringTimeMs();
    @Shadow @Final
    private  LightmapTextureManager lightmapTextureManager;
    @Shadow
    private int field_3995;
    @Shadow
    private final float[] field_3991 = new float[1024];
    @Shadow
    private final float[] field_3989 = new float[1024];
    @Shadow @Final
    private  BackgroundRenderer backgroundRenderer;
    @Shadow
    private boolean field_4001;
    @Shadow
    private double field_4005 = 1.0D;
    @Shadow
    private double field_3988;
    @Shadow
    private double field_4004;
    @Shadow
    private ItemStack floatingItem;
    @Shadow
    private int floatingItemTimeLeft;
    @Shadow
    private float floatingItemWidth;
    @Shadow
    private float floatingItemHeight;
    @Shadow
    private ShaderEffect shader;
    @Shadow
    private static final Identifier[] SHADERS_LOCATIONS = new Identifier[]{new Identifier("shaders/post/notch.json"), new Identifier("shaders/post/fxaa.json"), new Identifier("shaders/post/art.json"), new Identifier("shaders/post/bumpy.json"), new Identifier("shaders/post/blobs2.json"), new Identifier("shaders/post/pencil.json"), new Identifier("shaders/post/color_convolve.json"), new Identifier("shaders/post/deconverge.json"), new Identifier("shaders/post/flip.json"), new Identifier("shaders/post/invert.json"), new Identifier("shaders/post/ntsc.json"), new Identifier("shaders/post/outline.json"), new Identifier("shaders/post/phosphor.json"), new Identifier("shaders/post/scan_pincushion.json"), new Identifier("shaders/post/sobel.json"), new Identifier("shaders/post/bits.json"), new Identifier("shaders/post/desaturate.json"), new Identifier("shaders/post/green.json"), new Identifier("shaders/post/blur.json"), new Identifier("shaders/post/wobble.json"), new Identifier("shaders/post/blobs.json"), new Identifier("shaders/post/antialias.json"), new Identifier("shaders/post/creeper.json"), new Identifier("shaders/post/spider.json")};
    @Shadow @Final
    public static  int SHADER_COUNT;
    @Shadow
    private int forcedShaderIndex;
    @Shadow
    private boolean shadersEnabled;
    @Shadow
    private int field_4021;
    @Shadow @Final
    private  Camera camera;

    @Shadow
    private boolean shouldRenderBlockOutline() { return false; } //Should I check this out later?
    @Shadow
    private double getFov(Camera camera_1, float float_1, boolean boolean_1) { return 0.0;}
    @Shadow
    private void renderAboveClouds(Camera camera_1, WorldRenderer worldRenderer_1, float float_1, double double_1, double double_2, double double_3) {}
    @Shadow
    public void disableLightmap() {}
    @Shadow
    public void enableLightmap() {}
    @Shadow
    protected void renderWeather(float float_1) {}
    @Shadow
    private void renderHand(Camera camera_1, float float_1) {}
    @Shadow
    private void applyCameraTransformations(float float_1) {}
    @Shadow
    private void updateMovementFovMultiplier() {}
    @Shadow
    private void renderRain() {}

    Vec3d travel;

    @Inject(at = @At("RETURN"), method="<init>*")
    private void constructor(MinecraftClient minecraftClient_1, ResourceManager resourceManager_1, CallbackInfo info) {
        travel = null;
    }
/*


    private void renderCenter(float float_1, long long_1) {
        WorldRenderer worldRenderer_1 = this.client.worldRenderer;
        ParticleManager particleManager_1 = this.client.particleManager;
        boolean boolean_1 = this.shouldRenderBlockOutline();
        GlStateManager.enableCull();
        this.client.getProfiler().swap("camera");
        this.applyCameraTransformations(float_1);
        Camera camera_1 = this.camera;
        camera_1.update(this.client.world, (Entity)(this.client.getCameraEntity() == null ? this.client.player : this.client.getCameraEntity()), this.client.options.perspective > 0, this.client.options.perspective == 2, float_1);
        Frustum frustum_1 = GlMatrixFrustum.get();
        this.client.getProfiler().swap("clear");
        GlStateManager.viewport(0, 0, this.client.window.getFramebufferWidth(), this.client.window.getFramebufferHeight());
        //this.backgroundRenderer.renderBackground(camera_1, float_1);
        GlStateManager.clear(16640, MinecraftClient.IS_SYSTEM_MAC);
        this.client.getProfiler().swap("culling");
        VisibleRegion visibleRegion_1 = new FrustumWithOrigin(frustum_1);
        double double_1 = camera_1.getPos().x;
        double double_2 = camera_1.getPos().y;
        double double_3 = camera_1.getPos().z;
        visibleRegion_1.setOrigin(double_1, double_2, double_3);
        if (this.client.options.viewDistance >= 4) {
            this.backgroundRenderer.applyFog(camera_1, -1);
            this.client.getProfiler().swap("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.multMatrix(Matrix4f.method_4929(this.getFov(camera_1, float_1, true), (float)this.client.window.getFramebufferWidth() / (float)this.client.window.getFramebufferHeight(), 0.05F, this.viewDistance * 2.0F));
            GlStateManager.matrixMode(5888);
            worldRenderer_1.renderSky(float_1);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.multMatrix(Matrix4f.method_4929(this.getFov(camera_1, float_1, true), (float)this.client.window.getFramebufferWidth() / (float)this.client.window.getFramebufferHeight(), 0.05F, this.viewDistance * MathHelper.SQUARE_ROOT_OF_TWO));
            GlStateManager.matrixMode(5888);
        }

        this.backgroundRenderer.applyFog(camera_1, 0);
        GlStateManager.shadeModel(7425);
        if (camera_1.getPos().y < 128.0D) {
            this.renderAboveClouds(camera_1, worldRenderer_1, float_1, double_1, double_2, double_3);
        }

        this.client.getProfiler().swap("prepareterrain");
        this.backgroundRenderer.applyFog(camera_1, 0);
        this.client.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        GuiLighting.disable();
        this.client.getProfiler().swap("terrain_setup");
        this.client.world.method_2935().getLightingProvider().doLightUpdates(2147483647, true, true);
        worldRenderer_1.setUpTerrain(camera_1, visibleRegion_1, this.field_4021++, this.client.player.isSpectator());
        this.client.getProfiler().swap("updatechunks");
        this.client.worldRenderer.updateChunks(long_1);
        this.client.getProfiler().swap("terrain");
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlphaTest();
        worldRenderer_1.renderLayer(BlockRenderLayer.SOLID, camera_1);
        GlStateManager.enableAlphaTest();
        worldRenderer_1.renderLayer(BlockRenderLayer.CUTOUT_MIPPED, camera_1);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).pushFilter(false, false);
        worldRenderer_1.renderLayer(BlockRenderLayer.CUTOUT, camera_1);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).popFilter();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GuiLighting.enable();
        this.client.getProfiler().swap("entities");
        worldRenderer_1.renderEntities(camera_1, visibleRegion_1, float_1);
        GuiLighting.disable();
        this.disableLightmap();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (boolean_1 && this.client.hitResult != null) {
            GlStateManager.disableAlphaTest();
            this.client.getProfiler().swap("outline");
            worldRenderer_1.drawHighlightedBlockOutline(camera_1, this.client.hitResult, 0);
            GlStateManager.enableAlphaTest();
        }

        if (this.client.debugRenderer.shouldRender()) {
            this.client.debugRenderer.renderDebuggers(long_1);
        }

        this.client.getProfiler().swap("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).pushFilter(false, false);
        worldRenderer_1.renderPartiallyBrokenBlocks(Tessellator.getInstance(), Tessellator.getInstance().getBufferBuilder(), camera_1);
        this.client.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).popFilter();
        GlStateManager.disableBlend();
        this.enableLightmap();
        this.backgroundRenderer.applyFog(camera_1, 0);
        this.client.getProfiler().swap("particles");
        particleManager_1.renderParticles(camera_1, float_1);
        this.disableLightmap();
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        this.client.getProfiler().swap("weather");
        this.renderWeather(float_1);
        GlStateManager.depthMask(true);
        worldRenderer_1.renderWorldBorder(camera_1, float_1);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        this.backgroundRenderer.applyFog(camera_1, 0);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        this.client.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        GlStateManager.shadeModel(7425);
        this.client.getProfiler().swap("translucent");
        worldRenderer_1.renderLayer(BlockRenderLayer.TRANSLUCENT, camera_1);
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (camera_1.getPos().y >= 128.0D) {
            this.client.getProfiler().swap("aboveClouds");
            this.renderAboveClouds(camera_1, worldRenderer_1, float_1, double_1, double_2, double_3);
        }

        this.client.getProfiler().swap("hand");
        if (this.renderHand) {
            GlStateManager.clear(256, MinecraftClient.IS_SYSTEM_MAC);
            this.renderHand(camera_1, float_1);
        }

    }


    public void tick() {

        if (GLX.usePostProcess && GlProgramManager.getInstance() == null) {
            GlProgramManager.init();
        }

        this.updateMovementFovMultiplier();
        this.lightmapTextureManager.tick();
        if (this.client.getCameraEntity() == null) {
            this.client.setCameraEntity(this.client.player);
        }

        this.camera.updateEyeHeight();
        ++this.ticks;
        this.firstPersonRenderer.updateHeldItems();
        this.renderRain();
        this.lastSkyDarkness = this.skyDarkness;
        if (this.client.inGameHud.getBossBarHud().shouldDarkenSky()) {
            this.skyDarkness += 0.05F;
            if (this.skyDarkness > 1.0F) {
                this.skyDarkness = 1.0F;
            }
        } else if (this.skyDarkness > 0.0F) {
            this.skyDarkness -= 0.0125F;
        }

        if (this.floatingItemTimeLeft > 0) {
            --this.floatingItemTimeLeft;
            if (this.floatingItemTimeLeft == 0) {
                this.floatingItem = null;
            }
        }

    }


}*/
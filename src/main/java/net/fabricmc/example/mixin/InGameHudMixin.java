package net.fabricmc.example.mixin;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.MinecraftClientExtended;
import net.fabricmc.example.TacticalHud;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.options.AttackIndicator;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_LOCATION;
import static net.minecraft.client.gui.DrawableHelper.STATS_ICON_LOCATION;
import static org.lwjgl.opengl.GL11.*;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {


    @Shadow @Final
    private static  Identifier VIGNETTE_TEX;
    @Shadow @Final
    private static  Identifier WIDGETS_TEX;
    @Shadow @Final
    private static  Identifier PUMPKIN_BLUR;
    @Shadow @Final
    private  Random random = new Random();
    @Shadow @Final
    private  MinecraftClient client;
    @Shadow @Final
    private  ItemRenderer itemRenderer;
    @Shadow @Final
    private  ChatHud chatHud;
    @Shadow
    private int ticks;
    @Shadow
    private String overlayMessage;
    @Shadow
    private int overlayRemaining;
    @Shadow
    private boolean overlayTinted;
    @Shadow
    public float field_2013 = 1.0F;
    @Shadow
    private int heldItemTooltipFade;
    @Shadow
    private ItemStack currentStack;
    @Shadow @Final
    private  DebugHud debugHud;
    @Shadow @Final
    private  SubtitlesHud subtitlesHud;
    @Shadow @Final
    private  SpectatorHud spectatorHud;
    @Shadow @Final
    private  PlayerListHud playerListHud;
    @Shadow @Final
    private  BossBarHud bossBarHud;
    @Shadow
    private int titleTotalTicks;
    @Shadow
    private String title;
    @Shadow
    private String subtitle;
    @Shadow
    private int titleFadeInTicks;
    @Shadow
    private int titleRemainTicks;
    @Shadow
    private int titleFadeOutTicks;
    @Shadow
    private int field_2014;
    @Shadow
    private int field_2033;
    @Shadow
    private long field_2012;
    @Shadow
    private long field_2032;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Shadow @Final
    private Map<ChatMessageType, List<ClientChatListener>> listeners;
    @Shadow
    public TextRenderer getFontRenderer() {
        return this.client.textRenderer;
    }
    private void renderVignetteOverlay(Entity entity_1) {}
    @Shadow
    private void renderPumpkinOverlay() {}
    @Shadow
    private void renderPortalOverlay(float float_1) {}
    @Shadow
    protected void renderHotbar(float float_1) {}
    @Shadow
    private void renderCrosshair() {}
    @Shadow
    private void renderStatusBars() {}
    @Shadow
    private void drawMountHealth() {}
    @Shadow
    public void renderMountJumpBar(int int_1) {}
    @Shadow
    public void renderExperienceBar(int int_1) {}
    @Shadow
    public void renderHeldItemTooltip() {}
    @Shadow
    public void renderDemoTimer() {}
    @Shadow
    protected void renderStatusEffectOverlay() {}
    @Shadow
    private void method_19346(TextRenderer textRenderer_1, int int_1, int int_2) {}
    @Shadow
    private void renderScoreboardSidebar(ScoreboardObjective scoreboardObjective_1) {}
        public void draw(float float_1) {
        this.scaledWidth = this.client.window.getScaledWidth();
        this.scaledHeight = this.client.window.getScaledHeight();
        TextRenderer textRenderer_1 = this.getFontRenderer();
        GlStateManager.enableBlend();
        if (MinecraftClient.isFancyGraphicsEnabled()) {
            this.renderVignetteOverlay(this.client.getCameraEntity());
        } else {
            GlStateManager.enableDepthTest();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        ItemStack itemStack_1 = this.client.player.inventory.getArmorStack(3);
        if (this.client.options.perspective == 0 && itemStack_1.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            this.renderPumpkinOverlay();
        }

        float float_6;
        if (!this.client.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            float_6 = MathHelper.lerp(float_1, this.client.player.lastNauseaStrength, this.client.player.nextNauseaStrength);
            if (float_6 > 0.0F) {
                this.renderPortalOverlay(float_6);
            }
        }

        if (this.client.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) {
            this.spectatorHud.draw(float_1);
        } else if (!this.client.options.hudHidden) {
            this.renderHotbar(float_1);
        }

        if (!this.client.options.hudHidden) {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            GlStateManager.enableBlend();
            GlStateManager.enableAlphaTest();
            this.renderCrosshair();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.client.getProfiler().push("bossHealth");
            this.bossBarHud.draw();
            this.client.getProfiler().pop();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            if (this.client.interactionManager.hasStatusBars()) {
                this.renderStatusBars();
            }

            this.drawMountHealth();
            GlStateManager.disableBlend();
            int int_1 = this.scaledWidth / 2 - 91;
            if (this.client.player.hasJumpingMount()) {
                this.renderMountJumpBar(int_1);
            } else if (this.client.interactionManager.hasExperienceBar()) {
                this.renderExperienceBar(int_1);
            }

            if (this.client.options.heldItemTooltips && this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
                this.renderHeldItemTooltip();
            } else if (this.client.player.isSpectator()) {
                this.spectatorHud.draw();
            }
        }
        TacticalHud thud = ((MinecraftClientExtended)client).getTacticalHud();
        if (thud != null) {
            //this.client.getTextureManager().bindTexture(new Identifier("textures/gui/icons.png"));
            this.client.getTextureManager().bindTexture(new Identifier("us","icon.png"));
            thud.renderGeneral();
        }

        int int_7;
        if (this.client.player.getSleepTimer() > 0) {
            this.client.getProfiler().push("sleep");
            GlStateManager.disableDepthTest();
            GlStateManager.disableAlphaTest();
            float_6 = (float)this.client.player.getSleepTimer();
            float float_4 = float_6 / 100.0F;
            if (float_4 > 1.0F) {
                float_4 = 1.0F - (float_6 - 100.0F) / 10.0F;
            }

            int_7 = (int)(220.0F * float_4) << 24 | 1052704;
            fill(0, 0, this.scaledWidth, this.scaledHeight, int_7);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableDepthTest();
            this.client.getProfiler().pop();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (this.client.isDemo()) {
            this.renderDemoTimer();
        }

        this.renderStatusEffectOverlay();
        if (this.client.options.debugEnabled) {
            this.debugHud.draw();
        }

        if (!this.client.options.hudHidden) {
            int int_8;
            int int_6;
            if (this.overlayRemaining > 0) {
                this.client.getProfiler().push("overlayMessage");
                float_6 = (float)this.overlayRemaining - float_1;
                int_6 = (int)(float_6 * 255.0F / 20.0F);
                if (int_6 > 255) {
                    int_6 = 255;
                }

                if (int_6 > 8) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight - 68), 0.0F);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    int_7 = 16777215;
                    if (this.overlayTinted) {
                        int_7 = MathHelper.hsvToRgb(float_6 / 50.0F, 0.7F, 0.6F) & 16777215;
                    }

                    int_8 = int_6 << 24 & -16777216;
                    this.method_19346(textRenderer_1, -4, textRenderer_1.getStringWidth(this.overlayMessage));
                    textRenderer_1.draw(this.overlayMessage, (float)(-textRenderer_1.getStringWidth(this.overlayMessage) / 2), -4.0F, int_7 | int_8);
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }

                this.client.getProfiler().pop();
            }

            if (this.titleTotalTicks > 0) {
                this.client.getProfiler().push("titleAndSubtitle");
                float_6 = (float)this.titleTotalTicks - float_1;
                int_6 = 255;
                if (this.titleTotalTicks > this.titleFadeOutTicks + this.titleRemainTicks) {
                    float float_7 = (float)(this.titleFadeInTicks + this.titleRemainTicks + this.titleFadeOutTicks) - float_6;
                    int_6 = (int)(float_7 * 255.0F / (float)this.titleFadeInTicks);
                }

                if (this.titleTotalTicks <= this.titleFadeOutTicks) {
                    int_6 = (int)(float_6 * 255.0F / (float)this.titleFadeOutTicks);
                }

                int_6 = MathHelper.clamp(int_6, 0, 255);
                if (int_6 > 8) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight / 2), 0.0F);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.pushMatrix();
                    GlStateManager.scalef(4.0F, 4.0F, 4.0F);
                    int_7 = int_6 << 24 & -16777216;
                    int_8 = textRenderer_1.getStringWidth(this.title);
                    this.method_19346(textRenderer_1, -10, int_8);
                    textRenderer_1.drawWithShadow(this.title, (float)(-int_8 / 2), -10.0F, 16777215 | int_7);
                    GlStateManager.popMatrix();
                    if (!this.subtitle.isEmpty()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.scalef(2.0F, 2.0F, 2.0F);
                        int int_9 = textRenderer_1.getStringWidth(this.subtitle);
                        this.method_19346(textRenderer_1, 5, int_9);
                        textRenderer_1.drawWithShadow(this.subtitle, (float)(-int_9 / 2), 5.0F, 16777215 | int_7);
                        GlStateManager.popMatrix();
                    }

                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }

                this.client.getProfiler().pop();
            }

            this.subtitlesHud.draw();
            Scoreboard scoreboard_1 = this.client.world.getScoreboard();
            ScoreboardObjective scoreboardObjective_1 = null;
            Team team_1 = scoreboard_1.getPlayerTeam(this.client.player.getEntityName());
            if (team_1 != null) {
                int_8 = team_1.getColor().getId();
                if (int_8 >= 0) {
                    scoreboardObjective_1 = scoreboard_1.getObjectiveForSlot(3 + int_8);
                }
            }

            ScoreboardObjective scoreboardObjective_2 = scoreboardObjective_1 != null ? scoreboardObjective_1 : scoreboard_1.getObjectiveForSlot(1);
            if (scoreboardObjective_2 != null) {
                this.renderScoreboardSidebar(scoreboardObjective_2);
            }

            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.disableAlphaTest();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, (float)(this.scaledHeight - 48), 0.0F);
            this.client.getProfiler().push("chat");
            this.chatHud.draw(this.ticks);
            this.client.getProfiler().pop();
            GlStateManager.popMatrix();
            scoreboardObjective_2 = scoreboard_1.getObjectiveForSlot(0);
            if (!this.client.options.keyPlayerList.isPressed() || this.client.isInSingleplayer() && this.client.player.networkHandler.getPlayerList().size() <= 1 && scoreboardObjective_2 == null) {
                this.playerListHud.tick(false);
            } else {
                this.playerListHud.tick(true);
                this.playerListHud.draw(this.scaledWidth, scoreboard_1, scoreboardObjective_2);
            }
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlphaTest();
    }

}

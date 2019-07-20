package net.fabricmc.example.mixin;

import com.google.common.collect.Queues;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.datafixers.DataFixer;
import com.sun.istack.internal.Nullable;
import net.fabricmc.example.*;
import net.minecraft.SharedConstants;
import net.minecraft.client.*;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.GlDebug;
import net.minecraft.client.gl.GlFramebuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.options.*;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.resource.*;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.*;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.WindowProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.resource.*;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.DisableableProfiler;
import net.minecraft.util.snooper.Snooper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLXEXTStereoTree;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

import static net.minecraft.client.gui.DrawableHelper.BACKGROUND_LOCATION;
import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_LOCATION;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientExtended {
    ///*


    ///*
    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Final
    public static boolean IS_SYSTEM_MAC;
    @Shadow
    @Final
    public static Identifier DEFAULT_TEXT_RENDERER_ID;
    @Shadow
    @Final
    public static Identifier ALT_TEXT_RENDERER_ID;
    @Shadow
    public static CompletableFuture<Unit> voidFuture;
    @Shadow
    public static byte[] memoryReservedForCrash;
    @Shadow
    private static int cachedMaxTextureSize;
    @Shadow
    @Final
    private File resourcePackDir;
    @Shadow
    @Final
    private PropertyMap sessionPropertyMap;
    @Shadow
    @Final
    private WindowSettings windowSettings;
    @Shadow
    private ServerEntry currentServerEntry;
    @Shadow
    private TextureManager textureManager;
    @Shadow
    private static MinecraftClient instance;
    @Shadow
    @Final
    private DataFixer dataFixer;
    @Shadow
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    private WindowProvider windowProvider;
    @Shadow
    public Window window;
    @Shadow
    private boolean crashed;
    @Shadow
    private CrashReport crashReport;
    @Shadow
    private boolean connectedToRealms;
    @Shadow
    @Final
    private RenderTickCounter renderTickCounter;
    @Shadow
    @Final
    private Snooper snooper;
    @Shadow
    public ClientWorld world;
    @Shadow
    public WorldRenderer worldRenderer;
    @Shadow
    private EntityRenderDispatcher entityRenderManager;
    @Shadow
    private ItemRenderer itemRenderer;
    @Shadow
    private FirstPersonRenderer firstPersonRenderer;
    @Shadow
    public ClientPlayerEntity player;

    @Nullable
    @Shadow
    public Entity cameraEntity;

    @Nullable
    @Shadow
    public Entity targetedEntity;
    @Shadow
    public ParticleManager particleManager;
    @Shadow
    @Final
    private SearchManager searchManager;
    @Shadow
    @Final
    private Session session;
    @Shadow
    private boolean paused;
    @Shadow
    private float pausedTickDelta;
    @Shadow
    public TextRenderer textRenderer;

    @Nullable
    @Shadow
    public Screen currentScreen;

    @Nullable
    @Shadow
    public Overlay overlay;
    @Shadow
    public GameRenderer gameRenderer;
    @Shadow
    public DebugRenderer debugRenderer;
    @Shadow
    protected int attackCooldown;

    @Nullable
    @Shadow
    private IntegratedServer server;
    @Shadow
    @Final
    private AtomicReference<WorldGenerationProgressTracker> worldGenProgressTracker;
    @Shadow
    public InGameHud inGameHud;
    @Shadow
    public boolean skipGameRender;
    @Shadow
    public HitResult hitResult;
    @Shadow
    @Final
    public GameOptions options;
    @Shadow
    private HotbarStorage creativeHotbarStorage;
    @Shadow
    public Mouse mouse;
    @Shadow
    public Keyboard keyboard;
    @Shadow
    @Final
    public File runDirectory;
    @Shadow
    @Final
    private File assetDirectory;
    @Shadow
    @Final
    private String gameVersion;
    @Shadow
    @Final
    private String versionType;
    @Shadow
    @Final
    private Proxy netProxy;
    @Shadow
    private LevelStorage levelStorage;
    @Shadow
    private static int currentFps;
    @Shadow
    private int itemUseCooldown;
    @Shadow
    private String autoConnectServerIp;
    @Shadow
    private int autoConnectServerPort;
    @Shadow
    @Final
    public MetricsData metricsData;
    @Shadow
    private long lastMetricsSampleTime;
    @Shadow
    @Final
    private boolean is64Bit;
    @Shadow
    @Final
    private boolean isDemo;

    @Nullable
    @Shadow
    private ClientConnection clientConnection;
    @Shadow
    private boolean isIntegratedServerRunning;
    @Shadow
    @Final
    private DisableableProfiler profiler;
    @Shadow
    private ReloadableResourceManager resourceManager;
    @Shadow
    @Final
    private ClientResourcePackCreator resourcePackCreator;
    @Shadow
    @Final
    private ResourcePackContainerManager<ClientResourcePackContainer> resourcePackContainerManager;
    @Shadow
    private LanguageManager languageManager;
    @Shadow
    private BlockColors blockColorMap;
    @Shadow
    private ItemColors itemColorMap;
    @Shadow
    private GlFramebuffer framebuffer;
    @Shadow
    private SpriteAtlasTexture spriteAtlas;
    @Shadow
    private SoundManager soundManager;
    @Shadow
    private MusicTracker musicTracker;
    @Shadow
    private FontManager fontManager;
    @Shadow
    private SplashTextResourceSupplier splashTextLoader;
    @Shadow
    @Final
    private MinecraftSessionService sessionService;
    @Shadow
    private PlayerSkinProvider skinProvider;
    @Shadow
    private final Thread thread = Thread.currentThread();
    @Shadow
    private BakedModelManager bakedModelManager;
    @Shadow
    private BlockRenderManager blockRenderManager;
    @Shadow
    private PaintingManager paintingManager;
    @Shadow
    private StatusEffectSpriteManager statusEffectSpriteManager;
    @Shadow
    @Final
    private ToastManager toastManager;
    @Shadow
    private MinecraftClientGame game;
    @Shadow
    private volatile boolean isRunning;
    @Shadow
    public String fpsDebugString;
    @Shadow
    public boolean field_1730 = true;
    @Shadow
    private long nextDebugInfoUpdateTime;
    @Shadow
    private int fpsCounter;
    @Shadow
    @Final
    private TutorialManager tutorialManager;
    @Shadow
    private boolean windowFocused;
    @Shadow
    @Final
    private Queue<Runnable> renderTaskQueue;
    @Shadow
    private CompletableFuture<Void> resourceReloadFuture;
    @Shadow
    private String openProfilerSection;

    @Shadow
    private void doAttack() {
    }

    @Shadow
    public Entity getCameraEntity() {
        return null;
    }

    @Shadow
    public void openScreen(@Nullable Screen screen_1) {
    }

    @Shadow
    public ClientPlayNetworkHandler getNetworkHandler() {
        return null;
    }

    @Shadow
    private void doItemUse() {
    }

    @Shadow
    private void doItemPick() {
    }

    @Shadow
    private void method_1590(boolean boolean_1) {
    }
//*/


    //*/
///*
    @Shadow
    private void handleInputEvents() {}



    int tactical;


    KeyBinding tacticalKey;
    TacticalMode tmode;
    TacticalHud thud;


    public TacticalMode getTacticalMode() {
        return tmode;
    }
    public void setTacticalMode(TacticalMode newtmode) { tmode = newtmode; }
    public void setTactical(int i) { tactical = i; System.out.println("tactical is 0.");}
    public TacticalHud getTacticalHud() { return thud; }
    public void setTacticalHud(TacticalHud newthud) { thud = newthud; }


    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo info) {


        tacticalKey = ((OptionsExtended) this.options).getTacticalKey();
        tactical = 0;
        System.out.println("This line is printed by an example mod mixin!");
    }


    @Inject(at = @At("HEAD"), method = "handleInputEvents")
    private void handleInputEvents(CallbackInfo info) {

                if (tacticalKey.wasPressed()) {
                tactical = 1;
                System.out.println("initializing new tmode and thud!");
                tmode = new TacticalMode();
                thud = new TacticalHud(tmode);
            }

    }
//*/
/*
    private void handleInputEvents() {
            for (; this.options.keyTogglePerspective.wasPressed(); this.worldRenderer.scheduleTerrainUpdate()) {
                ++this.options.perspective;
                if (this.options.perspective > 2) {
                    this.options.perspective = 0;
                }

                if (this.options.perspective == 0) {
                    this.gameRenderer.onCameraEntitySet(this.getCameraEntity());
                } else if (this.options.perspective == 1) {
                    this.gameRenderer.onCameraEntitySet((Entity) null);
                }
            }

            while (this.options.keySmoothCamera.wasPressed()) {
                this.options.smoothCameraEnabled = !this.options.smoothCameraEnabled;
            }

            for (int int_1 = 0; int_1 < 9; ++int_1) {
                boolean boolean_1 = this.options.keySaveToolbarActivator.isPressed();
                boolean boolean_2 = this.options.keyLoadToolbarActivator.isPressed();
                if (this.options.keysHotbar[int_1].wasPressed()) {
                    if (this.player.isSpectator()) {
                        this.inGameHud.getSpectatorWidget().onHotbarKeyPress(int_1);
                    } else if (!this.player.isCreative() || this.currentScreen != null || !boolean_2 && !boolean_1) {
                        this.player.inventory.selectedSlot = int_1;
                    } else {
                        CreativeInventoryScreen.onHotbarKeyPress((MinecraftClient) (Object) this, int_1, boolean_2, boolean_1);
                    }
                }
            }

            while (this.options.keyInventory.wasPressed()) {
                if (this.interactionManager.hasRidingInventory()) {
                    this.player.openRidingInventory();
                } else {
                    this.tutorialManager.onInventoryOpened();
                    this.openScreen(new InventoryScreen(this.player));
                }
            }

            while (this.options.keyAdvancements.wasPressed()) {
                this.openScreen(new AdvancementsScreen(this.player.networkHandler.getAdvancementHandler()));
            }

            while (this.options.keySwapHands.wasPressed()) {
                if (!this.player.isSpectator()) {
                    this.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, Direction.DOWN));
                }
            }

            while (this.options.keyDrop.wasPressed()) {
                if (!this.player.isSpectator()) {
                    this.player.dropSelectedItem(Screen.hasControlDown());
                }
            }

            boolean boolean_3 = this.options.chatVisibility != ChatVisibility.HIDDEN;
            if (boolean_3) {
                while (this.options.keyChat.wasPressed()) {
                    this.openScreen(new ChatScreen(""));
                }

                if (this.currentScreen == null && this.overlay == null && this.options.keyCommand.wasPressed()) {
                    this.openScreen(new ChatScreen("/"));
                }
            }

            if (this.player.isUsingItem()) {
                if (!this.options.keyUse.isPressed()) {
                    this.interactionManager.stopUsingItem(this.player);
                }

                label111:
                while (true) {
                    if (!this.options.keyAttack.wasPressed()) {
                        while (this.options.keyUse.wasPressed()) {
                        }

                        while (true) {
                            if (this.options.keyPickItem.wasPressed()) {
                                continue;
                            }
                            break label111;
                        }
                    }
                }
            } else {
                while (this.options.keyAttack.wasPressed()) {
                    this.doAttack();
                }

                while (this.options.keyUse.wasPressed()) {
                    this.doItemUse();
                }

                while (this.options.keyPickItem.wasPressed()) {
                    this.doItemPick();
                }
            }

            if (this.options.keyUse.isPressed() && this.itemUseCooldown == 0 && !this.player.isUsingItem()) {
                this.doItemUse();
            }

            this.method_1590(this.currentScreen == null && this.options.keyAttack.isPressed() && this.mouse.isCursorLocked());


    }
*/

    @Shadow
    public TextureManager getTextureManager() {
        return this.textureManager;
    }

    ///*
    public void tick() {
        if (this.itemUseCooldown > 0) {
            --this.itemUseCooldown;
        }

        this.profiler.push("gui");
        if (!this.paused) {
            this.inGameHud.tick();
        }

        this.profiler.pop();
        this.gameRenderer.updateTargetedEntity(1.0F);
        this.tutorialManager.tick(this.world, this.hitResult);
        this.profiler.push("gameMode");
        if (!this.paused && this.world != null) {
            this.interactionManager.tick();
        }

        this.profiler.swap("textures");
        if (this.world != null) {
            this.textureManager.tick();
        }

        if (this.currentScreen == null && this.player != null) {
            if (this.player.getHealth() <= 0.0F && !(this.currentScreen instanceof DeathScreen)) {
                this.openScreen((Screen)null);
            } else if (this.player.isSleeping() && this.world != null) {
                this.openScreen(new SleepingChatScreen());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof SleepingChatScreen && !this.player.isSleeping()) {
            this.openScreen((Screen)null);
        }

        if (this.currentScreen != null) {
            this.attackCooldown = 10000;
        }

        if (this.currentScreen != null) {
            Screen.wrapScreenError(() -> {
                this.currentScreen.tick();
            }, "Ticking screen", this.currentScreen.getClass().getCanonicalName());
        }

        if (!this.options.debugEnabled) {
            this.inGameHud.resetDebugHudChunk();
        }

        if (this.overlay == null && (this.currentScreen == null || this.currentScreen.passEvents)) {
            this.profiler.swap("GLFW events");
            GLX.pollEvents();
            if (tactical == 0) {
                this.handleInputEvents();
            }
            else if (tactical == 1) { //tactical == 1
                thud.handleInputEvents();
            }
            if (this.attackCooldown > 0) {
                --this.attackCooldown;
            }
        }

        if (this.world != null) {
            this.profiler.swap("gameRenderer");
            if (!this.paused) {
                this.gameRenderer.tick();
            }

            this.profiler.swap("levelRenderer");
            if (!this.paused) {
                this.worldRenderer.tick();
            }

            this.profiler.swap("level");
            if (!this.paused) {
                if (this.world.getTicksSinceLightning() > 0) {
                    this.world.setTicksSinceLightning(this.world.getTicksSinceLightning() - 1);
                }

                this.world.tickEntities();
            }
        } else if (this.gameRenderer.isShaderEnabled()) {
            this.gameRenderer.disableShader();
        }

        if (!this.paused) {
            this.musicTracker.tick();
        }

        this.soundManager.tick(this.paused);
        if (this.world != null) {
            if (!this.paused) {
                this.world.setMobSpawnOptions(this.world.getDifficulty() != Difficulty.PEACEFUL, true);
                this.tutorialManager.tick();

                try {
                    this.world.tick(() -> {
                        return true;
                    });
                } catch (Throwable var4) {
                    CrashReport crashReport_1 = CrashReport.create(var4, "Exception in world tick");
                    if (this.world == null) {
                        CrashReportSection crashReportSection_1 = crashReport_1.addElement("Affected level");
                        crashReportSection_1.add("Problem", "Level is null!");
                    } else {
                        this.world.addDetailsToCrashReport(crashReport_1);
                    }

                    throw new CrashException(crashReport_1);
                }
            }

            this.profiler.swap("animateTick");
            if (!this.paused && this.world != null) {
                this.world.doRandomBlockDisplayTicks(MathHelper.floor(this.player.x), MathHelper.floor(this.player.y), MathHelper.floor(this.player.z));
            }

            this.profiler.swap("particles");
            if (!this.paused) {
                this.particleManager.tick();
            }
        } else if (this.clientConnection != null) {
            this.profiler.swap("pendingConnection");
            this.clientConnection.tick();
        }

        this.profiler.swap("keyboard");
        this.keyboard.pollDebugCrash();
        this.profiler.pop();
    }
   // */
}
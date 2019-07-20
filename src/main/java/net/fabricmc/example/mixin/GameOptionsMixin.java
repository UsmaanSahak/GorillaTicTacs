package net.fabricmc.example.mixin;

import net.fabricmc.example.OptionsExtended;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements OptionsExtended {
    @Final
    public KeyBinding keyOne;
    @Final
    public KeyBinding keyTwo;
    @Final
    public KeyBinding keyThree;
    @Final
    public KeyBinding keyFour;
    @Final
    public KeyBinding keyTactical;
    @Shadow @Final
    public  KeyBinding keyForward;
    @Shadow @Final
    public  KeyBinding keyLeft;
    @Shadow @Final
    public  KeyBinding keyBack;
    @Shadow @Final
    public  KeyBinding keyRight;
    @Shadow @Final
    public  KeyBinding keyJump;
    @Shadow @Final
    public  KeyBinding keySneak;
    @Shadow @Final
    public  KeyBinding keySprint;
    @Shadow @Final
    public  KeyBinding keyInventory;
    @Shadow @Final
    public  KeyBinding keySwapHands;
    @Shadow @Final
    public  KeyBinding keyDrop;
    @Shadow @Final
    public  KeyBinding keyUse;
    @Shadow @Final
    public  KeyBinding keyAttack;
    @Shadow @Final
    public  KeyBinding keyPickItem;
    @Shadow @Final
    public  KeyBinding keyChat;
    @Shadow @Final
    public  KeyBinding keyPlayerList;
    @Shadow @Final
    public  KeyBinding keyCommand;
    @Shadow @Final
    public  KeyBinding keyScreenshot;
    @Shadow @Final
    public  KeyBinding keyTogglePerspective;
    @Shadow @Final
    public  KeyBinding keySmoothCamera;
    @Shadow @Final
    public  KeyBinding keyFullscreen;
    @Shadow @Final
    public  KeyBinding keySpectatorOutlines;
    @Shadow @Final
    public  KeyBinding keyAdvancements;
    @Shadow @Final
    public  KeyBinding[] keysHotbar;
    @Shadow @Final
    public  KeyBinding keySaveToolbarActivator;
    @Shadow @Final
    public  KeyBinding keyLoadToolbarActivator;
    @Shadow @Final
    public KeyBinding[] keysAll;
    @Inject(at = @At("RETURN"), method="<init>*")
    private void constructor(MinecraftClient minecraftClient_1, File file_1, CallbackInfo info) {
        this.keyTactical = new KeyBinding("key.tactical", 82, "key.categories.gameplay");
        /*
        this.keyOne = new KeyBinding("key.one", 321, "key.categories.gameplay");
        this.keyTwo = new KeyBinding("key.two", 322, "key.categories.gameplay");
        this.keyThree = new KeyBinding("key.three", 323, "key.categories.gameplay");
        this.keyFour = new KeyBinding("key.four", 324, "key.categories.gameplay");
        */
        this.keysAll = (KeyBinding[]) ArrayUtils.addAll(new KeyBinding[]{this.keyAttack, this.keyUse, this.keyForward, this.keyLeft, this.keyBack, this.keyRight, this.keyJump, this.keySneak, this.keySprint, this.keyDrop, this.keyInventory, this.keyChat, this.keyPlayerList, this.keyPickItem, this.keyCommand, this.keyScreenshot, this.keyTogglePerspective, this.keySmoothCamera, this.keyFullscreen, this.keySpectatorOutlines, this.keySwapHands, this.keySaveToolbarActivator, this.keyLoadToolbarActivator, this.keyAdvancements, this.keyTactical/*,this.keyOne,this.keyTwo,this.keyThree,this.keyFour*/}, this.keysHotbar);
    }
    public KeyBinding getTacticalKey() {
        return keyTactical;
    }
    /*
    public KeyBinding getOneKey() { return keyOne; }
    public KeyBinding getTwoKey() { return keyTwo; }
    public KeyBinding getThreeKey() { return keyThree; }
    public KeyBinding getFourKey() { return keyFour; }
     */
}

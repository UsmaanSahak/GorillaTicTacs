package net.fabricmc.example;

public interface MinecraftClientExtended {
    public TacticalMode getTacticalMode();
    public void setTacticalMode(TacticalMode newtmode);
    public void setTactical(int i);
    public TacticalHud getTacticalHud();
    public void setTacticalHud(TacticalHud newthud);
}

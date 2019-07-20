package net.fabricmc.example;

import java.util.ArrayList;

public interface Ally {
    ArrayList<String> availableMoves(); //Returns an array of what moves should be listed for guy. Gets updated. Use "Abilities" map to get the runnable.
    void refresh(); //Updates cooldowns, attack/reload status, etc. Should be called every move.
    int getAP();
    void spendAP(int amount);
    double shortDistance();
    double longDistance();
    int isCovered();
    void setCovered();
    Ally.State getState2();
    public static enum State {
            CROSSED,
            ATTACKING,
            SPELLCASTING,
            BOW_AND_ARROW,
            CROSSBOW_HOLD,
            CROSSBOW_CHARGE,
            CELEBRATING,
            COVERED;

            private State() {
            }
    }
    ModelContainer askAnimation();
}

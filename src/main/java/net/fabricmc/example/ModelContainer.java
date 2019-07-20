package net.fabricmc.example;

public class ModelContainer {
    public CuboidData rightArm;
    public CuboidData leftArm;
    public CuboidData head;
    public CuboidData torso;
    public CuboidData rightLeg;
    public CuboidData leftLeg;
    public ModelContainer(CuboidData ra, CuboidData la, CuboidData he, CuboidData to, CuboidData rl, CuboidData ll) {
        this.rightArm = ra;
        this.leftArm = la;
        this.head = he;
        this.torso = to;
        this.rightLeg = rl;
        this.leftLeg = ll;
    }
}
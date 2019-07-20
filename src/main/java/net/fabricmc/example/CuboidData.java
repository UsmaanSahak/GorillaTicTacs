package net.fabricmc.example;

public class CuboidData {
    public float yaw;
    public float pitch;
    public float rotationPointX;
    public float rotationPointZ;
    public float x;
    public float z;
    public CuboidData(float y, float p, float rx, float rz, float x1, float z1) {
        yaw = y;
        pitch = p;
        rotationPointX = rx;
        rotationPointZ = rz;
        x = x1;
        z = z1;
    }
    public CuboidData() {
        yaw = 0.0F;
        pitch = 0.0F;
        rotationPointX = 0.0F;
        rotationPointZ = 0.0F;
    }
}
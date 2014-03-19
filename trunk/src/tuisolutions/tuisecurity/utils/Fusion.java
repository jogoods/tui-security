package tuisolutions.tuisecurity.utils;

public class Fusion {
    private float x;
    private float y;
    private float z;
    private long t;
    
    public float getZ() {
        return z;
    }
    
    public void setZ(float _z) {
        this.z = _z;
    }
    
    public float getY() {
        return y;
    }
    
    public void setY(float _y) {
        this.y = _y;
    }
    
    public float getX() {
        return x;
    }
    
    public void setX(float _x) {
        this.x = _x;
    }
    
    public void setT(long _t) {
        this.t = _t;
    }
    
    public long getT() {
        return this.t;
    }
    
    public Fusion() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.t = 0;
    }
    
    public Fusion(long t) {
        this.t = t;
    }
    
    public Fusion(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Fusion(float x, float y, float z, long _t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = _t;
    }
    
    public Fusion(float[] data) {
        setData(data);
    }
    
    public Fusion(float[] data, long t) {
        setData(data);
        this.t = t;
    }
    
    public void setData(float[] data) {
        this.x = data[0];
        this.y = data[1];
        this.z = data[2];
    }
    
    public void setData(float[] data, long t) {
        this.setData(data);
        this.t = t;
    }
    
    public void setData(Fusion f) {
        this.x = f.getX();
        this.y = f.getY();
        this.z = f.getZ();
        this.t = f.getT();
    }
    
    public float getDX(Fusion f) {
        return Math.abs(this.getX() - f.getX());
    }
    
    public float getDY(Fusion f) {
        return Math.abs(this.getY() - f.getY());
    }
    
    public float getDZ(Fusion f) {
        return Math.abs(this.getZ() - f.getZ());
    }
    
    public float getDT(Fusion f) {
        return Math.abs(this.getT() - f.getT());
    }
}

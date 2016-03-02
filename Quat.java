package objview;

public class Quat {
    public double w, x, y, z;
    public Quat(double w, Vec v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }
    public Quat(double w, double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Quat(Quat q){
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }
    
    public Quat product(Quat q){
        double w = this.w*q.w - this.x*q.x - this.y*q.y - this.z*q.z;
        double x = this.w*q.x + this.x*q.w + this.y*q.z - this.z*q.y;
        double y = this.w*q.y - this.x*q.z + this.y*q.w + this.z*q.x;
        double z = this.w*q.z + this.x*q.y - this.y*q.x + this.z*q.w;
        return new Quat(w, x, y, z);
    }
    public void print(){
        System.out.println("["+w+", "+x+", "+y+", "+z+"]");
    }
    
    public Quat conj(){
        return new Quat(this.w, -this.x, -this.y, -this.z);
    }
    public Vec toVec(){
        return new Vec(this.x, this.y, this.z);
    }
}

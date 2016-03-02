
package objview;

public class Vec {
    public double x, y, z;
    
    public Vec(){
        this.x = this.y = this.z = 0;
    }
    public Vec(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec(Vec v){
        x = v.x;
        y = v.y;
        z = v.z;
    }
    
    /**
     * Creates a normalized vector using 2 parameters - pitch and yaw
     * @param pitch Pitch of direction vector
     * @param yaw Yaw of direction vector
     */
    public Vec(double pitch, double yaw){
        double xyLen = Math.cos(pitch);
        x = xyLen * Math.cos(yaw);
        y = xyLen * Math.sin(-yaw);
        z = Math.sin(pitch);
    }
    
    public Vec add(Vec v){
        return new Vec(this.x+v.x, this.y+v.y, this.z+v.z);
    }
    public Vec sub(Vec v){
        return new Vec(this.x-v.x, this.y-v.y, this.z-v.z);
    }
    public Vec mul(double n){
        return new Vec(this.x*n, this.y*n, this.z*n);
    }
    public Vec div(double n){
        return new Vec(this.x/n, this.y/n, this.z/n);
    }
    public Vec neg(){
        return new Vec(-this.x, -this.y, -this.z);
    }
    public boolean eq(Vec v){
        return this.x==v.x && this.y==v.y && this.z==v.z;
    }
    public void print(){
        System.out.println("["+this.x+", "+this.y+", "+this.z+"]");
    }
    
    public double angle(Vec v){
        return Math.acos(this.dot(v)/(this.length()*v.length()));
    }
    public double length(){
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    public Vec normalize(){
        double len = this.length();
        return new Vec(this.x/len, this.y/len, this.z/len);
    }
    public double dot(Vec v){
        return this.x*v.x + this.y*v.y + this.z*v.z;
    }
    public Vec cross(Vec v){
        double x = this.y*v.z - v.y*this.z;
        double y = this.z*v.x - v.z*this.x;
        double z = this.x*v.y - v.x*this.y;
        return new Vec(x, y, z).normalize();
    }
    public double planeDist(Vec pos, Vec norm){
        return norm.dot(this.sub(pos))/norm.length();
    }
    public Vec rotateAroundAxis(Vec axis, double ang){
        Quat q = new Quat(0, this);
        Quat rot = new Quat(Math.cos(ang/2), Math.sin(ang/2)*axis.x, Math.sin(ang/2)*axis.y, Math.sin(ang/2)*axis.z);
        return rot.product(q).product(rot.conj()).toVec();
    }
    public double distance(Vec v){
        return Math.sqrt(Math.pow(v.x-this.x, 2)+Math.pow(v.y-this.y, 2)+Math.pow(v.z-this.z, 2));
    }
}

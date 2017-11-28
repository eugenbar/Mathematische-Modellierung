public class Vertex {
    private double x,y,z,tx,ty,tz;
    public Vertex(double x,double y,double z){
        this.x = x;this.y=y;this.z=z;
    }
    public double getX(){
        return this.x;
    }
    public void setT(double tx,double ty,double tz){
        this.tx=tx;this.ty=ty;this.tz=tz;
    }
    public void setTx(double tx){
        this.tx = tx;
    }
    public void setTy(double ty){
        this.ty=ty;
    }
    public void setTz(double tz){
        this.tz=tz;
    }
    public double getTx(){
        return tx;
    }
    public double getTy(){
        return ty;
    }
    public double getTz(){
        return tz;
    }

    public double getY(){
        return this.y;
    }

    public double getZ(){
        return this.z;
    }

    public void setP(double x,double y,double z){
        this.x = x;this.y=y;this.z=z;
    }
    public void setX(double x){
        this.x=x;
    }
    public void setY(double y){
        this.y=y;
    }
    public void setZ(double z){
        this.z=z;
    }
}

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Object3D extends RenderEngine{
    protected List<Vertex> vL = new ArrayList<>();
    protected List<Integer> pL = new ArrayList<>();
    protected List<Color> cL = new ArrayList<>();
    protected List<Color> cTL = new ArrayList<>();
    public Vertex lightSource;
    public double lightSourceScalar;
    public boolean wire = false;


    private double tx,ty,tz,sx,sy,sz,dx,dy,dz;
    public int width,height;
    public double movingX,movingY,movingZ,rotatingX,rotatingY,rotatingZ;
    public Object3D(int width,int height){
        tx=0;ty=0;tz=0;dx=0;dy=0;dz=0;
        tx+=width/2;ty+=height/2;
        this.width=width;this.height=height;
        sx=1;sy=1;sz=1;
        movingX=0;movingY=0;movingZ=0;rotatingX=0;rotatingY=0;rotatingZ=0;
        lightSource = new Vertex(0,0,1);
        setLightSourceScalar();

    }
    public void addV(Vertex v){
        vL.add(v);
    }

    public void addP(int a,int b, int c,Color color){
        pL.add(a);pL.add(b);pL.add(c);
        cL.add(color);
        cTL.add(color);


    }

    public boolean checkNormal(int p){
        double vx,vy,vz,ux,uy,uz,nx,ny,nz;
        vx = vL.get(pL.get(p+2)).getTx()-vL.get(pL.get(p)).getTx();
        vy = vL.get(pL.get(p+2)).getTy()-vL.get(pL.get(p)).getTy();
        vz = vL.get(pL.get(p+2)).getTz()-vL.get(pL.get(p)).getTz();
        ux = vL.get(pL.get(p+1)).getTx()-vL.get(pL.get(p)).getTx();
        uy = vL.get(pL.get(p+1)).getTy()-vL.get(pL.get(p)).getTy();
        uz = vL.get(pL.get(p+1)).getTz()-vL.get(pL.get(p)).getTz();
        nx = vy*uz-vz*uy;
        ny = vz*ux-vx*uz;
        nz = vx*uy-vy*ux;
        //  nz*=-1;ny*=-1;nx*=-1;
        double nnz=nz;
        double nScalar = Math.sqrt(nx*nx+ny*ny+nz*nz);
        /*nx += vL.get(pL.get(p+2)).getTx();
        ny += vL.get(pL.get(p+2)).getTy();
        nz += vL.get(pL.get(p+2)).getTz();*/

        Vertex view = new Vertex(0,0,-1);/*(vL.get(pL.get(p)).getTx(),vL.get(pL.get(p)).getTy(),
                vL.get(pL.get(p)).getTz()-200);*/
        double viewS = Math.sqrt((view.getX()*view.getX()+view.getY()*view.getY()+
                view.getZ()*view.getZ()));

        if (((nx*view.getX()+ny*view.getY()+nz*view.getZ())/
                (nScalar*viewS))>0) {
            //nScalar=Math.sqrt(nx*nx+ny*ny+nz*nz);
            Vertex dl = new Vertex(lightSource.getX()-vL.get(pL.get(p)).getTx(),
                    lightSource.getY()-vL.get(pL.get(p)).getTy(),
                    lightSource.getZ()-vL.get(pL.get(p)).getTz());
            double dlS = Math.sqrt(dl.getX()*dl.getX()+dl.getY()*dl.getY()+
                    dl.getZ()*dl.getZ());
            double ang = (((nx*dl.getX()+ny*dl.getY()+
                    nz*dl.getZ())/(dlS*nScalar)));

            Color c = cL.get(p/3);
            ang*=-1;
            if(ang<0)ang=0;
            cTL.set(p / 3, getShade(c,ang));

            return true;

        }
        else return false;
    }
    public void setLightSourceScalar(){
        this.lightSourceScalar = Math.sqrt(lightSource.getX()*lightSource.getX()+
                lightSource.getY()*lightSource.getY()+lightSource.getZ()*lightSource.getZ());
    }


    public void loadFile(String source) throws FileNotFoundException {
        Scanner scanner;
        File f = new File(source);
        scanner = new Scanner(f);
        String s="";
        String[] sA = new String[5];
        sA[0]="";
        while (scanner.hasNextLine() && !s.equals("end_header")){
            s=scanner.nextLine();
        }
        while (scanner.hasNextLine()&& !sA[0].equals("3")){
            s=scanner.nextLine();
            sA = s.split(" ");
            vL.add(new Vertex(Double.parseDouble(sA[0]),
                    Double.parseDouble(sA[1]),
                    Double.parseDouble(sA[2])));
            // System.out.println(vL.get(vL.size()-1).getX());
        }
        Color c = new Color(1f,0f,0f);
        c = Color.MAGENTA;
        while (scanner.hasNextLine()){
            addP(Integer.parseInt(sA[1]),
                    Integer.parseInt(sA[2]),
                    Integer.parseInt(sA[3]),
                    c);
            //System.out.println(pL.get(pL.size()-3)+" "+pL.get(pL.size()-2)+" "+pL.get(pL.size()-1));
            s=scanner.nextLine();
            sA = s.split(" ");
        }
        addP(Integer.parseInt(sA[1]),
                Integer.parseInt(sA[2]),
                Integer.parseInt(sA[3]),
                c);
        scanner.close();




    }

    public void addObject(){

        addV(new Vertex(-50,-50,-50));
        addV(new Vertex(-50,50,-50));
        addV(new Vertex(50,50,-50));
        addV(new Vertex(50,-50,-50));

        addV(new Vertex(-50,-50,50));
        addV(new Vertex(50,-50,50));
        addV(new Vertex(50,50,50));
        addV(new Vertex(-50,50,50));
        Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(4,6,7,c);
        addP(4,5,6,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(0,1,2,c);
        addP(2,3,0,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(0,4,7,c);
        addP(7,1,0,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(6,5,3,c);
        addP(3,2,6,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(0,3,5,c);
        addP(5,4,0,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(7,6,1,c);
        addP(1,6,2,c);
        //scale(10,10,10);
       // translate(0,0,-500);

    }

    private void checkBoundaries(){
        /*for(Vertex v:vL){
            if((v.getX()+tx)>width && movingX > 0){
                movingX*=-1;
            }
            else if((v.getX()+tx)<0 && movingX < 0){
                movingX*=-1;
            }
            else if((v.getY()+ty)>height && movingY > 0){
                movingY*=-1;
            }
            else if((v.getY()+ty)<0 && movingY < 0){
                movingY*=-1;
            }
            else if((v.getZ()+tz)<-3000 && movingZ <0){
                movingZ*=-1;
            }
            else if((v.getZ()+tz)>-250 && movingZ >0){
                movingZ*=-1;
            }
            *//*if((v.getTx())>width && movingX > 0){
                movingX*=-1;
            }
            else if((v.getTx())<0 && movingX < 0){
                movingX*=-1;
            }
            else if((v.getTy())>height && movingY > 0){
                movingY*=-1;
            }
            else if((v.getTy())<0 && movingY < 0){
                movingY*=-1;
            }
            else if((v.getTz())<-3000 && movingZ <0){
                movingZ*=-1;
            }
            else if((v.getTz())>-250 && movingZ >0){
                movingZ*=-1;
            }*//*
        }*/
    }
    public void render(Graphics2D g2d, BufferedImage bf){
        System.out.println(vL.get(0).getTx()+ " "+vL.get(0).getTy()+" "+
                vL.get(0).getTz());
        transformV();
        System.out.println(vL.get(0).getTx()+ " "+vL.get(0).getTy()+" "+
                vL.get(0).getTz());
        Polygon p = new Polygon();
       /* p.addPoint(500,100);
        p.addPoint(300,300);
        p.addPoint(600,500);
        rasterizePolygon(p,bf,Color.CYAN);
        g2d.drawImage(bf,0,0,null);*/
        for(int i =0;i<pL.size();i+=3){
            if(wire) {
                g2d.setColor(cL.get(i / 3));
                g2d.drawLine((int) vL.get(pL.get(i)).getTx(),
                        (int) vL.get(pL.get(i)).getTy(),
                        (int) vL.get(pL.get(i + 1)).getTx(),
                        (int) vL.get(pL.get(i + 1)).getTy());
                g2d.drawLine((int) vL.get(pL.get(i)).getTx(),
                        (int) vL.get(pL.get(i)).getTy(),
                        (int) vL.get(pL.get(i + 2)).getTx(),
                        (int) vL.get(pL.get(i + 2)).getTy());
                g2d.drawLine((int) vL.get(pL.get(i + 2)).getTx(),
                        (int) vL.get(pL.get(i + 2)).getTy(),
                        (int) vL.get(pL.get(i + 1)).getTx(),
                        (int) vL.get(pL.get(i + 1)).getTy());
            }else if(checkNormal(i)) {
                p = new Polygon();
                p.addPoint((int) vL.get(pL.get(i)).getTx(), (int) vL.get(pL.get(i)).getTy());
                p.addPoint((int) vL.get(pL.get(i + 1)).getTx(), (int) vL.get(pL.get(i + 1)).getTy());
                p.addPoint((int) vL.get(pL.get(i + 2)).getTx(), (int) vL.get(pL.get(i + 2)).getTy());
                //rasterizePolygon(p,bf, cTL.get(i / 3));

                g2d.setColor(cTL.get(i / 3));
                g2d.fillPolygon(p);
                //g2d.fillRect(0,0,800,800);
            }

        }
        //g2d.drawImage(bf, 0, 0, width, height, null);


    }



    private void transformV() {
        for (int i = 0; i < vL.size(); i++) {
            double trX = vL.get(i).getX();
            double trY = vL.get(i).getY();
            double trZ = vL.get(i).getZ();
            System.out.println(vL.get(0).getTx()+ " "+vL.get(0).getTy()+" "+
                    vL.get(0).getTz());
            trX *= sx;
            trY *= sy;//(1+tz/5000)*//*;
            trZ *= sz;

            double ttrX = trX * Math.cos(dz) + trY * Math.sin(dz);
            double ttrY = -trX * Math.sin(dz) + trY * Math.cos(dz);
            double ttrZ = trZ;
            trX = ttrX;
            trY = ttrY;
            ttrX = trX * Math.cos(dy) - trZ * Math.sin(dy);
            ttrZ = trX * Math.sin(dy) + trZ * Math.cos(dy);
            ttrZ += tz;


            ttrX = ttrX / (0.005 * ttrZ);
            ttrY = ttrY / (0.005 * ttrZ);




            ttrX += tx;
            ttrY += ty;

            /*trX = trX*height*0.5/(-trZ*width*Math.tan(Math.PI/7));//+width*trZ/2;
            trY = trY/(-trZ*Math.tan(Math.PI/7));// + height*trZ/2;
            trZ = (trZ-1)/trZ;*/

            trX = ttrX;trY=ttrY;
            vL.get(i).setT(trX, trY, trZ);
            checkBoundaries();
            //int[] tr = {(int) Math.round(ttrX),//+ttrZ
            //      (int) Math.round(ttrY),//+ttrZ
            //    (int) Math.round(ttrZ)};
            //return tr;
        }
    }

    public void scale(double sx,double sy, double sz){

        this.sx*=sx;this.sy*=sy;this.sz*=sz;
    }
    public void translate(double tx,double ty,double tz){
        //checkBoundaries();
        this.tx+=tx;this.ty+=ty;this.tz+=tz;

    }
    public void setNewPosition(double tx,double ty,double tz){
        this.tx = tx;this.ty=ty;this.tz=tz;
    }
    public void rotate(double dx,double dy,double dz){
        this.dx+=dx;this.dy+=dy;this.dz+=dz;
    }


}

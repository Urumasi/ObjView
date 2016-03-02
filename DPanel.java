package objview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener{
    private boolean setTitle = true;
    private double scale = 100;
    private Vec camDir;
    private double pitch=-30, yaw=45;
    private Obj o;
    private List<Point> verts = new ArrayList();
    private List<Point[]> lines = new ArrayList();
    private List<Color> colors = new ArrayList();
    private List<int[][]> faces = new ArrayList();
    private Dimension size;
    private Timer t = new Timer(50, this);
    private JFrame frame;
    private Point dragStart;
    
    private boolean showPoints=false, showLines=false, showFaces=true;
    private double sensitivity=0.5;
    
    
    public DPanel(JFrame jf, Dimension size, Obj obj){
        frame = jf;
        this.size = size;
        this.setPreferredSize(size);
        this.setBackground(new Color(128,128,128));
        this.o = obj;
        scale = Math.min(size.height, size.width)/o.maxLen/2;
        t.start();
        addMouseListener(this);
        setFocusable(true);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        upDir();
    }
    
    public void reload(){
        scale = Math.min(size.height, size.width)/o.maxLen/2;
        pitch=-30;
        yaw=45;
        upDir();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(showFaces){
            for(int i=0; i<faces.size(); i++){
                g.setColor(colors.get(i));
                int[][] face = faces.get(i);
                g.fillPolygon(face[0], face[1], face[0].length);
            }
        }
        g.setColor(Color.WHITE);
        if(showLines){
            for(Point[] line : lines){
                g.drawLine(line[0].x, line[0].y, line[1].x, line[1].y);
            }
        }
        if(showPoints){
            for(Point vert : verts){
                g.drawLine(vert.x-3, vert.y, vert.x+3, vert.y);
                g.drawLine(vert.x, vert.y-3, vert.x, vert.y+3);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(setTitle){
            frame.setTitle("ObjView - "+o.getVerts().size()+" verts "+o.getFaces().size()+" faces");
            setTitle = true;
        }
        verts = new ArrayList();
        for(Vec vert : o.getVerts()){
            Vec hit = vert.add(camDir.mul(vert.planeDist(camDir, camDir)));
            Vec rotated = hit.rotateAroundAxis(new Vec(0,0,1), Math.toRadians(yaw)).rotateAroundAxis(new Vec(0,1,0), Math.toRadians(pitch));
            verts.add(new Point((int)(rotated.y*scale)+this.getSize().width/2, (int)(rotated.z*scale)+this.getSize().height/2));
        }
        if(showLines){
            lines = new ArrayList();
            for(List<Integer> face : o.getIdFaces()){
                for(int i=0; i<face.size(); i++){
                    Point[] line = new Point[2];
                    line[0] = verts.get(face.get(i)-1);
                    line[1] = verts.get(face.get((i+1)%face.size())-1);
                    lines.add(line);
                }
            }
        }
        if(showFaces){
            Vec somePos = camDir.mul(1e6);
            faces = new ArrayList();
            colors = new ArrayList();
            List<Face> facelU = o.getFaces();
            List<List<Integer>> idfcU = o.getIdFaces();
            List<Face> facel = new ArrayList();
            List<List<Integer>> idfc = new ArrayList();
            while(facelU.size()>0){
                int max = 0;
                double maxd = 0;
                for(int j=0; j<facelU.size(); j++){
                    Face face = facelU.get(j);
                    Vec center = new Vec(face.center);
                    if(center.distance(somePos)>maxd){
                        maxd = center.distance(somePos);
                        max = j;
                    }
                }
                facel.add(facelU.remove(max));
                idfc.add(idfcU.remove(max));
            }
            for(int id=0; id<idfc.size(); id++){
                List<Integer> face = idfc.get(id);
                if(facel.get(id).normal.dot(camDir)>=0){
                    int[][] nface = new int[2][face.size()];
                    for(int i=0; i<face.size(); i++){
                        nface[0][i] = verts.get(face.get(i)-1).x;
                        nface[1][i] = verts.get(face.get(i)-1).y;
                    }
                    faces.add(nface);
                    int shade = (int) (facel.get(id).normal.dot(camDir)*100+155);
                    colors.add(new Color(shade, shade, shade));
                    
                }
            }
            
        }
        repaint();
    }
    
    private void upDir(){
        double pt = Math.toRadians(pitch);
        double yw = Math.toRadians(yaw);
        double xyLen = Math.cos(pt);
        double x = xyLen * Math.cos(yw);
        double y = xyLen * Math.sin(-yw);
        double z = Math.sin(pt);
        camDir = new Vec(x, y, z).normalize();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        yaw = (yaw+(e.getX()-dragStart.x)*sensitivity+360)%360;
        pitch = Math.max(Math.min((pitch-(e.getY()-dragStart.y)*sensitivity), 90), -90);
        upDir();
        dragStart = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if(mwe.getWheelRotation()==1){
            scale *= 0.95;
        }else{
            scale *= 1.05;
        }
    }
}

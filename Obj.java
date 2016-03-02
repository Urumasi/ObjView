package objview;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class Obj {
    private List<Vec> verts = new ArrayList();
    private List<Vec> norms = new ArrayList();
    private List<Face> faces = new ArrayList();
    private List<List<Integer>> faceids = new ArrayList();
    public double maxLen = 0;
    
    public Obj(){}
    
    public void setObj(String path){
        verts = new ArrayList();
        norms = new ArrayList();
        faces = new ArrayList();
        faceids = new ArrayList();
        try {
            String[] t = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8).split("\n");
            for(String s : t){
                if(s.startsWith("#") | s.length()<2) // Comment
                    continue;
                s = s.replaceAll("  ", " ");
                String[] d = s.split(" ");
                if("v".equals(d[0])){ // Vertex
                    verts.add(new Vec(Double.parseDouble(d[1]), Double.parseDouble(d[3]), -Double.parseDouble(d[2])));
                    maxLen = Math.max(maxLen, verts.get(verts.size()-1).distance(new Vec()));
                }
                if("vn".equals(d[0])){ // Face normal
                    norms.add(new Vec(Double.parseDouble(d[1]), Double.parseDouble(d[3]), -Double.parseDouble(d[2])));
                }
                if("f".equals(d[0])){
                    Face face = new Face();
                    List<Integer> faceid = new ArrayList();
                    for(int i=1; i<d.length; i++){
                        if(d[i].trim().isEmpty())
                            continue;
                        String[] f = d[i].trim().split("/");
                        face.verts.add(new Vec(verts.get(Integer.parseInt(f[0])-1)));
                        faceid.add(Integer.parseInt(f[0]));
                        if(i==d.length-1){
                            if(f.length==3)
                                face.normal = new Vec(norms.get(Integer.parseInt(f[2])-1));
                            else
                                face.normal = face.verts.get(1).sub(face.verts.get(0)).cross(face.verts.get(2).sub(face.verts.get(0)));
                        }
                    }
                    System.out.println(faces.size()+" "+faceid.size());
                    Vec cent = new Vec();
                    for(Vec vc : face.verts)
                        cent = cent.add(vc);
                    cent.div(face.verts.size());
                    face.center = new Vec(cent);
                    faces.add(face);
                    faceids.add(faceid);
                }
            }
            
        } catch (IOException ex){}
    }
    
    public List<Vec> getVerts(){
        return new ArrayList(verts);
    }
    
    public List<List<Integer>> getIdFaces(){
        return new ArrayList(faceids);
    }
    
    public List<Face> getFaces(){
        return new ArrayList(faces);
    }
}

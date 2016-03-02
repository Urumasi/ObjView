package objview;

import java.util.ArrayList;
import java.util.List;

public class Face {
    public List<Vec> verts;
    public Vec normal;
    public Vec center;
    
    public Face(){
        verts = new ArrayList();
        normal = new Vec();
        center = new Vec();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author student
 */
public class ObjView extends JFrame{
    private Obj obj;
    private JMenuBar menu;
    private JMenu file;
    private JMenuItem open;
    private JMenuItem exit;
    private JPopupMenu.Separator sep;
    
    public ObjView(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ObjView");
        obj = new Obj();
        DPanel p = new DPanel(this, new Dimension(800,800), obj);
        this.add(p);
        
        menu = new JMenuBar();
        file = new JMenu("File");
        open = new JMenuItem("Open...");
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(ObjView.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Object files", "obj");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(p);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    obj.setObj(chooser.getSelectedFile().getAbsolutePath());
                    p.reload();
                }
            }
        });
        
        exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        
        sep = new JPopupMenu.Separator();
        file.add(open);
        file.add(sep);
        file.add(exit);
        menu.add(file);
        this.setJMenuBar(menu);
        
        this.pack();
        /*obj.setObj("");
        p.reload();*/
    }
    
    public static void main(String[] args) {
        ObjView okno = new ObjView();
        okno.setVisible(true);
    }
}

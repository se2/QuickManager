package kulcomponent;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Dam Linh
 */
public class KulImageChooser extends JFileChooser {

    public KulImageChooser() {
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(new ImageFileFilter());
    }

    @Override
    public void approveSelection() {
        File f = getSelectedFile();
        if (!f.exists() && getDialogType() == OPEN_DIALOG) {
            JOptionPane.showMessageDialog(this, "File Not Found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        super.approveSelection();
    }

    private class ImageFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String[] temp = f.getName().split("\\.");
            String ext = temp[temp.length - 1];
            switch (ext) {
                case "jpg":
                case "gif":
                case "png":
                case "jpeg":
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public String getDescription() {
            return "Image Only";
        }
    }
}

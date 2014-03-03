package kulcomponent;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Dam Linh
 */
public class KulCSVFileChooser extends JFileChooser {

    public KulCSVFileChooser() {
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(new KulCSVFileChooser.CSVFileFilter());
    }

    @Override
    public void approveSelection() {
        File f = getSelectedFile();
        if (f.exists() && getDialogType() != OPEN_DIALOG) {
            int result = JOptionPane.showConfirmDialog(this,
                    "File already exists. Do you want to overwrite?",
                    "File exists", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.CANCEL_OPTION:
                    cancelSelection();
                    return;
                default:
                    return;
            }
        } else if (!f.exists() && getDialogType() == OPEN_DIALOG) {
            JOptionPane.showMessageDialog(this, "File Not Found!", "File Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        super.approveSelection();
    }

    @Override
    public File getSelectedFile() {
        if (super.getSelectedFile() == null) {
            return null;
        } else {
            return appendExtension(super.getSelectedFile(), "csv");
        }
    }

    private class CSVFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String[] temp = f.getName().split("\\.");
            String ext = temp[temp.length - 1];
            switch (ext) {
                case "csv":
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public String getDescription() {
            return "csv";
        }
    }

    private File appendExtension(File file, String fileExtension) {
        String[] temp = file.getName().split("\\.");
        String ext = temp[temp.length - 1];
        if (!ext.equalsIgnoreCase(fileExtension)) {
            file = new File(file.getPath() + "." + fileExtension);
        }
        return file;
    }
}

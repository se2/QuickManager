package viewcontroller;

/**
 *
 * @author Dam Linh
 *
 * All classes(such as: ClassForm, TeacherForm, EnrollForm) that implements this
 * interface will have the below method. Those classes call some methods in the
 * mainFrame to display new frames for user to select something (classes,
 * teachers,..) Then after choosing, those new frames will call this method to
 * return the objects that the classes need. Therefore, there will be
 * interaction between those frame
 *
 * This interface is copy-righted LOL
 */
public interface HasReturn {

    public void setReturnObj(Object o);
}

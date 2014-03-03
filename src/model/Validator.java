package model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.LocalDate;

/**
 *
 * @author S3275145-Hoang Ngoc Thanh
 */
public class Validator implements Serializable {

    private static Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*"
            + "@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z0-9]{2,})$";
    private static final String NAME_PATTERN = "^[0-9a-zA-Z]+$";
    private static final String PASSWOED_PARTTEN = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#*$%&]).*$";

    //Validate email
    public static boolean emailValidate(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean usernameValidate(String username) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean passwordValidate(String password) {
        Pattern pattern = Pattern.compile(PASSWOED_PARTTEN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //Validate Teacher's skills and class's major
    public static boolean teacherClassValidate(String[][] skills, ClassType ct) {

        for (int i = 0; i < skills.length; i++) {
            if (skills[i][0].equals(ct.getSkill())) {
                return true;
            }
        }
        return false;
    }

    /**
     * validate 2 classes times (time only, room is not checked here and there
     * is no need to check room) to check those classes' times conflict or not
     */
    public static boolean validateClassTime(Class c1, Class c2, School model) {
        Iterator<Map.Entry<String, ClassSession>> iterSes1 = model.getSessions().entrySet().iterator();
        while (iterSes1.hasNext()) {
            ClassSession ses1 = iterSes1.next().getValue();
            if (ses1.getClassId().equals(c1.getClassId())) {
                Iterator<Map.Entry<String, ClassSession>> iterSes2 = model.getSessions().entrySet().iterator();
                while (iterSes2.hasNext()) {
                    ClassSession ses2 = iterSes2.next().getValue();
                    if (ses2.getClassId().equals(c2.getClassId())) {
                        if (!validateSessionTime(ses1, ses2)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /*
     * validate 2 sesion time to check those sessions' times conflict or not
     */
    public static boolean validateSessionTime(ClassSession ses1, ClassSession ses2) {
        // smallest and biggest 1
        Iterator<Integer> subSubi1 = ses1.getTimes().keySet().iterator();
        int smallest1 = subSubi1.next();
        int biggest1 = smallest1;
        while (subSubi1.hasNext()) {
            int i = subSubi1.next();
            if (smallest1 > i) {
                smallest1 = i;
            }
            if (biggest1 < i) {
                biggest1 = i;
            }
        }

        // smallest and biggest 2
        Iterator<Integer> subSubi2 = ses2.getTimes().keySet().iterator();
        int smallest2 = subSubi2.next();
        int biggest2 = smallest2;
        while (subSubi2.hasNext()) {
            int i = subSubi2.next();
            if (smallest2 > i) {
                smallest2 = i;
            }
            if (biggest2 < i) {
                biggest2 = i;
            }
        }
        if ((smallest1 >= smallest2 && smallest1 <= biggest2)
                || (biggest1 <= biggest2 && biggest1 > smallest2)) {
            return false;
        }
        return true;
    }

    /*
     * validate teacher's timetable with the session to check if the teacher has
     * timetable conflict with this session or not. The classStartDate and classEndDate
     * are to check the start date and end date of the class that has this session(ses2)
     */
    public static boolean validateTeacherAndClassTime(School model, Teacher t, ClassSession ses2, LocalDate classStartDate, LocalDate classEndDate) {
        Iterator<Entry<String, TeacherClass>> iterTC = model.getTeacherClass().entrySet().iterator();
        while (iterTC.hasNext()) {
            TeacherClass tc = iterTC.next().getValue();

            if (tc.getTeacherId().equals(t.getId())) {
                Class c = model.getClasses().get(tc.getClassId());
                if ((classStartDate.compareTo(c.getStartDate()) >= 0 && classStartDate.compareTo(c.getEndDate()) <= 0)
                        || (classEndDate.compareTo(c.getEndDate()) <= 0 && classEndDate.compareTo(c.getStartDate()) >= 0)) {

                    Iterator<Entry<String, ClassSession>> iterSes = model.getSessions().entrySet().iterator();

                    while (iterSes.hasNext()) {
                        ClassSession ses1 = iterSes.next().getValue();
                        if (ses1.getClassId().equals(tc.getClassId())
                                && !ses1.getClassId().equals(ses2.getClassId())) {
                            if (!validateSessionTime(ses1, ses2)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

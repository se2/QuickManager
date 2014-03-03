package model;

import java.io.Serializable;

/**
 *
 * @author Dam Linh
 */
public class TeacherClass implements Serializable {

    private static int currentId;
    private String id;
    private String teacherId;
    private String classId;

    public TeacherClass(String teacherId, String classId) {
        this.id = "TC" + currentId;
        this.teacherId = teacherId;
        this.classId = classId;
        currentId++;
    }

    public TeacherClass(String id, String teacherId, String classId) {
        this.id = id;
        this.teacherId = teacherId;
        this.classId = classId;
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        TeacherClass.currentId = currentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String printDetail() {
        String output;
        output = id + "\t,"
                + teacherId + "\t,"
                + classId + "\n";

        return output;
    }
}

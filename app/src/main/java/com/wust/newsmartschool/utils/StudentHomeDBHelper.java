package com.wust.newsmartschool.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class StudentHomeDBHelper extends SQLiteOpenHelper
{
    public final static String DB_NAME = "_Student_Home.db";
    public static final String TABLES_NAME_COURSETABLE = "coursetable";
    public static final String TABLES_NAME_GRADETABLE = "gradetable";

    private SQLiteDatabase sqLiteDatabase;
    private Context mContext;

    public StudentHomeDBHelper(Context context, int version)
    {

        super(context, GlobalVar.userid + DB_NAME, null, version);
        Log.d("cbh", GlobalVar.userid);
        // TODO Auto-generated constructor stub
        mContext = context;
        openDB(false);
    }

    class BaseColumn
    {
        public static final String ID = "ID";
    }

    public class CourseTableColumn extends BaseColumn
    {
        /** 学期 */
        public static final String SEMESTER = "semester";
        /** 课程信息 */
        public static final String COURSE = "courseJson";
    }
    public class GradeTableColumn extends BaseColumn
    {
        /** 学期 */
        public static final String SEMESTER = "semester";
        /** 成绩信息 */
        public static final String GRADE = "gradeJson";
    }

    public void openDB(boolean isReadonly)
    {
        if (isReadonly)
        {
            sqLiteDatabase = this.getReadableDatabase();
        } else
        {
            sqLiteDatabase = this.getWritableDatabase();
        }
    }

    public final void reopen()
    {
        closeDB();
        openDB(false);
    }

    private void closeDB()
    {
        if (sqLiteDatabase != null)
        {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        createTables(db);
    }

    private void createTables(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        createTableForCourseTable(db);
        createTableForGradeTable(db);

    }

    /**
     * 创建成绩信息表
     *
     * @param db
     */
    private void createTableForGradeTable(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLES_NAME_GRADETABLE
                + " (" + GradeTableColumn.ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GradeTableColumn.SEMESTER
                + " TEXT UNIQUE ON CONFLICT ABORT, " + GradeTableColumn.GRADE
                + " TEXT)";
        db.execSQL(sql);
    }

    /**
     * 创建课程信息表
     *
     * @param db
     */
    private void createTableForCourseTable(SQLiteDatabase db)
    {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLES_NAME_COURSETABLE
                + " (" + CourseTableColumn.ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CourseTableColumn.SEMESTER
                + " TEXT UNIQUE ON CONFLICT ABORT, " + CourseTableColumn.COURSE
                + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub

    }

    public synchronized void InitStudentCourse(ArrayList<String> semesters)
    {

        for (int i = 0; i < semesters.size(); i++)
        {
            InsertCourse(semesters.get(i));
        }
    }
    public void InitStudentGrade(ArrayList<String> semesters)
    {

        for (int i = 0; i < semesters.size(); i++)
        {
            InsertGrade(semesters.get(i));
        }
    }

    public String getCourseBySemester(String semester)
    {
        String string = null;
        if (sqLiteDatabase == null)
        {
            openDB(true);
        }
        String sql = "SELECT * FROM " + TABLES_NAME_COURSETABLE + " WHERE "
                + CourseTableColumn.SEMESTER + " = ?";
        Log.d("semester", semester);
        Log.d("sql1", sql);
        String[] args = new String[]
                { semester };
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        if (cursor != null)
        {
            // cursor.moveToFirst();
            while (cursor.moveToNext())
            {
                string = cursor.getString(cursor
                        .getColumnIndexOrThrow(CourseTableColumn.COURSE));
                if (string != null)
                {

                    Log.d("result", string);
                }
            }
        }
        closeDB();
        return string;
    }

    public String getGradeBySemester(String semester)
    {
        String string = null;
        if (sqLiteDatabase == null)
        {
            openDB(true);
        }
        String sql = "SELECT * FROM " + TABLES_NAME_GRADETABLE + " WHERE "
                + GradeTableColumn.SEMESTER + " = ?";
        Log.d("semester", semester);
        Log.d("sql1", sql);
        String[] args = new String[]
                { semester };
        Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
        if (cursor != null)
        {
            // cursor.moveToFirst();
            while (cursor.moveToNext())
            {
                string = cursor.getString(cursor
                        .getColumnIndexOrThrow(GradeTableColumn.GRADE));
                if (string != null)
                {

                    Log.d("result", string);
                }
            }
        }
        closeDB();
        return string;
    }

    public ArrayList<String> getCourses()
    {
        ArrayList<String> semesters = null;
        if (sqLiteDatabase == null)
        {
            openDB(true);
        }
        String sql = "SELECT * FROM " + TABLES_NAME_COURSETABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor != null)
        {
            semesters = new ArrayList<String>();
            while (cursor.moveToNext())
            {
                String string = cursor.getString(cursor
                        .getColumnIndexOrThrow(CourseTableColumn.SEMESTER));
                semesters.add(string);
            }

        }

        closeDB();
        return semesters;
    }

    public ArrayList<String> getGrades()
    {
        ArrayList<String> semesters = null;
        if (sqLiteDatabase == null)
        {
            openDB(true);
        }
        String sql = "SELECT * FROM " + TABLES_NAME_GRADETABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor != null)
        {
            semesters = new ArrayList<String>();
            while (cursor.moveToNext())
            {
                String string = cursor.getString(cursor
                        .getColumnIndexOrThrow(CourseTableColumn.SEMESTER));
                semesters.add(string);
            }

        }

        closeDB();
        return semesters;
    }

    public void InsertCourse(String semester)
    {
        if (sqLiteDatabase == null)
        {
            openDB(false);
        }
        ContentValues cv = new ContentValues();
        cv.put(CourseTableColumn.SEMESTER, semester);
        sqLiteDatabase.insert(TABLES_NAME_COURSETABLE, null, cv);
        closeDB();
    }

    public void InsertGrade(String semester)
    {
        if (sqLiteDatabase == null)
        {
            openDB(false);
        }
        ContentValues cv = new ContentValues();
        cv.put(GradeTableColumn.SEMESTER, semester);
        sqLiteDatabase.insert(TABLES_NAME_GRADETABLE, null, cv);
        closeDB();
    }

    public synchronized void InsertCourseInfoAtSemester(String semester, String courseInfo)
    {
        Log.d("aa", "InsertCourseInfoAtSemester");
        Log.d("semester", semester);
        Log.d("courseInfo", courseInfo);
        if (sqLiteDatabase == null)
        {
            openDB(false);
        }
        String sql = "UPDATE " + TABLES_NAME_COURSETABLE + " SET "
                + CourseTableColumn.COURSE + "=?" + " WHERE "
                + CourseTableColumn.SEMESTER + "=?";
        Log.d("sql2", sql);
        Object[] objects = new Object[]
                { courseInfo, semester };
        sqLiteDatabase.execSQL(sql, objects);
        closeDB();
    }

    public void InsertGradeInfoAtSemester(String semester, String courseInfo)
    {
        Log.d("aa", "InsertGradeInfoAtSemester");
        Log.d("semester", semester);
        Log.d("courseInfo", courseInfo);
        if (sqLiteDatabase == null)
        {
            openDB(false);
        }
        String sql = "UPDATE " + TABLES_NAME_GRADETABLE + " SET "
                + GradeTableColumn.GRADE + "=?" + " WHERE "
                + GradeTableColumn.SEMESTER + "=?";
        Log.d("sql2", sql);
        Object[] objects = new Object[]
                { courseInfo, semester };
        sqLiteDatabase.execSQL(sql, objects);
        closeDB();
    }

    public void DeleteAllCourseInfo(ArrayList<String> semesters)
    {
        for (int i = 0; i < semesters.size(); i++)
        {
            DeleteCourse(semesters.get(i));
        }
    }
    public void DeleteAllGradeInfo(ArrayList<String> semesters)
    {
        for (int i = 0; i < semesters.size(); i++)
        {
            DeleteGrade(semesters.get(i));
        }
    }

    private void DeleteCourse(String semester)
    {
        // TODO Auto-generated method stub
        if (sqLiteDatabase==null)
        {
            openDB(false);
        }
        Log.d("Delete_Semester",semester);
        String sql = "DELETE FROM "+TABLES_NAME_COURSETABLE + " WHERE "+CourseTableColumn.SEMESTER+" = ?";
        Log.d("sql3", sql);
        Object[] args = new Object[]{semester};
        sqLiteDatabase.execSQL(sql, args);
        closeDB();
    }

    private void DeleteGrade(String semester)
    {
        // TODO Auto-generated method stub
        if (sqLiteDatabase==null)
        {
            openDB(false);
        }
        Log.d("Delete_Semester",semester);
        String sql = "DELETE FROM "+TABLES_NAME_GRADETABLE + " WHERE "+CourseTableColumn.SEMESTER+" = ?";
        Log.d("sql3", sql);
        Object[] args = new Object[]{semester};
        sqLiteDatabase.execSQL(sql, args);
        closeDB();
    }

}


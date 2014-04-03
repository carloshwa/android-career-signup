package com.doubleencore.careersignup;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by carlos on 4/2/14.
 */
@DatabaseTable(tableName = User.TABLE_NAME)
public class User {
    private static final String TAG = User.class.getSimpleName();
    static final String TABLE_NAME = "user";

    @DatabaseField(generatedId = true, uniqueIndex = true)
    private int id;

    @DatabaseField
    private String email;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String school;

    @DatabaseField
    private String grade;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static User create(final DatabaseHelper helper,
                                      final String firstName, final String lastName, final String email,
                                      final String school, final String grade) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setSchool(school);
        user.setGrade(grade);

        try {
            Dao<User, Integer> dao = helper.getUserDao();
            dao.create(user);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to create/update user: " + email, e);
            return null;
        }

        return user;
    }
}

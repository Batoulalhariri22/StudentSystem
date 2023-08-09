package com.example.SchoolSpring.service;

import com.example.SchoolSpring.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

class  RowMap implements RowMapper<ArrayList<Course>>{
    private final ArrayList<String> coursesNames = new ArrayList<>(
            Arrays.asList(
                    "Biology",
                    "Physics",
                    "Mathematics"
            )
    );
    @Override
    public ArrayList<Course> mapRow(ResultSet resultSet, int i) throws SQLException {
        ArrayList<Course> marks = new ArrayList<>();
        for (String cName : coursesNames) {
            Double mark = resultSet.getDouble(cName);
            marks.add(new Course(cName, mark));
        }
        return marks;
    }
}

@Service
public class DatabaseService {
    private JdbcTemplate jdbcTemplate;

    public DatabaseService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isStudentRegistered(int studentId) {
        String SQL = "SELECT password FROM student WHERE id = ?";
        String password = jdbcTemplate.queryForObject(SQL, new Object[]{studentId}, String.class);
        return (password != null);
    }

    public boolean isValidPassword(int studentId, String password) {
        final String SQL = "SELECT password FROM student WHERE id = ?";
        String correctPassword = jdbcTemplate.queryForObject(SQL, new Object[]{studentId}, String.class);
        return correctPassword.equals(password);
    }

    public ArrayList<Course> getAllCoursesForStudent(int studentId)  {
        final String SQL = "SELECT Biology , Physics, Mathematics FROM mark WHERE id = " + studentId;
        ArrayList<Course> courses = jdbcTemplate.query(SQL , new RowMap()).get(0);
        return courses;
    }

    public Course getCourseForStudent(int studentId, String courseName)  {
        final String SQL = "SELECT " + courseName + " FROM mark WHERE id = ?";
        Double mark = jdbcTemplate.queryForObject(SQL, new Object[]{studentId}, Double.class);
        return new Course(courseName, mark);
    }

    public Course getAvgGrade(String courseName) {
        final String SQL = "SELECT AVG( "+ courseName +" ) FROM mark";
        Double mark = jdbcTemplate.queryForObject(SQL, Double.class);
        return new Course(courseName, mark);
    }

    public Course getMaxGrade(String courseName) {
        final String SQL = "SELECT MAX( "+ courseName +" ) FROM mark";
        Double mark = jdbcTemplate.queryForObject(SQL, Double.class);
        return new Course(courseName, mark);
    }
    public Course getMinGrade(String courseName) {
        final String SQL = "SELECT MIN( "+courseName +" ) FROM mark";
        Double mark = jdbcTemplate.queryForObject(SQL , Double.class);
        System.out.println("Course Mark min : " + courseName +" " + mark);
        return new Course(courseName, mark);
    }

    public Course getMedian(String courseName){
        final String SQL = "SELECT AVG( subq." + courseName + ") as median_value " +
                "FROM (" +
                "    SELECT @row_index:=@row_index + 1 AS row_index, " + courseName +
                "    FROM mark" +
                "    ORDER BY " + courseName +
                "  ) AS subq" +
                "  WHERE subq.row_index " +
                "  IN (FLOOR(@row_index / 2) , CEIL(@row_index / 2))";
        jdbcTemplate.execute("SET @row_index := -1");
        Double mark = jdbcTemplate.queryForObject(SQL , Double.class);
        System.out.println("Course Mark: " + courseName +" " + mark);
        return new Course(courseName, mark);
    }

}

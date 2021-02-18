package service;

import com.sun.corba.se.pept.transport.ConnectionCache;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    public static final String SORT_LIST_BY_NAME = "select * from users order by name asc;";
    private String jdbcURL = "jdbc:mysql://localhost:3306/demo";
    private String jdbcUsername = "root";
    private String jdbcPassword = "hunghip12";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +
            " (?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String FIND_BY_COUNTRY = "select * from users where country = ?;";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

    public UserDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insert(User user) {
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(INSERT_USERS_SQL);
            p.setString(1, user.getName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getCountry());
            System.out.println(p);
            p.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public User select(int id) {
        User user = null;
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(SELECT_USER_BY_ID);
            p.setInt(1, id);
            System.out.println(p);
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id, name, email, country);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAll() {
        List<User> list = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(SELECT_ALL_USERS);
            System.out.println(p);
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                list.add(new User(id, name, email, country));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean delete(int id) {
        boolean rowDelete = false;
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(DELETE_USERS_SQL);
            p.setInt(1, id);
            rowDelete = p.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowDelete;
    }

    @Override
    public boolean update(User user) {
        boolean rowUpdate = false;
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(UPDATE_USERS_SQL);
            p.setString(1, user.getName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getCountry());
            p.setInt(4, user.getId());
            rowUpdate = p.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowUpdate;
    }

    @Override
    public User findByCountry(String country) {
        User user = null;
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(FIND_BY_COUNTRY);
            p.setString(1, country);
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int id = resultSet.getInt("id");
                user = new User(id, name, email, country);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> sort() {
        List<User> list = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement p = connection.prepareStatement(SORT_LIST_BY_NAME);
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int id = resultSet.getInt("id");
                String country = resultSet.getString("country");
                list.add(new User(id, name,email,country));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    private void printSQLExeption(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQL State:" + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}

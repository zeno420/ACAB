package de.ndfnb.acab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
    Connection con;

    public Connection getConnection() {
        return con;
    }

    public Database(String hostname, String port, String database, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void method() {
        //TODO
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("select * from emp");
//        while (rs.next())
//            System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//        con.close();
    }
}
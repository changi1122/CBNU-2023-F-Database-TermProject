package controller;

import enums.Mode;

import java.sql.Connection;
import java.sql.DriverManager;

public class Controller {

    protected static Connection con;

    public Controller() {
        /* Connection 생성 */
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://192.168.56.101:3308/term_pos",
                    "root", "1234"
            );
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void closeConnection() {
        /* Connection 닫기 */
        try {
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Mode getCommand() {
        return Mode.Sale;
    }
}

package org.bdg;

import java.sql.*;
import java.util.Scanner;


public class Application {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String mysqlDriver = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "Aa_094242632";
        String url = "jdbc:mysql://localhost:3306/userManagement";
        Class.forName(mysqlDriver);

        Connection con= DriverManager.getConnection(
                url,username,password);

        int select;

        do{
            System.out.println("please select menu item");
            System.out.println("1-Create user, 2-Create role, 3 - Set user role, 4 - to exit");
            Scanner scanner = new Scanner(System.in);
            select = scanner.nextInt();
            if(select == 1){
                System.out.println("please enter user first name");
                Scanner scannerFirstName = new Scanner(System.in);
                String firstName = scannerFirstName.nextLine();

                System.out.println("please enter user  last name");
                Scanner scannerLastName = new Scanner(System.in);
                String lastName = scannerLastName.nextLine();

                System.out.println("please enter user password");
                Scanner scannerPassword = new Scanner(System.in);
                String userPassword = scannerPassword.nextLine();

                PreparedStatement createUsers = con.prepareStatement("CREATE TABLE IF NOT EXISTS users (\n" +
                                                                            "    id INT(11) AUTO_INCREMENT PRIMARY KEY,\n" +
                                                                            "    firstName VARCHAR(255) NOT NULL,\n" +
                                                                            "    lastName VARCHAR(255) NOT NULL,\n" +
                                                                            "    password VARCHAR(255) NOT NULL\n" +
                                                                            ");");
                int i = createUsers.executeUpdate();
                if(i != 0 && i != 1){
                    System.out.println("Something went wrong");
                    return;
                }
                PreparedStatement insertUser = con.prepareStatement("insert into users (firstName, lastName, password) values(?,?,?)");
                insertUser.setString(1,firstName);
                insertUser.setString(2,lastName);
                insertUser.setString(3,userPassword);
                i = insertUser.executeUpdate();
                if(i == 1){
                    System.out.println("User added");
                }

            }else if(select == 2){
                System.out.println("please enter role name");
                Scanner scannerRole = new Scanner(System.in);
                String role = scannerRole.nextLine();

                PreparedStatement createRoles = con.prepareStatement("CREATE TABLE IF NOT EXISTS roles (\n" +
                                                                             "    id INT(11) AUTO_INCREMENT PRIMARY KEY,\n" +
                                                                             "    title VARCHAR(255) NOT NULL\n" +
                                                                             ");");
                int i = createRoles.executeUpdate();
                if(i != 0 && i != 1){
                    System.out.println("Something went wrong");
                    return;
                }
                PreparedStatement insertRole = con.prepareStatement("insert into roles (title) values(?)");
                insertRole.setString(1,role);

                i = insertRole.executeUpdate();
                if(i == 1){
                    System.out.println("Role added");
                }

            }else if(select == 3){
                System.out.println("please select user by id");
                System.out.println("id - firstName  -  lastName" );

                String selectUsers = "select id, firstName, lastName from users";
                Statement stmt = con.createStatement();
                ResultSet rsUsers = stmt.executeQuery(selectUsers);
                while (rsUsers.next()) {
                    int id = rsUsers.getInt("id");
                    String firstName = rsUsers.getString("firstName");
                    String lastName = rsUsers.getString("lastName");
                    System.out.println(id + " - " + firstName + " - " + lastName );
                }

                Scanner scannerUserId = new Scanner(System.in);
                int userId = scannerUserId.nextInt();

                System.out.println("please select role for user by id");
                System.out.println("id  -  title");
                String selectRoles = "select id, title from roles";
                ResultSet rsRoles = stmt.executeQuery(selectRoles);
                while (rsRoles.next()) {
                    int id = rsRoles.getInt("id");
                    String title = rsRoles.getString("title");

                    System.out.println(id + " - " + title);
                }

                Scanner scannerRoleId = new Scanner(System.in);
                int roleId = scannerRoleId.nextInt();

                PreparedStatement createUsersRoles = con.prepareStatement("CREATE TABLE IF NOT EXISTS `usersRoles` (  \n" +
                                                                          "  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                                                                          "  `userId` INT(11) NOT NULL,\n" +
                                                                          "  `roleId` INT(11) NOT NULL,\n" +
                                                                          "  PRIMARY KEY (`id`) ,\n" +
                                                                          "  FOREIGN KEY (`userId`) REFERENCES `usermanagement`.`users`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                                                                          "  FOREIGN KEY (`roleId`) REFERENCES `usermanagement`.`roles`(`id`) ON UPDATE CASCADE ON DELETE CASCADE\n" +
                                                                          ") ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;");
                int i = createUsersRoles.executeUpdate();
                if(i != 0 && i != 1){
                    System.out.println("Something went wrong");
                    return;
                }

                PreparedStatement insertUserRole = con.prepareStatement("insert into usersRoles (userId, roleId) values(?, ?)");
                insertUserRole.setInt(1,userId);
                insertUserRole.setInt(2,roleId);

                i = insertUserRole.executeUpdate();
                if(i == 1){
                    System.out.println("For user set role");
                }

            }
        }while (select != 4);
        con.close();
    }
}


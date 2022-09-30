package kdk10_lab2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class KDK10_LAB2 {

    public static void main(String[] args) {
        try {
            // Адрес нашей базы данных "tsn_demo" на локальном компьютере (localhost)
            String url = "jdbc:mysql://localhost:3306/kdk10_lab2?useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

            // Создание свойств соединения с базой данных
            Properties authorization = new Properties();
            authorization.put("user", "root"); // Зададим имя пользователя БД
            authorization.put("password", "root"); // Зададим пароль доступа в БД

            // Создание соединения с базой данных
            Connection connection = DriverManager.getConnection(url, authorization);

            // Создание оператора доступа к базе данных
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            // Выполнение запроса к базе данных, получение набора данных
            ResultSet table = statement.executeQuery("SELECT * FROM TV ");

            System.out.println("Начальная БД:");
            table.first(); // Выведем имена полей
            for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                System.out.print(table.getMetaData().getColumnName(j) + "\t\t");
            }
            System.out.println();

            table.beforeFirst(); // Выведем записи таблицы
            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            Scanner sc = new Scanner(System.in);
            System.out.println("Введите параметры нового поля таблицы:");
            System.out.print("name - ");
            String scannedName = sc.nextLine();
            System.out.print("manufacturer - ");
            String scannedManufacturer = sc.nextLine();
            
            System.out.println("После добавления:");
            statement.execute("INSERT TV(name, manufacturer) VALUES ('" + scannedName + "', '" + scannedManufacturer + "')");
            table = statement.executeQuery("SELECT * FROM TV");

            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            System.out.println("Строку с каким id хотите удалить?");
            System.out.print("id - ");
            int scannedId = sc.nextInt();
            statement.execute("DELETE FROM TV WHERE Id = " + scannedId);
            
            System.out.println("После удаления:");
            table = statement.executeQuery("SELECT * FROM TV");
            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }
            sc.nextLine();
            
            System.out.println("На что изменить в первую строку?");
            System.out.print("name - ");
            String scannedNameUp = sc.nextLine();
            System.out.print("manufacturer - ");
            String scannedManufacturerUp = sc.nextLine();
            statement.executeUpdate("UPDATE TV SET Name = '" + scannedNameUp + "' WHERE Id = 1");
            statement.executeUpdate("UPDATE TV SET Manufacturer = '" + scannedManufacturerUp + "' WHERE id = 1");
            System.out.println("После изменения:");
            table = statement.executeQuery("SELECT * FROM TV");

            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            System.out.println("После сортировки:");
            table = statement.executeQuery("SELECT * FROM TV ORDER BY Id DESC");

            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }

            if (table != null) {
                table.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            } // Отключение от базы данных

        } catch (Exception e) {
            System.err.println("Error accessing database!");
            e.printStackTrace();
        }
    }

}

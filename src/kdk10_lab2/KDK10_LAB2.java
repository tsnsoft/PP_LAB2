package kdk10_lab2;

import java.net.InetAddress;
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
            String url = "jdbc:mysql://" + InetAddress.getLocalHost().getHostName() + ".local"
                    + ":3306/tsn_demo?serverTimezone=UTC&useSSL=false";

            // Создание свойств соединения с базой данных
            Properties authorization = new Properties();
            authorization.put("user", "root"); // Зададим имя пользователя БД
            authorization.put("password", "PassW0Rd+"); // Зададим пароль доступа в БД

            // Создание соединения с базой данных
            Connection connection = DriverManager.getConnection(url, authorization);

            // Создание оператора доступа к базе данных
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            // Выполнение запроса к базе данных, получение набора данных
            ResultSet table = statement.executeQuery("SELECT * FROM my_books");

            System.out.println("Начальная таблица БД:");
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
            System.out.println();

            Scanner sc = new Scanner(System.in);
            System.out.println("Введите параметры новой записи для таблицы данных:");
            System.out.print("Название книги: ");
            String scannedName = sc.nextLine();
            System.out.print("Автор: ");
            String scannedAuthor = sc.nextLine();
            System.out.println();

            System.out.println("После добавления строки:");
            statement.execute("INSERT my_books(name, author) VALUES ('" + scannedName + "', '" + scannedAuthor + "')");
            table = statement.executeQuery("SELECT * FROM my_books");

            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("Строку с каким id хотите удалить?");
            System.out.print("id: ");
            String scannedId = sc.nextLine();
            if (!scannedId.equals("")) {
                statement.execute("DELETE FROM my_books WHERE id = " + scannedId);
            }
            System.out.println();

            System.out.println("Таблица после удаления записи:");
            table = statement.executeQuery("SELECT * FROM my_books");
            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("Какую запись вы хотите изменить?");
            System.out.print("id: ");
            scannedId = sc.nextLine();
            System.out.println("Теперь вводите новые данные для данной записи");
            System.out.print("Название книги: ");
            String scannedNameUp = sc.nextLine();
            System.out.print("Автор: ");
            String scannedAuthorUp = sc.nextLine();
            if (!scannedId.equals("")) {
                statement.executeUpdate("UPDATE my_books SET name = '" + scannedNameUp + "' WHERE id = " + scannedId);
                statement.executeUpdate("UPDATE my_books SET author = '" + scannedAuthorUp + "' WHERE id = " + scannedId);
            }
            System.out.println("Данные таблицы после изменения:");
            table = statement.executeQuery("SELECT * FROM my_books");
            System.out.println();

            System.out.print("Введите фрагмент названия для фильтрации: ");
            String filter = sc.nextLine();

            while (table.next()) {
                for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                    System.out.print(table.getString(j) + "\t\t");
                }
                System.out.println();
            }
            System.out.println();

            System.out.println("Данные таблицыс фильтром и сортировкой:");
            table = statement.executeQuery("SELECT * FROM my_books WHERE name like '%"
                    + filter + "%' ORDER BY name DESC");

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
            System.err.println("Ошибка доступа к базе или вы вводите не то, что надо !!!");
        }
    }

}

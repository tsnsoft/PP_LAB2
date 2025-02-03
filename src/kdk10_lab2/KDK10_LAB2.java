package kdk10_lab2;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class KDK10_LAB2 {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Адрес нашей базы данных "tsn_demo" на локальном компьютере (localhost)
            String url = "jdbc:mysql://localhost:3306/tsn_demo?useSSL=false&serverTimezone=UTC";

            // Создание свойств соединения с базой данных
            Properties authorization = new Properties();
            authorization.put("user", "root"); // Зададим имя пользователя БД
            authorization.put("password", "PassW0Rd++"); // Зададим пароль доступа в БД

            // Создание соединения с базой данных
            try (Connection connection = DriverManager.getConnection(url, authorization);
                 Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                // Выполнение запроса к базе данных, получение набора данных
                try (ResultSet table = statement.executeQuery("SELECT * FROM my_books")) {

                    // Вывод начальной таблицы БД
                    System.out.println("Начальная таблица БД:");
                    printTable(table);

                    // Ввод новых данных для вставки записи
                    System.out.println("Введите параметры новой записи для таблицы данных:");
                    System.out.print("Название книги: ");
                    String scannedName = sc.nextLine();
                    System.out.print("Автор: ");
                    String scannedAuthor = sc.nextLine();
                    System.out.println();

                    // Вставка новой записи в таблицу
                    String insertSQL = "INSERT INTO my_books (name, author) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                        preparedStatement.setString(1, scannedName);
                        preparedStatement.setString(2, scannedAuthor);
                        preparedStatement.executeUpdate();
                    }

                    System.out.println("После добавления строки:");
                    // Вывод таблицы после вставки новой записи
                    try (ResultSet updatedTable = statement.executeQuery("SELECT * FROM my_books")) {
                        printTable(updatedTable);
                    }

                    // Удаление записи из таблицы
                    System.out.println("Строку с каким id хотите удалить?");
                    System.out.print("id: ");
                    String scannedId = sc.nextLine();
                    if (!scannedId.isEmpty()) {
                        String deleteSQL = "DELETE FROM my_books WHERE id = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                            preparedStatement.setString(1, scannedId);
                            preparedStatement.executeUpdate();
                        }
                    }

                    // Вывод таблицы после удаления записи
                    System.out.println("Таблица после удаления записи:");
                    try (ResultSet updatedTable = statement.executeQuery("SELECT * FROM my_books")) {
                        printTable(updatedTable);
                    }

                    // Обновление записи в таблице
                    System.out.println("Какую запись вы хотите изменить?");
                    System.out.print("id: ");
                    scannedId = sc.nextLine();
                    System.out.println("Теперь вводите новые данные для данной записи");
                    System.out.print("Название книги: ");
                    String scannedNameUp = sc.nextLine();
                    System.out.print("Автор: ");
                    String scannedAuthorUp = sc.nextLine();
                    if (!scannedId.isEmpty()) {
                        String updateSQL = "UPDATE my_books SET name = ?, author = ? WHERE id = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                            preparedStatement.setString(1, scannedNameUp);
                            preparedStatement.setString(2, scannedAuthorUp);
                            preparedStatement.setString(3, scannedId);
                            preparedStatement.executeUpdate();
                        }
                    }

                    // Вывод таблицы после обновления записи
                    System.out.println("Данные таблицы после изменения:");
                    try (ResultSet updatedTable = statement.executeQuery("SELECT * FROM my_books")) {
                        printTable(updatedTable);
                    }

                    // Фильтрация и сортировка таблицы
                    System.out.print("Введите фрагмент названия для фильтрации: ");
                    String filter = sc.nextLine();
                    System.out.println("Данные таблицы с фильтром и сортировкой:");
                    try (ResultSet filteredTable = statement.executeQuery("SELECT * FROM my_books WHERE name LIKE '%" + filter + "%' ORDER BY name DESC")) {
                        printTable(filteredTable);
                    }
                }
            } catch (SQLException e) {
                // Обработка исключений при работе с базой данных
                System.err.println(e.getMessage());
                System.err.println("Ошибка доступа к базе или вы вводите не то, что надо !!!");
            }
        }
    }

    // Метод для вывода данных таблицы
    private static void printTable(ResultSet table) throws SQLException {
        table.beforeFirst(); // Переместимся в начало набора данных
        ResultSetMetaData metaData = table.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Вывод имен столбцов
        for (int j = 1; j <= columnCount; j++) {
            System.out.print(metaData.getColumnName(j) + "\t\t");
        }
        System.out.println();

        // Вывод строк таблицы
        while (table.next()) {
            for (int j = 1; j <= columnCount; j++) {
                System.out.print(table.getString(j) + "\t\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}
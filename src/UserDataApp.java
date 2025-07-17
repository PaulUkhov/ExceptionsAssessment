
import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

public class UserDataApp {

    public static void main(String[] args) {
        System.out.println("Введите данные через пробел (Фамилия Имя Отчество дата_рождения номертелефона пол):");
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();

        try {
            String[] data = inputLine.trim().split("\\s+");

            // Проверка количества данных
            if (data.length < 6) {
                throw new IllegalArgumentException("Введено меньше данных, чем требуется (6)");
            } else if (data.length > 6) {
                throw new IllegalArgumentException("Введено больше данных, чем требуется (6)");
            }

            // Распределяем данные по переменным
            String lastName = data[0];
            String firstName = data[1];
            String middleName = data[2];
            String birthDateStr = data[3];
            String phoneStr = data[4];
            String genderStr = data[5];

            // Проверка и парсинг даты
            Date birthDate = parseDate(birthDateStr);

            // Проверка номера телефона (целое беззнаковое число)
            long phone = parsePhone(phoneStr);

            // Проверка пола
            char gender = parseGender(genderStr);

            // Формируем строку для записи
            String outputLine = String.format("%s %s %s %s %d %c",
                    lastName, firstName, middleName, birthDateStr, phone, gender);

            // Записываем в файл с названием <Фамилия>.txt
            writeToFile(lastName, outputLine);

            System.out.println("Данные успешно сохранены.");

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Ошибка формата даты: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом:");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Произошла ошибка:");
            e.printStackTrace();
        }
    }

    private static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false); // строгая проверка даты
        return sdf.parse(dateStr);
    }

    private static long parsePhone(String phoneStr) {
        if (!phoneStr.matches("\\d+")) {
            throw new IllegalArgumentException("Номер телефона должен содержать только цифры");
        }
        try {
            return Long.parseLong(phoneStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Номер телефона слишком большой для типа long");
        }
    }

    private static char parseGender(String genderStr) {
        if (genderStr.length() != 1) {
            throw new IllegalArgumentException("Пол должен быть одним символом 'f' или 'm'");
        }
        char g = Character.toLowerCase(genderStr.charAt(0));
        if (g != 'f' && g != 'm') {
            throw new IllegalArgumentException("Пол должен быть 'f' или 'm'");
        }
        return g;
    }

    private static void writeToFile(String lastName, String line) throws IOException {
        String fileName = lastName + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(fileName),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        }
    }
}

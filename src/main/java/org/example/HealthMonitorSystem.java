package org.example;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HealthMonitorSystem {
    private static final String DATA_DIRECTORY = "C:\\Users\\Javier\\IdeaProjects\\HealthTracker\\src\\main\\java\\org\\example\\model\\data";
    private static final String USER_FILE = "users.txt";
    private static final String CALORIE_INTAKE_FILE = "calorie_intake.txt";
    private static final String EXERCISE_FILE = "exercise.txt";
    private static final String SLEEP_FILE = "sleep.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Health Monitor System");

        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. Create a new user");
            System.out.println("2. Log in");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    System.out.print("Enter your username: ");
                    String username = scanner.nextLine();
                    if (validateUser(username)) {
                        loggedInMenu(username);
                    } else {
                        System.out.println("Invalid username.");
                    }
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting the Health Monitor System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void createUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (validateUser(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + File.separator + USER_FILE, true)) {
            writer.write(username + "\n");
            System.out.println("User created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private static boolean validateUser(String username) {
        try (FileReader reader = new FileReader(DATA_DIRECTORY + File.separator + USER_FILE);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }

    private static void loggedInMenu(String username) {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nLogged in as: " + username);
            System.out.println("1. Record daily calorie intake");
            System.out.println("2. Log exercise activity");
            System.out.println("3. Log sleep record");
            System.out.println("4. View daily caloric balance");
            System.out.println("5. View sleep analysis");
            System.out.println("6. View exercise log");
            System.out.println("7. View health summary");
            System.out.println("8. Log out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    recordCalorieIntake(username);
                    break;
                case 2:
                    logExerciseActivity(username);
                    break;
                case 3:
                    logSleepRecord(username);
                    break;
                case 4:
                    viewDailyCaloricBalance(username);
                    break;
                case 5:
                    viewSleepAnalysis(username);
                    break;
                case 6:
                    viewExerciseLog(username);
                    break;
                case 7:
                    viewHealthSummary(username);
                    break;
                case 8:
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Exiting the application...");
        scanner.close();
        System.exit(0);
    }

    private static void recordCalorieIntake(String username) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter food item: ");
        String foodItem = scanner.nextLine();
        System.out.print("Enter caloric value: ");
        int caloricValue = scanner.nextInt();
        scanner.nextLine();

        String date = getDate();

        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + File.separator + CALORIE_INTAKE_FILE, true)) {
            writer.write(username + "," + foodItem + "," + caloricValue + "," + date + "\n");
            System.out.println("Calorie intake recorded successfully.");
        } catch (IOException e) {
            System.out.println("Error recording calorie intake: " + e.getMessage());
        }
    }

    private static void logExerciseActivity(String username) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter exercise type: ");
        String exerciseType = scanner.nextLine();
        System.out.print("Enter exercise duration in minutes: ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter estimated calories burned: ");
        int caloriesBurned = scanner.nextInt();
        scanner.nextLine();

        String date = getDate();

        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + File.separator + EXERCISE_FILE, true)) {
            writer.write(username + "," + exerciseType + "," + duration + "," + caloriesBurned + "," + date + "\n");
            System.out.println("Exercise activity logged successfully.");
        } catch (IOException e) {
            System.out.println("Error logging exercise activity: " + e.getMessage());
        }
    }

    private static void logSleepRecord(String username) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter sleep start time (HH:mm): ");
        String sleepStart = scanner.nextLine();
        System.out.print("Enter sleep end time (HH:mm): ");
        String sleepEnd = scanner.nextLine();

        int sleepDuration = calculateSleepDuration(sleepStart, sleepEnd);

        String date = getDate();

        try (FileWriter writer = new FileWriter(DATA_DIRECTORY + File.separator + SLEEP_FILE, true)) {
            writer.write(username + "," + sleepStart + "," + sleepEnd + "," + sleepDuration + "," + date + "\n");
            System.out.println("Sleep record logged successfully.");
        } catch (IOException e) {
            System.out.println("Error logging sleep record: " + e.getMessage());
        }
    }

    private static void viewDailyCaloricBalance(String username) {
        String date = getDate();
        int totalCaloriesConsumed = 0;
        int totalCaloriesBurned = 0;

        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + CALORIE_INTAKE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                int calories = Integer.parseInt(parts[2]);
                String recordDate = parts[3];

                if (recordUsername.equals(username) && recordDate.equals(date)) {
                    totalCaloriesConsumed += calories;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading calorie intake file: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + EXERCISE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                int caloriesBurned = Integer.parseInt(parts[3]);
                String recordDate = parts[4];

                if (recordUsername.equals(username) && recordDate.equals(date)) {
                    totalCaloriesBurned += caloriesBurned;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading exercise file: " + e.getMessage());
        }

        int dailyCaloricBalance = totalCaloriesConsumed - totalCaloriesBurned;
        System.out.println("Daily Caloric Balance for " + date + ": " + dailyCaloricBalance);
    }

    private static void viewSleepAnalysis(String username) {
        int totalSleepMinutes = 0;
        int sleepRecordsCount = 0;

        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + SLEEP_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                int sleepDuration = Integer.valueOf(parts[3]);

                if (recordUsername.equals(username)) {
                    totalSleepMinutes += sleepDuration;
                    sleepRecordsCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading sleep file: " + e.getMessage());
        }

        if (sleepRecordsCount > 0) {
            int averageSleepMinutes = totalSleepMinutes / sleepRecordsCount;
            int averageSleepHours = averageSleepMinutes / 60;
            System.out.println("Average Sleep Hours: " + averageSleepHours);

            try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + SLEEP_FILE))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    String recordUsername = parts[0];
                    int sleepDuration = Integer.valueOf(parts[3]);
                    String recordDate = parts[4];

                    if (recordUsername.equals(username) && sleepDuration < averageSleepMinutes) {
                        System.out.println("Day with below-average sleep: " + recordDate + ", Sleep Duration: " + sleepDuration);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error reading sleep file: " + e.getMessage());
            }
        } else {
            System.out.println("No sleep records found.");
        }
    }

    private static void viewExerciseLog(String username) {
        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + EXERCISE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                String exerciseType = parts[1];
                int duration = Integer.parseInt(parts[2]);
                int caloriesBurned = Integer.parseInt(parts[3]);
                String recordDate = parts[4];

                if (recordUsername.equals(username)) {
                    System.out.println("Exercise Type: " + exerciseType + ", Duration: " + duration + " minutes, Calories Burned: " + caloriesBurned + ", Date: " + recordDate);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading exercise file: " + e.getMessage());
        }
    }

//    private static void viewHealthSummary(String username) {
//        int totalCaloriesConsumed = 0;
//        int totalCaloriesBurned = 0;
//        int totalSleepHours = 0;
//        int sleepRecordsCount = 0;
//        int cardioExercises = 0;
//        int strengthTrainingExercises = 0;
//
//        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + CALORIE_INTAKE_FILE))) {
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] parts = line.split(",");
//                String recordUsername = parts[0];
//                int calories = Integer.parseInt(parts[2]);
//
//                if (recordUsername.equals(username)) {
//                    totalCaloriesConsumed += calories;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Error reading calorie intake file: " + e.getMessage());
//        }
//
//        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + EXERCISE_FILE))) {
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] parts = line.split(",");
//                String recordUsername = parts[0];
//                int caloriesBurned = Integer.parseInt(parts[3]);
//                String exerciseType = parts[1];
//
//                if (recordUsername.equals(username)) {
//                    totalCaloriesBurned += caloriesBurned;
//
//                    if (exerciseType.equalsIgnoreCase("cardio")) {
//                        cardioExercises++;
//                    } else if (exerciseType.equalsIgnoreCase("strength training")) {
//                        strengthTrainingExercises++;
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Error reading exercise file: " + e.getMessage());
//        }
//
//        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + SLEEP_FILE))) {
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] parts = line.split(",");
//                String recordUsername = parts[0];
//                int sleepDuration = Integer.parseInt(parts[3]);
//
//                if (recordUsername.equals(username)) {
//                    totalSleepHours += sleepDuration;
//                    sleepRecordsCount++;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Error reading sleep file: " + e.getMessage());
//        }
//
//        int averageSleepHours = (sleepRecordsCount > 0) ? totalSleepHours / sleepRecordsCount : 0;
//
//        System.out.println("Health Summary for " + username);
//        System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
//        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
//        System.out.println("Total Sleep Hours: " + totalSleepHours);
//        System.out.println("Average Sleep Hours: " + averageSleepHours);
//        System.out.println("Cardio Exercises: " + cardioExercises);
//        System.out.println("Strength Training Exercises: " + strengthTrainingExercises);
//    }

    private static void viewHealthSummary(String username) {
        int totalCaloriesConsumed = 0;
        int totalCaloriesBurned = 0;
        int totalSleepMinutes = 0;
        int sleepRecordsCount = 0;
        int cardioExercises = 0;
        int strengthTrainingExercises = 0;

        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + CALORIE_INTAKE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                int calories = Integer.parseInt(parts[2]);

                if (recordUsername.equals(username)) {
                    totalCaloriesConsumed += calories;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading calorie intake file: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + EXERCISE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                int caloriesBurned = Integer.parseInt(parts[3]);
                String exerciseType = parts[1];

                if (recordUsername.equals(username)) {
                    totalCaloriesBurned += caloriesBurned;

                    if (exerciseType.equalsIgnoreCase("cardio")) {
                        cardioExercises++;
                    } else if (exerciseType.equalsIgnoreCase("strength training")) {
                        strengthTrainingExercises++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading exercise file: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(new File(DATA_DIRECTORY + File.separator + SLEEP_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String recordUsername = parts[0];
                int sleepDuration = Integer.valueOf(parts[3]);

                if (recordUsername.equals(username)) {
                    totalSleepMinutes += sleepDuration;
                    sleepRecordsCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading sleep file: " + e.getMessage());
        }

        int totalSleepHours = totalSleepMinutes / 60;
        int averageSleepHours = (sleepRecordsCount > 0) ? totalSleepHours / sleepRecordsCount : 0;

        System.out.println("Health Summary for " + username);
        System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        System.out.println("Total Sleep Hours: " + totalSleepHours);
        System.out.println("Average Sleep Hours: " + averageSleepHours);
        System.out.println("Cardio Exercises: " + cardioExercises);
        System.out.println("Strength Training Exercises: " + strengthTrainingExercises);
    }

    private static int calculateSleepDuration(String sleepStart, String sleepEnd) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date startDate;
        Date endDate;
        int sleepDuration = 0;

        try {
            startDate = format.parse(sleepStart);
            endDate = format.parse(sleepEnd);

            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
            int startMinute = calendar.get(Calendar.MINUTE);

            // Establecer la fecha actual en la fecha de fin del sueño
            calendar.setTime(endDate);
            int endHour = calendar.get(Calendar.HOUR_OF_DAY);
            int endMinute = calendar.get(Calendar.MINUTE);

            // Calcular la duración del sueño
            int hours = endHour - startHour;
            int minutes = endMinute - startMinute;

            // Ajustar los minutos si es necesario
            if (minutes < 0) {
                minutes += 60;
                hours--;
            }

            // Asegurarse de que la duración sea siempre positiva
            if (hours < 0) {
                hours += 24;
            }

            // Convertir la duración del sueño a minutos
            sleepDuration = hours * 60 + minutes;
        } catch (ParseException e) {
            System.out.println("Error parsing sleep start/end time: " + e.getMessage());
        }

        return sleepDuration;
    }


    private static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ExpenseTracker {
    private Map<String, User> users;
    private static final String DATA_FILE = "expenses.txt";

    public ExpenseTracker() {
        this.users = new HashMap<>();
        loadExpenses("expense.txt");
    }

    public void registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
            saveExpenses();
        } else {
            System.out.println("Username already exists!");
        }
    }

    public void login(String username, String password) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            System.out.println("Login successful!");
            displayMenu(username);
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private void displayMenu(String username) {
        Scanner scanner = new Scanner(System.in);
        User user = users.get(username);

        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Calculate Category-wise Summation");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addExpense(user);
                    break;
                case 2:
                    viewExpenses(user);
                    break;
                case 3:
                    calculateCategoryWiseSummation(user);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void addExpense(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter date (yyyy-mm-dd): ");
        String date = scanner.next();

        System.out.print("Enter category: ");
        String category = scanner.next();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        Expense expense = new Expense(date, category, amount);
        user.addExpense(expense);
        saveExpenses();

        System.out.println("Expense added successfully!");
    }

    private void viewExpenses(User user) {
        List<Expense> expenses = user.getExpenses();

        System.out.println("Expenses:");
        for (Expense expense : expenses) {
            System.out.println(expense.getDate() + " - " + expense.getCategory() + " - " + expense.getAmount());
        }
    }

    private void calculateCategoryWiseSummation(User user) {
        Map<String, Double> categoryWiseSummation = new HashMap<>();

        for (Expense expense : user.getExpenses()) {
            String category = expense.getCategory();
            double amount = expense.getAmount();

            if (categoryWiseSummation.containsKey(category)) {
                categoryWiseSummation.put(category, categoryWiseSummation.get(category) + amount);
            } else {
                categoryWiseSummation.put(category, amount);
            }
        }

        System.out.println("Category-wise Summation:");
        for (Map.Entry<String, Double> entry : categoryWiseSummation.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public void loadExpenses(String filename) {
    try {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String expenseLine;

        // Check if the file is empty
        if ((expenseLine = bufferedReader.readLine()) == null) {
            System.out.println("Error: File is empty - " + filename);
            return;
        }

        // Read the header line (if present)
        String[] headerColumns = expenseLine.split(",");
        if (headerColumns.length < 3) {
            System.out.println("Error: Invalid header format. Expected at least 3 columns, but got " + headerColumns.length);
            return;
        }

        // Read the expense data
        while ((expenseLine = bufferedReader.readLine()) != null) {
            String[] expenseData = expenseLine.split(",");
            if (expenseData.length < 3) {
                System.out.println("Error: Invalid expense data format. Expected at least 3 columns, but got " + expenseData.length);
                continue;
            }

            String date = expenseData[0];
            String category = expenseData[1];
            String amount = expenseData[2];

            // ... rest of the code ...
        }

        bufferedReader.close();
    } catch (FileNotFoundException e) {
        System.out.println("Error: File not found - " + filename);
    } catch (IOException e) {
        System.out.println("Error: Unable to read file - " + filename);
    }
}

    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword());
                for (Expense expense : user.getExpenses()) {
                    writer.write("," + expense.getDate() + "-" + expense.getCategory() + "-" + expense.getAmount());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }
}
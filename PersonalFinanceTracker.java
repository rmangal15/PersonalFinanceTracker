import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class PersonalFinanceTracker {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<String[]> transactions = new ArrayList<>();

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n===== PERSONAL FINANCE TRACKER =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Monthly Summary");
            System.out.println("5. Filter by Category");
            System.out.println("6. Filter by Date");
            System.out.println("7. Export CSV");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addTransaction("Income");
                    break;
                case 2:
                    addTransaction("Expense");
                    break;
                case 3:
                    viewTransactions(transactions);
                    break;
                case 4:
                    monthlySummary();
                    break;
                case 5:
                    filterCategory();
                    break;
                case 6:
                    filterDate();
                    break;
                case 7:
                    saveData();
                    System.out.println("CSV file exported successfully.");
                    break;
                case 8:
                    saveData();
                    System.out.println("Thank you for using the tracker!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void addTransaction(String type) {
        try {
            System.out.print("Enter category: ");
            String category = sc.nextLine().trim();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            if (category.isEmpty() || amount <= 0
                    || !Double.isFinite(amount)) {
                System.out.println("Enter valid category and amount.");
                return;
            }

            System.out.print("Enter description: ");
            String description = sc.nextLine().replace(",", " ");

            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            String[] data = {
                type, category, String.valueOf(amount),
                description, date.toString()
            };

            transactions.add(data);
            saveData();

            System.out.println(type + " added successfully!");

        } catch (Exception e) {
            System.out.println(
                "Invalid input. Check amount and date format."
            );
        }
    }

    static void viewTransactions(ArrayList<String[]> list) {
        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\nType | Category | Amount | Description | Date");

        for (String[] t : list) {
            System.out.println(
                t[0] + " | " + t[1] + " | " + t[2]
                + " | " + t[3] + " | " + t[4]
            );
        }
    }

    static void monthlySummary() {
        try {
            System.out.print("Enter month (YYYY-MM): ");
            YearMonth month = YearMonth.parse(sc.nextLine().trim());

            double income = 0;
            double expense = 0;

            for (String[] t : transactions) {
                LocalDate date = LocalDate.parse(t[4]);

                if (YearMonth.from(date).equals(month)) {
                    double amount = Double.parseDouble(t[2]);

                    if (t[0].equals("Income")) {
                        income += amount;
                    } else {
                        expense += amount;
                    }
                }
            }

            System.out.println("\nMonthly Summary: " + month);
            System.out.printf("Total Income: %.2f%n", income);
            System.out.printf("Total Expense: %.2f%n", expense);
            System.out.printf("Balance: %.2f%n", income - expense);

        } catch (Exception e) {
            System.out.println("Invalid month. Example: 2026-10");
        }
    }

    static void filterCategory() {
        System.out.print("Enter category: ");
        String category = sc.nextLine().trim();

        ArrayList<String[]> result = new ArrayList<>();

        for (String[] t : transactions) {
            if (t[1].equalsIgnoreCase(category)) {
                result.add(t);
            }
        }

        viewTransactions(result);
    }

    static void filterDate() {
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            ArrayList<String[]> result = new ArrayList<>();

            for (String[] t : transactions) {
                if (LocalDate.parse(t[4]).equals(date)) {
                    result.add(t);
                }
            }

            viewTransactions(result);

        } catch (Exception e) {
            System.out.println("Invalid date. Example: 2026-10-22");
        }
    }

    static void saveData() {
        try (PrintWriter writer =
                 new PrintWriter(new FileWriter("transactions.csv"))) {

            writer.println("Type,Category,Amount,Description,Date");

            for (String[] t : transactions) {
                writer.println(
                    t[0] + "," + t[1] + "," + t[2] + ","
                    + t[3] + "," + t[4]
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadData() {
        File file = new File("transactions.csv");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(file))) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 5);

                if (data.length == 5) {
                    transactions.add(data);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading saved data.");
        }
    }
}
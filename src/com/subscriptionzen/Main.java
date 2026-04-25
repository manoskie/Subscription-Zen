package com.subscriptionzen;

import com.subscriptionzen.exceptions.CustomUserNotFoundException;
import com.subscriptionzen.exceptions.InvalidSubscriptionException;
import com.subscriptionzen.models.*;
import com.subscriptionzen.services.*;
import com.subscriptionzen.utils.DateUtils;
import com.subscriptionzen.utils.StringUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════════════════════╗
 * ║ SUBSCRIPTION ZEN — CLI ║
 * ║ Manage your subscriptions from the command line ║
 * ╚══════════════════════════════════════════════════════════╝
 *
 * Demonstrates: Scanner-based CLI, Exception Handling (try-catch),
 * OOP concepts (services layer), ArrayList, custom exceptions.
 */
public class Main {

    // ── Service instances ───────────────────────────────────
    private static UserService userService = new UserService();
    private static SubscriptionService subscriptionService = new SubscriptionService();
    private static PaymentService paymentService = new PaymentService();
    private static CategoryService categoryService = new CategoryService();
    private static AlertService alertService = new AlertService();

    private static Scanner scanner = new Scanner(System.in);

    // ══════════════════════════════════════════════════════════
    // MAIN METHOD
    // ══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           Welcome to Subscription Zen!                  ║");
        System.out.println("║        Your personal subscription manager               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntSafe("Choose an option: ");

            try {
                switch (choice) {
                    case 1:
                        userMenu();
                        break;
                    case 2:
                        subscriptionMenu();
                        break;
                    case 3:
                        paymentMenu();
                        break;
                    case 4:
                        categoryMenu();
                        break;
                    case 5:
                        alertMenu();
                        break;
                    case 6:
                        monthlyExpenseReport();
                        break;
                    case 7:
                        daysUntilRenewal();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\nGoodbye! Keep your subscriptions zen.");
                        break;
                    default:
                        System.out.println("[!] Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
        scanner.close();
    }

    // ══════════════════════════════════════════════════════════
    // MAIN MENU
    // ══════════════════════════════════════════════════════════
    private static void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("  1. User Management");
        System.out.println("  2. Subscription Management");
        System.out.println("  3. Payment Management");
        System.out.println("  4. Category Management");
        System.out.println("  5. Alert Management");
        System.out.println("  6. Monthly Expense Report");
        System.out.println("  7. Days Until Renewal (Function)");
        System.out.println("  0. Exit");
        System.out.println("===============================");
    }

    // ══════════════════════════════════════════════════════════
    // 1. USER MANAGEMENT
    // ══════════════════════════════════════════════════════════
    private static void userMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- User Management ---");
            System.out.println("  1. Register User");
            System.out.println("  2. View All Users");
            System.out.println("  3. Find User by ID");
            System.out.println("  4. Update User");
            System.out.println("  5. Delete User");
            System.out.println("  0. Back to Main Menu");

            int choice = readIntSafe("Choose: ");
            try {
                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        viewAllUsers();
                        break;
                    case 3:
                        findUserById();
                        break;
                    case 4:
                        updateUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("[!] Invalid option.");
                }
            } catch (CustomUserNotFoundException e) {
                System.out.println("[USER ERROR] " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("[DB ERROR] " + e.getMessage());
            }
        }
    }

    private static void registerUser() throws CustomUserNotFoundException, SQLException {
        System.out.print("  Name: ");
        String name = scanner.nextLine();
        System.out.print("  Email: ");
        String email = scanner.nextLine();
        System.out.print("  Password: ");
        String password = scanner.nextLine();
        userService.registerUser(name, email, password);
        System.out.println("  [OK] User registered successfully!");
    }

    private static void viewAllUsers() throws SQLException {
        ArrayList<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("  No users found.");
            return;
        }
        System.out.println(StringUtils.buildTableSeparator(4));
        System.out.println(StringUtils.buildTableRow("ID", "Name", "Email", "Created At"));
        System.out.println(StringUtils.buildTableSeparator(4));
        for (User u : users) {
            System.out.println(StringUtils.buildTableRow(
                    String.valueOf(u.getUserId()),
                    u.getName(),
                    u.getEmail(),
                    u.getCreatedAt() != null ? u.getCreatedAt().toString() : "N/A"));
        }
        System.out.println(StringUtils.buildTableSeparator(4));
    }

    private static void findUserById() throws CustomUserNotFoundException, SQLException {
        int id = readIntSafe("  Enter User ID: ");
        User user = userService.getUserById(id);
        System.out.println("  " + user);
    }

    private static void updateUser() throws CustomUserNotFoundException, SQLException {
        int id = readIntSafe("  Enter User ID to update: ");
        User user = userService.getUserById(id);
        System.out.print("  New Name (current: " + user.getName() + "): ");
        String name = scanner.nextLine();
        System.out.print("  New Email (current: " + user.getEmail() + "): ");
        String email = scanner.nextLine();

        if (!name.trim().isEmpty())
            user.setName(name.trim());
        if (!email.trim().isEmpty())
            user.setEmail(email.trim());
        userService.updateUser(user);
        System.out.println("  [OK] User updated.");
    }

    private static void deleteUser() throws CustomUserNotFoundException, SQLException {
        int id = readIntSafe("  Enter User ID to delete: ");
        userService.deleteUser(id);
        System.out.println("  [OK] User deleted.");
    }

    // ══════════════════════════════════════════════════════════
    // 2. SUBSCRIPTION MANAGEMENT
    // ══════════════════════════════════════════════════════════
    private static void subscriptionMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(
                    "\n---!!!NOTE!!!: Please have a look at catagory first to check the ID's of all Categories. ---");
            System.out.println("\n--- Subscription Management ---");
            System.out.println("  1. Add Subscription");
            System.out.println("  2. View My Subscriptions (by User ID)");
            System.out.println("  3. View All Subscriptions");
            System.out.println("  4. Update Subscription");
            System.out.println("  5. Delete Subscription");
            System.out.println("  6. Search Subscription by ID");
            System.out.println("  0. Back to Main Menu");

            int choice = readIntSafe("Choose: ");
            try {
                switch (choice) {
                    case 1:
                        addSubscription();
                        break;
                    case 2:
                        viewUserSubscriptions();
                        break;
                    case 3:
                        viewAllSubscriptions();
                        break;
                    case 4:
                        updateSubscription();
                        break;
                    case 5:
                        deleteSubscription();
                        break;
                    case 6:
                        findSubscriptionById();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("[!] Invalid option.");
                }
            } catch (InvalidSubscriptionException e) {
                System.out.println("[SUBSCRIPTION ERROR] " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("[DB ERROR] " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("[DATE ERROR] Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    private static void addSubscription() throws InvalidSubscriptionException, SQLException, ParseException {
        int userId = readIntSafe("  User ID: ");
        int categoryId = readIntSafe("  Category ID: ");
        System.out.print("  Service Name: ");
        String service = scanner.nextLine();
        double cost = readDoubleSafe("  Monthly Cost (Rs.): ");
        System.out.print("  Start Date (yyyy-MM-dd): ");
        Date startDate = DateUtils.parseDate(scanner.nextLine().trim());
        int alertDays = readIntSafe("  Alert Days Before Renewal: ");
        System.out.print("  Status (ACTIVE/PAUSED/CANCELLED): ");
        String status = scanner.nextLine().trim();

        subscriptionService.addSubscription(userId, categoryId, service, cost, startDate, alertDays, status);
        System.out.println("  [OK] Subscription added! Renewal date set automatically by trigger.");
    }

    private static void viewUserSubscriptions() throws SQLException {
        int userId = readIntSafe("  Enter User ID: ");
        ArrayList<Subscription> subs = subscriptionService.getSubscriptionsByUserId(userId);
        if (subs.isEmpty()) {
            System.out.println("  No subscriptions found for User ID " + userId);
            return;
        }
        printSubscriptionTable(subs);
    }

    private static void viewAllSubscriptions() throws SQLException {
        ArrayList<Subscription> subs = subscriptionService.getAllSubscriptions();
        if (subs.isEmpty()) {
            System.out.println("  No subscriptions found.");
            return;
        }
        printSubscriptionTable(subs);
    }

    private static void printSubscriptionTable(ArrayList<Subscription> subs) {
        System.out.println(StringUtils.buildTableSeparator(6));
        System.out.println(StringUtils.buildTableRow("ID", "Service", "Cost", "Start Date", "Next Renewal", "Status"));
        System.out.println(StringUtils.buildTableSeparator(6));
        for (Subscription s : subs) {
            System.out.println(StringUtils.buildTableRow(
                    String.valueOf(s.getSubscriptionId()),
                    s.getServiceName(),
                    String.format("Rs. %.2f", s.getCost()),
                    DateUtils.formatDate(s.getStartDate()),
                    DateUtils.formatDate(s.getNextRenewalDate()),
                    s.getStatus()));
        }
        System.out.println(StringUtils.buildTableSeparator(6));
    }

    private static void updateSubscription() throws InvalidSubscriptionException, SQLException {
        int id = readIntSafe("  Subscription ID to update: ");
        Subscription sub = subscriptionService.getSubscriptionById(id);

        System.out.print("  New Service Name (current: " + sub.getServiceName() + "): ");
        String service = scanner.nextLine();
        if (!service.trim().isEmpty())
            sub.setServiceName(service.trim());

        System.out.print("  New Cost (current: " + sub.getCost() + "): ");
        String costStr = scanner.nextLine();
        if (!costStr.trim().isEmpty())
            sub.setCost(Double.parseDouble(costStr.trim()));

        System.out.print("  New Status (current: " + sub.getStatus() + "), options (ACTIVE/PAUSED/CANCELLED): ");
        String status = scanner.nextLine();
        if (!status.trim().isEmpty())
            sub.setStatus(status.trim().toUpperCase());

        subscriptionService.updateSubscription(sub);
        System.out.println("  [OK] Subscription updated.");
    }

    private static void deleteSubscription() throws InvalidSubscriptionException, SQLException {
        int id = readIntSafe("  Subscription ID to delete: ");
        subscriptionService.deleteSubscription(id);
        System.out.println("  [OK] Subscription deleted.");
    }

    private static void findSubscriptionById() throws InvalidSubscriptionException, SQLException {
        int id = readIntSafe("  Enter Subscription ID: ");
        Subscription sub = subscriptionService.getSubscriptionById(id);
        System.out.println("  " + StringUtils.buildSubscriptionSummary(
                sub.getSubscriptionId(), sub.getServiceName(), sub.getCost(), sub.getStatus()));
        System.out.println("  Start: " + DateUtils.formatDate(sub.getStartDate())
                + "  |  Next Renewal: " + DateUtils.formatDate(sub.getNextRenewalDate()));
    }

    // ══════════════════════════════════════════════════════════
    // 3. PAYMENT MANAGEMENT
    // ══════════════════════════════════════════════════════════
    private static void paymentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Payment Management ---");
            System.out.println("  1. Record Payment");
            System.out.println("  2. View Payments by Subscription");
            System.out.println("  3. View All Payments");
            System.out.println("  4. Delete Payment");
            System.out.println("  0. Back to Main Menu");

            int choice = readIntSafe("Choose: ");
            try {
                switch (choice) {
                    case 1:
                        recordPayment();
                        break;
                    case 2:
                        viewPaymentsBySub();
                        break;
                    case 3:
                        viewAllPayments();
                        break;
                    case 4:
                        deletePayment();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("[!] Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("[DB ERROR] " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("[DATE ERROR] Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    private static void recordPayment() throws SQLException, ParseException {
        int subId = readIntSafe("  Subscription ID: ");
        double amount = readDoubleSafe("  Amount (Rs.): ");
        System.out.print("  Payment Date (yyyy-MM-dd): ");
        Date date = DateUtils.parseDate(scanner.nextLine().trim());
        System.out.print("  Payment Method (UPI/Card/NetBanking/Cash): ");
        String method = scanner.nextLine().trim();
        paymentService.recordPayment(subId, amount, date, method);
        System.out.println("  [OK] Payment recorded.");
    }

    private static void viewPaymentsBySub() throws SQLException {
        int subId = readIntSafe("  Subscription ID: ");
        ArrayList<Payment> payments = paymentService.getPaymentsBySubscription(subId);
        if (payments.isEmpty()) {
            System.out.println("  No payments found for Subscription ID " + subId);
            return;
        }
        System.out.println(StringUtils.buildTableSeparator(5));
        System.out.println(StringUtils.buildTableRow("Payment ID", "Sub ID", "Amount", "Date", "Method"));
        System.out.println(StringUtils.buildTableSeparator(5));
        for (Payment p : payments) {
            System.out.println(StringUtils.buildTableRow(
                    String.valueOf(p.getPaymentId()),
                    String.valueOf(p.getSubscriptionId()),
                    String.format("Rs. %.2f", p.getAmount()),
                    DateUtils.formatDate(p.getPaymentDate()),
                    p.getPaymentMethod()));
        }
        System.out.println(StringUtils.buildTableSeparator(5));
    }

    private static void viewAllPayments() throws SQLException {
        ArrayList<Payment> payments = paymentService.getAllPayments();
        if (payments.isEmpty()) {
            System.out.println("  No payments found.");
            return;
        }
        System.out.println(StringUtils.buildTableSeparator(5));
        System.out.println(StringUtils.buildTableRow("Payment ID", "Sub ID", "Amount", "Date", "Method"));
        System.out.println(StringUtils.buildTableSeparator(5));
        for (Payment p : payments) {
            System.out.println(StringUtils.buildTableRow(
                    String.valueOf(p.getPaymentId()),
                    String.valueOf(p.getSubscriptionId()),
                    String.format("Rs. %.2f", p.getAmount()),
                    DateUtils.formatDate(p.getPaymentDate()),
                    p.getPaymentMethod()));
        }
        System.out.println(StringUtils.buildTableSeparator(5));
    }

    private static void deletePayment() throws SQLException {
        int id = readIntSafe("  Payment ID to delete: ");
        paymentService.deletePayment(id);
        System.out.println("  [OK] Payment deleted.");
    }

    // ══════════════════════════════════════════════════════════
    // 4. CATEGORY MANAGEMENT
    // ══════════════════════════════════════════════════════════
    private static void categoryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Category Management ---");
            System.out.println("  1. Add Category");
            System.out.println("  2. View All Categories");
            System.out.println("  3. Delete Category");
            System.out.println("  0. Back to Main Menu");

            int choice = readIntSafe("Choose: ");
            try {
                switch (choice) {
                    case 1:
                        addCategory();
                        break;
                    case 2:
                        viewCategories();
                        break;
                    case 3:
                        deleteCategory();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("[!] Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("[DB ERROR] " + e.getMessage());
            }
        }
    }

    private static void addCategory() throws SQLException {
        System.out.print("  Category Name: ");
        String name = scanner.nextLine();
        System.out.print("  Description: ");
        String desc = scanner.nextLine();
        categoryService.addCategory(name, desc);
        System.out.println("  [OK] Category added.");
    }

    private static void viewCategories() throws SQLException {
        ArrayList<Category> cats = categoryService.getAllCategories();
        if (cats.isEmpty()) {
            System.out.println("  No categories found.");
            return;
        }
        int[] colWidths = { 6, 20, 50 };
        System.out.println(StringUtils.buildTableSeparator(colWidths));
        System.out.println(StringUtils.buildTableRow(colWidths, "ID", "Name", "Description"));
        System.out.println(StringUtils.buildTableSeparator(colWidths));
        for (Category c : cats) {
            System.out.println(StringUtils.buildTableRow(colWidths,
                    String.valueOf(c.getCategoryId()),
                    c.getCategoryName(),
                    c.getDescription() != null ? c.getDescription() : ""));
        }
        System.out.println(StringUtils.buildTableSeparator(colWidths));
    }

    private static void deleteCategory() throws SQLException {
        int id = readIntSafe("  Category ID to delete: ");
        categoryService.deleteCategory(id);
        System.out.println("  [OK] Category deleted.");
    }

    // ══════════════════════════════════════════════════════════
    // 5. ALERT MANAGEMENT
    // ══════════════════════════════════════════════════════════
    private static void alertMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Alert Management ---");
            System.out.println("  1. Create Alert");
            System.out.println("  2. View Alerts by Subscription");
            System.out.println("  3. View All Alerts");
            System.out.println("  4. Mark Alert as Sent");
            System.out.println("  5. Delete Alert");
            System.out.println("  0. Back to Main Menu");

            int choice = readIntSafe("Choose: ");
            try {
                switch (choice) {
                    case 1:
                        createAlert();
                        break;
                    case 2:
                        viewAlertsBySub();
                        break;
                    case 3:
                        viewAllAlerts();
                        break;
                    case 4:
                        markAlertSent();
                        break;
                    case 5:
                        deleteAlert();
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("[!] Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("[DB ERROR] " + e.getMessage());
            } catch (ParseException e) {
                System.out.println("[DATE ERROR] Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    private static void createAlert() throws SQLException, ParseException {
        int subId = readIntSafe("  Subscription ID: ");
        System.out.print("  Alert Message: ");
        String msg = scanner.nextLine();
        System.out.print("  Alert Date (yyyy-MM-dd): ");
        Date date = DateUtils.parseDate(scanner.nextLine().trim());
        alertService.createAlert(subId, msg, date);
        System.out.println("  [OK] Alert created.");
    }

    private static void viewAlertsBySub() throws SQLException {
        int subId = readIntSafe("  Subscription ID: ");
        ArrayList<Alert> alerts = alertService.getAlertsBySubscription(subId);
        if (alerts.isEmpty()) {
            System.out.println("  No alerts found.");
            return;
        }
        printAlerts(alerts);
    }

    private static void viewAllAlerts() throws SQLException {
        ArrayList<Alert> alerts = alertService.getAllAlerts();
        if (alerts.isEmpty()) {
            System.out.println("  No alerts found.");
            return;
        }
        printAlerts(alerts);
    }

    private static void printAlerts(ArrayList<Alert> alerts) {
        System.out.println(StringUtils.buildTableSeparator(5));
        System.out.println(StringUtils.buildTableRow("Alert ID", "Sub ID", "Message", "Date", "Sent?"));
        System.out.println(StringUtils.buildTableSeparator(5));
        for (Alert a : alerts) {
            System.out.println(StringUtils.buildTableRow(
                    String.valueOf(a.getAlertId()),
                    String.valueOf(a.getSubscriptionId()),
                    a.getAlertMessage(),
                    DateUtils.formatDate(a.getAlertDate()),
                    a.isSent() ? "Yes" : "No"));
        }
        System.out.println(StringUtils.buildTableSeparator(5));
    }

    private static void markAlertSent() throws SQLException {
        int id = readIntSafe("  Alert ID to mark as sent: ");
        alertService.markAlertAsSent(id);
        System.out.println("  [OK] Alert marked as sent.");
    }

    private static void deleteAlert() throws SQLException {
        int id = readIntSafe("  Alert ID to delete: ");
        alertService.deleteAlert(id);
        System.out.println("  [OK] Alert deleted.");
    }

    // ══════════════════════════════════════════════════════════
    // 6. MONTHLY EXPENSE REPORT (Stored Procedure)
    // ══════════════════════════════════════════════════════════
    private static void monthlyExpenseReport() {
        System.out.println("\n--- Monthly Expense Report ---");
        System.out.println("  1. Expense for a particular User");
        System.out.println("  2. Expense for all Users (Table format)");
        int choice = readIntSafe("Choose option: ");

        try {
            if (choice == 1) {
                int userId = readIntSafe("  Enter User ID: ");
                double total = subscriptionService.getMonthlyExpense(userId);
                System.out.println("\n  ╔═══════════════════════════════════╗");
                System.out.println("  ║   Monthly Expense Report          ║");
                System.out.println("  ╠═══════════════════════════════════╣");
                System.out.println("  ║   User ID : " + StringUtils.padRight(String.valueOf(userId), 20) + "  ║");
                System.out.println("  ║   Total   : Rs. " + StringUtils.padRight(String.format("%.2f", total), 16) + "  ║");
                System.out.println("  ╚═══════════════════════════════════╝");
            } else if (choice == 2) {
                java.util.Map<Integer, Double> expenses = subscriptionService.getAllUsersMonthlyExpense();
                if (expenses.isEmpty()) {
                    System.out.println("  No active subscriptions found.");
                } else {
                    int[] colWidths = { 10, 20 };
                    System.out.println("\n  " + StringUtils.buildTableSeparator(colWidths));
                    System.out.println("  " + StringUtils.buildTableRow(colWidths, "User ID", "Total Expense (Rs.)"));
                    System.out.println("  " + StringUtils.buildTableSeparator(colWidths));
                    for (java.util.Map.Entry<Integer, Double> entry : expenses.entrySet()) {
                        System.out.println("  " + StringUtils.buildTableRow(colWidths, 
                                String.valueOf(entry.getKey()), 
                                String.format("%.2f", entry.getValue())));
                    }
                    System.out.println("  " + StringUtils.buildTableSeparator(colWidths));
                }
            } else {
                System.out.println("  [!] Invalid option.");
            }
        } catch (SQLException e) {
            System.out.println("[DB ERROR] " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════
    // 7. DAYS UNTIL RENEWAL (MySQL Function)
    // ══════════════════════════════════════════════════════════
    private static void daysUntilRenewal() {
        System.out.println("\n--- Days Until Renewal (MySQL Function) ---");
        int subId = readIntSafe("  Enter Subscription ID: ");
        try {
            int days = subscriptionService.getDaysUntilRenewal(subId);
            System.out.println("\n  ╔═══════════════════════════════════════╗");
            System.out.println("  ║   Days Until Renewal                  ║");
            System.out.println("  ╠═══════════════════════════════════════╣");
            System.out.println("  ║   Subscription ID : " + StringUtils.padRight(String.valueOf(subId), 16) + "  ║");
            if (days >= 0) {
                System.out.println("  ║   Days Remaining  : " + StringUtils.padRight(String.valueOf(days), 16) + "  ║");
            } else {
                System.out.println("  ║   Status          : " + StringUtils.padRight("Expired / N/A", 16) + "  ║");
            }
            System.out.println("  ╚═══════════════════════════════════════╝");
        } catch (SQLException e) {
            System.out.println("[DB ERROR] " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════
    // INPUT HELPERS (safe reading with error recovery)
    // ══════════════════════════════════════════════════════════

    /**
     * Reads an integer safely, re-prompting on invalid input.
     */
    private static int readIntSafe(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads a double safely, re-prompting on invalid input.
     */
    private static double readDoubleSafe(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }
}

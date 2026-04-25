<h1 align="center">
  🧘 Subscription Zen
</h1>

<p align="center">
  <b>Your personal subscription manager — from the command line.</b><br/>
  A Core Java &amp; MySQL (JDBC) CLI application to track, manage, and stay on top of all your subscriptions.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/JDBC-Connector%2FJ-00758F?style=for-the-badge" alt="JDBC" />
</p>

---

## ✨ Features

| Module                 | Capabilities                                                     |
| ---------------------- | ---------------------------------------------------------------- |
| **User Management**    | Register · View All · Find by ID · Update · Delete               |
| **Subscriptions**      | Add · View (by user / all) · Update · Delete · Search by ID      |
| **Payments**           | Record · View (by subscription / all) · Delete                   |
| **Categories**         | Add · View All · Delete                                          |
| **Alerts**             | Create · View (by subscription / all) · Mark as Sent · Delete    |
| **Expense Report**     | Monthly expense via MySQL Stored Procedure                       |

### 🎓 Academic Concepts Demonstrated

- **JDBC** — `DriverManager`, `PreparedStatement`, `CallableStatement`
- **Layered Architecture** — Models → DAO → Services → CLI
- **OOP** — Inheritance, Interfaces, Constructor Overloading
- **Custom Exceptions** — `CustomUserNotFoundException`, `InvalidSubscriptionException`
- **Collections** — `ArrayList` used throughout
- **SQL** — DDL, DML, Triggers, Stored Procedures, Foreign Keys, CHECK Constraints

---

## 🗂️ Project Structure

```
SubscriptionZen/
├── .env.example                  # Environment variable template
├── .gitignore
├── README.md
└── src/
    ├── database/
    │   └── schema.sql            # Full DDL, triggers, stored procedure, seed data
    └── com/subscriptionzen/
        ├── Main.java             # CLI entry-point
        ├── config/
        │   └── DatabaseConfig.java
        ├── models/               # POJOs — User, Subscription, Payment, Category, Alert
        ├── dao/                  # DAO interfaces + JDBC implementations
        ├── services/             # Business-logic layer
        ├── exceptions/           # Custom exception classes
        └── utils/                # DateUtils, StringUtils helpers
```

---

## 🚀 Getting Started

### Prerequisites

| Tool            | Version   | Link                                                                 |
| --------------- | --------- | -------------------------------------------------------------------- |
| **Java JDK**    | 8 or later| [Download](https://adoptium.net/)                                    |
| **MySQL Server**| 8.0+      | [Download](https://dev.mysql.com/downloads/mysql/)                   |
| **MySQL Connector/J** | 8.x | [Download](https://dev.mysql.com/downloads/connector/j/)            |

### 1 — Create the Database

Open a MySQL shell and run:

```sql
SOURCE src/database/schema.sql;
```

This creates the `subscription_zen` database with all tables, a trigger, a stored procedure, and seed data.

### 2 — Configure Credentials

The app reads database credentials from **environment variables** so that secrets are never hard-coded.

**Option A — Set env vars directly (recommended):**

```bash
# Linux / macOS
export DB_URL="jdbc:mysql://localhost:3306/subscription_zen"
export DB_USER="root"
export DB_PASSWORD="your_password"
```

```cmd
:: Windows CMD
set DB_URL=jdbc:mysql://localhost:3306/subscription_zen
set DB_USER=root
set DB_PASSWORD=your_password
```

**Option B — Copy the template and fill it in:**

```bash
cp .env.example .env
# Edit .env with your credentials
```

> **Note:** If no environment variables are set, the app defaults to `localhost:3306/subscription_zen` with user `root` and an **empty password**.

### 3 — Compile

From the project root directory:

```cmd
javac -cp ".;path\to\mysql-connector-j-8.x.x.jar" -d out src\com\subscriptionzen\*.java src\com\subscriptionzen\config\*.java src\com\subscriptionzen\dao\*.java src\com\subscriptionzen\exceptions\*.java src\com\subscriptionzen\models\*.java src\com\subscriptionzen\services\*.java src\com\subscriptionzen\utils\*.java
```

> On **Linux / macOS**, use `:` instead of `;` as the classpath separator.

### 4 — Run

```cmd
java -cp "out;path\to\mysql-connector-j-8.x.x.jar" com.subscriptionzen.Main
```

You'll see:

```
╔══════════════════════════════════════════════════════════╗
║           Welcome to Subscription Zen!                  ║
║        Your personal subscription manager               ║
╚══════════════════════════════════════════════════════════╝

========== MAIN MENU ==========
  1. User Management
  2. Subscription Management
  3. Payment Management
  4. Category Management
  5. Alert Management
  6. Monthly Expense Report
  0. Exit
===============================
```

---

## 🛠️ Tech Stack

```
 ┌──────────┐     ┌────────────┐     ┌────────────┐     ┌───────────┐
 │  CLI UI  │ ──▸ │  Services  │ ──▸ │    DAOs    │ ──▸ │   MySQL   │
 │ (Main)   │     │  (Logic)   │     │   (JDBC)   │     │    DB     │
 └──────────┘     └────────────┘     └────────────┘     └───────────┘
```

---

## 📜 License

This project is for academic / educational purposes. Feel free to use and modify.

---

<p align="center">Made with ☕ and 🧘 by <b>Manan</b></p>


<!-- For rahil -->
# Step 1: Recompile the source file into the 'out' directory
javac -d "d:\AllProjects\SubscriptionZen\Subscription-Zen\out" "d:\AllProjects\SubscriptionZen\Subscription-Zen\src\com\subscriptionzen\Main.java"

# Step 2: Run the updated code
java -cp "d:\AllProjects\SubscriptionZen\Subscription-Zen\out;C:\Users\rahil\Downloads\mysql-connector-j-9.6.0\mysql-connector-j-9.6.0\mysql-connector-j-9.6.0.jar" com.subscriptionzen.Main

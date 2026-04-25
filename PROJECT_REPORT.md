# Subscription Zen — Project Report

> **Course**: Database Management Systems — Phase 2  
> **Project**: Subscription Zen — Personal Subscription Manager  
> **Technology**: Core Java 17, MySQL 8.0, JDBC (Connector/J)  
> **Architecture**: CLI → Services → DAO → MySQL  

---

## Table of Contents

1. [Anomalies in the Relational Schema](#1-anomalies-in-the-relational-schema)
2. [Functional Dependencies](#2-functional-dependencies)
3. [Normalization](#3-normalization)
4. [Table Implementation in MySQL](#4-table-implementation-in-mysql)
5. [Queries, Functions, Procedures, and Triggers](#5-queries-functions-procedures-and-triggers)
6. [Conclusion](#6-conclusion)

---

## 1. Anomalies in the Relational Schema

Database anomalies are problems that arise when a relational schema is not properly designed. The three types of anomalies are:

### 1.1 Insertion Anomaly

An insertion anomaly occurs when we cannot insert data into the database without the presence of other unrelated data.

**Example in our schema (if not normalized):**  
If subscription details and category information were stored in a single combined table, we would not be able to add a new category (e.g., "Gaming") without also adding a subscription row. This would force us to insert NULL or dummy values for subscription fields like `SERVICE_NAME`, `COST`, and `START_DATE`.

**How our design avoids it:**  
The `CATEGORY` table is a separate, independent entity. We can freely insert categories like `('Gaming', 'Video game subscriptions')` without needing any subscription to exist.

### 1.2 Deletion Anomaly

A deletion anomaly occurs when deleting data causes unintended loss of other meaningful information.

**Example in our schema (if not normalized):**  
If user information and subscription information were stored in one table, deleting the last subscription of a user would also remove the user's name, email, and other data entirely.

**How our design avoids it:**  
The `USER` table is separate from the `SUBSCRIPTION` table. Deleting all subscriptions for a user (via `DELETE FROM SUBSCRIPTION WHERE USER_ID = ?`) does **not** affect the `USER` row. The user's profile data remains intact.

**Note on CASCADE behavior:**  
Our schema uses `ON DELETE CASCADE` from `USER` → `SUBSCRIPTION`. This means deleting a **user** intentionally cascades to remove their subscriptions, payments, and alerts. This is **by design** — when a user account is deleted, all their data should be cleaned up. This is not a deletion anomaly; it is deliberate referential integrity enforcement.

### 1.3 Update Anomaly

An update anomaly occurs when the same data stored in multiple places must be updated in every location, and failure to do so leads to inconsistency.

**Example in our schema (if not normalized):**  
If `CATEGORY_NAME` were stored directly inside the `SUBSCRIPTION` table (instead of using a foreign key `CATEGORY_ID`), then renaming a category from "Entertainment" to "Streaming & Media" would require updating every subscription row that had "Entertainment." Missing even one row would create an inconsistency.

**How our design avoids it:**  
Category information is stored once in the `CATEGORY` table and referenced via `CATEGORY_ID` (foreign key) in the `SUBSCRIPTION` table. Updating a category name requires changing only one row in `CATEGORY`, and the change is reflected across all subscriptions automatically via the join.

---

## 2. Functional Dependencies

A functional dependency `X → Y` means that for each unique value of X, there is exactly one corresponding value of Y.

### 2.1 USER Table

| Functional Dependency | Description |
|---|---|
| `USER_ID → NAME, EMAIL, PASSWORD_HASH, CREATED_AT` | Primary key determines all other attributes |
| `EMAIL → USER_ID, NAME, PASSWORD_HASH, CREATED_AT` | Email is UNIQUE — acts as a candidate key |

### 2.2 CATEGORY Table

| Functional Dependency | Description |
|---|---|
| `CATEGORY_ID → CATEGORY_NAME, DESCRIPTION` | Primary key determines all other attributes |
| `CATEGORY_NAME → CATEGORY_ID, DESCRIPTION` | Category name is UNIQUE — acts as a candidate key |

### 2.3 SUBSCRIPTION Table

| Functional Dependency | Description |
|---|---|
| `SUBSCRIPTION_ID → USER_ID, CATEGORY_ID, SERVICE_NAME, COST, START_DATE, NEXT_RENEWAL_DATE, ALERT_DAYS, STATUS` | Primary key determines all other attributes |

Note: `USER_ID` and `CATEGORY_ID` are foreign keys but do **not** determine subscription attributes on their own (a user can have multiple subscriptions).

### 2.4 PAYMENT Table

| Functional Dependency | Description |
|---|---|
| `PAYMENT_ID → SUBSCRIPTION_ID, AMOUNT, PAYMENT_DATE, PAYMENT_METHOD` | Primary key determines all other attributes |

### 2.5 ALERTS Table

| Functional Dependency | Description |
|---|---|
| `ALERT_ID → SUBSCRIPTION_ID, ALERT_MESSAGE, ALERT_DATE, IS_SENT` | Primary key determines all other attributes |

### 2.6 Summary of All Functional Dependencies

```
USER:         USER_ID → {NAME, EMAIL, PASSWORD_HASH, CREATED_AT}
              EMAIL → {USER_ID, NAME, PASSWORD_HASH, CREATED_AT}

CATEGORY:     CATEGORY_ID → {CATEGORY_NAME, DESCRIPTION}
              CATEGORY_NAME → {CATEGORY_ID, DESCRIPTION}

SUBSCRIPTION: SUBSCRIPTION_ID → {USER_ID, CATEGORY_ID, SERVICE_NAME, COST,
              START_DATE, NEXT_RENEWAL_DATE, ALERT_DAYS, STATUS}

PAYMENT:      PAYMENT_ID → {SUBSCRIPTION_ID, AMOUNT, PAYMENT_DATE, PAYMENT_METHOD}

ALERTS:       ALERT_ID → {SUBSCRIPTION_ID, ALERT_MESSAGE, ALERT_DATE, IS_SENT}
```

---

## 3. Normalization

We analyze each table against Normal Forms (1NF, 2NF, 3NF, BCNF) to verify correctness.

### 3.1 First Normal Form (1NF)

A table is in 1NF if:
- All columns contain atomic (indivisible) values
- Each column has a unique name
- Each row is uniquely identifiable (primary key exists)

| Table | Atomic Values? | Primary Key? | 1NF? |
|---|---|---|---|
| USER | ✅ All attributes are scalar (no arrays, no comma-separated values) | `USER_ID` (AUTO_INCREMENT) | ✅ Yes |
| CATEGORY | ✅ `CATEGORY_NAME` and `DESCRIPTION` are single values | `CATEGORY_ID` (AUTO_INCREMENT) | ✅ Yes |
| SUBSCRIPTION | ✅ Each field holds one value (one cost, one date, one status) | `SUBSCRIPTION_ID` (AUTO_INCREMENT) | ✅ Yes |
| PAYMENT | ✅ Amount, date, method are all scalar | `PAYMENT_ID` (AUTO_INCREMENT) | ✅ Yes |
| ALERTS | ✅ Message, date, sent-flag are all scalar | `ALERT_ID` (AUTO_INCREMENT) | ✅ Yes |

**Result: All tables satisfy 1NF.**

### 3.2 Second Normal Form (2NF)

A table is in 2NF if:
- It is in 1NF
- No non-key attribute is partially dependent on a composite primary key

Since **all tables use a single-column auto-increment primary key**, there is no composite key anywhere in the schema. Partial dependencies are **impossible** with single-column PKs.

**Result: All tables satisfy 2NF trivially (no composite keys).**

### 3.3 Third Normal Form (3NF)

A table is in 3NF if:
- It is in 2NF
- No non-key attribute is transitively dependent on the primary key (i.e., no non-key attribute depends on another non-key attribute)

**Analysis per table:**

| Table | Transitive Dependency Check | 3NF? |
|---|---|---|
| USER | `NAME`, `EMAIL`, `PASSWORD_HASH`, `CREATED_AT` — all depend directly on `USER_ID`, not on each other | ✅ Yes |
| CATEGORY | `CATEGORY_NAME`, `DESCRIPTION` — both depend directly on `CATEGORY_ID` | ✅ Yes |
| SUBSCRIPTION | `SERVICE_NAME`, `COST`, `START_DATE`, etc. — all depend directly on `SUBSCRIPTION_ID`. `USER_ID` and `CATEGORY_ID` are FKs, not transitive paths. | ✅ Yes |
| PAYMENT | `AMOUNT`, `PAYMENT_DATE`, `PAYMENT_METHOD` — all depend directly on `PAYMENT_ID` | ✅ Yes |
| ALERTS | `ALERT_MESSAGE`, `ALERT_DATE`, `IS_SENT` — all depend directly on `ALERT_ID` | ✅ Yes |

**Result: All tables satisfy 3NF.**

### 3.4 Boyce-Codd Normal Form (BCNF)

A table is in BCNF if for every functional dependency `X → Y`, X is a superkey.

- In `USER`: both `USER_ID` and `EMAIL` are candidate keys (superkeys). All FDs have a superkey on the left. ✅
- In `CATEGORY`: both `CATEGORY_ID` and `CATEGORY_NAME` are candidate keys. ✅
- In `SUBSCRIPTION`, `PAYMENT`, `ALERTS`: Only determinant is the primary key. ✅

**Result: All tables satisfy BCNF.**

### 3.5 Normalization Conclusion

```
┌──────────────────────────────────────────────────────────────┐
│  All 5 tables are already in BCNF (Boyce-Codd Normal Form). │
│  No further normalization or decomposition is required.      │
│  The schema is free of insertion, deletion, and update       │
│  anomalies by design.                                        │
└──────────────────────────────────────────────────────────────┘
```

---

## 4. Table Implementation in MySQL

### 4.1 Entity-Relationship Diagram

```
 ┌──────────────────┐
 │     CATEGORY     │
 │──────────────────│
 │ PK CATEGORY_ID   │
 │    CATEGORY_NAME  │◄──────────┐
 │    DESCRIPTION    │           │
 └──────────────────┘           │
                                │ FK (CATEGORY_ID)
 ┌──────────────────┐    ┌──────┴───────────────┐     ┌──────────────────┐
 │      USER        │    │    SUBSCRIPTION       │     │     PAYMENT      │
 │──────────────────│    │──────────────────────│     │──────────────────│
 │ PK USER_ID       │◄───│ FK USER_ID           │     │ PK PAYMENT_ID    │
 │    NAME          │    │ PK SUBSCRIPTION_ID   │◄────│ FK SUBSCRIPTION_ID│
 │    EMAIL (UQ)    │    │ FK CATEGORY_ID       │     │    AMOUNT         │
 │    PASSWORD_HASH  │    │    SERVICE_NAME      │     │    PAYMENT_DATE   │
 │    CREATED_AT    │    │    COST              │     │    PAYMENT_METHOD │
 └──────────────────┘    │    START_DATE        │     └──────────────────┘
                         │    NEXT_RENEWAL_DATE │
                         │    ALERT_DAYS        │     ┌──────────────────┐
                         │    STATUS            │     │     ALERTS       │
                         └──────────────────────┘     │──────────────────│
                                │                     │ PK ALERT_ID      │
                                └────────────────────►│ FK SUBSCRIPTION_ID│
                                                      │    ALERT_MESSAGE │
                                                      │    ALERT_DATE    │
                                                      │    IS_SENT       │
                                                      └──────────────────┘
```

### 4.2 DDL Statements

All tables are implemented in MySQL 8.0 using the InnoDB engine. The complete SQL is in `src/database/schema.sql`.

#### CATEGORY Table
```sql
CREATE TABLE IF NOT EXISTS CATEGORY (
    CATEGORY_ID   INT           AUTO_INCREMENT PRIMARY KEY,
    CATEGORY_NAME VARCHAR(100)  NOT NULL UNIQUE,
    DESCRIPTION   VARCHAR(255)
) ENGINE=InnoDB;
```

#### USER Table
```sql
CREATE TABLE IF NOT EXISTS USER (
    USER_ID       INT           AUTO_INCREMENT PRIMARY KEY,
    NAME          VARCHAR(100)  NOT NULL,
    EMAIL         VARCHAR(150)  NOT NULL UNIQUE,
    PASSWORD_HASH VARCHAR(255)  NOT NULL,
    CREATED_AT    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
```

#### SUBSCRIPTION Table
```sql
CREATE TABLE IF NOT EXISTS SUBSCRIPTION (
    SUBSCRIPTION_ID  INT           AUTO_INCREMENT PRIMARY KEY,
    USER_ID          INT           NOT NULL,
    CATEGORY_ID      INT           NOT NULL,
    SERVICE_NAME     VARCHAR(150)  NOT NULL,
    COST             DECIMAL(10,2) NOT NULL,
    START_DATE       DATE          NOT NULL,
    NEXT_RENEWAL_DATE DATE         DEFAULT NULL,
    ALERT_DAYS       INT           DEFAULT 3,
    STATUS           VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_subscription_user
        FOREIGN KEY (USER_ID) REFERENCES USER(USER_ID)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_subscription_category
        FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(CATEGORY_ID)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_status
        CHECK (STATUS IN ('ACTIVE', 'PAUSED', 'CANCELLED'))
) ENGINE=InnoDB;
```

#### PAYMENT Table
```sql
CREATE TABLE IF NOT EXISTS PAYMENT (
    PAYMENT_ID     INT           AUTO_INCREMENT PRIMARY KEY,
    SUBSCRIPTION_ID INT          NOT NULL,
    AMOUNT         DECIMAL(10,2) NOT NULL,
    PAYMENT_DATE   DATE          NOT NULL,
    PAYMENT_METHOD VARCHAR(50)   NOT NULL,

    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES SUBSCRIPTION(SUBSCRIPTION_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
```

#### ALERTS Table
```sql
CREATE TABLE IF NOT EXISTS ALERTS (
    ALERT_ID       INT           AUTO_INCREMENT PRIMARY KEY,
    SUBSCRIPTION_ID INT          NOT NULL,
    ALERT_MESSAGE  VARCHAR(255)  NOT NULL,
    ALERT_DATE     DATE          DEFAULT NULL,
    IS_SENT        BOOLEAN       DEFAULT FALSE,

    CONSTRAINT fk_alert_subscription
        FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES SUBSCRIPTION(SUBSCRIPTION_ID)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
```

### 4.3 Constraints Summary

| Constraint Type | Table | Details |
|---|---|---|
| Primary Key | All 5 tables | `AUTO_INCREMENT` integer ID |
| Foreign Key (CASCADE) | SUBSCRIPTION → USER | `ON DELETE CASCADE ON UPDATE CASCADE` |
| Foreign Key (RESTRICT) | SUBSCRIPTION → CATEGORY | `ON DELETE RESTRICT ON UPDATE CASCADE` |
| Foreign Key (CASCADE) | PAYMENT → SUBSCRIPTION | `ON DELETE CASCADE ON UPDATE CASCADE` |
| Foreign Key (CASCADE) | ALERTS → SUBSCRIPTION | `ON DELETE CASCADE ON UPDATE CASCADE` |
| UNIQUE | USER.EMAIL | Prevents duplicate email addresses |
| UNIQUE | CATEGORY.CATEGORY_NAME | Prevents duplicate category names |
| CHECK | SUBSCRIPTION.STATUS | Must be `'ACTIVE'`, `'PAUSED'`, or `'CANCELLED'` |

---

## 5. Queries, Functions, Procedures, and Triggers

### 5.1 Queries (DML)

The application executes a full suite of DML queries across all 5 tables through the DAO (Data Access Object) layer using JDBC `PreparedStatement`.

#### INSERT Examples
```sql
-- Insert a new user
INSERT INTO USER (NAME, EMAIL, PASSWORD_HASH) VALUES (?, ?, ?);

-- Insert a new subscription
INSERT INTO SUBSCRIPTION (USER_ID, CATEGORY_ID, SERVICE_NAME, COST, START_DATE, ALERT_DAYS, STATUS)
VALUES (?, ?, ?, ?, ?, ?, ?);

-- Record a payment
INSERT INTO PAYMENT (SUBSCRIPTION_ID, AMOUNT, PAYMENT_DATE, PAYMENT_METHOD)
VALUES (?, ?, ?, ?);
```

#### SELECT Examples
```sql
-- Find user by ID
SELECT * FROM USER WHERE USER_ID = ?;

-- Get all subscriptions for a user
SELECT * FROM SUBSCRIPTION WHERE USER_ID = ?;

-- Get all payments for a subscription
SELECT * FROM PAYMENT WHERE SUBSCRIPTION_ID = ?;

-- Get all alerts
SELECT * FROM ALERTS;
```

#### UPDATE Examples
```sql
-- Update user details
UPDATE USER SET NAME = ?, EMAIL = ? WHERE USER_ID = ?;

-- Update subscription
UPDATE SUBSCRIPTION SET SERVICE_NAME = ?, COST = ?, ALERT_DAYS = ?, STATUS = ?, CATEGORY_ID = ?
WHERE SUBSCRIPTION_ID = ?;

-- Mark alert as sent
UPDATE ALERTS SET IS_SENT = TRUE WHERE ALERT_ID = ?;
```

#### DELETE Examples
```sql
-- Delete a subscription
DELETE FROM SUBSCRIPTION WHERE SUBSCRIPTION_ID = ?;

-- Delete a payment
DELETE FROM PAYMENT WHERE PAYMENT_ID = ?;

-- Delete a user (cascades to subscriptions, payments, alerts)
DELETE FROM USER WHERE USER_ID = ?;
```

### 5.2 MySQL Function

A stored function is created to calculate the number of days remaining until a subscription's next renewal date.

#### Definition
```sql
CREATE FUNCTION fn_days_until_renewal(p_subscription_id INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE days_left INT;
    SELECT DATEDIFF(NEXT_RENEWAL_DATE, CURDATE()) INTO days_left
    FROM SUBSCRIPTION
    WHERE SUBSCRIPTION_ID = p_subscription_id;
    RETURN IFNULL(days_left, -1);
END;
```

#### Usage in Application
The function is called from Java via JDBC:
```java
String sql = "SELECT fn_days_until_renewal(?) AS days_left";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, subscriptionId);
ResultSet rs = pstmt.executeQuery();
if (rs.next()) {
    return rs.getInt("days_left");
}
```

#### Direct MySQL Execution
```sql
-- Check days until renewal for subscription ID 1
SELECT fn_days_until_renewal(1) AS days_remaining;
-- Output: days_remaining = <number of days until next_renewal_date>
```

### 5.3 Stored Procedures

Two stored procedures are defined for generating expense reports.

#### Procedure 1: Monthly expense for a single user
```sql
CREATE PROCEDURE sp_get_monthly_expense(IN p_user_id INT, OUT p_total DECIMAL(10,2))
BEGIN
    SELECT IFNULL(SUM(COST), 0.00)
    INTO   p_total
    FROM   SUBSCRIPTION
    WHERE  USER_ID = p_user_id
      AND  STATUS  = 'ACTIVE';
END;
```

**Usage in Application** (via `CallableStatement`):
```java
String sql = "{CALL sp_get_monthly_expense(?, ?)}";
CallableStatement cstmt = conn.prepareCall(sql);
cstmt.setInt(1, userId);
cstmt.registerOutParameter(2, Types.DECIMAL);
cstmt.execute();
double total = cstmt.getDouble(2);
```

#### Procedure 2: Monthly expense for all users
```sql
CREATE PROCEDURE sp_get_all_users_monthly_expense()
BEGIN
    SELECT USER_ID, IFNULL(SUM(COST), 0.00) AS TOTAL_EXPENSE
    FROM   SUBSCRIPTION
    WHERE  STATUS = 'ACTIVE'
    GROUP BY USER_ID;
END;
```

**Direct MySQL Execution:**
```sql
CALL sp_get_monthly_expense(1, @total);
SELECT @total AS monthly_expense;

CALL sp_get_all_users_monthly_expense();
```

### 5.4 Trigger

A `BEFORE INSERT` trigger automatically calculates the `NEXT_RENEWAL_DATE` when a new subscription is created.

#### Definition
```sql
CREATE TRIGGER trg_set_renewal_date
BEFORE INSERT ON SUBSCRIPTION
FOR EACH ROW
BEGIN
    IF NEW.NEXT_RENEWAL_DATE IS NULL THEN
        SET NEW.NEXT_RENEWAL_DATE = DATE_ADD(NEW.START_DATE, INTERVAL 30 DAY);
    END IF;
END;
```

#### Behavior
- When a subscription is inserted **without** specifying `NEXT_RENEWAL_DATE`, the trigger automatically sets it to `START_DATE + 30 days`.
- If the user explicitly provides a `NEXT_RENEWAL_DATE`, the trigger does not override it.

#### Example
```sql
INSERT INTO SUBSCRIPTION (USER_ID, CATEGORY_ID, SERVICE_NAME, COST, START_DATE, STATUS)
VALUES (1, 1, 'Netflix Premium', 649.00, '2024-04-01', 'ACTIVE');

-- NEXT_RENEWAL_DATE is automatically set to '2024-05-01' by the trigger
SELECT SUBSCRIPTION_ID, SERVICE_NAME, START_DATE, NEXT_RENEWAL_DATE
FROM SUBSCRIPTION WHERE SERVICE_NAME = 'Netflix Premium';
```

### 5.5 Summary of SQL Objects

| SQL Object Type | Name | Purpose |
|---|---|---|
| **Function** | `fn_days_until_renewal()` | Returns days remaining until next renewal |
| **Procedure** | `sp_get_monthly_expense()` | Calculates total monthly expense for one user |
| **Procedure** | `sp_get_all_users_monthly_expense()` | Calculates total monthly expense for all users |
| **Trigger** | `trg_set_renewal_date` | Auto-sets renewal date on subscription insert |
| **Queries** | Throughout DAO layer | Full CRUD (INSERT, SELECT, UPDATE, DELETE) on all 5 tables |

---

## 6. Conclusion

The **Subscription Zen** project demonstrates a complete database-driven application covering the full lifecycle of database design and implementation:

1. **Anomaly Analysis** — Identified and explained insertion, deletion, and update anomalies and how the schema design prevents them through proper decomposition and referential integrity.

2. **Functional Dependencies** — Documented all functional dependencies across the 5 relations, identifying both primary keys and candidate keys.

3. **Normalization** — Verified that all tables satisfy 1NF, 2NF, 3NF, and BCNF. No decomposition was required as the schema was designed correctly from the start.

4. **MySQL Implementation** — Implemented 5 tables with appropriate data types, primary keys, foreign keys (CASCADE and RESTRICT), UNIQUE constraints, CHECK constraints, and seed data.

5. **Queries, Functions, Procedures, and Triggers** — Implemented and executed:
   - Full CRUD queries across all tables
   - 1 MySQL stored function (`fn_days_until_renewal`)
   - 2 stored procedures (`sp_get_monthly_expense`, `sp_get_all_users_monthly_expense`)
   - 1 trigger (`trg_set_renewal_date`)

6. **Application Architecture** — Built a layered Java application (CLI → Services → DAO → JDBC → MySQL) demonstrating real-world usage of all database concepts.

---

*End of Report*

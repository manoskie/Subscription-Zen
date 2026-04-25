-- ============================================================
-- Subscription Zen — Database Schema
-- MySQL DDL, DML, Triggers, and Stored Procedures
-- ============================================================

CREATE DATABASE IF NOT EXISTS subscription_zen;
USE subscription_zen;

-- ============================================================
-- 1. TABLES (Entity & Referential Integrity)
-- ============================================================

-- CATEGORY table (independent — no FKs)
CREATE TABLE IF NOT EXISTS CATEGORY (
    CATEGORY_ID   INT           AUTO_INCREMENT PRIMARY KEY,
    CATEGORY_NAME VARCHAR(100)  NOT NULL UNIQUE,
    DESCRIPTION   VARCHAR(255)
) ENGINE=InnoDB;

-- USER table
CREATE TABLE IF NOT EXISTS USER (
    USER_ID       INT           AUTO_INCREMENT PRIMARY KEY,
    NAME          VARCHAR(100)  NOT NULL,
    EMAIL         VARCHAR(150)  NOT NULL UNIQUE,
    PASSWORD_HASH VARCHAR(255)  NOT NULL,
    CREATED_AT    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- SUBSCRIPTION table (FK → USER, FK → CATEGORY)
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

-- PAYMENT table (FK → SUBSCRIPTION)
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

-- ALERTS table (FK → SUBSCRIPTION)
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


-- ============================================================
-- 2. TRIGGER — Auto-set NEXT_RENEWAL_DATE on SUBSCRIPTION insert
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_set_renewal_date
BEFORE INSERT ON SUBSCRIPTION
FOR EACH ROW
BEGIN
    IF NEW.NEXT_RENEWAL_DATE IS NULL THEN
        SET NEW.NEXT_RENEWAL_DATE = DATE_ADD(NEW.START_DATE, INTERVAL 30 DAY);
    END IF;
END$$

DELIMITER ;


-- ============================================================
-- 3. STORED PROCEDURE — Monthly expense for a given user
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_get_monthly_expense(IN p_user_id INT, OUT p_total DECIMAL(10,2))
BEGIN
    SELECT IFNULL(SUM(COST), 0.00)
    INTO   p_total
    FROM   SUBSCRIPTION
    WHERE  USER_ID = p_user_id
      AND  STATUS  = 'ACTIVE';
END$$

DELIMITER ;

-- ============================================================
-- 3a. STORED PROCEDURE — Monthly expense for all users
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_get_all_users_monthly_expense()
BEGIN
    SELECT USER_ID, IFNULL(SUM(COST), 0.00) AS TOTAL_EXPENSE
    FROM   SUBSCRIPTION
    WHERE  STATUS = 'ACTIVE'
    GROUP BY USER_ID;
END$$

DELIMITER ;

-- ============================================================
-- 4. FUNCTION — Days until next renewal for a subscription
-- ============================================================

DELIMITER $$

CREATE FUNCTION fn_days_until_renewal(p_subscription_id INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE days_left INT;
    SELECT DATEDIFF(NEXT_RENEWAL_DATE, CURDATE()) INTO days_left
    FROM SUBSCRIPTION
    WHERE SUBSCRIPTION_ID = p_subscription_id;
    RETURN IFNULL(days_left, -1);
END$$

DELIMITER ;

-- ============================================================
-- 5. SEED DATA
-- ============================================================

-- Default categories
INSERT INTO CATEGORY (CATEGORY_NAME, DESCRIPTION) VALUES
('Entertainment',   'Streaming, gaming, and media subscriptions'),
('Productivity',    'Tools for work and project management'),
('Cloud Storage',   'Cloud storage and backup services'),
('Music',           'Music streaming subscriptions'),
('Education',       'Online courses and learning platforms'),
('Fitness',         'Gym memberships and fitness apps'),
('News',            'News and magazine subscriptions');

-- Demo user (password hash is a placeholder)
INSERT INTO USER (NAME, EMAIL, PASSWORD_HASH) VALUES
('Demo User', 'demo@subscriptionzen.com', 'hashed_password_placeholder');

USE subscription_zen;
-- Users
INSERT IGNORE INTO USER (NAME, EMAIL, PASSWORD_HASH) VALUES 
('Alice Smith', 'alice.smith@zen.com', 'pass123'),
('Bob Jones', 'bob.jones@zen.com', 'pass456'),
('Charlie Brown', 'charlie.brown@zen.com', 'pass789'),
('Diana Prince', 'diana.prince@zen.com', 'pass012'),
('Evan Wright', 'evan.wright@zen.com', 'pass345');

-- Categories
INSERT IGNORE INTO CATEGORY (CATEGORY_NAME, DESCRIPTION) VALUES
('SaaS Software', 'Cloud based productivity tools'),
('Audio Books', 'Audio and kindle books subscriptions'),
('Meal Prep Delivery', 'Healthy food delivery packages'),
('VPN & Privacy', 'Privacy networking and VPN tools'),
('Web Hosting / Cloud', 'Cloud infrastructure and domains');

-- Subscriptions 
INSERT IGNORE INTO SUBSCRIPTION (USER_ID, CATEGORY_ID, SERVICE_NAME, COST, START_DATE, STATUS) VALUES
((SELECT USER_ID FROM USER WHERE EMAIL='alice.smith@zen.com'), (SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME='SaaS Software'), 'Figma Premium', 15.00, '2024-01-01', 'ACTIVE'),
((SELECT USER_ID FROM USER WHERE EMAIL='bob.jones@zen.com'), (SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME='Audio Books'), 'Audible Premium Plus', 14.95, '2024-01-10', 'ACTIVE'),
((SELECT USER_ID FROM USER WHERE EMAIL='charlie.brown@zen.com'), (SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME='Meal Prep Delivery'), 'HelloFresh Weekly', 60.00, '2024-02-01', 'ACTIVE'),
((SELECT USER_ID FROM USER WHERE EMAIL='diana.prince@zen.com'), (SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME='VPN & Privacy'), 'NordVPN Annual', 11.99, '2023-11-15', 'ACTIVE'),
((SELECT USER_ID FROM USER WHERE EMAIL='evan.wright@zen.com'), (SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME='Web Hosting / Cloud'), 'DigitalOcean Droplet', 12.00, '2023-12-01', 'ACTIVE');

-- Payments
INSERT IGNORE INTO PAYMENT (SUBSCRIPTION_ID, AMOUNT, PAYMENT_DATE, PAYMENT_METHOD) VALUES
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='Figma Premium' LIMIT 1), 15.00, '2024-02-01', 'Card'),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='Audible Premium Plus' LIMIT 1), 14.95, '2024-02-10', 'Card'),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='HelloFresh Weekly' LIMIT 1), 60.00, '2024-03-01', 'NetBanking'),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='NordVPN Annual' LIMIT 1), 11.99, '2024-02-15', 'UPI'),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='DigitalOcean Droplet' LIMIT 1), 12.00, '2024-03-01', 'Card');

-- Alerts
INSERT IGNORE INTO ALERTS (SUBSCRIPTION_ID, ALERT_MESSAGE, ALERT_DATE, IS_SENT) VALUES
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='Figma Premium' LIMIT 1), 'Figma bill coming', '2024-03-01', FALSE),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='Audible Premium Plus' LIMIT 1), 'Audible bill coming', '2024-03-10', FALSE),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='HelloFresh Weekly' LIMIT 1), 'HelloFresh bill coming', '2024-03-31', FALSE),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='NordVPN Annual' LIMIT 1), 'NordVPN bill coming', '2024-03-15', FALSE),
((SELECT SUBSCRIPTION_ID FROM SUBSCRIPTION WHERE SERVICE_NAME='DigitalOcean Droplet' LIMIT 1), 'DigitalOcean bill coming', '2024-04-01', FALSE);

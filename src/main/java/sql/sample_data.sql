-- =========================
-- 1. Roles
-- =========================
INSERT INTO roles (role_name, created_at) VALUES
('Admin', NOW()),
('Organizer', NOW()),
('Attendee', NOW());

-- =========================
-- 2. Users
-- =========================
INSERT INTO users
(full_name, email, phone, gender, password_hash, role_id, status, created_at)
VALUES
('Amit Sharma', 'amit.sharma@mail.com', '9876543210', 'Male', 'hash_admin', 1, 'ACTIVE', NOW()),
('Neha Verma', 'neha.verma@mail.com', '9123456780', 'Female', 'hash_org1', 2, 'ACTIVE', NOW()),
('Rohan Iyer', 'rohan.iyer@mail.com', '9988776655', 'Male', 'hash_org2', 2, 'ACTIVE', NOW()),
('Sara Khan', 'sara.khan@mail.com', '9011223344', 'Female', 'hash_user1', 3, 'ACTIVE', NOW()),
('Vikram Patel', 'vikram.patel@mail.com', '9090909090', 'Male', 'hash_user2', 3, 'ACTIVE', NOW());

-- =========================
-- 3. Categories
-- =========================
INSERT INTO categories (name) VALUES
('Technology'),
('Music'),
('Business'),
('Education');

-- =========================
-- 4. Venues
-- =========================
INSERT INTO venues
(name, street, city, state, pincode, max_capacity, created_at)
VALUES
('Tech Convention Center', 'MG Road', 'Bengaluru', 'Karnataka', '560001', 500, NOW()),
('City Auditorium', 'Park Street', 'Kolkata', 'West Bengal', '700016', 800, NOW()),
('Startup Hub', 'Bandra Kurla Complex', 'Mumbai', 'Maharashtra', '400051', 300, NOW());

-- =========================
-- 5. Events
-- =========================
INSERT INTO events
(organizer_id, title, description, category_id, venue_id,
 start_datetime, end_datetime, capacity, status,
 approved_by, approved_at, created_at)
VALUES
(2, 'AI & Future Tech Summit',
 'Conference on artificial intelligence trends',
 1, 1,
 '2026-03-15 09:00:00',
 '2026-03-15 18:00:00',
 400,
 'PUBLISHED',
 1,
 NOW(),
 NOW()),

(3, 'Indie Music Night',
 'Live performances by indie artists',
 2, 2,
 '2026-04-05 17:00:00',
 '2026-04-05 22:00:00',
 700,
 'APPROVED',
 1,
 NOW(),
 NOW());

-- =========================
-- 6. Tickets
-- =========================
INSERT INTO tickets
(event_id, ticket_type, price, total_quantity, available_quantity)
VALUES
(1, 'Standard Pass', 1500.00, 300, 300),
(1, 'VIP Pass', 3000.00, 100, 100),
(2, 'General Entry', 800.00, 600, 600),
(2, 'Backstage Pass', 2000.00, 100, 100);

-- =========================
-- 7. Offers
-- =========================
INSERT INTO offers
(event_id, code, discount_percentage, valid_from, valid_to)
VALUES
(1, 'AI2026EARLY', 20, '2026-02-01 00:00:00', '2026-02-28 23:59:59'),
(2, 'MUSICFAN10', 10, '2026-03-01 00:00:00', '2026-03-31 23:59:59');

-- =========================
-- 8. Registrations
-- =========================
INSERT INTO registrations
(user_id, event_id, registration_date, status)
VALUES
(4, 1, NOW(), 'CONFIRMED'),
(5, 2, NOW(), 'CONFIRMED');

-- =========================
-- 9. Registration Tickets
-- =========================
INSERT INTO registration_tickets
(registration_id, ticket_id, quantity)
VALUES
(1, 1, 2),
(2, 3, 3);

-- =========================
-- 10. Payments
-- =========================
INSERT INTO payments
(registration_id, amount, payment_method, payment_status, created_at)
VALUES
(1, 3000.00, 'Credit Card', 'SUCCESS', NOW()),
(2, 2400.00, 'UPI', 'SUCCESS', NOW());

-- =========================
-- 11. Offer Usage
-- =========================
INSERT INTO offer_usages
(offer_id, user_id, registration_id, used_at)
VALUES
(1, 4, 1, NOW());

-- =========================
-- 12. Notifications
-- =========================
INSERT INTO notifications
(user_id, message, type, created_at, read_status)
VALUES
(4, 'Your registration for AI & Future Tech Summit is confirmed.', 'EVENT', NOW(), 0),
(5, 'Your ticket for Indie Music Night has been booked.', 'EVENT', NOW(), 0);

-- =========================
-- 13. Feedback
-- =========================
INSERT INTO feedback
(event_id, user_id, rating, comments, submitted_at)
VALUES
(1, 4, 5, 'Excellent sessions and speakers.', NOW()),
(2, 5, 4, 'Great music and sound quality.', NOW());

-- =========================
-- 14. System Logs
-- =========================
INSERT INTO system_logs
(user_id, action, entity, entity_id, message, created_at)
VALUES
(1, 'CREATE', 'EVENT', 1, 'Admin approved AI & Future Tech Summit', NOW()),
(2, 'CREATE', 'EVENT', 2, 'Organizer created Indie Music Night', NOW());

-- USE event_management_db;

-- =========================
-- 1. Roles
-- =========================
-- Standard roles for the system
INSERT INTO roles (role_name, created_at, is_active) VALUES
('ADMIN', UTC_TIMESTAMP(), 1),
('ORGANIZER', UTC_TIMESTAMP(), 1),
('ATTENDEE', UTC_TIMESTAMP(), 1);

-- =========================
-- 2. Categories
-- =========================
-- Major event categories
INSERT INTO categories (name, is_active) VALUES
('Technology', 1),
('Music & Concerts', 1),
('Business & Startup', 1),
('Education & Workshops', 1),
('Sports & Fitness', 1),
('Arts & Culture', 1),
('Food & Drink', 1);

-- =========================
-- 3. Users
-- =========================
-- Password hashes are placeholders (e.g., 'hash_admin', 'hash_org')
INSERT INTO users 
(full_name, email, phone, gender, password_hash, role_id, status, created_at)
VALUES
-- Admins (Role ID: 1)
('System Administrator', 'admin@ems.com', '9999999999', 'Male', '$2a$10$r.E6A91YqW7XmUvQxLz...placeholder', 1, 'ACTIVE', UTC_TIMESTAMP()),
('Super Admin', 'superadmin@ems.com', '8888888888', 'Female', '$2a$10$r.E6A91YqW7XmUvQxLz...placeholder', 1, 'ACTIVE', UTC_TIMESTAMP()),

-- Organizers (Role ID: 2)
('Tech Events India', 'contact@techevents.in', '9123456780', 'Other', 'hash_org1', 2, 'ACTIVE', UTC_TIMESTAMP()),
('Music Hub Mumbai', 'bookings@musichub.com', '9876543210', 'Female', 'hash_org2', 2, 'ACTIVE', UTC_TIMESTAMP()),
('Education First Ltd.', 'info@edufirst.co.in', '9444455555', 'Male', 'hash_org3', 2, 'ACTIVE', UTC_TIMESTAMP()),
('Foodie Explorers', 'hello@foodie.com', '9111122222', 'Female', 'hash_org4', 2, 'ACTIVE', UTC_TIMESTAMP()),

-- Attendees (Role ID: 3)
('Kavinkumar S', 'kavin@mail.com', '9000000001', 'Male', 'hash_user1', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Ananya Reddy', 'ananya@mail.com', '9000000002', 'Female', 'hash_user2', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Rahul Dravid', 'rahul@mail.com', '9000000003', 'Male', 'hash_user3', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Priya Sundar', 'priya@mail.com', '9000000004', 'Female', 'hash_user4', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Suresh Raina', 'suresh@mail.com', '9000000005', 'Male', 'hash_user5', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Deepika P', 'deepika@mail.com', '9000000006', 'Female', 'hash_user6', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Rohit Sharma', 'rohit@mail.com', '9000000007', 'Male', 'hash_user7', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Mithali Raj', 'mithali@mail.com', '9000000008', 'Female', 'hash_user8', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Sania Mirza', 'sania@mail.com', '9000000009', 'Female', 'hash_user9', 3, 'ACTIVE', UTC_TIMESTAMP()),
('Virat Kohli', 'virat@mail.com', '9000000010', 'Male', 'hash_user10', 3, 'ACTIVE', UTC_TIMESTAMP());

-- =========================
-- 4. Venues
-- =========================
INSERT INTO venues
(name, street, city, state, pincode, max_capacity, created_at)
VALUES
('Jawaharlal Nehru Stadium', 'Lodhi Road', 'New Delhi', 'Delhi', '110003', 60000, UTC_TIMESTAMP()),
('Tech Convention Centre', 'Electronic City', 'Bengaluru', 'Karnataka', '560100', 5000, UTC_TIMESTAMP()),
('Grand Palace Hotel', 'Marine Drive', 'Mumbai', 'Maharashtra', '400020', 1000, UTC_TIMESTAMP()),
('Arts & Science College', 'Anna Salai', 'Chennai', 'Tamil Nadu', '600002', 2000, UTC_TIMESTAMP()),
('Cyber Towers Hall', 'HITEC City', 'Hyderabad', 'Telangana', '500081', 800, UTC_TIMESTAMP()),
('Heritage Mahal', 'Old City', 'Jaipur', 'Rajasthan', '302002', 1500, UTC_TIMESTAMP()),
('Science City Auditorium', 'EM Bypass', 'Kolkata', 'West Bengal', '700046', 2200, UTC_TIMESTAMP()),
('Phoenix Mall Arena', 'Viman Nagar', 'Pune', 'Maharashtra', '411014', 1200, UTC_TIMESTAMP());

-- =========================
-- 5. Events (15 Events)
-- =========================
-- Status: DRAFT, PUBLISHED, APPROVED, CANCELLED, COMPLETED
INSERT INTO events
(organizer_id, title, description, category_id, venue_id, start_datetime, end_datetime, capacity, status, approved_by, approved_at, created_at)
VALUES
-- Event 1: Future Tech Summit (Tech)
(3, 'Future Tech Summit 2026', 'Innovations in AI, Quantum Computing and Robotics', 1, 2, '2026-03-20 09:00:00', '2026-03-22 18:00:00', 1000, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 2: Jazz Night Mumbai (Music)
(4, 'Jazz Night Mumbai', 'Smooth jazz performances by national and international artists', 2, 3, '2026-04-05 19:00:00', '2026-04-05 23:00:00', 500, 'PUBLISHED', NULL, NULL, UTC_TIMESTAMP()),
-- Event 3: Startup Pitch Day (Business)
(5, 'Startup Pitch Day', 'Pitch your ideas to top VCs in India', 3, 5, '2026-03-25 10:00:00', '2026-03-25 17:00:00', 300, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 4: Data Science Workshop (Education)
(5, 'Data Science Masterclass', 'Intensive 2-day workshop on Python and ML', 4, 2, '2026-05-10 10:00:00', '2026-05-11 17:00:00', 200, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 5: Annual Athletics Meet (Sports)
(3, 'Annual Athletics Meet 2026', 'Inter-state athletics competition', 5, 1, '2026-06-15 07:00:00', '2026-06-18 20:00:00', 5000, 'PUBLISHED', NULL, NULL, UTC_TIMESTAMP()),
-- Event 6: Contemporary Art Expo (Arts)
(6, 'Contemporary Art Expo', 'Exhibition of modern Indian painters', 6, 6, '2026-04-12 11:00:00', '2026-04-20 19:00:00', 800, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 7: Global Food Festival (Food)
(6, 'Global Food Festival', 'Taste cuisines from 20 different countries', 7, 8, '2026-04-25 12:00:00', '2026-04-27 23:00:00', 2000, 'PUBLISHED', NULL, NULL, UTC_TIMESTAMP()),
-- Event 8: Python Developers meet (Tech)
(3, 'Python Developers Meetup', 'Networking and talks for Pythonistas', 1, 5, '2026-03-10 18:00:00', '2026-03-10 21:00:00', 500, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 9: Classical Vocal Concert (Music)
(4, 'Classical Vocal Evening', 'Hindustani and Carnatic classical music', 2, 7, '2026-05-20 18:00:00', '2026-05-20 21:30:00', 1200, 'APPROVED', 2, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 10: Marketing Strategy Seminar (Business)
(5, 'Marketing Strategy 2026', 'Seminar on digital marketing trends', 3, 3, '2026-04-02 09:30:00', '2026-04-02 17:30:00', 400, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 11: Yoga for Life (Sports)
(3, 'Yoga for Life Workshop', 'Learn holistic living through Yoga', 5, 4, '2026-06-21 06:00:00', '2026-06-21 10:00:00', 1000, 'APPROVED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
-- Event 12: Robotics for Kids (Education)
(5, 'Robotics for Kids', 'Building simple robots with LEGO', 4, 4, '2026-05-02 10:00:00', '2026-05-02 16:00:00', 100, 'DRAFT', NULL, NULL, UTC_TIMESTAMP()),
-- Event 13: Indie Rock Night (Music)
(4, 'Indie Rock Bangalore', 'Live indie rock band performances', 2, 2, '2026-04-15 19:00:00', '2026-04-15 23:30:00', 2000, 'CANCELLED', NULL, NULL, UTC_TIMESTAMP()),
-- Event 14: Tech Founders Summit (Business)
(5, 'Tech Founders Summit', 'Discussions with unicorns founders', 3, 5, '2026-03-30 10:00:00', '2026-03-30 17:00:00', 500, 'PUBLISHED', NULL, NULL, UTC_TIMESTAMP()),
-- Event 15: Winter Hackathon (Tech)
(3, 'Winter Hackathon 2026', '48-hour coding marathon', 1, 2, '2026-01-10 09:00:00', '2026-01-12 12:00:00', 400, 'COMPLETED', 1, UTC_TIMESTAMP(), UTC_TIMESTAMP());

-- =========================
-- 6. Tickets
-- =========================
INSERT INTO tickets
(event_id, ticket_type, price, total_quantity, available_quantity)
VALUES
-- Event 1 (1000 capacity)
(1, 'General Entrance', 1500.00, 800, 750),
(1, 'VIP Pass', 5000.00, 200, 180),
-- Event 2 (500 capacity)
(2, 'Early Bird', 800.00, 200, 150),
(2, 'Standard', 1200.00, 300, 300),
-- Event 3 (300 capacity)
(3, 'Standard Pass', 2000.00, 300, 250),
-- Event 4 (200 capacity)
(4, 'Full Workshop Pass', 4500.00, 200, 190),
-- Event 6 (800 capacity)
(6, 'Visitor Ticket', 100.00, 800, 780),
-- Event 8 (500 capacity)
(8, 'Basic Ticket', 500.00, 500, 480),
-- Event 9 (1200 capacity)
(9, 'Stalls', 1000.00, 700, 650),
(9, 'Balcony', 500.00, 500, 490),
-- Event 11 (1000 capacity)
(11, 'General registration', 0.00, 1000, 950),
-- Event 15 (Hackathon)
(15, 'Developer Pass', 200.00, 400, 0);

-- =========================
-- 7. Offers
-- =========================
INSERT INTO offers
(event_id, code, discount_percentage, valid_from, valid_to)
VALUES
(1, 'FUTURE20', 20, '2026-02-01 00:00:00', '2026-03-15 23:59:59'),
(3, 'STARTUP50', 50, '2026-03-01 00:00:00', '2026-03-20 23:59:59'),
(4, 'LEARN10', 10, '2026-04-01 00:00:00', '2026-05-01 23:59:59'),
(8, 'PYTHONEER', 15, '2026-02-15 00:00:00', '2026-03-05 23:59:59');

-- =========================
-- 8. Registrations
-- =========================
INSERT INTO registrations
(user_id, event_id, registration_date, status)
VALUES
(7, 1, UTC_TIMESTAMP(), 'CONFIRMED'),
(8, 1, UTC_TIMESTAMP(), 'CONFIRMED'),
(9, 3, UTC_TIMESTAMP(), 'CONFIRMED'),
(10, 4, UTC_TIMESTAMP(), 'CONFIRMED'),
(11, 6, UTC_TIMESTAMP(), 'CONFIRMED'),
(12, 8, UTC_TIMESTAMP(), 'CONFIRMED'),
(13, 9, UTC_TIMESTAMP(), 'CONFIRMED'),
(14, 11, UTC_TIMESTAMP(), 'CONFIRMED'),
(15, 1, UTC_TIMESTAMP(), 'CONFIRMED'),
(16, 15, '2026-01-05 10:20:00', 'CONFIRMED');

-- =========================
-- 9. Registration Tickets
-- =========================
INSERT INTO registration_tickets
(registration_id, ticket_id, quantity)
VALUES
(1, 1, 1),
(2, 2, 2),
(3, 5, 1),
(4, 6, 1),
(5, 7, 2),
(6, 8, 1),
(7, 9, 3),
(8, 11, 1),
(9, 1, 1),
(10, 12, 1);

-- =========================
-- 10. Payments
-- =========================
INSERT INTO payments
(registration_id, amount, payment_method, payment_status, created_at, offer_id)
VALUES
(1, 1200.00, 'Credit Card', 'SUCCESS', UTC_TIMESTAMP(), 1),
(2, 10000.00, 'Debit Card', 'SUCCESS', UTC_TIMESTAMP(), NULL),
(3, 1000.00, 'UPI', 'SUCCESS', UTC_TIMESTAMP(), 2),
(4, 4500.00, 'Net Banking', 'SUCCESS', UTC_TIMESTAMP(), NULL),
(5, 200.00, 'UPI', 'SUCCESS', UTC_TIMESTAMP(), NULL),
(6, 425.00, 'Credit Card', 'SUCCESS', UTC_TIMESTAMP(), 4),
(7, 3000.00, 'Debit Card', 'SUCCESS', UTC_TIMESTAMP(), NULL),
(8, 0.00, 'FREE', 'SUCCESS', UTC_TIMESTAMP(), NULL),
(9, 1500.00, 'UPI', 'SUCCESS', UTC_TIMESTAMP(), NULL),
(10, 200.00, 'Net Banking', 'SUCCESS', '2026-01-05 10:25:00', NULL);

-- =========================
-- 11. Offer Usage
-- =========================
INSERT INTO offer_usages
(offer_id, user_id, registration_id, used_at)
VALUES
(1, 7, 1, UTC_TIMESTAMP()),
(2, 9, 3, UTC_TIMESTAMP()),
(4, 12, 6, UTC_TIMESTAMP());

-- =========================
-- 12. Feedback
-- =========================
INSERT INTO feedback
(event_id, user_id, rating, comments, submitted_at)
VALUES
(15, 16, 5, 'Best coding experience ever!', '2026-01-13 14:00:00');

-- =========================
-- 13. Notifications
-- =========================
INSERT INTO notifications
(user_id, message, type, created_at, read_status)
VALUES
(7, 'Your registration for Future Tech Summit 2026 is confirmed.', 'EVENT', UTC_TIMESTAMP(), 1),
(9, 'Flash Sale! Get 50% off on Startup Pitch Day!', 'PROMOTION', UTC_TIMESTAMP(), 0),
(13, 'Reminder: Classical Vocal Evening starts in 2 hours.', 'REMINDER', UTC_TIMESTAMP(), 0);

-- =========================
-- 14. System Logs
-- =========================
INSERT INTO system_logs
(user_id, action, entity, entity_id, message, created_at)
VALUES
(1, 'APPROVE', 'EVENT', 1, 'Admin approved Future Tech Summit', UTC_TIMESTAMP()),
(1, 'APPROVE', 'EVENT', 3, 'Admin approved Startup Pitch Day', UTC_TIMESTAMP()),
(2, 'APPROVE', 'EVENT', 9, 'Admin approved Classical Vocal Concert', UTC_TIMESTAMP());

-- ============================================================================
-- End of Sample Data
-- ============================================================================

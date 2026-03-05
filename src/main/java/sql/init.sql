CREATE DATABASE  IF NOT EXISTS `event_management_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `event_management_db`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: event_management_db
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `unique_category_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Technology',1),(2,'Music',1),(3,'Business',1),(4,'Health & Wellness',1),(5,'Education',1),(6,'Arts & Culture',1),(7,'Sports',1),(8,'Food & Beverage',1),(9,'Networking',1),(10,'Entertainment',1),(11,'Infotainment',0);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `organizer_id` int NOT NULL,
  `title` varchar(150) NOT NULL,
  `description` text,
  `category_id` int NOT NULL,
  `venue_id` int NOT NULL,
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `capacity` int NOT NULL,
  `status` enum('DRAFT','PUBLISHED','APPROVED','CANCELLED','COMPLETED') NOT NULL,
  `approved_by` int DEFAULT NULL,
  `approved_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `organizer_id` (`organizer_id`),
  KEY `category_id` (`category_id`),
  KEY `venue_id` (`venue_id`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`organizer_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `events_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`),
  CONSTRAINT `events_ibfk_3` FOREIGN KEY (`venue_id`) REFERENCES `venues` (`venue_id`),
  CONSTRAINT `events_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (1,4,'AI & Machine Learning Summit 2026','Deep-dive into the latest advances in AI and ML with industry experts from across India.',1,1,'2026-04-10 09:00:00','2026-04-10 18:00:00',1000,'DRAFT',NULL,NULL,'2026-02-01 10:00:00',NULL),(2,6,'Classical Carnatic Music Evening','An enchanting evening of classical Carnatic music performed by acclaimed artists.',2,10,'2026-04-20 18:30:00','2026-04-20 21:30:00',400,'DRAFT',NULL,NULL,'2026-02-03 11:00:00',NULL),(3,9,'Startup Founders Bootcamp','Intensive two-day bootcamp for early-stage startup founders covering funding, product and growth.',3,3,'2026-05-05 09:00:00','2026-05-06 17:00:00',300,'DRAFT',NULL,NULL,'2026-02-05 09:30:00',NULL),(4,4,'Yoga & Mindfulness Retreat','A full-day wellness retreat combining yoga sessions, meditation and nutrition workshops.',4,5,'2026-05-15 07:00:00','2026-05-15 17:00:00',200,'DRAFT',NULL,NULL,'2026-02-06 08:00:00',NULL),(5,6,'Digital Marketing Masterclass','Hands-on masterclass on SEO, social media advertising and content strategy for 2026.',5,9,'2026-05-25 10:00:00','2026-05-25 16:00:00',150,'DRAFT',NULL,NULL,'2026-02-07 10:00:00',NULL),(6,4,'Chennai Tech Fest 2026','Tamil Nadu\'s biggest technology festival featuring talks, hackathons and product demos.',1,1,'2026-03-15 09:00:00','2026-03-16 18:00:00',3000,'PUBLISHED',NULL,NULL,'2026-02-08 09:00:00',NULL),(7,6,'Rock Night Chennai','An electrifying live rock concert featuring top indie bands from South India.',2,7,'2026-03-22 17:00:00','2026-03-22 22:00:00',2000,'PUBLISHED',NULL,NULL,'2026-02-09 10:00:00',NULL),(8,9,'Women in Business Forum','Inspiring panel discussions and networking sessions celebrating women entrepreneurs.',3,2,'2026-03-28 09:00:00','2026-03-28 17:00:00',500,'PUBLISHED',NULL,NULL,'2026-02-10 09:00:00',NULL),(9,4,'Half Marathon – Coimbatore','A scenic 21 km half marathon through the streets of Coimbatore. Open to all fitness levels.',7,3,'2026-04-05 05:30:00','2026-04-05 10:00:00',2000,'PUBLISHED',NULL,NULL,'2026-02-11 08:00:00',NULL),(10,6,'Culinary Arts Festival','A two-day food festival showcasing cuisines from all 38 districts of Tamil Nadu.',8,5,'2026-04-12 10:00:00','2026-04-13 21:00:00',1500,'PUBLISHED',NULL,NULL,'2026-02-12 10:00:00',NULL),(11,9,'Blockchain & Web3 Conference','Expert sessions on DeFi, NFTs, smart contracts and the future of decentralised finance.',1,6,'2026-04-18 09:00:00','2026-04-18 18:00:00',400,'PUBLISHED',NULL,NULL,'2026-02-13 09:00:00',NULL),(12,4,'International Robotics Expo','Showcasing cutting-edge robotics innovations from academic institutions and industry leaders.',1,1,'2026-03-05 09:00:00','2026-03-06 18:00:00',4000,'PUBLISHED',1,'2026-02-10 12:00:00','2026-02-08 10:00:00','2026-02-23 18:06:23'),(13,6,'Trichy Business Conclave','Annual gathering of business leaders, policy makers and investors in central Tamil Nadu.',3,4,'2026-03-10 09:00:00','2026-03-10 18:00:00',800,'APPROVED',1,'2026-02-11 12:00:00','2026-02-09 10:00:00',NULL),(14,9,'Photography & Film Workshop','Two-day workshop on portrait photography, cinematography and post-production techniques.',6,8,'2026-03-18 09:00:00','2026-03-19 17:00:00',100,'APPROVED',1,'2026-02-12 12:00:00','2026-02-10 09:00:00',NULL),(15,4,'Cyber Security Summit','Sessions on ethical hacking, data privacy, zero-trust architecture and compliance.',1,2,'2026-03-25 09:00:00','2026-03-25 18:00:00',600,'APPROVED',1,'2026-02-13 11:00:00','2026-02-11 10:00:00',NULL),(16,6,'Tamil Literary Fest','A celebration of Tamil literature featuring readings, debates and awards.',6,6,'2026-04-01 10:00:00','2026-04-02 19:00:00',500,'APPROVED',1,'2026-02-14 11:00:00','2026-02-12 10:00:00',NULL),(17,9,'HR & Future of Work Summit','Industry leaders discuss remote work, AI-driven HR tools and employee well-being.',9,10,'2026-04-07 09:00:00','2026-04-07 17:00:00',400,'APPROVED',1,'2026-02-15 10:00:00','2026-02-13 09:00:00',NULL),(18,4,'Madurai Heritage Walk & Talk','Guided cultural walk through Madurai\'s historic temples followed by panel discussions.',6,5,'2026-04-14 07:00:00','2026-04-14 13:00:00',200,'APPROVED',1,'2026-02-15 12:00:00','2026-02-13 10:00:00',NULL),(19,6,'Kids Science Carnival','Hands-on science experiments, robotics demos and astronomy sessions for children aged 6-15.',5,9,'2026-04-22 09:00:00','2026-04-22 17:00:00',300,'APPROVED',1,'2026-02-16 11:00:00','2026-02-14 09:00:00',NULL),(20,4,'Data Science Bootcamp','Intensive weekend bootcamp covering Python, pandas, visualisation and ML basics.',1,8,'2026-01-18 09:00:00','2026-01-19 17:00:00',120,'COMPLETED',1,'2025-12-20 12:00:00','2025-12-15 10:00:00',NULL),(21,6,'South India Startup Expo','Annual showcase of the most promising startups from Tamil Nadu, Kerala and Karnataka.',3,3,'2026-01-25 10:00:00','2026-01-26 18:00:00',2000,'COMPLETED',1,'2025-12-28 12:00:00','2025-12-20 09:00:00',NULL),(22,9,'Wellness & Mental Health Conclave','Experts discuss stress management, corporate wellness programs and mental health awareness.',4,2,'2026-02-01 09:00:00','2026-02-01 17:00:00',400,'COMPLETED',1,'2026-01-05 11:00:00','2026-01-01 10:00:00',NULL),(23,4,'Indo-Japanese Cultural Festival','Celebrating the friendship between India and Japan through art, food, music and craft.',6,1,'2026-02-07 10:00:00','2026-02-08 20:00:00',2000,'COMPLETED',1,'2026-01-10 12:00:00','2026-01-05 09:00:00',NULL),(24,6,'Cloud Computing & DevOps Conference','Deep-dive talks on Kubernetes, CI/CD pipelines, cloud cost optimisation and SRE practices.',1,6,'2026-02-14 09:00:00','2026-02-14 18:00:00',400,'COMPLETED',1,'2026-01-18 12:00:00','2026-01-12 10:00:00',NULL),(25,9,'Coimbatore Marathon 2026','Full marathon and 10K fun run through Coimbatore\'s green corridors. Chip-timed race.',7,3,'2026-02-21 05:00:00','2026-02-21 11:00:00',3000,'COMPLETED',1,'2026-01-25 12:00:00','2026-01-20 08:00:00',NULL),(26,4,'E-Sports Championship Chennai','Competitive gaming tournament across BGMI, Valorant and FIFA. Cancelled due to venue unavailability.',10,7,'2026-02-28 10:00:00','2026-03-01 20:00:00',1500,'CANCELLED',NULL,NULL,'2026-01-15 10:00:00',NULL),(27,6,'Fashion & Design Expo','Showcase of emerging fashion designers from Tamil Nadu. Cancelled due to low registrations.',6,10,'2026-03-08 10:00:00','2026-03-09 19:00:00',500,'CANCELLED',NULL,NULL,'2026-01-20 10:00:00',NULL),(28,9,'Real Estate Investment Summit','Panel discussions on property market trends, REITs and NRI investments. Cancelled.',3,4,'2026-03-12 09:00:00','2026-03-12 17:00:00',600,'CANCELLED',NULL,NULL,'2026-01-22 09:00:00',NULL),(29,4,'Stand-Up Comedy Night','A hilarious evening with top comedians from Chennai\'s underground circuit. Show cancelled.',10,10,'2026-03-20 19:00:00','2026-03-20 22:00:00',400,'CANCELLED',NULL,NULL,'2026-01-28 10:00:00',NULL),(30,6,'Digital Art & NFT Exhibition','Curated exhibition of digital artworks and NFT drops. Event cancelled by organizer.',6,9,'2026-03-30 10:00:00','2026-03-30 18:00:00',200,'CANCELLED',NULL,NULL,'2026-02-01 09:00:00',NULL),(31,4,'Chennai Music & Arts Festival','A grand two-day outdoor festival celebrating indie music, street art, and spoken word performances across multiple stages.',2,7,'2026-04-05 14:00:00','2026-04-06 22:00:00',4000,'PUBLISHED',NULL,NULL,'2026-02-20 09:00:00',NULL),(32,6,'Tamil Nadu Entrepreneurs Summit','Premier annual gathering of founders, investors and ecosystem builders. Keynotes, fireside chats, investor meets and workshops.',3,1,'2026-04-18 09:00:00','2026-04-19 18:00:00',3000,'PUBLISHED',NULL,NULL,'2026-02-21 10:00:00',NULL),(33,9,'FitFest Coimbatore','A full-day wellness and fitness festival featuring yoga, Zumba, CrossFit demos, nutrition talks and a 5K fun run.',4,3,'2026-04-26 06:00:00','2026-04-26 18:00:00',2000,'PUBLISHED',NULL,NULL,'2026-02-22 08:00:00',NULL),(34,4,'DevConf India 2026','India\'s favourite developer conference — two days of technical talks, workshops, open-source sprints and career fair.',1,1,'2026-05-10 09:00:00','2026-05-11 18:00:00',5000,'PUBLISHED',NULL,NULL,'2026-02-22 11:00:00',NULL),(35,6,'Madurai Food & Culture Carnival','Celebrate Madurai\'s rich culinary heritage with live cooking demos, street food stalls, folk performances and heritage walks.',8,5,'2026-05-17 10:00:00','2026-05-18 21:00:00',2500,'PUBLISHED',NULL,NULL,'2026-02-23 09:00:00',NULL),(36,9,'Leadership & Innovation Conclave','Executive-level conclave bringing together CEOs, CXOs and thought leaders to discuss innovation, strategy and future of work.',9,2,'2026-05-24 09:00:00','2026-05-24 18:00:00',400,'PUBLISHED',NULL,NULL,'2026-02-23 10:00:00',NULL),(37,4,'Tech conference 2026 on LLMs','You can learn the ultimate llm tool to procced your professional life into something unique.You can learn the ultimate llm tool to procced your professional life into something unique.You can learn the ultimate llm tool to procced your professional life into something unique.',1,10,'2026-02-25 00:00:00','2026-02-26 14:00:00',350,'DRAFT',NULL,NULL,'2026-02-23 18:05:03',NULL);
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `feedback_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `user_id` int NOT NULL,
  `rating` int DEFAULT NULL,
  `comments` text,
  `submitted_at` datetime NOT NULL,
  PRIMARY KEY (`feedback_id`),
  UNIQUE KEY `unique_user_event` (`event_id`,`user_id`),
  KEY `event_id` (`event_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,20,2,5,'Excellent bootcamp! The hands-on sessions were incredibly practical.','2026-01-20 10:00:00'),(2,20,3,4,'Very well organised. Would have loved more time on deep learning.','2026-01-20 11:00:00'),(3,21,5,5,'Amazing exposure to so many startups. Met some great founders!','2026-01-27 09:00:00'),(4,21,7,4,'Great event. Networking was the best part.','2026-01-27 10:00:00'),(5,21,8,4,'Well curated expo. Some stalls were a bit crowded.','2026-01-27 11:00:00'),(6,22,5,5,'The mental health sessions were truly eye-opening and impactful.','2026-02-02 09:00:00'),(7,22,7,4,'Very informative. Appreciated the open Q&A with specialists.','2026-02-02 10:00:00'),(8,22,10,5,'Highly recommend. Speakers were top class.','2026-02-02 11:00:00'),(9,23,2,5,'A beautiful fusion of two cultures. The food stalls were amazing!','2026-02-09 10:00:00'),(10,23,3,4,'Loved the art installations and live performances.','2026-02-09 11:00:00'),(11,23,8,5,'One of the best cultural events I have attended in Chennai.','2026-02-09 12:00:00'),(12,24,2,5,'Top-notch speakers and very relevant content for cloud professionals.','2026-02-15 09:00:00'),(13,24,5,4,'Great conference. The DevOps workshop was especially useful.','2026-02-15 10:00:00'),(14,25,3,5,'Fantastic race route and excellent event management!','2026-02-22 08:00:00'),(15,25,7,4,'Really enjoyed it. Would love a full marathon next year.','2026-02-22 09:00:00'),(16,25,10,5,'Perfect organisation. Hydration stations were well placed.','2026-02-22 10:00:00'),(17,21,2,5,NULL,'2026-03-02 16:36:56');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `message` text NOT NULL,
  `type` varchar(30) NOT NULL,
  `created_at` datetime NOT NULL,
  `read_status` tinyint(1) NOT NULL,
  PRIMARY KEY (`notification_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,4,'Your event \"Chennai Tech Fest 2026\" has been published successfully.','EVENT_PUBLISHED','2026-02-08 09:05:00',1),(2,4,'Your event \"International Robotics Expo\" has been approved by admin.','EVENT_APPROVED','2026-02-10 12:05:00',1),(3,6,'Your event \"Trichy Business Conclave\" has been approved by admin.','EVENT_APPROVED','2026-02-11 12:05:00',1),(4,9,'Your event \"Photography & Film Workshop\" has been approved.','EVENT_APPROVED','2026-02-12 12:05:00',1),(5,4,'Your event \"Cyber Security Summit\" has been approved by admin.','EVENT_APPROVED','2026-02-13 11:05:00',1),(6,2,'Your registration for Data Science Bootcamp is confirmed!','REGISTRATION','2026-01-05 10:16:00',1),(7,3,'Your registration for Data Science Bootcamp is confirmed!','REGISTRATION','2026-01-06 11:01:00',1),(8,5,'Your registration for South India Startup Expo is confirmed!','REGISTRATION','2026-01-08 09:31:00',1),(9,7,'Your registration for South India Startup Expo is confirmed!','REGISTRATION','2026-01-09 10:01:00',1),(10,8,'Your registration for Wellness & Mental Health Conclave is confirmed!','REGISTRATION','2026-01-17 11:01:00',1),(11,2,'Your registration for Coimbatore Marathon 2026 is confirmed!','REGISTRATION','2026-02-05 10:01:00',1),(12,3,'Coimbatore Marathon 2026 is tomorrow! Get ready.','REMINDER','2026-02-20 09:00:00',1),(13,1,'New event \"AI & Machine Learning Summit 2026\" submitted for review.','ADMIN_ALERT','2026-02-01 10:05:00',1),(14,1,'New event \"Startup Founders Bootcamp\" submitted for review.','ADMIN_ALERT','2026-02-05 09:35:00',1),(15,4,'Offer code TECHFEST20 is now live for Chennai Tech Fest 2026.','OFFER','2026-02-08 09:10:00',1),(16,6,'Your event \"E-Sports Championship\" has been cancelled.','EVENT_CANCELLED','2026-02-15 10:00:00',1),(17,10,'Thank you for attending Coimbatore Marathon 2026! Share your feedback.','POST_EVENT','2026-02-21 12:00:00',0),(18,8,'Thank you for attending Coimbatore Marathon 2026! Share your feedback.','POST_EVENT','2026-02-21 12:00:00',0),(19,4,'Your event \"Chennai Music & Arts Festival\" has been published successfully.','EVENT_PUBLISHED','2026-02-20 09:05:00',1),(20,6,'Your event \"Tamil Nadu Entrepreneurs Summit\" has been published successfully.','EVENT_PUBLISHED','2026-02-21 10:05:00',0),(21,9,'Your event \"FitFest Coimbatore\" has been published successfully.','EVENT_PUBLISHED','2026-02-22 08:05:00',0),(22,4,'Your event \"DevConf India 2026\" has been published successfully.','EVENT_PUBLISHED','2026-02-22 11:05:00',1),(23,6,'Your event \"Madurai Food & Culture Carnival\" has been published successfully.','EVENT_PUBLISHED','2026-02-23 09:05:00',0),(24,9,'Your event \"Leadership & Innovation Conclave\" has been published successfully.','EVENT_PUBLISHED','2026-02-23 10:05:00',0),(25,2,'Registration confirmed. Amount paid: ₹499.0','EVENT','2026-03-02 22:06:27',0);
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offer_usages`
--

DROP TABLE IF EXISTS `offer_usages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offer_usages` (
  `offer_usage_id` int NOT NULL AUTO_INCREMENT,
  `offer_id` int NOT NULL,
  `user_id` int NOT NULL,
  `registration_id` int NOT NULL,
  `used_at` datetime NOT NULL,
  PRIMARY KEY (`offer_usage_id`),
  UNIQUE KEY `offer_id` (`offer_id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `registration_id` (`registration_id`),
  CONSTRAINT `offer_usages_ibfk_1` FOREIGN KEY (`offer_id`) REFERENCES `offers` (`offer_id`),
  CONSTRAINT `offer_usages_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `offer_usages_ibfk_3` FOREIGN KEY (`registration_id`) REFERENCES `registrations` (`registration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offer_usages`
--

LOCK TABLES `offer_usages` WRITE;
/*!40000 ALTER TABLE `offer_usages` DISABLE KEYS */;
/*!40000 ALTER TABLE `offer_usages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offers`
--

DROP TABLE IF EXISTS `offers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offers` (
  `offer_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `code` varchar(30) NOT NULL,
  `discount_percentage` int DEFAULT NULL,
  `valid_from` datetime DEFAULT NULL,
  `valid_to` datetime DEFAULT NULL,
  PRIMARY KEY (`offer_id`),
  UNIQUE KEY `unique_event_code` (`event_id`,`code`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `offers_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offers`
--

LOCK TABLES `offers` WRITE;
/*!40000 ALTER TABLE `offers` DISABLE KEYS */;
INSERT INTO `offers` VALUES (1,6,'TECHFEST20',20,'2026-02-08 00:00:00','2026-03-10 23:59:59'),(2,7,'ROCK15',15,'2026-02-09 00:00:00','2026-03-18 23:59:59'),(3,8,'WOMEN10',10,'2026-02-10 00:00:00','2026-03-25 23:59:59'),(4,9,'RUN10',10,'2026-02-11 00:00:00','2026-04-01 23:59:59'),(5,12,'ROBOT25',25,'2026-02-08 00:00:00','2026-02-28 23:59:59'),(6,15,'CYBER15',15,'2026-02-11 00:00:00','2026-03-20 23:59:59'),(7,17,'HR20',20,'2026-02-13 00:00:00','2026-04-03 23:59:59'),(8,31,'ARTFEST15',15,'2026-02-20 00:00:00','2026-03-31 23:59:59'),(9,31,'VIPMUSIC10',10,'2026-02-20 00:00:00','2026-03-15 23:59:59'),(10,32,'SUMMIT20',20,'2026-02-21 00:00:00','2026-04-10 23:59:59'),(11,32,'INVESTOR10',10,'2026-02-21 00:00:00','2026-04-10 23:59:59'),(12,33,'FIT25',25,'2026-02-22 00:00:00','2026-04-20 23:59:59'),(13,34,'DEV20',20,'2026-02-22 00:00:00','2026-05-01 23:59:59'),(14,35,'FOODIE10',10,'2026-02-23 00:00:00','2026-05-10 23:59:59'),(15,36,'LEADER15',15,'2026-02-23 00:00:00','2026-05-18 23:59:59');
/*!40000 ALTER TABLE `offers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `registration_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(30) NOT NULL,
  `payment_status` varchar(30) NOT NULL,
  `created_at` datetime NOT NULL,
  `offer_id` int DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `registration_id` (`registration_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`registration_id`) REFERENCES `registrations` (`registration_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,1,3999.00,'CARD','SUCCESS','2026-01-05 10:16:00',NULL),(2,2,3999.00,'UPI','SUCCESS','2026-01-06 11:01:00',NULL),(3,3,399.00,'UPI','SUCCESS','2026-01-08 09:31:00',NULL),(4,4,399.00,'CARD','SUCCESS','2026-01-09 10:01:00',NULL),(5,5,399.00,'UPI','SUCCESS','2026-01-10 11:01:00',NULL),(6,6,399.00,'CARD','SUCCESS','2026-01-11 09:01:00',NULL),(7,7,399.00,'UPI','SUCCESS','2026-01-12 10:31:00',NULL),(8,8,399.00,'CARD','SUCCESS','2026-01-13 11:01:00',NULL),(9,9,799.00,'CARD','SUCCESS','2026-01-15 09:01:00',NULL),(10,10,799.00,'UPI','SUCCESS','2026-01-16 10:01:00',NULL),(11,11,799.00,'CARD','SUCCESS','2026-01-17 11:01:00',NULL),(12,12,799.00,'UPI','SUCCESS','2026-01-18 09:31:00',NULL),(13,13,299.00,'CARD','SUCCESS','2026-01-20 10:01:00',NULL),(14,14,299.00,'UPI','SUCCESS','2026-01-21 11:01:00',NULL),(15,15,299.00,'CARD','SUCCESS','2026-01-22 09:31:00',NULL),(16,16,299.00,'UPI','SUCCESS','2026-01-23 10:31:00',NULL),(17,17,299.00,'CARD','SUCCESS','2026-01-24 11:31:00',NULL),(18,18,299.00,'UPI','SUCCESS','2026-01-25 09:01:00',NULL),(19,19,1499.00,'CARD','SUCCESS','2026-01-28 09:01:00',NULL),(20,20,1499.00,'UPI','SUCCESS','2026-01-29 10:01:00',NULL),(21,21,1499.00,'CARD','SUCCESS','2026-01-30 11:01:00',NULL),(22,22,499.00,'UPI','SUCCESS','2026-02-01 08:01:00',NULL),(23,23,499.00,'CARD','SUCCESS','2026-02-02 09:01:00',NULL),(24,24,499.00,'UPI','SUCCESS','2026-02-03 10:01:00',NULL),(25,25,499.00,'CARD','SUCCESS','2026-02-04 09:31:00',NULL),(26,26,499.00,'UPI','SUCCESS','2026-02-05 10:01:00',NULL),(27,27,499.00,'CARD','SUCCESS','2026-02-06 11:01:00',NULL),(28,28,499.00,'CARD','SUCCESS','2026-03-02 22:06:26',NULL);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registration_tickets`
--

DROP TABLE IF EXISTS `registration_tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registration_tickets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `registration_id` int NOT NULL,
  `ticket_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `registration_id` (`registration_id`),
  KEY `ticket_id` (`ticket_id`),
  CONSTRAINT `registration_tickets_ibfk_1` FOREIGN KEY (`registration_id`) REFERENCES `registrations` (`registration_id`),
  CONSTRAINT `registration_tickets_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`ticket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registration_tickets`
--

LOCK TABLES `registration_tickets` WRITE;
/*!40000 ALTER TABLE `registration_tickets` DISABLE KEYS */;
INSERT INTO `registration_tickets` VALUES (1,1,15,1),(2,2,15,1),(3,3,16,1),(4,4,16,1),(5,5,16,1),(6,6,16,1),(7,7,16,1),(8,8,16,1),(9,9,17,1),(10,10,17,1),(11,11,17,1),(12,12,17,1),(13,13,18,1),(14,14,18,1),(15,15,18,1),(16,16,18,1),(17,17,18,1),(18,18,18,1),(19,19,19,1),(20,20,19,1),(21,21,19,1),(22,22,20,1),(23,23,20,1),(24,24,20,1),(25,25,20,1),(26,26,20,1),(27,27,20,1),(28,28,1,1);
/*!40000 ALTER TABLE `registration_tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrations`
--

DROP TABLE IF EXISTS `registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registrations` (
  `registration_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `event_id` int NOT NULL,
  `registration_date` datetime NOT NULL,
  `status` enum('CONFIRMED','CANCELLED') NOT NULL,
  PRIMARY KEY (`registration_id`),
  KEY `user_id` (`user_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `registrations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `registrations_ibfk_2` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrations`
--

LOCK TABLES `registrations` WRITE;
/*!40000 ALTER TABLE `registrations` DISABLE KEYS */;
INSERT INTO `registrations` VALUES (1,2,20,'2026-01-05 10:15:00','CONFIRMED'),(2,3,20,'2026-01-06 11:00:00','CONFIRMED'),(3,5,21,'2026-01-08 09:30:00','CONFIRMED'),(4,7,21,'2026-01-09 10:00:00','CONFIRMED'),(5,8,21,'2026-01-10 11:00:00','CONFIRMED'),(6,10,21,'2026-01-11 09:00:00','CONFIRMED'),(7,2,21,'2026-01-12 10:30:00','CONFIRMED'),(8,3,21,'2026-01-13 11:00:00','CONFIRMED'),(9,5,22,'2026-01-15 09:00:00','CONFIRMED'),(10,7,22,'2026-01-16 10:00:00','CONFIRMED'),(11,8,22,'2026-01-17 11:00:00','CONFIRMED'),(12,10,22,'2026-01-18 09:30:00','CONFIRMED'),(13,2,23,'2026-01-20 10:00:00','CONFIRMED'),(14,3,23,'2026-01-21 11:00:00','CONFIRMED'),(15,5,23,'2026-01-22 09:30:00','CONFIRMED'),(16,7,23,'2026-01-23 10:30:00','CONFIRMED'),(17,8,23,'2026-01-24 11:30:00','CONFIRMED'),(18,10,23,'2026-01-25 09:00:00','CONFIRMED'),(19,2,24,'2026-01-28 09:00:00','CONFIRMED'),(20,5,24,'2026-01-29 10:00:00','CONFIRMED'),(21,8,24,'2026-01-30 11:00:00','CONFIRMED'),(22,3,25,'2026-02-01 08:00:00','CONFIRMED'),(23,7,25,'2026-02-02 09:00:00','CONFIRMED'),(24,10,25,'2026-02-03 10:00:00','CONFIRMED'),(25,5,25,'2026-02-04 09:30:00','CONFIRMED'),(26,2,25,'2026-02-05 10:00:00','CONFIRMED'),(27,8,25,'2026-02-06 11:00:00','CONFIRMED'),(28,2,6,'2026-03-02 22:06:26','CONFIRMED');
/*!40000 ALTER TABLE `registrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN',1,'2026-01-29 08:40:40'),(2,'ATTENDEE',1,'2026-01-29 08:40:40'),(3,'ORGANIZER',1,'2026-01-29 08:40:40');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_logs`
--

DROP TABLE IF EXISTS `system_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_logs` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `action` varchar(50) NOT NULL,
  `entity` varchar(50) NOT NULL,
  `entity_id` int DEFAULT NULL,
  `message` text NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_logs`
--

LOCK TABLES `system_logs` WRITE;
/*!40000 ALTER TABLE `system_logs` DISABLE KEYS */;
INSERT INTO `system_logs` VALUES (1,1,'APPROVE','event',12,'Admin approved International Robotics Expo','2026-02-10 12:00:00'),(2,1,'APPROVE','event',13,'Admin approved Trichy Business Conclave','2026-02-11 12:00:00'),(3,1,'APPROVE','event',14,'Admin approved Photography & Film Workshop','2026-02-12 12:00:00'),(4,1,'APPROVE','event',15,'Admin approved Cyber Security Summit','2026-02-13 11:00:00'),(5,1,'APPROVE','event',16,'Admin approved Tamil Literary Fest','2026-02-14 11:00:00'),(6,1,'APPROVE','event',17,'Admin approved HR & Future of Work Summit','2026-02-15 10:00:00'),(7,1,'APPROVE','event',18,'Admin approved Madurai Heritage Walk & Talk','2026-02-15 12:00:00'),(8,1,'APPROVE','event',19,'Admin approved Kids Science Carnival','2026-02-16 11:00:00'),(9,4,'CREATE','event',6,'Organizer created Chennai Tech Fest 2026','2026-02-08 09:00:00'),(10,6,'CREATE','event',7,'Organizer created Rock Night Chennai','2026-02-09 10:00:00'),(11,9,'CREATE','event',8,'Organizer created Women in Business Forum','2026-02-10 09:00:00'),(12,4,'CANCEL','event',26,'Organizer cancelled E-Sports Championship Chennai','2026-02-15 10:00:00'),(13,6,'CANCEL','event',27,'Organizer cancelled Fashion & Design Expo','2026-02-16 10:00:00'),(14,2,'REGISTER','event',20,'User registered for Data Science Bootcamp','2026-01-05 10:16:00'),(15,5,'REGISTER','event',21,'User registered for South India Startup Expo','2026-01-08 09:31:00'),(16,3,'REGISTER','event',25,'User registered for Coimbatore Marathon 2026','2026-02-01 08:01:00'),(17,0,'ERROR','TICKET',1,'Failed to get available tickets: No tickets configured for eventId: 1','2026-02-23 09:19:41'),(18,0,'ERROR','TICKET',2,'Failed to get available tickets: No tickets configured for eventId: 2','2026-02-23 09:19:41'),(19,0,'ERROR','TICKET',3,'Failed to get available tickets: No tickets configured for eventId: 3','2026-02-23 09:19:41'),(20,0,'ERROR','TICKET',4,'Failed to get available tickets: No tickets configured for eventId: 4','2026-02-23 09:19:41'),(21,0,'ERROR','TICKET',5,'Failed to get available tickets: No tickets configured for eventId: 5','2026-02-23 09:19:41'),(22,0,'ERROR','TICKET',1,'Failed to get available tickets: No tickets configured for eventId: 1','2026-02-23 09:19:58'),(23,0,'ERROR','TICKET',2,'Failed to get available tickets: No tickets configured for eventId: 2','2026-02-23 09:19:58'),(24,0,'ERROR','TICKET',3,'Failed to get available tickets: No tickets configured for eventId: 3','2026-02-23 09:19:59'),(25,0,'ERROR','TICKET',4,'Failed to get available tickets: No tickets configured for eventId: 4','2026-02-23 09:19:59'),(26,0,'ERROR','TICKET',5,'Failed to get available tickets: No tickets configured for eventId: 5','2026-02-23 09:19:59'),(27,0,'ERROR','TICKET',1,'Failed to get available tickets: No tickets configured for eventId: 1','2026-02-23 09:20:04'),(28,0,'ERROR','TICKET',2,'Failed to get available tickets: No tickets configured for eventId: 2','2026-02-23 09:20:04'),(29,0,'ERROR','TICKET',3,'Failed to get available tickets: No tickets configured for eventId: 3','2026-02-23 09:20:04'),(30,0,'ERROR','TICKET',4,'Failed to get available tickets: No tickets configured for eventId: 4','2026-02-23 09:20:04'),(31,0,'ERROR','TICKET',5,'Failed to get available tickets: No tickets configured for eventId: 5','2026-02-23 09:20:04'),(32,0,'ERROR','TICKET',1,'Failed to get available tickets: No tickets configured for eventId: 1','2026-02-23 12:30:48'),(33,0,'ERROR','TICKET',2,'Failed to get available tickets: No tickets configured for eventId: 2','2026-02-23 12:30:48'),(34,0,'ERROR','TICKET',3,'Failed to get available tickets: No tickets configured for eventId: 3','2026-02-23 12:30:48'),(35,0,'ERROR','TICKET',4,'Failed to get available tickets: No tickets configured for eventId: 4','2026-02-23 12:30:48'),(36,0,'ERROR','TICKET',5,'Failed to get available tickets: No tickets configured for eventId: 5','2026-02-23 12:30:48'),(37,4,'CREATE','EVENT',37,'Event created in DRAFT state','2026-02-23 12:35:03'),(38,NULL,'CREATE','TICKET',12,'Ticket type created: VIP','2026-02-23 12:36:22'),(39,NULL,'CREATE','TICKET',12,'Ticket type created: BASIC','2026-02-23 12:36:22'),(40,NULL,'CREATE','TICKET',12,'Ticket type created: STANDARD','2026-02-23 12:36:23'),(41,NULL,'PUBLISH','EVENT',12,'Event published','2026-02-23 12:36:23'),(42,NULL,'UPDATE_STATUS','USER',7,'User status changed to SUSPENDED','2026-03-02 15:48:02'),(43,NULL,'UPDATE_STATUS','USER',7,'User status changed to ACTIVE','2026-03-02 15:48:10'),(44,NULL,'CREATE','CATEGORY',NULL,'Category created: Infotainment','2026-03-02 15:48:34'),(45,NULL,'UPDATE','CATEGORY',11,'Category name updated','2026-03-02 15:49:10'),(46,NULL,'DELETE','CATEGORY',11,'Category deactivated','2026-03-02 15:49:17'),(47,2,'REGISTER','EVENT',6,'User registered for event using ticket 1 with offer code ','2026-03-02 16:36:26'),(48,2,'SUBMIT_FEEDBACK','EVENT',21,'User submitted rating: 5','2026-03-02 16:36:56');
/*!40000 ALTER TABLE `system_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `ticket_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `ticket_type` varchar(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `total_quantity` int NOT NULL,
  `available_quantity` int NOT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (1,6,'General Admission',499.00,3000,2999),(2,7,'General Admission',799.00,2000,2000),(3,8,'Delegate Pass',999.00,500,500),(4,9,'Race Entry',349.00,2000,2000),(5,10,'Festival Pass',299.00,1500,1500),(6,11,'Conference Pass',1199.00,400,400),(7,12,'Expo Pass',599.00,4000,4000),(8,13,'Delegate Pass',1499.00,800,800),(9,14,'Workshop Seat',2499.00,100,100),(10,15,'Summit Pass',1299.00,600,600),(11,16,'Literary Pass',199.00,500,500),(12,17,'Summit Pass',1999.00,400,400),(13,18,'Heritage Pass',249.00,200,200),(14,19,'Kids Pass',149.00,300,300),(15,20,'Bootcamp Seat',3999.00,120,118),(16,21,'Expo Pass',399.00,2000,1994),(17,22,'Delegate Pass',799.00,400,396),(18,23,'Festival Pass',299.00,2000,1994),(19,24,'Conference Pass',1499.00,400,397),(20,25,'Race Entry',499.00,3000,2994),(21,31,'Day 1 Pass',499.00,2000,2000),(22,31,'Day 2 Pass',499.00,2000,2000),(23,31,'Weekend Combo Pass',899.00,1500,1500),(24,31,'VIP Weekend Pass',2499.00,200,200),(25,32,'Startup Founder Pass',1499.00,1500,1500),(26,32,'Investor Pass',4999.00,200,200),(27,32,'Workshop Only Pass',799.00,800,800),(28,32,'VIP All-Access Pass',7999.00,50,50),(29,33,'General Entry',199.00,1200,1200),(30,33,'Fitness + 5K Run Pass',399.00,600,600),(31,33,'Premium Wellness Pass',799.00,200,200),(32,34,'Community Pass',599.00,2500,2500),(33,34,'Conference Pass',1499.00,1500,1500),(34,34,'Workshop Add-on',999.00,800,800),(35,34,'Pro All-Access Pass',2999.00,300,300),(36,35,'Day 1 Entry',149.00,1500,1500),(37,35,'Day 2 Entry',149.00,1500,1500),(38,35,'Both Days Pass',249.00,1000,1000),(39,35,'Foodie VIP Pass',699.00,200,200),(40,36,'Standard Delegate',3999.00,250,250),(41,36,'Premium Delegate',6999.00,100,100),(42,36,'Corporate Table (5)',29999.00,10,10),(43,12,'VIP',2000.00,2000,2000),(44,12,'BASIC',1000.00,1500,1500),(45,12,'STANDARD',500.00,500,500);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `gender` varchar(15) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role_id` int NOT NULL,
  `status` enum('ACTIVE','SUSPENDED') NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `failed_attempts` int DEFAULT '0',
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'ADMIN 01','admin@ems.com','7878787878','Male','$2a$12$unmKJ.trAsOSmBqGDdlAA.uOg5lSG25hcWG/xMgJG3kIG3gAusNni',1,'ACTIVE','2026-01-29 14:11:05',NULL,0,'2026-03-02 16:18:51'),(2,'Ram gopal varma','user@ems.com','4545454545','Opt-out','$2a$12$pRyXSfgkf0lhZDfVTQllxeCLFSH8nEWda49z.0EvHo83/xFUwWtGu',2,'ACTIVE','2026-01-29 14:12:28','2026-02-23 15:21:10',0,'2026-03-02 16:35:43'),(3,'USER 02','user2@ems.com','7878787878','Male','$2a$12$6oe3kuQxy3CR6BA30TbqneYXRlr9F1aggIdtNP0hBBa2wMILFymZW',2,'ACTIVE','2026-01-29 14:13:22',NULL,0,NULL),(4,'ORGANIZER 3','organizer1@ems.com','7889786756','Opt-out','$2a$12$EK1qd3oXHEkIHOxuOz0xjOHSMeaqIw0bzZFf/GBRKVCcRUgn62AkS',3,'ACTIVE','2026-01-29 14:13:50',NULL,0,'2026-02-23 12:32:01'),(5,'Kavinkumar R','user03@ems.com',NULL,'Male','$2a$12$fwovTMBR/OaWSy/wsZufb.u24u1kdl3gS.3eNOsw3lU/eQ6bHOsoK',2,'ACTIVE','2026-02-14 16:43:34',NULL,0,'2026-02-14 16:15:28'),(6,'Organizer 03','organizer03@ems.com',NULL,'Male','$2a$12$DnvoEuhMN/Im6RbyamPIEeu1Pm7sahC3/5PZ3LYjVEj9nhAaYp.3u',3,'ACTIVE','2026-02-14 16:54:14',NULL,1,NULL),(7,'KK','kk@ems.com',NULL,'Male','$2a$12$hqN2kAnPUOYNVUkiJpUKB.9WX9VdB5jajywYBtnooBht5/qrH6vFy',2,'ACTIVE','2026-02-14 17:26:00',NULL,0,'2026-03-02 15:48:09'),(8,'Soma suntharam S','soma@yahoo.com',NULL,'Male','$2a$12$PtsFlMb0UVG3V43DyUDoDe1CUEAvSngtIf6h12q9SPkL7iMxzTC.K',2,'ACTIVE','2026-02-21 11:17:47',NULL,0,'2026-02-21 05:48:20'),(9,'Raghavan','raghavan@email.com',NULL,'Male','$2a$12$FP29L/CBL4fbsAwBSqDCq.JkXRZdnAXpJIC.kEDLOYmXfxGcECYQi',3,'ACTIVE','2026-02-21 11:20:19',NULL,0,'2026-02-21 05:50:33'),(10,'Muthu kirishnan','muthu@email.com','8610715314','Female','$2a$12$ooYXqruuePuEq5afK5v.w.WlcwYv9ZaBeMOwJXqkc/6nKpwbqtZ1e',2,'ACTIVE','2026-02-21 11:21:24',NULL,0,NULL),(11,'KK','kavin@ems.com','7567893456','Male','$2a$12$uwc0fq6TeSlGwVhEipVuoeUXV9UzzYaOECZiLKprKorJPKqnF1kDG',2,'ACTIVE','2026-03-02 19:27:15',NULL,0,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venues`
--

DROP TABLE IF EXISTS `venues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venues` (
  `venue_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `street` varchar(100) NOT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `pincode` varchar(10) NOT NULL,
  `max_capacity` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`venue_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venues`
--

LOCK TABLES `venues` WRITE;
/*!40000 ALTER TABLE `venues` DISABLE KEYS */;
INSERT INTO `venues` VALUES (1,'Chennai Trade Centre','1 Nandambakkam Main Rd','Chennai','Tamil Nadu','600089',5000,1,'2026-01-01 08:00:00',NULL),(2,'ITC Grand Chola Ballroom','63 Mount Road','Chennai','Tamil Nadu','600032',800,1,'2026-01-01 08:00:00',NULL),(3,'CODISSIA Trade Fair Complex','1050 Avinashi Rd','Coimbatore','Tamil Nadu','641014',3000,1,'2026-01-01 08:00:00',NULL),(4,'Trichy Rockfort Convention','12 Rockfort Road','Trichy','Tamil Nadu','620001',1200,1,'2026-01-01 08:00:00',NULL),(5,'Madurai Convention Centre','5 Bypass Road','Madurai','Tamil Nadu','625016',2000,1,'2026-01-01 08:00:00',NULL),(6,'Anna Centenary Library Hall','Kotturpuram','Chennai','Tamil Nadu','600085',600,1,'2026-01-01 08:00:00',NULL),(7,'Nehru Indoor Stadium','Periyamet','Chennai','Tamil Nadu','600003',4000,1,'2026-01-01 08:00:00',NULL),(8,'PSG Tech Auditorium','Peelamedu','Coimbatore','Tamil Nadu','641004',1000,1,'2026-01-01 08:00:00',NULL),(9,'Vels University Seminar Hall','1 Velan Nagar, Pallavaram','Chennai','Tamil Nadu','600117',400,1,'2026-01-01 08:00:00',NULL),(10,'The Residency Banquet Hall','49 GN Chetty Road','Chennai','Tamil Nadu','600017',500,1,'2026-01-01 08:00:00',NULL);
/*!40000 ALTER TABLE `venues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'event_management_db'
--
/*!50003 DROP PROCEDURE IF EXISTS `sp_register_for_event` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_register_for_event`(
    -- INPUT PARAMETERS
    IN p_user_id INT,
    IN p_event_id INT,
    IN p_ticket_id INT,
    IN p_quantity INT,
    IN p_price DECIMAL(10,2),
    IN p_payment_method VARCHAR(30),
    IN p_offer_code VARCHAR(50),

    -- OUTPUT PARAMETERS
    OUT o_success BOOLEAN,
    OUT o_message VARCHAR(255),
    OUT o_registration_id INT,
    OUT o_final_amount DECIMAL(10,2)
)
proc: BEGIN
    /*
        Local variables used only inside this procedure.
        These keep intermediate state so that Java does not
        have to manage partial results.
    */
    DECLARE v_available INT;
    DECLARE v_offer_id INT DEFAULT NULL;
    DECLARE v_discount INT DEFAULT 0;
    DECLARE v_base_amount DECIMAL(10,2);
    DECLARE v_discount_amount DECIMAL(10,2);

    /*
        Global error handler.
        If ANY SQL exception occurs after START TRANSACTION,
        this handler executes automatically.
        This guarantees atomicity.
    */
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET o_success = FALSE;
        SET o_message = 'Transaction failed due to database error';
    END;

    /*
        Always initialize OUT parameters.
        This avoids returning garbage values.
    */
    SET o_success = FALSE;
    SET o_message = '';
    SET o_registration_id = NULL;
    SET o_final_amount = 0;

    START TRANSACTION;

    /*
        Step 1: Lock the ticket row.
        FOR UPDATE ensures that concurrent registrations
        cannot overbook tickets.
    */
    SELECT available_quantity
    INTO v_available
    FROM tickets
    WHERE ticket_id = p_ticket_id
    FOR UPDATE;

    /*
        Step 2: Validate ticket availability.
        If insufficient, rollback and exit procedure early.
    */
    IF v_available < p_quantity THEN
        ROLLBACK;
        SET o_message = 'Insufficient tickets available';
        LEAVE proc;
    END IF;

    /*
        Step 3: Validate offer code only if provided.
        Blank or NULL offer codes are ignored.
    */
    IF p_offer_code IS NOT NULL AND TRIM(p_offer_code) <> '' THEN

        SELECT offer_id, discount_percentage
        INTO v_offer_id, v_discount
        FROM offers
        WHERE event_id = p_event_id
          AND UPPER(code) = UPPER(p_offer_code)
          AND (valid_from IS NULL OR valid_from <= NOW())
          AND (valid_to IS NULL OR valid_to >= NOW());

        /*
            If no offer row is found, offer_id remains NULL.
        */
        IF v_offer_id IS NULL THEN
            ROLLBACK;
            SET o_message = 'Invalid or expired offer code';
            LEAVE proc;
        END IF;

        /*
            Prevent the same user from using the same offer again.
        */
        IF EXISTS (
            SELECT 1
            FROM offer_usages
            WHERE offer_id = v_offer_id
              AND user_id = p_user_id
        ) THEN
            ROLLBACK;
            SET o_message = 'Offer code already used by user';
            LEAVE proc;
        END IF;

    END IF;

    /*
        Step 4: Create registration record.
        LAST_INSERT_ID is safe inside a transaction.
    */
    INSERT INTO registrations (
        user_id,
        event_id,
        registration_date,
        status
    )
    VALUES (
        p_user_id,
        p_event_id,
        UTC_TIMESTAMP(),
        'CONFIRMED'
    );

    SET o_registration_id = LAST_INSERT_ID();

    /*
        Step 5: Link tickets to registration.
    */
    INSERT INTO registration_tickets (
        registration_id,
        ticket_id,
        quantity
    )
    VALUES (
        o_registration_id,
        p_ticket_id,
        p_quantity
    );

    /*
        Step 6: Calculate payable amount.
        All monetary calculation happens in DB
        to avoid mismatch with Java.
    */
    SET v_base_amount = p_price * p_quantity;
    SET v_discount_amount = (v_base_amount * v_discount) / 100;
    SET o_final_amount = v_base_amount - v_discount_amount;

    /*
        Step 7: Record payment.
        If this insert fails, EXIT HANDLER rolls back everything.
    */
    INSERT INTO payments (
        registration_id,
        amount,
        payment_method,
        payment_status,
        created_at,
        offer_id
    )
    VALUES (
        o_registration_id,
        o_final_amount,
        p_payment_method,
        'SUCCESS',
        UTC_TIMESTAMP(),
        v_offer_id
    );

    /*
        Step 8: Deduct ticket quantity only after payment success.
    */
    UPDATE tickets
    SET available_quantity = available_quantity - p_quantity
    WHERE ticket_id = p_ticket_id;

    /*
        Step 9: Record offer usage only if offer was applied.
    */
    IF v_offer_id IS NOT NULL THEN
        INSERT INTO offer_usages (
            offer_id,
            user_id,
            registration_id,
            used_at
        )
        VALUES (
            v_offer_id,
            p_user_id,
            o_registration_id,
            UTC_TIMESTAMP()
        );
    END IF;

    /*
        Step 10: Commit transaction.
        At this point, all data is consistent.
    */
    COMMIT;

    SET o_success = TRUE;
    SET o_message = 'Registration successful';

END proc ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-03  9:04:28

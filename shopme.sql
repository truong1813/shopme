-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: localhost    Database: shopmedb
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country_id` int DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `address_line_1` varchar(128) NOT NULL,
  `address_line_2` varchar(64) DEFAULT NULL,
  `phone_number` varchar(15) NOT NULL,
  `city` varchar(45) NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `state` varchar(45) NOT NULL,
  `default_address` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn3sth7s3kur1rafwbbrqqnswt` (`country_id`),
  KEY `FKhrpf5e8dwasvdc5cticysrt2k` (`customer_id`),
  CONSTRAINT `FKhrpf5e8dwasvdc5cticysrt2k` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FKn3sth7s3kur1rafwbbrqqnswt` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,1,10,'Trần','Văn Trường','Khu 6 - TT Hồi Xuân  - Quan Hoá','','0972681613','Thanh Hoá','','Thanh Hoá',_binary '\0'),(2,1,10,'Trần','Văn Trường','Thắng Lợi ','','0972681613','Binh Duong','','Binh Duong',_binary '\0'),(3,1,10,'Nguyễn','Thế Đạt','Thắng Lợi ','','0972681613','Sông Công','','Thái Nguyên',_binary '\0');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `logo` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_oce3937d2f4mpfqrycbr0l93m` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (12,'Canon','Canon.png'),(13,'Acer','Acer.png'),(14,'Samsung Electronics','Samsung.png'),(15,'Asus','logo-Asus.jpg');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brands_categories`
--

DROP TABLE IF EXISTS `brands_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands_categories` (
  `brand_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`brand_id`,`category_id`),
  KEY `FK6x68tjj3eay19skqlhn7ls6ai` (`category_id`),
  CONSTRAINT `FK58ksmicdguvu4d7b6yglgqvxo` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FK6x68tjj3eay19skqlhn7ls6ai` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands_categories`
--

LOCK TABLES `brands_categories` WRITE;
/*!40000 ALTER TABLE `brands_categories` DISABLE KEYS */;
INSERT INTO `brands_categories` VALUES (13,5),(15,5),(13,6),(15,6),(15,7),(12,10),(12,11),(12,12),(14,25),(15,25),(15,26),(14,27),(15,28),(14,29),(15,30);
/*!40000 ALTER TABLE `brands_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKehe6sev71h6jfmfjyeebcu1c2` (`customer_id`),
  KEY `FKqkqmvkmbtiaqn2nfqf25ymfs2` (`product_id`),
  CONSTRAINT `FKehe6sev71h6jfmfjyeebcu1c2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FKqkqmvkmbtiaqn2nfqf25ymfs2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (16,11,19,2);
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `alias` varchar(64) NOT NULL,
  `image` varchar(128) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `parent_id` int DEFAULT NULL,
  `all_parent_ids` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jx1ptm0r46dop8v0o7nmgfbeq` (`alias`),
  UNIQUE KEY `UK_t8o6pivur7nn124jehx7cygw5` (`name`),
  KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`),
  CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Electronics','electronics','electronics.png',_binary '',NULL,NULL),(2,'Camera & Photo','camera','camera.jpg',_binary '',1,'-1-'),(3,'Computers','computers','computers.png',_binary '',NULL,NULL),(4,'Cell Phones & Accessories','cellphones','cellphones.png',_binary '',1,'-1-'),(5,'Desktops','desktop_computers','desktop computers.png',_binary '',3,'-3-'),(6,'Laptops','laptop_computers','laptop computers.png',_binary '',3,'-3-'),(7,'Tablets','tablet_computers','tablets.png',_binary '',3,'-3-'),(8,'Computer Components','computer_components','computer components.png',_binary '',3,'-3-'),(10,'Digital Cameras','digital_cameras','digital cameras.png',_binary '',2,'-1-2-'),(11,'Flashes','camera_flashes','flashes.png',_binary '',2,'-1-2-'),(12,'Lenses','camera_lenses','lenses.png',_binary '',2,'-1-2-'),(13,'Tripods & Monopods','camera_tripods_monopods','tripods_monopods.png',_binary '',2,'-1-2-'),(14,'Carrier Cell Phones','carrier_cellphones','carrier cellphones.png',_binary '',4,'-1-4-'),(15,'Unlocked Cell Phones','unlocked_cellphones','unlocked cellphones.png',_binary '',4,'-1-4-'),(16,'Accessories','cellphone_accessories','cellphone accessories.png',_binary '',4,'-1-4-'),(17,'Cables & Adapters','cellphone_cables_adapters','cables and adapters.png',_binary '',16,'-1-4-16-'),(18,'MicroSD Cards','microsd_cards','microsd cards.png',_binary '',16,'-1-4-16-'),(19,'Stands','cellphone_stands','cellphone_stands.png',_binary '',16,'-1-4-16-'),(20,'Cases','cellphone_cases','cellphone cases.png',_binary '',16,'-1-4-16-'),(21,'Headphones','headphones','headphones.png',_binary '',16,'-1-4-16-'),(25,'Internal Optical Drives','computer_optical_drives','internal optical drives.png',_binary '',8,'-3-8-'),(26,'Power Supplies','computer_power_supplies','power supplies.png',_binary '',8,'-3-8-'),(27,'Solid State Drives','solid_state_drives','solid state drives.png',_binary '',8,'-3-8-'),(28,'Sound Cards','computer_sound_cards','sound cards.png',_binary '',8,'-3-8-'),(29,'Memory','computer_memory','computer memory.png',_binary '\0',8,'-3-8-'),(30,'Motherboard','computer_motherboard','motherboards.png',_binary '',8,'-3-8-'),(31,'Network Cards','computer_network_cards','network cards.png',_binary '',8,'-3-8-');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `countries` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `code` varchar(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,'Việt Nam','VN'),(6,'United State','US');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `currencies`
--

DROP TABLE IF EXISTS `currencies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `currencies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `symbol` varchar(3) NOT NULL,
  `code` varchar(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `currencies`
--

LOCK TABLES `currencies` WRITE;
/*!40000 ALTER TABLE `currencies` DISABLE KEYS */;
INSERT INTO `currencies` VALUES (1,'United States Dollar','$','USD'),(2,'Vietnamese đồng','đ','VND');
/*!40000 ALTER TABLE `currencies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `password` varchar(64) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `address_line_1` varchar(64) NOT NULL,
  `address_line_2` varchar(64) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `country_id` int DEFAULT NULL,
  `postal_code` varchar(10) NOT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `verification_code` varchar(64) DEFAULT NULL,
  `authentication_type` varchar(10) DEFAULT NULL,
  `reset_password_token` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `FK7b7p2myt0y31l4nyj1p7sk0b1` (`country_id`),
  CONSTRAINT `FK7b7p2myt0y31l4nyj1p7sk0b1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (10,'vantruongtran1813@gmail.com','$2a$10$n4NGitVSvuErUiHh0vh9/u0c8ASRQD3PtLmmTTKROtf7bjA/qH0nK','Văn Trường','Trần','0972681613','Quang Minh - Quảng Văn - Quảng Xương ','','Thanh Hoá','Thanh Hoá',1,'1111','2022-11-19 16:33:53.320000',_binary '',NULL,'DATABASE',NULL),(11,'vantruong1813@gmail.com','$2a$10$14a.G/HWJaCFiR9heU2.oOsX62hu8tESMcAH.Ef2Gno5cIMfMO1/q','Eyes','Blue','0972681613','Khu 6 - TT Hồi Xuân  - Quan Hoá','','Thanh Hoá','Thanh Hoá',1,'037','2022-11-21 17:17:32.616000',_binary '',NULL,'FACEBOOK',NULL),(12,'sonk2qh@gmail.com','','Anh','Son','0972681613','Khu 6 - TT Hồi Xuân  - Quan Hoá','','Thanh Hoá','Thanh Hoá',1,'','2022-12-05 09:53:32.895000',_binary '',NULL,'GOOGLE',NULL);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_cost` float NOT NULL,
  `quantity` int NOT NULL,
  `shipping_cost` float NOT NULL,
  `sub_total` float NOT NULL,
  `unit_price` float NOT NULL,
  `order_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrws2q0si6oyd6il8gqe2aennc` (`order_id`),
  KEY `FKc7q42e9tu0hslx6w4wxgomhvn` (`product_id`),
  CONSTRAINT `FKc7q42e9tu0hslx6w4wxgomhvn` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKrws2q0si6oyd6il8gqe2aennc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (36,17000000,1,6900,13140000,13140000,36,20);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_track`
--

DROP TABLE IF EXISTS `order_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_track` (
  `id` int NOT NULL AUTO_INCREMENT,
  `notes` varchar(256) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK31jv1s212kajfn3kk1ksmnyfl` (`order_id`),
  CONSTRAINT `FK31jv1s212kajfn3kk1ksmnyfl` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_track`
--

LOCK TABLES `order_track` WRITE;
/*!40000 ALTER TABLE `order_track` DISABLE KEYS */;
INSERT INTO `order_track` VALUES (45,'Order was placed by the customer','NEW','2023-02-24 11:38:27.000000',36);
/*!40000 ALTER TABLE `order_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  `deliver_date` datetime(6) DEFAULT NULL,
  `deliver_days` int NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `order_time` datetime(6) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `phone_number` varchar(15) NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `product_cost` float NOT NULL,
  `shipping_cost` float NOT NULL,
  `state` varchar(45) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `sub_total` float NOT NULL,
  `tax` float NOT NULL,
  `total` float NOT NULL,
  `customer_id` int DEFAULT NULL,
  `address_line_1` varchar(64) NOT NULL,
  `address_line_2` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpxtb8awmi0dk6smoh2vp1litg` (`customer_id`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (36,'Thanh Hoá','Việt Nam','2023-02-26 00:00:00.000000',2,'Trần','Văn Trường','2023-02-24 11:38:27.595000','COD','0972681613','1111',17000000,6900,'Thanh Hoá','NEW',13140000,0,13146900,10,'Quang Minh - Quảng Văn - Quảng Xương ','');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_details`
--

DROP TABLE IF EXISTS `product_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnfvvq3meg4ha3u1bju9k4is3r` (`product_id`),
  CONSTRAINT `FKnfvvq3meg4ha3u1bju9k4is3r` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_details`
--

LOCK TABLES `product_details` WRITE;
/*!40000 ALTER TABLE `product_details` DISABLE KEYS */;
INSERT INTO `product_details` VALUES (8,'adda','dâdaa',16),(9,'dâd','dâdaaa',16),(10,'CPU','Intel Core i5-10300H (2.50GHz upto 4.50GHz, 8MB)',20),(11,'RAM','8GB DDR4 2933MHz (2 khe, tối đa 32GB)',20),(12,'SDD','512GB M.2 NVMe PCIe 3.0 SSD',20),(13,'Màn hình','15.6 inch FHD (1920×1080) 16:9, Value IPS-level, 144Hz, 45% NTSC, 62.5% SRGB, anti-glare display',20),(14,'VGA','NVIDIA GeForce GTX 1650 4GB GDDR6',20),(16,'Cổng kết nối','1 x USB 2.0 Type-A 1 x USB 3.2 Gen 2 Type-C support DisplayPort™ / G-SYNC 2 x USB 3.2 Gen 1 Type-A 1 x HDMI 2.0b 1 x 3.5mm Combo Audio Jack',20);
/*!40000 ALTER TABLE `product_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqnq71xsohugpqwf3c9gxmsuy` (`product_id`),
  CONSTRAINT `FKqnq71xsohugpqwf3c9gxmsuy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (22,'Canon.png',16),(31,'ASUS TUF Gaming top view.png',20),(32,'ASUS TUF Gaming 2.png',20),(33,'ASUS TUF Gaming back.png',20),(34,'ASUS TUF Gaming side.png',20);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `alias` varchar(256) NOT NULL,
  `short_description` varchar(1024) NOT NULL,
  `full_description` varchar(8092) NOT NULL,
  `main_image` varchar(255) NOT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `in_stock` bit(1) DEFAULT NULL,
  `price` float NOT NULL,
  `cost` float NOT NULL,
  `discount_percent` float DEFAULT NULL,
  `length` float NOT NULL,
  `weight` float NOT NULL,
  `height` float NOT NULL,
  `width` float NOT NULL,
  `brand_id` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8qwq8q3hk7cxkp9gruxupnif5` (`alias`),
  UNIQUE KEY `UK_o61fmio5yukmmiqgnxf8pnavn` (`name`),
  KEY `FKa3a4mpsfdf4d2y6r8ra3sc8mv` (`brand_id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  FULLTEXT KEY `products_FTS` (`name`,`short_description`,`full_description`),
  CONSTRAINT `FKa3a4mpsfdf4d2y6r8ra3sc8mv` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (16,'Acer 477','Acer_477','<font face=\"Arial Black\" color=\"#953734\">đaa</font>','<div>dadada</div>','ASUS TUF Gaming main.png',NULL,'2023-03-06 16:15:37.149000',_binary '',_binary '',16500000,15000000,0,0.07,0,0,0.12,12,12),(17,'sddsdsddsdsdsd','dsddssdsdsdddsd','<div>fff</div>','<div>ffffff</div>','Canon.png',NULL,'2022-10-29 15:30:20.274000',_binary '',_binary '',0,0,0,0,0,0,0,13,6),(18,'Computers','c','<div>gggg</div>','<div>gggggg</div>','Samsung.png',NULL,'2022-10-29 15:31:01.066000',_binary '',_binary '',0,0,0,0,0,0,0,13,5),(19,'Acer 4774','Acer_4774','<div>dw</div>','<div>ds</div>','Acer.png',NULL,'2022-12-08 15:23:52.468000',_binary '',_binary '',18000000,15000000,30,24,2.5,22,36,13,6),(20,'Laptop Asus TUF Gaming FX506LHB-HN188W ','Laptop_Asus_TUF_Gaming_FX506LHB-HN188W','<span style=\"font-size:14px;\">·&nbsp; CPU: Intel Core i5-10300H (2.50GHz upto 4.50GHz, 8MB)<br>\r\n·&nbsp; &nbsp;RAM: 8GB DDR4 2933MHz (2 khe, tối đa 32GB)<br>\r\n·&nbsp; &nbsp;Ổ cứng: 512GB M.2 NVMe PCIe 3.0 SSD<br>\r\n·&nbsp; &nbsp;VGA: NVIDIA GeForce GTX 1650 4GB GDDR6<br>\r\n·&nbsp; &nbsp;Màn hình: 15.6 inch FHD (1920×1080) 16:9, Value IPS-level, 144Hz, 45% NTSC, 62.5% SRGB, anti-glare display<br>\r\n·&nbsp; &nbsp;Pin: 3-cell, 48WHrs<br>\r\n·&nbsp; &nbsp;Hệ điều hành: Windows 11 Home<br>\r\n·&nbsp; &nbsp;Bảo hành: 2 năm, pin 1 năm</span><br>','<div><span style=\"font-size:14.0pt;line-height:107%;mso-bidi-font-weight:\r\nbold\"></span></div><span style=\"font-size:16px;\">Laptop Asus TUF Gaming FX506LHB-HN188W (i5-10300H, 8GB DDR4-3200 SO-DIMM, 512GB M.2 NVMe PCIe 3.0 SSD, GTX1650/4GB, 15.6″ FHD 144Hz, Win11, Bonfire Black)<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; CPU: Intel Core i5-10300H (2.50GHz upto 4.50GHz, 8MB)<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; RAM: 8GB DDR4 2933MHz (2 khe, tối đa 32GB)<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ổ cứng: 512GB M.2 NVMe PCIe 3.0 SSD<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; VGA: NVIDIA GeForce GTX 1650 4GB GDDR6<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Màn hình: 15.6 inch FHD (1920×1080) 16:9, Value IPS-level, 144Hz, 45% NTSC, 62.5% SRGB, anti-glare display<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Pin: 3-cell, 48WHrs<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Hệ điều hành: Windows 11 Home<br>\r\n·&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Bảo hành: 2 năm, pin 1 năm</span><span style=\"font-size:14px;\"></span>','ASUS TUF Gaming main.png',NULL,'2022-12-08 11:33:38.847000',_binary '',_binary '',18000000,17000000,27,25.6,2.3,2.5,35.4,15,6);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `description` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin','manage everything'),(2,'Salesperson','manage product price, customers, shipping, orders and sales report'),(3,'Editor','manage categories, brands, products, articles and menus'),(4,'Shipper','view products, view orders and update order status'),(5,'Assistant','manage questions and reviews');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `key` varchar(128) NOT NULL,
  `value` varchar(4024) NOT NULL,
  `category` varchar(45) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES ('COPYRIGHT','Copyright (C) 2022 Ngoc Duong Ltd.','GENERAL'),('CURRENCY_ID','2','CRURRENCY'),('CURRENCY_SYMBOL','đ','CRURRENCY'),('CURRENCY_SYMBOL_POSITION','After price','CRURRENCY'),('CUSTOMER_VERIFY_CONTENT','<span style=\"font-size:18px;\">Dear [[name]],<br><i>\r\nClick&nbsp; the link below to verify your registration</i></span><span style=\"font-size:24px;\"><br>\r\n<br></span><div><span style=\"font-size:24px;\"><a href=\"[[URL]]\" target=\"_self\">VERIFY</a><br></span><div><div><div><span style=\"font-size:18px;\"><br></span></div><div><span style=\"font-size:18px;\">Thank,<br>\r\nNgoc Duong Team.</span><div><br></div><div><br></div></div></div></div></div>','MAIL_TEMPLATE'),('CUSTOMER_VERIFY_SUBJECT','Please verify your registration to continue shopping','MAIL_TEMPLATE'),('DECIMAL_DIGITS','0','CRURRENCY'),('DECIMAL_POINT_TYPE','POINT','CRURRENCY'),('MAIL_FROM','vantruongtran1813@gmail.com','MAIL_SERVER'),('MAIL_HOST','smtp.gmail.com','MAIL_SERVER'),('MAIL_PASSWORD','vknmxxlqftkondmg','MAIL_SERVER'),('MAIL_PORT','587','MAIL_SERVER'),('MAIL_SENDER_NAME','ngoc duong team','MAIL_SERVER'),('MAIL_USERNAME','vantruongtran1813@gmail.com','MAIL_SERVER'),('ORDER_CONFIRMATION_CONTENT','<span style=\"font-size:18px;\">Dear [[name]],<br><i></i></span><div><span style=\"font-size:18px;\">This email to confirm that you have successfully placed order through our&nbsp; website.Please review the following order summary:</span></div><div><span style=\"font-size:18px;\">- Order ID: [[orderId]]</span></div><div><span style=\"font-size:18px;\">- Order time: [[orderTime]]</span></div><div><span style=\"font-size:18px;\">- Ship to: [[shippingAddress]]</span></div><div><span style=\"font-size:18px;\">- Total: [[total]]</span></div><div><span style=\"font-size:18px;\">- Payment method: [[paymentMethod]]</span></div><div><span style=\"font-size:18px;\"><br></span></div><div><span style=\"font-size: 18px;\">We\'re currently processing your order.Click here to view full details of your order on our website(login required).</span></div><div><span style=\"font-size: 18px;\"><br></span></div><div><span style=\"font-size: 18px;\">Thanks for purchasing products at Shopme.</span></div><div><span style=\"font-size: 18px;\">The Ngoc Duong Team.</span></div><div><span style=\"font-size:18px;\"><br></span></div>','MAIL_TEMPLATE'),('ORDER_CONFIRMATION_SUBJECT','Confirmation of your order ID #[[orderId]]','MAIL_TEMPLATE'),('PAYPAL_API_BASE_URL','https://api-m.sandbox.paypal.com','PAYMENT'),('PAYPAL_API_CLIENT_ID','PAYPAL_CLIENT_ID','PAYMENT'),('PAYPAL_API_CLIENT_SECRET','PAYPAL_CLIENT_SECRET','PAYMENT'),('SITE_LOGO','/site-logo/ShopmeSmall.png','GENERAL'),('SITE_NAME','Ngọc Dương','GENERAL'),('SMTP_AUTH','true','MAIL_SERVER'),('SMTP_SECURED','true','MAIL_SERVER'),('THOUSANDS_POINT_TYPE','COMMA','CRURRENCY');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping_rates`
--

DROP TABLE IF EXISTS `shipping_rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping_rates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code_supported` bit(1) DEFAULT NULL,
  `days` int NOT NULL,
  `rate` float NOT NULL,
  `state` varchar(45) NOT NULL,
  `country_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKef7sfgeybt3xn13nlt2j6sljw` (`country_id`),
  CONSTRAINT `FKef7sfgeybt3xn13nlt2j6sljw` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping_rates`
--

LOCK TABLES `shipping_rates` WRITE;
/*!40000 ALTER TABLE `shipping_rates` DISABLE KEYS */;
INSERT INTO `shipping_rates` VALUES (1,_binary '',3,5000,'Hà Nội',1),(2,_binary '',5,6000,'Lạng Sơn',1),(3,_binary '',2,3000,'Thanh Hoá',1),(4,_binary '',5,10000,'Yên Bái',1),(5,_binary '\0',0,0,'Thái Nguyên',1),(6,_binary '\0',0,0,'Quảng Trị',1);
/*!40000 ALTER TABLE `shipping_rates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `states`
--

DROP TABLE IF EXISTS `states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `states` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `country_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKskkdphjml9vjlrqn4m5hi251y` (`country_id`),
  CONSTRAINT `FKskkdphjml9vjlrqn4m5hi251y` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `states`
--

LOCK TABLES `states` WRITE;
/*!40000 ALTER TABLE `states` DISABLE KEYS */;
INSERT INTO `states` VALUES (1,'Hà Nội',1),(8,'Thanh Hoá',1),(9,'Lào Cai',1),(10,'Yên Bái',1),(11,'Lạng Sơn',1),(18,'Hà Giang',1),(19,'Thái Nguyên',1),(20,'Cao Bằng',1),(21,'Điện Biên',1),(22,'Quảng Ninh',1),(23,'Vĩnh Phúc',1),(24,'Phú Thọ',1),(25,'Bắc Giang',1),(26,'Bắc Ninh',1),(27,'Hải Dương',1),(28,'Hải Phòng',1),(29,'Tuyên Quang',1),(30,'Thái Bình',1),(31,'Nam Định',1),(32,'Ninh Bình',1),(33,'Hà Nam',1),(34,'Lai Châu',1),(35,'Nghệ An',1),(36,'Hà Tĩnh',1),(37,'Quảng Bình',1),(38,'Quảng Trị',1),(39,'Thừa Thiên Huế',1),(40,'Đà Nẵng',1),(41,'Quảng Nam',1),(42,'Hưng Yên',1),(43,'Phú Yên',1);
/*!40000 ALTER TABLE `states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(125) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `password` varchar(64) NOT NULL,
  `photo` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'nam@codejava.net',_binary '','Nam','Ha Minh','$2a$10$bDqskP9Z/y6BIZnFLgJ8HuwMYaZXD9w2jVk2pYHXgn1k6N4nckleu','namhm.png'),(2,'kanna.allada@gmail.com',_binary '','Allada','Pavan','$2a$10$zRa/rmQ8JarpYG2bNKftyelKnsUhsHwGB.xmCKTWJClsB7O9wzTnG','Allada Pavan.png'),(3,'aecllc.bnk@gmail.com',_binary '\0','Bruce','Kitchell','$2a$10$GINThwCjVZAbGnmOe9BIeuDuvDlyfuwZrg/Rsmrjs1Lsq2pnXtO/S','Bruce Kitchell.png'),(4,'muhammad.evran13@gmail.com',_binary '','Muhammad','Evran','$2a$10$UcHWHC72azPVZJb5Ky.Yy.X695WGf1ZkkGMS3WL3B9WqWf2dQD04G','Muhammad Evran.png'),(5,'kent.hervey8@gmail.com',_binary '','Kent','Hervey','$2a$10$YHXRsZ07/Btv.qCgGht.7u2PW.GLWzpVB7eabfgH1mhTVVXffDT6K','KentHervey.png'),(7,'nathihsa@gmail.com',_binary '','Nathi','Sangweni','$2a$10$WyHmQiXCSYuHcGeg8eFWvOScqzSgg88MmqpajPdzSkLsvZjT3tKC.','Nathi_Sangweni.png'),(8,'ammanollashirisha2020@gmail.com',_binary '','Ammanolla','Shirisha','$2a$10$N1eE87eXFB2XQ5nmWKaTXOofnrPn8koeNvZhEpleJzO49i55e/Vk.','Ammanolla.png'),(9,'bfeeny238@hotmail.com',_binary '','Bill','Feeny','$2a$10$VaJMR518yF/rj9EzyGUVyeHbKfuUFlSp.2TdWp8HCWjt2npeGxqo6','Bill Feeny.png'),(10,'redsantosph@gmail.com',_binary '','Frederick','delos Santos','$2a$10$KXHmKpE6YB/0sjiy3fkMv.muKyxqvOXE6jVeaPu8KEaExx62ZmmNe','Frederick Santos.png'),(11,'ro_anamaria@mail.ru',_binary '','Ana','Maria','$2a$10$sz0CHOHAY1Xjt2ajIZgnG.L2KBQ4SsQkOGsPYue.C5gr6j.KMDdqS','Anna Maria.jpg'),(12,'maytassatya@hotmail.com',_binary '\0','Satya','Narayana','$2a$10$R7EJcaYijjJo/IVk6c1CieBML2uP3RAKMVlCxylPAePlCfJsX7OOy','Satya Narayana.png'),(13,'matthewefoli@gmail.com',_binary '','Matthew','Efoli','$2a$10$ECNnxXSVArnwS9KCet3yguQ1qHKyBIhh2G8c4F9CYgvp/Hadl8OS6','Matthew.png'),(14,'davekumara2@gmail.com',_binary '','Dave','Kumar','$2a$10$5ebeZu18V5RheieYqpl/LORCN41E3H7yvxKqEwtq5Zq2JVw.E9dva','Dave Kumar.png'),(15,'jackkbruce@yahoo.com',_binary '','Jack','Bruce','$2a$10$a6iyIHRj8DNpu15obVHTSOGcLe4IfpBcD4iEEJesWaFpBQvRoF2da','Jack Bruce.png'),(16,'zirri.mohamed@gmail.com',_binary '','Mohamed','Zirri','$2a$10$TmvyH1AoyDqRmQ4uC8NAZOOV29CPEDGuxVsHLP1cJoHQGr78L4kjW','Mohamed Zirri.jpg'),(17,'mk.majumdar009@hotmail.com',_binary '\0','Mithun','Kumar Majumdar','$2a$10$Y6SEy2INN0Rk/vhLHHJUYO6IMqNW3Ar.jVe9o0W1lpBRX8xr2Itui','Mithun Kumar Majumdar.png'),(18,'Aliraza.arain.28@gmail.com',_binary '','Ali','Raza','$2a$10$suCab72m2VGaW9vHG8E5B.TUEbyzkSWS2YhF./OVtiJA/6QleVJ/K','Ali Raza.png'),(19,'isaachenry2877@gmail.com',_binary '','Isaac','Henry','$2a$10$CtmhrOz/AhDoCpKbeYl8O.0ngCFMukcznNZq7.YcHrkRyKpBG8Zca','Isaac Henry.jpg'),(20,'s.stasovska82@hotmail.com',_binary '','Svetlana','Stasovska','$2a$10$fcN2cNa7vB.78QnmzfNZEeUBkrwUaM.bVK3iDB.KFQlR15DwL7QZy','Svetlana Stasovska.png'),(21,'mikegates2012@gmail.com',_binary '','Mike','Gates','$2a$10$zIO1tygsw6cB2ymiR.WX0ulr9NKdTlZHqu7w/W/LLwk8HhK7nVnH.','Mike Gates.png'),(22,'pedroquintero67@gmail.com',_binary '\0','Pedro','Quintero','$2a$10$UPX5EwZw0MyBvbe.7mxg2u8GOl/4KgaUU40iSjr1PLFYvhu35fMmu','Pedro Quintero.png'),(23,'amina.elshal2@yahoo.com',_binary '','Amina','Elshal','$2a$10$J1yoyqG5vWNe5N663PkgY.h53gfJtTR7Bb8E8u3sXdNbOZxhXgHu.','Amina Elshal.png'),(24,'a@gmail.com',_binary '\0','fdfd','fdf','111111111',NULL),(25,'vantruongtran1813@gmail.com',_binary '','Trần','Văn Trường','$2a$10$Wq2hEUtj6tkeMvGia39DCOgiWc0Y5GfEbMPjwI3DBujJm2c1WXC7K','logo-Asus.jpg');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (1,1),(2,1),(24,1),(3,2),(9,2),(10,2),(11,2),(12,2),(13,2),(18,2),(19,2),(20,2),(4,3),(5,3),(7,3),(8,3),(11,3),(15,3),(20,3),(14,4),(15,4),(16,4),(17,4),(18,4),(25,4),(5,5),(14,5),(18,5),(19,5),(20,5),(21,5),(22,5),(23,5);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-08 14:50:58

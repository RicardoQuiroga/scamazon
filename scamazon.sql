/* A product management database, similar to something like Amazon. We will have */
/* a database of all hte products offered and track data of "users" of the database */
/* and what products they purchase. We will have tables to track each "users" purchase */
/* history and their location. Reviews for products will can be featured along the product */
DROP DATABASE IF EXISTS `scamazon`;

CREATE DATABASE `scamazon`;

DROP TABLE IF EXISTS `scamazon`.`emp`;


CREATE TABLE `scamazon`.`emp` (
  `empID` INT NOT NULL AUTO_INCREMENT,
  `empPass` VARCHAR(45) NOT NULL,
  `empName` VARCHAR(45) NOT NULL,
  `empAddress` VARCHAR(45) NULL DEFAULT 'n/a',
  `empActive` VARCHAR(1) NOT NULL DEFAULT 'y',
  `empSalary` INT NULL DEFAULT 0,
  PRIMARY KEY (`empID`)
);

INSERT INTO `scamazon`.`emp` VALUES (1, 'password', 'Rick Quiroga', '123 test ln.', 'y', 3),
(2, 'whatsapassword', 'Gibbard McChico', '456 down the st.', 'y', 55555555),
(3, 'soupinmypocket!', 'Mr. Soup', '11 Soup blvd.', 'y', 9880),
(4, 'iamsecretelyadog33*', 'Not Dogman', 'Dog st.', 'n', 0),
(5, '12345', 'Charlie Barley', '448 House rd.', 'y', 33000),
(6, 'timelord', 'Maxwell Keeper', '22 Home ave.', 'n', 12000);

/* Holds info needed for when a user wants to place an order, as well as their status */
/* NOTE: User states = 1 - new, 2 - regular, 3 - prestige */

DROP TABLE IF EXISTS `scamazon`.`users`;

CREATE TABLE `scamazon`.`users` (
    `userNum` INT NOT NULL,
    `userID` CHAR(45) NOT NULL,
    `userEmail` CHAR(45) NOT NULL,
    `userPass` CHAR(45) NOT NULL,
    `userName` CHAR(45) NOT NULL,
    `userAddress` CHAR(45) NOT NULL,
    `userCity` CHAR(45) NOT NULL,
    `userZip` CHAR(45) NOT NULL,
    `userState` CHAR(45) NOT NULL,
    `userStatus` INT NOT NULL,
    PRIMARY KEY (`userID`)
);

INSERT INTO `scamazon`.`users` VALUES (1, 'GarbageMan64', 'thegarbage@thedumpster.com', 'iamgarbage!!', 'Gar Bage', 'the dump', 'Dumpstown', '55555', 'Montana', 1),
(2, 'RealMadDog', 'largecheese279@gmail.com', 'whosthere?!?', 'Chris Hungry', '123 Test st.', 'Dumpstown', '55555', 'Montana', 3),
(3, 'Homiedome', 'usedcarpart@gmail.com', 'alphanumeric', 'Tom Moppin', '3998 Camp rd.', 'Ungosville', '98989', 'Virginia', 3),
(4, 'MarmaladeMike', 'marmalademike@gmail.com', 'tacobellparkinglot', 'Mike Moppin', '3998 Camp rd.', 'Ungosville', '98989', 'Virginia', 2),
(5, 'csMajor', 'pantsOnFire@yahoo.com', 'masterHacker420', 'Tom Tubbs', '610 Maple st.', 'Kalamazoo', '49007', 'Michigan', 2),
(6, '111939328', 'hardMascarpone6@gmail.com', 'gibyono84', 'Hard Mascarpone', '231 Fake ln.', 'Faketown', '99999', 'Vermont', 3),
(7, 'SixWholeChickens', 'cuckaww@yahoo.com', 'cuckawwChickenBawkBawk', 'Chicken Chicken', '47 Coop st.', 'Farmland', '12321', 'Tennessee', 2),
(8, 'MYBRAND', 'imissmyfish@gmail.com', 'wherehasmyfishgone22332', 'Gino Mancini', '113 Cart ave.', 'Cityville', '38765', 'California', 1),
(9, 'Stabley', 'treeboy98@gmail.com', 'climbingtreesallday0', 'Stanley Manly', '3 Forest st.', 'Kalamazoo', '49007', 'Michigan', 1),
(10, 'heavybreathing95', 'heavybreathing95@yahoo.com', 'heavybreathingheavybreathing', 'Heavy Breathing', '613 Cedar st.', 'Kalamazoo', '49007', 'Michigan', 3);

/* Describes what can be found in a certain category, and who manages that category */

DROP TABLE IF EXISTS `scamazon`.`categories`;

CREATE TABLE `scamazon`.`categories` (
    `category` INT NOT NULL AUTO_INCREMENT,
    `empID` INT NOT NULL,
    `description` tinytext NOT NULL,
    PRIMARY KEY (`category`),
    FOREIGN KEY (`empID`) REFERENCES `scamazon`.`emp` (`empID`)
);

INSERT INTO `scamazon`.`categories` VALUES (1, 2, 'Books that you probably don''t want to read!'),
(2, 1, 'Movies that are all around considered bad!'),
(3, 5, 'Only the best videogames with a 50% or below average review score!'),
(4, 3, 'Gadgets and doodads that probably won''t blow up..');

/* All products sold are on-hand, so handling restocking through the database is unnecessary */

DROP TABLE IF EXISTS `scamazon`.`items`;

CREATE TABLE `scamazon`.`items` (
    `itemID` INT NOT NULL AUTO_INCREMENT,
    `itemName` CHAR(45) NOT NULL,
    `itemCategory` INT NOT NULL,
    `itemPrice` DOUBLE NOT NULL,
    `itemStock` INT NOT NULL,
    `onOrder` INT NOT NULL,
    `description` tinytext NOT NULL,
    `avgRating` INT,
    PRIMARY KEY (`itemID`),
    FOREIGN KEY (`itemCategory`) REFERENCES `scamazon`.`categories` (`category`)
);

INSERT INTO `scamazon`.`items` VALUES (1, 'Edge in the Snake', 1, 10.99, 8, 0, 'Snakes are ON THE EDGE.', NULL),
(2, 'Master of Extremism', 1, 10.99, 9, 0, 'It''s honestly how extreme this master is.', NULL),
(3, 'Infinite Death', 1, 16.99, 13, 1, 'Based on that one videogame where you die a bunch, it just keeps happening!', NULL),
(4, 'Inferno of Vengeance', 1, 8.99, 5, 0, 'The flame of revenge has been lit - that''s it.', 5),
(5, 'Maximum Termination', 1, 12.50, 8, 0, 'The protagonist gets fired in the wildest way!', 8),
(6, 'Sudden Surrender', 2, 9.99, 12, 0, 'So sudden the movie ends in at least 3 seconds and at most 1 hour.', 4),
(7, 'mailiens', 2, 12.99, 18, 0, 'Is it aliens that look like mail or regular aliens that abduct mail? Order and find out!', 10),
(8, 'War for Humiliation', 2, 15.99, 3, 0, 'Simply people humiliating each other.', 8),
(9, 'Fist of Payback', 2, 9.99, 5, 0, 'It''s honestly so boring that I can''t bother to write a description for it.', 1),
(10, 'Battle of Jeopardy', 2, 19.99, 9, 0, 'What is people fighting over trivia questions?', NULL),
(11, 'Instant Victory', 3, 1.99, 20, 0, 'Done before you can even turn on the console!', 0),
(12, 'Bear Planet of Love', 3, 59.99, 6, 0, 'This is really an odd one. NOTE: THE BEARS ARE NOT IN LOVE THEY ACTUALLY HATE EACH OTHER.', NULL),
(13, 'The Agile Bird Squad', 3, 39.99, 0, 0, 'They used to be angry now they''re agile.', 8),
(14, 'The Stone that Amused the Alchemist', 3, 39.99, 22, 0, 'A visual novel where you meet the actual, factual, funniest rock around', 7),
(15, 'Double Danger', 3, 20.00, 2, 0, 'Some say there''s simply too much danger, don''t tell the feds.', NULL),
(16, 'Thermo-kinetic Anti Projection Apparatus', 4, 129.95, 16, 0, 'Thoroughly advanced VX projection apparatus that converts alpha-time waves into thermo-kinetic energy.', 6),
(17, 'Leverage intuition portal', 4, 35.00, 10, 0, 'Measure delta percentage error rates down to a millionth!', NULL),
(18, 'disintermediate end-to-end infrastructure', 4, 92.99, 1, 0, 'It''s just a usb-c to micro-usb adapter', 3),
(19, '1952 Mega-Ionizer', 4, 149.95, 1, 1, 'Vintage piece of VX hardware used by Dr. Antony Garver, does not include theptometer.', 0),
(20, 'MX32 nano-oscillators', 4, 2.99, 16, 0, 'Keeps Rhoder levels down to 18nR with half the power consumption of previous gen! Packs of 12.', 9);


/* Users can post a review for a product, along with an image. User status displayed with review */
/* NOTE: image is stored as a CHAR since it is storing a location of an image, not the image itself */

DROP TABLE IF EXISTS `scamazon`.`reviews`;

CREATE TABLE `scamazon`.`reviews` (
    `itemID` INT NOT NULL,
    `rating` INT NOT NULL,
    `review` TEXT,
    `image` CHAR(45),
    `userID` CHAR(45) NOT NULL,
    FOREIGN KEY (`itemID`) REFERENCES `scamazon`.`items` (`itemID`),
    FOREIGN KEY (`userID`) REFERENCES `scamazon`.`users` (`userID`)
);

INSERT INTO `scamazon`.`reviews` VALUES (4, 5, 'I liked the half-hour long fireplace scene a lot :)', NULL, 'Homiedome'),
(20, 10, 'Upgraded from a Nodstrom TZ21 NO set and these work like a charm right out of the box.', NULL, 'RealMadDog'),
(5, 8, NULL, NULL, 'MarmaladeMike'),
(7, 10, 'I don''t want to spoil the ending but just know that this is my favorite movie this year!', NULL, 'Homiedome'),
(9, 0, 'I fell asleep before I even turned the tv on. That''s how boring it is.', NULL, 'MYBRAND'),
(11, 0, 'What did I accomplish here besides wasting my money?', NULL, 'RealMadDog'),
(13, 7, NULL, NULL, 'heavybreathing95'),
(14, 7, 'I haven''t laughed this hard in 323 years.', NULL, 'heavybreathing95'),
(7, 9, NULL, NULL, '111939328'),
(9, 2, 'This is at least a fun story to tell friends about.', NULL, '111939328'),
(16, 6, 'Bawk bawk bawk cuckaww!', NULL, 'SixWholeChickens'),
(20, 8, 'Had to fiddle around with my 8 speed trinometer but they ended up splicing well enough.', NULL, 'GarbageMan64'),
(18, 3, NULL, NULL, 'csMajor'),
(13, 9, 'They seem to be a lot less angry now, and for the better.', NULL, 'Homiedome'),
(6, 4, 'I couldn''t get behind the confusing plot. Doesn''t have enough time to develop.', NULL, 'RealMadDog'),
(8, 8, NULL, NULL, 'heavybreathing95');


/* Tracks the ID of the order along with who ordered it and their email in case the company */
/* wants to access them quicker, as opposed to cross-referencing with the entire users table */

DROP TABLE IF EXISTS `scamazon`.`orders`;

CREATE TABLE `scamazon`.`orders` (
    `orderID` INT NOT NULL AUTO_INCREMENT,
    `userID` CHAR(45) NOT NULL,
    `userEmail` CHAR(45) NOT NULL,
    `orderedOn` DATE DEFAULT NULL,
    `shippedOn` DATE DEFAULT NULL,
    `shippedBy` CHAR(45) NOT NULL,
    PRIMARY KEY (`orderID`),
    FOREIGN KEY (`userID`) REFERENCES `scamazon`.`users` (`userID`)
);

INSERT INTO `scamazon`.`orders` VALUES (1, 'Homiedome', 'usedcarpart@gmail.com', DATE '2019-06-12', NULL, 'fedex'),
(2, 'RealMadDog', 'largecheese279@gmail.com', DATE '2019-06-13', DATE '2019-06-17', 'UPS'),
(3, 'MarmaladeMike', 'marmalademike@gmail.com', DATE '2019-06-15', DATE '2019-06-19', 'UPS'),
(4, 'Homiedome', 'usedcarpart@gmail.com', DATE '2019-06-15', DATE '2019-06-19', 'UPS'),
(5, 'MYBRAND', 'imissmyfish@gmail.com', DATE '2019-06-16', DATE '2019-06-20', 'fedex'),
(6, 'RealMadDog', 'largecheese279@gmail.com', DATE '2019-06-17', DATE '2019-06-21', 'USPS'),
(7, 'heavybreathing95', 'heavybreathing95@yahoo.com', DATE '2019-06-17', DATE '2019-06-21', 'UPS'),
(8, '111939328', 'hardMascarpone6@gmail.com', DATE '2019-06-17', DATE '2019-06-21', 'USPS'),
(9, 'SixWholeChickens', 'cuckaww@yahoo.com', DATE '2019-06-18', DATE '2019-06-22', 'USPS'),
(10, 'GarbageMan64', 'thegarbage@thedumpster.com', DATE '2019-06-19', DATE '2019-06-23', 'fedex'),
(11, 'csMajor', 'pantsOnFire@yahoo.com', DATE '2019-06-19', DATE '2019-06-23', 'UPS'),
(12, 'Homiedome', 'usedcarpart@gmail.com', DATE '2019-06-20', DATE '2019-06-24', 'fedex'),
(13, 'RealMadDog', 'largecheese279@gmail.com', DATE '2019-06-20', DATE '2019-06-24', 'UPS'),
(14, 'heavybreathing95', 'heavybreathing95@yahoo.com', DATE '2019-06-20', DATE '2019-06-24', 'UPS');

/* Holds the amount of each item that is ordered for any given order */

DROP TABLE IF EXISTS `scamazon`.`orderDetails`;

CREATE TABLE `scamazon`.`orderDetails` (
    `ID` INT NOT NULL AUTO_INCREMENT,
    `orderID` INT NOT NULL,
    `itemID` INT NOT NULL,
    `quantity` INT NOT NULL,
    PRIMARY KEY (`ID`),
    FOREIGN KEY (`orderID`) REFERENCES `scamazon`.`orders` (`orderID`),
    FOREIGN KEY (`itemID`) REFERENCES `scamazon`.`items` (`itemID`)
);

INSERT INTO `scamazon`.`orderDetails` VALUES (1, 1, 4, 1),
(2, 2, 20, 1),
(3, 3, 5, 1),
(4, 4, 7, 2),
(5, 5, 9, 1),
(6, 6, 11, 1),
(7, 7, 13, 2),
(8, 7, 14, 1),
(9, 8, 7, 1),
(10, 8, 9, 1),
(11, 9, 16, 3),
(12, 10, 20, 10),
(13, 11, 18, 1),
(14, 12, 13, 2),
(15, 13, 6, 1),
(16, 14, 8, 2);

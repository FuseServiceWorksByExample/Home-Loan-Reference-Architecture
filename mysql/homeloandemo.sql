
--
-- Current Database: `homeloan_demo`
--
create database homeloan_demo;
-- CREATE DATABASE `homeloan_demo`;

USE `homeloan_demo`;

--
-- Table structure for table `customer`
--


CREATE TABLE customer (
    CustomerID VARCHAR(11) PRIMARY KEY,
    FIRSTNAME VARCHAR(50),
    LASTNAME VARCHAR(50),
    STREETADDRESS VARCHAR(255),
    CITY VARCHAR(60),
    STATE VARCHAR(2),
    POSTALCODE VARCHAR(60),
    DOB DATE,
    CHECKINGBALANCE DECIMAL(14,2),
    SAVINGSBALANCE DECIMAL(14,2));

INSERT INTO customer VALUES 
('800559876','Joseph','Deeppockets-existing','345 Pine Avenue','Springfield','MO','65810','1966-07-04 00:00:00','8000','35000'),
('610761010','Sally','Shortchange-existing','456 Larch Lane','Springfield','MA','01107','1966-08-05 00:00:00','450','75'),
('680777098','Barbara','Borderline-existing','567 Poplar Pkwy','Worcester','MA','01604','1976-09-16 00:00:00','1000','325'),
('123456789','Mary','Miller-existing','876 Main St','Redbank','NJ','07703','1965-01-15 00:00:00','3300','8550'),
('234567890','Marty','Miller-existing','876 Main St','Redbank','NJ','07703','1964-02-18 00:00:00','4000','8550'),
('345678901','John','Adams-existing','765 Broadway','Buffalo','NY','14211','1967-03-21 00:00:00','4330','12000'),
('456789012','William','Adams-existing','543 Apple Grove','Portland','ME','04101','1968-04-28 00:00:00','890','4200');


grant all privileges on *.* to 'admin'@'%' identified by 'admin' with grant option;
grant all privileges on *.* to 'admin'@'localhost' identified by 'admin' with grant option;                                                                                                                                       

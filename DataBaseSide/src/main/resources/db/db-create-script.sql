
-- -----------------------------------------------------
-- Schema contacts
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `contacts` DEFAULT CHARACTER SET utf8 ;
USE `contacts` ;

-- -----------------------------------------------------
-- Table `contacts`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`address` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(45) NULL DEFAULT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  `streetAddress` VARCHAR(45) NULL DEFAULT NULL,
  `index` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contacts`.`photo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`photo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `pathToFile` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `photo_name_uindex` (`name` ASC))
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contacts`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`contact` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `thirdName` VARCHAR(45) NULL DEFAULT NULL,
  `dateOfBirth` DATE NULL DEFAULT NULL,
  `sex` VARCHAR(45) NULL DEFAULT NULL,
  `citizenship` VARCHAR(45) NULL DEFAULT NULL,
  `maritalStatus` VARCHAR(45) NULL DEFAULT NULL,
  `webSite` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NOT NULL,
  `job` VARCHAR(45) NULL DEFAULT NULL,
  `Address_id` INT(11) NULL DEFAULT NULL,
  `photo_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Contact_Address_idx` (`Address_id` ASC),
  INDEX `fk_contact_photo1_idx` (`photo_id` ASC),
  CONSTRAINT `fk_Contact_Address`
  FOREIGN KEY (`Address_id`)
  REFERENCES `contacts`.`address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_contact_photo1`
  FOREIGN KEY (`photo_id`)
  REFERENCES `contacts`.`photo` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contacts`.`attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`attachment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `dateOfDownload` DATETIME NULL DEFAULT NULL,
  `fileName` VARCHAR(45) NOT NULL,
  `comment` VARCHAR(45) NULL DEFAULT NULL,
  `Contact_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Attachment_Contact1_idx` (`Contact_id` ASC),
  CONSTRAINT `fk_Attachment_Contact1`
  FOREIGN KEY (`Contact_id`)
  REFERENCES `contacts`.`contact` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `contacts`.`phonenumber`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`phonenumber` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `countryCode` VARCHAR(45) NOT NULL,
  `operatorCode` VARCHAR(45) NOT NULL,
  `number` VARCHAR(45) NOT NULL,
  `numberType` VARCHAR(10) NULL DEFAULT NULL,
  `comment` VARCHAR(45) NULL DEFAULT NULL,
  `Contact_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PhoneNumber_Contact1_idx` (`Contact_id` ASC),
  CONSTRAINT `fk_PhoneNumber_Contact1`
  FOREIGN KEY (`Contact_id`)
  REFERENCES `contacts`.`contact` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  DEFAULT CHARACTER SET = utf8;
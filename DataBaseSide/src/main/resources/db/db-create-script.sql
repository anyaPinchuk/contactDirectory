create database if not exists `contacts`;
use `contacts`;

CREATE TABLE IF NOT EXISTS `contacts`.`Address` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `streetAddress` VARCHAR(45) NULL,
  `index` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));


-- -----------------------------------------------------
-- Table `contacts`.`Contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`Contact` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `thirdName` VARCHAR(45) NULL,
  `dateOfBirth` DATE NULL,
  `sex` VARCHAR(45) NULL,
  `citizenship` VARCHAR(45) NULL,
  `maritalStatus` VARCHAR(45) NULL,
  `webSite` VARCHAR(45) NULL,
  `email` VARCHAR(45) NOT NULL,
  `job` VARCHAR(45) NULL,
  `Address_id` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Contact_Address_idx` (`Address_id` ASC),
  CONSTRAINT `fk_Contact_Address`
    FOREIGN KEY (`Address_id`)
    REFERENCES `contacts`.`Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `contacts`.`PhoneNumber`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`PhoneNumber` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `countryCode` VARCHAR(45) NOT NULL,
  `operatorCode` VARCHAR(45) NOT NULL,
  `number` VARCHAR(45) NOT NULL,
  `numberType` VARCHAR(10) NULL,
  `comment` VARCHAR(45) NULL,
  `Contact_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PhoneNumber_Contact1_idx` (`Contact_id` ASC),
  CONSTRAINT `fk_PhoneNumber_Contact1`
    FOREIGN KEY (`Contact_id`)
    REFERENCES `contacts`.`Contact` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `contacts`.`Attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `contacts`.`Attachment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dateOfDownload` DATETIME NULL,
  `fileName` VARCHAR(45) NOT NULL,
  `comment` VARCHAR(45) NULL,
  `Contact_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Attachment_Contact1_idx` (`Contact_id` ASC),
  CONSTRAINT `fk_Attachment_Contact1`
    FOREIGN KEY (`Contact_id`)
    REFERENCES `contacts`.`Contact` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
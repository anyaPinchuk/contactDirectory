
CREATE SCHEMA IF NOT EXISTS `anya_pinchuk` DEFAULT CHARACTER SET utf8 ;
USE `anya_pinchuk` ;

-- -----------------------------------------------------
-- Table `anya_pinchuk`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `anya_pinchuk`.`contact` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `third_name` VARCHAR(45) NULL DEFAULT NULL,
  `date_of_birth` DATE NULL DEFAULT NULL,
  `gender` ENUM('man', 'woman') NULL DEFAULT NULL,
  `citizenship` VARCHAR(45) NULL DEFAULT NULL,
  `marital_status` VARCHAR(45) NULL DEFAULT NULL,
  `web_site` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NOT NULL,
  `job` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `anya_pinchuk`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `anya_pinchuk`.`address` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(45) NULL DEFAULT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  `street_address` VARCHAR(45) NULL DEFAULT NULL,
  `index` VARCHAR(45) NULL DEFAULT NULL,
  `contactId` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_address_contact1_idx` (`contactId` ASC),
  CONSTRAINT `fk_address_contact1`
  FOREIGN KEY (`contactId`)
  REFERENCES `anya_pinchuk`.`contact` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `anya_pinchuk`.`attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `anya_pinchuk`.`attachment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `date_of_download` DATETIME NULL DEFAULT NULL,
  `file_name` VARCHAR(45) NOT NULL,
  `comment` VARCHAR(45) NULL DEFAULT NULL,
  `contactId` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_attachment_sontact1_idx` (`contactId` ASC),
  CONSTRAINT `fk_attachment_contact1`
  FOREIGN KEY (`contactId`)
  REFERENCES `anya_pinchuk`.`contact` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `anya_pinchuk`.`phone_number`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `anya_pinchuk`.`phone_number` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `country_code` VARCHAR(45) NOT NULL,
  `operator_code` VARCHAR(45) NOT NULL,
  `number` VARCHAR(45) NOT NULL,
  `phone_type` ENUM('mobile', 'home') NULL DEFAULT NULL,
  `comment` VARCHAR(45) NULL DEFAULT NULL,
  `contactId` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_phonenumber_contact1_idx` (`contactId` ASC),
  CONSTRAINT `fk_phonenumber_contact1`
  FOREIGN KEY (`contactId`)
  REFERENCES `anya_pinchuk`.`contact` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `anya_pinchuk`.`photo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `anya_pinchuk`.`photo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `contactId` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `photo_name_uindex` (`name` ASC),
  INDEX `fk_photo_contact1_idx` (`contactId` ASC),
  CONSTRAINT `fk_photo_contact1`
  FOREIGN KEY (`contactId`)
  REFERENCES `anya_pinchuk`.`contact` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


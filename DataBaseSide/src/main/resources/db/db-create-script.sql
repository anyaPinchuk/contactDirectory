
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
  `marital_status` ENUM('single', 'married') NULL DEFAULT NULL,
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
  contact_id INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_address_contact1_idx` (contact_id ASC),
  CONSTRAINT `fk_address_contact1`
  FOREIGN KEY (contact_id)
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
  `comment` VARCHAR(500) NULL DEFAULT NULL,
  contact_id INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_attachment_sontact1_idx` (contact_id ASC),
  CONSTRAINT `fk_attachment_contact1`
  FOREIGN KEY (contact_id)
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
  `country_code` VARCHAR(10) NOT NULL,
  `operator_code` VARCHAR(10) NOT NULL,
  `number` VARCHAR(10) NOT NULL,
  `phone_type` ENUM('mobile', 'home') NULL DEFAULT NULL,
  `comment` VARCHAR(500) NULL DEFAULT NULL,
  contact_id INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_phonenumber_contact1_idx` (contact_id ASC),
  CONSTRAINT `fk_phonenumber_contact1`
  FOREIGN KEY (contact_id)
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
  contact_id INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `photo_name_uindex` (`name` ASC),
  INDEX `fk_photo_contact1_idx` (contact_id ASC),
  CONSTRAINT `fk_photo_contact1`
  FOREIGN KEY (contact_id)
  REFERENCES `anya_pinchuk`.`contact` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `third_name`, `date_of_birth`, `gender`, `marital_status`, `email`, `job`) VALUES ('Анна', 'Пинчук', 'Александровна', '1996-09-30', 'woman', 'married', 'anyapinchuk3@gmail.com', 'itechart');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `date_of_birth`, `gender`, `marital_status`, `email`) VALUES ('Марина', 'Иванкина', '1996-04-03', 'woman', 'married', 'marina_ivankina@gmail.com');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `gender`, `marital_status`, `email`, `job`) VALUES ('Кирилл', 'Зюсько', 'man', 'single', 'kira1996@mail.ru', 'itechart');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `date_of_birth`, `gender`, `citizenship`, `marital_status`, `email`, `job`) VALUES ('Алексей', 'Ващабрович', '1997-11-07', 'man', 'belorussian', 'single', 'alexey_ale@gmail.com', 'Sam Solutions');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `third_name`, `gender`, `marital_status`, `email`, `job`) VALUES ('Кристина', 'Качан', 'Юрьевна', 'woman', 'married', 'kris+kachan@gmail.com', 'EPAM');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `gender`, `marital_status`, `email`, `job`) VALUES ('Ann', 'Pinchuk', 'woman', 'married', 'nutaanuta-30.10.30@mail.ru', 'itechart');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `date_of_birth`, `gender`, `marital_status`, `email`) VALUES ('Hanna', 'Abbott', '1993-12-10', 'woman', 'single', 'hanna_abb@gmail.com');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `gender`, `citizenship`, `marital_status`, `email`, `job`) VALUES ('Karina', 'Avetisyan', 'woman', 'russian', 'married', 'karina_av12@mail.ru', 'itransition');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `gender`, `marital_status`, `email`, `job`) VALUES ('Галина', 'Куташ', 'woman', 'married', 'galina.kutash@mail.ru', 'itechart');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `gender`, `marital_status`, `email`, `job`) VALUES ('Матвей', 'Жартун', 'man', 'single', 'matvey123@gmail.com', 'itechart');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `gender`, `marital_status`, `email`) VALUES ('Lina', 'Bober', 'woman', 'single', 'lina.bober96@mail.ru');
INSERT INTO `anya_pinchuk`.`contact` (`name`, `surname`, `date_of_birth`, `gender`, `marital_status`, `email`, `job`) VALUES ('Владимир', 'Голышев', '1993-12-20', 'man', 'married', 'wanted65@gmail.com', 'peleng');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('1', '1');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('2', '2');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('3', '3');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('4', '4');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('5', '5');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('6', '6');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('7', '7');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('8', '8');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('9', '9');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('10', '10');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('11', '11');
INSERT INTO `anya_pinchuk`.`address` (`id`, `contact_id`) VALUES ('12', '12');


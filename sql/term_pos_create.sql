CREATE DATABASE `term_pos`;
USE `term_pos`;

/* Sale */
CREATE TABLE `sale` (
	`sale_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`created_at` DATETIME(6) NULL DEFAULT NULL,
	`is_income` BIT(1) NULL DEFAULT NULL,
	`total_price` INT(11) NULL DEFAULT '0',
	`payment` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
	PRIMARY KEY (`sale_id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* Product */
CREATE TABLE `product` (
	`product_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`provider` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`regular_price` INT(11) NOT NULL DEFAULT '0',
	`barcode` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`inventory` INT(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (`product_id`) USING BTREE,
	UNIQUE INDEX `barcode` (`barcode`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* InventoryChange */
CREATE TABLE `inventorychange` (
	`sale_id` BIGINT(20) NOT NULL,
	`product_id` BIGINT(20) NOT NULL,
	`count` INT(11) NOT NULL DEFAULT '0',
	`price_paid` INT(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (`sale_id`, `product_id`) USING BTREE,
	INDEX `fk_product_id_in` (`product_id`) USING BTREE,
	CONSTRAINT `fk_product_id_in` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT `fk_sale_id_in` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* Provider */
CREATE TABLE `provider` (
	`provider` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`provider_address` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`provider`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* Origin */
CREATE TABLE `origin` (
	`barcode` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`origin_country` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`barcode`) USING BTREE,
	CONSTRAINT `fk_barcode` FOREIGN KEY (`barcode`) REFERENCES `product` (`barcode`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* Discount */
CREATE TABLE `discount` (
	`discount_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`rate` INT(11) NOT NULL DEFAULT '0',
	`start_date` DATETIME(6) NOT NULL DEFAULT '1970-01-01 00:00:01.000000',
	`end_date` DATETIME(6) NOT NULL DEFAULT '1970-01-01 00:00:01.000000',
	`product_id` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (`discount_id`) USING BTREE,
	INDEX `fk_product_id` (`product_id`) USING BTREE,
	CONSTRAINT `fk_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* Cart */
CREATE TABLE `cart` (
	`cart_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`count` INT(11) NOT NULL DEFAULT '0',
	`product_id` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (`cart_id`) USING BTREE,
	INDEX `fk_product_id_cart` (`product_id`) USING BTREE,
	CONSTRAINT `fk_product_id_cart` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

/* CartPrice */
CREATE TABLE `cartprice` (
	`count` INT(11) NOT NULL,
	`product_id` BIGINT(20) NOT NULL DEFAULT '0',
	`final_price` INT(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (`count`, `product_id`) USING BTREE,
	INDEX `fk_product_id_cartprice` (`product_id`) USING BTREE,
	CONSTRAINT `fk_product_id_cartprice` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON UPDATE CASCADE ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

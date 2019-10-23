CREATE TABLE `Spreker`
(
    `id`   bigint(20)                        NOT NULL AUTO_INCREMENT,
    `name` varchar(255)                      NOT NULL,
    `uuid` VARCHAR(36) DEFAULT RANDOM_UUID() NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`name`)
);

CREATE TABLE `Categorie`
(
    `id`   bigint(20)                         NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `uuid` VARCHAR(36)  DEFAULT RANDOM_UUID() NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`name`)
);

CREATE TABLE `Citaat`
(
    `id`        bigint(20)                        NOT NULL AUTO_INCREMENT,
    `name`      varchar(4096)                     NOT NULL,
    `categorie` bigint(20)                        NOT NULL,
    `spreker`   bigint(20)                        NOT NULL,
    `uuid`      VARCHAR(36) DEFAULT RANDOM_UUID() NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`name`),
    CONSTRAINT `FK785288E6469F3E0A` FOREIGN KEY (`categorie`) REFERENCES `Categorie` (`id`),
    CONSTRAINT `FK785288E651A78AC` FOREIGN KEY (`spreker`) REFERENCES `Spreker` (`id`)
);



CREATE TABLE `author`
(
    `id`   binary(32)   NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE `genre`
(
    `id`   binary(32)   NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE `quote`
(
    `id`        binary(32)    NOT NULL,
    `name`      varchar(1023) NOT NULL,
    `genre_id`  binary(32)    NOT NULL,
    `author_id` binary(32)    NOT NULL,
    PRIMARY KEY (`id`),
--     KEY         `FK_quote_genre` (`genre_id`),
--     KEY         `FK_quote_author` (`author_id`),
    CONSTRAINT `FK_quote_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
    CONSTRAINT `FK_quote_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
);

CREATE TABLE `author`
(
    `id`   UUID         NOT NULL default random_uuid(),
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX idx_author_name ON author (name);

CREATE TABLE `genre`
(
    `id`   UUID         NOT NULL default random_uuid(),
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX idx_genre_name ON genre (name);

CREATE TABLE `quote`
(
    `id`        UUID          NOT NULL default random_uuid(),
    `name`      varchar(1023) NOT NULL,
    `genre_id`  UUID          NOT NULL,
    `author_id` UUID          NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_quote_author` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
    CONSTRAINT `FK_quote_genre` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
);

CREATE INDEX idx_quote_name ON quote (name);

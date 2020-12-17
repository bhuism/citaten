INSERT INTO author (`id`, `name`) VALUES (X'7d1fd954371f98887b4f0258a2044962', 'sOnbekend1');
INSERT INTO author (`id`, `name`) VALUES (X'a2a2e622c746647f3ef069ad63036857', 'sOnbekend2');
INSERT INTO author (`id`, `name`) VALUES (X'05727f9f725ee737592a92feec26eaea', 'sOnbekend3');

INSERT INTO genre (id, name) VALUES (X'94e4c3bd0c32b6871a06c0a607813fb3', 'conbekend1');
INSERT INTO genre (id, name) VALUES (X'cfa98af2d0191f190c86eefd3c3e3356', 'conbekend2');
INSERT INTO genre (id, name) VALUES (X'fac072ba03fc0ac327036c91a987e434', 'conbekend3');

INSERT INTO quote (id, name, genre_id, author_id) VALUES (X'9fce15e35a84e4c180cb3f5011a4350f','Test Citaat from the future of time and space1',X'94e4c3bd0c32b6871a06c0a607813fb3',X'7d1fd954371f98887b4f0258a2044962');
INSERT INTO quote (id, name, genre_id, author_id) VALUES (X'2c531cc7c2d32ecc319aafee18b0a7c8','Test Citaat from the future of time and space2',X'cfa98af2d0191f190c86eefd3c3e3356',X'7d1fd954371f98887b4f0258a2044962');
INSERT INTO quote (id, name, genre_id, author_id) VALUES (X'9320683af43674138ea76b0425d6250e','Test Citaat from the future of time and space3',X'fac072ba03fc0ac327036c91a987e434',X'05727f9f725ee737592a92feec26eaea');



DROP TABLE IF EXISTS monsters;

CREATE TABLE monsters (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name TINYTEXT UNIQUE KEY,
    hd int,
    ac int
);

INSERT INTO monsters
    (name, hd, ac)
values
    ('aerial servent', 16, 3),
    ('ankheg', 3, 2),
    ('ant, giant', 2, 3);

DROP TABLE IF EXISTS pregens;
CREATE TABLE pregens (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name TINYTEXT,
    class TINYTEXT,
    strength int,
    dexterity int,
    intelligence int,
    charisma int
);

--INSERT INTO 
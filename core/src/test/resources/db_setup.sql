create table tags
(
    id   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL
);

create table gift_certificates
(
    id               INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name             varchar(45)  NOT NULL,
    description      VARCHAR(200) NOT NULL,
    price            DECIMAL(10, 2),
    duration         INT NOT NULL,
    create_date      TIMESTAMP DEFAULT NOW(),
    last_update_date TIMESTAMP DEFAULT NOW()
);

create table gift_certificate_has_tag
(
    gift_certificate_id INT NOT NULL,
    tag_id         INT NOT NULL,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

create table users
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    login   varchar(45)  NOT NULL
);

create table orders
(
    id                  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cost                DECIMAL(10, 2),
    purchase_date       TIMESTAMP DEFAULT NOW(),
    user_id             INT NOT NULL,
    gift_certificate_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
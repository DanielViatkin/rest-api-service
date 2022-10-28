INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('certificate 1', 'description 1', 1.1, 1, '2021-01-01 01:11:10', '2021-01-01 01:11:11'),
       ('certificate 2', 'description 2', 2.2, 2, '2022-02-02 02:22:20', '2022-02-02 02:22:21'),
       ('certificate 3', 'description 3', 3.3, 3, '2023-03-03 03:33:30', '2023-03-03 03:33:31');

INSERT INTO tags (name) VALUES ('tag 1'), ('tag 2'), ('tag 3'), ('tag 4');

INSERT INTO gift_certificate_has_tag (gift_certificate_id, tag_id) VALUES (1, 1), (1, 2), (1, 3), (2, 2);
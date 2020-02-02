INSERT INTO order_types(order_type_name, numbered) VALUES ("external", true);
INSERT INTO order_types(order_type_name, numbered) VALUES ("internal", false);

SELECT t.id FROM order_types t WHERE t.order_type_name = 'internal';
INSERT INTO user_roles(role_name, order_type_id)
VALUES ("Administrator", (SELECT t.id FROM order_types t WHERE t.order_type_name = 'internal'));

INSERT INTO user_roles(role_name, order_type_id)
VALUES ("Sprzedawca", (SELECT t.id FROM order_types t WHERE t.order_type_name = 'internal'));

INSERT INTO user_roles(role_name, order_type_id)
VALUES ("Klient", (SELECT t.id FROM order_types t WHERE t.order_type_name = 'external'));
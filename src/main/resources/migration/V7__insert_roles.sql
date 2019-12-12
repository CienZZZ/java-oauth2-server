INSERT INTO roles(description,name) SELECT 'Admin', 'ADMIN'
WHERE NOT EXISTS (SELECT * FROM roles WHERE name = 'ADMIN');
INSERT INTO roles(description,name) SELECT 'User', 'USER'
WHERE NOT EXISTS (SELECT * FROM roles WHERE name = 'USER');
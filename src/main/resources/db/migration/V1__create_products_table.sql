DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE products
(
    uuid        UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(20)    NOT NULL CHECK (LENGTH(name) >= 5 AND LENGTH(name) <= 20),
    description VARCHAR(50) CHECK (LENGTH(description) >= 10 AND LENGTH(description) <= 50),
    price       DECIMAL(10, 2) NOT NULL CHECK (price > 0)
);

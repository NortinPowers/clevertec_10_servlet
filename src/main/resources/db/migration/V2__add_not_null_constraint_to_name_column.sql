ALTER TABLE products
    ADD CONSTRAINT name_not_null
        CHECK (name IS NOT NULL);

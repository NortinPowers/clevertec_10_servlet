ALTER TABLE products
    ADD CONSTRAINT price_not_null
        CHECK (price IS NOT NULL);

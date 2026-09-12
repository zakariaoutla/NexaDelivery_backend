ALTER TABLE driver
    ADD CONSTRAINT fk_driver_user
        FOREIGN KEY (id) REFERENCES users(id),

    ADD CONSTRAINT fk_driver_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),

    ADD CONSTRAINT fk_driver_zone
        FOREIGN KEY (zone_id) REFERENCES zone(id);


ALTER TABLE merchant
    ADD CONSTRAINT fk_merchant_user
        FOREIGN KEY (id) REFERENCES users(id);



ALTER TABLE delivery
    ADD CONSTRAINT fk_delivery_driver
        FOREIGN KEY (driver_id) REFERENCES driver(id),

    ADD CONSTRAINT fk_delivery_merchant
        FOREIGN KEY (merchant_id) REFERENCES merchant(id);


ALTER TABLE rating
    ADD CONSTRAINT fk_rating_delivery
        FOREIGN KEY (delivery_id) REFERENCES delivery(id);

ALTER TABLE collection_point
    ADD CONSTRAINT fk_collection_point_merchant
        FOREIGN KEY (merchant_id) REFERENCES merchant(id),

    ADD CONSTRAINT fk_collection_point_zone
        FOREIGN KEY (zone_id) REFERENCES zone(id);


ALTER TABLE driver_location
    ADD CONSTRAINT fk_driver_location_driver
        FOREIGN KEY (driver_id) REFERENCES driver(id);
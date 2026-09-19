ALTER TABLE driver
DROP FOREIGN KEY fk_driver_zone;

ALTER TABLE driver
DROP COLUMN zone_id;


ALTER TABLE collection_point
DROP FOREIGN KEY fk_collection_point_zone;

ALTER TABLE collection_point
DROP COLUMN zone_id;


DROP TABLE zone;
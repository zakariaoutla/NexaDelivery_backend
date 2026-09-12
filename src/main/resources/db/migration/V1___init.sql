create table users(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    telephone  VARCHAR(255) NOT NULL,
    role_user  VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE driver (
                        id BIGINT PRIMARY KEY,
                        driver_status VARCHAR(50) NOT NULL,
                        average_rating DOUBLE DEFAULT 0,
                        vehicle_id BIGINT UNIQUE,
                        zone_id BIGINT
);

CREATE TABLE merchant (
                          id BIGINT PRIMARY KEY,
                          business_name VARCHAR(255) NOT NULL
);
CREATE TABLE rating (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        score INT NOT NULL,
                        comment TEXT,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        delivery_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE vehicle (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         type VARCHAR(100) NOT NULL,
                         capacity_kg DOUBLE NOT NULL
);

CREATE TABLE zone (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      description TEXT
);

CREATE TABLE delivery (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          pickup_address VARCHAR(255),
                          drop_address VARCHAR(255),
                          description TEXT,
                          delivery_status VARCHAR(50) NOT NULL,
                          tracking_code VARCHAR(255) NOT NULL UNIQUE,
                          client_name VARCHAR(255) NOT NULL,
                          client_phone VARCHAR(50) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          driver_id BIGINT,
                          merchant_id BIGINT
);

CREATE TABLE collection_point (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  address VARCHAR(255) NOT NULL,
                                  latitude DOUBLE NOT NULL,
                                  longitude DOUBLE NOT NULL,
                                  merchant_id BIGINT NOT NULL,
                                  zone_id BIGINT NOT NULL
);

CREATE TABLE driver_location (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 latitude DOUBLE NOT NULL,
                                 longitude DOUBLE NOT NULL,
                                 timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 driver_id BIGINT NOT NULL
);


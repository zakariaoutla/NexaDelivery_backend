CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,

                               message VARCHAR(255) NOT NULL,
                               `read` BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               user_id BIGINT NOT NULL,
                               delivery_id BIGINT,

                               CONSTRAINT fk_notification_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id),

                               CONSTRAINT fk_notification_delivery
                                   FOREIGN KEY (delivery_id)
                                       REFERENCES delivery(id)
);
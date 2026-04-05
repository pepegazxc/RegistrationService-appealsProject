CREATE TABLE mayor_request(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL ,
    status_id BIGINT NOT NULL ,
    token VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    reviewed_at TIMESTAMPTZ DEFAULT null,
    is_used BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE mayor_request_status(
    id BIGSERIAL PRIMARY KEY,
    status_name VARCHAR(25) UNIQUE
);

INSERT INTO mayor_request_status (status_name) VALUES ('PENDING'), ('APPROVED'), ('REJECTED'), ('EXPIRED');

ALTER TABLE mayor_request
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE mayor_request
ADD CONSTRAINT fk_status_id
FOREIGN KEY (status_id) REFERENCES mayor_request_status(id);
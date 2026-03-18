CREATE TABLE admin_request_status(
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50)
);
INSERT INTO admin_request_status (status) VALUES ('PENDING'), ('APPROVED'), ('REJECTED'), ('EXPIRED');
CREATE TABLE admin_request(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status_id BIGINT,
    token VARCHAR(255) NOT NULL,
    created_at timestamptz DEFAULT now() NOT NULL,
    expires_at timestamptz NOT NULL,
    reviewed_at timestamptz
);

CREATE UNIQUE INDEX one_admin_request_per_user
ON admin_request (user_id)
WHERE status_id = 1;
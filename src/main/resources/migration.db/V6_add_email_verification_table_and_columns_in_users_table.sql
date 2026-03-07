CREATE TABLE email_verification_tokens(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    hash_token VARCHAR(512) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    used BOOLEAN DEFAULT FALSE
);

ALTER TABLE users ADD COLUMN
is_email_verified BOOLEAN DEFAULT FALSE NOT NULL;

ALTER TABLE users ADD COLUMN
is_active BOOLEAN DEFAULT TRUE NOT NULL;

ALTER TABLE email_verification_tokens ADD CONSTRAINT fk_email_verification
FOREIGN KEY (user_id) REFERENCES users(id)


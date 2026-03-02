
CREATE TABLE roles(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    role_name VARCHAR(50) UNIQUE
);
INSERT INTO roles(role_name)
VALUES ('ROLE_USER'),('ROLE_ADMIN'),('ROLE_MAYOR');
ALTER TABLE users
    RENAME roles to  role_id;
ALTER TABLE users
    Alter COLUMN role_id TYPE BIGINT
        USING role_id::bigint;
ALTER TABLE users
    ALTER COLUMN role_id SET NOT NULL;
ALTER TABLE users
    ADD CONSTRAINT fk_roles
        FOREIGN KEY (role_id) REFERENCES roles(id);

DROP INDEX CONCURRENTLY one_admin_request_per_user;
CREATE UNIQUE INDEX one_admin_request_per_user
    ON admin_request (user_id)
    WHERE status_id = 1 AND 2;
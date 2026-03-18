ALTER TABLE admin_request
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE admin_request
ADD CONSTRAINT fk_status_id
FOREIGN KEY (status_id) REFERENCES admin_request_status(id);
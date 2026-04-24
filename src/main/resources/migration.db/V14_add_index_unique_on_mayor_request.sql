CREATE UNIQUE INDEX one_mayor_request_per_user
    ON mayor_request (user_id)
    WHERE status_id IN(1,2) ;
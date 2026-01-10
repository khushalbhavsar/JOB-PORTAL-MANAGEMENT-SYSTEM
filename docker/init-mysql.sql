-- Job Portal MySQL Initialization Script
-- Creates necessary databases for microservices

CREATE DATABASE IF NOT EXISTS jobportal_auth;
CREATE DATABASE IF NOT EXISTS jobportal_users;
CREATE DATABASE IF NOT EXISTS jobportal_applications;
CREATE DATABASE IF NOT EXISTS jobportal_admin;

-- Grant permissions
GRANT ALL PRIVILEGES ON jobportal_auth.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON jobportal_users.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON jobportal_applications.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON jobportal_admin.* TO 'root'@'%';

FLUSH PRIVILEGES;

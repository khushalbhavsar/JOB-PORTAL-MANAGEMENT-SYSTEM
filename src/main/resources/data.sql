-- =============================================
-- JOB PORTAL MANAGEMENT SYSTEM
-- Sample Data for Testing
-- =============================================

-- All passwords are: password123
-- BCrypt hash for 'password123': $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG

-- Insert Admin User (Password: password123)
INSERT INTO users (name, email, password, role, phone, is_active, created_at) VALUES 
('System Admin', 'admin@jobportal.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN', '9876543210', true, CURRENT_TIMESTAMP);

-- Insert Recruiters (Password: password123)
INSERT INTO users (name, email, password, role, phone, is_active, created_at) VALUES 
('Rahul Sharma', 'rahul@techcorp.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'RECRUITER', '9876543211', true, CURRENT_TIMESTAMP),
('Priya Patel', 'priya@innovate.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'RECRUITER', '9876543212', true, CURRENT_TIMESTAMP);

-- Insert Job Seekers (Password: password123)
INSERT INTO users (name, email, password, role, phone, skills, experience, education, is_active, created_at) VALUES 
('Amit Kumar', 'amit@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'JOB_SEEKER', '9876543213', 'Java, Spring Boot, MySQL, REST APIs', '3 years', 'B.Tech Computer Science', true, CURRENT_TIMESTAMP),
('Sneha Gupta', 'sneha@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'JOB_SEEKER', '9876543214', 'Python, Django, PostgreSQL, Machine Learning', '2 years', 'M.Tech AI/ML', true, CURRENT_TIMESTAMP),
('Vikram Singh', 'vikram@gmail.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'JOB_SEEKER', '9876543215', 'React, Node.js, MongoDB, JavaScript', '1 year', 'B.Tech IT', true, CURRENT_TIMESTAMP);

-- Insert Recruiter Profiles
INSERT INTO recruiters (company_name, company_description, company_website, industry, company_size, headquarters, founded_year, is_verified, user_id, created_at) VALUES 
('TechCorp Solutions', 'Leading technology company specializing in enterprise software solutions', 'https://techcorp.com', 'Information Technology', '500-1000', 'Bangalore, India', 2010, true, 2, CURRENT_TIMESTAMP),
('Innovate Labs', 'Cutting-edge AI and ML research company', 'https://innovatelabs.com', 'Artificial Intelligence', '100-500', 'Mumbai, India', 2015, true, 3, CURRENT_TIMESTAMP);

-- Insert Jobs
INSERT INTO jobs (title, description, requirements, skills, location, job_type, experience_level, min_salary, max_salary, salary_currency, vacancies, is_active, is_remote, views_count, applications_count, recruiter_id, created_at) VALUES 
('Senior Java Developer', 'We are looking for an experienced Java Developer to join our team. You will be responsible for developing high-quality software solutions using Java and Spring Boot.', 
'5+ years of experience in Java development, Strong knowledge of Spring Boot and Microservices, Experience with MySQL and MongoDB', 
'Java, Spring Boot, Microservices, MySQL, MongoDB, REST APIs', 'Bangalore, India', 'FULL_TIME', 'SENIOR', 1500000, 2500000, 'INR', 3, true, false, 150, 12, 1, CURRENT_TIMESTAMP),

('Python Data Scientist', 'Join our AI team to work on cutting-edge machine learning projects. You will be analyzing large datasets and building predictive models.', 
'3+ years of experience in Data Science, Proficiency in Python and ML frameworks, Strong statistical analysis skills', 
'Python, TensorFlow, PyTorch, Pandas, NumPy, Machine Learning, Deep Learning', 'Mumbai, India', 'FULL_TIME', 'MID', 1200000, 2000000, 'INR', 2, true, true, 200, 18, 2, CURRENT_TIMESTAMP),

('Full Stack Developer', 'Looking for a talented Full Stack Developer to build and maintain web applications. You will work with both frontend and backend technologies.', 
'2+ years of experience in full stack development, Strong knowledge of React and Node.js, Experience with databases', 
'React, Node.js, JavaScript, TypeScript, MongoDB, PostgreSQL', 'Hyderabad, India', 'FULL_TIME', 'MID', 800000, 1500000, 'INR', 5, true, true, 180, 25, 1, CURRENT_TIMESTAMP),

('DevOps Engineer', 'We need a DevOps Engineer to manage our cloud infrastructure and CI/CD pipelines. You will ensure smooth deployment and monitoring of applications.', 
'3+ years of DevOps experience, Strong knowledge of AWS/Azure, Experience with Docker and Kubernetes', 
'AWS, Azure, Docker, Kubernetes, Jenkins, Terraform, Linux', 'Pune, India', 'FULL_TIME', 'MID', 1000000, 1800000, 'INR', 2, true, false, 120, 8, 2, CURRENT_TIMESTAMP),

('Junior Frontend Developer', 'Great opportunity for freshers to start their career in frontend development. Training will be provided.', 
'Good understanding of HTML, CSS, and JavaScript, Eager to learn React or Angular, Good communication skills', 
'HTML, CSS, JavaScript, React, Bootstrap', 'Chennai, India', 'FULL_TIME', 'ENTRY', 400000, 600000, 'INR', 10, true, false, 300, 45, 1, CURRENT_TIMESTAMP);

-- Insert Sample Applications
INSERT INTO applications (cover_letter, resume_url, status, applied_date, user_id, job_id, created_at) VALUES 
('I am very interested in the Senior Java Developer position. With my 3 years of experience in Java and Spring Boot, I believe I can contribute effectively to your team.', 
'/resumes/amit_kumar_resume.pdf', 'APPLIED', CURRENT_TIMESTAMP, 4, 1, CURRENT_TIMESTAMP),

('I would love to join Innovate Labs as a Data Scientist. My expertise in Python and Machine Learning aligns well with your requirements.', 
'/resumes/sneha_gupta_resume.pdf', 'SHORTLISTED', CURRENT_TIMESTAMP, 5, 2, CURRENT_TIMESTAMP),

('As a fresher with strong frontend skills, I am excited about the Junior Frontend Developer opportunity. I am eager to learn and grow.', 
'/resumes/vikram_singh_resume.pdf', 'UNDER_REVIEW', CURRENT_TIMESTAMP, 6, 5, CURRENT_TIMESTAMP);

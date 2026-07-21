DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS responded_applicants CASCADE;
DROP TABLE IF EXISTS work_experience_info CASCADE;
DROP TABLE IF EXISTS education_info CASCADE;
DROP TABLE IF EXISTS vacancies CASCADE;
DROP TABLE IF EXISTS contacts_info CASCADE;
DROP TABLE IF EXISTS resumes CASCADE;
DROP TABLE IF EXISTS contact_types CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;


CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT REFERENCES categories (id)
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255),
    age INT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(55),
    avatar VARCHAR(500),
    account_type VARCHAR(50) NOT NULL
);

CREATE TABLE contact_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(100) NOT NULL
);

CREATE TABLE resumes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    applicant_id BIGINT REFERENCES users (id),
    name VARCHAR(255) NOT NULL,
    category_id BIGINT REFERENCES categories (id),
    salary DOUBLE,
    is_active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP,
    update_time TIMESTAMP
);

CREATE TABLE contacts_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_id BIGINT REFERENCES contact_types (id),
    resume_id BIGINT REFERENCES resumes (id),
    "value" VARCHAR(255)
);

CREATE TABLE vacancies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT REFERENCES categories (id),
    salary DOUBLE,
    exp_from INT,
    exp_to INT,
    is_active BOOLEAN DEFAULT TRUE,
    author_id BIGINT REFERENCES users (id),
    created_date TIMESTAMP,
    update_time TIMESTAMP
);

CREATE TABLE education_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resume_id BIGINT REFERENCES resumes (id),
    institution VARCHAR(255),
    program VARCHAR(255),
    start_date DATE,
    end_date DATE,
    degree VARCHAR(255)
);

CREATE TABLE work_experience_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resume_id BIGINT REFERENCES resumes (id),
    years INT,
    company_name VARCHAR(255),
    position VARCHAR(255),
    responsibilities TEXT
);

CREATE TABLE responded_applicants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resume_id BIGINT REFERENCES resumes (id),
    vacancy_id BIGINT REFERENCES vacancies (id),
    confirmation BOOLEAN DEFAULT FALSE
);

CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    responded_applicants_id BIGINT REFERENCES responded_applicants (id),
    content TEXT,
    timestamp TIMESTAMP
);




INSERT INTO categories (name, parent_id) VALUES ('IT', NULL);
INSERT INTO categories (name, parent_id) VALUES ('Backend разработка', 1);
INSERT INTO categories (name, parent_id) VALUES ('Frontend разработка', 1);

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Иван', 'Иванов', 25, 'ivan@mail.com', 'pass123', '+996700111222', NULL, 'APPLICANT');

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('ООО Технологии', NULL, NULL, 'hr@technologii.kg', 'pass456', '+996700333444', NULL, 'EMPLOYER');

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES (1, 'Java Backend разработчик', 2, 80000, TRUE, '2026-01-10 10:00:00', '2026-06-01 09:00:00');

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES (1, 'Frontend разработчик', 3, 65000, TRUE, '2026-02-15 12:00:00', '2026-06-10 14:00:00');

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES ('Java Backend разработчик', 'Разработка REST API на Spring Boot', 2, 90000, 1, 3, TRUE, 2, '2026-03-01 09:00:00', '2026-06-05 11:00:00');

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES ('Frontend разработчик', 'Разработка интерфейсов на React', 3, 70000, 0, 2, TRUE, 2, '2026-03-05 09:00:00', '2026-06-06 11:00:00');

INSERT INTO contact_types (type) VALUES ('Telegram');
INSERT INTO contact_types (type) VALUES ('Email');
INSERT INTO contact_types (type) VALUES ('LinkedIn');

INSERT INTO contacts_info (type_id, resume_id, "value") VALUES (1, 1, '@ivan_dev');
INSERT INTO contacts_info (type_id, resume_id, "value") VALUES (2, 1, 'ivan@mail.com');

INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES (1, 'КНУ им. Ж. Баласагына', 'Программная инженерия', '2018-09-01', '2022-06-30', 'Бакалавр');

INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES (1, 2, 'ИП Тестов', 'Java разработчик', 'Разработка и поддержка backend-сервисов');

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES (1, 1, TRUE);

INSERT INTO messages (responded_applicants_id, content, timestamp)
VALUES (1, 'Здравствуйте! Заинтересован в вашей вакансии.', '2026-06-15 10:30:00');
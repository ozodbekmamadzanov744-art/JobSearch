INSERT INTO categories (name, parent_id) VALUES ('IT', NULL);
INSERT INTO categories (name, parent_id) VALUES ('Backend разработка', (SELECT id FROM categories WHERE name = 'IT'));
INSERT INTO categories (name, parent_id) VALUES ('Frontend разработка', (SELECT id FROM categories WHERE name = 'IT'));

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('Иван', 'Иванов', 25, 'ivan@mail.com', 'pass123', '+996700111222', NULL, 'APPLICANT');

INSERT INTO users (name, surname, age, email, password, phone_number, avatar, account_type)
VALUES ('ООО Технологии', NULL, NULL, 'hr@technologii.kg', 'pass456', '+996700333444', NULL, 'EMPLOYER');

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES ((SELECT id FROM users WHERE email = 'ivan@mail.com'),
        'Java Backend разработчик',
        (SELECT id FROM categories WHERE name = 'Backend разработка'),
        80000, TRUE, '2026-01-10 10:00:00', '2026-06-01 09:00:00');

INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time)
VALUES ((SELECT id FROM users WHERE email = 'ivan@mail.com'),
        'Frontend разработчик',
        (SELECT id FROM categories WHERE name = 'Frontend разработка'),
        65000, TRUE, '2026-02-15 12:00:00', '2026-06-10 14:00:00');

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES ('Java Backend разработчик', 'Разработка REST API на Spring Boot',
        (SELECT id FROM categories WHERE name = 'Backend разработка'),
        90000, 1, 3, TRUE,
        (SELECT id FROM users WHERE email = 'hr@technologii.kg'),
        '2026-03-01 09:00:00', '2026-06-05 11:00:00');

INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time)
VALUES ('Frontend разработчик', 'Разработка интерфейсов на React',
        (SELECT id FROM categories WHERE name = 'Frontend разработка'),
        70000, 0, 2, TRUE,
        (SELECT id FROM users WHERE email = 'hr@technologii.kg'),
        '2026-03-05 09:00:00', '2026-06-06 11:00:00');

INSERT INTO contact_types (type) VALUES ('Telegram');
INSERT INTO contact_types (type) VALUES ('Email');
INSERT INTO contact_types (type) VALUES ('LinkedIn');

INSERT INTO contacts_info (type_id, resume_id, "value")
VALUES ((SELECT id FROM contact_types WHERE type = 'Telegram'),
        (SELECT id FROM resumes WHERE name = 'Java Backend разработчик'),
        '@ivan_dev');

INSERT INTO contacts_info (type_id, resume_id, "value")
VALUES ((SELECT id FROM contact_types WHERE type = 'Email'),
        (SELECT id FROM resumes WHERE name = 'Java Backend разработчик'),
        'ivan@mail.com');

INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree)
VALUES ((SELECT id FROM resumes WHERE name = 'Java Backend разработчик'),
        'КНУ им. Ж. Баласагына', 'Программная инженерия', '2018-09-01', '2022-06-30', 'Бакалавр');

INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities)
VALUES ((SELECT id FROM resumes WHERE name = 'Java Backend разработчик'),
        2, 'ИП Тестов', 'Java разработчик', 'Разработка и поддержка backend-сервисов');

INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation)
VALUES ((SELECT id FROM resumes WHERE name = 'Java Backend разработчик'),
        (SELECT id FROM vacancies WHERE name = 'Java Backend разработчик'),
        TRUE);

INSERT INTO messages (responded_applicants_id, content, timestamp)
VALUES ((SELECT ra.id FROM responded_applicants ra
                               JOIN resumes r ON ra.resume_id = r.id
                               JOIN vacancies v ON ra.vacancy_id = v.id
         WHERE r.name = 'Java Backend разработчик' AND v.name = 'Java Backend разработчик'),
        'Здравствуйте! Заинтересован в вашей вакансии.', '2026-06-15 10:30:00');
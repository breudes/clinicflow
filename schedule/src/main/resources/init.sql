DO $$
BEGIN
    CREATE TYPE user_type AS ENUM ('PATIENT', 'DOCTOR', 'NURSE');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$
BEGIN
    CREATE TYPE appointment_status AS ENUM ('SCHEDULED', 'CANCELED', 'UPDATED', 'COMPLETED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;


CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    birth_date DATE,
    user_type user_type NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    license VARCHAR(50),
    specialty VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    status appointment_status NOT NULL,
    date_time TIMESTAMP NOT NULL,
    timezone VARCHAR(50),

    CONSTRAINT fk_patient
        FOREIGN KEY (patient_id) REFERENCES users(id),

    CONSTRAINT fk_doctor
        FOREIGN KEY (doctor_id) REFERENCES users(id)
);

INSERT INTO users (
    name, username, password, email, birth_date, user_type, active
) VALUES (
    'Maria Silva',
    'maria.silva',
    '$2a$10$niylP0dR80IlKDWAPGQOBeGagkw4p1irCfodsuWCVU1yc.nMdq2Iu',
    'minhapepi@gmail.com',
    '1995-04-10',
    'PATIENT',
    TRUE
);

INSERT INTO users (
    name, username, password, email, birth_date, user_type, active, license, specialty
) VALUES (
    'Dr. João Souza',
    'joao.souza',
    '$2a$10$f27jzpWU3XCNt.2Q1qcUHeQHv.tSCeF1uAsGSp2yM6fR2pCRajgpa',
    'joao@email.com',
    '1980-08-22',
    'DOCTOR',
    TRUE,
    'CRM-123456',
    'Cardiologia'
);

INSERT INTO users (
    name, username, password, email, birth_date, user_type, active, license, specialty
) VALUES (
    'Ana Pereira',
    'ana.pereira',
    '$2a$10$XlZ65j.0jcrCzItH4BzN7e/sWUyUTnVcBQCTgT/ff5LfMdLXXEd9q',
    'breudes@outlook.com',
    '1988-02-15',
    'NURSE',
    TRUE,
    'COREN-987654',
    'UTI'
);

INSERT INTO appointments (
    patient_id, doctor_id, status, date_time, timezone
) VALUES (
    1,
    2,
    'SCHEDULED',
    '2026-01-15T14:00:00',
    'America/Sao_Paulo'
);
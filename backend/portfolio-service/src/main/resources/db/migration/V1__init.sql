
CREATE TYPE skill_category AS ENUM ('ARCHITECTURE', 'DEVELOPMENT', 'DEVOPS', 'MISCELLANEOUS', 'TESTING');

CREATE TABLE projects (
    is_deleted BOOLEAN NOT NULL,
    project_id UUID PRIMARY KEY,
    banner VARCHAR(255) NOT NULL,
    description VARCHAR NOT NULL,
    link VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    CONSTRAINT uq_projects_title UNIQUE (title),
    CONSTRAINT uq_projects_link UNIQUE (link)
);

CREATE TABLE skills (
    skill_id UUID PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    storage_key TEXT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE testimonials (
    is_deleted BOOLEAN NOT NULL,
    testimonial_id UUID PRIMARY KEY,
    accent VARCHAR(255) NOT NULL,
    initials VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    text VARCHAR NOT NULL,
    CONSTRAINT uq_testimonials_name UNIQUE (name)
);

CREATE TABLE workexps (
    end_date DATE,
    is_deleted BOOLEAN NOT NULL,
    start_date DATE NOT NULL,
    work_exp_id UUID PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    note VARCHAR,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT uq_workexps_role_company_start UNIQUE (role, company, start_date)
);

CREATE TABLE project_tech (
    position INTEGER NOT NULL,
    project_project_id UUID NOT NULL,
    tech VARCHAR(255),
    CONSTRAINT pk_project_tech PRIMARY KEY (position, project_project_id),
    CONSTRAINT fk_project FOREIGN KEY (project_project_id) REFERENCES projects(project_id)
);

CREATE TABLE work_exp_points (
    position INTEGER NOT NULL,
    work_exp_work_exp_id UUID NOT NULL,
    points VARCHAR(255),
    CONSTRAINT pk_work_exp_points PRIMARY KEY (position, work_exp_work_exp_id),
    CONSTRAINT fk_work_exp FOREIGN KEY (work_exp_work_exp_id) REFERENCES workexps(work_exp_id)
);

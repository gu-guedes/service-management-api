--liquibase formatted sql

--changeset gguedes:100-reset-schema-vet
-- RESET TOTAL (DEV/TEST): derruba tabelas antigas e recria modelo final

DROP TABLE IF EXISTS public.service_orders CASCADE;
DROP TABLE IF EXISTS public.services CASCADE;
DROP TABLE IF EXISTS public.patients CASCADE;
DROP TABLE IF EXISTS public.service_categories CASCADE;
DROP TABLE IF EXISTS public.service_types CASCADE;
DROP TABLE IF EXISTS public.customers CASCADE;
DROP TABLE IF EXISTS public.app_user CASCADE;


-- app_user
CREATE TABLE public.app_user (
                                 id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 username character varying NOT NULL,
                                 password_hash character varying NOT NULL,
                                 active boolean DEFAULT true,
                                 created_at timestamp with time zone DEFAULT now() NOT NULL,
                                 updated_at timestamp with time zone DEFAULT now() NOT NULL,
                                 CONSTRAINT app_user_username_key UNIQUE (username)
);

-- customers
CREATE TABLE public.customers (
                                  id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  name character varying NOT NULL,
                                  email character varying,
                                  phone character varying,
                                  created_at timestamp with time zone DEFAULT now() NOT NULL,
                                  updated_at timestamp with time zone DEFAULT now() NOT NULL,
                                  CONSTRAINT customers_email_key UNIQUE (email)
);

-- service_types
CREATE TABLE public.service_types (
                                      id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                      code varchar(30) NOT NULL,
                                      name varchar(120) NOT NULL,
                                      active boolean DEFAULT true,
                                      created_at timestamp with time zone DEFAULT now() NOT NULL,
                                      CONSTRAINT uq_service_types_code UNIQUE (code)
);

-- service_categories
CREATE TABLE public.service_categories (
                                           id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                           service_type_id bigint NOT NULL,
                                           name varchar(120) NOT NULL,
                                           description text,
                                           active boolean DEFAULT true,
                                           created_at timestamp with time zone DEFAULT now() NOT NULL,
                                           CONSTRAINT fk_service_categories_type
                                               FOREIGN KEY (service_type_id)
                                                   REFERENCES public.service_types(id)
                                                   ON DELETE RESTRICT,
                                           CONSTRAINT uq_category_per_type
                                               UNIQUE (service_type_id, name)
);

-- services (catálogo)
CREATE TABLE public.services (
                                 id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 service_category_id bigint NOT NULL,
                                 name character varying NOT NULL,
                                 description text,
                                 base_price numeric(10,2),
                                 active boolean DEFAULT true,
                                 created_at timestamp with time zone DEFAULT now() NOT NULL,
                                 CONSTRAINT fk_services_category
                                     FOREIGN KEY (service_category_id)
                                         REFERENCES public.service_categories(id)
                                         ON DELETE RESTRICT
);

-- patients (pets)
CREATE TABLE public.patients (
                                 id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 customer_id bigint NOT NULL,
                                 name varchar(120) NOT NULL,
                                 species varchar(50) NOT NULL,
                                 breed varchar(100),
                                 sex char(1),
                                 birth_date date,
                                 weight_kg numeric(5,2),
                                 neutered boolean DEFAULT false,
                                 notes text,
                                 active boolean DEFAULT true,
                                 created_at timestamp with time zone DEFAULT now() NOT NULL,
                                 CONSTRAINT patients_sex_check CHECK (sex IS NULL OR sex IN ('M','F')),
                                 CONSTRAINT fk_patients_customer
                                     FOREIGN KEY (customer_id)
                                         REFERENCES public.customers(id)
                                         ON DELETE RESTRICT
);

-- service_orders
CREATE TABLE public.service_orders (
                                       id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       patient_id bigint NOT NULL,
                                       service_id bigint NOT NULL,
                                       created_by bigint,
                                       status character varying NOT NULL,
                                       service_date timestamp with time zone NOT NULL,
                                       notes text,
                                       total_price numeric(10,2),
                                       created_at timestamp with time zone DEFAULT now() NOT NULL,
                                       CONSTRAINT service_orders_status_check
                                           CHECK (status IN ('PENDING','IN_PROGRESS','COMPLETED','CANCELED')),
                                       CONSTRAINT fk_service_orders_patient
                                           FOREIGN KEY (patient_id) REFERENCES public.patients(id) ON DELETE RESTRICT,
                                       CONSTRAINT fk_service_orders_service
                                           FOREIGN KEY (service_id) REFERENCES public.services(id) ON DELETE RESTRICT,
                                       CONSTRAINT fk_service_orders_user
                                           FOREIGN KEY (created_by) REFERENCES public.app_user(id) ON DELETE SET NULL
);

-- indexes úteis
CREATE INDEX IF NOT EXISTS ix_patients_customer ON public.patients(customer_id);
CREATE INDEX IF NOT EXISTS ix_service_categories_type ON public.service_categories(service_type_id);
CREATE INDEX IF NOT EXISTS ix_services_category ON public.services(service_category_id);
CREATE INDEX IF NOT EXISTS ix_service_orders_patient ON public.service_orders(patient_id);
CREATE INDEX IF NOT EXISTS ix_service_orders_service ON public.service_orders(service_id);

-- seeds mínimos
INSERT INTO public.service_types (code, name)
VALUES ('CONSULTA','Consulta'), ('CIRURGIA','Cirurgia')
ON CONFLICT (code) DO NOTHING;

--changeset gguedes:101-seed-surgery-categories

INSERT INTO public.service_categories (service_type_id, name, description)
SELECT st.id, x.name, x.description
FROM public.service_types st
         JOIN (VALUES
                   ('Castração', 'Procedimento cirúrgico de esterilização'),
                   ('Limpeza de dente', 'Procedimento odontológico com anestesia'),
                   ('Otohematoma', 'Correção cirúrgica de otohematoma'),
                   ('Tumor', 'Remoção cirúrgica de tumor')
) AS x(name, description) ON true
WHERE st.code = 'CIRURGIA'
ON CONFLICT (service_type_id, name) DO NOTHING;

--changeset gguedes:102-seed-consult-categories

INSERT INTO public.service_categories (service_type_id, name, description)
SELECT st.id, x.name, x.description
FROM public.service_types st
         JOIN (VALUES
                   ('Dermato', 'Consulta dermatológica'),
                   ('Verminose', 'Consulta para tratamento de verminoses'),
                   ('Briga', 'Consulta por ferimentos decorrentes de briga'),
                   ('Leishmaniose', 'Consulta para diagnóstico e acompanhamento de leishmaniose')
) AS x(name, description) ON true
WHERE st.code = 'CONSULTA'
ON CONFLICT (service_type_id, name) DO NOTHING;

--changeset gguedes:103-seed-services-initial runOnChange:true

INSERT INTO public.services (service_category_id, name, description, base_price)
SELECT sc.id, 'Consulta Dermato', 'Consulta dermatológica', 150.00
FROM public.service_categories sc
         JOIN public.service_types st ON st.id = sc.service_type_id
WHERE st.code = 'CONSULTA' AND sc.name = 'Dermato';


INSERT INTO public.services (service_category_id, name, description, base_price)
SELECT sc.id, 'Consulta Verminose', 'Tratamento de verminoses', 130.00
FROM public.service_categories sc
         JOIN public.service_types st ON st.id = sc.service_type_id
WHERE st.code = 'CONSULTA' AND sc.name = 'Verminose';


INSERT INTO public.services (service_category_id, name, description, base_price)
SELECT sc.id, 'Castração (base)', 'Castração - preço base', 500.00
FROM public.service_categories sc
         JOIN public.service_types st ON st.id = sc.service_type_id
WHERE st.code = 'CIRURGIA' AND sc.name = 'Castração';


--changeset gguedes:104-update-patients-sex-constraint
ALTER TABLE public.patients ALTER COLUMN sex TYPE varchar(10);
ALTER TABLE public.patients DROP CONSTRAINT IF EXISTS patients_sex_check;
ALTER TABLE public.patients ADD CONSTRAINT patients_sex_check
    CHECK (sex IS NULL OR sex IN ('macho', 'femea'));

--changeset gguedes:105-create-medical-records
-- Prontuário simples: histórico de atendimento por pet, sem depender do catálogo de serviços

CREATE TABLE public.medical_records (
                                         id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                         patient_id bigint NOT NULL,
                                         record_date timestamp with time zone DEFAULT now() NOT NULL,
                                         description text NOT NULL,
                                         weight_kg numeric(5,2),
                                         created_at timestamp with time zone DEFAULT now() NOT NULL,
                                         CONSTRAINT fk_medical_records_patient
                                             FOREIGN KEY (patient_id)
                                                 REFERENCES public.patients(id)
                                                 ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS ix_medical_records_patient ON public.medical_records(patient_id);

--changeset gguedes:106-add-customer-address
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS address character varying(255);

--changeset gguedes:107-split-customer-address
ALTER TABLE public.customers DROP COLUMN IF EXISTS address;
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS street character varying(150);
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS street_number character varying(20);
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS neighborhood character varying(100);
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS city character varying(100);
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS reference_point character varying(150);

--changeset gguedes:108-patient-age-instead-of-birthdate
-- Nem todo tutor sabe a data exata de nascimento do pet, só uma nocao de idade
ALTER TABLE public.patients DROP COLUMN IF EXISTS birth_date;
ALTER TABLE public.patients ADD COLUMN IF NOT EXISTS age_years smallint;

--changeset gguedes:109-fix-age-years-column-type
-- Hibernate mapeia Integer para "integer" (int4), nao "smallint" (int2)
ALTER TABLE public.patients ALTER COLUMN age_years TYPE integer;

--changeset gguedes:110-medical-record-complaint-treatment
-- Substitui a descricao generica por queixa e tratamento, campos especificos do atendimento
ALTER TABLE public.medical_records ADD COLUMN IF NOT EXISTS complaint text NOT NULL DEFAULT '';
ALTER TABLE public.medical_records ADD COLUMN IF NOT EXISTS treatment text NOT NULL DEFAULT '';
ALTER TABLE public.medical_records DROP COLUMN IF EXISTS description;

--changeset gguedes:111-customer-birth-date
-- Data de nascimento do tutor (opcional) — usada pra lembrete de aniversario
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS birth_date date;

--changeset gguedes:112-medical-record-follow-up
-- Lembrete de retorno pos-atendimento (opcional) — ex: avisar tutor apos fim de um tratamento
ALTER TABLE public.medical_records ADD COLUMN IF NOT EXISTS follow_up_date date;
ALTER TABLE public.medical_records ADD COLUMN IF NOT EXISTS follow_up_done boolean DEFAULT false NOT NULL;

--changeset gguedes:113-product-applications
-- Controle de validade de produtos (coleira, vermifugo, vacina) vendidos sem abrir atendimento.
-- So a aplicacao mais recente de cada produto por pet conta pra decidir se esta vencendo
-- (regra aplicada no frontend) — por isso nao tem campo de "resolvido" aqui.
CREATE TABLE public.product_applications (
                                              id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                              patient_id bigint NOT NULL,
                                              product_name varchar(120) NOT NULL,
                                              applied_date date,
                                              expires_at date NOT NULL,
                                              notes text,
                                              created_at timestamp with time zone DEFAULT now() NOT NULL,
                                              CONSTRAINT fk_product_applications_patient
                                                  FOREIGN KEY (patient_id)
                                                      REFERENCES public.patients(id)
                                                      ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS ix_product_applications_patient ON public.product_applications(patient_id);

--changeset gguedes:114-exam-requests
-- Exames solicitados num atendimento, com resultado (PDF) anexado depois que chega.
-- Guardado direto no Postgres (bytea) — sem storage externo, sem disco persistente no Render.
CREATE TABLE public.exam_requests (
                                       id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       medical_record_id bigint NOT NULL,
                                       exam_name varchar(120) NOT NULL,
                                       requested_date date,
                                       result_file bytea,
                                       result_file_name varchar(255),
                                       result_uploaded_at timestamp with time zone,
                                       created_at timestamp with time zone DEFAULT now() NOT NULL,
                                       CONSTRAINT fk_exam_requests_medical_record
                                           FOREIGN KEY (medical_record_id)
                                               REFERENCES public.medical_records(id)
                                               ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS ix_exam_requests_medical_record ON public.exam_requests(medical_record_id);

--changeset gguedes:111-medical-records-anamnesis
-- Anamnese obrigatoria: historico clinico relatado pelo tutor no atendimento
-- (substitui a versao opcional que existia so nesta branch, nunca chegou a rodar em producao)
ALTER TABLE public.medical_records ADD COLUMN anamnesis text NOT NULL DEFAULT '';
ALTER TABLE public.medical_records ALTER COLUMN anamnesis DROP DEFAULT;

--changeset gguedes:112-customer-patient-soft-delete
-- "deleted" e separado do "active" ja existente em patients (que so marca inativo, sem sumir da lista) —
-- excluir tutor/pet precisa sumir de verdade das listagens, sem apagar a linha (preserva historico)
ALTER TABLE public.customers ADD COLUMN deleted boolean NOT NULL DEFAULT false;
ALTER TABLE public.patients ADD COLUMN deleted boolean NOT NULL DEFAULT false;

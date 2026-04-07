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


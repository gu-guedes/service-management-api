--liquibase formatted sql

--changeset seu-nome:1.1
CREATE TABLE public.app_user (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username character varying NOT NULL,
    password_hash character varying NOT NULL,
    active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);

--changeset seu-nome:1.2
ALTER TABLE public.app_user
    ADD CONSTRAINT app_user_username_key UNIQUE (username);

--changeset seu-nome:1.3
CREATE TABLE public.customers (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying NOT NULL,
    email character varying,
    phone character varying,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);

--changeset seu-nome:1.4
ALTER TABLE public.customers
    ADD CONSTRAINT customers_email_key UNIQUE (email);

--changeset seu-nome:1.5
CREATE TABLE public.services (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying NOT NULL,
    description text,
    base_price numeric(10,2),
    active boolean DEFAULT true,
    created_at timestamp with time zone DEFAULT now() NOT NULL
);

--changeset seu-nome:1.6
CREATE TABLE public.service_orders (
    id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id bigint NOT NULL,
    service_id bigint NOT NULL,
    created_by bigint,
    status character varying NOT NULL,
    service_date timestamp with time zone NOT NULL,
    notes text,
    total_price numeric(10,2),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    CONSTRAINT service_orders_status_check CHECK (
        (status)::text = ANY ((ARRAY['PENDING','IN_PROGRESS','COMPLETED','CANCELED'])::text[])
    )
);

--changeset seu-nome:1.7
ALTER TABLE public.service_orders
    ADD CONSTRAINT fk_service_orders_customer
    FOREIGN KEY (customer_id) REFERENCES public.customers(id) ON DELETE RESTRICT;

--changeset seu-nome:1.8
ALTER TABLE public.service_orders
    ADD CONSTRAINT fk_service_orders_service
    FOREIGN KEY (service_id) REFERENCES public.services(id) ON DELETE RESTRICT;

--changeset seu-nome:1.9
ALTER TABLE public.service_orders
    ADD CONSTRAINT fk_service_orders_user
    FOREIGN KEY (created_by) REFERENCES public.app_user(id) ON DELETE SET NULL;

--changeset gguedes:2.1
ALTER TABLE public.customers
    ADD COLUMN updated_at timestamp with time zone DEFAULT now() NOT NULL;

--changeset gguedes:3.2
ALTER TABLE public.app_user
    ADD COLUMN IF NOT EXISTS updated_at timestamp with time zone DEFAULT now() NOT NULL;

--changeset gguedes:3.3
ALTER TABLE public.app_user
    ADD COLUMN IF NOT EXISTS updated_at timestamp with time zone DEFAULT now() NOT NULL;

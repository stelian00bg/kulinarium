--
-- PostgreSQL database dump
--

-- Dumped from database version 14.1 (Debian 14.1-1.pgdg110+1)
-- Dumped by pg_dump version 14.1

-- Started on 2021-11-26 20:12:52

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3452 (class 1262 OID 16384)
-- Name: tukulinarium; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE tukulinarium WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.utf8';


\connect tukulinarium

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 16537)
-- Name: categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categories (
    id bigint NOT NULL,
    categories_name character varying NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 16536)
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 225
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- TOC entry 230 (class 1259 OID 16591)
-- Name: category_images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.category_images (
    category_id integer NOT NULL,
    image_id integer NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 16498)
-- Name: comments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.comments (
    id bigint NOT NULL,
    comments_content character varying NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 16497)
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 221
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.comments_id_seq OWNED BY public.comments.id;


--
-- TOC entry 219 (class 1259 OID 16471)
-- Name: images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.images (
    id bigint NOT NULL,
    images_link character varying DEFAULT 'image.default.png'::character varying NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 16470)
-- Name: images_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 218
-- Name: images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.images_id_seq OWNED BY public.images.id;


--
-- TOC entry 227 (class 1259 OID 16545)
-- Name: recipe_categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recipe_categories (
    recipe_id integer NOT NULL,
    category_id integer NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 16506)
-- Name: recipe_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recipe_comment (
    recipe_id integer NOT NULL,
    comment_id integer NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 16481)
-- Name: recipe_images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recipe_images (
    recipe_id integer NOT NULL,
    image_id integer DEFAULT 1 NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 16447)
-- Name: recipes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recipes (
    id bigint NOT NULL,
    recipes_cooking_time integer NOT NULL,
    recipes_name character varying NOT NULL,
    recipes_created_on timestamp with time zone NOT NULL,
    recipes_instructions character varying NOT NULL,
    recipes_ingredients character varying NOT NULL,
    recipes_approved boolean DEFAULT false NOT NULL
);


--
-- TOC entry 216 (class 1259 OID 16446)
-- Name: recipes_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.recipes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 216
-- Name: recipes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.recipes_id_seq OWNED BY public.recipes.id;


--
-- TOC entry 215 (class 1259 OID 16429)
-- Name: refreshtoken; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.refreshtoken (
    id bigint NOT NULL,
    refreshtoken_user_id integer NOT NULL,
    refreshtoken_token character varying NOT NULL,
    refreshtoken_expiry_date timestamp with time zone NOT NULL
);


--
-- TOC entry 214 (class 1259 OID 16428)
-- Name: refreshtokens_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.refreshtokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 214
-- Name: refreshtokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.refreshtokens_id_seq OWNED BY public.refreshtoken.id;


--
-- TOC entry 212 (class 1259 OID 16393)
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    roles_name character varying NOT NULL
);


--
-- TOC entry 211 (class 1259 OID 16392)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 211
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- TOC entry 224 (class 1259 OID 16521)
-- Name: user_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_comment (
    user_id integer NOT NULL,
    comment_id integer NOT NULL
);


--
-- TOC entry 228 (class 1259 OID 16560)
-- Name: user_images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_images (
    user_id integer NOT NULL,
    image_id integer NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 16576)
-- Name: user_recipes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_recipes (
    user_id integer NOT NULL,
    recipe_id integer NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 16401)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


--
-- TOC entry 210 (class 1259 OID 16386)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    users_name character varying(20) NOT NULL,
    users_username character varying(20) NOT NULL,
    users_password character varying(60) NOT NULL,
    users_email character varying(60) NOT NULL
);


--
-- TOC entry 209 (class 1259 OID 16385)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 209
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 3238 (class 2604 OID 16540)
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- TOC entry 3237 (class 2604 OID 16501)
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comments ALTER COLUMN id SET DEFAULT nextval('public.comments_id_seq'::regclass);


--
-- TOC entry 3234 (class 2604 OID 16474)
-- Name: images id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.images ALTER COLUMN id SET DEFAULT nextval('public.images_id_seq'::regclass);


--
-- TOC entry 3232 (class 2604 OID 16450)
-- Name: recipes id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipes ALTER COLUMN id SET DEFAULT nextval('public.recipes_id_seq'::regclass);


--
-- TOC entry 3231 (class 2604 OID 16432)
-- Name: refreshtoken id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refreshtoken ALTER COLUMN id SET DEFAULT nextval('public.refreshtokens_id_seq'::regclass);


--
-- TOC entry 3230 (class 2604 OID 16396)
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- TOC entry 3229 (class 2604 OID 16389)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3442 (class 0 OID 16537)
-- Dependencies: 226
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.categories (id, categories_name) FROM stdin;
1	basic
\.


--
-- TOC entry 3446 (class 0 OID 16591)
-- Dependencies: 230
-- Data for Name: category_images; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.category_images (category_id, image_id) FROM stdin;
1	1
\.


--
-- TOC entry 3438 (class 0 OID 16498)
-- Dependencies: 222
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.comments (id, comments_content) FROM stdin;
\.


--
-- TOC entry 3435 (class 0 OID 16471)
-- Dependencies: 219
-- Data for Name: images; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.images (id, images_link) FROM stdin;
1	image.default.png
\.


--
-- TOC entry 3443 (class 0 OID 16545)
-- Dependencies: 227
-- Data for Name: recipe_categories; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.recipe_categories (recipe_id, category_id) FROM stdin;
\.


--
-- TOC entry 3439 (class 0 OID 16506)
-- Dependencies: 223
-- Data for Name: recipe_comment; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.recipe_comment (recipe_id, comment_id) FROM stdin;
\.


--
-- TOC entry 3436 (class 0 OID 16481)
-- Dependencies: 220
-- Data for Name: recipe_images; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.recipe_images (recipe_id, image_id) FROM stdin;
\.


--
-- TOC entry 3433 (class 0 OID 16447)
-- Dependencies: 217
-- Data for Name: recipes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.recipes (id, recipes_cooking_time, recipes_name, recipes_created_on, recipes_instructions, recipes_ingredients, recipes_approved) FROM stdin;
\.


--
-- TOC entry 3431 (class 0 OID 16429)
-- Dependencies: 215
-- Data for Name: refreshtoken; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.refreshtoken (id, refreshtoken_user_id, refreshtoken_token, refreshtoken_expiry_date) FROM stdin;
28	1	1d1572d0-b108-475b-9b36-e2a3111bd732	2021-11-25 23:57:43.63266+00
29	1	55b8c4ec-f081-498c-b897-da319129e8ca	2021-11-26 18:11:22.896991+00
30	1	167a25a7-9d0d-40d7-abe1-0fabc03dbff2	2021-11-26 18:18:53.212029+00
\.


--
-- TOC entry 3428 (class 0 OID 16393)
-- Dependencies: 212
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.roles (id, roles_name) FROM stdin;
1	ROLE_USER
2	ROLE_ADMIN
\.


--
-- TOC entry 3440 (class 0 OID 16521)
-- Dependencies: 224
-- Data for Name: user_comment; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_comment (user_id, comment_id) FROM stdin;
\.


--
-- TOC entry 3444 (class 0 OID 16560)
-- Dependencies: 228
-- Data for Name: user_images; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_images (user_id, image_id) FROM stdin;
1	1
\.


--
-- TOC entry 3445 (class 0 OID 16576)
-- Dependencies: 229
-- Data for Name: user_recipes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_recipes (user_id, recipe_id) FROM stdin;
\.


--
-- TOC entry 3429 (class 0 OID 16401)
-- Dependencies: 213
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_roles (user_id, role_id) FROM stdin;
1	1
1	2
\.


--
-- TOC entry 3426 (class 0 OID 16386)
-- Dependencies: 210
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.users (id, users_name, users_username, users_password, users_email) FROM stdin;
1	System Admin	superuser	$2a$10$/0XbMxZwu2otmswMptVOz.QCKvSHhnNYrMWw6F4MY8WU2vBYt5bcu	superuser@tukulinarium.com
\.


--
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 225
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.categories_id_seq', 49, true);


--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 221
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.comments_id_seq', 4, true);


--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 218
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.images_id_seq', 76, true);


--
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 216
-- Name: recipes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.recipes_id_seq', 34, true);


--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 214
-- Name: refreshtokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.refreshtokens_id_seq', 30, true);


--
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 211
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.roles_id_seq', 2, true);


--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 209
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_id_seq', 10, true);


--
-- TOC entry 3260 (class 2606 OID 16544)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- TOC entry 3268 (class 2606 OID 16595)
-- Name: category_images category_images_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category_images
    ADD CONSTRAINT category_images_pkey PRIMARY KEY (category_id, image_id);


--
-- TOC entry 3254 (class 2606 OID 16505)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 3250 (class 2606 OID 16478)
-- Name: images images_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_pkey PRIMARY KEY (id);


--
-- TOC entry 3262 (class 2606 OID 16549)
-- Name: recipe_categories recipe_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_categories
    ADD CONSTRAINT recipe_categories_pkey PRIMARY KEY (recipe_id, category_id);


--
-- TOC entry 3256 (class 2606 OID 16510)
-- Name: recipe_comment recipe_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_comment
    ADD CONSTRAINT recipe_comment_pkey PRIMARY KEY (recipe_id, comment_id);


--
-- TOC entry 3252 (class 2606 OID 16485)
-- Name: recipe_images recipe_images_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_images
    ADD CONSTRAINT recipe_images_pkey PRIMARY KEY (recipe_id, image_id);


--
-- TOC entry 3248 (class 2606 OID 16454)
-- Name: recipes recipes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipes
    ADD CONSTRAINT recipes_pkey PRIMARY KEY (id);


--
-- TOC entry 3246 (class 2606 OID 16436)
-- Name: refreshtoken refreshtoken_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refreshtoken
    ADD CONSTRAINT refreshtoken_pkey PRIMARY KEY (id);


--
-- TOC entry 3242 (class 2606 OID 16400)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 16525)
-- Name: user_comment user_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_comment
    ADD CONSTRAINT user_comment_pkey PRIMARY KEY (user_id, comment_id);


--
-- TOC entry 3264 (class 2606 OID 16564)
-- Name: user_images user_images_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_images
    ADD CONSTRAINT user_images_pkey PRIMARY KEY (user_id, image_id);


--
-- TOC entry 3266 (class 2606 OID 16580)
-- Name: user_recipes user_recipes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_recipes
    ADD CONSTRAINT user_recipes_pkey PRIMARY KEY (user_id, recipe_id);


--
-- TOC entry 3244 (class 2606 OID 16413)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 3240 (class 2606 OID 16391)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3284 (class 2606 OID 16596)
-- Name: category_images fk_category_image_category; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category_images
    ADD CONSTRAINT fk_category_image_category FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- TOC entry 3285 (class 2606 OID 16601)
-- Name: category_images fk_category_image_image; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category_images
    ADD CONSTRAINT fk_category_image_image FOREIGN KEY (image_id) REFERENCES public.images(id);


--
-- TOC entry 3279 (class 2606 OID 16555)
-- Name: recipe_categories fk_recipe_categories_category; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_categories
    ADD CONSTRAINT fk_recipe_categories_category FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- TOC entry 3278 (class 2606 OID 16550)
-- Name: recipe_categories fk_recipe_categories_recipe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_categories
    ADD CONSTRAINT fk_recipe_categories_recipe FOREIGN KEY (recipe_id) REFERENCES public.recipes(id);


--
-- TOC entry 3275 (class 2606 OID 16516)
-- Name: recipe_comment fk_recipe_comment_comment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_comment
    ADD CONSTRAINT fk_recipe_comment_comment FOREIGN KEY (comment_id) REFERENCES public.comments(id);


--
-- TOC entry 3274 (class 2606 OID 16511)
-- Name: recipe_comment fk_recipe_comment_recipe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_comment
    ADD CONSTRAINT fk_recipe_comment_recipe FOREIGN KEY (recipe_id) REFERENCES public.recipes(id);


--
-- TOC entry 3273 (class 2606 OID 16491)
-- Name: recipe_images fk_recipe_images_images; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_images
    ADD CONSTRAINT fk_recipe_images_images FOREIGN KEY (image_id) REFERENCES public.images(id);


--
-- TOC entry 3272 (class 2606 OID 16486)
-- Name: recipe_images fk_recipe_images_recipe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recipe_images
    ADD CONSTRAINT fk_recipe_images_recipe FOREIGN KEY (recipe_id) REFERENCES public.recipes(id);


--
-- TOC entry 3271 (class 2606 OID 16437)
-- Name: refreshtoken fk_refreshtoken_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refreshtoken
    ADD CONSTRAINT fk_refreshtoken_user FOREIGN KEY (refreshtoken_user_id) REFERENCES public.users(id);


--
-- TOC entry 3277 (class 2606 OID 16531)
-- Name: user_comment fk_user_comment_comment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_comment
    ADD CONSTRAINT fk_user_comment_comment FOREIGN KEY (comment_id) REFERENCES public.comments(id);


--
-- TOC entry 3276 (class 2606 OID 16526)
-- Name: user_comment fk_user_comment_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_comment
    ADD CONSTRAINT fk_user_comment_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3281 (class 2606 OID 16570)
-- Name: user_images fk_user_images_image; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_images
    ADD CONSTRAINT fk_user_images_image FOREIGN KEY (image_id) REFERENCES public.images(id);


--
-- TOC entry 3280 (class 2606 OID 16565)
-- Name: user_images fk_user_images_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_images
    ADD CONSTRAINT fk_user_images_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3283 (class 2606 OID 16586)
-- Name: user_recipes fk_user_recipes_recipe; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_recipes
    ADD CONSTRAINT fk_user_recipes_recipe FOREIGN KEY (recipe_id) REFERENCES public.recipes(id);


--
-- TOC entry 3282 (class 2606 OID 16581)
-- Name: user_recipes fk_user_recipes_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_recipes
    ADD CONSTRAINT fk_user_recipes_user FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3270 (class 2606 OID 16423)
-- Name: user_roles fk_user_role_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES public.roles(id) NOT VALID;


--
-- TOC entry 3269 (class 2606 OID 16418)
-- Name: user_roles fk_user_role_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;


-- Completed on 2021-11-26 20:12:52

--
-- PostgreSQL database dump complete
--


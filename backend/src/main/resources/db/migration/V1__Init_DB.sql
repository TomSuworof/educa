create table if not exists t_password_reset_request
(
    id       uuid         not null constraint t_password_reset_request_pkey primary key,
    created  timestamp,
    username varchar(255) not null
);

create table if not exists t_role
(
    id   bigint       not null constraint t_role_pkey primary key,
    name varchar(255) not null
);

insert into t_role (id, name) values
    (0, 'ROLE_BLOCKED'),
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_MODERATOR')
on conflict (id) do nothing;

create table if not exists t_user
(
    id       uuid         not null constraint t_user_pkey primary key,
    username varchar(255) not null,
    email    varchar(255) not null,
    bio      varchar(255),
    password varchar(255) not null,
    avatar   text
);

create table if not exists t_article
(
    dtype            varchar(31),
    id               uuid                     not null constraint t_article_pkey primary key,
    title            varchar(255)             not null,
    custom_url       varchar(255)             not null,
    summary          text                     not null,
    content          text                     not null,
    solution         text,
    publication_date timestamp with time zone not null,
    state            integer                  not null,
    author_id        uuid                     not null constraint references_to_not_null_user_id references t_user not null
);

create table if not exists t_question
(
    id          uuid          not null constraint t_question_pk primary key,
    answer      varchar(255),
    hint        varchar(255),
    remark      varchar(255),
    exercise_id uuid          not null constraint references_to_not_null_exercise_id references t_article not null
);

create table if not exists t_users_roles
(
    user_id  uuid  not null constraint references_to_not_null_user references t_user not null,
    role_id bigint not null constraint references_to_not_null_role references t_role not null,
    constraint t_user_roles_pkey primary key (user_id, role_id)
);

create table if not exists t_tag
(
    id   uuid         not null primary key,
    name varchar(255) not null
);

create table if not exists t_articles_tags
(
    article_id uuid not null constraint references_to_not_null_article_id references t_article not null,
    tag_id     uuid not null constraint references_to_not_null_tag_id     references t_tag     not null
);
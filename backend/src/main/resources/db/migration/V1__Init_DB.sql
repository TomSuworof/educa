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
    (2, 'ROLE_USER')
on conflict(id) do update set id = excluded.id, name = excluded.name;

create table if not exists t_user
(
    id       uuid         not null constraint t_user_pkey primary key,
    username varchar(255) not null,
    email    varchar(255) not null,
    bio      varchar(255),
    password varchar(255) not null,
    avatar   text
);

create table if not exists t_exercise
(
    id               uuid                     not null constraint t_exercise_pkey primary key,
    title            varchar(255)             not null,
    content          text                     not null,
    solution         text                     not null,
    publication_date timestamp with time zone not null,
    state            integer                  not null
);

create table if not exists t_users_roles
(
    user_id  uuid  not null constraint references_to_not_null_user references t_user not null,
    role_id bigint not null constraint references_to_not_null_role references t_role not null,
    constraint t_users_roles_pkey primary key (user_id, role_id)
);

create table if not exists t_tag
(
    id   uuid         not null primary key,
    name varchar(255) not null
);

create table if not exists t_articles_tags
(
    article_id uuid not null constraint references_to_not_null_article_id references t_exercise,
    tag_id    uuid  not null constraint references_to_not_null_tag_is references t_tag
);
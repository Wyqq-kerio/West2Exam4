create table t_file
(
    dbid              int auto_increment primary key,
    original_filename varchar(511) null,
    filename          varchar(511) null,
    status            int          null comment '状态：0未审核、1已审核',
    crete_time        varchar(31)  null comment '上传时间',
    path              text         null,
    create_id         int          null
);

create table t_user
(
    dbid  int auto_increment primary key,
    uname varchar(511) null,
    pwd   varchar(511) null,
    phone varchar(31)  null,
    email varchar(511) null
);



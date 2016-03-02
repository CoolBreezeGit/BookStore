#bookstore sql


create database bookstore charset=utf8;

use bookstore;

create user coolbreeze@'%' identified by '12345678';
grant all on bookstore.* to coolbreeze@'%';


create table admins(
aid int primary key,
adminname varchar(50) not null,
password varchar(50) not null
);


insert into admins values(1,"coolbreeze","fengjian669988");


create table users(
        uid char(32) primary key,
        username varchar(50) not null,
        password varchar(50) not null,
        email varchar(60) not null,
        activationcode char(64) not null,
        state boolean                
    );

create table category(
        cid char(32) primary key,
        cname varchar(50) not null
    );


create table books(
        bid char(32) primary key,
        bname varchar(60) not null,
        author varchar(50) not null,
        price decimal(6,1) not null,
        image varchar(200) not null,    /*vchar类型,存放的是书籍图片的路径！*/
        cid char(32) not null,
        constraint fk_books_category foreign key(cid) references category(cid)    /*book外键cid关联category主键cid*/
    );


create table orders(
        oid char(32) primary key,
        ordertime datetime,
        total decimal(10,1),
        state smallint(1),    /*订单状态：未付款，已付款未发货，已发货未确认收货，订单结束*/   
        uid char(32),    /*订单主人*/
        address varchar(100),    /*收货人地址*/
        constraint fk_orders_users foreign key(uid) references users(uid)    /*orders外键uid关联users主键uid*/
    );


create table orderitems(
        iid char(32) primary key,
        count int,     /*图书数目*/
        subtotal decimal(8,1),        /*订单条目的小计*/
        bid char(32),
        oid char(32),
        constraint fk_orderitems_books foreign key(bid) references books(bid),
        constraint fk_orderitems_orders foreign key(oid) references orders(oid)
    );

#建立一些图书分类
insert  into `category`(`cid`,`cname`) values ('c1','Java SE');
insert  into `category`(`cid`,`cname`) values ('c2','Java EE');
insert  into `category`(`cid`,`cname`) values ('c3','Javascript');
#导入一些图书
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b1','Java编程思想（第4版）','75.60','qdmmy6','books_img/9317290-1_l.jpg','c1');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b2','Java核心技术卷1','68.50','qdmmy6','books_img/20285763-1_l.jpg','c1');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b3','Java就业培训教程','39.90','张孝祥','books_img/8758723-1_l.jpg','c1');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b4','Head First java','47.50','（美）塞若','books_img/9265169-1_l.jpg','c1');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b5','JavaWeb开发详解','83.30','孙鑫','books_img/22788412-1_l.jpg','c2');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b6','Struts2深入详解','63.20','孙鑫','books_img/20385925-1_l.jpg','c2');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b7','精通Hibernate','30.00','孙卫琴','books_img/8991366-1_l.jpg','c2');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b8','精通Spring2.x','63.20','陈华雄','books_img/20029394-1_l.jpg','c2');
insert  into `books`(`bid`,`bname`,`price`,`author`,`image`,`cid` ) values ('b9','Javascript权威指南','93.60','（美）弗兰纳根','books_img/22722790-1_l.jpg','c3');


#为books表格记录添加del属性，默认为false
alter table books add del boolean;
update books set del=false;

drop table product if exists;

create table product (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	price DECIMAL(20,2) NOT NULL
);
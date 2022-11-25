drop table product if exists;
drop table discount if exists;
drop table basketItem if exists;
drop table customerOrder if exists;

create table product (
	id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	price DECIMAL(20,2) NOT NULL
);

create table basketItem (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    product_id INT NOT NULL,
    customer_id INT NOT NULL,
    quantity INT NOT NULL
);

create table discount (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    product_id INT NOT NULL,
    code VARCHAR(50) NOT NULL
);

create table customerOrder (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    customer_id INT NOT NULL,
    total_cost DECIMAL(20,2) NOT NULL
);
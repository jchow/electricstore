drop table product if exists;
drop table discount if exists;
drop table basketItem if exists;

create table product (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	price DECIMAL(20,2) NOT NULL
);

create table basketItem (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    productId INT NOT NULL,
    customerId INT NOT NULL,
    quantity INT NOT NULL
);

create table discount (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    productId INT NOT NULL,
    percentage DECIMAL(20,2) NOT NULL
);
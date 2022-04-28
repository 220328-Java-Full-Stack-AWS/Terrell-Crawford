drop table if exists ers_reimbursement;
drop table if exists ers_reimbursement_status;
drop table if exists ers_reimbursement_type;
drop table if exists ers_users;
drop table if exists ers_user_roles;

create table ers_reimbursement_status(
	reimb_status_id SERIAL primary key,
	reimb_status VARCHAR(10) not NULL
);

create table ers_reimbursement_type(
	reimb_type_id SERIAL primary key,
	reimb_type VARCHAR(10) not NULL
); 

create table ers_user_roles(
	ers_user_role_id SERIAL primary key,
	user_role VARCHAR(15) not NULL
);

create table ers_users(
	ers_users_id SERIAL primary key,
	ers_username VARCHAR(50) unique ,
	ers_password VARCHAR(50) ,
	user_first_name VARCHAR(100),
	user_last_name VARCHAR(100),
	user_email VARCHAR(150) unique,
	user_role_id INT,
	CONSTRAINT Role_ID_f_key FOREIGN KEY (user_role_id) REFERENCES ers_user_roles(ers_user_role_id) ON DELETE CASCADE
);

create table ers_reimbursement(
	reimb_id SERIAL primary key,
	reimb_ammount numeric not NULL,
	reimb_submitted timestamp not NULL,
	reimb_resolved timestamp,
	reimb_description VARCHAR(350),
	reimb_receipt bytea,
	reimb_author INT not null references ers_users ON DELETE CASCADE,
	reimb_resolver INT  REFERENCES ers_users,
	reimb_status_id SERIAL REFERENCES ers_reimbursement_status,
	reimb_type_id SERIAL,
    CONSTRAINT REIMB_TYPE_f_key FOREIGN KEY (reimb_type_id) REFERENCES ers_reimbursement_type(reimb_type_id) );

INSERT INTO ers_user_roles (user_role) VALUES ('Finance Manager');
INSERT INTO ers_users (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES ('Admin1', '123', 'Ad', 'Min', 'admin1@email.com', 1);

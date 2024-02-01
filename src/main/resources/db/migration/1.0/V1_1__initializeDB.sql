CREATE TABLE users (
  user_id BIGINT NOT NULL,
   social_id VARCHAR(255) NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NULL,
   is_email_valid BIT(1) NOT NULL,
   `role` VARCHAR(255) NULL,
   CONSTRAINT pk_users PRIMARY KEY (user_id)
);

ALTER TABLE users ADD CONSTRAINT email UNIQUE (email);

ALTER TABLE users ADD CONSTRAINT social_id UNIQUE (social_id);

CREATE TABLE user_generator (
  next_val BIGINT NULL
);
INSERT INTO user_generator (next_val) VALUES (1);




CREATE TABLE confirmation_token (
  id BIGINT NOT NULL,
   token VARCHAR(255) NULL,
   created_at datetime NULL,
   expires_at datetime NULL,
   confirmed_at datetime NULL,
   user BIGINT NULL,
   CONSTRAINT pk_confirmation_token PRIMARY KEY (id)
);

ALTER TABLE confirmation_token ADD CONSTRAINT FK_CONFIRMATION_TOKEN_ON_USER FOREIGN KEY (user) REFERENCES users (user_id);

CREATE TABLE confirmation_token_seq (
  next_val BIGINT NULL
);
INSERT INTO confirmation_token_seq (next_val) VALUES (1);


CREATE TABLE password_reset (
  id BIGINT NOT NULL,
   token VARCHAR(255) NULL,
   created_at datetime NULL,
   expires_at datetime NULL,
   user BIGINT NULL,
   CONSTRAINT pk_password_reset PRIMARY KEY (id)
);

ALTER TABLE password_reset ADD CONSTRAINT FK_PASSWORD_RESET_ON_USER FOREIGN KEY (user) REFERENCES users (user_id);

CREATE TABLE password_reset_token_generator (
  next_val BIGINT NULL
);
INSERT INTO password_reset_token_generator (next_val) VALUES (1);


CREATE TABLE course (
  course_id BIGINT NOT NULL,
   name VARCHAR(255) NULL,
   instructor VARCHAR(255) NULL,
   duration VARCHAR(255) NULL,
   date VARCHAR(255) NULL,
   time VARCHAR(255) NULL,
   location VARCHAR(255) NULL,
   price DOUBLE NOT NULL,
   `description` VARCHAR(3000) NULL,
   category VARCHAR(255) NULL,
   image_url VARCHAR(255) NULL,
   CONSTRAINT pk_course PRIMARY KEY (course_id)
);
CREATE TABLE course_seq (
  next_val BIGINT NULL
);
INSERT INTO course_seq (next_val) VALUES (1);


CREATE TABLE review (
  id BIGINT NOT NULL,
   course BIGINT NULL,
   user BIGINT NULL,
   comment VARCHAR(255) NULL,
   rating INT NOT NULL,
   CONSTRAINT pk_review PRIMARY KEY (id)
);

ALTER TABLE review ADD CONSTRAINT FK_REVIEW_ON_COURSE FOREIGN KEY (course) REFERENCES course (course_id);

ALTER TABLE review ADD CONSTRAINT FK_REVIEW_ON_USER FOREIGN KEY (user) REFERENCES users (user_id);
CREATE TABLE review_seq (
  next_val BIGINT NULL
);
INSERT INTO review_seq (next_val) VALUES (1);


CREATE TABLE booking (
  id BIGINT NOT NULL,
   user BIGINT NULL,
   course BIGINT NULL,
   booking_date_time datetime NULL,
   total_cost DOUBLE NULL,
   CONSTRAINT pk_booking PRIMARY KEY (id)
);

ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_ON_COURSE FOREIGN KEY (course) REFERENCES course (course_id);

ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_ON_USER FOREIGN KEY (user) REFERENCES users (user_id);
CREATE TABLE booking_seq (
  next_val BIGINT NULL
);
INSERT INTO booking_seq (next_val) VALUES (1);
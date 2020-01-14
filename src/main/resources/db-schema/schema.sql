create table open_question (
   id int not null primary key,
   statement varchar(250) not null,
   answer varchar(500) not null
);

create table true_false_question (
   id int not null primary key,
   initial_phrase varchar(250) not null,
   answer boolean not null
);

create table multiple_choice_question (
   id int not null primary key,
   initial_phrase varchar(250) not null
);

create table choice (
   id int not null primary key,
   multiple_choice_question_id int not null,
   choice varchar(250) not null,
   foreign key (multiple_choice_question_id) references multiple_choice_question(id)
);

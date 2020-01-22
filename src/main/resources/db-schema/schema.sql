drop table if exists open_question;
drop table if exists true_false_question;
drop table if exists choice;
drop table if exists multiple_choice_question;

create table open_question (
   id bigint identity,
   statement varchar(250) not null,
   answer varchar(500) not null
);

create table true_false_question (
   id bigint identity,
   initial_phrase varchar(250) not null,
   answer boolean not null
);

create table multiple_choice_question (
   id bigint identity,
   initial_phrase varchar(250) not null
);

create table choice (
   id bigint identity,
   multiple_choice_question_id int not null,
   choice varchar(250) not null,
   is_correct_answer boolean not null,
   foreign key (multiple_choice_question_id) references multiple_choice_question(id)
);

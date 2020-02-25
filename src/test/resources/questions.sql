insert into open_question (statement, answer) values ('how are you?', 'I am well');
insert into open_question (statement, answer) values ('where are you?', 'hidden');
insert into true_false_question (initial_phrase, answer) values ('you are well.', true);
insert into multiple_choice_question (initial_phrase) values ('how old are you?');
set @m = current value for question_seq;
insert into choice values (1, @m, '25 years old', true);
insert into choice values (2, @m, '26 years old', false);
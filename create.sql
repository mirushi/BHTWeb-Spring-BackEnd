create sequence activity_sequence start with 1 increment by 50;
create sequence announcement_sequence start with 1 increment by 3;
create sequence doc_category_sequence start with 1 increment by 3;
create sequence doc_comment_report_sequence start with 1 increment by 50;
create sequence doc_comment_sequence start with 1 increment by 50;
create sequence doc_download_sequence start with 1 increment by 50;
create sequence doc_report_sequence start with 1 increment by 50;
create sequence doc_sequence start with 1 increment by 50;
create sequence doc_view_sequence start with 1 increment by 50;
create sequence exercise_answer_sequence start with 1 increment by 50;
create sequence exercise_attempt_sequence start with 1 increment by 50;
create sequence exercise_category_sequence start with 1 increment by 50;
create sequence exercise_comment_report_sequence start with 1 increment by 50;
create sequence exercise_comment_sequence start with 1 increment by 50;
create sequence exercise_question_sequence start with 1 increment by 50;
create sequence exercise_report_sequence start with 1 increment by 50;
create sequence exercise_sequence start with 1 increment by 50;
create sequence exercise_topic_sequence start with 1 increment by 50;
create sequence notification_sequence start with 1 increment by 50;
create sequence post_category_sequence start with 1 increment by 3;
create sequence post_comment_report_sequence start with 1 increment by 50;
create sequence post_comment_sequence start with 1 increment by 50;
create sequence post_report_sequence start with 1 increment by 50;
create sequence post_sequence start with 1 increment by 50;
create sequence post_view_sequence start with 1 increment by 50;
create sequence report_reason_sequence start with 1 increment by 50;
create sequence subject_faculty_sequence start with 1 increment by 50;
create sequence subject_group_sequence start with 1 increment by 50;
create sequence subject_sequence start with 1 increment by 3;
create sequence tag_sequence start with 1 increment by 50;
create table activity (id bigint not null, activity_dtm timestamp not null, id_item bigint, name varchar(255) not null, type smallint, version smallint not null, actor_active_id uuid, actor_passive_id uuid, primary key (id));
create table announcement (id bigint not null, activated_status boolean not null, content varchar(255) not null, last_updated timestamp not null, version smallint not null, primary key (id));
create table doc (id bigint not null, deleted_date timestamp, description varchar(255) not null, doc_state smallint, imageurl varchar(255) not null, last_updated_dtm timestamp not null, publish_dtm timestamp not null, submit_dtm timestamp not null, title varchar(255) not null, version smallint not null, author_id uuid not null, category_id bigint not null, last_edited_user_id uuid, subject_id bigint not null, primary key (id));
create table doc_category (id bigint not null, name varchar(255) not null, version smallint not null, primary key (id));
create table doc_comment (id bigint not null, content text not null, deleted_date timestamp, last_edited_dtm timestamp not null, submit_dtm timestamp not null, version smallint not null, author_id uuid, doc_id bigint, parent_comment_id bigint, primary key (id));
create table doc_comment_report_reason (doc_comment_report_id bigint not null, user_id uuid not null, report_reason_id bigint not null, primary key (report_reason_id, doc_comment_report_id, user_id));
create table doc_doc_tag (doc_id bigint not null, tag_id bigint not null, primary key (doc_id, tag_id));
create table doc_file_upload (id uuid not null, created_dtm timestamp not null, downloadurl varchar(2048) not null, file_name varchar(255) not null, file_size bigint not null, rank integer not null, remoteid varchar(255) not null, thumbnailurl varchar(2048) not null, doc_id bigint, uploader_id uuid not null, primary key (id));
create table doc_report_reason (doc_report_id bigint not null, user_id uuid not null, report_reason_id bigint not null, primary key (report_reason_id, doc_report_id, user_id));
create table doc_comment_report (id bigint not null, action_taken smallint, report_time timestamp, resolved_note varchar(255), resolved_time timestamp, doc_comment_id bigint, resolved_by uuid, primary key (id));
create table doc_download (id bigint not null, ip_address varchar(255), viewed_at timestamp not null, doc_file_upload_id uuid not null, user_id uuid, primary key (id));
create table doc_report (id bigint not null, action_type smallint, report_time timestamp, resolved_note varchar(255), resolved_time timestamp, doc_id bigint, resolved_by uuid, primary key (id));
create table doc_view (id bigint not null, ip_address varchar(255), viewed_at timestamp not null, doc_id bigint not null, user_id uuid, primary key (id));
create table exercise (id bigint not null, description varchar(255), publish_dtm timestamp not null, rank integer, submit_dtm timestamp not null, suggested_duration integer, title varchar(255), version smallint not null, author_id uuid, category_id bigint, topic_id bigint, primary key (id));
create table exercise_answer (id bigint not null, content text not null, is_correct boolean not null, rank integer not null, version smallint not null, question_id bigint, primary key (id));
create table exercise_attempt (id bigint not null, attempt_dtm timestamp, correct_answered_questions integer, version smallint not null, exercise_id bigint, user_id uuid, primary key (id));
create table exercise_category (id bigint not null, name varchar(255), version smallint not null, primary key (id));
create table exercise_comment (id bigint not null, content text not null, deleted_date timestamp, last_edited_dtm timestamp not null, submit_dtm timestamp not null, version smallint not null, author_id uuid, exercise_id bigint, parent_comment_id bigint, primary key (id));
create table exercise_comment_report_reason (exercise_comment_report_id bigint not null, user_id uuid not null, report_reason_id bigint not null, primary key (report_reason_id, exercise_comment_report_id, user_id));
create table exercise_exercise_tag (exercise_id bigint not null, tag_id bigint not null, primary key (exercise_id, tag_id));
create table exercise_note (notes varchar(255), version smallint not null, user_id uuid not null, exercise_id bigint not null, primary key (exercise_id, user_id));
create table exercise_question (id bigint not null, content varchar(255), explanation varchar(255), rank integer, version smallint not null, exercise_id bigint, primary key (id));
create table exercise_report (id bigint not null, action_taken smallint, report_time timestamp, resolved_note varchar(255), resolved_time timestamp, exercise_id bigint, resolved_by uuid, primary key (id));
create table exercise_report_reason (exercise_report_id bigint not null, user_id uuid not null, report_reason_id bigint not null, primary key (report_reason_id, exercise_report_id, user_id));
create table exercise_topic (id bigint not null, name varchar(255), rank integer, version smallint not null, subject_id bigint, primary key (id));
create table exercise_comment_report (id bigint not null, action_taken smallint, report_time timestamp, resolved_note varchar(255), resolved_time timestamp, exercise_comment_id bigint, resolved_by uuid, primary key (id));
create table highlight_post (highlight_dtm timestamp not null, rank integer, post_id bigint not null, highlighted_by_user uuid, primary key (post_id));
create table notification (id bigint not null, action_dtm timestamp not null, is_read boolean not null, score bigint not null, title varchar(255) not null, type smallint, actor_id uuid, actor_passive_id uuid, primary key (id));
create table post (id bigint not null, admin_feedback text, content text not null, content_plain_text text not null, deleted_date timestamp, imageurl varchar(2048), last_updated_dtm timestamp, post_state smallint, publish_dtm timestamp not null, reading_time integer not null, submit_dtm timestamp not null, summary varchar(255) not null, title varchar(128) not null, version smallint not null, author_id uuid, category_id bigint, deleted_by_id uuid, last_updated_by_id uuid, primary key (id));
create table post_category (id bigint not null, name varchar(25) not null, version smallint not null, primary key (id));
create table post_comment (id bigint not null, content text not null, deleted_date timestamp, last_edited_dtm timestamp not null, submit_dtm timestamp not null, version smallint not null, author_id uuid, parent_comment_id bigint, post_id bigint, primary key (id));
create table post_comment_report_reason (post_comment_report_id bigint not null, user_id uuid not null, report_reason_id bigint not null, primary key (report_reason_id, post_comment_report_id, user_id));
create table post_post_tag (post_id bigint not null, tag_id bigint not null, primary key (post_id, tag_id));
create table post_report (id bigint not null, action_taken smallint, report_time timestamp, resolved_note varchar(255), resolved_time timestamp, post_id bigint, resolved_by uuid, primary key (id));
create table post_report_reason (post_report_id bigint not null, user_id uuid not null, report_reason_id bigint not null, primary key (report_reason_id, post_report_id, user_id));
create table post_comment_report (id bigint not null, action_taken smallint, report_time timestamp, resolved_note varchar(255), resolved_time timestamp, post_comment_id bigint, resolved_by uuid, primary key (id));
create table post_view (id bigint not null, ip_address varchar(255), viewed_at timestamp not null, post_id bigint not null, user_id uuid, primary key (id));
create table report_reason (id bigint not null, reason varchar(255), primary key (id));
create table subject (id bigint not null, description varchar(4096) not null, imageurl varchar(255), name varchar(255) not null, version smallint not null, subject_faculty bigint, subject_group bigint, primary key (id));
create table subject_faculty (id bigint not null, name varchar(255), version smallint not null, primary key (id));
create table subject_group (id bigint not null, description varchar(255), name varchar(255), version smallint not null, primary key (id));
create table tag (id bigint not null, content varchar(255) not null, version smallint not null, primary key (id));
create table user_doc_comment_like (user_id uuid not null, doc_comment_id bigint not null, primary key (doc_comment_id, user_id));
create table user_doc_reaction (doc_reaction_type smallint, last_reaction_dtm timestamp not null, doc_id bigint not null, user_id uuid not null, primary key (doc_id, user_id));
create table user_doc_save (doc_id bigint not null, user_id uuid not null, primary key (doc_id, user_id));
create table user_exercise_comment_like (user_id uuid not null, exercise_comment_id bigint not null, primary key (exercise_comment_id, user_id));
create table user_post_comment_like (post_comment_id bigint not null, user_id uuid not null, primary key (post_comment_id, user_id));
create table user_post_like (post_id bigint not null, user_id uuid not null, primary key (post_id, user_id));
create table user_post_save (post_id bigint not null, user_id uuid not null, primary key (post_id, user_id));
create table user_website (id uuid not null, about_me varchar(255) not null, avatarurl varchar(2048) not null, display_name varchar(255) not null, email varchar(255) not null, name varchar(255) not null, reputation_score bigint not null, version smallint not null, primary key (id));
create table user_doc_comment_report (feedback varchar(255), user_id uuid not null, doc_comment_report_id bigint not null, primary key (doc_comment_report_id, user_id));
create table user_doc_report (feedback varchar(255), doc_report_id bigint not null, user_id uuid not null, primary key (doc_report_id, user_id));
create table user_exercise_comment_report (feedback varchar(255), exercise_comment_report_id bigint not null, user_id uuid not null, primary key (exercise_comment_report_id, user_id));
create table user_exercise_report (feedback varchar(255), user_id uuid not null, exercise_report_id bigint not null, primary key (exercise_report_id, user_id));
create table user_post_comment_report (feedback varchar(255), post_comment_report_id bigint not null, user_id uuid not null, primary key (post_comment_report_id, user_id));
create table user_post_report (feedback varchar(255), post_report_id bigint not null, user_id uuid not null, primary key (post_report_id, user_id));
alter table post_category add constraint UK_p19r05m7v1x2k890ad5pjmai2 unique (name);
alter table tag add constraint UK_lkyiy3gbkiiffxufqh5ud57w3 unique (content);
alter table user_website add constraint UK_1b9pj2yn74fklvhr3nm28ylt2 unique (name);
alter table activity add constraint FK58m2cl9v8wrvshk7vtagr5lo1 foreign key (actor_active_id) references user_website;
alter table activity add constraint FK7tpjq47ihfo7s5pv4s4h3pag2 foreign key (actor_passive_id) references user_website;
alter table doc add constraint FK9igcqmngp70797jnytlk4a9h foreign key (author_id) references user_website;
alter table doc add constraint FK8r9qsbr4nxqxuao46l4ut6agg foreign key (category_id) references doc_category;
alter table doc add constraint FKch227kx46smb2344hljiet70c foreign key (last_edited_user_id) references user_website;
alter table doc add constraint FK1t4eb607ise6547wa07loacuq foreign key (subject_id) references subject;
alter table doc_comment add constraint FK1ng48n84do26fn884i3ecw6hq foreign key (author_id) references user_website;
alter table doc_comment add constraint FKmihpia1i59b3r03s39ra0bnpo foreign key (doc_id) references doc;
alter table doc_comment add constraint FK3y2c336e8muxr45eveo8ligav foreign key (parent_comment_id) references doc_comment;
alter table doc_comment_report_reason add constraint FKd7ejwcij69hf6pdlp9s36h5ku foreign key (doc_comment_report_id, user_id) references user_doc_comment_report;
alter table doc_comment_report_reason add constraint FK6dxdeyyy5a1377cl9wlh5hhll foreign key (report_reason_id) references report_reason;
alter table doc_doc_tag add constraint FK53n0sx8h27yk6pwciusljvrp8 foreign key (tag_id) references tag;
alter table doc_doc_tag add constraint FK50epqm1s2f3u4ytg9fpyvqjpb foreign key (doc_id) references doc;
alter table doc_file_upload add constraint FK9tok8df8u942v9eeko3oc3mm5 foreign key (doc_id) references doc;
alter table doc_file_upload add constraint FKn8wxhwf6eqnkwgfukt2c2nprt foreign key (uploader_id) references user_website;
alter table doc_report_reason add constraint FKr9hlaxealwaecd4niwx26rllf foreign key (doc_report_id, user_id) references user_doc_report;
alter table doc_report_reason add constraint FKj4s94s8b0wqrnoje6xohtah1s foreign key (report_reason_id) references report_reason;
alter table doc_comment_report add constraint FKr3r6vhwkrjcp27kjtvvjryi4c foreign key (doc_comment_id) references doc_comment;
alter table doc_comment_report add constraint FKhjvnpwnuxbdg1udx666y9bbs4 foreign key (resolved_by) references user_website;
alter table doc_download add constraint FKer222ivtgtmcn006w01iciqjf foreign key (doc_file_upload_id) references doc_file_upload;
alter table doc_download add constraint FK3hkpvnirra68vyxg1l7dxhr0d foreign key (user_id) references user_website;
alter table doc_report add constraint FKebhhg6564wnrp1sq8me81sr75 foreign key (doc_id) references doc;
alter table doc_report add constraint FK7n7koib1mitu4q1f2gfd3tpp7 foreign key (resolved_by) references user_website;
alter table doc_view add constraint FKt4kt305qxqcbc3m5xa2b5pqd6 foreign key (doc_id) references doc;
alter table doc_view add constraint FKkdx230isqeh2opie92o55siig foreign key (user_id) references user_website;
alter table exercise add constraint FKboxmq62kbmxur7vpv73cubjgy foreign key (author_id) references user_website;
alter table exercise add constraint FKikld4mdsxq1qdnu4r0ukh0mkn foreign key (category_id) references exercise_category;
alter table exercise add constraint FKcq5lpnt05x4gn9c6lsxw9vj6s foreign key (topic_id) references exercise_topic;
alter table exercise_answer add constraint FK8h0id93wbuoxra9px86ajm6l3 foreign key (question_id) references exercise_question;
alter table exercise_attempt add constraint FKhciuk3646io1n2ue54obcqpj6 foreign key (exercise_id) references exercise;
alter table exercise_attempt add constraint FKcbd74g8klsr6pnak6nbeg0npt foreign key (user_id) references user_website;
alter table exercise_comment add constraint FK86au3u2rdjdsfljnk2etynah9 foreign key (author_id) references user_website;
alter table exercise_comment add constraint FKob4swhr795vru7g5n9nvql599 foreign key (exercise_id) references exercise;
alter table exercise_comment add constraint FKnftwdidfts07cb91h37vnss0o foreign key (parent_comment_id) references exercise_comment;
alter table exercise_comment_report_reason add constraint FKsavetskjyqg5cw1n4x61axyqa foreign key (exercise_comment_report_id, user_id) references user_exercise_comment_report;
alter table exercise_comment_report_reason add constraint FK88ytuewjhjr5wn1jgp15cc9ld foreign key (report_reason_id) references report_reason;
alter table exercise_exercise_tag add constraint FKmr4vealljl2xgu812aisw0ay6 foreign key (tag_id) references tag;
alter table exercise_exercise_tag add constraint FK7gysg68oiqq269n9clupkaakv foreign key (exercise_id) references exercise;
alter table exercise_note add constraint FKjg60divy7fplso8yj1gsmcl62 foreign key (user_id) references user_website;
alter table exercise_note add constraint FK5cfndvbl39ebijoitcwu9snyt foreign key (exercise_id) references exercise;
alter table exercise_question add constraint FKhavh6yhia0k6qvy7gmf4vdw30 foreign key (exercise_id) references exercise;
alter table exercise_report add constraint FKiiyk6619ov96lno0hghvu4w26 foreign key (exercise_id) references exercise;
alter table exercise_report add constraint FK3k47yavbu85hy3p82isme316w foreign key (resolved_by) references user_website;
alter table exercise_report_reason add constraint FKhg39hkpyaeyt71uivuc9tqmho foreign key (exercise_report_id, user_id) references user_exercise_report;
alter table exercise_report_reason add constraint FK35mt3wavocdvf1r7g270o9jx foreign key (report_reason_id) references report_reason;
alter table exercise_topic add constraint FKm4mi56bk99lwp61hka87ya5b6 foreign key (subject_id) references subject;
alter table exercise_comment_report add constraint FKfr0mxmtxwv5crc7rmpfihf1bv foreign key (exercise_comment_id) references exercise_comment;
alter table exercise_comment_report add constraint FKg8kkrawn2dasqrpd4hqtl1jyn foreign key (resolved_by) references user_website;
alter table highlight_post add constraint FKk7m0nk56wkge75xykq1ydjbvr foreign key (post_id) references post;
alter table highlight_post add constraint FKqq8ej2w6douhj0as8jucsbdhx foreign key (highlighted_by_user) references user_website;
alter table notification add constraint FKehqs76eu7387a0baupq91c30d foreign key (actor_id) references user_website;
alter table notification add constraint FKiwtcvx4jiuy14shge7inc7pic foreign key (actor_passive_id) references user_website;
alter table post add constraint FKgq5lv04u2f03m5df8as85xadg foreign key (author_id) references user_website;
alter table post add constraint FKi4fb9yu9ap3j0g42j0qja9b4a foreign key (category_id) references post_category;
alter table post add constraint FKw03lmjbdgp04ehafvw57clv3 foreign key (deleted_by_id) references user_website;
alter table post add constraint FKg8dftf8ct9bq1c62pxlof1o11 foreign key (last_updated_by_id) references user_website;
alter table post_comment add constraint FKqjo1y361e682grynbn1l03cyx foreign key (author_id) references user_website;
alter table post_comment add constraint FK67mdpjurvg1a2dc49f2u99wy5 foreign key (parent_comment_id) references post_comment;
alter table post_comment add constraint FKna4y825fdc5hw8aow65ijexm0 foreign key (post_id) references post;
alter table post_comment_report_reason add constraint FKtoehqln0i617k8jruj44wsiot foreign key (post_comment_report_id, user_id) references user_post_comment_report;
alter table post_comment_report_reason add constraint FK972fs28hjcjidrtba30t2pmf5 foreign key (report_reason_id) references report_reason;
alter table post_post_tag add constraint FK913n181qg2d9gsjq3tybdkhmr foreign key (tag_id) references tag;
alter table post_post_tag add constraint FK88sct8vcodss3wgni9uuj0x11 foreign key (post_id) references post;
alter table post_report add constraint FKeyehd7v09u9oxijrfvw1ufof foreign key (post_id) references post;
alter table post_report add constraint FKndygoq8y0v8ohdlwakpun7gqh foreign key (resolved_by) references user_website;
alter table post_report_reason add constraint FKnijno7r3v2iglvq4fmugi5j8v foreign key (post_report_id, user_id) references user_post_report;
alter table post_report_reason add constraint FK1agx7b4epwu8ronck0syk1bw8 foreign key (report_reason_id) references report_reason;
alter table post_comment_report add constraint FK6g6f1kont48mo5brljuy4yo68 foreign key (post_comment_id) references post_comment;
alter table post_comment_report add constraint FKlixltqycinkghtaw5txwwjaaf foreign key (resolved_by) references user_website;
alter table post_view add constraint FKaymjwcor4fcsxhgqgus7302a0 foreign key (post_id) references post;
alter table post_view add constraint FKdxjm2p7n4weorb2dx64ot48ac foreign key (user_id) references user_website;
alter table subject add constraint FK8iyxcyx2tmoptm577r6cd6k55 foreign key (subject_faculty) references subject_faculty;
alter table subject add constraint FKjpb9ygg0nc5mskfatd87pd52j foreign key (subject_group) references subject_group;
alter table user_doc_comment_like add constraint FKhbu8mwoh4ewbcwv18o6e5cihq foreign key (user_id) references user_website;
alter table user_doc_comment_like add constraint FKhoa7dpf1w49x872b9fhya4uh2 foreign key (doc_comment_id) references doc_comment;
alter table user_doc_reaction add constraint FK1ov06u2d2kjkf2v0c090c88i6 foreign key (doc_id) references doc;
alter table user_doc_reaction add constraint FK4lituo5j0o57u3ixul5vd94gx foreign key (user_id) references user_website;
alter table user_doc_save add constraint FK9up33osc0n6b4h5moqx25gu1c foreign key (doc_id) references doc;
alter table user_doc_save add constraint FKexq6fw9ygnq1waike9e7ajj1l foreign key (user_id) references user_website;
alter table user_exercise_comment_like add constraint FKe0frbe3cbp9l0oxq0tkvuf30y foreign key (user_id) references user_website;
alter table user_exercise_comment_like add constraint FKm2aj2p23algj22t9uudeg99fq foreign key (exercise_comment_id) references exercise_comment;
alter table user_post_comment_like add constraint FKbpym05j0hltf7mr30qt01qeso foreign key (post_comment_id) references post_comment;
alter table user_post_comment_like add constraint FK587ejqml522a0clj270mrhs7j foreign key (user_id) references user_website;
alter table user_post_like add constraint FK8kkugb4ofjhcvvda7tm1r4928 foreign key (post_id) references post;
alter table user_post_like add constraint FKctd1egu8ge4dgxah7abp6ijsj foreign key (user_id) references user_website;
alter table user_post_save add constraint FK4lrg16jynlfl9xi9oggm1m3i0 foreign key (post_id) references post;
alter table user_post_save add constraint FKl8ihuwk4c576v9orgpdqgo5os foreign key (user_id) references user_website;
alter table user_doc_comment_report add constraint FK2vigdebhof6e5489gc1ildfu9 foreign key (user_id) references user_website;
alter table user_doc_comment_report add constraint FKqomd4w5oon5jlyu6i3g01xsx7 foreign key (doc_comment_report_id) references doc_comment_report;
alter table user_doc_report add constraint FK2rkbmwmmj8bl25yv4dpmuer0s foreign key (doc_report_id) references doc_report;
alter table user_doc_report add constraint FKaw50jgbphrnbfg34v0f581k9j foreign key (user_id) references user_website;
alter table user_exercise_comment_report add constraint FKngfkw5gt7r8m6l2b821d08xiv foreign key (exercise_comment_report_id) references exercise_comment_report;
alter table user_exercise_comment_report add constraint FKjpqlba17ujs1vks68ujv8otyu foreign key (user_id) references user_website;
alter table user_exercise_report add constraint FK6ccqy5m1fctpyl95ltdjjyn1c foreign key (user_id) references user_website;
alter table user_exercise_report add constraint FKgjmvp5wwis4o93c5kqtlc36ph foreign key (exercise_report_id) references exercise_report;
alter table user_post_comment_report add constraint FKovcss7lbo64c7dtco8e84j6o8 foreign key (post_comment_report_id) references post_comment_report;
alter table user_post_comment_report add constraint FKbv0kfl7aou6g1m87o7gf42o8f foreign key (user_id) references user_website;
alter table user_post_report add constraint FKjxetbex9hrsypqiy9exnrqyrn foreign key (post_report_id) references post_report;
alter table user_post_report add constraint FKgh9d5kpnf42rnh5q6d39rr5wi foreign key (user_id) references user_website;

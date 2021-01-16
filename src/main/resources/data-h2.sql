SELECT 'Hello world';

INSERT INTO ANNOUNCEMENT (ID, CONTENT, ACTIVATED_STATUS, LAST_UPDATED, VERSION)
VALUES (announcement_sequence.NEXTVAL, 'Thong bao thu nhat - Server vua khai truong', true, '20201226', 0),
       (announcement_sequence.NEXTVAL, 'Thong bao - Qua tang giang sinh :v', true, '20201225', 0);

INSERT INTO USER_WEBSITE_ROLE (ID, NAME, VERSION)
VALUES (user_website_role_sequence.NEXTVAL, 'ADMIN', 0),
       (user_website_role_sequence.NEXTVAL, 'USER', 0);

INSERT INTO USER_WEBSITE (ID, NAME, DISPLAY_NAME, HASHED_PASSWORD, EMAIL, REPUTATION_SCORE, ROLE_ID, AVATARURL, BAN_STATUS, VERSION)
VALUES (user_website_sequence.NEXTVAL, 'alex', 'Alexa', 'sha512hashedpassword', '123@gmail.com', 100, (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME = 'ADMIN'), 'https://tophinhanhdep.com/wp-content/uploads/2017/07/avatar-dep-de-thuong.jpg', false, 0),
       (user_website_sequence.NEXTVAL, 'bran', 'Branxol Muns', 'sha512hashedpassword' , '456@gmail.com', 300, (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME = 'USER'), 'https://tophinhanhdep.com/wp-content/uploads/2017/07/avatar-facebook-dep.jpg', false, 0);

-- INSERT ACTIVITIES
WITH activities (ID, NAME, TYPE, ACTOR_ACTIVE_NAME, ACTOR_PASSIVE_NAME, ACTIVITY_DTM, ID_ITEM, VERSION) AS
         ( VALUES
        (activity_sequence.NEXTVAL ,'New Post Added', '1', 'bran', 'alex', '20201214', '1', 0),
        (activity_sequence.NEXTVAL ,'New Post Approved', '1', 'alex', 'bran', '20201215', '2', 0)
         )
INSERT INTO ACTIVITY (ID, NAME, TYPE, ACTOR_ACTIVE_ID, ACTOR_PASSIVE_ID, ACTIVITY_DTM, ID_ITEM, VERSION)
SELECT
    activities.ID, activities.NAME, activities.TYPE, active_user.ID, passive_user.ID, activities.ACTIVITY_DTM, activities.ID_ITEM, activities.VERSION
FROM
    activities JOIN USER_WEBSITE AS active_user
                    ON activities.ACTOR_ACTIVE_NAME = active_user.NAME
               JOIN USER_WEBSITE AS passive_user
                    ON activities.ACTOR_PASSIVE_NAME = passive_user.NAME;

INSERT INTO DOC_CATEGORY (ID, NAME, VERSION) VALUES
(doc_category_sequence.NEXTVAL, 'Doc Category 01', 0),
(doc_category_sequence.NEXTVAL, 'Doc Category 02', 0);

INSERT INTO DOC_SUBJECT(ID, NAME, VERSION) VALUES
(doc_subject_sequence.NEXTVAL, 'Doc Subject 01', 0),
(doc_subject_sequence.NEXTVAL, 'Doc Subject 02', 0);

WITH docs (ID, CREATED_DTM, DESCRIPTION, DOCURL, DOWNLOAD_COUNT, IMAGEURL, IS_APPROVED, IS_PENDING_USER_ACTION, IS_SOFT_DELETED, PUBLISHED_DTM, TITLE, VIEW_COUNT, AUTHOR_NAME, CATEGORY_NAME, SUBJECT_NAME, VERSION) AS
         ( VALUES
		(doc_sequence.NEXTVAL, '20200114', 'Doc 01 Description', 'http://google.com/docs01', 10, ' tinyurl.com/2fhx8t34', true, false, false, '20200115', 'Doc 01 Title', 34, 'bran', 'Doc Category 01', 'Doc Subject 02', 0),
		(doc_sequence.NEXTVAL, '20210114', 'Doc 02 Description', 'http://google.com/docs02', 100, ' tinyurl.com/2fhx8t34', true, false, false, '20210115', 'Doc 02 Title', 256, 'alex', 'Doc Category 02', 'Doc Subject 01', 0)
         )

INSERT INTO DOC (ID, CREATED_DTM, DESCRIPTION, DOCURL, DOWNLOAD_COUNT, IMAGEURL, IS_APPROVED, IS_PENDING_USER_ACTION, IS_SOFT_DELETED, PUBLISHED_DTM, TITLE, VIEW_COUNT, AUTHOR_ID, CATEGORY_ID, SUBJECT_ID, VERSION)
SELECT
    docs.ID, docs.CREATED_DTM, docs.DESCRIPTION, docs.DOCURL, docs.DOWNLOAD_COUNT, docs.IMAGEURL, docs.IS_APPROVED, docs.IS_PENDING_USER_ACTION, docs.IS_SOFT_DELETED, docs.PUBLISHED_DTM, docs.TITLE, docs.VIEW_COUNT, author.ID, category.ID, subject.ID, docs.VERSION
FROM
    docs JOIN USER_WEBSITE AS author
              ON docs.AUTHOR_NAME = author.NAME
         JOIN DOC_CATEGORY AS category
              ON docs.CATEGORY_NAME = category.NAME
         JOIN DOC_SUBJECT AS subject
              ON docs.SUBJECT_NAME = subject.NAME;

WITH userDocReactions (USER_NAME, DOC_TITLE, DOC_REACTION_TYPE) AS
         (
             VALUES
		('bran', 'Doc 01 Title', 0),
		('alex', 'Doc 01 Title', 1),
		('bran', 'Doc 02 Title', 1),
		('alex', 'Doc 02 Title', 0)
         )
INSERT INTO USER_DOC_REACTION (USER_ID, DOC_ID, DOC_REACTION_TYPE)
SELECT
    actor.ID, doc.ID, userDocReactions.DOC_REACTION_TYPE
FROM
    userDocReactions JOIN USER_WEBSITE AS actor
                          ON userDocReactions.USER_NAME = actor.NAME
                     JOIN DOC AS doc
                          ON userDocReactions.DOC_TITLE = doc.TITLE;
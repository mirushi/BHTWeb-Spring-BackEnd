SELECT 'Hello world';

INSERT INTO ANNOUNCEMENT (ID, CONTENT, ACTIVATED_STATUS, LAST_UPDATED, VERSION)
VALUES (announcement_sequence.NEXTVAL, 'Thong bao thu nhat - Server vua khai truong', true, '20201226', 0),
       (announcement_sequence.NEXTVAL, 'Thong bao - Qua tang giang sinh :v', true, '20201225', 0);

INSERT INTO USER_WEBSITE_ROLE (ID, NAME, VERSION)
VALUES (user_website_role_sequence.NEXTVAL, 'ADMIN', 0),
       (user_website_role_sequence.NEXTVAL, 'USER', 0);

INSERT INTO USER_WEBSITE (ID, NAME, DISPLAY_NAME, HASHED_PASSWORD, EMAIL, REPUTATION_SCORE, ROLE_ID, AVATARURL, BAN_STATUS, VERSION)
VALUES (user_website_sequence.NEXTVAL, 'alex', 'Alexa', 'sha512hashedpassword', '123@gmail.com', 100, (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME = 'ADMIN'), 'https://tophinhanhdep.com/wp-content/uploads/2017/07/avatar-dep-de-thuong.jpg', false, 0),
       (user_website_sequence.NEXTVAL, 'bran', 'Branxol Muns', 'sha512hashedpassword' , '456@gmail.com', 300, (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME = 'USER'), 'https://tophinhanhdep.com/wp-content/uploads/2017/07/avatar-facebook-dep.jpg', false, 0),
       (user_website_sequence.NEXTVAL, 'david', 'Branxol Muns', 'sha512hashedpassword' , '456@gmail.com', 300, (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME = 'USER'), 'https://tophinhanhdep.com/wp-content/uploads/2017/07/avatar-facebook-dep.jpg', false, 0);

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

WITH docs (ID, CREATED_DTM, DESCRIPTION, DOCURL, DOWNLOAD_COUNT, IMAGEURL, DOC_STATE, LAST_EDIT_DTM, PUBLISH_DTM, TITLE, VIEW_COUNT, AUTHOR_NAME, CATEGORY_NAME, SUBJECT_NAME, VERSION) AS
         ( VALUES
		(doc_sequence.NEXTVAL, '20200114', 'Doc 01 Description', 'http://google.com/docs01', 10, 'tinyurl.com/2fhx8t34', 0,'20200115' ,'20200115', 'Doc 01 Title', 34, 'bran', 'Doc Category 01', 'Doc Subject 02', 0),
		(doc_sequence.NEXTVAL, '20210114', 'Doc 02 Description', 'http://google.com/docs02', 100, 'tinyurl.com/2fhx8t34', 0,'20200115' ,'20210115', 'Doc 02 Title', 256, 'alex', 'Doc Category 02', 'Doc Subject 01', 0)
         )

INSERT INTO DOC (ID, CREATED_DTM, DESCRIPTION, DOCURL, DOWNLOAD_COUNT, IMAGEURL, DOC_STATE, LAST_EDIT_DTM, PUBLISH_DTM, TITLE, VIEW_COUNT, AUTHOR_ID, CATEGORY_ID, SUBJECT_ID, VERSION)
SELECT
    docs.ID, docs.CREATED_DTM, docs.DESCRIPTION, docs.DOCURL, docs.DOWNLOAD_COUNT, docs.IMAGEURL, docs.DOC_STATE, docs.LAST_EDIT_DTM ,docs.PUBLISH_DTM, docs.TITLE, docs.VIEW_COUNT, author.ID, category.ID, subject.ID, docs.VERSION
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
		('alex', 'Doc 02 Title', 0),
             ('david', 'Doc 01 Title', 0)
         )
INSERT INTO USER_DOC_REACTION (USER_ID, DOC_ID, DOC_REACTION_TYPE)
SELECT
    actor.ID, doc.ID, userDocReactions.DOC_REACTION_TYPE
FROM
    userDocReactions JOIN USER_WEBSITE AS actor
                          ON userDocReactions.USER_NAME = actor.NAME
                     JOIN DOC AS doc
                          ON userDocReactions.DOC_TITLE = doc.TITLE;

-- INSERT DOC COMMENT.
WITH docComments (ID, CONTENT, AUTHOR_NAME, DOC_TITLE) AS (
    VALUES (doc_comment_sequence.NEXTVAL, 'Tài liệu hay và chính xác quá. Cảm ơn admin.', 'alex', 'Doc 01 Title'),
    (doc_comment_sequence.NEXTVAL,'Tài liệu hữu ích. Nhưng có nhiều chỗ sai. Admin xem lại nha.', 'bran', 'Doc 02 Title'),
    (doc_comment_sequence.NEXTVAL,'Thanks nhé.', 'bran', 'Doc 01 Title'),
    (doc_comment_sequence.NEXTVAL,'Cảm ơn ạ.', 'bran', 'Doc 01 Title')

)
INSERT INTO DOC_COMMENT (ID, CONTENT, AUTHOR_ID, DOC_ID, PARENT_COMMENT_ID)
SELECT docComments.ID, docComments.CONTENT, author.ID ,doc.ID, null
FROM
    docComments JOIN USER_WEBSITE AS author ON docComments.AUTHOR_NAME = author.NAME
                JOIN DOC AS doc ON docComments.DOC_TITLE = doc.TITLE;

-- INSERT DOC COMMENT CHILD.
WITH docComments (ID, CONTENT, AUTHOR_NAME, DOC_TITLE, PARENT_COMMENT_CONTENT) AS (
    VALUES (doc_comment_sequence.NEXTVAL, 'Ok thanks bạn nhé', 'bran', 'Doc 01 Title', 'Tài liệu hay và chính xác quá. Cảm ơn admin.'),
    (doc_comment_sequence.NEXTVAL,'Cảm ơn bạn đã nhắc nhở', 'bran', 'Doc 02 Title', 'Tài liệu hữu ích. Nhưng có nhiều chỗ sai. Admin xem lại nha.')
)
INSERT INTO DOC_COMMENT (ID, CONTENT, AUTHOR_ID, DOC_ID, PARENT_COMMENT_ID)
SELECT docComments.ID, docComments.CONTENT, author.ID ,doc.ID, childComment.ID
FROM
    docComments JOIN USER_WEBSITE AS author ON docComments.AUTHOR_NAME = author.NAME
                JOIN DOC AS doc ON docComments.DOC_TITLE = doc.TITLE
                JOIN DOC_COMMENT AS childComment ON docComments.PARENT_COMMENT_CONTENT = childComment.CONTENT;

-- INSERT POST CATEGORY
INSERT INTO POST_CATEGORY (ID, NAME, VERSION)
VALUES ( post_category_sequence.NEXTVAL, 'Post Category 01', 0),
       (post_category_sequence.NEXTVAL, 'Post Category 02', 0),
       (post_category_sequence.NEXTVAL, 'Hoạt động', 0);

-- INSERT POST.
WITH posts (ID, CONTENT, IMAGEURL, PUBLISH_DTM, SUMMARY, TITLE, READING_TIME, AUTHOR_NAME, CATEGORY_NAME, POST_STATE, VERSION) AS (
    VALUES (post_sequence.NEXTVAL,'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
    'https://i.imgur.com/LnHFl0h.png', '20200126' , 'Summary of post 01',
    'Post 01 title', 300,'alex', 'Post Category 01', 1, 0),
    (post_sequence.NEXTVAL,'augue, eu tempor erat neque non quam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus. Nullam scelerisque neque sed sem egestas blandit. Nam nulla magna, malesuada vel, convallis in, cursus et, eros. Proin ultrices. Duis volutpat nunc sit amet metus. Aliquam erat volutpat. Nulla facilisis. Suspendisse commodo tincidunt nibh. Phasellus nulla. Integer vulputate, risus a ultricies adipiscing, enim mi tempor lorem, eget mollis lectus pede et risus. Quisque libero lacus, varius et, euismod et, commodo at, libero. Morbi accumsan laoreet ipsum. Curabitur consequat, lectus sit amet luctus vulputate, nisi sem semper erat, in consectetuer ipsum nunc id enim. Curabitur massa. Vestibulum accumsan neque et nunc. Quisque ornare tortor at risus. Nunc ac sem ut dolor dapibus gravida. Aliquam tincidunt, nunc ac mattis ornare, lectus ante dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus.',
                'https://i.imgur.com/LnHFl0h.png', '20200127' , 'etNullam ut nisi a odio semper cursus.',
    'magna. Nam ligula elit, pretium et, rutrum non, hendrerit id, ante.', 300,'alex', 'Hoạt động', 1, 0),
    (post_sequence.NEXTVAL,'Aenean eget metus. In nec orci. Donec nibh. Quisque nonummy ipsum non arcu. Vivamus sit amet risus. Donec egestas. Aliquam nec enim. Nunc ut erat. Sed nunc est, mollis non, cursus non, egestas a, dui. Cras pellentesque. Sed dictum. Proin eget odio. Aliquam vulputate ullamcorper magna. Sed eu eros. Nam consequat dolor vitae dolor. Donec fringilla. Donec feugiat metus sit amet ante. Vivamus non lorem vitae odio sagittis semper. Nam tempor diam dictum sapien. Aenean massa. Integer vitae nibh. Donec est mauris, rhoncus id, mollis nec, cursus a, enim. Suspendisse aliquet, sem ut cursus luctus, ipsum leo elementum sem, vitae aliquam eros turpis non enim. Mauris quis turpis vitae',
    'https://i.imgur.com/LnHFl0h.png', '20200126' , 'Nullam scelerisque neque sed sem egestas blandit. Vestibulum',
    'mvel, faucibus id, libero. Donec consectetuer mauris id sapien. Cras', 300,'alex', 'Hoạt động', 1, 0),
    (post_sequence.NEXTVAL,'Đây là một bài post mới',
    'https://i.imgur.com/LnHFl0h.png', '20200128' , 'Đây là tóm tắt của bài post',
    'Đây là tiêu đề của bài post', 300,'alex', 'Post Category 02', 1, 0)
)
INSERT INTO POST (ID, CONTENT, IMAGEURL, PUBLISH_DTM, SUMMARY, TITLE, READING_TIME, AUTHOR_ID, CATEGORY_ID, POST_STATE, VERSION)
SELECT
    posts.ID, posts.CONTENT, posts.IMAGEURL, posts.PUBLISH_DTM, posts.SUMMARY, posts.TITLE, posts.READING_TIME, author.ID, category.ID, posts.POST_STATE, posts.VERSION
FROM
    posts JOIN USER_WEBSITE AS author
                ON posts.AUTHOR_NAME = author.NAME
          JOIN POST_CATEGORY AS category
                ON posts.CATEGORY_NAME = category.NAME;

-- INSERT POST USER LIKE.

WITH postUserLiked (POST_TITLE, USER_NAME) AS (
    VALUES ('Post 01 title', 'bran'),
    ('Post 01 title', 'alex')
)
INSERT INTO USER_POST_LIKE (POST_ID, USER_ID)
SELECT
    posts.ID, users.ID
FROM
    postUserLiked JOIN POST AS posts
                        ON postUserLiked.POST_TITLE = posts.TITLE
                  JOIN USER_WEBSITE AS users
                        ON postUserLiked.USER_NAME = users.NAME;

-- INSERT POST COMMENT.
WITH postComments (ID, CONTENT, AUTHOR_NAME, POST_TITLE, PARENT_COMMENT_CONTENT, VERSION) AS (
    VALUES (post_comment_sequence.NEXTVAL, 'Bài hay, thanks admin', 'alex', 'Post 01 title', null, 0),
    (post_comment_sequence.NEXTVAL, 'Thanks nhé', 'bran', 'Post 01 title', null, 0)
)
INSERT INTO POST_COMMENT (ID, CONTENT, AUTHOR_ID, PARENT_COMMENT_ID, POST_ID, VERSION)
SELECT
    postComments.ID, postComments.CONTENT, author.ID, null, post.ID, postComments.VERSION
FROM
    postComments JOIN USER_WEBSITE as author
                        ON postComments.AUTHOR_NAME = author.NAME
                 JOIN POST as post
                        ON postComments.POST_TITLE = post.TITLE;

-- INSERT CHILD COMMENT.
WITH childComments (ID, CONTENT, AUTHOR_NAME, POST_TITLE, PARENT_COMMENT_CONTENT, VERSION) AS (
    VALUES(post_comment_sequence.NEXTVAL, 'Child comment nè', 'alex', 'Post 01 title', 'Thanks nhé', 0)
)
INSERT INTO POST_COMMENT (ID, CONTENT, AUTHOR_ID, PARENT_COMMENT_ID, POST_ID, VERSION)
SELECT
    childComments.ID, childComments.CONTENT, author.ID, parentComment.ID, post.ID, childComments.VERSION
FROM
    childComments JOIN USER_WEBSITE as author
                      ON childComments.AUTHOR_NAME = author.NAME
                 JOIN POST as post
                      ON childComments.POST_TITLE = post.TITLE
                 JOIN POST_COMMENT as parentComment
                      ON childComments.PARENT_COMMENT_CONTENT = parentComment.CONTENT;

-- INSERT TAG.
INSERT INTO TAG(ID, CONTENT, VERSION) VALUES
(tag_sequence.NEXTVAL, 'elit', 0),
(tag_sequence.NEXTVAL, 'erat', 0),
(tag_sequence.NEXTVAL, 'rutrum', 0),
(tag_sequence.NEXTVAL, 'nunc', 0),
(tag_sequence.NEXTVAL, 'felis', 0),
(tag_sequence.NEXTVAL, 'tag111', 0),
(tag_sequence.NEXTVAL, 'tag11', 0),
(tag_sequence.NEXTVAL, 'tag111111', 0),
(tag_sequence.NEXTVAL, 'tag1111', 0),
(tag_sequence.NEXTVAL, 'tag11111', 0);

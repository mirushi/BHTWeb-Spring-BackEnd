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
        (activity_sequence.NEXTVAL ,'New Post Added', '2', 'bran', 'alex', '20201214', '1', 0),
        (activity_sequence.NEXTVAL ,'New Post Approved', '2', 'alex', 'bran', '20201215', '2', 0)
    )
INSERT INTO ACTIVITY (ID, NAME, TYPE, ACTOR_ACTIVE_ID, ACTOR_PASSIVE_ID, ACTIVITY_DTM, ID_ITEM, VERSION)
SELECT
    activities.ID, activities.NAME, activities.TYPE, active_user.ID, passive_user.ID, activities.ACTIVITY_DTM, activities.ID_ITEM, activities.VERSION
FROM
    activities JOIN USER_WEBSITE AS active_user
    ON activities.ACTOR_ACTIVE_NAME = active_user.NAME
    JOIN USER_WEBSITE AS passive_user
    ON activities.ACTOR_PASSIVE_NAME = passive_user.NAME

-- INSERT INTO DOC (ID, CREATED_DTM, DESCRIPTION, DISLIKE_COUNT, )
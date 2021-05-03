SELECT 'Hello world';

INSERT INTO ANNOUNCEMENT (ID, CONTENT, ACTIVATED_STATUS, LAST_UPDATED, VERSION)
VALUES (announcement_sequence.NEXTVAL, 'Thong bao thu nhat - Server vua khai truong', true, '20201226', 0),
       (announcement_sequence.NEXTVAL, 'Thong bao - Qua tang giang sinh :v', true, '20201225', 0);

-- INSERT ACTIVITIES
WITH activities (ID, NAME, TYPE, ACTOR_ACTIVE_NAME, ACTOR_PASSIVE_NAME, ACTIVITY_DTM, ID_ITEM, VERSION) AS
         ( VALUES
        (activity_sequence.NEXTVAL ,'New Post Added', '1', 'nghi', 'dong', '20201214', '1', 0),
        (activity_sequence.NEXTVAL ,'New Post Approved', '1', 'dong', 'nghi', '20201215', '2', 0)
         )
INSERT INTO ACTIVITY (ID, NAME, TYPE, ACTOR_ACTIVE_ID, ACTOR_PASSIVE_ID, ACTIVITY_DTM, ID_ITEM, VERSION)
SELECT
    activities.ID, activities.NAME, activities.TYPE, active_user.ID, passive_user.ID, activities.ACTIVITY_DTM, activities.ID_ITEM, activities.VERSION
FROM
    activities JOIN USER_WEBSITE AS active_user
                    ON activities.ACTOR_ACTIVE_NAME = active_user.NAME
               JOIN USER_WEBSITE AS passive_user
                    ON activities.ACTOR_PASSIVE_NAME = passive_user.NAME;

-- INSERT POST COMMENT.
WITH postComments (ID, CONTENT, AUTHOR_NAME, POST_TITLE, PARENT_COMMENT_CONTENT, VERSION) AS (
    VALUES (post_comment_sequence.NEXTVAL, 'Bài hay, thanks admin', 'dong', 'Nỗ lực làm việc', null, 0),
    (post_comment_sequence.NEXTVAL, 'Thanks nhé', 'hai', 'Nỗ lực làm việc', null, 0)
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
    VALUES(post_comment_sequence.NEXTVAL, 'Chúc bạn thành công.', 'dong', 'Nỗ lực làm việc', 'Thanks nhé', 0)
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
(tag_sequence.NEXTVAL, 'no_luc', 0),
(tag_sequence.NEXTVAL, 'cham_chi', 0),
(tag_sequence.NEXTVAL, 'dam_thoai', 0),
(tag_sequence.NEXTVAL, 'co_gang', 0),
(tag_sequence.NEXTVAL, 'khon_ngoan', 0),
(tag_sequence.NEXTVAL, 'tap_trung', 0),
(tag_sequence.NEXTVAL, 'suy_ngam', 0),
(tag_sequence.NEXTVAL, 'cach_song', 0),
(tag_sequence.NEXTVAL, 'thuong_nguoi', 0),
(tag_sequence.NEXTVAL, 'se_chia', 0);

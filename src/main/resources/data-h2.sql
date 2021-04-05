SELECT 'Hello world';

INSERT INTO ANNOUNCEMENT (ID, CONTENT, ACTIVATED_STATUS, LAST_UPDATED, VERSION)
VALUES (announcement_sequence.NEXTVAL, 'Thong bao thu nhat - Server vua khai truong', true, '20201226', 0),
       (announcement_sequence.NEXTVAL, 'Thong bao - Qua tang giang sinh :v', true, '20201225', 0);

-- INSERT USER ROLE.

INSERT INTO USER_WEBSITE_ROLE (ID, NAME, VERSION)
VALUES (1, 'ADMIN', 0),
       (2, 'USER', 0);

-- INSERT USER.
INSERT INTO USER_WEBSITE (ID, NAME, DISPLAY_NAME, HASHED_PASSWORD, EMAIL, REPUTATION_SCORE, AVATARURL, BAN_STATUS, VERSION)
VALUES (user_website_sequence.NEXTVAL, 'dong', 'Nguyen Van Dong', '{bcrypt}$2y$10$nShAWCpLkCxoYKR3hoYFhuaQF2og9q6OMuHFd.YvgLE0mubSfl7eS', '123@gmail.com', 100, 'https://i.ytimg.com/vi/VdhJ_Fm3_mk/maxresdefault.jpg', false, 0),
       (user_website_sequence.NEXTVAL, 'nghi', 'Luu Bieu Nghi', '{bcrypt}$2y$10$C/itsd7lLBUJ93PzmgUYXuUVRNgVq2ZGqsUCBWn5M1NIhq.DyR3C6' , '456@gmail.com', 300, 'https://www.vippng.com/png/detail/136-1363405_avatar-png-image-with-transparent-background-funny-avatar.png', false, 0),
       (user_website_sequence.NEXTVAL, 'hai', 'Vu Tuan Hai', '{bcrypt}$2y$10$ASuiBrP65KvyLx7TQ0WMq.MZzg1d9cLerk4sErQH0kYXLRG1siibi' , '456@gmail.com', 300, 'https://media.images.yourquote.in/post/large/0/0/5/223/WGvR1449.jpg', false, 0);

-- INSERT ROLES OF USERS.
INSERT INTO USER_ROLE (USER_ID, ROLE_ID) VALUES
    ((SELECT ID FROM USER_WEBSITE WHERE NAME='dong'), (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME='ADMIN')),
    ((SELECT ID FROM USER_WEBSITE WHERE NAME='nghi'), (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME='USER')),
    ((SELECT ID FROM USER_WEBSITE WHERE NAME='hai'), (SELECT ID FROM USER_WEBSITE_ROLE WHERE NAME='USER'));

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

-- INSERT POST CATEGORY
INSERT INTO POST_CATEGORY (ID, NAME, VERSION)
VALUES ( post_category_sequence.NEXTVAL, 'Tin tức', 0),
       (post_category_sequence.NEXTVAL, 'Kinh nghiệm học tập', 0),
       (post_category_sequence.NEXTVAL, 'Hoạt động', 0);

-- INSERT POST.
WITH posts (ID, CONTENT, IMAGEURL, PUBLISH_DTM, SUMMARY, TITLE, READING_TIME, AUTHOR_NAME, CATEGORY_NAME, POST_STATE, VERSION) AS (
    VALUES (post_sequence.NEXTVAL,'Không ít người đi làm tại các công ty đặt ra cho mình những suy nghĩ rằng:
- "Tại sao tôi lại phải dốc sức làm việc hết mình? Đây cũng không phải công ty của tôi, tôi chỉ đi làm công và nhận lương hàng tháng, đâu cần phải cố gắng 100% làm gì?",
- "Anh ta cũng làm cùng 1 công việc như tôi, đâu cần phải làm thật giỏi, mà vẫn có mức lương bằng với tôi thôi"...
- "Tôi chẳng đam mê gì công việc của mình, tôi chỉ cần làm đúng nhiệm vụ, không cần làm thêm cái gì phát sinh ngoài công việc cả"...
Thật ra, có 3 lí do tại sao bạn nên luôn luôn cố gắng làm việc thật tốt không phải chỉ để nhận lương, hãy cùng xem phía dưới đây nhé:',
    'https://stickybranding.com/wp-content/uploads/2019/01/SBQ-Hard-Work-946x532.jpg', '20200126' , 'Tại sao phải luôn nỗ lực?',
    'Nỗ lực làm việc', 300,'dong', 'Tin tức', 1, 0),
    (post_sequence.NEXTVAL,'Với những ai chưa biết, tôi muốn giúp các bạn tìm hiểu rõ hơn về con đường đưa Elon Musk trở thành tỷ phú tự thân, đồng thời là nguyên mẫu đời thực của nhân vật Tony Stark trong bộ phim Iron Man (Người Sắt). Tuy nhiên, trước hết, hãy để Richard Branson giải thích ngắn gọn...',
                'https://topdev.vn/blog/wp-content/uploads/2017/05/gjdasd.jpg', '20200127' , 'Elon Musk đã thành công như thế nào ?',
    'Con đường lập nghiệp của Elon Musk', 300,'nghi', 'Hoạt động', 1, 0),

    (post_sequence.NEXTVAL,'Cuộc sống luôn ngập tràn những điều bất ngờ thú vị, và con người thì không ngừng khát khao tìm lời giải cho mọi vấn đề hiện hữu quanh mình.

Xuyên suốt quá trình chinh phục thế giới, một trong những câu hỏi day dứt và ám ảnh của nhiều người nhất chính là làm thế nào để có được sự may mắn? Theo quan niệm chung thì may mắn là một khái niệm gì đó khá trừu tượng và gần như nghiêng về yếu tố tâm linh nhiều hơn.

Nhiều người cho rằng mọi việc xảy ra trên đời này đều có lí do của nó – hệt như một sự sắp đặt của tự nhiên. Và vận may cũng là một phần của quy luật bất biến ấy…

Thế nhưng liệu có đúng khi chúng ta cứ thuận theo quan niệm có phần bị động như thế?

Chắc hẳn mình và bạn – những người trẻ mang tư duy rộng mở về thế giới sẽ chẳng khi nào chấp nhận nó. Ai cũng nhận thức được chúng ta mới là người kiến tạo nên cuộc đời của chính mình.',
    'https://blogchiasekienthuc.com/wp-content/uploads/2020/05/cach-tu-tao-ra-may-man-cho-chinh-minh-3.jpg', '20200126' , 'Làm sao để tự tạo ra may mắn cho chính mình ?',
    'Tạo ra may mắn cho chính mình', 300,'dong', 'Hoạt động', 1, 0),

    (post_sequence.NEXTVAL,'Đầu tiên, sự siêng năng chăm chỉ luôn là điều quan trọng nhất nếu bạn muốn thành công bất cứ điều gì. Môn Toán thật sự không khó, có điều phải học đúng cách và sự đầu tư đúng hướng thì bạn sẽ giỏi thôi. Không chỉ học trên lớp mà về nhà cũng phải trau dồi và luyện tập thì bạn sẽ cảm thấy môn học này thật sự chẳng khó tí nào đâu!',
    'https://i.imgur.com/LnHFl0h.png', '20200128' , 'Môn Toán luôn là một trong những môn mà biết bao học sinh sợ hãi khi nhắc đến. Vậy làm cách nào để học tốt môn Toán? ',
    'Phương pháp học tốt môn Toán', 300,'hai', 'Kinh nghiệm học tập', 1, 0)
)
INSERT INTO POST (ID, CONTENT, CONTENT_PLAIN_TEXT, IMAGEURL, PUBLISH_DTM, SUMMARY, TITLE, READING_TIME, AUTHOR_ID, CATEGORY_ID, POST_STATE, VERSION)
SELECT
    posts.ID, posts.CONTENT, posts.CONTENT, posts.IMAGEURL, posts.PUBLISH_DTM, posts.SUMMARY, posts.TITLE, posts.READING_TIME, author.ID, category.ID, posts.POST_STATE, posts.VERSION
FROM
    posts JOIN USER_WEBSITE AS author
                ON posts.AUTHOR_NAME = author.NAME
          JOIN POST_CATEGORY AS category
                ON posts.CATEGORY_NAME = category.NAME;

-- INSERT POST USER LIKE.

WITH postUserLiked (POST_TITLE, USER_NAME) AS (
    VALUES ('Nỗ lực làm việc', 'nghi'),
    ('Nỗ lực làm việc', 'dong'),
    ('Phương pháp học tốt môn Toán', 'hai'),
    ('Nỗ lực làm việc', 'hai')
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

CREATE TABLE post_AUD
(
    id      BIGINT NOT NULL,
    REV     INTEGER NOT NULL,
    REVTYPE TINYINT,
    title   VARCHAR(255),
    PRIMARY KEY ( id, REV )
)
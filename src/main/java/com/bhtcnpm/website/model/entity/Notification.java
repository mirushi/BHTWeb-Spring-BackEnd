package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.enumeration.NotificationType.NotificationType;
import com.bhtcnpm.website.model.entity.enumeration.NotificationType.NotificationTypeConverter;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "notification_sequence"
    )
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence"
    )
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    private UserWebsite actor;

    @ManyToOne (fetch = FetchType.LAZY)
    private UserWebsite actorPassive;

    @Column(nullable = false)
    private LocalDateTime actionDtm;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private Long score;

    @Column(nullable = false)
    private String title;

    @Convert(converter = NotificationTypeConverter.class)
    @Column(columnDefinition = "smallint")
    private NotificationType type;

}

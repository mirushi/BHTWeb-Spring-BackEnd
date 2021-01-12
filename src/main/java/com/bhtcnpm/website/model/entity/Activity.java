package com.bhtcnpm.website.model.entity;

import com.bhtcnpm.website.model.entity.enumeration.ActivityType.ActivityType;
import com.bhtcnpm.website.model.entity.enumeration.ActivityType.ActivityTypeConverter;
import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@Data
public class Activity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "activity_sequence"
    )
    @SequenceGenerator(
            name = "activity_sequence",
            sequenceName = "activity_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private UserWebsite actorActive;

    @ManyToOne
    private UserWebsite actorPassive;

    @Column(nullable = false)
    private LocalDateTime activityDtm;

    //ID of items that relates to actorActive and/or actorPassive.
    private Long idItem;

    @Convert(converter = ActivityTypeConverter.class)
    @Column(columnDefinition = "smallint")
    private ActivityType type;

    @Version
    private short version;

}

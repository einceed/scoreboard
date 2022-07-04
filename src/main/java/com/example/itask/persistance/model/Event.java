package com.example.itask.persistance.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "SEQ_NAME1")
//    @GenericGenerator(name = "SEQ_NAME1",strategy = "SEQUENCE", parameters = { @Parameter(name = "SEQUENCE", value = "EVENT_SEQ") })
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "team_1", nullable = false)
    private String team1;

    @Column(name = "team_2", nullable = false)
    private String team2;

    @OneToMany(mappedBy="event",
            cascade = {javax.persistence.CascadeType.ALL},
            orphanRemoval = true
    )
    private Set<Score> items = new HashSet<>();
}

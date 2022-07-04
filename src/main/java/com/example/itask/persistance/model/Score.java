package com.example.itask.persistance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "SEQ_NAME")
//    @GenericGenerator(name = "SEQ_NAME",strategy = "SEQUENCE", parameters = { @Parameter(name = "SEQUENCE", value = "SCORE_SEQ") })
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "score", nullable = false)
    private String score;

    @Column(name = "minutes", nullable = false)
    private Integer minute;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;
}

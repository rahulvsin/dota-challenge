package gg.bayes.challenge.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "HeroKills")
@Setter
@Getter
public class HeroKillsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long matchId;

    private String hero;

    private String heroDeceased;

}

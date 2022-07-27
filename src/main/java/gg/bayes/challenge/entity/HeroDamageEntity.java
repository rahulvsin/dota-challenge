package gg.bayes.challenge.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "HeroDamage")
@Setter
@Getter
public class HeroDamageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long matchId;

    private String hero;

    private String target;

    private int damage;
}

package gg.bayes.challenge.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "HeroItem")
@Setter
@Getter
public class HeroItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long matchId;

    private String hero;

    private String item;

    private long timestamp;

}

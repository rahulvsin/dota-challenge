package gg.bayes.challenge.service.repository;

import gg.bayes.challenge.entity.HeroKillsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroKillsRepository extends JpaRepository<HeroKillsEntity, Long> {

    List<HeroKillsEntity> findByMatchId(long matchId);
}

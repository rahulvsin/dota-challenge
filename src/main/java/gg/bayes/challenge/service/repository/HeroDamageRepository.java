package gg.bayes.challenge.service.repository;

import gg.bayes.challenge.entity.HeroDamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroDamageRepository extends JpaRepository<HeroDamageEntity, Long> {

    List<HeroDamageEntity> findByMatchIdAndHero(long matchId, String hero);
}

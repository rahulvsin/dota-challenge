package gg.bayes.challenge.service.repository;

import gg.bayes.challenge.entity.HeroItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroItemRepository extends JpaRepository<HeroItemEntity, Long> {

    List<HeroItemEntity> findByMatchIdAndHero(long matchId, String hero);
}

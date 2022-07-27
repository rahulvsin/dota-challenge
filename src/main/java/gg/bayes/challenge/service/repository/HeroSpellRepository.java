package gg.bayes.challenge.service.repository;

import gg.bayes.challenge.entity.HeroSpellEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroSpellRepository extends JpaRepository<HeroSpellEntity, Long> {

    List<HeroSpellEntity> findByMatchIdAndHero(long matchId, String hero);
}

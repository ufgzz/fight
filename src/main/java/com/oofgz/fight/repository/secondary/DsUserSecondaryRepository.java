package com.oofgz.fight.repository.secondary;

import com.oofgz.fight.domain.secondary.DsUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DsUserSecondaryRepository extends JpaRepository<DsUser, Long> {
}

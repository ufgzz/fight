package com.oofgz.fight.repository.primary;

import com.oofgz.fight.domain.primary.JpaDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeptRepository extends JpaRepository<JpaDept, Long> {

    JpaDept findByName(String name);

    JpaDept findByNameAndNum(String name, Integer num);

    @Query("from JpaDept d where d.name =:name")
    JpaDept findJpaDept(@Param("name") String name);

}

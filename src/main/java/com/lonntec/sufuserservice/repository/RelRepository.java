package com.lonntec.sufuserservice.repository;

import com.lonntec.sufuserservice.entity.Domain_DomainUser_Rel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelRepository extends JpaRepository<Domain_DomainUser_Rel,String>{

    @Query("select rel from Domain_DomainUser_Rel rel " +
            "where rel.domainId=:domainId and (" +
            "rel.domainUser.userName like :keyword or rel.domainUser.mobile like :keyword or rel.domainUser.email)" +
            "order by rel.domainUser.userName desc")
    List<Domain_DomainUser_Rel> findAllByMyQuery(
            @Param("domainId") String domainId,
            @Param("keyword") String keyword
    );
    @Query("select count (rel) from Domain_DomainUser_Rel rel " +
            "where rel.domainId=:domain and (rel.domainUser.userName like :keyword)")
    Integer countByMyQuery(
            @Param("domainId") String domainId,
            @Param("keyword") String keyword
    );
    @Query("select rel from Domain_DomainUser_Rel rel where rel.domainUser.rowId=:domainUserId and rel.domainId=:domainId")
    Optional<Domain_DomainUser_Rel> findByMyQuery(
            @Param("domainUserId") String domainUserId,
            @Param("domainId") String domainId
    );
}

package com.lonntec.sufuserservice.repository;

import com.lonntec.sufuserservice.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<Domain,String>{
    Optional<Domain> findByDomainNumber(String domainNumber);
}

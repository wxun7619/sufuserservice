package com.lonntec.sufuserservice.repository;

import com.lonntec.sufuserservice.entity.DomainUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SufUserRepository extends PagingAndSortingRepository<DomainUser,String>{
    Optional<DomainUser> findByMobile(String mobile);
    Optional<DomainUser> findByEmail(String email);

}

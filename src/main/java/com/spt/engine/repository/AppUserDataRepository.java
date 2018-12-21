package com.spt.engine.repository;

import com.spt.engine.entity.AppUserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppUserDataRepository extends JpaSpecificationExecutor<AppUserData>,
        JpaRepository<AppUserData, Long>,
        PagingAndSortingRepository<AppUserData, Long> {
    @Query("SELECT a FROM AppUserData a where lower(a.firstName)like lower(concat(:firstName,'%')) ")
    List<AppUserData> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    //@Query ("SELECT ") SELECT * FROM APP_USER_DATA
    @Query("SELECT b FROM AppUserData b where lower(b.lastName)like lower(concat('%',concat(:lastName,'%')) ) ")
    List<AppUserData> findByLastNameIsContaining(@Param("lastName") String lastName);

    @Query("SELECT a FROM AppUserData a where  lower(a.deseription)like lower(concat('%',concat(:deseription,'%')) ) ")
    List<AppUserData> searchdeseription(@Param("deseription") String deseription);

    List<AppUserData> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
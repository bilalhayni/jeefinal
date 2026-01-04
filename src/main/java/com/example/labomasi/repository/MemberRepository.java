package com.example.labomasi.repository;

import com.example.labomasi.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<Member> findByLnameContainingIgnoreCase(String lname, Pageable pageable);

    List<Member> findTop5ByOrderByCreatedAtDesc();

    //@Query("SELECT r.rolename, COUNT(m) FROM Member m JOIN m.roles r GROUP BY r.rolename")
    @Query("SELECT r.rolename, COUNT(m) FROM Member m JOIN m.role r GROUP BY r.rolename")
    List<Object[]> countMembersByRole();
}
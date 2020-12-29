package com.griffin.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")  // @Query : SpringDataJpa 에서 제공하지 않는 메소드의 경우 쿼리 직접 작성 (querydsl)
    List<Posts> findAllDesc();

}

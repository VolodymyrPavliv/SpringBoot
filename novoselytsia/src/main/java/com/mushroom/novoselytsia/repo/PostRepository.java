package com.mushroom.novoselytsia.repo;

import com.mushroom.novoselytsia.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findByAnons(String anons);
}

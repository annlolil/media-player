package com.example.media_player.repositories;

import com.example.media_player.entities.UserMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserMedia, Long> {

    List<UserMedia> findUserHistoriesByUserId(String userId);

    Optional<UserMedia> findByUserIdAndMediaId(String testSub, Long mediaId); //remove? replace?

    UserMedia findByMediaIdAndUserId(Long mediaId, String userId);
}

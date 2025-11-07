package com.example.media_player.repositories;

import com.example.media_player.entities.UserMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserMedia, Long> {

    Optional<UserMedia> findByUserIdAndMediaId(String userId, Long mediaId);

    List<UserMedia> findByUserIdAndLikedMediaTrue(String userId);

    List<UserMedia> findByUserIdAndDislikedMediaTrue(String userId);

    List<UserMedia> findUserMediaByUserId(String userId);
}

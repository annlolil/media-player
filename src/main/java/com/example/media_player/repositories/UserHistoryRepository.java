package com.example.media_player.repositories;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    List<UserHistory> findUserHistoriesByUserId(String userId);
}

package ru.practicum.mainsvc.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE (:ids is null or id IN (:ids))")
    List<User> getUsersListByIdList(List<Long> ids, Pageable pageable);

    Boolean existsUserByName(String name);
}

package ru.practicum.mainsvc.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.util.Pagination;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(Long[] ids, Pagination page);
}

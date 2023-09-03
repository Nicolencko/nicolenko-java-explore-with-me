package ru.practicum.mainsvc.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainsvc.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE event = :eventId")
    List<Request> getRequestsByEvent(Long eventId);

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE event = :eventId AND id = :reqId")
    Request getRequestsByReqId(Long eventId, Long reqId);

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE requester = :userId")
    List<Request> getRequestsByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM requests WHERE requester = :userId AND event = :eventId")
    Request getRequestByUserIdAndEventId(Long userId, Long eventId);
}

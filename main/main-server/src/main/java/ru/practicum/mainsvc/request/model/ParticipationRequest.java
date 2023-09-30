package ru.practicum.mainsvc.request.model;

import lombok.*;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.mainsvc.request.model.RequestStatus.PENDING;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @ToString.Exclude
    private User requester;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RequestStatus status = PENDING;
}

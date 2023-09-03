package ru.practicum.mainsvc.compilations.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(EventsCompilationsId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events_compilations")
public class EventsCompilations {
    @Id
    private Long compilationId;
    @Id
    private Long eventId;
}

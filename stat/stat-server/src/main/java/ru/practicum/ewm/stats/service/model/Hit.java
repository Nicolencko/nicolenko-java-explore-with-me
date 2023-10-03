package ru.practicum.ewm.stats.service.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String app;

    @Column(length = 512)
    private String uri;

    @Column(length = 20)
    private String ip;

    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}

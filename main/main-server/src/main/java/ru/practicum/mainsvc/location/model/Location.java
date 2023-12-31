package ru.practicum.mainsvc.location.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;
}

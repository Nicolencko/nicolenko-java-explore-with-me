package ru.practicum.mainsvc.user.model;

import java.util.Optional;

public interface InitiatorRateView {

    Optional<Long> getInitiatorId();

    Optional<String> getName();

    Optional<Long> getRating();

}

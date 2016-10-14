package net.bakulin;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class Event {

    private final Instant created = Instant.now();
    private final int clientId;
    private final UUID uuid;

    public Event(int clientId, UUID uuid) {
        this.clientId = clientId;
        this.uuid = uuid;
    }
}

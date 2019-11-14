package nl.appsource.stream.demo;

import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Util {

    public static Long getLongOrDefault(final ServerRequest ServerRequest, final String name, final Long value) {

        return ServerRequest.queryParam(name)
                .flatMap(Util::safeLongValueofOptional)
                .orElse(value);
    }

    public static Long safeLongValueOf(final String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static UUID safeUuidValueOf(final String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Optional<Long> safeLongValueofOptional(final String value) {
        return Optional.ofNullable(safeLongValueOf(value));
    }

    public static Optional<UUID> safeUuidValueofOptional(final String value) {
        return Optional.ofNullable(safeUuidValueOf(value));
    }

    public static Mono<Long> safeLongValueofMono(final String value) {
        return Mono.justOrEmpty(safeLongValueofOptional(value));
    }

    public static Mono<UUID> safeUuidValueofMono(final String value) {
        return Mono.justOrEmpty(safeUuidValueofOptional(value));
    }

}

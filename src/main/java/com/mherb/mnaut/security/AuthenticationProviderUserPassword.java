package com.mherb.mnaut.security;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import jakarta.inject.Singleton;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import java.util.ArrayList;

@Singleton
@Slf4j
@Data
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    @Override
    public Publisher<AuthenticationResponse> authenticate(
        @Nullable final HttpRequest<?> httpRequest,
        final AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            final Object identity = authenticationRequest.getIdentity();
            final Object secret = authenticationRequest.getSecret();

            log.debug("User {} tries to login...", identity);

            if (identity.equals("my-user") && secret.equals("secret")) {
                // pass
                log.debug("User logged in.");
                emitter.onNext(AuthenticationResponse.success((String) identity, new ArrayList<>()));
                emitter.onComplete();
                return;
            }
            emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password!")));
        }, BackpressureStrategy.ERROR);    }
}

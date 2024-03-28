
package me.shukawam.app;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import io.helidon.metrics.api.MetricsFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @AfterAll
    static void clear() {
        MetricsFactory.closeAll();
    }

    @Test
    void testHealth() {
        Response response = target.path("health").request().get();
        assertThat(response.getStatus(), is(200));
    }

}

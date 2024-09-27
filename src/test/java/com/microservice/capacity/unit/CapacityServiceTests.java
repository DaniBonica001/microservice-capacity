package com.microservice.capacity.unit;

import com.microservice.capacity.application.ports.output.CapacityPersistencePort;
import com.microservice.capacity.application.service.CapacityService;
import com.microservice.capacity.domain.model.Capacity;
import com.microservice.capacity.domain.model.Tech_Capacity;
import com.microservice.capacity.domain.model.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CapacityServiceTests {

    @Mock
    private CapacityPersistencePort persistencePort;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private CapacityService service;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service = new CapacityService(persistencePort, webClient);
    }

    @Test
    void testCreateCapacitySuccess() {
        // Arrange
        Capacity capacity = Capacity.builder().name("New Capacity").build();
        List<Integer> technologies = Arrays.asList(1, 2, 3);

        // Mocking persistencePort
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(persistencePort.createCapacity(any(Capacity.class))).thenReturn(Mono.just(capacity));

        // Mocking WebClient chain
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(Tech_Capacity.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.createCapacity(capacity, technologies))
                .expectNext(capacity)
                .verifyComplete();

        verify(persistencePort).existsByName(capacity.getName());
        verify(persistencePort).createCapacity(capacity);
    }

    @Test
    void testCreateCapacityAlreadyExists() {
        // Arrange
        Capacity capacity = Capacity.builder().name("Existing Capacity").build();
        List<Integer> technologies = Arrays.asList(1, 2, 3);

        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(service.createCapacity(capacity, technologies))
                .expectErrorMatches(e -> e.getMessage().equals("Capacity already exists"))
                .verify();

        verify(persistencePort).existsByName(capacity.getName());
        verify(persistencePort, never()).createCapacity(any(Capacity.class));
    }

    @Test
    void testCreateCapacityInvalidTechnologyCount() {
        // Arrange
        Capacity capacity = Capacity.builder().name("New Capacity").build();
        List<Integer> technologies = Arrays.asList(1, 2); // Solo 2 tecnologías (menos de 3)

        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(service.createCapacity(capacity, technologies))
                .expectErrorMatches(e -> e.getMessage().equals("Technologies must be between 3 and 20"))
                .verify();

        verify(persistencePort).existsByName(capacity.getName());
        verify(persistencePort, never()).createCapacity(any(Capacity.class));
    }

    @Test
    void testFindAllPagedSuccess() {
        // Arrange
        Capacity capacity1 = Capacity.builder().id(1).name("Capacity 1").build();
        Capacity capacity2 = Capacity.builder().id(2).name("Capacity 2").build();
        List<Capacity> capacities = Arrays.asList(capacity1, capacity2);

        Technology tech1 = Technology.builder().name("Tech 1").build();
        Technology tech2 = Technology.builder().name("Tech 2").build();
        List<Technology> techs = Arrays.asList(tech1, tech2);

        // Mocks de WebClient
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        // Mock comportamiento del WebClient
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec)requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString(), anyInt())).thenAnswer(invocation -> requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(Technology.class)).thenReturn(Flux.fromIterable(techs));

        // Mock persistence behavior
        when(persistencePort.findAll()).thenReturn(Flux.fromIterable(capacities));

        // Act & Assert
        StepVerifier.create(service.findAllPaged(0, 2, "name", "asc"))
                .expectNextMatches(page -> page.getContent().size() == 2 &&
                        page.getContent().get(0).getTechnologies().size() == 2)
                .verifyComplete();

        verify(persistencePort).findAll();
    }

    @Test
    void testFindAllPagedCapacityNotFound() {
        // Arrange
        when(persistencePort.findAll()).thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(service.findAllPaged(0, 2, "name", "asc"))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Capacity not found"))
                .verify();

        verify(persistencePort).findAll();
    }

    @Test
    void testFindAllPagedOrderByTechnologies() {
        // Arrange
        Capacity capacity1 = Capacity.builder().id(1).name("Capacity 1").build();
        Capacity capacity2 = Capacity.builder().id(2).name("Capacity 2").build();
        List<Capacity> capacities = Arrays.asList(capacity1, capacity2);

        Technology tech1 = Technology.builder().name("Tech 1").build();
        Technology tech2 = Technology.builder().name("Tech 2").build();
        List<Technology> techs = Arrays.asList(tech1, tech2);

        // Mocks de WebClient
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        // Mock comportamiento del WebClient
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec)requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString(), anyInt())).thenAnswer(invocation -> requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(Technology.class)).thenReturn(Flux.fromIterable(techs));

        // Mock persistence behavior
        when(persistencePort.findAll()).thenReturn(Flux.fromIterable(capacities));

        // Act & Assert
        StepVerifier.create(service.findAllPaged(0, 2, "technologies", "asc"))
                .expectNextMatches(page -> page.getContent().size() == 2 &&
                        page.getContent().get(0).getTechnologies().size() == 2)
                .verifyComplete();

        verify(persistencePort).findAll();
    }






}

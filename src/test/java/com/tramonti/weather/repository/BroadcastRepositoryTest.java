package com.tramonti.weather.repository;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.tramonti.weather.StubFactory;
import com.tramonti.weather.domain.broadcast.BroadcastCity;
import org.bson.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BroadcastRepositoryTest {

    private static final String LONDON = "London";
    private static final Query TODAY_CITY_QUERY;

    static {
        LocalDateTime dateMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime nextDateMidnight = dateMidnight.plusDays(1);
        TODAY_CITY_QUERY = new Query(Criteria.where("city").is(LONDON).and("dateTime").gte(dateMidnight).lt(nextDateMidnight));
    }

    private BroadcastRepository broadcastRepository;

    @Mock
    private MongoTemplate mongoTemplate;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        broadcastRepository = new BroadcastRepositoryImpl(mongoTemplate);
    }

    @Test
    public void save() {

    }

    @Test
    public void findSuccess() {
        List<BroadcastCity> stubCities = Arrays.asList(StubFactory.getStub("london-11-01", BroadcastCity[].class));
        when(mongoTemplate.find(TODAY_CITY_QUERY, BroadcastCity.class)).thenReturn(stubCities);

        List<BroadcastCity> result = broadcastRepository.find(LONDON, LocalDate.now());
        stubCities.sort(Comparator.comparing(BroadcastCity::getDateTime));

        assertEquals(stubCities, result);
        verify(mongoTemplate, times(1)).find(TODAY_CITY_QUERY, BroadcastCity.class);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void findFail() {
        when(mongoTemplate.find(TODAY_CITY_QUERY, BroadcastCity.class)).thenReturn(new ArrayList<>());

        List<BroadcastCity> result = broadcastRepository.find(LONDON, LocalDate.now());

        assertEquals(new ArrayList<>(), result);
        verify(mongoTemplate, times(1)).find(TODAY_CITY_QUERY, BroadcastCity.class);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void existsSuccessTest() {
        when(mongoTemplate.findOne(TODAY_CITY_QUERY, BroadcastCity.class)).thenReturn(new BroadcastCity());

        Boolean cityExists = broadcastRepository.exists(LONDON, LocalDate.now());

        assertTrue(cityExists);
        verify(mongoTemplate, times(1)).findOne(TODAY_CITY_QUERY, BroadcastCity.class);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void existsFailTest() {
        when(mongoTemplate.findOne(TODAY_CITY_QUERY, BroadcastCity.class)).thenReturn(null);

        Boolean cityExists = broadcastRepository.exists(LONDON, LocalDate.now());

        assertFalse(cityExists);
        verify(mongoTemplate, times(1)).findOne(TODAY_CITY_QUERY, BroadcastCity.class);
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void getAvailableCities() {
        MongoCollection<Document> mongoCollection = mock(MongoCollection.class);
        DistinctIterable<String> distinctIterable = mock(DistinctIterable.class);
        when(mongoTemplate.getCollection("broadcast")).thenReturn(mongoCollection);
        when(mongoCollection.distinct("city", String.class)).thenReturn(distinctIterable);
        doNothing().when(distinctIterable).forEach(any(Consumer.class));

        broadcastRepository.getAvailableCities();

        verify(mongoTemplate, times(1)).getCollection("broadcast");
        verify(mongoCollection, times(1)).distinct("city", String.class);
        verify(distinctIterable, times(1)).forEach(any(Consumer.class));
        verifyNoMoreInteractions(mongoTemplate);
        verifyNoMoreInteractions(mongoCollection);
        verifyNoMoreInteractions(distinctIterable);
    }
}
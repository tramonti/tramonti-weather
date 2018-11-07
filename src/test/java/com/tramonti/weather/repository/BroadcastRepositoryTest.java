package com.tramonti.weather.repository;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.utils.TestUtils;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

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
    public void saveExistingCities() {
        List<BroadcastCity> citiesStub = Arrays.asList(TestUtils
                .getStub("broadcasts", BroadcastCity[].class));
        MongoConverter mongoConverter = mock(MongoConverter.class);
        UpdateResult updateResult = mock(UpdateResult.class);

        when(mongoTemplate.getConverter()).thenReturn(mongoConverter);
        doNothing().when(mongoConverter).write(any(BroadcastCity.class), any(Document.class));
        when(mongoTemplate.upsert(any(Query.class), any(Update.class), eq(BroadcastCity.class))).thenReturn(updateResult);
        when(mongoTemplate.findOne(any(Query.class), eq(BroadcastCity.class))).thenReturn(new BroadcastCity().setId("00000"));
        when(updateResult.getUpsertedId()).thenReturn(null);

        broadcastRepository.save(citiesStub);

        verify(mongoTemplate, times(citiesStub.size())).upsert(any(Query.class), any(Update.class), eq(BroadcastCity.class));
        verify(mongoTemplate, times(citiesStub.size())).findOne(any(Query.class), eq(BroadcastCity.class));
        verify(mongoTemplate, times(citiesStub.size())).getConverter();
        verify(mongoConverter, times(citiesStub.size())).write(any(BroadcastCity.class), any(Document.class));
        verify(updateResult, times(citiesStub.size())).getUpsertedId();

        assertEquals("00000", citiesStub.get(0).getId());
    }

    @Test
    public void saveNotExistingCities() {
        List<BroadcastCity> citiesStub = Arrays.asList(TestUtils
                .getStub("broadcasts", BroadcastCity[].class));
        MongoConverter mongoConverter = mock(MongoConverter.class);
        UpdateResult updateResult = mock(UpdateResult.class);
        BsonValue bsonValue = new BsonObjectId(new ObjectId("5abe381661731b8cb6bddde0"));

        when(mongoTemplate.getConverter()).thenReturn(mongoConverter);
        doNothing().when(mongoConverter).write(any(BroadcastCity.class), any(Document.class));
        when(mongoTemplate.upsert(any(Query.class), any(Update.class), eq(BroadcastCity.class))).thenReturn(updateResult);
        when(mongoTemplate.findOne(any(Query.class), eq(BroadcastCity.class))).thenReturn(new BroadcastCity().setId("00000"));
        when(updateResult.getUpsertedId()).thenReturn(bsonValue);

        broadcastRepository.save(citiesStub);

        verify(mongoTemplate, times(citiesStub.size())).upsert(any(Query.class), any(Update.class), eq(BroadcastCity.class));
        verify(mongoTemplate, times(citiesStub.size())).getConverter();
        verify(mongoConverter, times(citiesStub.size())).write(any(BroadcastCity.class), any(Document.class));
        verify(updateResult, times(citiesStub.size() * 2)).getUpsertedId();

        assertEquals("5abe381661731b8cb6bddde0", citiesStub.get(0).getId());
    }

    @Test
    public void findSuccess() {
        List<BroadcastCity> stubCities = Arrays.asList(TestUtils
                .getStub("london-11-01", BroadcastCity[].class));
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
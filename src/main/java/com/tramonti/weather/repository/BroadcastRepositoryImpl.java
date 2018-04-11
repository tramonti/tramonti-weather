package com.tramonti.weather.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.tramonti.weather.domain.broadcast.BroadcastCity;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Repository
public class BroadcastRepositoryImpl implements BroadcastRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public BroadcastRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<BroadcastCity> save(List<BroadcastCity> cities) {
        cities.parallelStream().forEach(this::upsert);
        return cities;
    }

    @Override
    public List<BroadcastCity> find(String cityName, LocalDate date) {
        Query query = getCityDateQuery(cityName, date);
        List<BroadcastCity> result = mongoTemplate.find(query, BroadcastCity.class);
        result.sort(Comparator.comparing(BroadcastCity::getDateTime));
        return result;
    }

    @Override
    public boolean exists(String cityName, LocalDate date) {
        Query query = getCityDateQuery(cityName, date);
        BroadcastCity result = mongoTemplate.findOne(query, BroadcastCity.class);
        return result != null;
    }

    private Query getCityDateQuery(String cityName, LocalDate date) {
        LocalDateTime dateMidnight = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        LocalDateTime nextDateMidnight = dateMidnight.plusDays(1);

        return new Query(Criteria.where("city").is(cityName)
                .and("dateTime").gte(dateMidnight).lt(nextDateMidnight));
    }

    private void upsert(BroadcastCity city) {
        Query query = new Query(Criteria.where("city").is(city.getCity())
                .and("dateTime").is(city.getDateTime()));

        Document dbDoc = new Document();
        mongoTemplate.getConverter().write(city, dbDoc);
        Update update = Update.fromDocument(dbDoc, "_id");
        UpdateResult result = mongoTemplate.upsert(query, update, BroadcastCity.class);
        String id;
        if (result.getUpsertedId() != null) {
            id = result.getUpsertedId().asObjectId().getValue().toString();
            city.setId(id);
        } else {
            id = mongoTemplate.findOne(query, BroadcastCity.class).getId();
        }
        city.setId(id);
    }

    @Override
    public List<String> getAvailableCities() {
        List<String> cities = new ArrayList<>();

        MongoCollection<Document> collection = mongoTemplate.getCollection("broadcast");

        collection.distinct("city", String.class).forEach((Consumer<String>) cities::add);
        cities.sort(Comparator.naturalOrder());
        return cities;
    }
}

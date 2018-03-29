package com.tramonti.weather.repository;

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
import java.util.List;

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

    //    TODO: mongo find by date
    @Override
    public List<BroadcastCity> find(String cityName, LocalDate date) {
        cityName = cityName.toLowerCase();
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);

        Query query = new Query(Criteria.where("city").is(cityName)
                .and("dateTime").is(dateTime));
        return mongoTemplate.find(query, BroadcastCity.class);
    }
    private void upsert(BroadcastCity city){
        Query query = new Query(Criteria.where("city").is(city.getCity())
                .and("dateTime").is(city.getDateTime()));

        Document dbDoc = new Document();
        mongoTemplate.getConverter().write(city, dbDoc);
        Update update = Update.fromDocument(dbDoc, "_id");
        UpdateResult result = mongoTemplate.upsert(query, update, BroadcastCity.class, "broadcast");
        String id;
        if (result.getUpsertedId() != null) {
            id = result.getUpsertedId().asObjectId().getValue().toString();
            city.setId(id);
        } else {
            id = mongoTemplate.findOne(query, BroadcastCity.class).getId();
        }
        city.setId(id);
    }
}

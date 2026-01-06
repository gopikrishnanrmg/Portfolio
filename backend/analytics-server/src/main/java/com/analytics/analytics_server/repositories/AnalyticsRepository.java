package com.analytics.analytics_server.repositories;

import com.analytics.analytics_server.models.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnalyticsRepository extends MongoRepository<Analytics, String> {
}

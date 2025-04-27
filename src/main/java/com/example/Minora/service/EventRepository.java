package com.example.Minora.service;

import com.example.Minora.entity.Event;
import com.example.Minora.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByInsuredId(String insuredId);

   @Query(value = "SELECT e FROM event e INNER JOIN product p ON p.event_id = e.id WHERE e.insured_id = :insuredId", nativeQuery = true)
   List<Event> findByInsuredIdWithProducts(@Param("insuredId") String insuredId);



    @Query(value = "SELECT e.id AS event_id, e.insured_id, e.type AS event_type, e.request_id, " +
            "p.id AS product_id, p.type AS product_type, p.price, p.start_date, p.end_date " +
            "FROM event e INNER JOIN product p ON p.event_id = e.id WHERE e.insured_id = :insuredId",
            nativeQuery = true)
    List<Object[]> findByInsuredIdWithProductsNative(@Param("insuredId") String insuredId);
}

package com.example.Minora.service;

import com.example.Minora.dto.CompanyProductsDTO;
import com.example.Minora.dto.ProductDto;
import com.example.Minora.entity.Event;
import com.example.Minora.entity.Product;
import com.example.Minora.entity.Request;
import com.example.Minora.dto.RootDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final RequestRepository requestRepository;



    @Transactional
    public void readXmlAndSaveToDatabase(Path sourceFile) {
        try {
            // Read XML
            File file = new File(sourceFile.toString());
            JAXBContext context = JAXBContext.newInstance(RootDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            RootDto rootDto = (RootDto) unmarshaller.unmarshal(file);

            // Mapping Request
            Request requestEntity = Request.builder()
                    .id(rootDto.getRequestDetails().getId())
                    .acceptDate(LocalDateTime.parse(rootDto.getRequestDetails().getAcceptDate().replace(" ", "T")))
                    .sourceCompany(rootDto.getRequestDetails().getSourceCompany())
                    .build();

            // Save Request separetly
            requestRepository.save(requestEntity);

            // Mapping Event + Products
            List<Event> eventEntities = rootDto.getEvents().stream()
                    .map(eventDto -> {
                        Event event = Event.builder()
                                .id(eventDto.getId())
                                .type(eventDto.getType())
                                .insuredId(eventDto.getInsuredId())
                                .request(requestEntity) // referents with  Request
                                .build();

                        List<Product> products = eventDto.getProducts().stream()
                                .map(productDto -> Product.builder()
                                        .type(productDto.getType())
                                        .price(productDto.getPrice())
                                        .startDate(LocalDate.parse(productDto.getStartDate().toString()))
                                        .endDate(LocalDate.parse(productDto.getEndDate().toString()))
                                        .event(event) // referents with  Event
                                        .build())
                                .collect(Collectors.toList());

                        event.setProducts(products);
                        return event;
                    })
                    .collect(Collectors.toList());

            // Save Event + Products cascade
            eventRepository.saveAll(eventEntities);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read and save XML data", e);
        }
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void moveFileToBackup(Path sourceFile, Path backupDirectory) throws IOException {
        if (!Files.exists(backupDirectory)) {
            Files.createDirectories(backupDirectory);
        }
        Path targetFile = backupDirectory.resolve(sourceFile.getFileName());
        Files.move(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public List<CompanyProductsDTO> getProductsGroupedBySourceCompany(String insuredId) {


        // 1. get events
        List<Event> events = eventRepository.findByInsuredId(insuredId);

        // 2.get products
        List<String> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<Product> products = productRepository.findByEventIdIn(eventIds);

        // 3. groups producrs by company
        Map<String, List<ProductDto>> groupedProducts = products.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getEvent().getRequest().getSourceCompany(),
                        Collectors.mapping(product -> new ProductDto(
                                product.getType(),
                                product.getPrice(),
                                product.getStartDate(),
                                product.getEndDate()
                        ), Collectors.toList())
                ));
        List<CompanyProductsDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<ProductDto>> entry : groupedProducts.entrySet()) {
            String company = entry.getKey();
            List<ProductDto> productDtos = entry.getValue();
            CompanyProductsDTO companyProductsDTO = new CompanyProductsDTO(company, productDtos);
            result.add(companyProductsDTO);
        }

        return result;
    }
}
   /* public List<CompanyProductsDTO> getProductsGroupedBySourceCompany(String insuredId) {

        List<Object[]> result = eventRepository.findByInsuredIdWithProductsNative(insuredId);
        System.out.println(result.size());

        Map<String, List<ProductDto>> groupedProducts = new HashMap<>();

        for (Object[] row : result) {
            // Розпаковуємо дані з Object[]
            String eventId = (String) row[0];
            String insuredIdFromDb = (String) row[1];
            String eventType = (String) row[2];
            String requestId = (String) row[3];

            Long productId = (Long) row[4];
            String productType = (String) row[5];
            BigDecimal price = (BigDecimal) row[6];
            Date startDate = (Date) row[7];
            Date endDate = (Date) row[8];


            ProductDto productDto = new ProductDto(productType, price, null, null);


            String sourceCompany = "";  // Маємо отримати SourceCompany через Event, але дані в Object[] це не містять
            if (!groupedProducts.containsKey(sourceCompany)) {
                groupedProducts.put(sourceCompany, new ArrayList<>());
            }
            groupedProducts.get(sourceCompany).add(productDto);
        }

        return groupedProducts.entrySet().stream()
                .map(entry -> new CompanyProductsDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


}
*/
/*
    public List<CompanyProductsDTO> getProductsGroupedBySourceCompany(String insuredId) {
        List<Event> events = eventRepository.findByInsuredId(insuredId);
        //List<Event> events = eventRepository.findByInsuredIdWithProductsNative(insuredId);
        List<Object[]> results = eventRepository.findByInsuredIdWithProductsNative(insuredId);
        System.out.println(results.size());


        List<Product> products = events.stream()
                .flatMap(event -> event.getProducts().stream())
               .collect(Collectors.toList());


        Map<String, List<ProductDto>> groupedProducts = products.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getEvent().getRequest().getSourceCompany(),
                        Collectors.mapping(product -> new ProductDto(
                                product.getType(),
                                product.getPrice(),
                                product.getStartDate(),
                                product.getEndDate()
                        ), Collectors.toList())
                ));

        return groupedProducts.entrySet().stream()
                .map(entry -> new CompanyProductsDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }*/




package org.kozlowski.msscbeerservice.services;

import lombok.extern.slf4j.Slf4j;
import org.kozlowski.msscbeerservice.config.JmsConfig;
import org.kozlowski.msscbeerservice.domain.Beer;
import org.kozlowski.msscbeerservice.events.BrewBeerEvent;
import org.kozlowski.msscbeerservice.repositories.BeerRepository;
import org.kozlowski.msscbeerservice.services.inventory.BeerInventoryService;
import org.kozlowski.msscbeerservice.web.mappers.BeerMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    public BrewingService(BeerRepository beerRepository, BeerInventoryService beerInventoryService, JmsTemplate jmsTemplate, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerInventoryService = beerInventoryService;
        this.jmsTemplate = jmsTemplate;
        this.beerMapper = beerMapper;
    }


    @Scheduled(fixedRate = 5000)//every 5 seconds
    public void checkForLowInventory() {
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            Integer invQOH = beerInventoryService.getOnHandInventory(beer.getId());
            log.debug("Min Onhas is: " + beer.getMinOnHand());
            log.debug("Inventory is: " + invQOH);

            if (beer.getMinOnHand() >= invQOH) {
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}

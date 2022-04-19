package org.kozlowski.msscbeerservice.services.brewing;

import lombok.extern.slf4j.Slf4j;
import org.kozlowski.common.events.BrewBeerEvent;
import org.kozlowski.common.events.NewInventoryEvent;
import org.kozlowski.msscbeerservice.config.JmsConfig;
import org.kozlowski.msscbeerservice.domain.Beer;
import org.kozlowski.msscbeerservice.repositories.BeerRepository;
import org.kozlowski.msscbeerservice.web.model.BeerDto;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BrewBeerListener {
    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    public BrewBeerListener(BeerRepository beerRepository, JmsTemplate jmsTemplate) {
        this.beerRepository = beerRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event) {
        BeerDto beerDto = event.getBeerDto();

        Beer beer = beerRepository.getById(beerDto.getId());

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewer beer: " + beer.getMinOnHand() + " : QOH: " + beerDto.getQuantityOnHand());
        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }
}

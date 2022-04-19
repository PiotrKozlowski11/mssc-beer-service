package org.kozlowski.common.events;

import org.kozlowski.msscbeerservice.web.model.BeerDto;

public class BrewBeerEvent extends BeerEvent {

    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }

    public BrewBeerEvent() {
    }
}

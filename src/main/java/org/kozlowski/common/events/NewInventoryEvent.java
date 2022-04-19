package org.kozlowski.common.events;

import org.kozlowski.msscbeerservice.web.model.BeerDto;

public class NewInventoryEvent extends BeerEvent {
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }

    public NewInventoryEvent() {
    }
}

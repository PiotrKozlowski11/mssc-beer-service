package org.kozlowski.common.events;

import lombok.Builder;
import lombok.Data;
import org.kozlowski.msscbeerservice.web.model.BeerDto;

import java.io.Serializable;

@Data
@Builder
public class BeerEvent implements Serializable {

    private BeerDto beerDto;

    public BeerEvent(BeerDto beerDto) {
        this.beerDto = beerDto;
    }

    public BeerEvent() {
    }
}


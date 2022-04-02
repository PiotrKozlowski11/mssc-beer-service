package org.kozlowski.msscbeerservice.events;

import lombok.Builder;
import lombok.Data;
import org.kozlowski.msscbeerservice.web.model.BeerDto;

import java.io.Serializable;

@Data
@Builder
public class BeerEvent implements Serializable {

    private final BeerDto beerDto;

    public BeerEvent(BeerDto beerDto) {
        this.beerDto = beerDto;
    }
}


package org.kozlowski.msscbeerservice.services;

import org.kozlowski.msscbeerservice.web.model.BeerDto;
import org.kozlowski.msscbeerservice.web.model.BeerPagedList;
import org.kozlowski.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    BeerDto getById(UUID beerId);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    List<BeerDto> findAll();

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest of);
}

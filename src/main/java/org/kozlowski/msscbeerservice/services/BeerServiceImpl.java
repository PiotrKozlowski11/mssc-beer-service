package org.kozlowski.msscbeerservice.services;

import org.kozlowski.msscbeerservice.domain.Beer;
import org.kozlowski.msscbeerservice.repositories.BeerRepository;
import org.kozlowski.msscbeerservice.web.mappers.BeerMapper;
import org.kozlowski.msscbeerservice.web.model.BeerDto;
import org.kozlowski.msscbeerservice.web.model.BeerPagedList;
import org.kozlowski.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    public BeerDto getById(UUID beerId) {
        return beerMapper.beerToBeerDto(
                beerRepository.findById(beerId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format("Beer for id %s not found", beerId)))
        );
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Beer for id %s not found", beerId)));

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());
        return beerMapper.beerToBeerDto(beer);
    }

    @Override
    public List<BeerDto> findAll() {
        return StreamSupport.stream(beerRepository.findAll().spliterator(), false)
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest of) {

        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        boolean beerNameEmpty;
        boolean beerStyleEmpty;

        beerNameEmpty = beerName == null || beerName.isEmpty();

        beerStyleEmpty = beerStyle == null || beerStyle.name().isEmpty();

        if (!beerNameEmpty && !beerStyleEmpty) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle.name(), of);
        } else if (!beerNameEmpty) {
            beerPage = beerRepository.findAllByBeerName(beerName, of);
        } else if (!beerStyleEmpty) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle.name(), of);
        } else {
            beerPage = beerRepository.findAll(of);
        }

        beerPagedList = new BeerPagedList(beerPage
                .getContent()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList()),
                PageRequest.of(beerPage.getPageable().getPageNumber(),
                        beerPage.getPageable().getPageSize()),
                beerPage.getTotalElements());

        return beerPagedList;
    }
}

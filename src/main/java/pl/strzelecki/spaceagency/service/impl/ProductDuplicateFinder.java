package pl.strzelecki.spaceagency.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.strzelecki.spaceagency.model.Product;
import pl.strzelecki.spaceagency.repository.ProductRepository;
import pl.strzelecki.spaceagency.service.DuplicateFinder;

@Service
@Transactional
public class ProductDuplicateFinder implements DuplicateFinder<Product> {

    private static final Logger logger = LogManager.getLogger(ProductDuplicateFinder.class);

    private ProductRepository productRepo;

    @Autowired
    public ProductDuplicateFinder(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public boolean lookForDuplicateInDb(Product product) {
        logger.info("Looking for duplicate product in database");
        logger.trace("Calling productRepo to find mission by its name");
        logger.info("Returning result of check");
        return productRepo.existsByMissionAndAcquisitionDateAndFootprintAndPriceAndUrl(
                product.getMission(),
                product.getAcquisitionDate(),
                product.getFootprint(),
                product.getPrice(),
                product.getUrl());
    }
}

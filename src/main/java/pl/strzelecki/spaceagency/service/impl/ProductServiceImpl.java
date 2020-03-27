package pl.strzelecki.spaceagency.service.impl;

import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.entity.DTO.ProductByMissionDTO;
import pl.strzelecki.spaceagency.entity.DTO.ProductByTypeOrDateDTO;
import pl.strzelecki.spaceagency.entity.ImageryTypeEnum;
import pl.strzelecki.spaceagency.entity.Product;
import pl.strzelecki.spaceagency.repository.ProductRepository;
import pl.strzelecki.spaceagency.service.DuplicateFinder;
import pl.strzelecki.spaceagency.service.ProductService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepo;
    private DuplicateFinder<Product> duplicateFinder;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepo,
                              DuplicateFinder<Product> duplicateFinder) {
        this.productRepo = productRepo;
        this.duplicateFinder = duplicateFinder;
    }

    @Override
    public List<Product> findAll() {
        logger.info("Find all products");
        logger.trace("Calling productRepo to find all products");
        return productRepo.findAll();
    }

    @Override
    public void save(Product product) {
        logger.info("Save a product");
        logger.trace("Checking if the product already exists in the database");
        if (duplicateFinder.lookForDuplicateInDb(product)) {
            logger.error("Exception while searching for duplicate - product already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This product already exists in the database.");
        }
        logger.info("Saving the product");
        productRepo.save(product);
    }

    @Override
    public void remove(long id) {
        logger.info("Remove a product");
        logger.trace("Checking if the product exists in the database");
        Optional<Product> productById = productRepo.findById(id);
        logger.trace("Checking if result (Optional<Product>) is empty");
        if (productById.isEmpty()) {
            logger.error("Exception - Result is empty");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id: " + id + " not found.");
        }
        logger.info("Deleting the product");
        productRepo.deleteById(id);
    }

    @Override
    public Product getProduct(long id) {
        return productRepo.findById(id).orElseThrow();
    }

    @Override
    public List<ProductByMissionDTO> findAllByMissionName(String missionName) {
        logger.info("Find products by mission name");
        logger.trace("Calling productRepo to find product by mission name. Replacing '+' with ' '");
        return productRepo.findAllByMissionName(missionName.replace("+", " "));
    }

    @Override
    public List<ProductByTypeOrDateDTO> findAllByProductType(String productType) {
        logger.info("Find products by product type");
        logger.trace("Checking if provided product type is valid");
        if (!EnumUtils.isValidEnum(ImageryTypeEnum.class, productType)) {
            logger.error("Exception - product type is invalid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That is not a valid imagery type.");
        }
        logger.trace("Calling productRepo to find products by product type");
        ImageryTypeEnum imageryTypeToSearch = ImageryTypeEnum.valueOf(productType);
        return productRepo.findAllByProductType(imageryTypeToSearch);
    }

    @Override
    public List<ProductByTypeOrDateDTO> findAllLowerThanAcquisitionDate(LocalDate date) {
        logger.info("Find products with acquisitionDate before provided date");
        return productRepo.findAllLowerThanAcquisitionDate(date);
    }

    @Override
    public List<ProductByTypeOrDateDTO> findAllGreaterThanAcquisitionDate(LocalDate date) {
        logger.info("Find products with acquisitionDate after provided date");
        return productRepo.findAllGreaterThanAcquisitionDate(date);
    }

    @Override
    public List<ProductByTypeOrDateDTO> findAllBetweenAcquisitionDates(LocalDate firstDate, LocalDate secondDate) {
        logger.info("Find products with acquisitionDate between provided dates");
        return productRepo.findAllBetweenAcquisitionDates(firstDate, secondDate);
    }
}

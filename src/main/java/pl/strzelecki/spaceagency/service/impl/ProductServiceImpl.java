package pl.strzelecki.spaceagency.service.impl;

import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.model.DTO.ProductByMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO;
import pl.strzelecki.spaceagency.model.ImageryTypeEnum;
import pl.strzelecki.spaceagency.model.Product;
import pl.strzelecki.spaceagency.repository.MissionRepository;
import pl.strzelecki.spaceagency.repository.ProductOrderRepository;
import pl.strzelecki.spaceagency.repository.ProductRepository;
import pl.strzelecki.spaceagency.service.BackupService;
import pl.strzelecki.spaceagency.service.ProductService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepo;
    private ProductOrderRepository productOrderRepository;
    private BackupService backupService;
    private MissionRepository missionRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepo,
                              ProductOrderRepository productOrderRepository,
                              BackupService backupService,
                              MissionRepository missionRepository) {
        this.productRepo = productRepo;
        this.productOrderRepository = productOrderRepository;
        this.backupService = backupService;
        this.missionRepository = missionRepository;
    }

    @Override
    public void save(Product product) {
        logger.info("Save a product");
        logger.trace("Checking if the product already exists in the database");
        if (lookForDuplicate(product)) {
            logger.error("Exception while searching for duplicate - product already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This product already exists in the database.");
        }
        logger.trace("Checking if mission exists in the database");
        if (!missionRepository.existsById(product.getMission().getId())) {
            logger.error("Exception while searching for mission - mission does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission does not exist in the database");
        }
        logger.info("Saving the product");
        productRepo.save(product);
    }

    private boolean lookForDuplicate(Product product) {
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
        logger.trace("Performing backup of product and productOrder");
        backupService.backupProductAndProductOrder(id);
        logger.trace("Deleting product");
        productOrderRepository.deleteProductOrder(id);
        productRepo.deleteById(id);
    }

    @Override
    public Product getProduct(long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
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

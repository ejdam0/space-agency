package pl.strzelecki.spaceagency.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.model.DTO.ProductByMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO;
import pl.strzelecki.spaceagency.model.Product;
import pl.strzelecki.spaceagency.service.ProductService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private static final Logger logger = LogManager.getLogger(ProductRestController.class);

    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAll() {
        logger.info("Find all products");
        logger.trace("Calling productService to find all products");
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Product product) {
        logger.info("Add a product");
        logger.trace("Setting product ID to 0, to force saving");
        // set id to force saving
        product.setId(0);
        try {
            logger.trace("Calling productService to try to save the product");
            productService.save(product);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to save the product: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.info("Product added successfully");
        return new ResponseEntity<>("Added new product:\n " + product, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> remove(@PathVariable("productId") long id) {
        logger.info("Remove a product");
        try {
            logger.trace("Calling productService to try to remove the product");
            productService.remove(id);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to save the product: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.info("Product removed successfully");
        return new ResponseEntity<>("Deleted product with id: " + id, HttpStatus.OK);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<?> findByMissionName(@RequestParam(value = "mission-name") String missionName) {
        logger.info("Find product by mission name");
        logger.trace("Calling productService to find products by mission name");
        List<ProductByMissionDTO> result = productService.findAllByMissionName(missionName);

        logger.trace("Checking size of results list. Returning responseEntity");
        return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(TypeMismatchException.class)
    @GetMapping("/search-by-type")
    public ResponseEntity<?> findByProductType(@RequestParam(value = "product-type") String productType) {
        logger.info("Find product by product type");
        logger.trace("Creating empty list of results");
        List<ProductByTypeOrDateDTO> result;
        try {
            logger.trace("Calling productService to find products by product type, saving them to list");
            result = productService.findAllByProductType(productType);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to save the product: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.trace("Checking size of results list. Returning responseEntity");
        return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search-by-date")
    public ResponseEntity<?> findByAcquisitionDate(
            @RequestParam(value = "flag") String flag,
            @RequestParam(value = "first-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDate,
            @RequestParam(value = "second-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate secondDate) {

        logger.info("Find product by acquisition date");
        logger.trace("Checking parameters");
        // if flag is lower than and only first date is provided
        if (flag.equals("LT") && secondDate == null) {
            logger.trace("Flag is LT, second date is null");
            logger.trace("Calling productService to find products.");
            List<ProductByTypeOrDateDTO> result = productService.findAllLowerThanAcquisitionDate(firstDate);
            logger.trace("Checking size of results list. Returning responseEntity");
            return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                    new ResponseEntity<>(result, HttpStatus.OK);
            // if flag is greater than and only first date is provided
        } else if (flag.equals("GT") && secondDate == null) {
            logger.trace("Flag is GT, second date is null");
            logger.trace("Calling productService to find products.");
            List<ProductByTypeOrDateDTO> result = productService.findAllGreaterThanAcquisitionDate(firstDate);
            logger.trace("Checking size of results list. Returning responseEntity");
            return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                    new ResponseEntity<>(result, HttpStatus.OK);
            // ignore what flag is set to, first and second dates are provided
        } else if (flag.equals("LT") || flag.equals("GT")) {
            logger.trace("Flag is either LT or GT, second date is not null");
            logger.trace("Calling productService to find products.");
            logger.trace("Checking if first date is before second date or the dates are equal");
            if (firstDate.compareTo(secondDate) > 0) {
                // first date occurs after second date
                logger.error("First date must be before second date");
                return new ResponseEntity<>("First date have to occur before second!", HttpStatus.BAD_REQUEST);
            } else if (firstDate.compareTo(secondDate) == 0) {
                // first date occurs after second date
                logger.error("Dates must not be equal");
                return new ResponseEntity<>("Dates must not be equal!", HttpStatus.BAD_REQUEST);
            }
            logger.info("Dates are correct");
            logger.trace("Calling productService to find products.");
            List<ProductByTypeOrDateDTO> result = productService.findAllBetweenAcquisitionDates(firstDate, secondDate);
            logger.trace("Checking size of results list. Returning responseEntity");
            return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                    new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            logger.error("Wrong parameters were provided");
            return new ResponseEntity<>("Provided wrong parameters", HttpStatus.BAD_REQUEST);
        }
    }
}
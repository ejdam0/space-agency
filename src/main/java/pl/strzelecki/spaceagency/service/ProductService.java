package pl.strzelecki.spaceagency.service;

import pl.strzelecki.spaceagency.model.DTO.ProductByMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO;
import pl.strzelecki.spaceagency.model.Product;

import java.time.LocalDate;
import java.util.List;

public interface ProductService extends AgencyService<Product> {

    Product getProduct(long id);

    List<ProductByMissionDTO> findAllByMissionName(String name);

    List<ProductByTypeOrDateDTO> findAllByProductType(String productType);

    List<ProductByTypeOrDateDTO> findAllLowerThanAcquisitionDate(LocalDate date);

    List<ProductByTypeOrDateDTO> findAllGreaterThanAcquisitionDate(LocalDate date);

    List<ProductByTypeOrDateDTO> findAllBetweenAcquisitionDates(LocalDate firstDate, LocalDate secondDate);

}

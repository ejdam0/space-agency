package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.strzelecki.spaceagency.model.DTO.ProductByMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO;
import pl.strzelecki.spaceagency.model.ImageryTypeEnum;
import pl.strzelecki.spaceagency.model.Mission;
import pl.strzelecki.spaceagency.model.Product;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByMissionAndAcquisitionDateAndFootprintAndPriceAndUrl(
            Mission mission, LocalDate acquisitionDate, String footprint, double price, String url);

    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.ProductByMissionDTO" +
            "(p.id, p.acquisitionDate, p.footprint, p.price, p.url) " +
            "FROM Product p WHERE p.mission.name = :missionName")
    List<ProductByMissionDTO> findAllByMissionName(@Param("missionName") String theMissionName);


    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO" +
            "(p.id, p.acquisitionDate, p.footprint, p.price, p.url, p.mission.name) " +
            "FROM Product p WHERE p.mission.imageryType = :productType")
    List<ProductByTypeOrDateDTO> findAllByProductType(@Param("productType") ImageryTypeEnum productType);


    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO" +
            "(p.id, p.acquisitionDate, p.footprint, p.price, p.url, p.mission.name) " +
            "FROM Product p WHERE p.acquisitionDate < :providedDate")
    List<ProductByTypeOrDateDTO> findAllLowerThanAcquisitionDate(@Param("providedDate") LocalDate date);

    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO" +
            "(p.id, p.acquisitionDate, p.footprint, p.price, p.url, p.mission.name) " +
            "FROM Product p WHERE p.acquisitionDate > :providedDate")
    List<ProductByTypeOrDateDTO> findAllGreaterThanAcquisitionDate(@Param("providedDate") LocalDate date);

    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.ProductByTypeOrDateDTO" +
            "(p.id, p.acquisitionDate, p.footprint, p.price, p.url, p.mission.name) " +
            "FROM Product p WHERE p.acquisitionDate > :firstProvidedDate AND p.acquisitionDate < :secondProvidedDate")
    List<ProductByTypeOrDateDTO> findAllBetweenAcquisitionDates(@Param("firstProvidedDate") LocalDate firstDate,
                                                                @Param("secondProvidedDate") LocalDate secondDate);

}

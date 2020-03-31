package pl.strzelecki.spaceagency.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.strzelecki.spaceagency.repository.ProductOrderRepository;
import pl.strzelecki.spaceagency.repository.ProductRepository;
import pl.strzelecki.spaceagency.service.BackupService;

@Service
public class BackupServiceImpl implements BackupService {

    private ProductRepository productRepository;
    private ProductOrderRepository productOrderRepository;

    @Autowired
    public BackupServiceImpl(ProductRepository productRepository, ProductOrderRepository productOrderRepository) {
        this.productRepository = productRepository;
        this.productOrderRepository = productOrderRepository;
    }

    @Override
    public void backupProductAndProductOrder(long productId) {
        productRepository.backupProduct(productId);
        productOrderRepository.backupProductOrder(productId);
    }
}

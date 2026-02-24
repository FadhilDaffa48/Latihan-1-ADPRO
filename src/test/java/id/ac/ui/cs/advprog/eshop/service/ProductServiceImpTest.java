package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(ProductServiceImpl.class)
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl service;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void create_shouldSetRandomUUID_callRepositoryCreate_andReturnProduct() {
        Product p = new Product();

        Product result = service.create(p);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).create(captor.capture());
        Product sentToRepo = captor.getValue();

        assertThat(sentToRepo.getProductId()).isNotNull();
        assertThat(sentToRepo.getProductId()).isNotBlank();

        assertThat(result).isSameAs(p);
        assertThat(result.getProductId()).isNotNull();
    }

    @Test
    void delete_shouldDelegateToRepository() {
        service.delete("P001");
        verify(productRepository, times(1)).delete("P001");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findById_shouldReturnRepositoryResult() {
        Product p = new Product();
        when(productRepository.findById("P001")).thenReturn(p);

        Product result = service.findById("P001");

        assertThat(result).isSameAs(p);
        verify(productRepository, times(1)).findById("P001");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void edit_shouldReturnRepositoryEditResult() {
        Product p = new Product();
        Product edited = new Product();

        when(productRepository.edit(p)).thenReturn(edited);

        Product result = service.edit(p);

        assertThat(result).isSameAs(edited);
        verify(productRepository, times(1)).edit(p);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findAll_shouldCollectIteratorIntoList() {
        Product p1 = new Product();
        Product p2 = new Product();
        List<Product> repoList = List.of(p1, p2);

        Iterator<Product> iterator = repoList.iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = service.findAll();

        assertThat(result).containsExactly(p1, p2);
        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }
}
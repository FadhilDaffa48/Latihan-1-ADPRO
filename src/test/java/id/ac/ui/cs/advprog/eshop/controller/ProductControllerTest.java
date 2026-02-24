package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void getCreatePage_shouldReturnCreateProductView_andPutEmptyProductInModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", org.hamcrest.Matchers.instanceOf(Product.class)));

        verifyNoInteractions(service);
    }

    @Test
    void postCreate_shouldCallServiceCreate_andRedirectToList() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("id", "P001")
                        .param("name", "Keyboard")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:list"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(service, times(1)).create(captor.capture());
        Product sent = captor.getValue();

        assertThat(sent).isNotNull();
    }

    @Test
    void getList_shouldReturnProductListView_andPutProductsInModel() throws Exception {
        Product p1 = new Product();
        Product p2 = new Product();
        List<Product> products = Arrays.asList(p1, p2);

        when(service.findAll()).thenReturn(products);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList")) // FIX: match templates/ProductList.html
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", products));

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEdit_whenProductNotFound_shouldRedirectToList() throws Exception {
        when(service.findById("NOPE")).thenReturn(null);

        mockMvc.perform(get("/product/edit/NOPE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:../list"));

        verify(service, times(1)).findById("NOPE");
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEdit_whenProductFound_shouldReturnEditProductView_andPutProductInModel() throws Exception {
        Product found = new Product();
        when(service.findById("P001")).thenReturn(found);

        mockMvc.perform(get("/product/edit/P001"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", found));

        verify(service, times(1)).findById("P001");
        verifyNoMoreInteractions(service);
    }

    @Test
    void postEdit_shouldCallServiceEdit_andRedirectToList() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("id", "P001")
                        .param("name", "Mouse")
                        .param("quantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:list"));

        verify(service, times(1)).edit(any(Product.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDelete_shouldCallServiceDelete_andRedirectToList() throws Exception {
        mockMvc.perform(get("/product/delete/P001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:../list"));

        verify(service, times(1)).delete("P001");
        verifyNoMoreInteractions(service);
    }
}
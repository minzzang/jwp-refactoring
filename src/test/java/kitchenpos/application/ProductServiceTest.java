package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    void create() {
        //given
        String name = "product";
        BigDecimal price = BigDecimal.valueOf(1000);
        Product request = 상품_요청_데이터_생성(name, price);

        Long id = 1L;
        given(productDao.save(request)).willReturn(new Product(id, name, price));

        //when
        Product product = productService.create(request);

        //then
        상품_데이터_확인(product, id, name, price);
    }

    @Test
    void create_fail_null() {
        //given
        String name = "product";
        Product request = 상품_요청_데이터_생성(name, null);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
    }

    @Test
    void create_fail_negative() {
        //given
        String name = "product";
        Product request = 상품_요청_데이터_생성(name, BigDecimal.valueOf(-1));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(request));
    }

    @Test
    void list() {
        //given
        Long id = 1L;
        String name = "product";
        BigDecimal price = BigDecimal.valueOf(1000);
        given(productDao.findAll()).willReturn(Arrays.asList(상품_데이터_생성(id, name, price)));

        //when
        List<Product> list = productService.list();

        //then
        assertEquals(1, list.size());
        상품_데이터_확인(list.get(0), id, name, price);
    }
}
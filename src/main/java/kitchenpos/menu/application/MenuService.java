package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.group.application.MenuGroupService;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        final Menu menu = menuRequest.toEntity();
        menu.grouping(menuGroup);

        final List<Product> products = productService.findAllByIds(menuRequest.getProductIds());

        Map<Product, Long> menuProducts = new HashMap<>();

        for(Product product : products)
        {
            MenuProductRequest menuProductRequest = menuRequest.find(product);
            menuProducts.put(product,  menuProductRequest.getQuantity());
        }
        menu.addProducts(menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }

    public List<Menu> findAllByIds(List<Long> menuIds) {
        final List<Menu> menus = menuRepository.findAllById(menuIds);

        if (menus.size() != menus.size()) {
            throw new IllegalArgumentException("일부 메뉴가 존재하지 않습니다.");
        }

        return menus;
    }
}

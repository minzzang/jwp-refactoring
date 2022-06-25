package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> asList);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);

    List<Order> findAllByOrOrderTableIdIn(List<Long> orderTableIds);

}

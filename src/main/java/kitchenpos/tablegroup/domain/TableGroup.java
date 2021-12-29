package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.domain.validator.CreateTableGroupValidator;
import kitchenpos.tablegroup.domain.validator.UnGroupTableGroupValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TableGroupOrderTableIds tableGroupOrderTableIds;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(List<Long> orderTableIds) {
        this.tableGroupOrderTableIds = TableGroupOrderTableIds.of(orderTableIds);
    }

    private TableGroup(Long id, List<Long> orderTableIds) {
        this.id = id;
        this.tableGroupOrderTableIds = TableGroupOrderTableIds.of(orderTableIds);
    }

    public static TableGroup generate(Long id, List<Long> orderTableIds) {
        return new TableGroup(id, orderTableIds);
    }

    public static TableGroup create(List<Long> orderTableIds, CreateTableGroupValidator createTableGroupValidator) {
        createTableGroupValidator.validate(orderTableIds);
        return new TableGroup(orderTableIds);
    }

    public void ungroup(UnGroupTableGroupValidator tableGroupValidator) {
        tableGroupValidator.validate(getOrderTableIds());
        registerEvent(TableUnGroupedEvent.of(this));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return tableGroupOrderTableIds.getOrderTableIds();
    }
}

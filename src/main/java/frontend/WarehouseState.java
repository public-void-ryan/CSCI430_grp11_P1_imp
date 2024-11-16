package frontend;

import backend.*;

public abstract class WarehouseState {
    protected WarehouseContext warehouseContext;
    protected Warehouse warehouse;

    protected WarehouseState(WarehouseContext warehouseContext, Warehouse warehouse) {
        this.warehouseContext = warehouseContext;
        this.warehouse = warehouse;
    }

    public abstract void run();
}


# Warehouse Management System — API and Event Contracts

## 1. REST API conventions

- API version: v1
- Authentication: HTTP Basic
- Request/response format: JSON
- Resource identifiers: UUID
- Successful resource creation: HTTP 201
- Successful retrieval/update: HTTP 200
- Missing resource: HTTP 404
- Validation failure: HTTP 400
- Duplicate resource or invalid operation: HTTP 409, where configured

## 2. Warehouse Service

| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/v1/warehouses | Create a warehouse |
| GET | /api/v1/warehouses/{id} | Retrieve a warehouse |

## 3. Location and Bin Service

| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/v1/locations | Create a location |
| GET | /api/v1/locations/{id} | Retrieve a location |
| GET | /api/v1/warehouses/{warehouseId}/locations | List locations in a warehouse |
| POST | /api/v1/bins | Create a bin |
| GET | /api/v1/bins/{id} | Retrieve a bin |
| GET | /api/v1/locations/{locationId}/bins | List bins in a location |

## 4. Inbound Service

| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/v1/goods-receipts | Create a goods receipt |
| GET | /api/v1/goods-receipts/{id} | Retrieve a goods receipt |

The create request supports multiple receipt lines.

## 5. Put-Away Service

| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/v1/putaway/tasks | Create a put-away task |
| GET | /api/v1/putaway/tasks/{id} | Retrieve a put-away task |
| POST | /api/v1/putaway/tasks/{id}/complete | Complete a put-away task |

A task must reference an eligible goods receipt line and a valid target bin.
A completed or cancelled task cannot be completed again.

## 6. Inventory Service

| Method | Endpoint | Purpose |
|---|---|---|
| GET | /api/v1/inventory/{skuId} | Retrieve inventory by SKU |
| GET | /api/v1/inventory/sku/{skuId}/warehouse/{warehouseId} | Retrieve inventory by SKU and warehouse |
| GET | /api/v1/inventory/sku/{skuId}/warehouse/{warehouseId}/bin/{binId} | Retrieve a specific inventory balance |
| GET | /api/v1/inventory/warehouse/{warehouseId} | Retrieve inventory by warehouse |

## 7. Kafka events

### GoodsReceived
- Producer: inbound-service
- Topic: goods-received
- Consumer: putaway-service
- Purpose: Make received goods lines eligible for put-away.

### PutAwayCompleted
- Producer: putaway-service
- Topic: putaway-completed
- Consumer: inventory-service
- Purpose: Add put-away quantities to the target-bin inventory balance.

### InventoryUpdated
- Producer: inventory-service
- Topic: inventory-updated
- Purpose: Notify downstream consumers that inventory has changed.


## Required event envelope

Every Kafka message uses this structure:

- eventId: unique event UUID used for idempotency
- eventType: event name
- eventVersion: schema version
- occurredAt: event creation timestamp
- source: producing service
- correlationId: identifier linking related operations
- entityId: identifier of the affected business entity
- payload: event-specific business data

The metadata fields must be at the top level of the message.
The payload must contain business data and must not duplicate
the top-level event metadata.

Topics:

- goods-received: published by inbound-service
- putaway-completed: published by putaway-service
- inventory-updated: published by inventory-service


## 9. Event-processing rules

- Consumers must not apply the same event twice.
- Inventory balance and movement records must remain consistent.
- Failed event processing must support retries and dead-letter handling.
- The outbox record and associated business update must be committed
  transactionally.
- Services must not access another service's database directly.

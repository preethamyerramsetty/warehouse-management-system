# WMS Kafka Event Contracts

## 1. Common event envelope

All WMS domain events use the following envelope:

| Field | Type | Description |
|---|---|---|
| eventId | UUID | Unique event identifier |
| eventType | String | Event name |
| eventVersion | Integer | Contract version, starting at 1 |
| occurredAt | LocalDateTime | Time the event occurred |
| source | String | Service that created the event |
| correlationId | String | Identifier used to trace a workflow |
| entityId | String | Identifier of the main entity |
| payload | Object | Event-specific business data |

Consumers must use `eventId` to identify duplicate deliveries. The `correlationId` should be propagated to logs and downstream events.

## 2. GoodsReceived

**Producer:** `inbound-service`

**Consumer:** `putaway-service`

**Kafka topic:** `goods-received`

**Purpose:** Notifies the put-away service that goods have been received.

Payload fields:

- `goodsReceiptId`: UUID of the goods receipt
- `warehouseId`: UUID of the warehouse
- `receiptNumber`: Receipt number
- `lines`: Received lines containing `goodsReceiptLineId`, `skuId`, and `quantity`

Example:

```json
{
  "eventId": "11111111-1111-4111-8111-111111111111",
  "eventType": "GoodsReceived",
  "eventVersion": 1,
  "occurredAt": "2026-10-28T12:00:00",
  "source": "inbound-service",
  "correlationId": "example-correlation-id",
  "entityId": "22222222-2222-4222-8222-222222222222",
  "payload": {
    "goodsReceiptId": "22222222-2222-4222-8222-222222222222",
    "warehouseId": "33333333-3333-4333-8333-333333333333",
    "receiptNumber": "GR-1001",
    "lines": [
      {
        "goodsReceiptLineId": "44444444-4444-4444-8444-444444444444",
        "skuId": "55555555-5555-4555-8555-555555555555",
        "quantity": 10
      }
    ]
  }
}
```

## 3. PutAwayCompleted

**Producer:** `putaway-service`

**Consumer:** `inventory-service`

**Kafka topic:** `putaway-completed`

**Purpose:** Notifies inventory that a put-away task has been completed.

Payload fields:

- `putAwayTaskId`: UUID of the completed task
- `taskNumber`: Task number
- `goodsReceiptLineId`: UUID of the receipt line
- `skuId`: UUID of the item
- `quantity`: Quantity put away
- `warehouseId`: UUID of the warehouse
- `targetBinId`: UUID of the destination bin

The inventory service must process each event idempotently so duplicate deliveries do not increase stock twice.

## 4. InventoryUpdated

**Producer:** `inventory-service`

**Kafka topic:** `inventory-updated`

**Purpose:** Publishes the resulting inventory balance and movement details for downstream consumers.

Payload fields:

- `skuId`: UUID of the item
- `warehouseId`: UUID of the warehouse
- `binId`: UUID of the bin
- `quantityChanged`: Quantity added or removed
- `availableQuantity`: Available balance after the change
- `movementType`: Type of inventory movement
- `referenceId`: UUID of the related task or business transaction

The event is intended for downstream WMS, Control Tower, and Data consumers. Those applications are outside the scope of this implementation unless separately required.

## 5. Reliability requirements

- Persist outgoing events in the service's outbox as part of the business transaction.
- Publish pending outbox events to their configured Kafka topics.
- Consumers must detect duplicate event IDs and avoid repeating business changes.
- Processing failures must be retried according to the configured Kafka retry policy.
- Events that exhaust retries should be routed to the configured dead-letter topic.
- Preserve the correlation ID across the event flow.
- Increment `eventVersion` when introducing a new incompatible contract version.

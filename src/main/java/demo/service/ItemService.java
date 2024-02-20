package demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import demo.domain.Item;
import demo.exception.ItemNotFoundException;
import demo.rest.api.CreateItemRequest;
import demo.rest.api.GetItemResponse;
import demo.rest.api.UpdateItemRequest;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ItemService {

    private final Map<UUID, Item> itemStore = new HashMap<>();

    public UUID createItem(CreateItemRequest request) {
        Item item = new Item(UUID.randomUUID(), request.name());
        itemStore.put(item.id(), item);
        log.info("Item created with id: {}", item.id());
        return item.id();
    }

    public GetItemResponse getItem(UUID itemId) {
        Item item = itemStore.get(itemId);
        GetItemResponse getItemResponse;
        if(item != null) {
            log.info("Found item with id: {}", item.id());
            getItemResponse = new GetItemResponse(item.id(), item.name());
        } else {
            log.warn("Item with id: {} not found.", itemId);
            throw new ItemNotFoundException();
        }
        return getItemResponse;
    }

    public void updateItem(UUID itemId, UpdateItemRequest request) {
        Item item = itemStore.get(itemId);
        if(item != null) {
            log.info("Found item with id: " + itemId);
            Item updatedItem = new Item(item.id(), request.name());
            itemStore.put(item.id(), updatedItem);
            log.info("Item updated with id: {} - name: {}", itemId, request.name());
        } else {
            log.error("Item with id: {} not found.", itemId);
            throw new ItemNotFoundException();
        }
    }

    public void deleteItem(UUID itemId) {
        Item item = itemStore.get(itemId);
        if(item != null) {
            itemStore.remove(item.id());
            log.info("Deleted item with id: {}", item.id());
        } else {
            log.error("Item with id: {} not found.", itemId);
            throw new ItemNotFoundException();
        }
    }
}

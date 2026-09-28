package chnu.edu.lab1khrystafulova.controller;

import chnu.edu.lab1khrystafulova.model.Item;
import chnu.edu.lab1khrystafulova.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @author Khrystafulova
 * @project Lab1Khrystafulova
 * @class ItemRestController
 * @version 1.0.0
 * @since 28/09/2026
 */
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemRestController {

    private final ItemService itemService;

    @GetMapping
    public List<Item> getAll() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getOne(@PathVariable String id) {
        return itemService.getItem(id);
    }

    @PostMapping
    public Item create(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @PutMapping
    public Item update(@RequestBody Item item) {
        return itemService.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        itemService.deleteItem(id);
    }
}
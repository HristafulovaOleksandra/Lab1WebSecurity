package chnu.edu.lab1khrystafulova.controller;

import chnu.edu.lab1khrystafulova.model.Item;
import chnu.edu.lab1khrystafulova.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // видаляти може тільки SUPERADMIN
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        itemService.deleteItem(id);
    }

    //------------------- JWT: hasAuthority замість hasRole -------------------
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/hello/admin")
    public String helloAdmin() { return "Hello, Admin!"; }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/hello/user")
    public String helloUser() { return "Hello, User!"; }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERADMIN')")
    @GetMapping("/hello/man")
    public String helloMan() { return "Hello, man!"; }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping("/hello/superadmin")
    public String helloSuperAdmin() { return "Hello, SuperAdmin!"; }

    // без анотації: доступ має будь-хто з валідним токеном
    @GetMapping("/hello/unknown")
    public String helloUnknown() { return "Hello, Unknown!"; }
}
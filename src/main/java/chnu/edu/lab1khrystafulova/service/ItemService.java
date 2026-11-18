package chnu.edu.lab1khrystafulova.service;

import chnu.edu.lab1khrystafulova.model.Item;
import chnu.edu.lab1khrystafulova.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    private final List<Item> items = new ArrayList<>();

    {
        items.add(new Item("1", "name1", "description1"));
        items.add(new Item("2", "name2", "description2"));
        items.add(new Item("3", "name3", "description3"));
    }

    @PostConstruct
    void init() {
        itemRepository.deleteAll();
        items.forEach(this::createItem);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItem(String id) {
        return itemRepository.findById(id).orElse(null);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        Item existing = itemRepository.findById(item.getId()).orElse(null);
        if (existing != null) {
            existing.setName(item.getName());
            existing.setDescription(item.getDescription());
            return itemRepository.save(existing);
        }
        return null;
    }

    // видалення: запис зникає з бази, тому "хто видалив" пишемо в лог (консоль)
    public void deleteItem(String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "unknown";

        Item item = getItem(id);
        log.info("AUDIT: {} was deleted by {}", item, username);

        itemRepository.deleteById(id);
    }
}
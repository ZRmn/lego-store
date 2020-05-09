package com.zrmn.web.controllers;

import com.zrmn.model.entities.*;
import com.zrmn.model.forms.ProductForm;
import com.zrmn.model.forms.SignUpForm;
import com.zrmn.model.forms.StockItemForm;
import com.zrmn.model.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private SignUpService signUpService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private WarehousesService warehousesService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CartService cartService;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/sign-up")
    public ResponseEntity signUpAdmin(@RequestBody SignUpForm signUpForm)
    {
        signUpService.signUpAdmin(signUpForm.getLogin(), signUpForm.getPassword());
        return ResponseEntity.ok("Signed up");
    }

    @GetMapping("/users")
    public ResponseEntity getUsers()
    {
        List<User> users = usersService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable Long id)
    {
        User user = usersService.get(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody User user)
    {
        usersService.update(user);
        return ResponseEntity.ok("User updated");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id)
    {
        usersService.delete(id);
        return ResponseEntity.ok("User deleted");
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUsers()
    {
        usersService.deleteAll();
        return ResponseEntity.ok("Users deleted");
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity markUserAsBanned(@PathVariable Long id)
    {
        usersService.setState(id, User.State.BANNED);
        return ResponseEntity.ok("User marked as banned");
    }

    @PutMapping("/users/{id}/delete")
    public ResponseEntity markUserAsDeleted(@PathVariable Long id)
    {
        usersService.setState(id, User.State.DELETED);
        return ResponseEntity.ok("User marked as deleted");
    }

    @PutMapping("/users/{id}/authorities")
    public ResponseEntity setUserAuthorities(@PathVariable Long id, @RequestBody List<Authority> authorities)
    {
        usersService.setAuthorities(id, authorities);
        return ResponseEntity.ok("User authorities updated");
    }

    @GetMapping("/products")
    public ResponseEntity getProducts()
    {
        List<Product> products = productsService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProduct(@PathVariable Long id)
    {
        Product product = productsService.get(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping(path = "/products", consumes = "multipart/form-data")
    public ResponseEntity addProduct(@ModelAttribute ProductForm productForm)
    {
        List<MultipartFile> images = productForm.getImages();
        List<String> imageUrls = images.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(multipartFile -> {
                    String path = "/" + productForm.getCategory() + "/"
                            + productForm.getArticle() + "/"
                            + multipartFile.getOriginalFilename();

                    fileStorageService.store(uploadPath + path, multipartFile);

                    return path;
                })
                .collect(Collectors.toList());

        Product product = Product.builder()
                .title(productForm.getTitle())
                .article(productForm.getArticle())
                .category(productForm.getCategory())
                .description(productForm.getDescription())
                .pieces(productForm.getPieces())
                .price(productForm.getPrice())
                .releaseDate(productForm.getReleaseDate())
                .imageUrls(imageUrls)
                .build();

        productsService.save(product);
        return ResponseEntity.ok("Product added");
    }

    @PutMapping("/products")
    public ResponseEntity updateProduct(@RequestBody Product product)
    {
        productsService.update(product);
        return ResponseEntity.ok("Product updated");
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id)
    {
        productsService.delete(id);
        return ResponseEntity.ok("Product deleted");
    }

    @DeleteMapping("/products")
    public ResponseEntity deleteProducts()
    {
        productsService.deleteAll();
        return ResponseEntity.ok("Products deleted");
    }

    @GetMapping("/warehouses")
    public ResponseEntity getWarehouses()
    {
        List<Warehouse> warehouses = warehousesService.getAll();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/warehouses/{id}")
    public ResponseEntity getWarehouse(@PathVariable Long id)
    {
        Warehouse warehouse = warehousesService.get(id);
        return ResponseEntity.ok(warehouse);
    }

    @PostMapping("/warehouses")
    public ResponseEntity addWarehouse(@RequestBody Warehouse warehouse)
    {
        warehousesService.save(warehouse);
        return ResponseEntity.ok("Warehouse added");
    }

    @PutMapping("/warehouses")
    public ResponseEntity updateWarehouse(@RequestBody Warehouse warehouse)
    {
        warehousesService.update(warehouse);
        return ResponseEntity.ok("Warehouse updated");
    }

    @DeleteMapping("/warehouses/{id}")
    public ResponseEntity deleteWarehouse(@PathVariable Long id)
    {
        warehousesService.delete(id);
        return ResponseEntity.ok("Warehouse deleted");
    }

    @DeleteMapping("/warehouses")
    public ResponseEntity deleteWarehouses()
    {
        warehousesService.deleteAll();
        return ResponseEntity.ok("Warehouses deleted");
    }

    @GetMapping("/warehouses/stock")
    public ResponseEntity getStockItems()
    {
        List<StockItem> stockItems = stockService.getAll();
        return ResponseEntity.ok(stockItems);
    }

    @GetMapping("/warehouses/{id}/stock")
    public ResponseEntity getStockItemsFromWarehouse(@PathVariable Long id)
    {
        Warehouse warehouse = warehousesService.get(id);
        List<StockItem> stockItems = stockService.getAll(warehouse);
        return ResponseEntity.ok(stockItems);
    }

    @GetMapping("/warehouses/stock/{id}")
    public ResponseEntity getStockItem(@PathVariable Long id)
    {
        StockItem stockItem = stockService.get(id);
        return ResponseEntity.ok(stockItem);
    }

    @PostMapping("/warehouses/{id}/stock")
    public ResponseEntity addStockItem(@PathVariable Long id, @RequestBody StockItemForm stockItemForm)
    {
        Warehouse warehouse = warehousesService.get(id);
        Product product = productsService.get(stockItemForm.getProductId());
        int quantity = stockItemForm.getQuantity();

        StockItem stockItem = stockService.addToStock(warehouse, product, quantity);
        return ResponseEntity.ok(stockItem);
    }

    @PutMapping("/warehouses/stock")
    public ResponseEntity updateStockItem(@RequestBody StockItem stockItem)
    {
        stockService.update(stockItem);
        return ResponseEntity.ok("Stock item updated");
    }

    @DeleteMapping("/warehouses/stock/{id}")
    public ResponseEntity deleteStockItem(@PathVariable Long id)
    {
        stockService.delete(id);
        return ResponseEntity.ok("Stock item deleted");
    }

    @DeleteMapping("/warehouses/stock")
    public ResponseEntity deleteStockItems()
    {
        stockService.deleteAll();
        return ResponseEntity.ok("Stock items deleted");
    }

    @DeleteMapping("/warehouses/{id}/stock")
    public ResponseEntity deleteStockItemsFromWarehouse(@PathVariable Long id)
    {
        Warehouse warehouse = warehousesService.get(id);
        stockService.deleteAll(warehouse);
        return ResponseEntity.ok("Stock items from warehouse deleted");
    }

    @GetMapping("/orders")
    public ResponseEntity getOrders()
    {
        List<Order> orders = ordersService.getAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity getOrder(@PathVariable Long id)
    {
        Order order = ordersService.get(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders")
    public ResponseEntity updateOrder(@RequestBody Order order)
    {
        ordersService.update(order);
        return ResponseEntity.ok("Order updated");
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity deleteOrder(@PathVariable Long id)
    {
        ordersService.delete(id);
        return ResponseEntity.ok("Order deleted");
    }

    @DeleteMapping("/orders")
    public ResponseEntity deleteOrders()
    {
        ordersService.deleteAll();
        return ResponseEntity.ok("Orders deleted");
    }

    @DeleteMapping("/orders/customers/{id}")
    public ResponseEntity deleteCustomerOrders(@PathVariable Long id)
    {
        Customer customer = (Customer) usersService.get(id);
        ordersService.deleteAll(customer);
        return ResponseEntity.ok("Customer orders deleted");
    }

    @PutMapping("/orders/{id}/process")
    public ResponseEntity markOrderAsProcessed(@PathVariable Long id)
    {
        ordersService.setStatus(id, Order.Status.PROCESSING);
        return ResponseEntity.ok("Order marked as processing");
    }

    @PutMapping("/orders/{id}/deliver")
    public ResponseEntity markOrderAsDelivered(@PathVariable Long id)
    {
        ordersService.setStatus(id, Order.Status.DELIVERED);
        return ResponseEntity.ok("Order marked as delivered");
    }

    @PostMapping("/orders/{id}/write-off")
    public ResponseEntity writeOffOrder(@PathVariable Long id)
    {
        Order order = ordersService.get(id);
        List<Warehouse> writeOffMap = warehousesService.collect(order);
        stockService.writeOff(writeOffMap);
        return ResponseEntity.ok("Stock items wrote off");
    }

    @GetMapping("/orders/{id}/collect")
    public ResponseEntity collectOrder(@PathVariable Long id)
    {
        Order order = ordersService.get(id);
        List<Warehouse> collectMap = warehousesService.collect(order);
        return ResponseEntity.ok(collectMap);
    }

    @DeleteMapping("/carts")
    public ResponseEntity deleteCarts()
    {
        cartService.deleteAll();
        return ResponseEntity.ok("Carts deleted");
    }
}

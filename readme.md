<style>
    .pic-container {
        display: flex;
        justify-content: center;
    }

    .desc {
        text-align: center;
    }
</style>

# Drive Wheels Deals

>Kacper Szot, Jakub Rękas, Tomasz Grzybacz

Projekt oparty jest o Spring Freamwork wraz z ORMem Hibernate. Użyta baza danych to PostgreSQL.
Tematem projektu jest oprogramowanie obsługujące sklep sprzedający samochody i opony.

<div class="pic-container">
    <img src="./img/dbDiagram.png">
</div>

<p class="desc"><b>Zdj. 1</b> - diagram bazy danych</p>


## Model
### User
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "shop_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```
<p class="desc"><b>Kod 1</b> - klasa <code>User</code></p>

Klasa zawiera podstawowe informacje o użytkownikach wspólne dla klientów i administratorów:
- Imię
- Nazwisko
- Email
- Hasło

Pole `ID` jest kluczem głównym tabeli.

### Customer
```java
@Entity
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String phone;
    private String zipCode;
    private String city;
    private String street;
    private String country;
    @ManyToMany
    private List<Product> basket;


    public Customer() {
    }
    
    public Customer(String firstName, String lastName, String email, String password, String phone, String zipCode, String city, String street, String country) {
        super(firstName, lastName, email, password);
        this.phone = phone;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.country = country;
    }

    public List<Product> getBasket() {
        return basket;
    }

    public void setBasket(List<Product> basket) {
        this.basket = basket;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
```
<p class="desc"><b>Kod 2</b> - kod klasy <code>Customer</code></p>

Klasa dziedziczy z klasy `User`, zawiera informacje o klientach:
- Numer telefonu
- Kod pocztowy
- Ulica
- Miasto
- Kraj

Elementem klasy jest również lista, która zawiera informacje o produktach w koszyku użytknownika.

Kluczem głównym jest pole `ID`, które jest jednocześnie kluczem obcy do tabeli `User`
odnoszącym się do pola `ID`.

### Administrator
```java
@Entity
public class Administrator extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public Administrator() {
    }

    public Administrator(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
```
<p class="desc"><b>Kod 3</b> - kod klasy <code>Administrator</code></p>

Klasa dziedziczy z klasy `User`, zawiera informacje o użytkownikach, którzy są administratorami.

Kluczem głównym jest pole `ID`, które jest jednocześnie kluczem obcym do tabeli `User`.

### Product
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private BigDecimal price;

    private int quantityInStock;

    public Product() {
    }

    public Product(BigDecimal price, int quantityInStock) {
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
```
<p class="desc"><b>Kod 4</b> - kod klasy <code>Product</code></p>

Klasa `Product` zawiera podstawowe informacje o produktach - cenę oraz ilość w magazynie.

Kluczem głównym jest pole `ID`.

### Car
```java
@Entity
public class Car extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    String brand;
    String model;
    int year;


    public Car(String brand, String model, int year, BigDecimal price,int quantityInStock) {
        super(price,quantityInStock);
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
    public Car() {}

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
```
<p class="desc"><b>Kod 5</b> - kod klasy <code>Car</code></p>

Klasa zawiera informacje o samochodach:
- Marka
- Model
- Rok produkcji

Klasa dziedziczy z klasy `Product`

Kluczem głównym i obcym do klasy `Product` jest pole `ID`

### Tire
```java
@Entity
public class Tire extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    String brand;
    String size;

    public Tire() {
    }

    public Tire(String brand, String size, BigDecimal price,int quantityInStock) {
        super(price, quantityInStock);
        this.brand = brand;
        this.size = size;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
```
<p class="desc"><b>Kod 6</b> - kod klasy <code>Tire</code></p>

Klasa zawiera informacje o oponach:
- Marka
- Rozmiar

Klasa dziedziczy z klasy `Product`. Pole `ID` jest kluczem głównym oraz kluczem obcym
do tabeli `Product`.

### Order
```java
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;
    private Timestamp orderDate;
    private BigDecimal totalDiscount;

    public Order() {
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Order(Customer customer, List<OrderItem> items, Timestamp orderDate, BigDecimal totalDiscount) {
        this.customer = customer;
        this.items = items;
        this.orderDate = orderDate;
        this.totalDiscount = totalDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
```
<p class="desc"><b>Kod 7</b> - kod klasy <code>Order</code></p>

Klasa zawiera informacje o zamówieniach:
- Całkowita zniżka na zamówienie
- Data zamówienia
- Klient składający zamówienie

Kluczem głównym jest pole `ID`.
Pole `customer_id` jest kluczem obcym do tabeli `Customer`.

### OrderItem
```java
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public OrderItem() {
    }

    public OrderItem(Product product, Order order, BigDecimal unitPrice, BigDecimal discount) {
        this.product = product;
        this.order = order;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    @ManyToOne
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private BigDecimal unitPrice;
    private BigDecimal discount;

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getDiscount() {
        return discount;
    }
}
```
<p class="desc"><b>Kod 8</b> - kod klasy <code>OrderItem</code></p>

Klasa zawiera informacje o zamówionych produktach
- Cena za produkt
- Zniżka na daną sztukę produktu
- Informacja o produkcie
- Informacja o zamówieniu

Pole `ID` jest kluczem głównym.
Pole `order_id` jest kluczem obcym do tabeli `Order`, a pole `product_id` do tabeli `Product`.

## Endpoints

### POST /login
Request
```java
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
```

Response
```java
public class LoginResponse {
    private String token;
}
```

Zwraca token JWT gdy dane logowania są poprawne, w przeciwnym razie rzuca `BadCredentialsException`

### POST /register

Request
```java
public class RegisterRequest {
    @NotBlank(message = "Country is required")

    public String country;
    @NotBlank(message = "Email is required")
    public String email;
    @NotBlank(message = "Password is required")

    public String password;
    @NotBlank(message = "City is required")

    public String city;
    @NotBlank(message = "Street is required")

    public String street;
    @NotBlank(message = "Zip code is required")
    public String zipCode;
    @NotBlank(message = "Phone is required")
    public String phone;
    @NotBlank(message = "Firstname is required")
    public String firstName;
    @NotBlank(message = "lastname is required")

    public String lastName;
}
```

Response
```java
public class RegisterResponse {
    public String country;
    public String email;
    public String city;
    public String street;
    public String zipCode;
    public String phone;
    public String firstName;
    public String lastName;
}
```

Tworzy nowego użytkownika. Jeśli wszystkie dane są poprawne zwraca je (bez hasła).
W przeciwnym wypadku `BadRequestError`

### PUT /user/profile

Request
```java
public class EditProfileRequest {
    public String country;
    public String city;
    public String street;
    public String zipCode;
    public String phone;
    public String firstName;
    public String lastName;
}
```

Response
```java
public class EditProfileResponse {
    public String country;
    public String city;
    public String street;
    public String zipCode;
    public String phone;
    public String firstName;
    public String lastName;
}
```

Zmienia dane zalogowanego użytkownika. Jeśli użytkownik nie jest zalogowany lub jest zalogowany
jako administrator rzuca `BadRequestException`

### GET /user/profile

Response
```java
public class EditProfileResponse {
    public String country;
    public String city;
    public String street;
    public String zipCode;
    public String phone;
    public String firstName;
    public String lastName;
}
```

Zwraca informacje o profilu.

### GET /order

Response
```java
public class ListOrdersResponse {
    public List<Order> orders;
}
```

Zwraca listę zamówień dla aktualnie zalogowanego użytkownika. W przeciwnym wypadku `BadRequestException`

### GET /order/admin

Response:  
Lista wszystkich zamówień `Iterable<Order>`

Zwraca listę wszystkich zamówień. Jedynie admin może korzystać z tego endpointa.
W przypadku niepowodzenia `BadRequestException`.

### POST /order/make

Response
```java
public class CreateOrderResponse {
    public String status;
}
```

Tworzy zamówienie na podstawie koszyka zalogowanego użytkownika. W przypadku niepowodzenia `BadRequestException`

### GET /product

Response  
Lista produktów w postaci `List<Product>`

Zwraca listę wszystkich produktów.

### GET /product/{id}

Response  
Produkt w postaci`Product`.

Wyszukuje produkt po `ID`.

### POST /product/admin/add

Request
```java
public class ProductOperationRequest {
    @NotBlank(message = "Product type is required")
    public String productType;
    public BigDecimal price;
    public Long id;
    public String brand;
    public String model;
    public Integer year;
    public String size;
}
```

Response
```java
public class ProductCreateEditResponse {

    public Long id;
    public BigDecimal price;
    public String brand;
    public String model;
    public Integer year;
    public String size;
}
```

Dodaje produkt. Możliwe do wykonania jedynie przez admina. W wypadku niepowodzenia
`BadRequestException`

### POST /product/admin/edit

Request
```java
public class ProductOperationRequest {
    @NotBlank(message = "Product type is required")
    public String productType;
    public BigDecimal price;
    public Long id;
    public String brand;
    public String model;
    public Integer year;
    public String size;
}
```

Response
```java
public class ProductCreateEditResponse {

    public Long id;
    public BigDecimal price;
    public String brand;
    public String model;
    public Integer year;
    public String size;
}
```
Edytuje istniejący produkt. Możliwe do wykonania jedynie przez admina. W wypadku niepowodzenia
`BadRequestException`

### POST /product/admin/delete

Request
```java
public class ProductOperationRequest {
    @NotBlank(message = "Product type is required")
    public String productType;
    public BigDecimal price;
    public Long id;
    public String brand;
    public String model;
    public Integer year;
    public String size;
}
```

Response
```java
public class ProductRemoveResponse {
    public String status;
}
```

Usuwa produkt. Jedynie dla admina. W wypadku niepowodzenia `BadRequestException`

### GET /basket

Response  
Lista produktów w postaci `List<Product>`.

Zwraca koszyk aktualnie zalogowanego użytkownika. W wypadku niepowodzenia `BadRequestException`.

### POST /basket/add/{id}

Response  
Lista produktów w postaci `List<Product>`

Dodaje element o zadanym `ID` do koszyka, a nastepnie zwraca uaktualniony koszyk. W wypadku niepowodzenia `BadRequestException`.

### DELETE /basket/remove/{id}

Response  
Lista produktów w postaci `List<Product>`

Usuwa element z koszyka, zwraca uaktualniony koszyk. W wypadku niepowodzenia `BadRequestException`.

### GET /report/cars-sold-by-brand

Response  
Lista w postaci `List<BrandsSold>` zawierająca informację o ilości sprzedanych aut dla każdej marki.

### GET /report/cars-sold-to-country/{country}

Response  
Lista postawci `List<SoldToCountry>`.

Zwraca informacje o ilości sprzedanych aut do zadanego kraju.

### GET /report/tires-sold-to-country/{country}

Response  
Lista postaci `List<SoldToCountry>`

Zwraca informacje o ilości sprzedanych opon do zadanego kraju.

### GET /report/customers-per-country

Response  
Lista postaci `List<CustomersPerCountry>`

Zwraca informacje o ilości klientów dla każdego kraju.

### GET /report/income

Response  
`BigDecimal`

Zwraca informacje o całkowitym uzyskanym dochodzie.

### GET /report/type-sold

Response  
Lista postaci `List<TypeSold>`

Zwraca informacje o ilości sprzedanego towaru dla każdego typu.











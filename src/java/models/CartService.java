package models;

public class CartService {

    private Cart cart;
    private int cartId;
    private Service service;
    private int quantity;
    private int priceAtTime;

    public CartService() {
    }

    public CartService(Cart cart, Service service, int quantity) {
        this.cart = cart;
        this.service = service;
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getPriceAtTime() {
        return priceAtTime;
    }
    public void setPriceAtTime(int priceAtTime) {
        this.priceAtTime = priceAtTime;
    }
    
    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
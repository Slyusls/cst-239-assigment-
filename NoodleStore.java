package store;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Noodle {
    private String name;
    private String description;
    private double price;
    private int quantity;

    public Noodle(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class InventoryManager {
    private Map<String, Noodle> inventory;

    public InventoryManager(Map<String, Noodle> inventory) {
        this.inventory = inventory;
    }

    public void addNoodle(Noodle noodle) {
        inventory.put(noodle.getName(), noodle);
    }

    public Noodle getNoodle(String name) {
        return inventory.get(name);
    }

    public Map<String, Noodle> getInventory() {
        return inventory;
    }
}

class StoreFront {
    private InventoryManager inventoryManager;
    private Map<String, Integer> cart;

    public StoreFront(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.cart = new HashMap<>();
    }

    public Map<String, Integer> getCart() {
        return cart;
    }

    public void initializeStore() {
        Noodle ramenNoodle = new Noodle("Ramen", "Japanese noodle soup", 5.99, 100);
        Noodle spaghetti = new Noodle("Spaghetti", "Italian pasta", 7.99, 80);
        Noodle udonNoodle = new Noodle("Udon", "Thick wheat noodle", 6.49, 120);
        Noodle sobaNoodle = new Noodle("Soba", "Japanese buckwheat noodle", 8.99, 60);
        Noodle riceNoodle = new Noodle("Rice Noodle", "Thin rice noodle", 4.99, 150);

        inventoryManager.addNoodle(ramenNoodle);
        inventoryManager.addNoodle(spaghetti);
        inventoryManager.addNoodle(udonNoodle);
        inventoryManager.addNoodle(sobaNoodle);
        inventoryManager.addNoodle(riceNoodle);
    }

    public void purchaseNoodle(String noodleName, int quantity) {
        Noodle noodle = inventoryManager.getNoodle(noodleName);

        if (noodle != null && noodle.getQuantity() >= quantity) {
            cart.put(noodleName, cart.getOrDefault(noodleName, 0) + quantity);
            noodle.setQuantity(noodle.getQuantity() - quantity);
            System.out.println(noodleName + " added to cart.");
        } else {
            System.out.println(noodleName + " not available or insufficient quantity.");
        }
    }

    public void cancelPurchase(String noodleName, int quantity) {
        Noodle noodle = inventoryManager.getNoodle(noodleName);

        if (noodle != null && cart.containsKey(noodleName)) {
            cart.put(noodleName, cart.get(noodleName) - quantity);
            if (cart.get(noodleName) <= 0) {
                cart.remove(noodleName);
            }
            noodle.setQuantity(noodle.getQuantity() + quantity);
            System.out.println("Purchase of " + noodleName + " canceled.");
        } else {
            System.out.println("No purchase found for " + noodleName + ".");
        }
    }

    public void viewCart() {
        System.out.println("Your Shopping Cart:");

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String noodleName = entry.getKey();
            int quantity = entry.getValue();
            Noodle noodle = inventoryManager.getNoodle(noodleName);

            if (noodle != null) {
                System.out.println(noodleName + " - $" + noodle.getPrice() + " x " + quantity);
            }
        }

        double total = cart.entrySet().stream()
                .mapToDouble(entry -> {
                    Noodle noodle = inventoryManager.getNoodle(entry.getKey());
                    return noodle.getPrice() * entry.getValue();
                })
                .sum();

        System.out.println("Total price in the shopping cart: $" + total);
    }

    public void checkout() {
        System.out.println("Checking out...");

        // Process the checkout logic here
        // For simplicity, let's just print the items in the cart and the total amount
        viewCart();

        // Clear the cart after checkout
        cart.clear();

        System.out.println("Thank you for your purchase!");
    }
}

public class NoodleStore {
    public static void main(String[] args) {
        Map<String, Noodle> inventory = new HashMap<>();
        InventoryManager inventoryManager = new InventoryManager(inventory);
        StoreFront storeFront = new StoreFront(inventoryManager);

        // Initialize the store with some noodles
        storeFront.initializeStore();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. View Noodles");
            System.out.println("2. Purchase Noodle");
            System.out.println("3. View Cart");
            System.out.println("4. Cancel Purchase");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Available Noodles:");
                    inventoryManager.getInventory().forEach((name, noodle) ->
                            System.out.println(name + " - $" + noodle.getPrice() + " (" + noodle.getQuantity() + " available)"));
                    break;
                case 2:
                    System.out.println("Enter the name of the noodle you want to purchase:");
                    String purchaseName = scanner.next();
                    System.out.println("Enter the quantity:");
                    int purchaseQuantity = scanner.nextInt();
                    storeFront.purchaseNoodle(purchaseName, purchaseQuantity);
                    break;
                case 3:
                    storeFront.viewCart();
                    break;
                case 4:
                    System.out.println("Enter the name of the noodle to cancel the purchase:");
                    String cancelName = scanner.next();
                    System.out.println("Enter the quantity to cancel:");
                    int cancelQuantity = scanner.nextInt();
                    storeFront.cancelPurchase(cancelName, cancelQuantity);
                    break;
                case 5:
                    storeFront.checkout();
                    break;
                case 6:
                    System.out.println("Exiting the Noodle Store. Thank you for shopping!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

package Contacts;

import org.w3c.dom.ls.LSOutput;
import util.Input;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ContactManager {
    private static List<Contacts> contacts = new ArrayList<>();
    private static final String FILE_PATH = "./src/contacts.txt";

    public static void main(String[] args) {
        loadContacts();
        welcome();

        Input scanner = new Input();
        int choice;

        do {
            choice = displayMainMenu(scanner);

            switch (choice) {
                case 1 -> viewContacts();
                case 2 -> {
                    addContact(scanner);
                    saveContacts();
                    addingContactArt();
                }
                case 3 -> {
                    searchingContactArt();
                    searchContact(scanner);
                }
                case 4 -> {
                    deleteContact(scanner);
                    deletingContactArt();
                    saveContacts();
                }
                case 5 -> {
                    saveContacts();
                    exitApp();
                    System.out.println("Exiting the application.");
                }
                default -> System.out.println("Invalid choice. Please choose a valid option.");
            }
        } while (choice != 5);
    }

    private static int displayMainMenu(Input scanner) {
        System.out.println("+===================+\n|     MAIN MENU     |\n+===================+");
        System.out.println("1. View contacts.");
        System.out.println("2. Add a new contact.");
        System.out.println("3. Search a contact by name.");
        System.out.println("4. Delete an existing contact.");
        System.out.println("5. Exit.");
        System.out.print("Enter an option (1, 2, 3, 4, or 5): ");

        int choice = scanner.getInt();

        if (choice == 1) {
            simulateTyping("Loading... View contacts...\n", 100, 150); // Simulate typing for "View contacts" option
        }

        return choice;
    }

    public static void simulateTyping(String text, int minDelay, int maxDelay) {
        Random random = new Random();
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            int delay = random.nextInt(maxDelay - minDelay + 1) + minDelay; // Generate random delay
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String name = parts[0].trim();
                String phoneNumber = parts[1].trim();
                String email = parts[2].trim();
                contacts.add(new Contacts(name, phoneNumber, email));
            }
        } catch (IOException e) {
            System.out.println("Error reading contacts file: " + e.getMessage());
        }
    }

    private static void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Contacts contact : contacts) {
                String formattedPhoneNumber = formatPhoneNumber(contact.getPhoneNumber()); // Format phone number
                writer.write(contact.getName() + " | " + formattedPhoneNumber + " | " + contact.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing contacts file: " + e.getMessage());
        }
    }

    private static String formatPhoneNumber(String phoneNumber) {
        // Assuming phoneNumber is a string of digits without dashes
        phoneNumber = phoneNumber.replace("-", "");
        return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
    }

    private static void viewContacts() {
        System.out.println("===============================================================================");
        System.out.println("|          NAME           |       PHONE NUMBER      |          EMAIL          |");
        System.out.println("===============================================================================");
        for (Contacts contact : contacts) {
            String formattedPhoneNumber = formatPhoneNumber(contact.getPhoneNumber()); // Format phone number
            System.out.printf("|%-25s|%-25s|%-25s|\n", contact.getName(), formattedPhoneNumber, contact.getEmail());
            System.out.println("+-------------------------+-------------------------+-------------------------+");
        }
        System.out.println();
    }

    private static void addContact(Input scanner) {
        System.out.print("Enter the name: ");
        String name = scanner.getString();

        // Check if the name already exists
        Contacts existingContact = null;
        for (Contacts contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                existingContact = contact;
                break;
            }
        }

        if (existingContact != null) {
            System.out.println("A contact with the same name already exists.");
            System.out.print("Do you want to override the existing contact? (yes/no): ");
            boolean overrideChoice = scanner.yesNo();
            if (overrideChoice) {
                // Remove the existing contact
                contacts.remove(existingContact);
                System.out.println("Existing contact overridden.");
            } else {
                System.out.println("Contact not overridden.");
                return; // Exit the method without adding the contact
            }
        }
        String phoneNumber = "";
        do {
            System.out.print("Enter the phone number (include area code): ");
            phoneNumber = scanner.getString();
        } while (phoneNumber.length() < 10);
        System.out.print("Enter the email: ");
        String email = scanner.getString();

        contacts.add(new Contacts(name, phoneNumber, email));
        System.out.println("Contact added successfully!");
        saveContacts();
    }

    private static void searchContact(Input scanner) {
        System.out.print("Enter the name to search: ");
        String searchName = scanner.getString().toLowerCase();
        boolean found = false;
        System.out.println("===============================================================================");
        System.out.println("|          NAME           |       PHONE NUMBER      |          EMAIL          |");
        System.out.println("===============================================================================");
        for (Contacts contact : contacts) {
            if (contact.getName().toLowerCase().contains(searchName)) {
                String formattedPhoneNumber = formatPhoneNumber(contact.getPhoneNumber());
                System.out.printf("|%-25s|%-25s|%-25s|\n", contact.getName(), formattedPhoneNumber, contact.getEmail());
                System.out.println("+-------------------------+-------------------------+-------------------------+");
                found = true;
            }
            System.out.println();
        }
        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    private static void deleteContact(Input scanner) {
        System.out.print("Enter the name to delete: ");
        String deleteName = scanner.getString().toLowerCase();
        Contacts contactToRemove = null;
        for (Contacts contact : contacts) {
            if (contact.getName().toLowerCase().contains(deleteName)) {
                contactToRemove = contact;
                break;
            }
        }
        if (contactToRemove != null) {
            contacts.remove(contactToRemove);
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found.");
        }
        saveContacts();
    }

    private static void addingContactArt() {
        String limeGreen = "\u001B[92m";
        String resetColor = "\u001B[0m";

        String art = """    
                    _        _      _  _                  ____               _                _  \s
                   / \\    __| |  __| |(_) _ __    __ _   / ___| ___   _ __  | |_  __ _   ___ | |_\s
                  / _ \\  / _` | / _` || || '_ \\  / _` | | |    / _ \\ | '_ \\ | __|/ _` | / __|| __|
                 / ___ \\| (_| || (_| || || | | || (_| | | |___| (_) || | | || |_| (_| || (__ | |_\s
                /_/   \\_\\\\__,_| \\__,_||_||_| |_| \\__, |  \\____|\\___/ |_| |_| \\__|\\__,_| \\___| \\__|
                                                 |___/                                           \s""";
        System.out.println(limeGreen + art + resetColor);
//        System.out.println(limeGreen + "Welcome to the Contact Management App!" + resetColor);

    }

    private static void searchingContactArt() {
        String blueColor = "\u001B[94m";
        String resetColor = "\u001B[0m";
        String art = """
                   _____                           __     _             \s
                  / ___/ ___   ____ _ _____ _____ / /_   (_)____   ____ _
                  \\__ \\ / _ \\ / __ `// ___// ___// __ \\ / // __ \\ / __ `/
                 ___/ //  __// /_/ // /   / /__ / / / // // / / // /_/ /\s
                /____/ \\___/ \\__,_//_/    \\___//_/ /_//_//_/ /_/ \\__, / \s
                                                                /____/  \s""";
        System.out.println(blueColor + art + resetColor);
    }

    private static void deletingContactArt() {
        String redColor = "\u001B[91m";
        String resetColor = "\u001B[0m";
        String art = """
                 ____            ___             __                             \s
                /\\  _`\\         /\\_ \\           /\\ \\__  __                      \s
                \\ \\ \\/\\ \\     __\\//\\ \\       __ \\ \\ ,_\\/\\_\\     ___       __    \s
                 \\ \\ \\ \\ \\  /'__`\\\\ \\ \\    /'__`\\\\ \\ \\/\\/\\ \\  /' _ `\\   /'_ `\\  \s
                  \\ \\ \\_\\ \\/\\  __/ \\_\\ \\_ /\\  __/ \\ \\ \\_\\ \\ \\ /\\ \\/\\ \\ /\\ \\L\\ \\ \s
                   \\ \\____/\\ \\____\\/\\____\\\\ \\____\\ \\ \\__\\\\ \\_\\\\ \\_\\ \\_\\\\ \\____ \\\s
                    \\/___/  \\/____/\\/____/ \\/____/  \\/__/ \\/_/ \\/_/\\/_/ \\/___L\\ \\
                                                                          /\\____/
                                                                          \\_/__/\s""";
        System.out.println(redColor + art + resetColor);
    }

    private static void exitApp() {
        String yellowColor = "\u001B[93m";
        String resetColor = "\u001B[0m";
        String art = """
                ████████╗██╗  ██╗ █████╗ ███╗   ██╗██╗  ██╗███████╗                            \s
                ╚══██╔══╝██║  ██║██╔══██╗████╗  ██║██║ ██╔╝██╔════╝                            \s
                   ██║   ███████║███████║██╔██╗ ██║█████╔╝ ███████╗                            \s
                   ██║   ██╔══██║██╔══██║██║╚██╗██║██╔═██╗ ╚════██║                            \s
                   ██║   ██║  ██║██║  ██║██║ ╚████║██║  ██╗███████║▄█╗                         \s
                   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝╚═╝                         \s
                ███████╗███╗   ██╗██████╗ ██╗███╗   ██╗ ██████╗          █████╗ ██████╗ ██████╗\s
                ██╔════╝████╗  ██║██╔══██╗██║████╗  ██║██╔════╝         ██╔══██╗██╔══██╗██╔══██╗
                █████╗  ██╔██╗ ██║██║  ██║██║██╔██╗ ██║██║  ███╗        ███████║██████╔╝██████╔╝
                ██╔══╝  ██║╚██╗██║██║  ██║██║██║╚██╗██║██║   ██║        ██╔══██║██╔═══╝ ██╔═══╝\s
                ███████╗██║ ╚████║██████╔╝██║██║ ╚████║╚██████╔╝        ██║  ██║██║     ██║██╗ \s
                ╚══════╝╚═╝  ╚═══╝╚═════╝ ╚═╝╚═╝  ╚═══╝ ╚═════╝         ╚═╝  ╚═╝╚═╝     ╚═╝╚═╝ \s
                                                                                               \s""";
        System.out.println(yellowColor + art + resetColor);
    }

    private static void welcome() {
        String magentaColor = "\u001B[95m";
        String resetColor = "\u001B[0m";
        String art = """
                 _     _  _______  ___      _______  _______  __   __  _______  __ \s
                | | _ | ||       ||   |    |       ||       ||  |_|  ||       ||  |\s
                | || || ||    ___||   |    |       ||   _   ||       ||    ___||  |\s
                |       ||   |___ | 
                  |    |       ||  | |  ||       ||   |___ |  |\s
                |       ||    ___||   |___ |      _||  |_|  ||       ||    ___||__|\s
                |   _   ||   |___ |       ||     |_ |       || ||_|| ||   |___  __ \s
                |__| |__||_______||_______||_______||_______||_|   |_||_______||__|\s""";
        System.out.println(magentaColor + art + resetColor);
    }
}

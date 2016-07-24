package com.example.dzhang.outsidehacks.dummy;

import com.example.dzhang.outsidehacks.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample username for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<User> ITEMS = new ArrayList<User>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, User> ITEM_MAP = new HashMap<String, User>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        addItem(new User("a", "Edward Zhang", "A cool person", null, null));
        addItem(new User("b", "Bill Gates", "Not as rich as Edward", null, null));
        addItem(new User("c", "Steve Jobs", "Not as successful as Edward", null, null));
        addItem(new User("d", "Anonymous", "I am anonymous", null, null));
        addItem(new User("e", "Test person #e", "id = e", null, null));
        addItem(new User("f", "Albert", "e = mc squared", null, null));
    }

    private static void addItem(User item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.userId, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore tagline information here.");
        }
        return builder.toString();
    }
}

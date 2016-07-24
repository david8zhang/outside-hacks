package com.example.dzhang.outsidehacks.dummy;

import com.example.dzhang.outsidehacks.Friend;

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
    public static final List<Friend> ITEMS = new ArrayList<Friend>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Friend> ITEM_MAP = new HashMap<String, Friend>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        addItem(new Friend("a", "Edward Zhang", "A cool person", 1000, null, null, 1000));
        addItem(new Friend("b", "Bill Gates", "Not as rich as Edward", 7, null, null, 3));
        addItem(new Friend("c", "Steve Jobs", "Not as successful as Edward", 7, null, null, 3));
        addItem(new Friend("d", "Anonymous", "I am anonymous", 7, null, null, 3));
        addItem(new Friend("e", "Test person #e", "id = e", 7, null, null, 3));
        addItem(new Friend("f", "Albert", "e = mc squared", 7, null, null, 3));
    }

    private static void addItem(Friend item) {
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

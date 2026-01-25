package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryTest {

    @Test
    void testEnumValues() {
        // When
        TransactionCategory[] categories = TransactionCategory.values();

        // Then
        assertNotNull(categories);
        assertEquals(7, categories.length);
    }

    @Test
    void testFoodCategory() {
        // When
        TransactionCategory category = TransactionCategory.FOOD;

        // Then
        assertNotNull(category);
        assertEquals("FOOD", category.name());
    }

    @Test
    void testTravelCategory() {
        // When
        TransactionCategory category = TransactionCategory.TRAVEL;

        // Then
        assertNotNull(category);
        assertEquals("TRAVEL", category.name());
    }

    @Test
    void testShoppingCategory() {
        // When
        TransactionCategory category = TransactionCategory.SHOPPING;

        // Then
        assertNotNull(category);
        assertEquals("SHOPPING", category.name());
    }

    @Test
    void testUtilitiesCategory() {
        // When
        TransactionCategory category = TransactionCategory.UTILITIES;

        // Then
        assertNotNull(category);
        assertEquals("UTILITIES", category.name());
    }

    @Test
    void testGroceriesCategory() {
        // When
        TransactionCategory category = TransactionCategory.GROCERIES;

        // Then
        assertNotNull(category);
        assertEquals("GROCERIES", category.name());
    }

    @Test
    void testEntertainmentCategory() {
        // When
        TransactionCategory category = TransactionCategory.ENTERTAINMENT;

        // Then
        assertNotNull(category);
        assertEquals("ENTERTAINMENT", category.name());
    }

    @Test
    void testOtherCategory() {
        // When
        TransactionCategory category = TransactionCategory.OTHER;

        // Then
        assertNotNull(category);
        assertEquals("OTHER", category.name());
    }

    @Test
    void testValueOf() {
        // When & Then
        assertEquals(TransactionCategory.FOOD, TransactionCategory.valueOf("FOOD"));
        assertEquals(TransactionCategory.TRAVEL, TransactionCategory.valueOf("TRAVEL"));
        assertEquals(TransactionCategory.SHOPPING, TransactionCategory.valueOf("SHOPPING"));
        assertEquals(TransactionCategory.UTILITIES, TransactionCategory.valueOf("UTILITIES"));
        assertEquals(TransactionCategory.GROCERIES, TransactionCategory.valueOf("GROCERIES"));
        assertEquals(TransactionCategory.ENTERTAINMENT, TransactionCategory.valueOf("ENTERTAINMENT"));
        assertEquals(TransactionCategory.OTHER, TransactionCategory.valueOf("OTHER"));
    }

    @Test
    void testValueOfInvalidCategory() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionCategory.valueOf("INVALID_CATEGORY");
        });
    }

    @Test
    void testValueOfNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            TransactionCategory.valueOf(null);
        });
    }

    @Test
    void testEnumEquality() {
        // Given
        TransactionCategory category1 = TransactionCategory.FOOD;
        TransactionCategory category2 = TransactionCategory.FOOD;
        TransactionCategory category3 = TransactionCategory.TRAVEL;

        // Then
        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        assertSame(category1, category2);
    }

    @Test
    void testEnumOrdinal() {
        // Then - verify ordinal positions
        assertEquals(0, TransactionCategory.FOOD.ordinal());
        assertEquals(1, TransactionCategory.TRAVEL.ordinal());
        assertEquals(2, TransactionCategory.SHOPPING.ordinal());
        assertEquals(3, TransactionCategory.UTILITIES.ordinal());
        assertEquals(4, TransactionCategory.GROCERIES.ordinal());
        assertEquals(5, TransactionCategory.ENTERTAINMENT.ordinal());
        assertEquals(6, TransactionCategory.OTHER.ordinal());
    }

    @Test
    void testEnumToString() {
        // Then
        assertEquals("FOOD", TransactionCategory.FOOD.toString());
        assertEquals("TRAVEL", TransactionCategory.TRAVEL.toString());
        assertEquals("SHOPPING", TransactionCategory.SHOPPING.toString());
        assertEquals("UTILITIES", TransactionCategory.UTILITIES.toString());
        assertEquals("GROCERIES", TransactionCategory.GROCERIES.toString());
        assertEquals("ENTERTAINMENT", TransactionCategory.ENTERTAINMENT.toString());
        assertEquals("OTHER", TransactionCategory.OTHER.toString());
    }

    @Test
    void testAllEnumValuesArePresent() {
        // When
        TransactionCategory[] categories = TransactionCategory.values();

        // Then - verify all expected categories are present
        assertTrue(containsCategory(categories, TransactionCategory.FOOD));
        assertTrue(containsCategory(categories, TransactionCategory.TRAVEL));
        assertTrue(containsCategory(categories, TransactionCategory.SHOPPING));
        assertTrue(containsCategory(categories, TransactionCategory.UTILITIES));
        assertTrue(containsCategory(categories, TransactionCategory.GROCERIES));
        assertTrue(containsCategory(categories, TransactionCategory.ENTERTAINMENT));
        assertTrue(containsCategory(categories, TransactionCategory.OTHER));
    }

    @Test
    void testSwitchStatement() {
        // Given
        TransactionCategory category = TransactionCategory.GROCERIES;
        String result;

        // When
        switch (category) {
            case FOOD:
                result = "Food";
                break;
            case TRAVEL:
                result = "Travel";
                break;
            case SHOPPING:
                result = "Shopping";
                break;
            case UTILITIES:
                result = "Utilities";
                break;
            case GROCERIES:
                result = "Groceries";
                break;
            case ENTERTAINMENT:
                result = "Entertainment";
                break;
            case OTHER:
                result = "Other";
                break;
            default:
                result = "Unknown";
        }

        // Then
        assertEquals("Groceries", result);
    }

    @Test
    void testEnumInArray() {
        // Given
        TransactionCategory[] myCategories = {
                TransactionCategory.FOOD,
                TransactionCategory.GROCERIES,
                TransactionCategory.TRAVEL
        };

        // Then
        assertEquals(3, myCategories.length);
        assertEquals(TransactionCategory.FOOD, myCategories[0]);
        assertEquals(TransactionCategory.GROCERIES, myCategories[1]);
        assertEquals(TransactionCategory.TRAVEL, myCategories[2]);
    }

    @Test
    void testCompareTo() {
        // Given
        TransactionCategory food = TransactionCategory.FOOD;
        TransactionCategory travel = TransactionCategory.TRAVEL;

        // Then
        assertTrue(food.compareTo(travel) < 0);
        assertTrue(travel.compareTo(food) > 0);
        assertEquals(0, food.compareTo(TransactionCategory.FOOD));
    }

    // Helper method
    private boolean containsCategory(TransactionCategory[] categories, TransactionCategory target) {
        for (TransactionCategory category : categories) {
            if (category == target) {
                return true;
            }
        }
        return false;
    }
}

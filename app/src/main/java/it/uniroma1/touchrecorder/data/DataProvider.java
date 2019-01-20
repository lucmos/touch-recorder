package it.uniroma1.touchrecorder.data;

import java.util.ArrayList;

public class DataProvider  {
    private  Configuration configuration;
    private ItemsProvider itemsProvider;

    private static DataProvider instance;
    public static DataProvider getInstance() {
        if (instance == null) {
            throw  new RuntimeException("The DataProvider hasn't been created!");
        }
        return instance;
    }

    public static DataProvider createInstance(Configuration configuration) {
        if (instance == null) {
            instance = new DataProvider(configuration);
        }
        return instance;
    }

    private DataProvider(Configuration configuration) {
        this.configuration = configuration;
        this.itemsProvider = new ItemsProvider();
    }

    public ItemsProvider getItemsProvider() {
        return itemsProvider;
    }

    public String getRepetitionsLabel() {
        return configuration.repetitions_label;
    }

    public String getTitle() {
        return configuration.title;
    }


    public class ItemsProvider {
        private ItemsProvider() {
        }

        public int getNumberOfItems() {
            return configuration.items.size() * configuration.repetitions;
        }

        public String get(int current_index) {
            return configuration.items.get(current_index % configuration.items.size());
        }

        public boolean hasNext(int index) {
            return index < getNumberOfItems();
        }

        public int nextIndex(int current_index) {
            return (current_index + 1);
        }

        public int getRepetitions() {
            return configuration.repetitions;
        }
    }
}

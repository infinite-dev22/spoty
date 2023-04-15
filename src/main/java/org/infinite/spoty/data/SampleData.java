package org.infinite.spoty.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.infinite.spoty.model.Category;
import org.infinite.spoty.model.QuickStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class SampleData implements Serializable {
    public static List<QuickStats> quickStatsSampleData() {
        List<QuickStats> samples = new ArrayList<>();

        QuickStats quickStats = new QuickStats();
        quickStats.setTitle("2050");
        quickStats.setSubtitle("Total Orders");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("3250");
        quickStats.setSubtitle("Total Expense");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("87.5%");
        quickStats.setSubtitle("Total Revenue");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("2550");
        quickStats.setSubtitle("New Users");
        samples.add(quickStats);

        return samples;
    }

    public static ObservableList<Category> categorySampleData() {
        ObservableList<Category> categoryList = FXCollections.observableArrayList();

        Category category = new Category();
        category.setCategoryCode("CA6");
        category.setCategoryName("Fruits");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA5");
        category.setCategoryName("Shoes");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA4");
        category.setCategoryName("T-Shirts");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA3");
        category.setCategoryName("Jackets");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA2");
        category.setCategoryName("Computers");
        categoryList.add(category);

        category = new Category();
        category.setCategoryCode("CA1");
        category.setCategoryName("Accessories");
        categoryList.add(category);

        return categoryList;
    }
}

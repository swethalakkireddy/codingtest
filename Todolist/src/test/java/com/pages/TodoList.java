package com.pages;

import io.cucumber.datatable.DataTable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TodoList {

    WebDriver driver;
    Logger logger = Logger.getLogger(TodoList.class.getName());

    @FindBy(className = "new-todo")
    public WebElement todo_textbox;

    @FindBy(xpath = "/html/body/section/section/ul/li")
    public List<WebElement> todoAllFilterTaskList;

    @FindBy(xpath = "/html/body/section/footer/ul/li")
    public List<WebElement> todoMVCFilters;

    @FindBy(className = "clear-completed")
    public WebElement todoclearcompletedBtn;

    @FindBy(xpath = "/html/body/section/footer/span/strong")
    public WebElement todoListSummarycount;

    @FindBy(xpath = "/html/body/section/section/label")
    public WebElement todoListCompletedToggle;
    public TodoList(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List getTasksFromFilters(String filter) throws Throwable {
        switch (filter) {
            case "All":
                WebElement allFilter = todoMVCFilters.get(0).findElement(By.tagName("a"));
                allFilter.click();
                Assert.assertTrue((allFilter.getText()).equalsIgnoreCase("All") &&
                        (allFilter.getAttribute("class")).equalsIgnoreCase("selected"), "[ERROR-APP]:User not in All Filter");

                break;

            case "Active":
                WebElement activeFilter = todoMVCFilters.get(1).findElement(By.tagName("a"));
                activeFilter.click();
                Assert.assertTrue(activeFilter.getText().equalsIgnoreCase("Active") &&
                        (activeFilter.getAttribute("class")).equalsIgnoreCase("selected"), "[ERROR-APP]:User not in Active Filter");
                break;

            case "Completed":
                WebElement completedFilter = todoMVCFilters.get(2).findElement(By.tagName("a"));
                completedFilter.click();
                Assert.assertTrue((completedFilter.getText()).equalsIgnoreCase("Completed") &&
                        (completedFilter.getAttribute("class")).equalsIgnoreCase("selected"), "[ERROR-APP]:User not in Completed Filter");
                break;

            default:
                logger.info("[ERROR-APP]:Invalid Filter");
                break;
        }
        List<String> allTaskList = new ArrayList<>();

        for (int i = 0; i < todoAllFilterTaskList.size(); i++) {
            String taskList = todoAllFilterTaskList.get(i).findElement(By.xpath("div/label")).getText();
            allTaskList.add(taskList);
        }
        return allTaskList;

    }

    public int getTaskListCount(String filter) throws Throwable {
        return getTasksFromFilters(filter).size();
    }

    public void clickTaskOnFilter(DataTable tasklist) {
        List<String> taskList = tasklist.asList();
        taskList.forEach(task -> todoAllFilterTaskList.stream().filter
                (element -> element.findElement(By.xpath("div/label")).getText().matches(task))
                .forEach(element -> element.findElement(By.xpath("div/input")).click()));
        taskList.forEach(task -> logger.info(task + " is clicked successfully"));

    }

    public long getCompletedTaskListCount() throws Throwable {
        return todoAllFilterTaskList.stream().filter(ele -> ele.getAttribute("class").matches("todo completed")).count();

    }


}

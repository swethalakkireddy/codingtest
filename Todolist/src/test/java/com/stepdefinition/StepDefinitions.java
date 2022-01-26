package com.stepdefinition;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;

import com.pages.TodoList;

import java.util.List;
import java.util.logging.Logger;

public class StepDefinitions {


    Logger logger = Logger.getLogger(StepDefinitions.class.getName());
    TodoList todoListobj;
    public static final String ALL = "All";
    public static final String ACTIVE = "Active";
    public static final String COMPLETED = "Completed";
    public static final String CLEAR_COMPLETED = "clear-completed";

    @Given("^I hit URL \"([^\"]*)\"$")
    public void i_hit_URL(String baseURL) throws Throwable {
        ServiceHooks.driver.get(baseURL);
    }

    @When("^I enter a todo task$")
    public void i_enter_a_todo_task_task(DataTable task) throws Throwable {
        List<String> taskList = task.asList();
        for (String taskName : taskList) {
            todoListobj.todo_textbox.sendKeys(taskName);
            todoListobj.todo_textbox.sendKeys(Keys.ENTER);
        }
    }

    @Then("^I (observe|cannotsee) the entries of todolist in \"([^\"]*)\" filter$")
    public void I_observe_the_entries_of_todolist_task_in_filter(String condition, String filter) throws Throwable {
        if (condition.equalsIgnoreCase("observe")) {
            Assert.assertTrue(!(todoListobj.getTasksFromFilters(filter).isEmpty()), "Task list is Empty");
            logger.info("Retrieving the tasklist of " + filter + " Filter" + todoListobj.getTasksFromFilters(filter));
        } else {
            Assert.assertFalse(!(todoListobj.getTasksFromFilters(filter).isEmpty()), "Task list is not Empty");
            logger.info("Task list is empty for " + filter + " Filter" + todoListobj.getTasksFromFilters(filter));
        }

    }

    @Then("^I (observe|cannotsee) \"([^\"]*)\" filter is displayed$")
    public void I_observe_filter_is_displayed(String condition, String filter) throws Throwable {

        if (condition.equalsIgnoreCase("observe")) {
            switch (filter) {
                case ALL:
                    Assert.assertTrue((todoListobj.todoMVCFilters.get(0).findElement(By.tagName("a")).getText()).equalsIgnoreCase(ALL), "[ERROR-APP]:All filter is not displayed");
                    logger.info("All Filter is displayed");
                    break;
                case ACTIVE:
                    Assert.assertTrue((todoListobj.todoMVCFilters.get(1).findElement(By.tagName("a")).getText()).equalsIgnoreCase(ACTIVE), "[ERROR-APP]:Active filter is not displayed");
                    logger.info("Active Filter is displayed");
                    break;
                case COMPLETED:
                    Assert.assertTrue((todoListobj.todoMVCFilters.get(2).findElement(By.tagName("a")).getText()).equalsIgnoreCase(COMPLETED), "[ERROR-APP]:Completed filter is not displayed");
                    logger.info("Completed Filter is displayed");
                    break;

                case CLEAR_COMPLETED:
                    Assert.assertTrue(!(todoListobj.todoclearcompletedBtn.getAttribute("style").contains("none")), "[ERROR-APP]:Completed filter is not displayed");
                    logger.info("clear-completed Filter is displayed");
                    break;
                default:
                    Assert.fail("[ERROR-APP]:Invalid Filter " + filter);
                    break;

            }
        } else {
            if (filter.equalsIgnoreCase(CLEAR_COMPLETED)) {
                Assert.assertTrue(todoListobj.todoclearcompletedBtn.getAttribute("style").contains("none"), "clear-completed filter is not displayed");
                logger.info("clear-completed Filter is not displayed");
            }
        }


    }


    @Then("^I count the displayed list items in \"([^\"]*)\" filter$")
    public void i_count_the_displayed_list_items_in_filter(String filter) throws Throwable {
        logger.info("The count of tasks in " + filter + "filter is:" + todoListobj.getTaskListCount(filter));
    }

    @Given("^I see \"([^\"]*)\" page$")
    public void I_observe_page(String page) throws Throwable {
        todoListobj = new TodoList(ServiceHooks.driver);
        String pageTitle = ServiceHooks.driver.getTitle();
        logger.info(ServiceHooks.driver.getTitle());
        Assert.assertTrue(pageTitle.contains(page), "[ERROR-APP:]:Not on Todo MVC page");
        logger.info("Landed on TodoMVC Page");
    }

    @When("^User clicks on \"([^\"]*)\" filter$")
    public void User_clicks_on_filter(String filter) throws Throwable {
        switch (filter) {
            case ALL:
                todoListobj.todoMVCFilters.get(0).findElement(By.tagName("a")).click();
                break;
            case ACTIVE:
                todoListobj.todoMVCFilters.get(1).findElement(By.tagName("a")).click();
                break;
            case COMPLETED:
                todoListobj.todoMVCFilters.get(2).findElement(By.tagName("a")).click();
                break;
            case CLEAR_COMPLETED:
                todoListobj.todoclearcompletedBtn.click();
                break;
            default:
                Assert.fail("[ERROR-APP]:Invalid filter entered " + filter);
                break;
        }

        logger.info(filter + " filter is successfully clicked");
    }


    @Then("I verify the task list in {string} filter and {string} filter are equal")
    public void i_verify_the_task_list_in_filter_and_filter_are_equal(String filter1, String filter2) throws Throwable {
        Assert.assertTrue(todoListobj.getTasksFromFilters(filter1).containsAll(todoListobj.getTasksFromFilters(filter2))
                        && (todoListobj.getTasksFromFilters(filter1).equals(todoListobj.getTasksFromFilters(filter2))),
                "[ERROR-APP]:Task list in " + filter1 + " and " + filter2 + " are not same");
        logger.info("Task list in " + filter1 + " and " + filter2 + " are same");

    }


    @When("User clicks on task on todo list")
    public void User_clicks_on_task_on_todo_list(DataTable dataTable) {
        todoListobj.clickTaskOnFilter(dataTable);
    }

    @Then("I observe the listSummary count is reduced by number of completed tasks in {string} filter")
    public void I_observe_the_listSummary_count_is_reduced_by_number_of_completed_tasks(String filter) throws Throwable {
        long taskcount = todoListobj.getTaskListCount(filter);
        logger.info("All tasks list count is: " + taskcount);
        logger.info("Completed tasks list count is: " + todoListobj.getCompletedTaskListCount());
        Assert.assertTrue(Long.parseLong(todoListobj.todoListSummarycount.getText()) - (taskcount - todoListobj.getCompletedTaskListCount()) == 0,
                "[ERROR-APP]:Invalid listSummary count");
        logger.info("List Summary Count is updated to " + (taskcount - todoListobj.getCompletedTaskListCount()) + " items left");
    }


    @Then("I cannot see {string} filter tasks in {string} list")
    public void I_cannot_see_filter_tasks_in_list(String filter1, String filter2) throws Throwable {
        Assert.assertFalse(todoListobj.getTasksFromFilters(filter2).stream().anyMatch(todoListobj.getTasksFromFilters(filter1)::contains), "[ERROR-APP]:" + filter2 + " has atleast 1 task in " + filter1);
        logger.info(filter2 + " filter has has no matching tasks in " + filter1 + " filter");
    }


    @When("^User clicks on cancel symbol for \"([^\"]*)\" in the list$")
    public void User_clicks_on_cancel_symbol_for_in_completed_list(String task) {

        for (WebElement taskName : todoListobj.todoAllFilterTaskList) {
            if (taskName.findElement(By.xpath("div/label")).getText().equalsIgnoreCase(task)) {
                taskName.findElement(By.xpath("div/label")).click();
                new WebDriverWait(ServiceHooks.driver, 20).
                        until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.destroy"))).click();
            }

        }

    }


    @Then("^I (observe|cannotsee) \"([^\"]*)\" task in \"([^\"]*)\" list$")
    public void I_cannotsee_task_in_list(String condition, String task, String filter) throws Throwable {
        if (condition.equalsIgnoreCase("cannotsee")) {
            Assert.assertFalse(todoListobj.getTasksFromFilters(filter).contains(task), "[ERROR-APP]: " + task + " is available in " + filter + " filter");
            logger.info(task + " is not available in " + filter + " filter");
        } else {
            Assert.assertTrue(todoListobj.getTasksFromFilters(filter).contains(task), "[ERROR-APP]: " + task + " is not available in " + filter + " filter");
            logger.info(task + " is available in " + filter + " filter");
        }
    }


    @Given("I verify the list summary count in {string} filter is equal to list summary count in {string} filter")
    public void i_verify_the_list_summary_count_in_filter_is_equal_to_list_summary_count_in_filter(String filter1, String filter2) throws Throwable {
        User_clicks_on_filter(filter1);
        String allListSummaryCount = todoListobj.todoListSummarycount.getText();
        User_clicks_on_filter(filter2);
        String activeListSummaryCount = todoListobj.todoListSummarycount.getText();
        Assert.assertTrue(allListSummaryCount.equalsIgnoreCase(activeListSummaryCount), "the list summary count doesn't match");
        logger.info("the list summary count in " + filter1 + " filter is equal to list summary count in " + filter2 + " filter");
    }

    @Then("^I verify the list summary count is updated in \"([^\"]*)\" filter$")
    public void i_verify_the_list_summary_count_is_updated(String filter) throws Throwable {
        Assert.assertTrue(todoListobj.getTaskListCount(filter) == Integer.parseInt(todoListobj.todoListSummarycount.getText()), "List Summary count is not updated");
        logger.info("the list summary count is updated to:" + todoListobj.getTaskListCount(filter));
    }


    @When("User clicks on mark all as completed drop mark")
    public void userClicksOnMarkAllAsCompletedDropMark() {
        todoListobj.todoListCompletedToggle.click();
    }

    @Then("I observe the task list is marked to completed")
    public void iobserveTheTaskListIsMarkedToCompleted() throws Throwable {
        Assert.assertTrue(todoListobj.getTasksFromFilters(ALL).size() == todoListobj.getTasksFromFilters(COMPLETED).size(), "Active tasks are left");
        logger.info("All the tasks are in Completed status");
    }


    @When("I edit the task {string} to {string}")
    public void iEditTheTaskTo(String oldTask, String newTask)  {
        Actions actions = new Actions(ServiceHooks.driver);
        todoListobj.todoAllFilterTaskList.stream().
                filter(ele-> ele.findElement(By.xpath("div/label")).getText().equalsIgnoreCase(oldTask))
                .forEach(ele -> actions.doubleClick(ele.findElement(By.xpath("div/label"))).doubleClick().sendKeys(newTask).sendKeys(Keys.ENTER).perform());

    }

    @Then("I should be able to see {string} task in the filter")
    public void iShouldBeAbleToSeeTaskInTheFilter(String newTask) throws Throwable {
        Assert.assertTrue(todoListobj.getTasksFromFilters(ALL).contains(newTask),"[ERROR-APP]:new task unavailable in task list");
        logger.info(newTask+" available in taskList");

    }
}

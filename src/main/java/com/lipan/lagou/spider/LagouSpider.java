package com.lipan.lagou.spider;

import com.lipan.lagou.entity.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 目标网站https://www.lagou.com/zhaopin/Java/?labelWords=label
 * @author: lipan
 * @time: 2020/3/19 17:48
 */
public class LagouSpider {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", LagouSpider.class.getClassLoader().getResource("driver/chromedriver.exe").getPath());
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");
        webDriver.findElement(By.className("body-btn")).click();

        clickOption(webDriver, "工作经验", "应届毕业生");
        clickOption(webDriver, "学历要求", "本科");
        clickOption(webDriver, "公司规模", "50-150人");
        clickOption(webDriver, "行业领域", "移动互联网");

        List<Job> jobList = new ArrayList<Job>();

        getJobsByPagination(webDriver, jobList);

        for (Job job : jobList) {
            System.out.println(job);
        }
    }

    /**
     * 设置筛选条件
     *
     * @param webDriver
     * @param chosenTitle
     * @param optionTitle
     */
    private static void clickOption(WebDriver webDriver, String chosenTitle, String optionTitle) {
        WebElement chosenElement = webDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + chosenTitle + "')]"));
        WebElement optionElement = chosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTitle + "')]"));
        optionElement.click();
    }

    /**
     * 递归获取所有页面的所有job的信息
     *
     * @param webDriver
     */
    private static void getJobsByPagination(WebDriver webDriver, List<Job> jobList) {

        List<WebElement> jobElementList = webDriver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElementList) {
            WebElement moneyElement = jobElement.findElement(By.className("position")).findElement(By.className("money"));
            String companyName = jobElement.findElement(By.className("company_name")).getText();
            String salary = moneyElement.getText();
            Job job = new Job();
            job.setCompanyName(companyName);
            job.setSalary(salary);
            jobList.add(job);
        }
        WebElement nextPageBtn = webDriver.findElement(By.className("pager_next"));
        if (!nextPageBtn.getAttribute("class").contains("pager_next_disabled")) {
            nextPageBtn.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getJobsByPagination(webDriver, jobList);
        }
    }


}

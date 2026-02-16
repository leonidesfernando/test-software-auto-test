package br.com.home.lab.softwaretesting.automation.selenium.webdriver.components;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public abstract class GenericUI extends LoadableComponent<GenericUI> implements WrapsElement {

    @Getter
    private final WebDriver webDriver;

    public GenericUI(WebDriver webDriver){
        this.webDriver = webDriver;
        init();
    }

    protected void init(){}


    /**
     * <p>The default behavior the GenericUI component is do nothing to load, its will loaded when the page is loaded.
     * <b>But if will be necessary you can implement this method(load),
     * but be aware what you are doing</b></p>
     * <p>Each GenericUI instance must implement the
     * <code>LoadableComponemt::isLoaded</code> method</p>
     */
    @Override
    protected void load() {}

    @Override
    protected void isLoaded() throws Error {
        boolean loaded = false;
        try{
            loaded = isReady();
        }catch (Exception e){
            log.error("Error on checking whether component is loaded: {}", e.getMessage());
        }
        if(!loaded){
            throw new Error(String.format("%s is not loaded yet :/", this.getClass().getSimpleName()));
        }
    }

    protected abstract boolean isReady();
}

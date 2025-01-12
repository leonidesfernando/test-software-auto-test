package br.com.home.lab.softwaretesting.automation.model;

import br.com.home.lab.softwaretesting.automation.config.Bundles;

public interface DescritivelPorBundle {

    default String getDescription(String language){
        return Bundles.getMessage(this.getKey(), language);
    }

    /**
     * Key presente no arquivo de bundle
     * @return
     */
    String getKey();
}

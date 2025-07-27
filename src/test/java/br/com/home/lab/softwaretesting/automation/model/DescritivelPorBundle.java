package br.com.home.lab.softwaretesting.automation.model;

import br.com.home.lab.softwaretesting.automation.util.Util;

public interface DescritivelPorBundle {

    default String getDescription(String language){
        return Util.getMessageByKey(getKey());
    }

    /**
     * Key presente no arquivo de bundle
     * @return
     */
    String getKey();
}

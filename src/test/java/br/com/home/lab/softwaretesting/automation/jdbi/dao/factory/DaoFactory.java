package br.com.home.lab.softwaretesting.automation.jdbi.dao.factory;

import br.com.home.lab.softwaretesting.automation.jdbi.JdbiConnection;

public class DaoFactory {

    private DaoFactory(){}

    public static  <T> T get(Class<T> tClass){
        return DaoFactoryHolder.jdbiConnection.getJdbi().onDemand(tClass);
    }

    private static class DaoFactoryHolder{
        private static final JdbiConnection jdbiConnection = new JdbiConnection();
    }
}

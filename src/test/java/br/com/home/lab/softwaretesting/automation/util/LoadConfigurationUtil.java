package br.com.home.lab.softwaretesting.automation.util;

import br.com.home.lab.softwaretesting.automation.aws.util.SecretsManagerUtil;
import br.com.home.lab.softwaretesting.automation.config.Configurations;
import br.com.home.lab.softwaretesting.automation.model.User;
import org.aeonbits.owner.ConfigFactory;
import org.testng.util.Strings;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public final class LoadConfigurationUtil {

    private static final String API_URL = "api.url";
    private static final String APP_URL = "app.url";
    private static final Configurations config = ConfigFactory.create(Configurations.class);
    private static final Map<String, Map<String, String>> secrets = new ConcurrentHashMap<>();

    private LoadConfigurationUtil(){}

    public static Map<String, String> getSecretsDbCredentials(){

        String secretName = config.awsSecretsManagerDbCredentials();
        return secrets.computeIfAbsent(secretName, key ->
            SecretsManagerUtil.getSecretKeyValue(secretName)
        );
    }

    public static String geApiUrl() {
        return getUrl(API_URL, config.apiUrl());
    }

    public static String getAppUrl(){
        return getUrl(APP_URL, config.appUrl());
    }

    private static String getUrl(String paramUrl, String configUrl){
        String urlByParameter = System.getProperty(paramUrl);
        return Strings.isNotNullAndNotEmpty(urlByParameter) ? urlByParameter : configUrl;
    }

    public static String getOnlyApiUrl() {
        String[] urlAndPort = getApiUrlAndPorAsArray();
        return String.format("%s:%s", urlAndPort[0], urlAndPort[1]);
    }

    public static int getPort() {
        String[] urlAndPort = getApiUrlAndPorAsArray();
        return urlAndPort.length > 2 ?
                Integer.parseInt(urlAndPort[2].replace("/", ""))
                : 80;
    }

    private static String[] getApiUrlAndPorAsArray(){
        return geApiUrl().split(":");
    }

    public static User getUser() {
        return new User(config.username(), config.password());
    }

    public static String getLanguage(){
        return Optional.ofNullable(config.language()).orElse("BR");
    }
}

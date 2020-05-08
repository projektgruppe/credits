package dk.creditoro.client.networking.rest_client;


import dk.creditoro.client.core.EventNames;
import dk.creditoro.client.model.crud.Channel;
import dk.creditoro.client.model.crud.Production;
import dk.creditoro.client.model.crud.User;
import dk.creditoro.client.networking.IClient;
import dk.creditoro.client.networking.rest_client.endpoints.ChannelsEndpoint;
import dk.creditoro.client.networking.rest_client.endpoints.HttpManager;
import dk.creditoro.client.networking.rest_client.endpoints.ProductionsEndpoint;
import dk.creditoro.client.networking.rest_client.endpoints.UsersEndpoint;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

public class RestClient implements IClient {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final PropertyChangeSupport propertyChangeSupport;
    private final UsersEndpoint usersEndpoint;
    private final ChannelsEndpoint channelsEndpoint;
    private final ProductionsEndpoint productionsEndpoint;

    public String getToken() {
        return token;
    }

    private String token;

    public RestClient() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        var httpManager = new HttpManager();
        usersEndpoint = new UsersEndpoint(httpManager);
        channelsEndpoint = new ChannelsEndpoint(httpManager);
        productionsEndpoint = new ProductionsEndpoint(httpManager);
    }
    @Override
    public User login(String email, String password) {
        var result = usersEndpoint.postLogin(email, password);
        propertyChangeSupport.firePropertyChange(EventNames.LOGIN_RESULT.toString(), null, result);
        updateToken(result);
        return result.getT();
    }

    @Override
    public void register(User user) {
        LOGGER.info("Called register.");
    }

    @Override
    public Channel[] searchChannels(String q) {
        var result = channelsEndpoint.getChannels(q, token);
        propertyChangeSupport.firePropertyChange(EventNames.ON_SEARCH_CHANNELS_RESULT.toString(), null, result);
        LOGGER.info("Fired property change event.");
        updateToken(result);
        return result.getT();
    }

    @Override
    public Production[] searchProductions(String q) {
        var result = productionsEndpoint.getProductions(q,token);
        propertyChangeSupport.firePropertyChange(EventNames.ON_SEARCH_PRODUCTIONS_RESULT.toString(),null,result);
        LOGGER.info("Fired property change event.");
        updateToken(result);
        return result.getT();
    }

    @Override
    public void addListener(String name, PropertyChangeListener propertyChangeListener) {
        LOGGER.info("added listener.");
        propertyChangeSupport.addPropertyChangeListener(name, propertyChangeListener);
    }

    private void updateToken(TokenResponse<?> response) {
        token = response.getToken();
    }
}
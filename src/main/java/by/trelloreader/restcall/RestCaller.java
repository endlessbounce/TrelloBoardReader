package by.trelloreader.restcall;

import java.io.IOException;	
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.trelloreader.constant.AppConst;
import by.trelloreader.entity.TrelloCard;
import by.trelloreader.entity.TrelloList;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A class which makes calls to the Trello API to read data from a board. Trello stores data in nested structures:
 * boards contain lists, lists contain cards, cards contain description, comments, etc.,
 * that's why a chain of nested calls is used here: first TrelloList objects are created, then each of these objects
 * obtains TrelloCard objects, each of which obtains the data containing in a card.
 */
public class RestCaller {

    // Constants ----------------------------------------------------------------------------------
    private final static Logger LOGGER = LogManager.getLogger();
    private final static String VALUE = "_value";
    private final static String NAME = "name";
    private final static String ID = "id";
    private final static String DESC = "desc";
    private final static String DATA = "data";
    private final static String TEXT = "text";
    private final static String QMARK = "?";
    private final static String FIELDS_ALL = "all";
    private final static String FIELDS_LIST = "id,name";
    private final static String FIELDS_CARD = "id,name,idList,desc";
    private final static String ACTION_TYPE = "commentCard";
    private final static String BOARD_RESOURCE = "https://api.trello.com/1/boards/?/name";
    private final static String LIST_RESOURCE = "https://api.trello.com/1/boards/?/lists";
    private final static String CARD_RESOURCE = "https://api.trello.com/1/lists/?/cards";
    private final static String COMMENT_RESOURCE = "https://api.trello.com/1/cards/?/actions";
    private final String KEY_VALUE;
    private final String TOKEN_VALUE;

    // Vars ---------------------------------------------------------------------------------------
    private static OkHttpClient client = new OkHttpClient();

    // Constructors -------------------------------------------------------------------------------

    /**
     * Public constructor
     *
     * @param KEY_VALUE   your Trello API-key
     * @param TOKEN_VALUE your token to access a user's application
     */
    public RestCaller(String KEY_VALUE, String TOKEN_VALUE) {
        this.KEY_VALUE = KEY_VALUE;
        this.TOKEN_VALUE = TOKEN_VALUE;
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Fetches the name for a board with such ID. It will be used to give an output file a name.
     *
     * @param boardId the ID of this board
     * @return the name if this board
     */
    public String fetchBoardName(String boardId) {
    	String response = call(buildURL(boardId, BOARD_RESOURCE, FIELDS_ALL));
    	
        try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
            return jsonReader.readObject()
                    .getString(VALUE);
        } catch (Exception e) {
        	LOGGER.error("Error fetching board name with id=" + boardId + ". Response: " + response, e);
            return "Error_" + boardId;
        }
    }

    /**
     * Finds all lists for this board. Makes nested calls to fetch all cards for each list.
     *
     * @param boardId the ID of this board
     * @return the list of lists on this board
     */
    public List<TrelloList> fetchLists(String boardId) {
        List<TrelloList> result = new ArrayList<>();
        String response = call(buildURL(boardId, LIST_RESOURCE, FIELDS_LIST));
        
        try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
            result = jsonReader.readArray()
                    .stream()
                    .map(jsonValue -> mapToTrelloList(jsonValue))
                    .collect(Collectors.toList());
        } catch (Exception e) {
        	LOGGER.error("Error fetching lists for board #" + boardId + ". Response: " + response, e);
        }

        result.forEach(trelloList -> trelloList.setCards(fetchCards(trelloList.getId())));
        return result;
    }

    /**
     * Finds all cards in this list. Makes a nested calls to fetch all comments for each card.
     *
     * @param listId the ID of this list
     * @return the list of cards in this list
     */
    public List<TrelloCard> fetchCards(String listId) {
        List<TrelloCard> result = new ArrayList<>();
        String response = call(buildURL(listId, CARD_RESOURCE, FIELDS_CARD));
        
        try (JsonReader jsonReader = Json.createReader(new StringReader(response))) {
            result = jsonReader.readArray()
                    .stream()
                    .map(jsonValue -> mapToTrelloCard(jsonValue))
                    .collect(Collectors.toList());
        } catch (Exception e) {
        	LOGGER.error("Error fetching cards for list #" + listId + ". Response: " + response, e);
        }

        result.forEach(trelloCard -> trelloCard.setComments(fetchComments(trelloCard.getId())));
        return result;
    }

    /**
     * Finds all comments in this card.
     *
     * @param cardId the ID of this card
     * @return the list of comments of this card
     * @throws IOException 
     */
    public List<String> fetchComments(String cardId) 	{
    	String response = call(buildCommentsURL(cardId, COMMENT_RESOURCE));
    	
    	try (JsonReader jsonReader = Json.createReader(new StringReader(response));) {
            return jsonReader.readArray()
                    .stream()
                    .map(jsonValue -> mapToString(jsonValue))
                    .filter(string -> !string.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error fetching comments for card #" + cardId + ". Response: " + response, e);
            return Collections.emptyList();
        }
    }

    /**
     * Makes a GET request to a web-service
     *
     * @param url to access
     * @return String of a JSON response
     * @throws IOException
     */
    private String call(String url) {
    	Response response = null;
    	Request request = new Request.Builder()
                .url(url)
                .build();
        
        try {
        	response = client.newCall(request).execute();
        	return response.body().string().toString();
		} catch (Exception e) {
			LOGGER.error("Error making call to "+ url + ". Response: " + response, e);
			return AppConst.EMPTY;
		}
    }

    /**
     * Adds 'actions' parameter to a base URL
     *
     * @param id      of a card
     * @param pattern a URL in which the id will be inserted
     * @return a URL to access a recourse
     */
    private String buildCommentsURL(String id, String pattern) {
        return buildBaseURL(id, pattern)
                .addQueryParameter(AppConst.ACTIONS, ACTION_TYPE)
                .build()
                .toString();
    }

    /**
     * Adds 'fields' parameter to a base URL
     *
     * @param id      of a board, list, or card
     * @param pattern a URL in which the id will be inserted
     * @param fields  parameters which contains the names of a JSON properties to fetch
     * @return a URL to access a recourse
     */
    private String buildURL(String id, String pattern, String fields) {
        return buildBaseURL(id, pattern)
                .addQueryParameter(AppConst.FIELDS, fields)
                .build()
                .toString();
    }

    /**
     * Basic URL contains the key and token read from properties file, which are
     * necessary to access the Trello API
     *
     * @param id      of a board, list, or card
     * @param pattern a URL in which the id will be inserted
     * @return HttpUrl.Builder to continue building a URL
     */
    private HttpUrl.Builder buildBaseURL(String id, String pattern) {
        return HttpUrl.parse(pattern.replace(QMARK, id))
                .newBuilder()
                .addQueryParameter(AppConst.KEY, KEY_VALUE)
                .addQueryParameter(AppConst.TOKEN, TOKEN_VALUE);
    }

    /**
     * Maps JsonValue object to a TrelloList object, which contains its name
     * and all cards.
     *
     * @param JValue a list JSON object from Trello
     * @return a TrelloList object
     */
    private TrelloList mapToTrelloList(JsonValue JValue) {
        JsonObject obj = ((JsonObject) JValue);
        TrelloList tList = new TrelloList();
        tList.setId(obj.getString(ID));
        tList.setName(obj.getString(NAME));
        return tList;
    }

    /**
     * Maps JsonValue object to a TrelloCard object, containing an ID, Name and
     * Description of the card.
     *
     * @param JValue a card from current list
     * @return TrelloCard object
     */
    private TrelloCard mapToTrelloCard(JsonValue JValue) {
        JsonObject obj = ((JsonObject) JValue);
        TrelloCard tCard = new TrelloCard();
        tCard.setId(obj.getString(ID));
        tCard.setName(obj.getString(NAME));
        tCard.setDescription(obj.getString(DESC));
        return tCard;
    }

    /**
     * Maps JsonValue object to a string if it has comment data.
     * Otherwise returns an empty string.
     *
     * @param JValue "data" JSON object of a card.
     * @return either comment if exists, or an empty string if doesn't
     */
    private String mapToString(JsonValue JValue) {
        JsonObject obj = (JsonObject) JValue;
        JsonValue text = obj.getJsonObject(DATA).get(TEXT);
        return text == null ? AppConst.EMPTY : text.toString();
    }
}

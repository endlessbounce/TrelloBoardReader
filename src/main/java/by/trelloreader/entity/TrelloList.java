package by.trelloreader.entity;

import by.trelloreader.constant.AppConst;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This entity corresponds to a list from Trello, having such info as name and list of cards
 */
public class TrelloList {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<TrelloCard> cards;

    @Override
    public String toString() {
        return new StringBuilder().append("********************************************")
                .append(AppConst.NEW_LINE)
                .append("LIST ")
                .append(name)
                .append(AppConst.NEW_LINE)
                .append("********************************************")
                .append(AppConst.NEW_LINE)
                .append(cards.stream().map(c -> c.toString()).collect(Collectors.joining()))
                .append(AppConst.NEW_LINE)
                .toString();
    }
}

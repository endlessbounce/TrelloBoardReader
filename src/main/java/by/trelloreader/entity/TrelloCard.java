package by.trelloreader.entity;

import by.trelloreader.constant.AppConst;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This entity corresponds to a card from Trello, having such info as name, description and
 * comments
 */
public class TrelloCard {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private List<String> comments;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(AppConst.NEW_LINE);

        if (!description.isEmpty()) {
            sb.append(description).append(AppConst.NEW_LINE);
        }

        if (!comments.isEmpty()) {
            sb.append(comments.stream().collect(Collectors.joining(AppConst.NEW_LINE)));
        }

        return sb.append("--------------------------------------------")
                .append(AppConst.NEW_LINE)
                .toString();
    }
}

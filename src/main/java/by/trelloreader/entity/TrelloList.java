package by.trelloreader.entity;

import java.util.List;
import java.util.stream.Collectors;

import by.trelloreader.constant.AppConst;

/**
 * This entity corresponds to a list from Trello, having such info as name and
 * list of cards
 */
public class TrelloList {
	private String id;
	private String name;
	private List<TrelloCard> cards;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TrelloCard> getCards() {
		return cards;
	}

	public void setCards(List<TrelloCard> cards) {
		this.cards = cards;
	}

	@Override
	public String toString() {
		return new StringBuilder("//new_list//")
				.append(name)
				.append(AppConst.NEW_LINE)
				.append(cards.stream().map(c -> c.toString()).collect(Collectors.joining())).append(AppConst.NEW_LINE)
				.toString();
	}
}

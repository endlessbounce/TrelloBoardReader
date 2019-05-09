package by.trelloreader.entity;

import java.util.List;
import java.util.stream.Collectors;

import by.trelloreader.constant.AppConst;

public class TrelloCard {
	private String id;
	private String name;
	private String description;
	private List<String> comments;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("//new_card//");
		sb.append(name).append(AppConst.NEW_LINE);

		if (!description.isEmpty()) {
			sb.append(description).append(AppConst.NEW_LINE);
		}

		if (!comments.isEmpty()) {
			sb.append(comments.stream().collect(Collectors.joining(AppConst.NEW_LINE)));
		}

		return sb.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>").append(AppConst.NEW_LINE).toString();
	}
}

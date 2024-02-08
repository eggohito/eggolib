package io.github.eggohito.eggolib.util.chat;

import java.util.regex.Pattern;

public class MessageFilter {

	private final Pattern filter;

	public MessageFilter(String filter) {
		this(Pattern.compile(filter));
	}

	public MessageFilter(Pattern filter) {
		this.filter = filter;
	}

	public Pattern getFilter() {
		return filter;
	}

	public boolean matches(String message) {
		return filter.pattern().equals(message) || filter.matcher(message).find();
	}

}

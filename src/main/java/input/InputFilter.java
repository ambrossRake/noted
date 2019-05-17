package input;

import java.util.regex.Pattern;

public class InputFilter {

	private Pattern filter;
	private String filterMessage;

	public InputFilter(String filter, String filterMessage) {
		this.filter = Pattern.compile(filter);
		this.filterMessage = filterMessage;
	}


	boolean verify(String input) {
		return filter.matcher(input).matches();
	}

	String getFilterMessage() {
		return filterMessage;
	}
}

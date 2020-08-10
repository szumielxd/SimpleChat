package me.szumielxd.SimpleChat.libs.bungeechat.api.chat;

public final class ClickEvent {
	private final Action action;
	private final String value;

	public Action getAction() {
		return this.action;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		return "ClickEvent(action=" + this.getAction() + ", value=" + this.getValue() + ")";
	}

	public boolean equals(Object o2) {
		if (o2 == this) {
			return true;
		}
		if (!(o2 instanceof ClickEvent)) {
			return false;
		}
		ClickEvent other = (ClickEvent)o2;
		Action this$action = this.getAction();
		Action other$action = other.getAction();
		if (this$action == null ? other$action != null : !this$action.equals(other$action)) {
			return false;
		}
		String this$value = this.getValue();
		String other$value = other.getValue();
		return !(this$value == null ? other$value != null : !this$value.equals(other$value));
	}

	public int hashCode() {
		//int PRIME = 59;
		int result = 1;
		Action $action = this.getAction();
		result = result * 59 + ($action == null ? 43 : ((Object)((Object)$action)).hashCode());
		String $value = this.getValue();
		result = result * 59 + ($value == null ? 43 : $value.hashCode());
		return result;
	}

	public ClickEvent(Action action, String value) {
		this.action = action;
		this.value = value;
	}

	public static enum Action {
		OPEN_URL,
		OPEN_FILE,
		RUN_COMMAND,
		SUGGEST_COMMAND,
		CHANGE_PAGE,
		COPY_TO_CLIPBOARD;
		
	}

}
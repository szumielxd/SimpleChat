package me.szumielxd.SimpleChat.libs.bungeechat.api.chat;

import java.util.Arrays;

public final class HoverEvent {
	private final Action action;
	private final BaseComponent[] value;

	public Action getAction() {
		return this.action;
	}

	public BaseComponent[] getValue() {
		return this.value;
	}

	public String toString() {
		return "HoverEvent(action=" + (Object)((Object)this.getAction()) + ", value=" + Arrays.deepToString(this.getValue()) + ")";
	}

	public boolean equals(Object o2) {
		if (o2 == this) {
			return true;
		}
		if (!(o2 instanceof HoverEvent)) {
			return false;
		}
		HoverEvent other = (HoverEvent)o2;
		Action this$action = this.getAction();
		Action other$action = other.getAction();
		if (this$action == null ? other$action != null : !((Object)((Object)this$action)).equals((Object)other$action)) {
			return false;
		}
		return Arrays.deepEquals(this.getValue(), other.getValue());
	}

	public int hashCode() {
		//int PRIME = 59;
		int result = 1;
		Action $action = this.getAction();
		result = result * 59 + ($action == null ? 43 : ((Object)((Object)$action)).hashCode());
		result = result * 59 + Arrays.deepHashCode(this.getValue());
		return result;
	}

	public HoverEvent(Action action, BaseComponent[] value) {
		this.action = action;
		this.value = value;
	}

	public static enum Action {
		SHOW_TEXT,
		SHOW_ACHIEVEMENT,
		SHOW_ITEM,
		SHOW_ENTITY;
		
	}

}
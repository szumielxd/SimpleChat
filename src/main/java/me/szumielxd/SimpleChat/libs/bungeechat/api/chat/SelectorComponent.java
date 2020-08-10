package me.szumielxd.SimpleChat.libs.bungeechat.api.chat;

public final class SelectorComponent
extends BaseComponent {
	private String selector;

	public SelectorComponent(SelectorComponent original) {
		super(original);
		this.setSelector(original.getSelector());
	}

	@Override
	public SelectorComponent duplicate() {
		return new SelectorComponent(this);
	}

	@Override
	protected void toPlainText(StringBuilder builder) {
		builder.append(this.selector);
		super.toPlainText(builder);
	}

	@Override
	protected void toLegacyText(StringBuilder builder) {
		this.addFormat(builder);
		builder.append(this.selector);
		super.toLegacyText(builder);
	}

	public String getSelector() {
		return this.selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	@Override
	public String toString() {
		return "SelectorComponent(selector=" + this.getSelector() + ")";
	}

	@SuppressWarnings("deprecation")
	public SelectorComponent(String selector) {
		this.selector = selector;
	}

	@Override
	public boolean equals(Object o2) {
		if (o2 == this) {
			return true;
		}
		if (!(o2 instanceof SelectorComponent)) {
			return false;
		}
		SelectorComponent other = (SelectorComponent)o2;
		if (!other.canEqual(this)) {
			return false;
		}
		if (!super.equals(o2)) {
			return false;
		}
		String this$selector = this.getSelector();
		String other$selector = other.getSelector();
		return !(this$selector == null ? other$selector != null : !this$selector.equals(other$selector));
	}

	@Override
	protected boolean canEqual(Object other) {
		return other instanceof SelectorComponent;
	}

	@Override
	public int hashCode() {
		//int PRIME = 59;
		int result = super.hashCode();
		String $selector = this.getSelector();
		result = result * 59 + ($selector == null ? 43 : $selector.hashCode());
		return result;
	}
}
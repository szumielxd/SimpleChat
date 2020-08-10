package me.szumielxd.SimpleChat.libs.bungeechat.api.chat;

public final class KeybindComponent
extends BaseComponent {
	private String keybind;

	public KeybindComponent(KeybindComponent original) {
		super(original);
		this.setKeybind(original.getKeybind());
	}

	@SuppressWarnings("deprecation")
	public KeybindComponent(String keybind) {
		this.setKeybind(keybind);
	}

	@Override
	public KeybindComponent duplicate() {
		return new KeybindComponent(this);
	}

	@Override
	protected void toPlainText(StringBuilder builder) {
		builder.append(this.getKeybind());
		super.toPlainText(builder);
	}

	@Override
	protected void toLegacyText(StringBuilder builder) {
		this.addFormat(builder);
		builder.append(this.getKeybind());
		super.toLegacyText(builder);
	}

	public String getKeybind() {
		return this.keybind;
	}

	public void setKeybind(String keybind) {
		this.keybind = keybind;
	}

	@Override
	public String toString() {
		return "KeybindComponent(keybind=" + this.getKeybind() + ")";
	}

	@SuppressWarnings("deprecation")
	public KeybindComponent() {
	}

	@Override
	public boolean equals(Object o2) {
		if (o2 == this) {
			return true;
		}
		if (!(o2 instanceof KeybindComponent)) {
			return false;
		}
		KeybindComponent other = (KeybindComponent)o2;
		if (!other.canEqual(this)) {
			return false;
		}
		if (!super.equals(o2)) {
			return false;
		}
		String this$keybind = this.getKeybind();
		String other$keybind = other.getKeybind();
		return !(this$keybind == null ? other$keybind != null : !this$keybind.equals(other$keybind));
	}

	@Override
	protected boolean canEqual(Object other) {
		return other instanceof KeybindComponent;
	}

	@Override
	public int hashCode() {
		//int PRIME = 59;
		int result = super.hashCode();
		String $keybind = this.getKeybind();
		result = result * 59 + ($keybind == null ? 43 : $keybind.hashCode());
		return result;
	}
}
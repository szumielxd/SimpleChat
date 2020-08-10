package me.szumielxd.SimpleChat.libs.bungeechat.chat;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public final class TranslationRegistry {
	public static final TranslationRegistry INSTANCE = new TranslationRegistry();
	private final List<TranslationProvider> providers = new LinkedList<TranslationProvider>();

	private void addProvider(TranslationProvider provider) {
		this.providers.add(provider);
	}

	public String translate(String s2) {
		for (TranslationProvider provider : this.providers) {
			String translation = provider.translate(s2);
			if (translation == null) continue;
			return translation;
		}
		return s2;
	}

	public List<TranslationProvider> getProviders() {
		return this.providers;
	}

	public boolean equals(Object o2) {
		if (o2 == this) {
			return true;
		}
		if (!(o2 instanceof TranslationRegistry)) {
			return false;
		}
		TranslationRegistry other = (TranslationRegistry)o2;
		List<TranslationProvider> this$providers = this.getProviders();
		List<TranslationProvider> other$providers = other.getProviders();
		return !(this$providers == null ? other$providers != null : !((Object)this$providers).equals(other$providers));
	}

	public int hashCode() {
		//int PRIME = 59;
		int result = 1;
		List<TranslationProvider> $providers = this.getProviders();
		result = result * 59 + ($providers == null ? 43 : ((Object)$providers).hashCode());
		return result;
	}

	public String toString() {
		return "TranslationRegistry(providers=" + this.getProviders() + ")";
	}

	private TranslationRegistry() {
	}

	static {
		try {
			INSTANCE.addProvider(new JsonProvider("/assets/minecraft/lang/en_us.json"));
		}
		catch (Exception exception) {
			// empty catch block
		}
		try {
			INSTANCE.addProvider(new JsonProvider("/mojang-translations/en_us.json"));
		}
		catch (Exception exception) {
			// empty catch block
		}
		try {
			INSTANCE.addProvider(new ResourceBundleProvider("mojang-translations/en_US"));
		}
		catch (Exception exception) {
			// empty catch block
		}
	}

	private static class JsonProvider
	implements TranslationProvider {
		private final Map<String, String> translations = new HashMap<String, String>();

		public JsonProvider(String resourcePath) throws IOException {
			try (InputStreamReader rd = new InputStreamReader(JsonProvider.class.getResourceAsStream(resourcePath), Charsets.UTF_8);){
				JsonObject obj = new Gson().fromJson((Reader)rd, JsonObject.class);
				for (Map.Entry<String, JsonElement> entries : obj.entrySet()) {
					this.translations.put(entries.getKey(), entries.getValue().getAsString());
				}
			}
		}

		@Override
		public String translate(String s2) {
			return this.translations.get(s2);
		}

		public Map<String, String> getTranslations() {
			return this.translations;
		}

		public boolean equals(Object o2) {
			if (o2 == this) {
				return true;
			}
			if (!(o2 instanceof JsonProvider)) {
				return false;
			}
			JsonProvider other = (JsonProvider)o2;
			if (!other.canEqual(this)) {
				return false;
			}
			Map<String, String> this$translations = this.getTranslations();
			Map<String, String> other$translations = other.getTranslations();
			return !(this$translations == null ? other$translations != null : !((Object)this$translations).equals(other$translations));
		}

		protected boolean canEqual(Object other) {
			return other instanceof JsonProvider;
		}

		public int hashCode() {
			//int PRIME = 59;
			int result = 1;
			Map<String, String> $translations = this.getTranslations();
			result = result * 59 + ($translations == null ? 43 : ((Object)$translations).hashCode());
			return result;
		}

		public String toString() {
			return "TranslationRegistry.JsonProvider()";
		}
	}

	private static class ResourceBundleProvider
	implements TranslationProvider {
		private final ResourceBundle bundle;

		public ResourceBundleProvider(String bundlePath) {
			this.bundle = ResourceBundle.getBundle(bundlePath);
		}

		@Override
		public String translate(String s2) {
			return this.bundle.containsKey(s2) ? this.bundle.getString(s2) : null;
		}

		public ResourceBundle getBundle() {
			return this.bundle;
		}

		public boolean equals(Object o2) {
			if (o2 == this) {
				return true;
			}
			if (!(o2 instanceof ResourceBundleProvider)) {
				return false;
			}
			ResourceBundleProvider other = (ResourceBundleProvider)o2;
			if (!other.canEqual(this)) {
				return false;
			}
			ResourceBundle this$bundle = this.getBundle();
			ResourceBundle other$bundle = other.getBundle();
			return !(this$bundle == null ? other$bundle != null : !this$bundle.equals(other$bundle));
		}

		protected boolean canEqual(Object other) {
			return other instanceof ResourceBundleProvider;
		}

		public int hashCode() {
			//int PRIME = 59;
			int result = 1;
			ResourceBundle $bundle = this.getBundle();
			result = result * 59 + ($bundle == null ? 43 : $bundle.hashCode());
			return result;
		}

		public String toString() {
			return "TranslationRegistry.ResourceBundleProvider(bundle=" + this.getBundle() + ")";
		}
	}

	private static interface TranslationProvider {
		public String translate(String var1);
	}

}
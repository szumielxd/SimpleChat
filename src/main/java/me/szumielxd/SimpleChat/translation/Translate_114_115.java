package me.szumielxd.SimpleChat.translation;

import java.io.IOException;
import org.json.simple.parser.ParseException;

import me.szumielxd.SimpleChat.format.TranslatableItem;
import me.szumielxd.SimpleChat.utils.MainUtils.MCVersion;

public class Translate_114_115 implements Translate {
	
	
	private static Translate_114_115 translate;

	
	public static Translate_114_115 get(boolean forceReload) {
		if(translate == null || forceReload) {
			try {
				translate = new Translate_114_115();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		return translate;
	}
	
	
	private Translate_114_115() throws IOException, ParseException {
		
	}
		
	
	@Override
	public TranslatableItem downgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_15)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_14, String.format("With this function you can only translate %s to %s", MCVersion.v1_15, MCVersion.v1_14));
		return new TranslatableItem(item.getNamespace(), item.getCount(), item.getDamage(), item.getTag(), MCVersion.v1_14);
	}

	@Override
	public TranslatableItem upgrade(TranslatableItem item) {
		if(!item.getVersion().equals(MCVersion.v1_14)) throw new VersionTranslationException(item.getVersion(), MCVersion.v1_15, String.format("With this function you can only translate %s to %s", MCVersion.v1_14, MCVersion.v1_15));
		return new TranslatableItem(item.getNamespace(), item.getCount(), item.getDamage(), item.getTag(), MCVersion.v1_15);
	}
	
	
	
	

}

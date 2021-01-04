package karrus.shared;

import java.util.ArrayList;
import java.util.List;

import karrus.shared.language.Language;

public class Tools {
	
	public static List<String> getEmailTypes() {
		List<String> types = new ArrayList<String>();
		types.add(Language.alarmType);
		types.add(Language.alertType);
		types.add(Language.allString);
		return types;
	}
}

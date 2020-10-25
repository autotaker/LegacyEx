package com.legacy.action;

import com.legacy.form.UserSettingsForm;
import com.legacy.model.User;

public class UserSettingsAction {

	public void changeUserSettings(User user, UserSettingsForm form) {
		form.getLang().ifPresent(lang -> {
			user.setLang(lang);
		});
		form.getBirthday().ifPresent(birthday -> {
			user.setBirthday(birthday);
		});
	}

}

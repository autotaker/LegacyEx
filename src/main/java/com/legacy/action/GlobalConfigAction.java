package com.legacy.action;

import com.google.common.base.Strings;
import com.legacy.GlobalConfig;
import com.legacy.form.GlobalConfigEntryForm;

public class GlobalConfigAction {

	public void update(GlobalConfigEntryForm form) {
		form.getKey()
				.filter(key -> !Strings.isNullOrEmpty(key))
				.ifPresent(key -> {
					String value = form.getValue().orElse(null);
					GlobalConfig.instance().set(key, value);
				});
	}

}

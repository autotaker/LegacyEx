package com.legacy.model;

public class AnonymousUser extends User {

	public AnonymousUser() {
		super("anonymous");
	}

	@Override
	public boolean isAuthed() {
		return false;
	}


}

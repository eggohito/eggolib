package io.github.eggohito.eggolib.util;

public class Key {

	public String key = "none";
	public boolean continuous = false;

	public Key() {}

	public Key(String key) {
		this.key = key;
	}

	public Key(String key, boolean continuous) {
		this.key = key;
		this.continuous = continuous;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj
			|| (obj instanceof Key aKey && key.equals(aKey.key));
	}

}

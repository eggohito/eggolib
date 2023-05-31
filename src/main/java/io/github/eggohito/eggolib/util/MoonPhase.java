package io.github.eggohito.eggolib.util;

@SuppressWarnings("unused")
public enum MoonPhase {

	FULL_MOON(0),
	WANING_GIBBUS(1),
	LAST_QUARTER(2),
	WANING_CRESCENT(3),
	NEW_MOON(4),
	WAXING_CRESCENT(5),
	FIRST_QUARTER(6),
	WAXING_GIBBUS(7);

	private final int index;

	MoonPhase(int index) {
		this.index = index;
	}

	public boolean matches(int index) {
		return this.index == index;
	}

}

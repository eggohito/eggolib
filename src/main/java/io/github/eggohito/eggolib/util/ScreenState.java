package io.github.eggohito.eggolib.util;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unused")
public class ScreenState {

	private final String screenClassName;
	private final boolean inScreen;

	public ScreenState(boolean inScreen) {
		this(inScreen, null);
	}

	public ScreenState(boolean inScreen, @Nullable String screenClassName) {
		this.inScreen = inScreen;
		this.screenClassName = screenClassName;
	}

	@Nullable
	public String getScreenClassName() {
		return screenClassName;
	}

	public boolean isInScreen() {
		return inScreen;
	}

	public boolean inAnyOr(String... screenClassNames) {
		return inAnyOr(Arrays.asList(screenClassNames));
	}

	public boolean inAnyOr(Collection<String> screenClassNames) {
		if (screenClassName == null || (screenClassNames == null || screenClassNames.isEmpty())) {
			return inScreen;
		} else {
			return screenClassNames.contains(screenClassName);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj instanceof ScreenState ss && (Objects.equals(this.screenClassName, ss.screenClassName) && this.inScreen == ss.inScreen));
	}

	@Override
	public String toString() {
		return "ScreenState{" + inScreen + (inScreen ? ", " + (screenClassName != null ? screenClassName : "<unknown>") : "") + "}";
	}

}

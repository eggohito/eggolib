package io.github.eggohito.eggolib.util.ticker;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.DataWrapper;

import java.util.Objects;

public abstract class TickerUtil<T> {

	private boolean wasExecuted = false;

	protected int remainingTicks = 0;
	protected DataWrapper<T> dataWrapper = null;

	public final void start(T data) {

		if (dataWrapper != null && Objects.equals(dataWrapper.wrappedData(), data)) {
			return;
		}

		remainingTicks = Eggolib.config.client.syncTickRate;
		dataWrapper = new DataWrapper<>(data);
		wasExecuted = false;

	}

	public void tick() {
		if (remainingTicks > 0) {
			--remainingTicks;
		} else if (dataWrapper != null && !wasExecuted) {
			execute();
			wasExecuted = true;
		}
	}

	public void execute() {}

}

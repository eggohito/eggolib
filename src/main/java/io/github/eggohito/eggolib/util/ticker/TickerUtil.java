package io.github.eggohito.eggolib.util.ticker;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.DataWrapper;

import java.util.Objects;

public abstract class TickerUtil<T> {

	protected int remainingTicks = 0;
	protected DataWrapper<T> dataWrapper = null;

	public final void start(T data) {

		if (dataWrapper == null || !Objects.equals(dataWrapper.wrappedData(), data)) {
			remainingTicks = Eggolib.config.client.syncTickRate;
		}

		dataWrapper = new DataWrapper<>(data);

	}

	public void tick() {
		if (remainingTicks > 0) {
			--remainingTicks;
		} else if (dataWrapper != null) {
			execute();
			dataWrapper = null;
		}
	}

	public void execute() {}

}

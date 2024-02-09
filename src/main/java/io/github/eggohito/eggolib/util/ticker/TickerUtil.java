package io.github.eggohito.eggolib.util.ticker;

import io.github.eggohito.eggolib.util.DataWrapper;

import java.util.Objects;

public abstract class TickerUtil<T> {

	protected int remainingTicks = 0;
	protected DataWrapper<T> dataWrapper = null;

	public final void start(T data, int ticks) {

		if ((dataWrapper != null && Objects.deepEquals(dataWrapper.data(), data))) {
			return;
		}

		this.remainingTicks = ticks;
		this.dataWrapper = new DataWrapper<>(data);

	}

	public void tick() {
		if (remainingTicks > 0) {
			--remainingTicks;
		} else if (dataWrapper != null) {
			this.execute();
			this.dataWrapper = null;
		}

	}

	public abstract void execute();

}

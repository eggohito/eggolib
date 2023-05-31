package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A power class used for specifying priorities to other power classes.
 */
public class PrioritizedPower extends Power {

	private final int priority;

	public PrioritizedPower(PowerType<?> powerType, LivingEntity livingEntity, int priority) {
		super(powerType, livingEntity);
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	/**
	 * A generalized implementation for sorting powers based on the power's specified priority value.
	 *
	 * @param <P> the type of power classes in this map. <b>Only accepts classes that extend the {@link PrioritizedPower} class.</b>
	 */
	public static class CallInstance<P extends PrioritizedPower> {

		private final HashMap<Integer, List<P>> prioritiesAndPowersMap = new HashMap<>();
		private int minPriority = Integer.MAX_VALUE;
		private int maxPriority = Integer.MIN_VALUE;

		public int getMinPriority() {
			return minPriority;
		}

		public int getMaxPriority() {
			return maxPriority;
		}

		/**
		 * Add powers to the map and sort them based on their specified priorities.
		 *
		 * @param livingEntity the source of the power
		 * @param powerClass   the power class
		 */
		public void add(LivingEntity livingEntity, Class<P> powerClass) {
			add(livingEntity, powerClass, p -> true);
		}

		/**
		 * Add powers to the map with the specified filter and sort them based on their specified priorities.
		 *
		 * @param livingEntity the source of the power
		 * @param powerClass   the power class
		 * @param powerFilter  the filter to use for filtering the stream
		 */
		public void add(LivingEntity livingEntity, Class<P> powerClass, Predicate<P> powerFilter) {
			Stream<P> powerStream = PowerHolderComponent.getPowers(livingEntity, powerClass).stream().filter(powerFilter);
			powerStream.forEach(this::add);
		}

		private void add(P power) {

			int priority = power.getPriority();

			if (prioritiesAndPowersMap.containsKey(priority)) {
				prioritiesAndPowersMap.get(priority).add(power);
			} else {
				List<P> l = new LinkedList<>();
				l.add(power);
				prioritiesAndPowersMap.put(priority, l);
			}

			if (priority < minPriority) {
				minPriority = priority;
			}
			if (priority > maxPriority) {
				maxPriority = priority;
			}

		}

		/**
		 * Checks if the specified priority key exists in the map.
		 *
		 * @param priority an integer
		 * @return {@code true} if the specified priority key exists in the map. Otherwise, returns an empty {@link ArrayList}.
		 */
		public boolean hasPowers(int priority) {
			return prioritiesAndPowersMap.containsKey(priority);
		}

		/**
		 * Get all the power classes from the map with the specified priority key.
		 *
		 * @param priority an integer
		 * @return A {@link List} of the power classes if the priority key exists in the map. Otherwise, returns an empty {@link ArrayList}.
		 */
		public List<P> getPowers(int priority) {
			if (hasPowers(priority)) {
				return prioritiesAndPowersMap.get(priority);
			} else {
				return new LinkedList<>();
			}
		}

	}

}

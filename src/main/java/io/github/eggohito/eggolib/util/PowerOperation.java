package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.VariableIntPower;

import java.util.function.BiConsumer;

public enum PowerOperation {

	NONE(
		"",
		(targetPower, sourcePower) -> {
		}
	),
	ADD(
		"+=",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			setPowerValue(targetPower, a + b);

		}
	),
	DIVIDE(
		"/=",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			if (b == 0) {
				return;
			}

			setPowerValue(targetPower, Math.floorDiv(a, b));

		}
	),
	MAX(
		">",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			setPowerValue(targetPower, Math.max(a, b));

		}
	),
	MIN(
		"<",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			setPowerValue(targetPower, Math.min(a, b));

		}
	),
	MODULO(
		"%=",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			if (b == 0) {
				return;
			}

			setPowerValue(targetPower, Math.floorMod(a, b));

		}
	),
	MULTIPLY(
		"*=",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			setPowerValue(targetPower, a * b);

		}
	),
	SET(
		"=",
		(targetPower, sourcePower) -> {
			int a = getPowerValue(sourcePower);
			setPowerValue(targetPower, a);
		}
	),
	SUBTRACT(
		"-=",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			setPowerValue(targetPower, a - b);

		}
	),
	SWAP(
		"><",
		(targetPower, sourcePower) -> {

			int a = getPowerValue(targetPower);
			int b = getPowerValue(sourcePower);

			setPowerValue(targetPower, b);
			setPowerValue(sourcePower, a);

		}
	);

	final String operationName;
	final BiConsumer<Power, Power> operationBiConsumer;

	PowerOperation(String operationName, BiConsumer<Power, Power> operationBiConsumer) {
		this.operationName = operationName;
		this.operationBiConsumer = operationBiConsumer;
	}

	public void operate(Power targetPower, Power sourcePower) {
		operationBiConsumer.accept(targetPower, sourcePower);
	}

	public String getOperationName() {
		return operationName;
	}

	public static PowerOperation fromOperationName(String operationName) {
		return switch (operationName) {
			case "+=" -> ADD;
			case "/=" -> DIVIDE;
			case ">" -> MAX;
			case "<" -> MIN;
			case "%=" -> MODULO;
			case "*=" -> MULTIPLY;
			case "=" -> SET;
			case "-=" -> SUBTRACT;
			case "><" -> SWAP;
			default -> NONE;
		};
	}

	private static int getPowerValue(Power power) {
		if (power instanceof VariableIntPower resource) {
			return resource.getValue();
		} else if (power instanceof CooldownPower cooldown) {
			return cooldown.getRemainingTicks();
		} else {
			return 0;
		}
	}

	private static void setPowerValue(Power power, int value) {
		if (power instanceof VariableIntPower resource) {
			resource.setValue(value);
		} else if (power instanceof CooldownPower cooldown) {
			cooldown.setCooldown(value);
		}
	}

}

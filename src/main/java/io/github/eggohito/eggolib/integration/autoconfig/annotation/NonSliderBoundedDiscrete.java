package io.github.eggohito.eggolib.integration.autoconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface NonSliderBoundedDiscrete {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Integer {
		int min() default java.lang.Integer.MIN_VALUE;
		int max() default java.lang.Integer.MAX_VALUE;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Long {
		long min() default java.lang.Long.MIN_VALUE;
		long max() default java.lang.Long.MAX_VALUE;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Float {
		float min() default java.lang.Float.MIN_VALUE;
		float max() default java.lang.Float.MAX_VALUE;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Double {
		double min() default java.lang.Double.MIN_VALUE;
		double max() default java.lang.Double.MAX_VALUE;
	}

}

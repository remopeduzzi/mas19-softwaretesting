package ch.hsr.testing.unittest.weekenddiscount;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.Month;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import java.util.stream.Stream;

public class WeekendDiscountTimeSlotValidatorTest {

    @ParameterizedTest(name = "{index} => day to test:{0} => expected number Of Weekends:{1}")
    @MethodSource("createTestInputNumberOfWeekendsCalculator")
    public void testNumberOfWeekendsCalculator(LocalDateTime dayToTest, int expectedNumberOfWeekends) {
        WeekendDiscountTimeSlotValidator weekendDiscountTimeSlotValidator = new WeekendDiscountTimeSlotValidator();
        MatcherAssert.assertThat(expectedNumberOfWeekends, Matchers.is(weekendDiscountTimeSlotValidator.numberOfElligibleWeekendsOfCurrentMonth(dayToTest)));
    }

    @Tag("negative")
    @ParameterizedTest(name = "{index} => day to test:{0} => expected is elligible:{1}")
    @MethodSource("createTestInputFullDecemberThirdWeekend2020")
    public void testIsElligibleForDecember2020ThirdWeek(LocalDateTime dayToTest, boolean expectedIsElligible) {
        WeekendDiscountTimeSlotValidator weekendDiscountTimeSlotValidator = new WeekendDiscountTimeSlotValidator();
        try {
            weekendDiscountTimeSlotValidator.initializeWithWeekendNumber(3);
            MatcherAssert.assertThat(expectedIsElligible, Matchers.is(weekendDiscountTimeSlotValidator.isAuthorizedForDiscount(dayToTest)));
        } catch (IllegalWeekendNumberException e) {
            e.printStackTrace();
        }
    }

    @Tag("negative")
    @Test
    public void testIllegalWeekendExceptionThrows() {
        LocalDateTime dayToTest = LocalDateTime.of(2020, Month.NOVEMBER, 3, 0,0);
        // 0 is not valid since ther is no "0th" Weekend, you start counting at 1st
        test()
                .setWeekendOfMonth(0)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .setShouldThrow(true)
                .run();

        // 7 is illegal, November has only 4 Elligible Weekends
        test()
                .setWeekendOfMonth(7)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .setShouldThrow(true)
                .run();

        // Null is not valid either
        test()
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .setShouldThrow(true)
                .run();
    }

    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputFirstFullWeekendsOfMonths2020")
    public void testForFirstWeekendsOfMonth(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(1)
                .setDayToTest(dayToTest)
                .setExpectedResult(true)
                .run();
    }

    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputSecondWeekendsOfMonths2020")
    public void testForSecondWeekendsOfMonth(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(2)
                .setDayToTest(dayToTest)
                .setExpectedResult(true)
                .run();
    }

    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputThirdWeekendsOfMonths2020")
    public void testForThirdWeekendsOfMonth(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(3)
                .setDayToTest(dayToTest)
                .setExpectedResult(true)
                .run();
    }

    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputFourthWeekendsOfMonths2020")
    public void testForFourthWeekendsOfMonth(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(4)
                .setDayToTest(dayToTest)
                .setExpectedResult(true)
                .run();
    }

    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputFifthFullWeekendsOfMonths2020")
    public void testForFifthWeekendsOfMonth(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(5)
                .setDayToTest(dayToTest)
                .setExpectedResult(true)
                .run();
    }

    @Tag("negative")
    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputFirstNotFullWeekends2020")
    public void testForFirstBrokenWeekendsOfMonthReturnFalse(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(1)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .run();
    }

    @Tag("negative")
    @ParameterizedTest(name = "{index} => day to test:{0}")
    @MethodSource("createTestInputSomeWeekdays2020")
    public void testForWeekdaysInMonthReturnsFalse(LocalDateTime dayToTest) {
        test()
                .setWeekendOfMonth(1)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .run();
        test()
                .setWeekendOfMonth(2)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .run();
        test()
                .setWeekendOfMonth(3)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .run();
        test()
                .setWeekendOfMonth(4)
                .setDayToTest(dayToTest)
                .setExpectedResult(false)
                .run();
    }

    private WeekendDiscountTimeSlotValidatorTest.TestBuilder test() {
        return new WeekendDiscountTimeSlotValidatorTest.TestBuilder();
    }


    //Using Builder pattern to ensure independent Tests
    private static class TestBuilder {

        private final WeekendDiscountTimeSlotValidator weekendDiscountTimeSlotValidator;
        private LocalDateTime localDateTimeToTest;
        private boolean shouldThrowException = false;
        private boolean expectedResult = true;

        private TestBuilder() {
            weekendDiscountTimeSlotValidator = new WeekendDiscountTimeSlotValidator();
        }

        private WeekendDiscountTimeSlotValidatorTest.TestBuilder setWeekendOfMonth(int weekendOfMonth) {
            weekendDiscountTimeSlotValidator.initializeWithWeekendNumber(weekendOfMonth);
            return this;
        }

        private WeekendDiscountTimeSlotValidatorTest.TestBuilder setExpectedResult(boolean expectedResult) {
            this.expectedResult = expectedResult;
            return this;
        }

        private WeekendDiscountTimeSlotValidatorTest.TestBuilder setShouldThrow(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
            return this;
        }

        private WeekendDiscountTimeSlotValidatorTest.TestBuilder setDayToTest(LocalDateTime localDateTimeToTest) {
            this.localDateTimeToTest = localDateTimeToTest;
            return this;
        }

        private void run() {
            try {
                boolean isElligibleForDiscount = weekendDiscountTimeSlotValidator.isAuthorizedForDiscount(localDateTimeToTest);
                MatcherAssert.assertThat(weekendDiscountTimeSlotValidator.isAuthorizedForDiscount(localDateTimeToTest), Matchers.is(expectedResult));
                MatcherAssert.assertThat(false, Matchers.is(shouldThrowException));
            } catch (IllegalWeekendNumberException e) {
                MatcherAssert.assertThat(true, Matchers.is(shouldThrowException));
            }
        }
    }

    private static Stream<Arguments> createTestInputNumberOfWeekendsCalculator() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 3, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 3, 0, 0), 5),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 30, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 23, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 20, 0, 0), 5),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 21, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 21, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 21, 0, 0), 5),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 21, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 21, 0, 0), 5),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 21, 0, 0), 4),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 21, 0, 0), 4)
        );
    }

    private static Stream<Arguments> createTestInputFirstFullWeekendsOfMonths2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 4, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 5, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 2, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 7, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 8, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 4, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 5, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 2, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 3, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 6, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 7, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 4, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 5, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 1, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 2, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 5, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 6, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 3, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 4, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 7, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 8, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 5, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 6, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputSecondWeekendsOfMonths2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 11, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 12, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 8, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 9, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 14, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 15, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 11, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 12, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 9, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 10, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 13, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 14, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 11, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 12, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 8, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 9, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 12, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 13, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 10, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 11, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 14, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 15, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 12, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 13, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputThirdWeekendsOfMonths2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 18, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 19, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 15, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 16, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 21, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 22, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 18, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 19, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 16, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 17, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 20, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 21, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 18, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 19, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 15, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 16, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 19, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 20, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 17, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 18, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 21, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 22, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 19, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 20, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputFourthWeekendsOfMonths2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 25, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JANUARY, 26, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 22, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.FEBRUARY, 23, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 28, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 29, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 25, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.APRIL, 26, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 23, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 24, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 27, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 28, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 25, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 26, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 22, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 23, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 26, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.SEPTEMBER, 27, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 24, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.OCTOBER, 25, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 28, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 29, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 26, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 27, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputFifthFullWeekendsOfMonths2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 30, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.MAY, 31, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 29, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.AUGUST, 30, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputFirstNotFullWeekends2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 1, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 1, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputSomeWeekdays2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.MARCH, 3, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.NOVEMBER, 3, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 30, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JUNE, 23, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 20, 0, 0)),
                Arguments.of(LocalDateTime.of(2020, Month.JULY, 21, 0, 0))
        );
    }

    private static Stream<Arguments> createTestInputFullDecemberThirdWeekend2020() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 1, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 2, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 3, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 4, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 5, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 6, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 7, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 8, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 9, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 10, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 11, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 12, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 13, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 14, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 15, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 16, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 17, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 18, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 19, 0, 0), true),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 20, 0, 0), true),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 21, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 22, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 23, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 24, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 25, 1, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 26, 1, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 27, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 28, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 29, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 30, 0, 0), false),
                Arguments.of(LocalDateTime.of(2020, Month.DECEMBER, 31, 0, 0), false)
        );
    }
}
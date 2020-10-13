package ch.hsr.testing.unittest.weekenddiscount;

/*
 * HSR Hochschule für Technik Rapperswil
 * Master of Advanced Studies in Software Engineering
 * Module Software Testing
 *
 * Thomas Briner, thomas.briner@gmail.com
 */

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class WeekendDiscountTimeSlotValidator {

    public static final List<DayOfWeek> WEEKEND_DAYS = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    public static final int NOF_WEEKDAYS = 7;

    private Integer weekendNumber;

    public void initializeWithWeekendNumber(int weekendNumber) {
        this.weekendNumber = weekendNumber;
    }

    /**
     * Checks whether a date is within the nth weekend (Saturday 00:00 to Sunday
     * 23:59) of the month. The number n has to be given to the instance beforehand
     * using the initializeWithWeekendNumber Method.
     *
     * @param now the point in time for which the decision should be made whether weekend discount is applied or not
     * @return
     * @throws IllegalWeekendNumberException
     *             if weekend number is not set
     *             or if the weekend number is higher than the number of weekends in this month
     */
    public boolean isAuthorizedForDiscount(LocalDateTime now) throws IllegalWeekendNumberException {
        // make sure a weekend number has been set already
        //CORRECTION: it should throw if the weekendNumber is less than 1 or higher than the weekends in current month!
        if (this.weekendNumber == null || this.weekendNumber < 1 || this.weekendNumber > numberOfElligibleWeekendsOfCurrentMonth(now) ) {
            throw new IllegalWeekendNumberException("WeekendDiscountTimeSlotValidator has not been initialized correctly!");
        } else {

            if (WEEKEND_DAYS.contains(now.getDayOfWeek())) {
                Integer firstSaturdayInMonth = null;
                for (int i = 1; i < now.getMonth().maxLength() && firstSaturdayInMonth == null; i++) {
                    if (LocalDate.of(now.getYear(), now.getMonth(), i).getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                        firstSaturdayInMonth = i;
                    }
                }
                LocalDate beginningOfDiscountWeekend = LocalDate.of(
                        now.getYear(),
                        now.getMonth(),
                        firstSaturdayInMonth + (weekendNumber - 1) * NOF_WEEKDAYS);
                //CORRECTION: Condition was wrong, now assures only the specified weekend is elligible and checked
                if (now.getDayOfMonth() >= beginningOfDiscountWeekend.getDayOfMonth() &&
                        now.getDayOfMonth() <= beginningOfDiscountWeekend.getDayOfMonth() + 1) {
                    return true;
                }
            }
            return false;
        }
    }

    //ADDITION: Number of Saturdays equals number of elligible Weekends, even if weekend consists of 1 day only (last day of month)
    //The weekend MUST start with a Saturday in a Month to be elligible (if the 1st was a Sunday, thats NOT elligible)
    public int numberOfElligibleWeekendsOfCurrentMonth(LocalDateTime now) {
        Integer numberOfElligibleWeekends = 0;
        for (int i = 1; i < now.getMonth().maxLength() + 1; i++) {
            if (LocalDate.of(now.getYear(), now.getMonth(), i).getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                numberOfElligibleWeekends += 1;
            }
        }
        return numberOfElligibleWeekends;
    }
}

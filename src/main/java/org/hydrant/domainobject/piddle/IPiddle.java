package org.hydrant.domainobject.piddle;

/**
 * Created by asaxena on 2/19/17.
 */
public interface IPiddle {
    long WORK_CAPACITY_MILLS = 5 * 60 * 1000;

    /**
     * Method used to check if piddle is available to test.
     * @return boolean.
     */
    boolean isAvailable();

    /**
     * Method called on piddle to start the test.
     * @return boolean.
     */
    boolean startTest();

    /**
     * Helper method used by unit tests to
     * count the number of task currently
     * under execution by piddle.
     * Ideally it will be 1 per piddle
     * @return int
     */
    int getCurrentTaskUnderExecutionCount();
}

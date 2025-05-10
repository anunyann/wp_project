package mk.ukim.finki.akreditacii.model;

public enum SubjectDependencyType {
    /**
     * <b>SubjectDependencies.dependencies</b> is a
     * <b>number</b> representing minimum credits that should be obtained by the student followed by <b>ECTS</b>.
     * <p>
     * Example valid values:
     * "MINIMUM_CREDITS:180ECTS"
     */
    MINIMUM_CREDITS,

    /**
     * <b>SubjectDependencies.dependencies</b> is a comma separated string of subject codes.
     * The subject is allowed if at least one of the subjects is passed by the student. No spaces allowed.
     * <p>
     * Example valid values:
     * "ANY_OF_SELECTED:F23L1S001;F23L1S002"
     */
    ANY_OF_SELECTED,

    /**
     * <b>SubjectDependencies.dependencies</b> is a comma separated string of subject codes.
     * The subject is allowed if all of the subjects are passed by the student. No spaces allowed.
     * <p>
     * Example valid values:
     * "ALL_OF_SELECTED:F23L1S001;F23L1S002"
     */
    ALL_OF_SELECTED,

    /**
     * <b>SubjectDependencies.dependencies</b> is a custom bool expression composed of:
     *  - Subject code
     *  - && (and)
     *  - || (or)
     *  - ~ (not)
     *
     *  Example:
     *  "BOOLEAN_EXPRESSION:~F23L2S001 && (F23L2S002 || F23L2S002)"
     */
    BOOLEAN_EXPRESSION
}
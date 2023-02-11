import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PolicyRequest {

    static final String INCOMPLETE = "INCOMPLETE";
    static final String NEW = "NEW";
    static final String IN_REVIEW = "IN_REVIEW";
    static final String READY_FOR_TRANSMISSION = "READY_FOR_TRANSMISSION";
    static final String TRANSMITTED = "TRANSMITTED";
    static final String DUPLICATE_FAKE = "DUPLICATE_FAKE";
    static final String SUCCESSFUL = "SUCCESSFUL";
    static final String INACTIVE = "INACTIVE";
    static final String CANCELED = "CANCELED";
    static final String NOT_SUCCESSFUL = "NOT_SUCCESSFUL";
    static final String ERROR = "ERROR";

    private String state;

    private String IBAN;

    public PolicyRequest(String state) {
        this.state = state;
    }

    public String getLeadState() {
        return state;
    }

    public Set<String> getPossibleNextStates() {

        if(checkIBAN())
            return getPossibleNextStates(state);

        return new HashSet<>(List.of(ERROR));
    }

    public boolean transitionState(String to) {

        if(getPossibleNextStates().contains(to)) {
            state = to;

            return true;
        }

        return false;
    }

    private Set<String> getPossibleNextStates(String state) {

        switch (state) {
            case INCOMPLETE -> {
                return new HashSet<>(Arrays.asList(NEW, DUPLICATE_FAKE, ERROR));
            }
            case NEW -> {
                return new HashSet<>(Arrays.asList(IN_REVIEW, DUPLICATE_FAKE, ERROR));
            }
            case IN_REVIEW -> {
                return new HashSet<>(Arrays.asList(READY_FOR_TRANSMISSION, DUPLICATE_FAKE, ERROR));
            }
            case READY_FOR_TRANSMISSION -> {
                return new HashSet<>(Arrays.asList(TRANSMITTED, ERROR));
            }
            case TRANSMITTED -> {
                return new HashSet<>(Arrays.asList(SUCCESSFUL, NOT_SUCCESSFUL, ERROR));
            }
            case SUCCESSFUL -> {
                return new HashSet<>(Arrays.asList(INACTIVE, CANCELED, ERROR));
            }
            default -> {
                return new HashSet<>(List.of(ERROR));
            }
        }
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getIBAN() {
        return IBAN;
    }

    private boolean checkIBAN() {
        StringBuilder tempIBAN = new StringBuilder();

        String IBAN = getIBAN();

        try {
            if(IBAN.isEmpty()) return true;
        } catch (Exception e) {
            return true;
        }

        for (int i = 4; i < IBAN.length() + 4; i++) {

            char letter;

            if(i < IBAN.length())
                letter = IBAN.charAt(i);
            else
                letter = IBAN.charAt(i - IBAN.length());

            if(Character.getNumericValue(letter) > 9)
                tempIBAN.append((Character.getNumericValue(letter)));
            else
                tempIBAN.append(letter);
        }

        try {
            BigInteger number = new BigInteger(String.valueOf(tempIBAN));

            return number.mod(BigInteger.valueOf(97)).equals(BigInteger.valueOf(1));
        } catch (Exception e) {
            return false;
        }
    }
}

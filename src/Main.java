public class Main {
    public static void main(String[] args) {

        PolicyRequest policyRequest = new PolicyRequest(PolicyRequest.INCOMPLETE);

        policyRequest.setIBAN("");

        System.out.println(policyRequest.getPossibleNextStates());
    }
}
import java.rmi.Naming;

public class AtmClient {

    public static void main (String[] args) {
        try {
            String target = (args.length == 0) ? "Ireland" : args[0];
            BankServerInterface bankServer = (BankServerInterface) Naming.lookup("//localhost/Bank");

            String access = bankServer.login("atm12", "hashed_password");
            System.out.println(access);
        }
        catch (Exception e) {}
    }
}

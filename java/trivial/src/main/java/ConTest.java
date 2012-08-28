import java.io.*;
import java.util.Date;

public class ConTest
{
    public static void main(String args[])
    {
        Console cons = System.console();
        String username = cons.readLine("User name: ");
        char[] passwd = cons.readPassword("Password: ");
        
        System.out.println("Hi " + username + ", your password is: " + new String(passwd));
        
        System.out.printf("now is: %tc", new Date());
        
        return;
    }
}
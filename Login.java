package uta.cse.cse3310.webchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.UUID;

public class Login 
{
    String username;
    String password;

public Login(String username, String password)
{
    this.username = username;
    this.password = password;
}

public boolean equals(Object o)
{
    if(this == o)
    {
        return true;
    }
    if((o==null) || !(o instanceof Login))
    {
        return false;
    }

    Login l = (Login) o;
    return password.equals(l.password);
}

public boolean VerifyLogin()
{
    String currentLine;
    
    try {
        FileReader fp = new FileReader("users.txt");
        try (BufferedReader br = new BufferedReader(fp)) {
            //throw away the first line (number of clients)
            br.readLine();

            //continue reading the file
            while((currentLine = br.readLine()) != null)
            {
                String data[] = currentLine.split(",");
                if(data[2].equals(this.username) && data[3].equals(this.password))
                {
                    return true;
                } else {
                    System.out.println(data[2] + "!=" + this.username);
                    System.out.println(data[3] + "!=" + this.password);
                }
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }
    
    return false;    
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}



public boolean SignUp(String newUsername, String newPassword) {
    // Check if the username is already taken
    if (isUsernameTaken(newUsername)) {
        System.out.println("Username is already taken!");
        return false;
    }

    // Generate a new UUID for the user
    UUID uuid = UUID.randomUUID();

    // Get the next available index for the user
    int index = getNextIndex();

    // Write the new user's information to the file
    String userLine = index + "," + uuid.toString() + "," + newUsername + "," + newPassword;
    File file = new File("users.txt");

    try (FileWriter fw = new FileWriter(file, true);
         BufferedWriter bw = new BufferedWriter(fw);
         RandomAccessFile raf = new RandomAccessFile(file, "r")) {

        // Check if the file ends with a newline character
        long length = raf.length();
        if (length > 0) {
            raf.seek(length - 1);
            byte lastByte = raf.readByte();
            if (lastByte != '\n' && lastByte != '\r') {
                // If the file doesn't end with a newline, add one
                bw.newLine();
            }
        }

        // Write the user information
        bw.write(userLine);

        System.out.println("Sign up successful!");
        return true;
    } catch (IOException e) {
        System.out.println("An error occurred while signing up: " + e.getMessage());
        return false;
    }
}


private boolean isUsernameTaken(String username) {
    String currentLine;

    try (FileReader fp = new FileReader("users.txt");
         BufferedReader br = new BufferedReader(fp)) {
        
        while ((currentLine = br.readLine()) != null) {
            String data[] = currentLine.split(",");
            // Assuming the username is in the third position (index 2)
            if (data[2].equals(username)) {
                return true;
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }

    return false;
}

private int getNextIndex() {
    int index = -1;

    try (Scanner scanner = new Scanner(new File("users.txt"))) {
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            if (!currentLine.trim().isEmpty()) {
                String data[] = currentLine.split(",");
                index = Math.max(index, Integer.parseInt(data[0]));
            }
        }
    } catch (Exception e) {
        System.out.println(e);
    }

    return index + 1;
}

}
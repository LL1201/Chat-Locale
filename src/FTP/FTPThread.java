// Loner Luca
// 5B IA
// 27/12/2021
package FTP;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FTPThread implements Runnable {
    private Socket cmd = null;
    private Socket data = null;
    private ServerSocket wsdata = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String str = null;
    private String[] cmdstr;
    private String currentPath = "/";
    public static boolean clientClose = true;
    private boolean anonymousLogged = false;
    private boolean stdUserLogged = false;
    private String userName = "";
    private String fileToBeRenamed = "";

    public FTPThread(Socket cmd) {
        this.cmd = cmd;
    }

    public void run() {
        try {
            cmd.setSoTimeout(4500);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        try {
            in = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
            out = new PrintWriter(cmd.getOutputStream(), true);
            out.println("220 Loner FTP Service");
            System.out.println("Connected host: " + cmd.getInetAddress() + cmd.getPort());
            GUI.textAreaLog.append(new SimpleDateFormat("hh:mm").format(new Date()) + " " + "Connected host: "
                    + cmd.getInetAddress() + cmd.getPort() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to get I/O stream on socket!");
        }

        while (clientClose) {
            try {
                if (cmd.isConnected())
                    str = in.readLine();
                else
                    str = null;
            } catch (Exception e) {
                continue;
            }

            if (str != null) {
                System.out.println("Remote host: " + cmd.getInetAddress() + ": " + str); // debug
                GUI.textAreaLog.append(new SimpleDateFormat("hh:mm").format(new Date()) + " "
                        + "Remote host: " + cmd.getInetAddress() + ": " + str + "\n");

                cmdstr = str.split("\\s+");
                if (cmdstr.length > 1) {
                    switch (cmdstr[0].toUpperCase()) {
                        case "RNTO":
                            Rnto(cmdstr[1]);
                            break;
                        case "RNFR":
                            Rnfr(cmdstr[1]);
                            break;
                        case "PORT":
                            Port(cmdstr);
                            break;
                        case "OPTS":
                            out.println("502 Command not implemented.");
                            /*
                             * if (cmdstr[1].toUpperCase().equals("UTF8"))
                             * out.println("200 OPTS UTF8 command successful - UTF8 encoding now ON.");
                             */
                            break;
                        case "AUTH":
                            if (cmdstr[1].toUpperCase().equals("TLS"))
                                out.println("534 Local policy on server does not allow TLS secure connections.");
                            else if (cmdstr[1].toUpperCase().equals("SSL"))
                                out.println("534 Local policy on server does not allow TLS secure connections.");
                            else
                                out.println("502 Command not implemented.");
                            break;
                        case "USER":
                            User(cmdstr[1]);
                            break;
                        case "PASS":
                            Pass(cmdstr[1]);
                            break;
                        case "TYPE":
                            if (cmdstr[1].toUpperCase().equals("I") || cmdstr[1].toUpperCase().equals("A"))
                                out.println("230 Command OK.");
                            else
                                out.println("500 Command not understood.");
                            break;
                        case "LIST":
                            if (anonymousLogged || stdUserLogged)
                                List(cmdstr);
                            else
                                out.println("530 Please login with USER and PASS.");
                            break;
                        case "RETR":
                            Retr(cmdstr[1]);
                            break;
                        case "CWD":
                            Cwd(cmdstr[1]);
                            break;
                        case "STOR":
                            Stor(cmdstr[1]);
                            break;
                        case "MLSD":
                            out.println("502 Command not implemented.");
                            break;
                        case "MLST":
                            out.println("502 Command not implemented.");
                            break;
                        case "MKD":
                            Mkd(cmdstr[1]);
                            break;
                        case "RMD":
                            Rmd(cmdstr[1]);
                            break;
                        case "DELE":
                            Dele(cmdstr[1]);
                            break;
                        default:
                            out.println("500 Command not understood.");
                    }
                } else
                    switch (str.toUpperCase()) {
                        case "SYST":
                            out.println("215 Windows");
                            break;
                        case "FEAT":
                            out.println("500 Syntax error, command unrecognized.");
                            break;
                        case "EPSV":
                            Epsv();
                            break;
                        case "PWD":
                            Pwd();
                            break;
                        case "LIST":
                            List(cmdstr);
                            break;
                        case "PASV":
                            Pasv();
                            // out.println("502 Command not implemented.");
                            break;
                        case "QUIT":
                            QuitCmdConnection();
                            break;
                        case "CDUP":
                            Cdup();
                            break;
                        default:
                            out.println("500 Command not understood.");
                    }
            } else {
                QuitCmdConnection();
                break;
            }
        }
        QuitCmdConnection();
        System.out.println("Client is down");
    }

    private void List(String[] cmdstr) {
        // String path = "drwxrwxrwx 1 owner group 0 Dec 9 15:23 pub \r\n -rw-r--r-- 1
        // owner group 213 Aug 26 16:31 README";
        if (anonymousLogged || stdUserLogged) {
            out.println("150 Data connection is ready for ASCII.");

            String path = "";
            if (cmdstr.length > 2) {
                for (int i = 1; i < cmdstr.length; i++) {
                    if (cmdstr[i].charAt(0) == '/')
                        path = Server.ftpPath + cmdstr[i];
                    else
                        path = Server.ftpPath + "/" + cmdstr[i];

                    if (new File(path).exists())
                        continue;
                    else if (i == cmdstr.length - 1)
                        path = Server.ftpPath + currentPath;
                }
            } else {
                path = Server.ftpPath + currentPath;
            }

            // System.out.println(path);
            File folder = new File(path);
            // System.out.println(folder.getAbsolutePath()); debug
            String list = "";

            for (File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile())
                    list += "-";
                else
                    list += "d";

                if (fileEntry.canRead())
                    list += "r";
                else
                    list += "-";

                if (fileEntry.canWrite())
                    list += "w";
                else
                    list += "-";

                if (fileEntry.canExecute())
                    list += "x";
                else
                    list += "-";

                list += "------   1 owner    group             " + fileEntry.length() + " "
                        + new SimpleDateFormat("MMM  dd  yyyy").format(new Date(fileEntry.lastModified()))
                        + " "
                        + fileEntry.getName() + "\r\n";
            }

            try {
                // new PrintWriter(data.getOutputStream(), true).println(list);
                data.getOutputStream().write(list.getBytes());
                // new DataOutputStream(data.getOutputStream()).writeBytes(path);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Unable to get I/O stream on socket!");
            }

            try {
                data.close();
                System.out.println("Data connection socket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println("226 Transfer complete.");
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void Retr(String fileName) {
        if (anonymousLogged || stdUserLogged) {
            out.println("150 Data connection already open; transfer starting.");
            String path = "";
            if (currentPath.equals("/"))
                path = Server.ftpPath + currentPath + fileName;
            else
                path = Server.ftpPath + currentPath + "/" + fileName;

            if (new File(path).exists()) {

                FileInputStream inputStream = null;
                BufferedInputStream bInputStream = null;
                OutputStream outputStream = null;
                // File file = new File("file" + currentPath + cmdstr[1]);
                File file = new File(path);
                byte[] bytes = new byte[(int) file.length()];

                try {
                    inputStream = new FileInputStream(file);
                    bInputStream = new BufferedInputStream(inputStream);
                    bInputStream.read(bytes, 0, bytes.length);
                    outputStream = data.getOutputStream();
                    outputStream.write(bytes, 0, bytes.length);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Transfer problems!");
                }

                try {
                    data.close();
                    bInputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println("226 Transfer complete. Closing data connection.");
            } else
                out.println("550 The system cannot find the file specified.");
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void Pasv() {
        if (anonymousLogged || stdUserLogged) {
            int upper = (int) (Math.random() * (190) + 4);
            int lower = (int) (Math.random() * (511) + 1);
            out.println("227 Entering Passive Mode (127,0,0,1," + upper + "," + lower + ")");

            try {
                wsdata = new ServerSocket(((upper * 256) + lower));
                data = wsdata.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Welcoming socket error!");
            }
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void QuitCmdConnection() {
        // clientClose = false;
        try {
            in.close();
            out.close();
            cmd.close();
            System.out.println("Disconnected host: " + cmd.getInetAddress());
            GUI.textAreaLog.append("Disconnected host: " + cmd.getInetAddress());
        } catch (IOException e) {

        }
    }

    private void Epsv() {
        if (anonymousLogged || stdUserLogged) {
            if (Server.epsvPort == 49151)
                Server.epsvPort = 1024;
            else
                Server.epsvPort++;

            out.println("229 Entering Extended Passive Mode (|||" + Server.epsvPort + "|).");
            try {
                wsdata = new ServerSocket(Server.epsvPort);
                data = wsdata.accept();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Welcoming socket error!");
            }
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void Stor(String fileName) {
        if (stdUserLogged) {
            out.println("150 Data connection already open; transfer starting.");
            String path = "";

            if (currentPath.equals("/"))
                path = Server.ftpPath + currentPath + fileName;
            else
                path = Server.ftpPath + currentPath + "/" + fileName;

            FileOutputStream fOutputStream = null;
            BufferedOutputStream bOutputStream = null;
            InputStream inputStream = null;
            int current = 0;
            int bytesRead;
            // byte[] bytes = new byte[602238689];
            byte[] bytes = new byte[602238689];

            try {
                inputStream = data.getInputStream();
                fOutputStream = new FileOutputStream(path);
                bOutputStream = new BufferedOutputStream(fOutputStream);
                bytesRead = inputStream.read(bytes, 0, bytes.length);
                if (bytesRead != -1) {

                    current = bytesRead;

                    do {
                        bytesRead = inputStream.read(bytes, current, (bytes.length - current));
                        if (bytesRead >= 0)
                            current += bytesRead;
                    } while (bytesRead > -1);

                    bOutputStream.write(bytes, 0, current);
                    bOutputStream.flush();
                }

            } catch (Exception e) {
                System.out.println("Transfer problems!");
            }

            try {
                data.close();
                bOutputStream.close();
                fOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println("226 Transfer complete. Closing data connection.");
        } else if (anonymousLogged)
            out.println("550 Access is denied.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private void Cdup() {
        if (anonymousLogged || stdUserLogged) {
            if (!currentPath.equals("/"))
                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));

            if (currentPath.equals(""))
                currentPath = "/";
            out.println("250 CDUP command successful.");
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void Cwd(String str) {
        // current path inizia SEMPRE con "/" e termina senza nulla
        if (anonymousLogged || stdUserLogged) {
            if (str.charAt(0) == '/') {
                if (str.equals("/")) {
                    currentPath = "/";
                    out.println("250 CWD command successful.");
                } else if (new File(Server.ftpPath + str).exists()) {
                    if (str.charAt(str.length() - 1) == '/') {
                        currentPath = str.substring(0, str.length() - 1);
                    } else
                        currentPath = str;
                    out.println("250 CWD command successful.");
                } else
                    out.println("550 path does not exist.");
            } else if (new File(Server.ftpPath + currentPath + "/" + str).exists()) {
                if (str.charAt(str.length() - 1) == '/') {
                    if (currentPath.equals("/"))
                        currentPath += str.substring(0, str.length() - 1);
                    else
                        currentPath += "/" + str.substring(0, str.length() - 1);
                } else
                    currentPath += "/" + str;
                out.println("250 CWD command successful.");
            } else
                out.println("550 path does not exist.");
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void Pwd() {
        if (anonymousLogged || stdUserLogged)
            out.println("257 \"" + currentPath + "\" is current directory.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private void User(String user) {
        if (user.equalsIgnoreCase("anonymous")) {
            out.println("331 Anonymous access allowed");
            userName = "anonymous";
        } else {
            out.println("331 Password required.");
            userName = user;
        }
    }

    private void Pass(String pass) {
        if (userName.equals(""))
            out.println("503 Login with USER first.");
        else if (userName.equals("anonymous")) {
            anonymousLogged = true;
            out.println("230 User logged in");
        } else if (CheckUserPassword(userName, pass)) {
            out.println("230 User logged in");
            stdUserLogged = true;
        } else
            out.println("530 User cannot log in.");
    }

    private void Mkd(String name) {
        if (stdUserLogged) {
            new File(Server.ftpPath + currentPath + "/" + name).mkdir();
            out.println("257 Folder created.");
        } else if (anonymousLogged)
            out.println("550 Access is denied.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private void Rmd(String name) {
        if (stdUserLogged) {
            new File(Server.ftpPath + currentPath + "/" + name).delete();
            out.println("250 Folder deleted.");
        } else if (anonymousLogged)
            out.println("550 Access is denied.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private void Dele(String name) {
        if (stdUserLogged) {
            new File(Server.ftpPath + currentPath + "/" + name).delete();
            out.println("250 File deleted.");
        } else if (anonymousLogged)
            out.println("550 Access is denied.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private void Port(String[] cmdstr) {
        if (anonymousLogged || stdUserLogged) {
            String[] str = cmdstr[1].replaceAll("[^\\d]", " ").trim().replaceAll(" +", " ").split("\\s+");

            try {
                out.println("200 PORT command successfull.");
                data = new Socket(str[0] + "." + str[1] + "." + str[2] + "." + str[3],
                        (Integer.parseInt(str[4]) * 256) + Integer.parseInt(str[5]), null, 20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            out.println("530 Please login with USER and PASS.");
    }

    private void Rnfr(String fileName) {
        if (stdUserLogged) {
            if (fileName != null) {
                fileToBeRenamed = fileName;
                out.println("350 RNFR accepted. Please supply new name for RNTO.");
            }
        } else if (anonymousLogged)
            out.println("550 Access is denied.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private void Rnto(String name) {
        if (stdUserLogged) {
            File file = new File(Server.ftpPath + currentPath + "/" + fileToBeRenamed);
            File rename = new File(Server.ftpPath + currentPath + "/" + name);
            boolean b = file.renameTo(rename);
            if (b) {
                out.println("250 File renamed.");
                fileToBeRenamed = "";
            }
        } else if (anonymousLogged)
            out.println("550 Access is denied.");
        else
            out.println("530 Please login with USER and PASS.");
    }

    private boolean CheckUserPassword(String name, String password) {
        for (Utente u : Server.lstUtenti) {
            if (u.name.equals(name) && u.password.equals(password)) {
                return true;
            } else
                return false;
        }
        return false;
    }
}
